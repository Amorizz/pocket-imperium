package Project;

public class Player {
    private int shipNumber;
    private int startPlayer;
    private String playerName;
    private String cardOrder[];
    private int points;
    private String name;

    public Player(int shipNumber, int startPlayer, String playerName, String[] cardOrder, int points) {
        this.shipNumber = shipNumber;
        this.startPlayer = startPlayer;
        this.playerName = playerName;
        this.cardOrder = cardOrder;
        this.points = points;
    }

    public Player(String playerName) {
        this.playerName = playerName;
    }

    public void chooseOrder() {
        // Logique pour choisir l'ordre des cartes
    }

    public void useCard(int cardId) {
        // Logique pour utiliser une carte spécifique
    }

    public void placeShips(int numberOfShips) {
        // Logique pour placer un certain nombre de vaisseaux
    }

    public int getShips() {
        return shipNumber;
    }

    public String getPlayerName() {
        return playerName;
    }
}
