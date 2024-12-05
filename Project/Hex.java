package Project;

public class Hex {
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
