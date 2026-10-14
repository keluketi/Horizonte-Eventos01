<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="theme-color" content="#f6f1e9"><title>Crear cuenta — Horizonte Eventos</title>
  <link rel="preconnect" href="https://fonts.googleapis.com"><link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="assets/styles.css"><link rel="stylesheet" href="assets/auth.css">
</head>
<body class="auth-page">
  <a class="skip-link" href="#contenido">Saltar al contenido</a>
  <header class="auth-header"><a class="brand" href="index.php" aria-label="Horizonte Eventos, inicio"><span class="brand-mark" aria-hidden="true"><span></span><span></span><span></span></span><span class="brand-name">horizonte<span>eventos</span></span></a><a class="auth-back" href="index.php">← Volver al sitio</a></header>
  <main class="auth-main" id="contenido">
    <section class="auth-card auth-card-wide" aria-labelledby="auth-title">
      <p class="eyebrow">EMPECEMOS A PLANEAR</p><h1 id="auth-title">Creá tu cuenta de <em>cliente.</em></h1>
      <p class="auth-intro">Este formulario crea exclusivamente una cuenta de cliente.</p>
      <?php if ($error !== ''): ?><p class="auth-alert" role="alert"><?= \Horizonte\Core\View::escape($error) ?></p><?php endif; ?>
      <form method="post" action="registro.php" class="auth-form">
        <input type="hidden" name="csrf_token" value="<?= \Horizonte\Core\View::escape($csrfToken) ?>">
        <div class="auth-fields">
          <div><label for="nombre">Nombre</label><input id="nombre" name="nombre" autocomplete="given-name" maxlength="100" value="<?= \Horizonte\Core\View::escape($values['nombre']) ?>" required></div>
          <div><label for="apellido">Apellido</label><input id="apellido" name="apellido" autocomplete="family-name" maxlength="100" value="<?= \Horizonte\Core\View::escape($values['apellido']) ?>" required></div>
          <div><label for="dni_cuit">DNI / CUIT</label><input id="dni_cuit" name="dni_cuit" autocomplete="off" maxlength="20" value="<?= \Horizonte\Core\View::escape($values['dni_cuit']) ?>" required></div>
          <div><label for="telefono">Teléfono <span>(opcional)</span></label><input id="telefono" name="telefono" type="tel" autocomplete="tel" maxlength="20" value="<?= \Horizonte\Core\View::escape($values['telefono']) ?>"></div>
          <div class="auth-field-full"><label for="email">Correo electrónico</label><input id="email" name="email" type="email" autocomplete="email" maxlength="100" value="<?= \Horizonte\Core\View::escape($values['email']) ?>" required></div>
          <div><label for="password">Contraseña <span>(10 caracteres como mínimo)</span></label><input id="password" name="password" type="password" autocomplete="new-password" minlength="10" required></div>
          <div><label for="confirm_password">Repetir contraseña</label><input id="confirm_password" name="confirm_password" type="password" autocomplete="new-password" minlength="10" required></div>
        </div>
        <button class="button button-dark auth-submit" type="submit">Crear cuenta <span aria-hidden="true">↗</span></button>
      </form>
      <p class="auth-switch">¿Ya tenés cuenta? <a href="login.php">Iniciar sesión</a></p>
    </section>
  </main>
  <footer class="auth-footer"><span>© <?= date('Y') ?> Horizonte Eventos</span><span>Celebraciones pensadas para vos.</span></footer>
</body>
</html>
