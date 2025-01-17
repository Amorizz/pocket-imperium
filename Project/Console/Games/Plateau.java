package Project.Console.Games;

import Project.Console.Card.SectorCard;
import Project.Console.Entity.Player;
import Project.Console.Entity.Ship;

import java.util.*;

/**
 * La classe {@code Plateau} représente le plateau de jeu de Pocket Imperium.
 * Elle contient les secteurs et les hexagones, et gère les vérifications du plateau,
 * les conflits, ainsi que le calcul des scores.
 */
public class Plateau {

    private HashMap<String, ArrayList<SectorCard>> jeux; // Structure contenant les secteurs organisés par niveaux (Top, Mid, Bottom)

    /**
     * Constructeur par défaut de la classe {@code Plateau}.
     * Initialise le plateau avec des secteurs et leurs hexagones.
     */
    public Plateau() {
        this.jeux = new HashMap<>();
        initialiserPlateau();
    }

    /**
     * Initialise le plateau en créant les secteurs et les hexagones associés
     * pour les niveaux Top, Mid et Bottom.
     */
    private void initialiserPlateau() {
        // Création Carte Top
        ArrayList<SectorCard> top = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SectorCard carteTop = new SectorCard(i+1, false, false, true);
            top.add(carteTop);
        }
        jeux.put("Top", top);

        // Création Carte Mid
        ArrayList<SectorCard> mid = new ArrayList<>();
        mid.add(new SectorCard(1, false, false, false)); // Carte Mid 1
        mid.add(new SectorCard(2, true, false, false));  // Carte TriPrime
        mid.add(new SectorCard(3, false, false, false)); // Carte Mid 2
        jeux.put("Mid", mid);

