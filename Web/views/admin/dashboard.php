<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Admin · Horizonte Eventos</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="assets/styles.css">
</head>
<body class="admin-page">
  <header class="admin-header">
    <div>
      <p class="eyebrow admin-eyebrow">SUPERUSUARIO</p>
      <h1>Configuración institucional</h1>
    </div>
    <form action="admin_actions.php" method="post">
      <input type="hidden" name="action" value="logout">
      <button type="submit" class="button button-light">Cerrar sesión</button>
    </form>
  </header>

  <main class="admin-shell">
    <?php if ($successText !== ''): ?>
      <div class="admin-banner success"><?= htmlspecialchars($successText, ENT_QUOTES, 'UTF-8') ?></div>
    <?php endif; ?>
    <?php if ($warningText !== ''): ?>
      <div class="admin-banner error"><?= htmlspecialchars($warningText, ENT_QUOTES, 'UTF-8') ?></div>
    <?php endif; ?>

    <section class="admin-card">
      <div class="admin-card-header">
        <h2>Contenido del home</h2>
      </div>
      <form action="admin_actions.php" method="post" class="admin-content-form">
        <input type="hidden" name="action" value="save_content">
        <div class="field-row admin-fields">
          <div>
            <label for="hero-title">Título principal</label>
            <input id="hero-title" type="text" name="hero_title" value="<?= htmlspecialchars((string) ($siteContent['hero_title'] ?? ''), ENT_QUOTES, 'UTF-8') ?>">
          </div>
          <div>
            <label for="hero-text">Texto de la portada</label>
            <input id="hero-text" type="text" name="hero_text" value="<?= htmlspecialchars((string) ($siteContent['hero_text'] ?? ''), ENT_QUOTES, 'UTF-8') ?>">
          </div>
        </div>

        <?php foreach ($events as $index => $event): ?>
          <div class="event-admin-block">
            <h3>Evento <?= $index + 1 ?></h3>
            <div class="field-row admin-fields">
              <div>
                <label>Nombre</label>
                <input type="text" name="evento[<?= $index ?>][name]" value="<?= htmlspecialchars((string) ($event['name'] ?? ''), ENT_QUOTES, 'UTF-8') ?>">
              </div>
              <div>
                <label>Etiqueta</label>
                <input type="text" name="evento[<?= $index ?>][label]" value="<?= htmlspecialchars((string) ($event['label'] ?? ''), ENT_QUOTES, 'UTF-8') ?>">
              </div>
            </div>
            <div class="field-row admin-fields">
              <div>
                <label>Título</label>
                <input type="text" name="evento[<?= $index ?>][title]" value="<?= htmlspecialchars((string) ($event['title'] ?? ''), ENT_QUOTES, 'UTF-8') ?>">
              </div>
              <div>
                <label>Imagen</label>
                <input type="text" name="evento[<?= $index ?>][image]" value="<?= htmlspecialchars((string) ($event['image'] ?? ''), ENT_QUOTES, 'UTF-8') ?>">
              </div>
            </div>
          </div>
        <?php endforeach; ?>

        <button type="submit" class="button button-dark">Guardar cambios</button>
      </form>
    </section>

  </main>
</body>
</html>
