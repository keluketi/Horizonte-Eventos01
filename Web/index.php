<?php
declare(strict_types=1);
session_start();
if (empty($_SESSION['csrf_token'])) {
    $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
}
$csrfToken = htmlspecialchars($_SESSION['csrf_token'], ENT_QUOTES, 'UTF-8');
require __DIR__ . '/db.php';
require __DIR__ . '/site_content.php';
ensureChatTable();
$siteContent = loadSiteContent();
$eventos = is_array($siteContent['eventos'] ?? null) ? $siteContent['eventos'] : [];
$heroTitle = (string) ($siteContent['hero_title'] ?? 'Hay momentos<br>que merecen <em>todo.</em>');
$heroText = (string) ($siteContent['hero_text'] ?? 'Imaginamos, planeamos y hacemos realidad encuentros que se quedan con vos para siempre.');
?>
<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="theme-color" content="#f6f1e9">
  <meta name="description" content="Celebraciones pensadas para vos. Explorá los eventos, espacios y servicios de Horizonte Eventos y contanos qué estás imaginando.">
  <title>Horizonte Eventos — Un día para recordar</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="assets/styles.css">
  <script>window.HORIZONTE_CSRF = '<?= $csrfToken ?>';</script>
  <script src="assets/app.js" defer></script>