        // Création Carte Bottom
        ArrayList<SectorCard> bottom = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SectorCard carteBottom = new SectorCard(i+1, false, true, false);
            bottom.add(carteBottom);
        }
        jeux.put("Bottom", bottom);
    }

    /**
     * Retourne la structure contenant les secteurs du plateau.
     *
     * @return un {@code HashMap} associant les niveaux à leurs listes de secteurs.
     */
    public HashMap<String, ArrayList<SectorCard>> getPlateau() {
        return this.jeux;
    }

    /**
     * Affiche une représentation visuelle du plateau dans la console.
     */
    public void afficherPlateau(){
        System.out.println(""+
                "  [ 1] [ 2] #  [ 8] [ 9] #  [15] [16]"+"\n"+
                "[ 3][ 4][ 5]#[10][11][12]#[17][18][19]"+"\n"+
                "  [ 6] [ 7] #  [13] [14] #  [20] [21]"+"\n"+
                "######################################"+"\n"+
                "[22][23][24]#[30]   [31]#[35][36][37]"+"\n"+
                "  [25] [26] #    [32]   #  [   38] [39]"+"\n"+
                "[27][28][29]#[33]   [34]#[40][41][42]"+"\n"+
                "######################################"+"\n"+
                "  [43] [44] #  [50] [51] #  [57] [58]"+"\n"+
                "[45][46][47]#[52][53][54]#[59][60][61]"+"\n"+
                "  [48] [49] #  [55] [56] #  [62] [63]"
        );
    }

    /**
     * Vérifie les règles et les conflits sur le plateau de jeu.
     * - Résout les conflits dans les hexagones où plusieurs joueurs occupent le même espace.
     * - Ajuste le nombre de vaisseaux pour respecter la capacité maximale des hexagones.
     */
    public void checkPlateau() {
        System.out.println("Vérification du plateau en cours...");

        for (String niveau : jeux.keySet()) {
            ArrayList<SectorCard> sectors = jeux.get(niveau);
            for (SectorCard sector : sectors) {
                HashMap<Integer, Hex> hexagones = (HashMap<Integer, Hex>) sector.getHex(); // Obtenir tous les hexagones du secteur
                for (Hex hex : hexagones.values()) {
                    ArrayList<Ship> ships = hex.getOccupation();

                    if (!ships.isEmpty()) {
                        // Vérifier si plusieurs joueurs occupent l'hexagone
                        Set<Player> players = new HashSet<>();
                        for (Ship ship : ships) {
                            players.add(ship.getPlayerName());
                        }

                        if (players.size() > 1) {
                            System.out.println("Conflit détecté dans l'hexagone " + hex);
                            resolveConflict(hex);
                        }

                        // Ajuster le nombre de vaisseaux pour chaque joueur pour respecter la capacité maximale
                        for (Ship ship : ships) {
                            if (ship.getNbrShipy() > hex.getMaxshipon()) {
                                ship.setQuantity(hex.getMaxshipon()); // Limiter au maximum permis
                                System.out.println("Nombre de vaisseaux ajusté pour " + ship.getPlayerName().getPlayerName() + " dans l'hexagone " + hex);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Vérification du plateau terminée.");
    }


    /**
     * Résout un conflit dans un hexagone en conservant uniquement le joueur
     * ayant le plus de vaisseaux.
     *
     * @param hex l'hexagone où le conflit doit être résolu.
     */
    private void resolveConflict(Hex hex) {
        // Trouver le joueur avec le plus de vaisseaux
        List<Ship> ships = hex.getOccupation();
        Player winner = null;
        int maxShips = 0;

        // Calculer le joueur avec le maximum de vaisseaux
        Map<Player, Integer> shipCounts = new HashMap<>();
        for (Ship ship : ships) {
            Player owner = ship.getPlayerName();
            shipCounts.put(owner, shipCounts.getOrDefault(owner, 0) + ship.getNbrShipy());
        }

        for (Map.Entry<Player, Integer> entry : shipCounts.entrySet()) {
            if (entry.getValue() > maxShips) {
                winner = entry.getKey();
                maxShips = entry.getValue();
            } else if (entry.getValue() == maxShips) {
                winner = null; // Égalité, aucun vainqueur clair
            }
        }

        // Résoudre le conflit
        if (winner != null) {
            hex.clearAllOccupation(); // Vider tous les vaisseaux de l'hexagone
            hex.addShip(winner, Math.min(maxShips, hex.getMaxshipon())); // Ajouter les vaisseaux du vainqueur
            System.out.println(
                    "Conflit résolu dans l'hexagone " + hex + ". Le vainqueur est " + winner.getPlayerName()
            );
        } else {
            System.out.println(
                    "Conflit non résolu dans l'hexagone " + hex + ". Égalité entre plusieurs joueurs."
            );
            hex.clearAllOccupation(); // En cas d'égalité, retirer tous les vaisseaux
        }
    }

    /**
     * Calcule les scores des joueurs en fonction de leur domination sur les hexagones.
     * Les joueurs dominants marquent :
     * - 1 point pour chaque hexagone contrôlé.
     * - 2 points supplémentaires pour les hexagones stratégiques (niveau 3).
     *
     * @param players la liste des joueurs participant au jeu.
     */
    public void calculateScore(List<Player> players) {
        System.out.println("Calcul des scores pour le plateau...");

        // Réinitialiser les scores de chaque joueur avant le calcul
        for (Player player : players) {
            player.resetPoints();
        }

        // Parcourir chaque niveau du plateau
        for (String niveau : getPlateau().keySet()) {
            List<SectorCard> sectors = getPlateau().get(niveau);

            for (SectorCard sector : sectors) {
                Map<Integer, Hex> hexes = sector.getHex();

                for (Hex hex : hexes.values()) {
                    // Identifier le joueur dominant sur cet hexagone
                    Player dominantPlayer = null;
                    int maxShips = 0;

                    // Compter le nombre total de vaisseaux pour chaque joueur
                    Map<Player, Integer> shipCounts = new HashMap<>();
                    for (Ship ship : hex.getOccupation()) {
                        Player owner = ship.getPlayerName();
                        shipCounts.put(owner, shipCounts.getOrDefault(owner, 0) + ship.getNbrShipy());
                    }

                    // Déterminer le joueur dominant
                    for (Map.Entry<Player, Integer> entry : shipCounts.entrySet()) {
                        int ships = entry.getValue();

                        if (ships > maxShips) {
                            dominantPlayer = entry.getKey();
                            maxShips = ships;
                        } else if (ships == maxShips) {
                            dominantPlayer = null; // Égalité, pas de joueur dominant
                        }
                    }

                    // Attribuer des points au joueur dominant
                    if (dominantPlayer != null) {
                        // 1 point pour contrôler un hexagone
                        dominantPlayer.addPoints(1);

                        // Bonus pour les hexagones stratégiques (niveau 3)
                        if (hex.getLevel() == 3) {
                            dominantPlayer.addPoints(2);
                        }

                        System.out.println(dominantPlayer.getPlayerName() + " gagne " +
                                (hex.getLevel() == 3 ? "3" : "1") + " points");
                    } else {
                        System.out.println("Aucun joueur ne gagne de points pour l'hexagone : " + hex);
                    }
                }
            }
        }

        // Afficher les scores finaux
        System.out.println("\nScores finaux :");
        for (Player player : players) {
            System.out.println(player.getPlayerName() + " : " + player.getPoints() + " points.");
        }
    }
}