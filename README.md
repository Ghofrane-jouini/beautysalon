# BeautySalon

Application web de gestion d'un salon de coiffure (clients, services, rendez-vous).
Spring Boot 4.2.0, Java 21, Thymeleaf, Spring Data JPA, base H2.
Projet de fin de module DevOps - Licence 3 DSI - ISET.

## Démarrage en une commande

Prérequis : Docker Desktop et Git Bash.

```bash
cp -n .env.example .env && docker compose up -d --build
```

Puis ouvrir http://localhost:8082 et tester la santé :

```bash
curl http://localhost:8082/health
```

Résultat attendu : `{"status":"UP"}`

Le port de l'application se change dans `.env` (variable `APP_PORT`, 8082 par défaut).

## Architecture

- `app` : application Spring Boot (image construite depuis le `Dockerfile`, utilisateur non root, HEALTHCHECK).
- `h2-db` : base H2 en mode serveur (image construite depuis `h2/Dockerfile`).
- L'application se connecte à la base par le nom de service `h2-db` (port 9092).
- Le port 9092 n'est pas publié sur la machine hôte.
- Les données sont dans le volume nommé `h2-data` (monté sur `/data`).
- L'application démarre seulement quand la base est `healthy`.

## Arrêter

```bash
docker compose down
```

Les données sont conservées. Pour tout effacer, y compris la base : `docker compose down -v`.

## Contrôle local

```bash
bash ./check.sh
```

Code de sortie 0 si tout est bon, 1 sinon.