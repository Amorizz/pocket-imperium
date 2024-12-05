package Project;

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
            System.out.println("dedans chef");
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
            string = "Cet Hexagone contient "+shipon+" bateau sur "+maxshipon+" il est occupé par le joueur de couleur "+occupation+" et son niveau est "+level;
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


}