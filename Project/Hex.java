package Project;

import java.util.*;

public class Hex {
    private int x;
    private int y;
    private final int maxShipOn;
    private final int level;
    private Map<Player, Integer> occupation;

    public Hex(int level) {
        this.level = level;
        this.maxShipOn = level + 1;
        this.occupation = new HashMap<>();
    }

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("X: " + this.x + " Y: " + this.y + " Level: " + this.level + " Occupants: ");
        for (Map.Entry<Player, Integer> entry : occupation.entrySet()) {
            sb.append(entry.getKey().getPlayerName()).append(": ").append(entry.getValue()).append(" ships, ");
        }
        return sb.toString();
    }

    public void addShip(Player player, int number) {
        occupation.put(player, occupation.getOrDefault(player, 0) + number);
    }

    public void removeShip(Player player, int number) {
        if (occupation.containsKey(player)) {
            int remainingShips = occupation.get(player) - number;
            if (remainingShips > 0) {
                occupation.put(player, remainingShips);
            } else {
                occupation.remove(player);
            }
        }
    }

    public void clearOccupation(Player player) {
        occupation.remove(player);
    }

    public void clearAllOccupation() {
        this.occupation.clear();
    }

    // Getters et Setters
    public int getShipon() {
        return occupation.values().stream().mapToInt(Integer::intValue).sum();
    }


    public int getMaxshipon() {
        return maxShipOn;
    }

    public Map<Player, Integer> getOccupation() {
        return occupation;
    }


    public int getLevel() {
        return level;
    }

    public void addShipsPlayer(Player player, int count) {
        this.occupation.put(player, count);
    }

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


    public boolean isAdjacent(Hex other, HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> adjacentHexes = this.rexAdjacent(plateau); // Obtenir les hexagones adjacents
        if (adjacentHexes.contains(other)) {
            return true; // Adjacent directement
        }

        // Vérifier si l'autre hexagone est adjacent à l'un des hexagones adjacents
        for (Hex hex : adjacentHexes) {
            List<Hex> secondAdjacentHexes = hex.rexAdjacent(plateau);
            if (secondAdjacentHexes.contains(other)) {
                return true; // Adjacent à 2 hexagones
            }
        }

        return false; // Pas adjacent
    }
}