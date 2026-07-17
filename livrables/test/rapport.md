# Analyse de la testabilité — Bibliothèque Flip7

## 1. Présentation des classes et de leurs responsabilités

### Classe `Flip7`

`Flip7` est la classe principale et le contrôleur du jeu de cartes.

**Rôle et responsabilités :** elle gère le déroulement d'une partie complète entre plusieurs joueurs (2 à 4),
met à jour les états des joueurs et des manches, et gère la distribution des cartes ainsi que leurs effets.

**Fonctionnalités :** elle fournit les opérations d'interaction majeures : faire piocher le joueur courant,
appliquer les cartes spéciales ciblées (`CarteStop`, `Carte3aLaSuite`), enregistrer un arrêt volontaire,
comptabiliser les scores de fin de manche et initialiser une nouvelle manche.

### Classe `OutilsCarte`

`OutilsCarte` est une interface sans état qui regroupe les règles du jeu appliquées aux différentes cartes du jeu.
Elle possède quatre méthodes : `verifiePiocheInitiale`, `verifieMainCorrecte`, `calculScore` et `estFlip7` qui seront
utilisées par la classe Flip7 pour vérifier différents états lors d'une partie.

### Classe de cartes

La classe carte possède toutes les cartes du jeu et sa hiérarchie de cartes (`CarteNum`, `CarteBonusPlus`, `CarteBonusMultiplie`, `CarteStop`,
`Carte2ndeChance`, `Carte3aLaSuite`) représente les entités du domaine. Chaque classe concrète
possède `equals()`, `toString()` et une méthode `estCarteXxx()` permettant d'identifier son type.

---

## 2. Heuristiques principales

### A. Observabilité

L'observabilité désigne la capacité à inspecter et vérifier facilement l'état interne d'un logiciel
après une exécution.

**Points forts :**
Les attributs fondamentaux de `Flip7` (`etatPartie`, `etatJoueur`, `joueurCourant`, `taillePioche`,
`score`, `main`, `defausse`) sont tous déclarés `{readOnly}`, ce qui donne un accès direct aux variables.
L'état interne de la partie est donc observable en continu,
ce qui facilite l'écriture d'oracles de tests plus compréhensibles.

Les méthodes de `OutilsCarte` retournent toutes des valeurs exploitables directement en lien avec les cartes :
`calculScore` retourne le score d'un joueur en `Int`, `estFlip7` qui vérifie si le joueur en obtient un
et retourne donc un `Boolean`.

### B. Contrôlabilité

La contrôlabilité est la capacité à forcer le logiciel dans un état précis pour exécuter un scénario défini.

**Bonne contrôlabilité des entrées :**
Le constructeur de `Flip7` accepte directement en paramètre une liste
que l'on peut préétablir d'objets `Carte` représentant la pioche
(`cartes : List<Carte>`). Cela élimine donc complètement
l'aléa lors d'un mélange de cartes non maîtrisé
lors de l'exécution des tests. Il devient alors très simple
de planifier l'ordre des pioches pour forcer le passage par
des chemins spécifiques notamment pour tester certains
(ex: piocher un doublon précis ou une carte spéciale). De plus
avec l'attribut debug créer une pioche personnalisée prend moins de temps.


L'interface `IOutilsCarte` est injectée via le constructeur, ce qui permet
de substituer un mock pour tester `Flip7` indépendamment de `OutilsCarte`.

Les classes de cartes concrètes sont instanciables directement et simplement
(exemple : `CarteNum(5)`, `CarteBonusPlus(4)`, `CarteStop()`, etc). Les mains et la pioche sont donc
entièrement maîtrisées dans les tests ce qui permet d'avoir un contrôle sur les événements que l'on veut
déclencher ou non pour tester un scénario précis.


---

## 3. Heuristiques secondaires

### A. Disponibilité

La spécification est explicite et disponible grâce au diagramme de classes et aux diagrammes d'états
de la partie et du joueur qui sont fournis. De plus toutes les spécifications sont présentes
dans une documentation externe. Ce qui facilite la compréhension des règles qui ont
pourtant été personnalisées.

### B. Stabilité

L'architecture logicielle est découpée de manière à ce que les différents éléments du programme soient séparés
en packages hermétiques(`etats`, `exceptions`, `cartes`). Cette organisation du projet garantit que toute modification
du code ou correction appliquée à une règle sur les types de cartes ne posera pas de problème à
la structure de gestion de la partie dans `Flip7`, ce qui assure ainsi un bon suivi évolutif de la suite de tests.

---

## 4. Synthèse

| Heuristique    | Niveau         | Remarque principale                                                     |
|----------------|----------------|-------------------------------------------------------------------------|
| Observabilité  | Bonne          | Propriétés `readOnly` rendent les variables accessibles directement     |
| Contrôlabilité | Bonne          | Pioche injectée donne un déterminisme total                             |
| Disponibilité  | Acceptable     | Spécifications claires grâce au diagramme et à la documentation externe |
| Stabilité      | Bonne          | Le projet est stable grâce à la séparation des éléments                 |

L'analyse globale montre que l'architecture du jeu Flip7 est bien réalisée pour permettre une modification et
une réalisation de tests plus compréhensible. L'utilisation d'abstractions (interfaces `IJoueur` et `IOutilsCarte`),
l'accessibilité directe à l'intégralité des variables d'état, la hiérarchisation des paquetages d'exceptions et
la contrôlabilité absolue de la pioche par injection de données éliminent tous les possibles événements aléatoires
ce qui peut freiner la testabilité.





