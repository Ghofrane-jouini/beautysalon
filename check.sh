#!/usr/bin/env bash
# check.sh : controle du projet BeautySalon
# Code de sortie : 0 = tout est OK, 1 = au moins un controle a echoue

ERREURS=0

echo "=== 1. Fichiers obligatoires ==="
for f in pom.xml mvnw .gitignore README.md src/main/resources/application.properties; do
  if [ -f "$f" ]; then
    echo "OK        $f"
  else
    echo "MANQUANT  $f"
    ERREURS=$((ERREURS + 1))
  fi
done

echo "=== 2. Contenu du .gitignore ==="
for motif in "target/" "data/"; do
  if grep -q "$motif" .gitignore 2>/dev/null; then
    echo "OK        .gitignore contient $motif"
  else
    echo "MANQUANT  .gitignore ne contient pas $motif"
    ERREURS=$((ERREURS + 1))
  fi
done

if [ "$ERREURS" -ne 0 ]; then
  echo "RESULTAT : ECHEC ($ERREURS probleme(s)), tests non lances"
  exit 1
fi

echo "=== 3. Tests automatiques ==="
if bash ./mvnw -B -q test; then
  echo "OK        tests reussis"
else
  echo "ECHEC     des tests ont echoue"
  echo "RESULTAT : ECHEC"
  exit 1
fi

echo "RESULTAT : OK, tout est bon"
exit 0
