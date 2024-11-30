package Project;

import java.lang.reflect.Array;
import java.util.List;

public class Player {
    private int shipNumber;
    private String playerName;
    private List<String> cardOrder;
    private int points;
    private String name;

    public Player(String playerName) {
        this.playerName = playerName;
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