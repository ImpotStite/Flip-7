# 📋 Scénarios de Tests Fonctionnels (Séquences)

Ce document propose plusieurs scénarios de test pour notre jeu, sous la forme d'une structure Gherkin. L'idée de cette structure a été fournie par une intelligence artificielle (Gemini 3.5 Flash), et nous l'avons retenue. 

---

### 🔹 SCÉNARIO 1 : Déroulement nominal d'une manche

**Contexte initial :**
* **Nombre de joueurs :** 3
* **Scores historiques :** `joueur0: 0`, `joueur1: 0`, `joueur2: 0` (Première manche)
* **États de départ :** Partie en `ATTENTE_CHOIX_JOUEUR` | Joueurs en `JOUE_ENCORE`

**Séquence de test :**
* Quand le joueur 0 pioche une CarteNum(5)
* Et que le joueur 1 pioche une CarteNum(12)
* Et que le joueur 2 pioche une CarteNum(10)
* Et que le joueur 0 pioche une CarteNum(12)
* Et que le joueur 1 pioche une CarteNum(4)
* Et que le joueur 2 pioche une CarteBonusMultiple()
* Et que le joueur 0 dit STOP
* Et que le joueur 1 dit STOP
* Et que le joueur 2 dit STOP
* Alors l'état de la partie doit basculer en MANCHE_TERMINEE
* Quand on appelle la méthode scoreManche() 
* Alors le joueur 0 doit marquer 17 points
* Et le joueur 1 doit marquer 16 points
* Et le joueur 2 doit marquer 20 points
* Et l'état de la partie doit devenir NOUVELLE_MANCHE
* Et la taillePioche doit avoir diminué de 6
* Et la taille de la défausse doit avoir augmenté de 6

---

### 🔹 SCÉNARIO 2 : Un joueur atteint scoreFinPartie

**Contexte initial :**
* **Nombre de joueurs :** 2
* **Condition de victoire :** `scoreFinPartie = 200`
* **Scores historiques :** `joueur0: 161`, `joueur1: 175`
* **États de départ :** Partie en `ATTENTE_CHOIX_JOUEUR` | Joueurs en `JOUE_ENCORE`

**Séquence de test :**
* Quand le joueur 0 pioche une CarteBonusMultiple()
* Et que le joueur 1 pioche une CarteNum(12)
* Et que le joueur 0 pioche une CarteNum(8)
* Et que le joueur 1 pioche une CarteNum(11)
* Et que le joueur 0 pioche une CarteNum(6)
* Et que le joueur 1 pioche une CarteBonusPlus(4)
* Et que le joueur 0 dit STOP
* Et que le joueur 1 dit STOP
* Alors l'état de la partie doit basculer en MANCHE_TERMINEE
* Quand on appelle la méthode scoreManche() 
* Alors le joueur 0 doit marquer 14 points
* Et le joueur 1 doit marquer 27 points
* Et la taillePioche doit avoir diminué de 6
* Et la taille de la defausse doit avoir augmenté de 6
* Et l'état de la partie doit basculer en PARTIE_TERMINEE

---

### 🔹 SCÉNARIO 3 : Un joueur pioche une CarteStop()

**Contexte initial :**
* **Nombre de joueurs :** 2
* **Condition de victoire :** `scoreFinPartie = 100`
* **Scores historiques :** `joueur0: 20`, `joueur1: 21`
* **États de départ :** Partie en `ATTENTE_CHOIX_JOUEUR` | Joueurs en `JOUE_ENCORE`

**Séquence de test :**
* Quand le joueur 1 pioche une CarteBonusMultiple()
* Et que le joueur 0 pioche une CarteNum(1)
* Et que le joueur 1 pioche une CarteNum(9)
* Et que le joueur 0 pioche une CarteNum(12)
* Et que le joueur 1 pioche une CarteStop()
* Alors l'état de la partie doit basculer en ATTENTE_CIBLE_STOP
* Quand la méthode joueurCourantCibleStop(carteStop, 0) est appelée
* Alors le joueur 0 (cible) doit passer à l'état STOP
* Et le joueur 1 doit rester en état JOUE_ENCORE
* Et l'état de la partie doit basculer en ATTENTE_CHOIX_JOUEUR
* Et la taillePioche doit avoir diminué de 3

