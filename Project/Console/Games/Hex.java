package Project.Console.Games;

import Project.Console.Card.SectorCard;
import Project.Console.Entity.Player;
import Project.Console.Entity.Ship;

import java.util.*;

/**
 * La classe {@code Hex} représente une cellule hexagonale dans le jeu.
 * Chaque hexagone possède des coordonnées, un niveau, une capacité maximale de vaisseaux
 * et une carte des occupants (joueurs et nombre de vaisseaux qu'ils y possèdent).
 */
public class Hex {
    private int x; // Coordonnée X de l'hexagone
    private int y; // Coordonnée Y de l'hexagone
    private final int maxShipOn; // Capacité maximale de vaisseaux
    private final int level; // Niveau de l'hexagone
    private ArrayList<Ship> occupation; // Carte des occupants

    /**
     * Constructeur pour initialiser un hexagone avec un niveau donné.
     * La capacité maximale de vaisseaux est calculée en fonction du niveau.
     *
     * @param level le niveau de l'hexagone.
     */
    public Hex(int level) {
        this.level = level;
        this.maxShipOn = level + 1;
        this.occupation = new ArrayList<>() {
        };
    }

    /**
     * Définit les coordonnées de l'hexagone en fonction de son emplacement
     * et des règles spécifiques pour différentes régions du plateau.
     *
     * @param triPrime indique si l'hexagone appartient à la région TriPrime.
     * @param bottom   indique si l'hexagone appartient à la région basse du plateau.
     * @param top      indique si l'hexagone appartient à la région haute du plateau.
     * @param place    la position de l'hexagone dans sa région.
     * @param number   le numéro de la carte auquel appartient l'hexagone.
     */
    public void reLoc(boolean triPrime, boolean bottom, boolean top, int place, int number) {
        if (triPrime) {
            System.out.println("C'est le TriPri");
            // Gestion de la carte TriPrime (hexagones spécifiques)
            int baseX = 3;
            int baseY = 4;
            if (place == 3){
                this.x = baseX;
                this.y = baseY+1;
            } else if (place <= 2){
                this.x = baseX + (place - 1);
                this.y = baseY;
            } else {
                this.x = baseX + (place - 4);
                this.y = baseY + 2;
            }
        } else if (bottom || top) {
            // Gestion des cartes Top et Bottom
            int baseX = 1 + (number - 1) * 2; // Décalage horizontal pour chaque carte
            int baseY = top ? 1 : 7;          // Niveau de départ (7 pour top, 1 pour bottom)

            if (place < 2) { // Ligne 1 (2 hexagones)
                this.x = baseX + place;
                this.y = baseY;
            } else if (place < 5) { // Ligne 2 (3 hexagones)
                this.x = baseX + (place - 2);
                this.y = baseY + 1;
            } else { // Ligne 3 (2 hexagones)
                this.x = baseX + (place - 5);
                this.y = baseY + 2;
            }
        } else {
            // Gestion des cartes Mid
            int baseX = (number == 1) ? 1 : 4; // Décalage horizontal pour Mid 1 ou Mid 3
            int baseY = 4;                     // Niveau de départ pour Mid

            if (place < 3) { // Ligne 1 (3 hexagones)
                this.x = baseX + place;
                this.y = baseY;
            } else if (place < 5) { // Ligne 2 (2 hexagones)
                this.x = baseX + (place - 3);
                this.y = baseY + 1;
            } else { // Ligne 3 (3 hexagones)
                this.x = baseX + (place - 5);
                this.y = baseY + 2;
            }
        }

        System.out.println("Hex placé : x=" + this.x + ", y=" + this.y);
    }

    /**
     * Retourne la coordonnée X de l'hexagone.
     *
     * @return la coordonnée X.
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la coordonnée Y de l'hexagone.
     *
     * @return la coordonnée Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Retourne une représentation textuelle de l'hexagone, incluant ses coordonnées,
     * son niveau et les occupants (joueurs et leurs vaisseaux).
     *
     * @return une chaîne de caractères décrivant l'hexagone.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("X: " + this.x + " Y: " + this.y + " Level: " + this.level + " Occupants: ");
        for (Ship entry : occupation) {
            sb.append(entry.getPlayerName().getPlayerName()).append(": ").append(entry.getNbrShipy()).append(" ships, ");
        }
        return sb.toString();
    }

    /**
     * Retourne le nombre total de vaisseaux appartenant à un joueur spécifique
     * dans cet hexagone.
     *
     * @param player Le joueur dont on veut connaître le nombre de vaisseaux.
     * @return Le nombre de vaisseaux appartenant au joueur.
     */
    public int getShipCountForPlayer(Player player) {
        for (Ship ship : occupation) {
            if (ship.getPlayerName().equals(player)) {
                return ship.getNbrShipy();
            }
        }
        return 0;
    }

