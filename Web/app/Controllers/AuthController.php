<?php
declare(strict_types=1);

namespace Horizonte\Controllers;

use Horizonte\Core\CsrfToken;
use Horizonte\Core\Session;
use Horizonte\Core\View;
use Horizonte\Models\Client;
use Horizonte\Models\Employee;
use Horizonte\Repositories\AccountRepository;
use Horizonte\Services\AuthService;
use Horizonte\Services\PasswordHasher;
use Throwable;

final class AuthController
{
    private AuthService $auth;
    private AccountRepository $accounts;

    public function __construct()
    {
        $this->accounts = new AccountRepository();
        $this->auth = new AuthService($this->accounts, new PasswordHasher());
    }

    public function login(): void
    {
        if (Session::authenticated()) {
            header('Location: ' . Session::home());
            exit;
        }

        $error = '';
        $identifier = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $identifier = trim((string) ($_POST['identifier'] ?? ''));
            $password = (string) ($_POST['password'] ?? '');
            $submittedToken = is_string($_POST['csrf_token'] ?? null) ? $_POST['csrf_token'] : null;
            if (!CsrfToken::isValid($submittedToken)) {
                $error = 'La sesión venció. Actualizá la página e intentá nuevamente.';
            } elseif ($identifier === '' || mb_strlen($identifier, 'UTF-8') > 100 || $password === '') {
                $error = 'Ingresá tu correo o CUIL y la contraseña.';
            } else {
                try {
                    $account = $this->auth->authenticate($identifier, $password);
                    if ($account instanceof Employee) {
                        session_regenerate_id(true);
                        $_SESSION['empleado_id'] = $account->id;
                        $_SESSION['usuario_tipo'] = $account->type();
                        $_SESSION['usuario_rol'] = $account->roleName;
                        $_SESSION['usuario_nombre'] = $account->name;
                        $_SESSION['usuario_apellido'] = $account->surname;
                        $_SESSION['usuario_email'] = $account->email;
                        unset($_SESSION['cliente_id']);
                        header('Location: panel-trabajador.php');
                        exit;
                    }
                    if ($account instanceof Client) {
                        session_regenerate_id(true);
                        $_SESSION['cliente_id'] = $account->id;
                        $_SESSION['usuario_tipo'] = $account->type();
                        $_SESSION['usuario_nombre'] = $account->name;
                        $_SESSION['usuario_apellido'] = $account->surname;
                        $_SESSION['usuario_email'] = $account->email;
                        unset($_SESSION['empleado_id'], $_SESSION['usuario_rol']);
                        header('Location: mi-cuenta.php');
                        exit;
                    }
                    $error = 'El usuario o la contraseña no coinciden.';
                } catch (Throwable $exception) {
                    error_log('Horizonte auth login: ' . $exception->getMessage());
                    $error = 'No pudimos conectar con el sistema. Revisá la configuración de la base de datos.';
                }
            }
        }

        View::render('auth/login', [
            'error' => $error,
            'identifier' => $identifier,
            'csrfToken' => CsrfToken::value(),
        ]);
    }

    public function registerClient(): void
    {
        if (Session::authenticated()) {
            header('Location: ' . Session::home());
            exit;
        }

        $error = '';
        $values = ['nombre' => '', 'apellido' => '', 'dni_cuit' => '', 'email' => '', 'telefono' => ''];
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            foreach ($values as $field => $_value) {
                $values[$field] = trim((string) ($_POST[$field] ?? ''));
            }
            $password = (string) ($_POST['password'] ?? '');
            $confirmation = (string) ($_POST['confirm_password'] ?? '');
            $submittedToken = is_string($_POST['csrf_token'] ?? null) ? $_POST['csrf_token'] : null;

            if (!CsrfToken::isValid($submittedToken)) {
                $error = 'La sesión venció. Actualizá la página e intentá nuevamente.';
            } elseif ($values['nombre'] === '' || mb_strlen($values['nombre']) > 100
                || $values['apellido'] === '' || mb_strlen($values['apellido']) > 100
                || $values['dni_cuit'] === '' || mb_strlen($values['dni_cuit']) > 20
                || !filter_var($values['email'], FILTER_VALIDATE_EMAIL) || mb_strlen($values['email']) > 100
                || mb_strlen($values['telefono']) > 20) {
                $error = 'Revisá nombre, apellido, DNI/CUIT y correo electrónico.';
            } elseif (mb_strlen($password, 'UTF-8') < 10) {
                $error = 'La contraseña debe tener al menos 10 caracteres.';
            } elseif ($password !== $confirmation) {
                $error = 'Las contraseñas no coinciden.';
            } else {
                try {
                    $clientId = $this->auth->registerClient($values, $password);
                    session_regenerate_id(true);
                    $_SESSION['cliente_id'] = $clientId;
                    $_SESSION['usuario_tipo'] = 'cliente';
                    $_SESSION['usuario_nombre'] = $values['nombre'];
                    $_SESSION['usuario_apellido'] = $values['apellido'];
                    $_SESSION['usuario_email'] = $values['email'];
                    header('Location: mi-cuenta.php');
                    exit;
                } catch (\mysqli_sql_exception $exception) {
                    $error = (int) $exception->getCode() === 1062
                        ? 'Ya existe una cuenta con ese correo o DNI/CUIT.'
                        : 'No pudimos crear la cuenta. Revisá la conexión e intentá nuevamente.';
                    error_log('Horizonte client registration: ' . $exception->getMessage());
                } catch (Throwable $exception) {
                    error_log('Horizonte client registration: ' . $exception->getMessage());
                    $error = 'No pudimos crear la cuenta. Intentá nuevamente más tarde.';
                }
            }
        }

        View::render('auth/register', [
            'error' => $error,
            'values' => $values,
            'csrfToken' => CsrfToken::value(),
        ]);
    }

    public function logout(): void
    {
        $submittedToken = is_string($_POST['csrf_token'] ?? null) ? $_POST['csrf_token'] : null;
        if ($_SERVER['REQUEST_METHOD'] !== 'POST' || !CsrfToken::isValid($submittedToken)) {
            http_response_code(400);
            exit('Solicitud no válida.');
        }

        $_SESSION = [];
        if (ini_get('session.use_cookies')) {
            $cookie = session_get_cookie_params();
            setcookie(session_name(), '', time() - 42000, $cookie['path'], $cookie['domain'], $cookie['secure'], $cookie['httponly']);
        }
        session_destroy();
        header('Location: login.php');
        exit;
    }
}
