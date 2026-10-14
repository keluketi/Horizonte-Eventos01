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
      <p class="eyebrow admin-eyebrow">ADMINISTRACIÓN</p>
      <h1>Horizonte Eventos</h1>
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
        <h2>Solicitudes web</h2>
        <span><?= count($solicitudes) ?> registros</span>
      </div>
      <div class="admin-table-wrap">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Cliente</th>
              <th>Evento</th>
              <th>Email</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach ($solicitudes as $solicitud): ?>
              <tr>
                <td>#<?= (int) $solicitud['id_solicitud'] ?></td>
                <td><?= htmlspecialchars(($solicitud['nombre'] ?? '') . ' ' . ($solicitud['apellido'] ?? ''), ENT_QUOTES, 'UTF-8') ?></td>
                <td>
                  <div><strong><?= htmlspecialchars((string) ($solicitud['tipo_evento'] ?? ''), ENT_QUOTES, 'UTF-8') ?></strong></div>
                  <small><?= htmlspecialchars((string) ($solicitud['fecha_tentativa'] ?? ''), ENT_QUOTES, 'UTF-8') ?></small>
                </td>
                <td><?= htmlspecialchars((string) ($solicitud['email'] ?? ''), ENT_QUOTES, 'UTF-8') ?></td>
                <td>
                  <form action="admin_actions.php" method="post" class="status-form">
                    <input type="hidden" name="action" value="update_status">
                    <input type="hidden" name="id_solicitud" value="<?= (int) $solicitud['id_solicitud'] ?>">
                    <select name="estado" aria-label="Estado de la solicitud">
                      <?php foreach (['Pendiente', 'Contactado', 'Aprobado', 'Rechazado'] as $status): ?>
                        <option value="<?= htmlspecialchars($status, ENT_QUOTES, 'UTF-8') ?>" <?= ($solicitud['estado'] ?? 'Pendiente') === $status ? 'selected' : '' ?>><?= htmlspecialchars($status, ENT_QUOTES, 'UTF-8') ?></option>
                      <?php endforeach; ?>
                    </select>
                    <button type="submit" class="button button-dark small">Guardar</button>
                  </form>
                </td>
              </tr>
            <?php endforeach; ?>
          </tbody>
        </table>
      </div>
    </section>

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

    <section class="admin-card">
      <div class="admin-card-header">
        <h2>Chat de clientes</h2>
      </div>
      <div class="chat-threads">
        <?php
        $roots = [];
        foreach ($chatRows as $row) {
            if ((int) ($row['respuesta_a'] ?? 0) === 0 && (int) ($row['es_admin'] ?? 0) === 0) {
                $roots[] = $row;
            }
        }
        if ($roots === []):
            echo '<p class="empty-state">Todavía no hay mensajes del chat.</p>';
        endif;
        foreach ($roots as $root):
            $messageId = (int) $root['id_mensaje'];
            $replies = array_values(array_filter($chatRows, static fn ($item): bool => (int) ($item['respuesta_a'] ?? 0) === $messageId));
            ?>
            <article class="chat-thread">
              <div class="thread-message user">
                <strong><?= htmlspecialchars((string) ($root['nombre'] ?? 'Cliente'), ENT_QUOTES, 'UTF-8') ?></strong>
                <span><?= htmlspecialchars((string) ($root['email'] ?? ''), ENT_QUOTES, 'UTF-8') ?></span>
                <p><?= nl2br(htmlspecialchars((string) ($root['mensaje'] ?? ''), ENT_QUOTES, 'UTF-8')) ?></p>
              </div>
              <?php foreach ($replies as $reply): ?>
                <div class="thread-message admin">
                  <strong>Equipo Horizonte</strong>
                  <span><?= htmlspecialchars((string) ($reply['creado_en'] ?? ''), ENT_QUOTES, 'UTF-8') ?></span>
                  <p><?= nl2br(htmlspecialchars((string) ($reply['mensaje'] ?? ''), ENT_QUOTES, 'UTF-8')) ?></p>
                </div>
              <?php endforeach; ?>
              <form action="admin_actions.php" method="post" class="reply-form">
                <input type="hidden" name="action" value="reply_chat">
                <input type="hidden" name="id_mensaje" value="<?= $messageId ?>">
                <textarea name="mensaje" rows="3" placeholder="Responder al cliente..." required></textarea>
                <button type="submit" class="button button-dark small">Responder</button>
              </form>
            </article>
        <?php endforeach; ?>
      </div>
    </section>
  </main>
</body>
</html>
