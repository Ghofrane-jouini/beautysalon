# Fiche projet DevOps - BeautySalon

Étudiante : Ghofrane Jouini - Licence 3 DSI - ISET
Dépôt : https://github.com/Ghofrane-jouini/beautysalon

## 1. Architecture

```
Git push / pull request --> GitHub Actions (CI : tests puis image)
Tag vX.Y.Z --> GitHub Actions (Release) --> image publiée sur GHCR
                                                |
                                       docker pull (sans rebuild)
                                                v
navigateur --> app (Spring Boot :8080) --> h2-db (H2 serveur :9092, non publié)
                                                |
                                          volume h2-data
```

- Application : Spring Boot 4.2.0, Java 21, image Docker multi-stage, utilisateur non root, HEALTHCHECK sur `/health`.
- Base : H2 en mode serveur (2.5.252), connexion par nom de service, port 9092 non publié, volume nommé, `app` démarre quand la base est `healthy`.
- Pas de secret dans Git : mot de passe dans `.env` (ignoré), modèle dans `.env.example`.

## 2. Version mise en service

- Image : `ghcr.io/ghofrane-jouini/beautysalon:v1.0.3` (digest `sha256:e735f9fd...`), publiée une seule fois par le workflow Release.
- Environnement de test : `compose.test.yaml`, port 8083, contrôle `/health` = UP.
- Changement visible : titre "Tableau de bord - v1.0.2".
- Les tags v1.0.1 et v1.0.2 ont été posés par erreur sur un mauvais commit, supprimés de Git et non utilisés. Règle : un nouveau tag par version, jamais d'écrasement.

## 3. Déroulé du retour arrière

1. État initial : `APP_TAG=v1.0.3 docker compose -f compose.test.yaml up -d` : titre "Tableau de bord - v1.0.2", `/health` UP.
2. Retour arrière (une commande) : `APP_TAG=v1.0.0 docker compose -f compose.test.yaml up -d`.
3. Contrôles : `app` healthy, `/health` UP, titre "Tableau de bord", conteneur sur l'image `sha256:32b86f03...` = digest de v1.0.0 publié au départ (aucune reconstruction).
4. Données : la base n'est pas touchée, même schéma dans les deux versions (seul du HTML change).

## 4. Preuves (exécutions GitHub Actions)

- Pipeline réussi : (coller l'URL du run CI vert)
- Pipeline en échec (test cassé, build-image ignoré) : (coller l'URL du run CI rouge)
- Release v1.0.0 : (coller l'URL)
- Deploy test réussi / en échec (tag inexistant) : (coller les 2 URL)