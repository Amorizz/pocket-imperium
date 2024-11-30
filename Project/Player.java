package Project;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private int shipNumber;
    private String playerName;
    private List<Integer> cards;
    private int points;
    private String name;

    public Player(int shipNumber, String playerName, int points) {
        this.shipNumber = shipNumber;
        this.playerName = playerName;
        this.cards = new ArrayList<Integer>();
        this.points = points;
    }

    public void chooseOrder() {
        cards.removeAll(cards);
        for (int i = 0; i < cards.size(); i++) {
            System.out.println("Sélectionnez l'ordre d'exécution pour la carte " + (i + 1) + " (ID de la carte) :");
            int choix = scanner.nextInt(); // Utiliser un Scanner pour lire l'entrée de l'utilisateur
            cards.add(choix);
        }
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