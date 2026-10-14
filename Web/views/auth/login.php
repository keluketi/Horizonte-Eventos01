<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="theme-color" content="#f6f1e9"><title>Iniciar sesión — Horizonte Eventos</title>
  <link rel="preconnect" href="https://fonts.googleapis.com"><link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="assets/styles.css"><link rel="stylesheet" href="assets/auth.css">
</head>
<body class="auth-page">
  <a class="skip-link" href="#contenido">Saltar al contenido</a>
  <header class="auth-header"><a class="brand" href="index.php" aria-label="Horizonte Eventos, inicio"><span class="brand-mark" aria-hidden="true"><span></span><span></span><span></span></span><span class="brand-name">horizonte<span>eventos</span></span></a><a class="auth-back" href="index.php">← Volver al sitio</a></header>
  <main class="auth-main" id="contenido">
    <section class="auth-card" aria-labelledby="auth-title">
      <p class="eyebrow">TU ESPACIO EN HORIZONTE</p><h1 id="auth-title">Qué bueno verte <em>de nuevo.</em></h1>
      <p class="auth-intro">Clientes ingresan con su correo. Los trabajadores pueden usar el correo o CUIL asociado a su cuenta.</p>
      <?php if ($error !== ''): ?><p class="auth-alert" role="alert"><?= \Horizonte\Core\View::escape($error) ?></p><?php endif; ?>
      <form method="post" action="login.php" class="auth-form">
        <input type="hidden" name="csrf_token" value="<?= \Horizonte\Core\View::escape($csrfToken) ?>">
        <label for="identifier">Correo electrónico o CUIL</label><input id="identifier" name="identifier" type="text" autocomplete="username" maxlength="100" value="<?= \Horizonte\Core\View::escape($identifier) ?>" required>
        <label for="password">Contraseña</label><input id="password" name="password" type="password" autocomplete="current-password" required>
        <button class="button button-dark auth-submit" type="submit">Iniciar sesión <span aria-hidden="true">↗</span></button>
      </form>
      <p class="auth-switch">¿Sos cliente y todavía no tenés cuenta? <a href="registro.php">Crear cuenta de cliente</a></p>
      <p class="auth-help">¿Sos trabajador? Tu cuenta la crea gerencia; pediles tus credenciales.</p>
    </section>
    <aside class="auth-aside"><span aria-hidden="true">✳</span><p>Tu celebración,<br><em>más cerca.</em></p><small>Un equipo cerca tuyo, desde la primera idea.</small></aside>
  </main>
  <footer class="auth-footer"><span>© <?= date('Y') ?> Horizonte Eventos</span><span>Celebraciones pensadas para vos.</span></footer>
</body>
</html>
