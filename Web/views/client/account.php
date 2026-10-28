<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="theme-color" content="#f6f1e9"><title>Mi cuenta — Horizonte Eventos</title>
  <link rel="preconnect" href="https://fonts.googleapis.com"><link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="assets/styles.css"><link rel="stylesheet" href="assets/auth.css">
</head>
<body class="auth-page">
  <a class="skip-link" href="#contenido">Saltar al contenido</a>
  <header class="auth-header"><a class="brand" href="index.php" aria-label="Horizonte Eventos, inicio"><span class="brand-mark" aria-hidden="true"><span></span><span></span><span></span></span><span class="brand-name">horizonte<span>eventos</span></span></a><form method="post" action="logout.php"><input type="hidden" name="csrf_token" value="<?= \Horizonte\Core\View::escape($csrfToken) ?>"><button class="auth-back auth-logout" type="submit">Cerrar sesión</button></form></header>
  <main class="auth-main account-main" id="contenido">
    <section class="auth-card account-card" aria-labelledby="account-title">
      <p class="eyebrow">MI ESPACIO HORIZONTE</p><h1 id="account-title">Hola, <?= \Horizonte\Core\View::escape($name) ?> <em>✳</em></h1>
      <p class="auth-intro">Tu cuenta ya está conectada. Desde acá vamos a poder acercarte la información de tus consultas y eventos.</p>
      <div class="account-note"><strong>Seguimiento de tus propuestas</strong><p>Acá podés consultar el estado y las respuestas del equipo a tus solicitudes.</p></div>
      <?php if ($requests !== []): ?>
        <div class="client-request-list">
          <?php foreach ($requests as $request): ?>
            <article class="account-note">
              <strong><?= \Horizonte\Core\View::escape((string) $request['tipo_evento']) ?> · <?= \Horizonte\Core\View::escape((string) ($request['estado'] ?? 'Pendiente')) ?></strong>
              <p><?= \Horizonte\Core\View::escape((string) ($request['fecha_tentativa'] ?: 'Fecha a definir')) ?> · <?= (int) $request['invitados'] ?> invitados</p>
              <?php if (!empty($request['respuesta_admin'])): ?><p><b>Mensaje del equipo:</b> <?= nl2br(\Horizonte\Core\View::escape((string) $request['respuesta_admin'])) ?></p><?php endif; ?>
            </article>
          <?php endforeach; ?>
        </div>
      <?php else: ?>
        <p class="auth-intro">Todavía no tenés propuestas registradas con este correo.</p>
      <?php endif; ?>
      <a class="button button-dark auth-submit account-link" href="index.php#solicitud">Empezar una propuesta <span aria-hidden="true">↗</span></a>
      <a class="account-secondary" href="index.php">Volver al sitio</a>
    </section>
  </main>
  <footer class="auth-footer"><span>© <?= date('Y') ?> Horizonte Eventos</span><span>Celebraciones pensadas para vos.</span></footer>
</body>
</html>
