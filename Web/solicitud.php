<?php
declare(strict_types=1);
session_start();
header('Content-Type: application/json; charset=utf-8');
header('X-Content-Type-Options: nosniff');
header('Cache-Control: no-store');

function respond(int $status, array $body): never
{
    http_response_code($status);
    echo json_encode($body, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    header('Allow: POST');
    respond(405, ['ok' => false, 'message' => 'Método no permitido.']);
}

$contentType = strtolower(trim(explode(';', $_SERVER['CONTENT_TYPE'] ?? '')[0]));
if ($contentType !== 'application/json') {
    respond(415, ['ok' => false, 'message' => 'Formato de solicitud no válido.']);
}

$raw = file_get_contents('php://input');
if ($raw === false || strlen($raw) > 20000) {
    respond(413, ['ok' => false, 'message' => 'La solicitud supera el tamaño permitido.']);
}
$data = json_decode($raw, true);
if (!is_array($data)) {
    respond(400, ['ok' => false, 'message' => 'No pudimos leer los datos enviados.']);
}

$submittedToken = is_string($data['csrf_token'] ?? null) ? $data['csrf_token'] : '';
if (empty($_SESSION['csrf_token']) || !hash_equals($_SESSION['csrf_token'], $submittedToken)) {
    respond(403, ['ok' => false, 'message' => 'La sesión venció. Actualizá la página e intentá nuevamente.']);
}

$clean = static function (mixed $value, int $maxLength): string {
    if (!is_string($value) && !is_numeric($value)) return '';
    $value = trim((string) $value);
    return mb_substr($value, 0, $maxLength, 'UTF-8');
};
$required = static function (string $key, int $maxLength) use ($data, $clean): string {
    return $clean($data[$key] ?? '', $maxLength);
};

$eventTypes = ['Casamiento', 'Cumpleaños de 15', 'Cumpleaños infantil', 'Evento corporativo', 'Aniversario', 'Bautismo', 'Reunión social', 'Otro'];
$timeOptions = ['', 'A definir', 'Día', 'Tarde', 'Noche'];
$type = $required('tipo_evento', 80);
$firstName = $required('nombre', 80);
$lastName = $required('apellido', 80);
$email = $required('email', 160);
$phone = $required('telefono', 40);
$date = $required('fecha_tentativa', 10);
$time = $required('horario', 20);
$space = $required('espacio_preferido', 120);
$notes = $required('comentarios', 1500);
$guestInput = filter_var($data['invitados'] ?? null, FILTER_VALIDATE_INT);
$services = $data['servicios'] ?? [];

if (!in_array($type, $eventTypes, true) || $firstName === '' || $lastName === '' || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
    respond(422, ['ok' => false, 'message' => 'Revisá el tipo de evento y tus datos de contacto.']);
}
if ($guestInput === false || $guestInput < 1 || $guestInput > 5000) {
    respond(422, ['ok' => false, 'message' => 'Ingresá una cantidad válida de invitados.']);
}
if ($date !== '' && (!preg_match('/^\d{4}-\d{2}-\d{2}$/', $date) || !checkdate((int) substr($date, 5, 2), (int) substr($date, 8, 2), (int) substr($date, 0, 4)))) {
    respond(422, ['ok' => false, 'message' => 'La fecha indicada no es válida.']);
}
if ($date !== '' && $date < date('Y-m-d')) {
    respond(422, ['ok' => false, 'message' => 'Elegí una fecha tentativa a partir de hoy.']);
}
if (!in_array($time, $timeOptions, true) || !is_array($services) || count($services) > 12) {
    respond(422, ['ok' => false, 'message' => 'Revisá las opciones seleccionadas.']);
}
if (empty($data['contacto_autorizado'])) {
    respond(422, ['ok' => false, 'message' => 'Necesitamos tu autorización para poder responder la consulta.']);
}
$allowedServices = ['Catering', 'DJ y música', 'Fotografía y video', 'Decoración', 'Barra', 'Show o animación'];
$services = array_values(array_unique(array_filter(array_map(static fn ($value): string => is_string($value) ? trim($value) : '', $services), static fn ($value): bool => in_array($value, $allowedServices, true))));

$host = getenv('HORIZONTE_DB_HOST') ?: '127.0.0.1';
$port = (int) (getenv('HORIZONTE_DB_PORT') ?: 3306);
$database = getenv('HORIZONTE_DB_NAME') ?: 'HE';
$username = getenv('HORIZONTE_DB_USER') ?: 'root';
$password = getenv('HORIZONTE_DB_PASSWORD') ?: '';
if ($database === '' || $username === '') {
    respond(503, ['ok' => false, 'message' => 'El formulario todavía no está conectado. Probá más tarde o contactanos por los canales de la empresa.']);
}

mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
try {
    $db = new mysqli($host, $username, $password, $database, $port);
    $db->set_charset('utf8mb4');
    $servicesJson = json_encode($services, JSON_UNESCAPED_UNICODE | JSON_THROW_ON_ERROR);
    $stmt = $db->prepare('INSERT INTO solicitudes_web (tipo_evento, fecha_tentativa, horario, invitados, espacio_preferido, servicios, comentarios, nombre, apellido, email, telefono) VALUES (?, NULLIF(?, \'\'), NULLIF(?, \'\'), ?, NULLIF(?, \'\'), ?, NULLIF(?, \'\'), ?, ?, ?, NULLIF(?, \'\'))');
    $stmt->bind_param('sssisssssss', $type, $date, $time, $guestInput, $space, $servicesJson, $notes, $firstName, $lastName, $email, $phone);
    $stmt->execute();
    $stmt->close();
    $db->close();
    respond(201, ['ok' => true, 'message' => 'Recibimos tu solicitud.']);
} catch (Throwable $exception) {
    error_log('Horizonte solicitud insert failed: ' . $exception->getMessage());
    respond(500, ['ok' => false, 'message' => 'No pudimos guardar la solicitud en este momento. Intentá nuevamente más tarde.']);
}
