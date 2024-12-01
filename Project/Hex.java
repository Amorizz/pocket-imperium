package Project;

public class Hex {
    private int shipon;
    final int maxshipon;
    private Player occupation;
    final int level;

    public Hex(int level) {
        this.level = level;
        this.maxshipon = level+1;
        this.occupation = null;
        this.shipon = 0;
    }

    public String toString(){
        return "Cet Hexagone contient "+shipon+" bateau sur "+maxshipon+" il est occupé par "+occupation+" et son niveau est "+level;
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

    public Player getOccupation() {
        return occupation;
    }

    public void setOccupation(Player occupation) {
        this.occupation = occupation;
    }

    public int getLevel() {
        return level;
    }

}
