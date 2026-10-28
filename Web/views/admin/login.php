<?php
if ($adminError !== ''): ?>
<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Acceso administrativo</title><link rel="stylesheet" href="assets/styles.css">
</head>
<body class="admin-page login-page">
  <div class="admin-login-box">
    <p class="eyebrow">SUPERUSUARIO</p><h1>Configuración institucional</h1>
    <p class="login-error"><?= htmlspecialchars($adminError, ENT_QUOTES, 'UTF-8') ?></p>
    <form action="admin_actions.php" method="post" class="admin-login-form">
      <input type="hidden" name="action" value="login">
      <label for="admin-password">Contraseña</label><input id="admin-password" type="password" name="password" placeholder="Ingresá la contraseña" required>
      <button type="submit" class="button button-dark">Entrar</button>
    </form>
  </div>
</body>
</html>
<?php else: ?>
<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Acceso administrativo</title><link rel="stylesheet" href="assets/styles.css">
</head>
<body class="admin-page login-page">
  <div class="admin-login-box">
    <p class="eyebrow">SUPERUSUARIO</p><h1>Configuración institucional</h1>
    <form action="admin_actions.php" method="post" class="admin-login-form">
      <input type="hidden" name="action" value="login">
      <label for="admin-password">Contraseña</label><input id="admin-password" type="password" name="password" placeholder="Ingresá la contraseña" required>
      <button type="submit" class="button button-dark">Entrar</button>
    </form>
  </div>
</body>
</html>
<?php endif; ?>
