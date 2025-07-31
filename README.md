
# Lancement du projet
## Linux 
```bash
make run
```
## Windows 
### vue un peu déformée
```bash 
bash runnable.sh run
```

# Documentation  

## Documentation  
### Documentation Javadoc :  
- [doc.md](./documentation/index.html)  
si vous avez besoin de re-generer la javadoc 
```bash
javadoc -d documentation -quiet -sourcepath forgefactory/src -subpackages main.java -Xdoclint:none --enable-preview --release 21
```

### Compte rendu 

La description de notre implémentation et de la manière dont nous avons rempli le cahier des charges.  
Les informations sont regroupées par sujet (par exemple, tout ce qui concerne la sauvegarde est regroupé, tout ce qui concerne les machines également, etc.).  
L'ordre des informations ne correspond pas nécessairement à celui du sujet initial.  

#### Compte rendu Markdown :  
- [Compte rendu Markdown](./compte%20rendu/compte%20rendu.md)  

#### Compte rendu PDF :  
- [Compte rendu PDF](./compte%20rendu/compte%20rendu.pdf)  

---

## Diagramme de classe  
### Diagramme des attributs, héritage, méthodes  
- [Diagramme 1](./diagramme/Diagramme%20des%20classes.pdf)  

### Diagramme des interactions  
Ce diagramme est moins complet sur les attributs, mais il décrit toutes les interactions.  
En raison de ses dimensions (plus de 10 000 x 10 000 pixels), nous n'avons pas pu le transformer en PDF tout en conservant un texte lisible.  
- [Diagramme 2](./diagramme/diagrameInteraction.png)  

---

# Informations sur le dépôt  

## Contribution  
- **Valentin** : De nombreux commits, mais certains contiennent des modifications très mineures (maximum 10 lignes), tandis que d'autres apportent beaucoup de contenu.  
- **Vincent** : Moins de commits, mais ces derniers contiennent généralement beaucoup de contenu.  

## Organisation et Méthodologie de Travail

### Relecture Mutuelle
À chaque grand changement, l'autre vérifiait le code, la lisibilité et testait le jeu. Il devait être capable de comprendre comment les interactions fonctionnaient et d'analyser le code pour s'assurer de sa qualité.

### Réunion Hebdomadaire
Tous les mercredis midi, nous nous concertions sur la direction à prendre pour le projet, afin de rester alignés sur nos objectifs et nos priorités.

### Partage des Tâches
Nous avons tous les deux énormément travaillé sur la base du projet, notamment sur les grandes classes. En complément, Valentin s'est beaucoup occupé de la relecture et des classes moins importantes, pour s'assurer de leur cohérence et de leur bonne intégration. Tandis que Vincent à beaucoup travailler sur la documentation et fait les diagrammes de classe même si Valentin qui les a push. (effectuer sur [miro](https://miro.com) et à l'aide de intelij idea)

## problèmes connues 
Parfois, l'affichage des scroll pane bug un peu. 
Il est arrivé que l'icone du marché ne s'affiche pas correctement ou que la dernière colonne soit un peu troncaturé. (il faut glisser horizotalement pour tout voir)
Le chargement de map est devenu lent au commit 8c523abe85add44886acece8084fb93676488632 sans avoir toucher à cette partie<br>
Plus d'information dans le [compte rendu](./compte%20rendu/compte%20rendu.pdf)

---

# Historique  
Nous avons importé un autre dépôt privé GitHub nous appartenant pour le transférer sur le GitLab de la faculté, conformément aux conditions de rendu demandées.  
Nous avons remarqué quelques décalages sur les dates des premiers commits du dépôt.  

---

## Auteurs  
### Groupe 16  
- **Valentin MOUCHES** (MI2)  
- **Vincent SCHOONHEERE** (MI2)  
