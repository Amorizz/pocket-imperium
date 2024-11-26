package Project;

public class SectorCard {
    private int number;
    private int position;
    private boolean isOccupied;
    private boolean isTriPrime;
    private boolean isBottom;
    private boolean isTop;
    private boolean hasBorder;
    private Player occupiedBy;

    // Constructeur
    public SectorCard() {
        this.isOccupied = false;
        this.isTriPrime = false;
        this.isBottom = false;
        this.isTop = false;
        this.hasBorder = false;
        this.occupiedBy = null;
    }

    // Getters et Setters
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }

    public boolean isTriPrime() {
        return isTriPrime;
    }

    public void setTriPrime(boolean triPrime) {
        this.isTriPrime = triPrime;
    }

    public boolean isBottom() {
        return isBottom;
    }

    public void setBottom(boolean bottom) {
        this.isBottom = bottom;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        this.isTop = top;
    }

    public boolean hasBorder() {
        return hasBorder;
    }

    public void setBorder(boolean border) {
        this.hasBorder = border;
    }

    public Player getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(Player player) {
        this.occupiedBy = player;
        this.isOccupied = (player != null);
    }

    // Méthodes utilitaires
    public void clearOccupation() {
        this.occupiedBy = null;
        this.isOccupied = false;
    }

    @Override
    public String toString() {
        return "SectorCard{" +
                "number=" + number +
                ", position=" + position +
                ", isOccupied=" + isOccupied +
                ", isTriPrime=" + isTriPrime +
                ", isBottom=" + isBottom +
                ", isTop=" + isTop +
                ", hasBorder=" + hasBorder +
                ", occupiedBy=" + occupiedBy +
                '}';
    }
}
