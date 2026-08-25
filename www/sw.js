/* DRAME OG BU LA QUALITÉ — service worker
 * Rend l'application utilisable sans réseau. Les données, elles, ne passent
 * jamais par ici : elles restent dans le stockage du téléphone.
 */
const CACHE = 'doq-v1';
const FICHIERS = [
  './',
  './index.html',
  './manifest.webmanifest',
  './icons/icon-192.png',
  './icons/icon-512.png',
  './icons/icon-maskable-192.png',
  './icons/icon-maskable-512.png',
];

self.addEventListener('install', e => {
  e.waitUntil(
    caches.open(CACHE)
      .then(c => c.addAll(FICHIERS))
      .then(() => self.skipWaiting())
      .catch(() => self.skipWaiting())   // une icône manquante ne doit pas bloquer l'installation
  );
});

self.addEventListener('activate', e => {
  e.waitUntil(
    caches.keys()
      .then(noms => Promise.all(noms.filter(n => n !== CACHE).map(n => caches.delete(n))))
      .then(() => self.clients.claim())
  );
});

self.addEventListener('fetch', e => {
  const r = e.request;
  if (r.method !== 'GET' || !r.url.startsWith(self.location.origin)) return;

  // Le document : réseau d'abord (pour recevoir les mises à jour), cache en secours.
  if (r.mode === 'navigate') {
    e.respondWith(
      fetch(r)
        .then(rep => {
          const copie = rep.clone();
          caches.open(CACHE).then(c => c.put('./index.html', copie));
          return rep;
        })
        .catch(() => caches.match('./index.html').then(x => x || caches.match('./')))
    );
    return;
  }

  // Le reste : cache d'abord, c'est ce qui rend l'appli instantanée hors ligne.
  e.respondWith(
    caches.match(r).then(hit => hit || fetch(r).then(rep => {
      if (rep && rep.status === 200 && rep.type === 'basic') {
        const copie = rep.clone();
        caches.open(CACHE).then(c => c.put(r, copie));
      }
      return rep;
    }).catch(() => new Response('', {status: 504, statusText: 'Hors ligne'})))
  );
});