</head>
<body>
  <a class="skip-link" href="#contenido">Saltar al contenido</a>
  <header class="site-header" id="inicio">
    <a class="brand" href="#inicio" aria-label="Horizonte Eventos, inicio">
      <span class="brand-mark" aria-hidden="true"><span></span><span></span><span></span></span>
      <span class="brand-name">horizonte<span>eventos</span></span>
    </a>
    <button class="menu-toggle" aria-expanded="false" aria-controls="main-nav" aria-label="Abrir menú"><span></span><span></span></button>
    <nav class="main-nav" id="main-nav" aria-label="Navegación principal">
      <a href="#eventos">Eventos</a><a href="#espacios">Espacios</a><a href="#servicios">Servicios</a>
      <a class="nav-cta" href="#solicitud">Empezar mi propuesta <span aria-hidden="true">↗</span></a>
    </nav>
  </header>

  <main id="contenido">
    <section class="hero">
      <div class="hero-copy">
        <p class="eyebrow"><span></span> CELEBRACIONES CON HISTORIA</p>
        <h1><?= $heroTitle ?></h1>
        <p class="hero-text"><?= $heroText ?></p>
        <div class="hero-actions"><a class="button button-dark" href="#solicitud">Empezá a imaginarlo <span aria-hidden="true">↗</span></a><a class="text-link" href="#eventos">Descubrí lo que hacemos <span aria-hidden="true">↓</span></a></div>
        <div class="hero-note"><span class="note-stars" aria-hidden="true">✳</span><span>Un equipo cerca tuyo,<br>desde la primera idea.</span></div>
      </div>
      <div class="hero-visual" role="img" aria-label="Mesa de celebración decorada con flores y luz cálida">
        <div class="hero-photo"></div><div class="photo-caption"><span>01 / 06</span><span>Un comienzo inolvidable</span></div>
        <div class="floating-tag"><span class="tag-icon" aria-hidden="true">✳</span><span>Tu historia,<br><strong>nuestro lugar.</strong></span></div>
      </div>
      <div class="hero-bottom"><span>HORIZONTE EVENTOS · BUENOS AIRES</span><span>DISEÑAMOS MOMENTOS, CUIDAMOS CADA DETALLE <b>↓</b></span></div>
    </section>

    <section class="intro section-pad" id="eventos">
      <div class="section-heading"><p class="eyebrow">MOMENTOS PARA CELEBRAR</p><h2>Tu motivo, <em>nuestro punto de partida.</em></h2></div>
      <p class="intro-copy">Cada celebración tiene su propio ritmo. Encontrá la inspiración y contanos qué tenés en mente.</p>
      <div class="event-grid">
        <?php foreach ($eventos as $index => $event): ?>
          <?php $eventName = (string) ($event['name'] ?? ''); $eventLabel = (string) ($event['label'] ?? ''); $eventTitle = (string) ($event['title'] ?? ''); $eventImage = (string) ($event['image'] ?? ''); $eventClass = (string) ($event['class'] ?? ''); ?>
          <button class="event-card <?= htmlspecialchars($eventClass, ENT_QUOTES, 'UTF-8') ?>" data-event="<?= htmlspecialchars($eventName, ENT_QUOTES, 'UTF-8') ?>" style="background-image:url('<?= htmlspecialchars($eventImage, ENT_QUOTES, 'UTF-8') ?>');">
            <span class="event-number"><?= str_pad((string) ($index + 1), 2, '0', STR_PAD_LEFT) ?></span>
            <span class="event-card-content"><span class="event-label"><?= htmlspecialchars($eventLabel, ENT_QUOTES, 'UTF-8') ?></span><strong><?= htmlspecialchars($eventTitle, ENT_QUOTES, 'UTF-8') ?></strong><span class="event-arrow" aria-hidden="true">↗</span></span>
          </button>
        <?php endforeach; ?>
      </div>
      <div class="event-types"><span>También celebramos</span><span>Cumpleaños infantiles</span><i>·</i><span>Aniversarios</span><i>·</i><span>Bautismos</span><i>·</i><span>Reuniones sociales</span></div>
    </section>

    <section class="spaces section-pad" id="espacios">
      <div class="spaces-top"><div><p class="eyebrow">EL ESCENARIO PERFECTO</p><h2>Un lugar que se siente <em>tuyo.</em></h2></div><p>Espacios con personalidad, preparados para recibir a tus invitados con comodidad y calidez.</p></div>
      <div class="space-showcase">
        <div class="space-image space-main" role="img" aria-label="Salón amplio preparado para una celebración"></div>
        <div class="space-info"><span class="space-index">01 — ESPACIOS HORIZONTE</span><h3>Celebrar,<br>con espacio para todos.</h3><p>Contanos cuántas personas querés invitar y te ayudamos a encontrar un salón que se adapte a tu encuentro.</p><a class="text-link" href="#solicitud">Consultar disponibilidad <span aria-hidden="true">↗</span></a><div class="space-features"><span><b>◌</b> Capacidades variadas</span><span><b>⌖</b> Opciones de interior y exterior</span><span><b>✳</b> Espacios para chicos en algunos salones</span></div></div>
        <div class="space-image space-detail" role="img" aria-label="Detalle de una mesa preparada para un evento"></div>
      </div>
      <p class="availability-note">La disponibilidad y capacidad se confirman al revisar cada solicitud.</p>
    </section>

    <section class="services section-pad" id="servicios">
      <div class="services-intro"><p class="eyebrow">CADA DETALLE, EN SU LUGAR</p><h2>Todo lo que imaginás,<br><em>en buenas manos.</em></h2><p>Combinamos opciones para crear una propuesta a medida. Elegí lo que te interesa y lo conversamos juntos.</p><a class="button button-light" href="#solicitud">Armar mi propuesta <span aria-hidden="true">↗</span></a></div>
      <div class="service-list">
        <div class="service-row"><span>01</span><strong>Gastronomía & catering</strong><small>Menús, opciones y necesidades especiales</small><b aria-hidden="true">↗</b></div>
        <div class="service-row"><span>02</span><strong>Música & entretenimiento</strong><small>DJ, shows y animación</small><b aria-hidden="true">↗</b></div>
        <div class="service-row"><span>03</span><strong>Ambientación & decoración</strong><small>Una atmósfera a tu estilo</small><b aria-hidden="true">↗</b></div>
        <div class="service-row"><span>04</span><strong>Foto, video & barra</strong><small>Esos detalles que hacen la diferencia</small><b aria-hidden="true">↗</b></div>
      </div>
    </section>

    <section class="request-section section-pad" id="solicitud">
      <div class="request-heading"><p class="eyebrow">EL PRIMER PASO ES TUYO</p><h2>¿Qué estás <em>imaginando?</em></h2><p>Contanos un poco y nuestro equipo se pondrá en contacto para preparar una propuesta. Enviar la solicitud no reserva ni confirma una fecha.</p><div class="request-aside"><span class="aside-icon" aria-hidden="true">✳</span><span>Nos gusta escuchar cada idea.<br><strong>La tuya también.</strong></span></div></div>
      <form class="request-form" id="request-form" novalidate>
        <div class="form-progress"><span class="progress-step active" data-step-label="1"><i>01</i> Tu evento</span><span class="progress-line"></span><span class="progress-step" data-step-label="2"><i>02</i> Tus opciones</span><span class="progress-line"></span><span class="progress-step" data-step-label="3"><i>03</i> Contacto</span></div>
        <fieldset class="form-step active" data-step="1"><legend class="sr-only">Detalles de tu evento</legend><p class="form-kicker">PRIMERO, LO ESENCIAL</p><h3>Contanos qué vamos a celebrar.</h3>
          <label class="field-label" for="event-type">Tipo de evento <span>*</span></label><select id="event-type" name="tipo_evento" required><option value="">Elegí una opción</option><option>Casamiento</option><option>Cumpleaños de 15</option><option>Cumpleaños infantil</option><option>Evento corporativo</option><option>Aniversario</option><option>Bautismo</option><option>Reunión social</option><option>Otro</option></select>
          <div class="field-row"><div><label class="field-label" for="event-date">Fecha tentativa</label><input id="event-date" name="fecha_tentativa" type="date" min="<?= date('Y-m-d') ?>"></div><div><label class="field-label" for="guest-count">Invitados aproximados <span>*</span></label><input id="guest-count" name="invitados" type="number" min="1" max="5000" placeholder="Ej. 120" required></div></div>
          <div class="field-row"><div><label class="field-label" for="event-time">Horario preferido</label><select id="event-time" name="horario"><option value="">A definir</option><option>Día</option><option>Tarde</option><option>Noche</option></select></div><div><label class="field-label" for="preferred-space">Salón o zona preferida</label><input id="preferred-space" name="espacio_preferido" type="text" placeholder="Si ya tenés alguno en mente"></div></div>
          <div class="form-actions"><span class="required-note">* Campos obligatorios</span><button class="button button-dark next-step" type="button">Seguir <span aria-hidden="true">→</span></button></div>
        </fieldset>
        <fieldset class="form-step" data-step="2"><legend class="sr-only">Servicios y preferencias</legend><p class="form-kicker">A TU MANERA</p><h3>¿Qué te gustaría incluir?</h3><p class="form-hint">Podés elegir varias opciones. La disponibilidad se confirma con el equipo.</p>
          <div class="service-options"><label><input type="checkbox" name="servicios[]" value="Catering"><span class="check-mark">✓</span><span><strong>Catering</strong><small>Comidas y bebidas</small></span></label><label><input type="checkbox" name="servicios[]" value="DJ y música"><span class="check-mark">✓</span><span><strong>DJ y música</strong><small>Ritmo para el encuentro</small></span></label><label><input type="checkbox" name="servicios[]" value="Fotografía y video"><span class="check-mark">✓</span><span><strong>Foto y video</strong><small>Para guardar el momento</small></span></label><label><input type="checkbox" name="servicios[]" value="Decoración"><span class="check-mark">✓</span><span><strong>Decoración</strong><small>Ambientación a medida</small></span></label><label><input type="checkbox" name="servicios[]" value="Barra"><span class="check-mark">✓</span><span><strong>Barra</strong><small>Bebidas y coctelería</small></span></label><label><input type="checkbox" name="servicios[]" value="Show o animación"><span class="check-mark">✓</span><span><strong>Show o animación</strong><small>Entretenimiento</small></span></label></div>
          <label class="field-label" for="notes">¿Algo más que debamos saber?</label><textarea id="notes" name="comentarios" rows="3" maxlength="1500" placeholder="Preferencias de menú, accesibilidad, espacio para chicos u otra idea..."></textarea>
          <div class="form-actions"><button class="button button-back" type="button">← Volver</button><button class="button button-dark next-step" type="button">Seguir <span aria-hidden="true">→</span></button></div>
        </fieldset>
        <fieldset class="form-step" data-step="3"><legend class="sr-only">Tus datos de contacto</legend><p class="form-kicker">NOS ENCANTARÍA CONOCERTE</p><h3>¿Cómo te contactamos?</h3>
          <div class="field-row"><div><label class="field-label" for="first-name">Nombre <span>*</span></label><input id="first-name" name="nombre" type="text" autocomplete="given-name" maxlength="80" required></div><div><label class="field-label" for="last-name">Apellido <span>*</span></label><input id="last-name" name="apellido" type="text" autocomplete="family-name" maxlength="80" required></div></div>
          <div class="field-row"><div><label class="field-label" for="email">Correo electrónico <span>*</span></label><input id="email" name="email" type="email" autocomplete="email" maxlength="160" required></div><div><label class="field-label" for="phone">Teléfono</label><input id="phone" name="telefono" type="tel" autocomplete="tel" maxlength="40" placeholder="Con característica"></div></div>
          <label class="consent"><input type="checkbox" name="contacto_autorizado" value="1" required><span>Autorizo a Horizonte Eventos a contactarme para conversar sobre esta solicitud. <b>*</b></span></label>
          <p class="privacy-note">Tus datos se utilizarán para responder esta consulta. La solicitud no confirma una reserva ni implica un compromiso de contratación.</p>
          <div class="form-actions"><button class="button button-back" type="button">← Volver</button><button class="button button-dark submit-button" type="submit">Enviar solicitud <span aria-hidden="true">↗</span></button></div>
        </fieldset>
        <div class="form-success" id="form-success" role="status" aria-live="polite" hidden><span class="success-mark" aria-hidden="true">✓</span><p class="form-kicker">SOLICITUD RECIBIDA</p><h3>Gracias por compartir tu idea.</h3><p>El equipo de Horizonte Eventos revisará los detalles y se pondrá en contacto. La fecha y los servicios quedan sujetos a confirmación.</p><button type="button" class="text-link reset-form">Enviar otra solicitud <span aria-hidden="true">↗</span></button></div>
        <p class="form-error" id="form-error" role="alert" hidden></p>
      </form>
    </section>

    <section class="closing-banner"><div class="closing-mark" aria-hidden="true">H</div><p>LOS GRANDES MOMENTOS<br>EMPIEZAN CON UNA IDEA.</p><a href="#solicitud" aria-label="Empezar una propuesta">↗</a></section>
  </main>
  <footer class="site-footer"><a class="brand brand-footer" href="#inicio"><span class="brand-mark" aria-hidden="true"><span></span><span></span><span></span></span><span class="brand-name">horizonte<span>eventos</span></span></a><span>Celebraciones pensadas para vos.</span><a href="#inicio">Volver arriba ↑</a><small>© <?= date('Y') ?> Horizonte Eventos</small></footer>

  <div class="chat-widget" aria-live="polite">
    <button class="chat-toggle" type="button" aria-expanded="false" aria-controls="chat-panel">Chat de consultas <span>●</span></button>
    <div class="chat-panel" id="chat-panel" hidden>
      <div class="chat-header">
        <strong>Consultas rápidas</strong>
        <span>Respondemos en breve</span>
      </div>
      <div class="chat-messages" id="chat-messages"></div>
      <form class="chat-form" id="chat-form">
        <input type="text" name="name" placeholder="Tu nombre" maxlength="80" required>
        <input type="email" name="email" placeholder="Tu email" maxlength="180" required>
        <textarea name="message" rows="3" maxlength="1000" placeholder="Escribí tu duda..." required></textarea>
        <button type="submit" class="button button-dark">Enviar mensaje</button>
      </form>
    </div>
  </div>
</body>
</html>
