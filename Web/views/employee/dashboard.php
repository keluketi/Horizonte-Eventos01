<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="theme-color" content="#f6f1e9"><title>Panel de <?= \Horizonte\Core\View::escape($role) ?> — Horizonte Eventos</title>
  <link rel="preconnect" href="https://fonts.googleapis.com"><link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="assets/styles.css"><link rel="stylesheet" href="assets/auth.css">
</head>
<body class="auth-page">
  <a class="skip-link" href="#contenido">Saltar al contenido</a>
  <header class="auth-header"><a class="brand" href="index.php" aria-label="Horizonte Eventos, inicio"><span class="brand-mark" aria-hidden="true"><span></span><span></span><span></span></span><span class="brand-name">horizonte<span>eventos</span></span></a><form method="post" action="logout.php"><input type="hidden" name="csrf_token" value="<?= \Horizonte\Core\View::escape($csrfToken) ?>"><button class="auth-back auth-logout" type="submit">Cerrar sesión</button></form></header>
  <main class="staff-main" id="contenido">
    <section class="staff-heading" aria-labelledby="staff-title">
      <p class="eyebrow">ÁREA INTERNA · <?= \Horizonte\Core\View::escape(mb_strtoupper($role, 'UTF-8')) ?></p>
      <h1 id="staff-title">Hola, <?= \Horizonte\Core\View::escape($name) ?>.</h1>
      <p>Este es tu espacio de trabajo para el rol de <strong><?= \Horizonte\Core\View::escape($role) ?></strong>.</p>
    </section>
    <?php if ($modules === []): ?>
      <p class="auth-alert" role="status">Tu rol todavía no tiene un panel asignado. Consultá con gerencia.</p>
    <?php else: ?>
      <section class="staff-modules" aria-label="Módulos de tu rol">
        <?php foreach ($modules as $index => [$title, $description]): ?>
          <article class="staff-module"><span class="staff-module-number"><?= str_pad((string) ($index + 1), 2, '0', STR_PAD_LEFT) ?></span><h2><?= \Horizonte\Core\View::escape($title) ?></h2><p><?= \Horizonte\Core\View::escape($description) ?></p><span class="staff-module-status">Módulo en preparación</span></article>
        <?php endforeach; ?>
      </section>
    <?php endif; ?>
    <p class="staff-note">Este acceso reconoce tu cuenta y rol. Las funciones internas se irán conectando a la aplicación de gestión.</p>
    <a class="account-secondary" href="index.php">Volver al sitio público</a>
  </main>
  <footer class="auth-footer"><span>© <?= date('Y') ?> Horizonte Eventos</span><span>Acceso para el equipo</span></footer>
</body>
</html>
