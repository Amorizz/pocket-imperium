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
            // Initialisation pour la carte TriPrime
            this.x = 3;
            this.y = 4;

            for (int i = 0; i < place; i++) {
                // Déplacement des hexagones dans la carte TriPrime
                if (this.y == 4 && this.x == 5) {
                    this.y += 1;
                    this.x = 2;
                }
                if (this.y == 5 && this.x == 4) {
                    this.y += 1;
                    this.x = 2;
                }
                this.x++;
            }
        } else if (bottom || top) {
            // Initialisation pour les cartes Bottom et Top
            this.x = 1 + 2 * number - 2;
            this.y = (top) ? 1 : 7;  // Y=7 pour les cartes top, Y=1 pour les cartes bottom

            for (int i = 0; i < place; i++) {
                // Vérification des positions spécifiques pour bottom et top
                if ((this.x == 1 + 2 * number && (this.y == 1 || this.y == 7))) {
                    this.y++;
                    this.x = 2 * number - 2;
                }
                if ((this.x == 2 + 2 * number && (this.y == 2 || this.y == 8))) {
                    this.y++;
                    this.x = 2 * number - 2;
                }
                this.x++;
            }
        } else {
            // Initialisation pour les cartes qui ne sont ni bottom ni top (par exemple, mid)
            this.y = 4;
            if (number == 1) {
                this.x = 1;
                // Gestion spécifique des indices
                if (this.x == 4 && this.y == 4) {
                    this.y++;
                    this.x = 0;
                }
                if (this.x == 3 && this.y == 5) {
                    this.y++;
                    this.x = 0;
                }
                this.x++;
            } else if (number == 3) {
                this.x = 4;
                // Gestion des indices pour number == 3
                if (this.x == 7 && this.y == 4) {
                    this.y++;
                    this.x = 3;
                }
                if (this.x == 6 && this.y == 5) {
                    this.y++;
                    this.x = 3;
                }
                this.x++;
            }
        }
        System.out.println("Hex placé : x=" + this.x + ", y=" + this.y);  // Affichage pour débogage
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
        Set<String> addedCoords = new HashSet<>();  // Utiliser un set pour éviter les doublons

        // Offsets des voisins en fonction de la parité de y
        int[][] offsets = (y % 2 == 0)
                ? new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {-1, -1}}
                : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, 1}, {1, -1}};

        // Parcours des offsets et recherche des hexagones adjacents
        for (int[] offset : offsets) {
            int nx = x + offset[0];
            int ny = y + offset[1];

            System.out.println("Recherche voisin : nx=" + nx + ", ny=" + ny);

            boolean found = false;

            // Parcourir chaque niveau et chaque SectorCard
            for (String niveau : plateau.keySet()) {
                ArrayList<SectorCard> sectors = plateau.get(niveau);

                for (SectorCard sector : sectors) {
                    // Vérification que l'hexagone est présent dans la sectorCard
                    Hex adjacentHex = sector.getHex(nx, ny);

                    if (adjacentHex != null) {
                        String coords = nx + "," + ny;  // Utilisation des coordonnées comme clé pour éviter les doublons
                        if (!addedCoords.contains(coords)) {
                            adjacents.add(adjacentHex);
                            addedCoords.add(coords);  // Ajouter les coordonnées à la liste des déjà ajoutés
                            found = true;
                            System.out.println("Hexagone ajouté : " + adjacentHex);
                        }
                    }
                }
            }

            if (!found) {
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
