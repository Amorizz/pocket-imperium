package Project.Console;

import java.util.*;

public class Plateau {

    private HashMap<String, ArrayList<SectorCard>> jeux;

    public Plateau() {
        this.jeux = new HashMap<>();
        initialiserPlateau();
    }

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

    public HashMap<String, ArrayList<SectorCard>> getPlateau() {
        return this.jeux;
    }

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

    public void checkPlateau() {
        System.out.println("Vérification du plateau en cours...");

        for (String niveau : jeux.keySet()) {
            ArrayList<SectorCard> sectors = jeux.get(niveau);
            for (SectorCard sector : sectors) {
                HashMap<Integer, Hex> hexagones = (HashMap<Integer, Hex>) sector.getHex(); // Obtenir tous les hexagones du secteur
                for (Hex hex : hexagones.values()) {
                    if (!hex.getOccupation().isEmpty()) {
                        // Vérifier si plusieurs joueurs occupent l'hexagone
                        if (hex.getOccupation().size() > 1) {
                            System.out.println("Conflit détecté dans l'hexagone " + hex);
                            resolveConflict(hex);
                        }

                        // Ajuster le nombre de vaisseaux pour respecter la capacité maximale
                        Player occupant = hex.getOccupation().keySet().iterator().next(); // Obtenir le seul occupant
                        int ships = hex.getOccupation().get(occupant);
                        if (ships > hex.getMaxshipon()) {
                            hex.getOccupation().put(occupant, hex.getMaxshipon()); // Limiter au maximum permis
                            System.out.println("Nombre de vaisseaux ajusté pour " + occupant.getPlayerName() + " dans l'hexagone " + hex);
                        }
                    }
                }
            }
        }
        System.out.println("Vérification du plateau terminée.");
    }

    private void resolveConflict(Hex hex) {
        // Trouver le joueur avec le plus de vaisseaux
        Map<Player, Integer> occupation = hex.getOccupation();
        Player winner = null;
        int maxShips = 0;

        for (Map.Entry<Player, Integer> entry : occupation.entrySet()) {
            if (entry.getValue() > maxShips) {
                winner = entry.getKey();
                maxShips = entry.getValue();
            }
        }

        // Conserver uniquement le joueur vainqueur
        if (winner != null) {
            hex.clearAllOccupation(); // Vider l'hexagone
            hex.addShipsPlayer(winner, Math.min(maxShips, hex.getMaxshipon())); // Ajouter les vaisseaux du vainqueur
            System.out.println("Conflit résolu dans l'hexagone " + hex + ". Le vainqueur est " + winner.getPlayerName());
        }
    }

    public void calculateScore(List<Player> players) {
        System.out.println("Calcul des scores pour le plateau...");

        // Réinitialiser les scores de chaque joueur avant le calcul
        for (Player player : players) {
            player.resetPoints(); // Supposons que cette méthode remet les points du joueur à 0
        }

        for (String niveau : getPlateau().keySet()) {
            List<SectorCard> sectors = getPlateau().get(niveau);

            for (SectorCard sector : sectors) {
                Map<Integer, Hex> hexes = sector.getHex();

                for (Hex hex : hexes.values()) {
                    // Identifier le joueur dominant sur cet hexagone
                    Player dominantPlayer = null;
                    int maxShips = 0;

                    for (Map.Entry<Player, Integer> entry : hex.getOccupation().entrySet()) {
                        int ships = entry.getValue();

                        if (ships > maxShips) {
                            dominantPlayer = entry.getKey();
                            maxShips = ships;
                        } else if (ships == maxShips) {
                            dominantPlayer = null; // Égalité, pas de joueur dominant
                        }
                    }

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
                        System.out.println("Aucun joueur ne gagne de points ");
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