# Modèle de Sauvegarde et Description

Voici un modèle structuré de sauvegarde pour un jeu, décrivant différents composants tels que la carte, les détails du
joueur, les informations des usines et des moissonneuses. Vous trouverez ci-dessous l'explication de chaque section.

---

### **1. Carte**

- **Format** : Chaque ligne représente un segment de la carte.
- **Séparation** : Chaque ligne de la carte est séparée par un symbole `/`, et chaque case (cellule) est séparée par une
  virgule `,`.
- **Date** : Chaque entrée de la carte est associée à un ID de liste (représenté par un entier), traité à l'aide des
  fonctions `DB.intToList` et `DB.listToInt`.

---

### **2. Porte-Monnaie du Joueur**

- **Format** : Il s'agit d'une valeur entière simple représentant le montant de monnaie que possède le joueur.

---

### **3. Inventaire du Joueur**

- **Format** : Une série d'ID d'objets et de leurs quantités respectives, représentées sous forme de paires d'entiers.
- **Séparation** : Chaque paire (ID d'objet et quantité) est séparée par un deux-points (`:`), et les éléments multiples
  sont séparés par un slash (`/`).

---

### **4. Informations des Usines**

- **Format** : Une liste d'usines, chaque usine étant séparée par un point-virgule (`;`).
- **Structure** : Les informations de chaque usine sont séparées par des virgules. Les composants clés de chaque usine
  sont :
    - **Position** : Une paire de coordonnées (par exemple, `x:y`).
    - **Inventaire** : Représenté dans le même format que l'inventaire du joueur (ID d'objet et quantité).
    - **Améliorations** : Une liste d'ID d'améliorations séparées par un slash (`/`).
    - **Recette** : Chaque usine a une recette identifiée par son emplacement dans `DB.recette`.

---

### **5. Informations des Moissonneuses**

- **Format** : Semblable aux usines, les informations des moissonneuses sont listées, chaque moissonneuse étant séparée
  par un point-virgule (`;`).
- **Structure** : Chaque moissonneuse contient les éléments suivants :
    - **Position** : Une paire de coordonnées (par exemple, `x:y`).
    - **Inventaire** : Les ID d'objets et leurs quantités, comme pour l'inventaire du joueur.
    - **Améliorations** : Une liste d'ID d'améliorations séparées par un slash (`/`).
    - **ID de Ressource** : L'ID de la ressource pour la moissonneuse, représenté par l'ID de `DB.intToList`.

---

### **Exemple de Fichier de Sauvegarde**

Voici comment un fichier de sauvegarde est structuré :

- **Carte** : Représente une grille de 2x2, avec certaines cases étant nulles ou vides, marquées par `/null`.
- **Porte-Monnaie du Joueur** : Représente l'argent du joueur (par exemple, `10`).
- **Inventaire du Joueur** : Exemple montrant 20 objets d'ID 0 et 20 objets d'ID 1 (`0:20/1:20`).
- **Usine** : Une usine située à `(0,0)` sans objet, améliorations ou recettes (`0:0`).
- **Moissonneuse** : Une moissonneuse située à `(1,0)` avec 20 objets d'ID 0 et une ressource d'ID 0 (`1:0.0:20`).

---

### **Exemple de Données de Sauvegarde**

#### Exemple de Sauvegarde :

7,7,7,7,7,7,7,7,7,7,7,7,7,7,3,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,3,7,7,7,7,0,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,7,7,7,7,2,3,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,7,7,7,7,7,7,4,7,7,7,7,7,7,7,7,7,1,7,7,4,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,7,7,1,7,7,7,7,7,7,1,7,7,7,7,7,7,7,7,7,7,7,2,7,7,7,7,7,7,7,7/7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,2,7,7,7,7,7,7,7/7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,4,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,7,7,0,7,7,0,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7/3,7,7,7,7,7,7,7,4,7,7,7,7,7,7,7,7,7,7,7,3,7,7,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,1,6,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,9,7,7,7,7,3,7,5,7,7,7,7,7,7,7,2,6,7/1,7,7,7,7,7,1,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,0,7,7,7,7,7,7,1,6,6,7,7/7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,2,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,7,7,7,7,2,7,7,7,7,7,4,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,3,1,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7/7,7,7,7,7,7,3,7,7,0,7,7,7,7,7,7,7,7,7,1,7,7,7,7,7,7,7,7,7,7,7,7,7,5,7/7,0,7,7,7,7,7,2,7,7,7,7,7,4,7,7,7,7,7,7,7,7,7,7,7,6,7,7,7,3,7,7,7,5,7/7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,2,7,7,7,7,7,7,7,7,0,6,7,7,7,7,7,7,5,7/7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,4,7,7,7,7,2,7,6,7,7,7,5,7/7,7,7,7,7,7,7,7,7,0,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,4,6,7,5,5,7/7,7,7,7,7,0,7,7,7,4,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,8/
500
2:2/3:6/0:27/15:2/1:1/
18:33,,200,NULL:1/, ,0;11:24,,200,NULL:1/XL:1/, ,0;19:33,,200,NULL:1/, ,0;20:32,4:1/,200,NULL:1/, ,2;20:
33,,500,POLYVALENTE:1/NULL:1/UNBREAKING:3/SPEED:2/, ,1;16:33,,200,NULL:1/XL:1/, ,0;17:33,,200,NULL:1/, ,0;
18:26,0:183/,159,NULL:1/,0;11:33,2:44/,178,NULL:1/,2;


---

### **Comment Lire le Fichier de Sauvegarde**

- **Carte** : Chaque segment (ligne) représente différentes zones de la carte.
    - Par exemple, `/null,null,/` indique une zone vide de 2x2.
- **Porte-Monnaie du Joueur** : Un seul entier, comme `10`, représente l'argent du joueur.
- **Inventaire du Joueur** : Le format `0:20/1:20` signifie que le joueur possède 20 objets d'ID 0 et 20 objets d'ID 1.
- **Usine** : `0:0,0:100,NULL:1/XL:3/,0` Une usine située à `(0,0)` avec 100 objet de type 0, aillant pour amèlioration
  XL*3 (et NULL valeur par défaut) avec la recette 0.
- **collecteur** : Exemple `1:0,0:20/,Null:1/,1` signifie une moissonneuse à la position `(1,0)` avec 20 objets de type
  0 , aucune amélioration et la ressource associé est celle d'ID 1

---

### **Résumé**

Ce format de sauvegarde est conçu pour suivre différents éléments du jeu, tels que la progression du joueur (argent et
objets), les usines (position, inventaire, améliorations, recettes) et les moissonneuses (position, inventaire,
ressources). Chaque élément est stocké de manière structurée pour une lecture et un traitement faciles lors de la
sauvegarde et du chargement du jeu.

---
