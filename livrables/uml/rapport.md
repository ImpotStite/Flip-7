**Explication des choix les plus déterminants :**

Nous avons implémentés une classe Gestionnaire de Partie qui va contenir les méthodes permettant de donner le "tempo" du déroulé de la partie. Cette classe a accès à la liste des joueurs, à la pioche et la défausse.

Le joueur possède la plupart des méthodes du jeu, il a été difficile pour certains méthodes de déterminer quelle classe devait la posseder. Nos choix 

Des méthodes privées sont présentes dans plusieurs classes. En effet, elles ne sont présentes que pour être appelées par d'autres méthodes de la classe (pour Joueur, choixAction() appele encore()).

Après un long débat entre les membres du groupe, nous avons finalement choisis de ne pas créer de classe Main, et d'inclure la main comme attribut du joueur. En effet, nous considérons qu'une main ne peut pas exister indépendamment du joueur qui la possède. 

Pour la taille de cette main, nous avons déterminés que les règles du jeu ne permettent pas d'accumuler plus de 15 cartes. 

Cette main est une MutableList de Carte, ce qui la rend flexible en terme de taille et autres manipulations.

Une carte possède un recto et un verso qui sont des images tirées de la librairie JavaFX, le verso a une valeur par défaut car ce sera le même pour toutes les cartes. 

Pour les cartes de type Speciale, une méthode abstraite -effet() ne peut être définie en elle-même, mais est requise pour les classes-filles. 

Enfin, pour la classe Défausse, la méthode jeterCartes() pourrait sembler redondante avec la méthode viderMain() de Joueur, mais jeterCartes() prendra en argument ce que renvoie viderMain