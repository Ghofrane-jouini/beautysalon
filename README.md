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

Le port se change dans `.env` (variable `APP_PORT`, 8082 par défaut).
Arrêter : `docker compose down` (données conservées). Tout effacer : `docker compose down -v`.

## Architecture

```
navigateur --> app (Spring Boot, port 8080) --> h2-db (H2 serveur, port 9092)
                                                    |
                                              volume h2-data (/data)
```

- `app` : image construite depuis le `Dockerfile` (multi-stage, utilisateur non root, HEALTHCHECK sur `/health`).
- `h2-db` : base H2 en mode serveur, image construite depuis `h2/Dockerfile` (même version H2 que l'application : 2.5.252).
- L'application se connecte par le nom de service `h2-db`.
- Le port 9092 de la base n'est pas publié sur la machine hôte.
- Les données sont dans le volume nommé `h2-data`.
- `app` démarre seulement quand `h2-db` est `healthy` (`depends_on` avec `service_healthy`).
- Aucun secret dans le dépôt : le mot de passe est dans `.env` (ignoré par Git), `.env.example` donne le modèle.

## Tests et contrôle local

```bash
bash ./check.sh
```

Vérifie les fichiers obligatoires et le `.gitignore`, puis lance les tests Maven. Code de sortie 0 si tout est bon, 1 sinon.
Seuls les tests : `bash ./mvnw -B test`.

## Intégration continue (CI)

Le workflow `.github/workflows/ci.yml` se déclenche sur chaque pull request vers `main` :

1. job `test` : Java 21, `./mvnw test`, rapport Surefire publié en artefact (`rapport-tests-surefire`), même en cas d'échec ;
2. job `build-image` (`needs: test`) : construit l'image Docker sans la publier. Il n'est pas lancé si les tests échouent.

## Livraison

Le workflow `.github/workflows/release.yml` se déclenche sur un tag `vX.Y.Z`. Il construit l'image une seule fois et la publie sur GHCR :

```bash
git tag -a v1.0.0 -m "Version 1.0.0"
git push origin v1.0.0
```

Image publiée : `ghcr.io/ghofrane-jouini/beautysalon:<tag>`. Une image publiée n'est jamais reconstruite ni écrasée : chaque version a son propre tag.

## Mise en service en test (sans reconstruction)

`compose.test.yaml` utilise uniquement l'image publiée (pas de `build` pour l'application). La version se choisit avec `APP_TAG` (le fichier `.env` doit exister) :

```bash
APP_TAG=v1.0.3 docker compose -f compose.test.yaml up -d
curl http://localhost:8083/health
```

Le workflow manuel `.github/workflows/deploy-test.yml` (Actions, Deploy test, Run workflow) fait la même chose sur un runner GitHub, contrôle `/health` et échoue si l'application n'est pas saine (par exemple tag inexistant).

## Retour arrière (rollback)

Une seule commande : on remet le tag de la version précédente.

```bash
APP_TAG=v1.0.0 docker compose -f compose.test.yaml up -d
```

La base H2 n'est pas touchée : les deux versions utilisent le même schéma (seul un texte HTML change), donc aucune perte de données.

## Versions

| Tag | Contenu |
|---|---|
| v1.0.0 | version de référence (titre : "Tableau de bord") |
| v1.0.3 | titre : "Tableau de bord - v1.0.2" (changement visible) |

Les tags v1.0.1 et v1.0.2 ont été créés par erreur sur un mauvais commit, puis supprimés de Git. Ils ne sont pas utilisés. Règle appliquée ensuite : un nouveau tag pour chaque version, jamais d'écrasement.