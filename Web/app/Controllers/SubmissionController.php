<?php
declare(strict_types=1);

namespace Horizonte\Controllers;

use Horizonte\Core\CsrfToken;
use Horizonte\Repositories\SubmissionRepository;
use Throwable;

final class SubmissionController
{
    private const EVENT_TYPES = ['Casamiento', 'Cumpleaños de 15', 'Cumpleaños infantil', 'Evento corporativo', 'Aniversario', 'Bautismo', 'Reunión social', 'Otro'];
    private const TIMES = ['', 'A definir', 'Día', 'Tarde', 'Noche'];
    private const SERVICES = ['Catering', 'DJ y música', 'Fotografía y video', 'Decoración', 'Barra', 'Show o animación'];

    public function __construct(private readonly SubmissionRepository $submissions = new SubmissionRepository())
    {
    }

    public function handle(): never
    {
        header('Content-Type: application/json; charset=utf-8');
        header('X-Content-Type-Options: nosniff');
        header('Cache-Control: no-store');
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            header('Allow: POST');
            $this->respond(405, ['ok' => false, 'message' => 'Método no permitido.']);
        }
        $contentType = strtolower(trim(explode(';', $_SERVER['CONTENT_TYPE'] ?? '')[0]));
        if ($contentType !== 'application/json') {
            $this->respond(415, ['ok' => false, 'message' => 'Formato de solicitud no válido.']);
        }
        $raw = file_get_contents('php://input');
        if ($raw === false || strlen($raw) > 20000) {
            $this->respond(413, ['ok' => false, 'message' => 'La solicitud supera el tamaño permitido.']);
        }
        $data = json_decode($raw, true);
        if (!is_array($data)) {
            $this->respond(400, ['ok' => false, 'message' => 'No pudimos leer los datos enviados.']);
        }
        $token = is_string($data['csrf_token'] ?? null) ? $data['csrf_token'] : null;
        if (!CsrfToken::isValid($token)) {
            $this->respond(403, ['ok' => false, 'message' => 'La sesión venció. Actualizá la página e intentá nuevamente.']);
        }

        $submission = [
            'tipo_evento' => $this->clean($data['tipo_evento'] ?? '', 80),
            'nombre' => $this->clean($data['nombre'] ?? '', 80),
            'apellido' => $this->clean($data['apellido'] ?? '', 80),
            'email' => $this->clean($data['email'] ?? '', 160),
            'telefono' => $this->clean($data['telefono'] ?? '', 40),
            'fecha_tentativa' => $this->clean($data['fecha_tentativa'] ?? '', 10),
            'horario' => $this->clean($data['horario'] ?? '', 20),
            'espacio_preferido' => $this->clean($data['espacio_preferido'] ?? '', 120),
            'comentarios' => $this->clean($data['comentarios'] ?? '', 1500),
        ];
        $guests = filter_var($data['invitados'] ?? null, FILTER_VALIDATE_INT);
        $services = $data['servicios'] ?? [];
        if (!in_array($submission['tipo_evento'], self::EVENT_TYPES, true) || $submission['nombre'] === ''
            || $submission['apellido'] === '' || !filter_var($submission['email'], FILTER_VALIDATE_EMAIL)) {
            $this->respond(422, ['ok' => false, 'message' => 'Revisá el tipo de evento y tus datos de contacto.']);
        }
        if ($guests === false || $guests < 1 || $guests > 5000) {
            $this->respond(422, ['ok' => false, 'message' => 'Ingresá una cantidad válida de invitados.']);
        }
        $date = $submission['fecha_tentativa'];
        if ($date !== '' && (!preg_match('/^\d{4}-\d{2}-\d{2}$/', $date)
            || !checkdate((int) substr($date, 5, 2), (int) substr($date, 8, 2), (int) substr($date, 0, 4)))) {
            $this->respond(422, ['ok' => false, 'message' => 'La fecha indicada no es válida.']);
        }
        if ($date !== '' && $date < date('Y-m-d')) {
            $this->respond(422, ['ok' => false, 'message' => 'Elegí una fecha tentativa a partir de hoy.']);
        }
        if (!in_array($submission['horario'], self::TIMES, true) || !is_array($services) || count($services) > 12) {
            $this->respond(422, ['ok' => false, 'message' => 'Revisá las opciones seleccionadas.']);
        }
        if (empty($data['contacto_autorizado'])) {
            $this->respond(422, ['ok' => false, 'message' => 'Necesitamos tu autorización para poder responder la consulta.']);
        }
        $submission['invitados'] = (int) $guests;
        $submission['servicios'] = array_values(array_unique(array_filter(
            array_map(static fn ($value): string => is_string($value) ? trim($value) : '', $services),
            static fn (string $value): bool => in_array($value, self::SERVICES, true),
        )));

        try {
            $this->submissions->create($submission);
            $this->respond(201, ['ok' => true, 'message' => 'Recibimos tu solicitud.']);
        } catch (Throwable $exception) {
            error_log('Horizonte solicitud insert failed: ' . $exception->getMessage());
            $this->respond(500, ['ok' => false, 'message' => 'No pudimos guardar la solicitud en este momento. Intentá nuevamente más tarde.']);
        }
    }

    private function clean(mixed $value, int $maxLength): string
    {
        if (!is_string($value) && !is_numeric($value)) {
            return '';
        }
        return mb_substr(trim((string) $value), 0, $maxLength, 'UTF-8');
    }

    private function respond(int $status, array $body): never
    {
        http_response_code($status);
        echo json_encode($body, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
        exit;
    }
}