---

### 🔹 SCÉNARIO 4 : Un joueur pioche une Carte3aLaSuite()

**Contexte initial :**
* **Nombre de joueurs :** 4
* **Scores historiques :** Tous à 0 (Première manche)
* **États de départ :** Partie en `ATTENTE_CHOIX_JOUEUR` | Joueurs en `JOUE_ENCORE`
* **Joueur courant :** `joueur0`
* **État de la pioche :** Contient exactement 94 cartes

**Séquence de test :**
* Quand le joueur 0 pioche une Carte3aLaSuite()
* Alors l'état de la partie doit basculer en ATTENTE_CIBLE_3SUITE 
* Quand la méthode joueurCourantCible3aLaSuite(carte, 1) est appelée pour cibler le joueur 1
* Et que le joueur 1 pioche dans l'ordre : [CarteNum(10), CarteNum(2), CarteStop(), CarteBonusMultiple()]
* Alors la CarteStop() doit être ignorée par l'effet de la carte
* Et les trois autres cartes doivent être ajoutées à la main du joueur 1
* Et l'état de la partie doit basculer en ATTENTE_CHOIX_JOUEUR
* Et la taillePioche doit être égale à 89

---

### 🔹 SCÉNARIO 5 : Un joueur pioche une Carte2ndeChance()

**Contexte initial :**
* **Nombre de joueurs :** 2
* **Scores historiques :** Tous à 0 (Première manche)
* **États de départ :** Partie en `ATTENTE_CHOIX_JOUEUR` | Joueurs en `JOUE_ENCORE`
* **Joueur courant :** `joueur1`
* **État de la pioche :** Contient exactement 94 cartes

**Séquence de test :**
* Quand le joueur 1 pioche une Carte2ndeChance()
* Et que le joueur 0 pioche une CarteNum(1)
* Et que le joueur 1 pioche une CarteNum(9)
* Et que le joueur 0 pioche une CarteNum(10)
* Et que le joueur 1 pioche une CarteNum(9)
* Alors le doublon est sauvé par la Carte2ndeChance() et le joueur continue de jouer
* Et le joueur 1 doit rester en état JOUE_ENCORE
* Et la taillePioche doit avoir diminué de 5
* Et la taille de la defausse doit avoir augmenté de 2

---

### 🔹 SCÉNARIO 6 : Partie à 4 joueurs dans laquelle 2 joueurs obtiennent un doublon

**Contexte initial :**
* **Nombre de joueurs :** 4
* **Scores historiques :** Tous à 0 (Première manche)
* **États de départ :** Partie en `ATTENTE_CHOIX_JOUEUR` | Joueurs en `JOUE_ENCORE`
* **Joueur courant :** `joueur3`
* **État de la pioche :** Contient exactement 94 cartes

**Séquence de test :**
* Quand le joueur 3 pioche une CarteNum(5)
* Et que le joueur 0 pioche une CarteNum(12)
* Et que le joueur 1 pioche une CarteNum(7)
* Et que le joueur 2 pioche une CarteNum(4)
* Et que le joueur 3 pioche une CarteNum(9)
* Et que le joueur 0 pioche une CarteNum(12)
* Alors le joueur 0 passe en `Perdu`
* Et que le joueur 1 dit STOP
* Alors le joueur 1 passe en `STOP`
* Et que le joueur 2 pioche une CarteBonusMultiple()
* Et que le joueur 3 pioche une CarteNum(9)
* Alors le joueur 3 passe en `Perdu`
* Alors le joueur 2 passe en `STOP`
* Alors l'état de la partie doit basculer en MANCHE_TERMINEE
* Quand on appelle la méthode scoreManche()
* Alors le joueur 3 doit marquer 0 points
* Et le joueur 0 doit marquer 0 points
* Et le joueur 1 doit marquer 7 points
* Et le joueur 2 doit marquer 8 points
* Et la taillePioche doit avoir diminué de 8
* Et la taille de la defausse doit avoir augmenté de 8

---




















