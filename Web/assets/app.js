(() => {
  const form = document.querySelector('#request-form');
  if (!form) return;

  const steps = [...form.querySelectorAll('.form-step')];
  const progress = [...form.querySelectorAll('.progress-step')];
  const errorBox = document.querySelector('#form-error');
  const success = document.querySelector('#form-success');
  const menuButton = document.querySelector('.menu-toggle');
  const nav = document.querySelector('.main-nav');
  let current = 0;

  const showError = (message) => {
    errorBox.textContent = message;
    errorBox.hidden = false;
    errorBox.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
  };

  const clearError = () => {
    errorBox.textContent = '';
    errorBox.hidden = true;
  };

  const updateStep = (next) => {
    current = next;
    steps.forEach((step, index) => {
      step.classList.toggle('active', index === current);
      step.setAttribute('aria-hidden', index === current ? 'false' : 'true');
    });
    progress.forEach((item, index) => {
      item.classList.toggle('active', index === current);
      item.classList.toggle('done', index < current);
    });
    clearError();
  };

  const validateStep = () => {
    const fields = [...steps[current].querySelectorAll('input, select, textarea')];
    for (const field of fields) {
      if (field.required && !field.checkValidity()) {
        field.reportValidity();
        return false;
      }
    }
    return true;
  };

  form.querySelectorAll('.next-step').forEach((button) => {
    button.addEventListener('click', () => {
      if (validateStep() && current < steps.length - 1) updateStep(current + 1);
    });
  });

  form.querySelectorAll('.button-back').forEach((button) => {
    button.addEventListener('click', () => updateStep(Math.max(0, current - 1)));
  });

  document.querySelectorAll('.event-card').forEach((card) => {
    card.addEventListener('click', () => {
      const type = document.querySelector('#event-type');
      const value = card.dataset.event;
      const matchingOption = [...type.options].find((option) => option.value === value || option.textContent === value);
      if (matchingOption) type.value = matchingOption.value;
      document.querySelector('#solicitud').scrollIntoView({ behavior: 'smooth' });
      type.focus({ preventScroll: true });
    });
  });

  if (menuButton && nav) {
    menuButton.addEventListener('click', () => {
      const expanded = menuButton.getAttribute('aria-expanded') === 'true';
      menuButton.setAttribute('aria-expanded', String(!expanded));
      menuButton.setAttribute('aria-label', expanded ? 'Abrir menú' : 'Cerrar menú');
      nav.classList.toggle('open', !expanded);
    });
    nav.querySelectorAll('a').forEach((link) => link.addEventListener('click', () => {
      nav.classList.remove('open');
      menuButton.setAttribute('aria-expanded', 'false');
      menuButton.setAttribute('aria-label', 'Abrir menú');
    }));
  }

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    clearError();
    if (!validateStep()) return;

    const submitButton = form.querySelector('.submit-button');
    const originalText = submitButton.innerHTML;
    submitButton.disabled = true;
    submitButton.textContent = 'Enviando…';

    const payload = Object.fromEntries(new FormData(form).entries());
    payload.servicios = new FormData(form).getAll('servicios[]');
    payload.csrf_token = window.HORIZONTE_CSRF;

    try {
      const response = await fetch('solicitud.php', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
        body: JSON.stringify(payload),
      });
      let result;
      try {
        result = await response.json();
      } catch {
        throw new Error('No pudimos procesar la respuesta. Intentá nuevamente más tarde.');
      }
      if (!response.ok || !result.ok) throw new Error(result.message || 'No pudimos enviar la solicitud. Intentá nuevamente.');
      steps.forEach((step) => { step.hidden = true; });
      form.querySelector('.form-progress').hidden = true;
      success.hidden = false;
      success.scrollIntoView({ behavior: 'smooth', block: 'center' });
    } catch (error) {
      showError(error.message || 'Hubo un problema de conexión. Por favor, intentá nuevamente.');
    } finally {
      submitButton.disabled = false;
      submitButton.innerHTML = originalText;
    }
  });

  form.querySelector('.reset-form').addEventListener('click', () => {
    form.reset();
    steps.forEach((step) => { step.hidden = false; });
    success.hidden = true;
    form.querySelector('.form-progress').hidden = false;
    updateStep(0);
    document.querySelector('#solicitud').scrollIntoView({ behavior: 'smooth' });
  });

  updateStep(0);
})();
