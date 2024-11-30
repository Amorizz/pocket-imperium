package Project;

import java.lang.reflect.Array;
import java.util.List;

public class Player {
    private int shipNumber;
    private String playerName;
    private List<Integer> cardOrder;
    private int points;
    private String name;

    public Player(int shipNumber, String playerName, List<Integer> cardOrder, int points) {
        this.shipNumber = shipNumber;
        this.playerName = playerName;
        this.cardOrder = cardOrder;
        this.points = points;
    }


    public void chooseOrder() {
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