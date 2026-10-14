<?php
declare(strict_types=1);

namespace Horizonte\Controllers;

use Horizonte\Core\CsrfToken;
use Horizonte\Core\Session;
use Horizonte\Core\View;

final class EmployeeController
{
    private const ROLE_MODULES = [
        'gerencia' => [
            ['Resumen general', 'Indicadores y estado general de Horizonte Eventos.'],
            ['Eventos y clientes', 'Consulta de eventos, clientes y ocupación de espacios.'],
            ['Ingresos y reportes', 'Información económica y reportes de gestión.'],
            ['Auditoría', 'Seguimiento de operaciones importantes del sistema.'],
            ['Usuarios y permisos', 'Administración de accesos y permisos del equipo.'],
        ],
        'administracion' => [
            ['Clientes', 'Consulta y gestión de la información de clientes.'],
            ['Presupuestos', 'Seguimiento de presupuestos y sus estados.'],
            ['Eventos', 'Consulta y gestión administrativa de eventos.'],
            ['Pagos y comprobantes', 'Registro y consulta de pagos y comprobantes.'],
            ['Proveedores y servicios', 'Información de proveedores y servicios contratados.'],
        ],
        'coordinacion' => [
            ['Calendario', 'Fechas, horarios y espacios de los eventos.'],
            ['Organización del evento', 'Invitados, servicios, cronograma y modificaciones.'],
            ['Personal asignado', 'Consulta del equipo que participa en cada evento.'],
            ['Incidencias', 'Registro y seguimiento de novedades operativas.'],
        ],
        'personal' => [
            ['Mis eventos', 'Consulta de los eventos en los que participás.'],
            ['Horarios y tareas', 'Información de horarios y tareas asignadas.'],
            ['Incidencias', 'Registro de novedades relacionadas con tu trabajo.'],
        ],
        'encargado de personal' => [
            ['Eventos y horarios', 'Consulta de eventos y necesidades de cada jornada.'],
            ['Personal disponible', 'Consulta y asignación de personal para los eventos.'],
            ['Tareas y asignaciones', 'Organización de funciones y participación del equipo.'],
        ],
    ];

    public function dashboard(): void
    {
        Session::requireEmployee();
        $role = trim((string) ($_SESSION['usuario_rol'] ?? ''));
        $roleKey = mb_strtolower($role, 'UTF-8');
        View::render('employee/dashboard', [
            'name' => (string) ($_SESSION['usuario_nombre'] ?? ''),
            'role' => $role,
            'modules' => self::ROLE_MODULES[$roleKey] ?? [],
            'csrfToken' => CsrfToken::value(),
        ]);
    }
}
