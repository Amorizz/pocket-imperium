package Project;

import java.util.*;

public class Hex {
    private int x;
    private int y;
    private int shipon;
    final int maxshipon;
    private ArrayList<Player> occupation; // Liste des joueurs occupant l'hexagone
    final int level;

    public Hex(int level) {
        this.level = level;
        this.maxshipon = level + 1;
        this.occupation = new ArrayList<>(); // Initialiser la liste
        this.shipon = 0;
    }

    public void reLoc(boolean triPrime, boolean bottom, boolean top, int place, int number) {
        if (triPrime) {
            System.out.println("C'est le TriPri");
            // Gestion de la carte TriPrime (hexagones spécifiques)
            int baseX = 3;
            int baseY = 4;
            if (place == 3) {
                this.x = baseX;
                this.y = baseY + 1;
            } else if (place <= 2) {
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

    public void clearOccupation() {
        if (this.occupation != null) {
            this.occupation.clear(); // Vide la liste des occupants
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "X : " + this.x + " Y : " + this.y + " level : " + this.level + " occuper par :" + this.occupation;
    }

    public void addShip(int number) {
        this.shipon += number;
    }

    public void removeShip(int number) {
        this.shipon -= number;
        if (this.shipon < 0) this.shipon = 0;
    }

    // Getters et Setters
    public int getShipon() {
        return shipon;
    }

    public int getMaxshipon() {
        return maxshipon;
    }

    public void setOccupation(Player player) {
        if (!occupation.contains(player)) {
            occupation.add(player);
        }
    }

    public void removeOccupation(Player player) {
        occupation.remove(player);
    }

    public List<Player> getOccupants() {
        return new ArrayList<>(occupation);
    }

    public void removeShips(Player player, int count) {
        if (occupation.contains(player)) {
            int shipsToRemove = Math.min(count, shipon);
            shipon -= shipsToRemove;
            if (shipon == 0) {
                occupation.remove(player);
            }
        }
    }

    public List<Hex> rexAdjacent(HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> adjacents = new ArrayList<>();
        Set<String> addedCoords = new HashSet<>();

        int[][] offsets = (y % 2 == 0)
                ? new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}}
                : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, 1}, {1, -1}};

        for (int[] offset : offsets) {
            int nx = x + offset[0];
            int ny = y + offset[1];
            System.out.println("Recherche voisin : nx=" + nx + ", ny=" + ny);

            for (String niveau : plateau.keySet()) {
                ArrayList<SectorCard> sectors = plateau.get(niveau);
                for (SectorCard sector : sectors) {
                    Hex voisin = sector.getHex(nx, ny);
                    if (voisin != null) {
                        String coords = nx + "," + ny;
                        if (!addedCoords.contains(coords)) {
                            adjacents.add(voisin);
                            addedCoords.add(coords);
                            System.out.println("Hexagone ajouté : " + voisin);
                        }
                    }
                }
            }
        }

        return adjacents;
    }

    public boolean isAdjacent(Hex other, HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> adjacentHexes = this.rexAdjacent(plateau); // Obtenir les hexagones adjacents
        return adjacentHexes.contains(other);
    }
}
