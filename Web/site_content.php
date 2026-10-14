<?php
declare(strict_types=1);
require_once __DIR__ . '/app/bootstrap.php';

// Adaptadores para conservar los nombres usados por vistas antiguas.
function defaultSiteContent(): array
{
    return (new \Horizonte\Repositories\SiteContentRepository())->defaults();
}

function loadSiteContent(): array
{
    return (new \Horizonte\Repositories\SiteContentRepository())->load();
}

function saveSiteContent(array $content): void
{
    (new \Horizonte\Repositories\SiteContentRepository())->save($content);
}