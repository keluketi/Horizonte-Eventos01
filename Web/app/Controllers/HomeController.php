<?php
declare(strict_types=1);

namespace Horizonte\Controllers;

use Horizonte\Core\CsrfToken;
use Horizonte\Core\View;
use Horizonte\Repositories\ChatRepository;
use Horizonte\Repositories\SiteContentRepository;
use Throwable;

final class HomeController
{
    public function index(): void
    {
        try {
            (new ChatRepository())->ensureSchema();
        } catch (Throwable $exception) {
            error_log('Horizonte chat schema error: ' . $exception->getMessage());
        }

        $content = (new SiteContentRepository())->load();
        View::render('home/index', [
            'csrfToken' => htmlspecialchars(CsrfToken::value(), ENT_QUOTES, 'UTF-8'),
            'siteContent' => $content,
            'eventos' => is_array($content['eventos'] ?? null) ? $content['eventos'] : [],
            'heroTitle' => (string) ($content['hero_title'] ?? 'Hay momentos<br>que merecen <em>todo.</em>'),
            'heroText' => (string) ($content['hero_text'] ?? 'Imaginamos, planeamos y hacemos realidad encuentros que se quedan con vos para siempre.'),
        ]);
    }
}
