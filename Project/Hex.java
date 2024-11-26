package Project;

public class Hex {
    private int shipon;
    private int maxshipon;
    private String occupation;
    private int level;

    public Hex(int level) {
        this.level = level;
        this.maxshipon = level+1;
        this.occupation = "";
        this.shipon = 0;
    }

    public String toString(){
        return "Cet Hexagone contient "+shipon+" bateau sur "+maxshipon+" il est occupé par "+occupation+" et son niveau est "+level;
    }

    public void addShip(){};

    public void removeShip(){}; //Pourquoi faire ?

    public void change(){};
}
