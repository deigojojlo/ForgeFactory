#!/bin/bash

# Nom du projet
PROJECT_NAME="POO2024-VINCENT-VALENTIN"

# Répertoire source
SRC_DIR="./ForgeFactory/src/main/java"

# Répertoire de sortie pour les fichiers compilés
OUT_DIR="./out"

# Classe contenant le main (avec son chemin package complet)
MAIN_CLASS="main.java.view.GUI"

# Version de Java à utiliser
JAVA_VERSION="11"

# Fonction pour compiler les fichiers sources
compile() {
    echo "Compilation des fichiers sources avec Java $JAVA_VERSION..."
    mkdir -p "$OUT_DIR"
    javac --release $JAVA_VERSION -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")
    if [ $? -eq 0 ]; then
        echo "Compilation réussie."
    else
        echo "Erreur lors de la compilation."
        exit 2
    fi
}

# Fonction pour nettoyer les fichiers compilés
clean() {
    echo "Suppression des fichiers compilés..."
    rm -rf "$OUT_DIR"
    echo "Fichiers supprimés."
}

# Fonction pour exécuter le programme
run() {
    echo "Exécution du programme avec Java $JAVA_VERSION..."
    if [ ! -d "$OUT_DIR" ]; then
        echo "Les fichiers compilés sont absents. Compilation en cours..."
        compile
    fi
    java -cp "$OUT_DIR" "$MAIN_CLASS"
    if [ $? -eq 0 ]; then
        echo "Programme exécuté avec succès."
    else
        echo "Erreur lors de l'exécution."
        exit 3
    fi
}

# Fonction pour afficher l'aide
usage() {
    echo "Usage: $0 [clean|compile|run]"
    echo "Options:"
    echo "  clean   Supprime les fichiers compilés"
    echo "  compile Compile les fichiers sources avec Java $JAVA_VERSION"
    echo "  run     Exécute le programme principal"
    exit 1
}

# Vérification des arguments
if [ $# -eq 0 ]; then
    usage
fi

# Traitement des options
case $1 in
    clean)
        clean
        ;;
    compile)
        compile
        ;;
    run)
        run
        ;;
    *)
        usage
        ;;
esac
