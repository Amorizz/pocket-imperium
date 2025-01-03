package Project.GUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La classe {@code Plateau} représente le plateau de jeu de Pocket Imperium.
 * Elle contient les secteurs et les hexagones, et gère les vérifications du plateau,
 * les conflits, ainsi que le calcul des scores.
 */
public class Plateau {

    private HashMap<String, ArrayList<SectorCard>> jeux;

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
     * Affiche le plateau de jeu dans la console.
     *
     * @param console l'interface graphique de la console.
     */
    public void afficherPlateau(ConsoleGUI console) {
         console.println(""+
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
     * Vérifie le plateau de jeu pour résoudre les conflits et ajuster les vaisseaux.
     *
     * @param console l'interface graphique de la console.
     */
    public void checkPlateau(ConsoleGUI console) {
        console.println("Vérification du plateau en cours...");

        for (String niveau : jeux.keySet()) {
            ArrayList<SectorCard> sectors = jeux.get(niveau);
            for (SectorCard sector : sectors) {
                HashMap<Integer, Hex> hexagones = (HashMap<Integer, Hex>) sector.getHex(); // Obtenir tous les hexagones du secteur
                for (Hex hex : hexagones.values()) {
                    int hexId = hex.getId(); // Obtenir l'ID de l'hexagone

                    if (!hex.getOccupation().isEmpty()) {
                        // Vérifier si plusieurs joueurs occupent l'hexagone
                        if (hex.getOccupation().size() > 1) {
                            console.println("Conflit détecté dans l'hexagone " + hexId);
                            resolveConflict(hex);
                        }

                        // Ajuster le nombre de vaisseaux pour respecter la capacité maximale
                        for (Ship ship : hex.getOccupation()) {
                            Player occupant = ship.getPlayerName();
                            int ships = ship.getNbrShipy();
                            if (ships > hex.getMaxshipon()) {
                                hex.clearAllOccupation(); // Vider l'hexagone
                                hex.addShip(occupant, hex.getMaxshipon()); // Limiter au maximum permis
                                console.println("Nombre de vaisseaux ajusté pour " + occupant.getPlayerName() + " dans l'hexagone " + hexId);
                            }
                        }
                    }

                    // Mettre à jour la carte graphique avec les bateaux restants
                    List<Color> updatedColors = new ArrayList<>();
                    for (Ship ship : hex.getOccupation()) {
                        Player occupant = ship.getPlayerName();
                        int ships = ship.getNbrShipy();
                        for (int i = 0; i < ships; i++) {
                            updatedColors.add(console.getColorFromName(occupant.getColor())); // Convertir la couleur en `Color`
                        }
                    }
                    console.updateHexShips(hexId, updatedColors); // Mettre à jour l'affichage des bateaux
                }
            }
        }

        console.println("Vérification du plateau terminée.");
    }

    /**
     * Attribue des identifiants uniques à chaque hexagone du plateau.
     */
    public void assignHexIds() {
        int idCounter = 1; // Compteur pour les identifiants
        for (String niveau : jeux.keySet()) {
            for (SectorCard sector : jeux.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    hex.setId(idCounter++); // Attribuer un identifiant unique
                }
            }
        }
    }

    /**
     * Retourne le niveau d'un hexagone à partir de son identifiant.
     *
     * @param hexId l'identifiant de l'hexagone.
     * @return le niveau de l'hexagone.
     */
    public int getLevel(int hexId) {
        for (String niveau : jeux.keySet()) {
            for (SectorCard sector : jeux.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getId() == hexId) {
                        return hex.getLevel(); // Retourne le niveau de l'hexagone
                    }
                }
            }
        }
        return 0; // Si aucun niveau trouvé
    }

    /**
     * Résout un conflit dans un hexagone en conservant uniquement le joueur
     * ayant le plus de vaisseaux.
     *
     * @param hex l'hexagone où le conflit doit être résolu.
     */
    private void resolveConflict(Hex hex) {
        // Trouver le joueur avec le plus de vaisseaux
        ArrayList<Ship> occupation = hex.getOccupation();
        Player winner = null;
        int maxShips = 0;

        for (Ship ship : occupation) {
            if (ship.getNbrShipy() > maxShips) {
                winner = ship.getPlayerName();
                maxShips = ship.getNbrShipy();
            }
        }

        // Conserver uniquement le joueur vainqueur
        if (winner != null) {
            hex.clearAllOccupation(); // Vider l'hexagone
            hex.addShip(winner, Math.min(maxShips, hex.getMaxshipon())); // Ajouter les vaisseaux du vainqueur
            System.out.println("Conflit résolu dans l'hexagone " + hex + ". Le vainqueur est " + winner.getPlayerName());
        }
    }
}