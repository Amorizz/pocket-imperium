package Project;

import java.util.*;

public class Hex {
    private int x;
    private int y;
    private int shipon;
    final int maxshipon;
    private String occupation;
    final int level;
    


    public Hex(int level) {
        this.level = level;
        this.maxshipon = level + 1;
        this.occupation = null;
        this.shipon = 0;
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

    public String toString(){
        String string;
        if (shipon == 0){
            string = "X : "+this.x+" Y : "+this.y+" level : "+this.level;
        }
        else{
            string = "Cet Hexagone est innocupé";
        }
        return string;
    }

    public void addShip(int number){
        //on ajoute un certain nombre de bateaux
        this.shipon += number;

    };

    public void removeShip(int number){
        this.shipon -= number;
    };

    // Getters et Setters
    public int getShipon() {
        return shipon;
    }

    public void setShipon(int shipon) {
        this.shipon = shipon;
    }

    public int getMaxshipon() {
        return maxshipon;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String color) {
        this.occupation = color;
    }

    public int getLevel() {
        return level;
    }

    public List<Hex> rexAdjacent(HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> adjacents = new ArrayList<>();
        Set<String> addedCoords = new HashSet<>();

        // Offsets des voisins en fonction de la parité de y
        int[][] offsets = (y % 2 == 0)
                ? new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}}
                : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, 1}, {1, -1}};

        // Ajout d'offsets supplémentaires si l'hexagone est celui de TriPrime (3, 5)
        if (x == 3 && y == 5) {
            System.out.println("Hexagone central de TriPrime détecté : (3, 5). Recherche de voisins spécifiques.");
            offsets = new int[][]{
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}, {1, 1}, {1, -1}, {0, 2}, {0, -2}
            };
        }
        // Cas spécifique : Hexagone (3, 4) et (3, 6)
        else if (x == 3 && (y == 4 || y == 6)) {
            System.out.println("Hexagone spécifique détecté : (3, 4). Recherche de voisins spécifiques.");
            offsets = new int[][]{
                    {-1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}
            };
        }
        // Cas spécifique : Hexagone (4, 4) et (4,6)
        else if (x == 4 && (y == 4 || y == 6)) {
            System.out.println("Hexagone spécifique détecté : (4, 4). Recherche de voisins spécifiques.");
            offsets = new int[][]{
                    {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}
            };
        }
        // Cas spécifique : Hexagone (3, 3)
        else if (x == 3 && y == 3) {
            System.out.println("Hexagone spécifique détecté : (3, 3). Recherche de voisins spécifiques.");
            offsets = new int[][]{
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {0, 2}, {1, -1}
            };
        }
        // Cas spécifique : Hexagone (4, 3)
        else if (x == 4 && y == 3) {
            System.out.println("Hexagone spécifique détecté : (4, 3). Recherche de voisins spécifiques.");
            offsets = new int[][]{
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 2}, {1, -1}
            };
        }
        // Cas spécifique : Hexagone (3, 7)
        else if (x == 3 && y == 7) {
            System.out.println("Hexagone spécifique détecté : (3, 7). Recherche de voisins spécifiques.");
            offsets = new int[][]{
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, 1}, {0, -2}
            };
        }
        // Cas spécifique : Hexagone (4, 7)
        else if (x == 4 && y == 7) {
            System.out.println("Hexagone spécifique détecté : (4, 7). Recherche de voisins spécifiques.");
            offsets = new int[][]{
                    {-1, 0}, {1, 0}, {-1, -2}, {0, 1}, {1, 1}, {0, -1}
            };
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
