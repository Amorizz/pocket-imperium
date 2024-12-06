package Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void reLoc(boolean triPrime, boolean bottom, boolean top, int place, int number){    // Cette fonction sert a trouver le x et le y
        if (triPrime) {
            //code
            this.y = 4;
            this.x = 3;
            for (int i = 0; i < place; i++) {
                if (this.y == 4 && this.x == 5){
                    this.y += 1;
                    this.x=2;
                }
                if (this.y == 5 && this.x ==4){
                    this.y += 1;
                    this.x = 2;
                }
                this.x++;
            }
        } if (bottom || top) {
            //code
            this.x = 1 + 2 * number - 2;
            this.y = 1;
            if (top){
                this.y = 7;
            }
            for (int i=0; i<place; i++) {
                if (this.x == 1+2*(number) && this.y == 1 || this.y == 7){
                    this.y++;
                    this.x = 2 * number - 2;
                }
                if (this.x == 2+2*(number) && this.y == 2 || this.y == 8){
                    this.y++;
                    this.x = 2 * number - 2;
                }
                this.x++;
            }
        } if (!bottom && !top && !triPrime) {
            //code
            this.y = 4;
            if (number == 1){
                this.x = 1;
                if (this.x == 4 && this.y == 4){
                    this.y++;
                    this.x = 0;
                }
                if (this.x == 3 && this.y == 5){
                    this.y++;
                    this.x = 0;
                }
                this.x++;
            }
            if (number == 3){
                this.x = 4;
                if (this.x == 7 && this.y == 4){
                    this.y++;
                    this.x = 3;
                }
                if (this.x == 6 && this.y == 5){
                    this.y++;
                    this.x = 3;
                }
                this.x++;
            }
        }
        return string;
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

        // Offsets des voisins en fonction de la parité de y
        int[][] offsets = (y % 2 == 0)
                ? new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, +1}, {-1, -1}}
                : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, +1}, {1, -1}};

        // Parcourir chaque niveau et chaque SectorCard
        for (String niveau : plateau.keySet()) {
            ArrayList<SectorCard> sectors = plateau.get(niveau);

            for (SectorCard sector : sectors) {
                for (int[] offset : offsets) {
                    int nx = x + offset[0];
                    int ny = y + offset[1];

                    // Chercher l'hexagone correspondant dans le sector
                    Hex adjacentHex = sector.getHex(nx, ny);
                    if (adjacentHex != null && !adjacents.contains(adjacentHex)) {
                        adjacents.add(adjacentHex);
                    }
                }
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
