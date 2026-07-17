# Explication des différentes vues

## Ecran d'accueil :

Première fenêtre de l’application.
Deux boutons cliquables reliés au modèle via des contrôleurs.
Les boutons s’assombrissent au clic.

![Écran d'accueil](Ecran%20d'accueil.png)


## Ecran selection joueurs - 2 joueurs:

Seconde fenêtre de l’application.
Cinq boutons. Selon le nombre de joueurs, redirection vers un Pane() adapté à ce dernier.
Possibilité d’entrer son nom, qui sera transmis au modèle (via contrôleur) et au constructeur de chaque Joueur(nom : String) : IJoueur.

![Écran sélection 2 joueurs](Ecran%20séléction%202%20joueurs.png)


## Ecran selection joueurs - 3 joueurs / 4 joueurs :

Voici comment doit s'adapter notre écran en cas d'ajout de joueurs.

![Écran sélection 3 joueurs](Ecran%20séléction%203%20joueurs.png)
![Écran sélection 4 joueurs](Ecran%20séléction%204%20joueurs.png)



## Vue du jeu - 2 joueurs :

Cet écran de jeu sera constitué d'un BorderPane subdivisé en deux autres Pane (left/center).
La "table" sera représentée par une forme rectangulaire sur laquelle seront inscrites les instructions en cours.
Le joueur choisit de piocher une carte en cliquant qsur la pioche, et de s'arreter en cliquant sur le bouton STOP.

![Vue du jeu 2 joueurs](Vue%20du%20jeu%20-%202%20joueurs.png)


## Vue du jeu - 3 joueurs :

Cet écran de jeu sera constitué d'un BorderPane subdivisé en deux autres Pane (left/center).
La "table" sera représentée par une forme triangulaire sur laquelle seront inscrites les instructions en cours.
Le joueur choisit de piocher une carte en cliquant qsur la pioche, et de s'arreter en cliquant sur le bouton STOP.

![Vue du jeu 3 joueurs](Vue%20du%20jeu%20-%203%20joueurs.png)

## Vue du jeu - 4 joueurs :

Cet écran de jeu sera constitué d'un BorderPane subdivisé en deux autres Pane (left/center).
La "table" sera représentée par une forme carrée sur laquelle seront inscrites les instructions en cours.
 Le joueur choisit de piocher une carte en cliquant qsur la pioche, et de s'arreter en cliquant sur le bouton STOP.

![Vue du jeu 4 joueurs](Vue%20du%20jeu%20-%204%20joueurs.png)

## Ecran de jeu - Carte STOP :

La pioche d'une Carte STOP ajoute un calque bleuté sur notre fenêtre de jeu. On peut choisir de stopper l'un des joueurs présents, y compris nous-mêmes.
On peut choisir de stopper un joueur en cliquant sur le bouton à côté de lui, puis la partie reprend.

![Carte STOP activée](Carte%20STOP%20activée.png)

## Ecran de jeu - Carte 3 à la suite :

La pioche d'une Carte 3aLaSuite ajoute un calque jaune sur notre fenêtre de jeu. On peut choisir de stopper l'un des joueurs présents, y compris nous-mêmes.
On peut choisir de stopper un joueur en cliquant sur le bouton à côté de lui, puis la partie reprend.

![Carte 3 à la suite activée](Carte%203aLaSuite%20activée.png)

## Ecran de jeu - Activation carte seconde chance :

La carte 2ndeChance, une fois piochée, reste dans la main sans rien déclencher. Si un doublon est pioché, elle s'active et jette la carte tout juste piochée ainsi qu'elle-même dans la défausse.

![Carte Seconde Chance activée](Carte%202ndeChance%20activée.png)

## Ecran final : fin de partie :

L'écran de fin de partie récupère dans le modèle les informations des joueurs en les triant par points décroissants.


![Fin de partie 2 joueurs](Ecran%20final%20fin%20de%20partie%202%20joueurs.png)
![Fin de partie 3 joueurs](Ecran%20final%20fin%20de%20partie%203%20joueurs.png)
![Fin de partie 4 joueurs](Ecran%20final%20fin%20de%20partie%204%20joueurs.png)