    /**
     * Supprime un certain nombre de vaisseaux pour un joueur spécifique dans cet hexagone.
     * Si le nombre de vaisseaux devient nul ou négatif, le joueur est retiré de l'occupation.
     *
     * @param player le joueur auquel les vaisseaux appartiennent.
     * @param number le nombre de vaisseaux à retirer.
     */
    public void removeShip(Player player, int number) {
        List<Ship> toRemove = new ArrayList<>();
        for (Ship ship : occupation) {
            if (ship.getPlayerName().equals(player)) {
                int remainingShips = ship.getNbrShipy() - number;
                if (remainingShips > 0) {
                    ship.setQuantity(remainingShips);
                } else {
                    toRemove.add(ship); // Ajouter à la liste des suppressions
                }
                break; // Quitter après modification
            }
        }
        occupation.removeAll(toRemove); // Supprimer après l'itération
    }

    /**
     * Ajoute un certain nombre de vaisseaux pour un joueur spécifique dans cet hexagone.
     *
     * @param player le joueur auquel les vaisseaux appartiennent.
     * @param count le nombre de vaisseaux à ajouter.
     */
    public void addShip(Player player, int count) {
        for (Ship ship : occupation) {
            if (ship.getPlayerName().equals(player)) {
                ship.addQuantity(count);
                return;
            }
        }
        occupation.add(new Ship(player, count));
    }


    /**
     * Supprime tous les occupants (joueurs et leurs vaisseaux) de cet hexagone.
     */
    public void clearAllOccupation() {
        this.occupation.clear();
    }

    /**
     * Retourne la capacité maximale de vaisseaux pour cet hexagone.
     *
     * @return le nombre maximal de vaisseaux autorisé.
     */
    public int getMaxshipon() {
        return maxShipOn;
    }

    /**
     * Retourne la carte des occupants de l'hexagone.
     * Chaque entrée correspond à un joueur et au nombre de vaisseaux qu'il possède.
     *
     * @return une carte des occupants.
     */
    public ArrayList<Ship> getOccupation() {
        return occupation;
    }

    /**
     * Retourne le niveau de cet hexagone.
     *
     * @return le niveau.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Retourne une liste des hexagones adjacents à cet hexagone en fonction de sa position
     * et des règles spécifiques du jeu.
     *
     * @param plateau le plateau contenant tous les secteurs et hexagones.
     * @return une liste des hexagones adjacents.
     */
    public List<Hex> rexAdjacent(HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> adjacents = new ArrayList<>();
        Set<String> addedCoords = new HashSet<>();

        // Offsets des voisins en fonction de la parité de y
        int[][] offsets = (y % 2 == 0)
                ? new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}}
                : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, 1}, {1, -1}};

        // Cas spécifiques pour certains hexagones
        if (x == 3 && y == 5) {
            System.out.println("Hexagone central de TriPrime détecté : (3, 5). Recherche de voisins spécifiques.");
            offsets = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}, {1, 1}, {1, -1}, {0, 2}, {0, -2}};
        }
        else if (x == 3 && (y == 4 || y == 6)) {
            System.out.println("Hexagone spécifique détecté : (3, 4). Recherche de voisins spécifiques.");
            offsets = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}};
        }
        else if (x == 4 && (y == 4 || y == 6)) {
            System.out.println("Hexagone spécifique détecté : (4, 4). Recherche de voisins spécifiques.");
            offsets = new int[][]{{1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}};
        }
        else if (x == 3 && y == 3) {
            System.out.println("Hexagone spécifique détecté : (3, 3). Recherche de voisins spécifiques.");
            offsets = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {0, 2}, {1, -1}};
        }
        else if (x == 4 && y == 3) {
            System.out.println("Hexagone spécifique détecté : (4, 3). Recherche de voisins spécifiques.");
            offsets = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 2}, {1, -1}};
        }
        else if (x == 3 && y == 7) {
            System.out.println("Hexagone spécifique détecté : (3, 7). Recherche de voisins spécifiques.");
            offsets = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, 1}, {0, -2}};
        }
        else if (x == 4 && y == 7) {
            System.out.println("Hexagone spécifique détecté : (4, 7). Recherche de voisins spécifiques.");
            offsets = new int[][]{{-1, 0}, {1, 0}, {-1, -2}, {0, 1}, {1, 1}, {0, -1}};
        }

        // Parcourir les offsets pour chercher les voisins
        for (int[] offset : offsets) {
            int nx = x + offset[0];
            int ny = y + offset[1];
            System.out.println("Recherche voisin : nx=" + nx + ", ny=" + ny);

            boolean voisinTrouve = false;

            // Parcourir tous les secteurs du plateau
            for (String niveau : plateau.keySet()) {
                ArrayList<SectorCard> sectors = plateau.get(niveau);
                for (SectorCard sector : sectors) {
                    Hex voisin = sector.getHex(nx, ny);
                    if (voisin != null) {
                        voisinTrouve = true;
                        String coords = nx + "," + ny;
                        if (!addedCoords.contains(coords)) {
                            adjacents.add(voisin);
                            addedCoords.add(coords);
                            System.out.println("Hexagone ajouté : " + voisin);
                        }
                    }
                }
            }

            if (!voisinTrouve) {
                System.out.println("Aucun hexagone trouvé aux coordonnées nx=" + nx + ", ny=" + ny);
            }
        }

        return adjacents;
    }
}