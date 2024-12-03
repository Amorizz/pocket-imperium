package Project;

public class Hex {
    private int shipon;
    final int maxshipon;
    private String occupation;
    final int level;
    private int number;

    public Hex(int level, int number) {
        this.level = level;
        this.maxshipon = level+1;
        this.occupation = null;
        this.shipon = 0;
        this.number = number;
    }

    public String toString(){
        if (this.occupation == null){
            return "Cet Hexagone contient "+shipon+" bateau sur "+maxshipon+" il est occupé par le joueur de couleur "+occupation+" et son niveau est "+level+" et son numero est "+this.number;
        }
        else{
            return "Cet Hexagone contient "+shipon+" bateau sur "+maxshipon+" il est occupé par le joueur de couleur "+occupation+" et son niveau est "+level+" et son numero est "+this.number;
        }
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
