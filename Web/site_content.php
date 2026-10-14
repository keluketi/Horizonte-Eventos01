<?php

declare(strict_types=1);

function defaultSiteContent(): array
{
    return [
        'hero_title' => 'Hay momentos<br>que merecen <em>todo.</em>',
        'hero_text' => 'Imaginamos, planeamos y hacemos realidad encuentros que se quedan con vos para siempre.',
        'eventos' => [
            ['name' => 'Casamiento', 'label' => 'PARA DECIR “SÍ”', 'title' => 'Casamientos', 'image' => 'img/casamiento.png', 'class' => 'event-wedding'],
            ['name' => 'Cumpleaños de 15', 'label' => 'UNA NOCHE ÚNICA', 'title' => 'Fiestas de 15', 'image' => 'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=700&q=80', 'class' => 'event-quinces'],
            ['name' => 'Reunión social', 'label' => 'JUNTOS ES MEJOR', 'title' => 'Celebraciones', 'image' => 'https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&w=700&q=80', 'class' => 'event-social'],
            ['name' => 'Evento corporativo', 'label' => 'ENCUENTROS CON PROPÓSITO', 'title' => 'Corporativos', 'image' => 'https://images.unsplash.com/photo-1517457373958-b7bdd4587205?auto=format&fit=crop&w=700&q=80', 'class' => 'event-corporate'],
        ],
    ];
}

function loadSiteContent(): array
{
    $path = __DIR__ . '/site_content.json';
    if (!is_file($path)) {
        saveSiteContent(defaultSiteContent());
    }

    $content = @json_decode((string) file_get_contents($path), true);
    if (!is_array($content)) {
        $content = defaultSiteContent();
        saveSiteContent($content);
    }

    $defaults = defaultSiteContent();
    $content = array_replace_recursive($defaults, $content);
    return $content;
}

function saveSiteContent(array $content): void
{
    $path = __DIR__ . '/site_content.json';
    $json = json_encode($content, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
    file_put_contents($path, $json . PHP_EOL, LOCK_EX);
}
