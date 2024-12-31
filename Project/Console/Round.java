package Project.Console;

import java.util.*;

public class Round {
    private int roundNumber;
    private List<Player> players;
    private Plateau plateau;

    public Round(int roundNumber, List<Player> players, Plateau plateau) {
        this.roundNumber = roundNumber;
        this.players = players;
        this.plateau = plateau;
    }

    public void startRound() {
        System.out.println("Round " + roundNumber + " started.");
        placeFirstShips();
        executePlayerActions();
        checkPlateau();
        System.out.println("Round " + roundNumber + " completed.");
    }

    private void placeFirstShips() {
        for (Player player : players) {
            System.out.println("C'est à " + player.getPlayerName() + " de placer ses deux premiers bateaux.");
            player.firstShip(plateau);
            player.firstShip(plateau);
        }
    }

    private void executePlayerActions() {
        System.out.println("Les joueurs doivent choisir leurs cartes.");
        for (Player player : players) {
            player.chooseOrder();
        }

        List<List<Player>> orderedPlayers = sensPlayer();

        for (int i = 0; i < 3; i++) { // Trois cartes à jouer
            System.out.println("Tour " + (i + 1) + " des actions.");
            for (Player player : orderedPlayers.get(i)) {
                System.out.println("C'est à " + player.getPlayerName() + " de jouer.");
                player.Card(i, plateau);
            }
        }
    }

    private List<List<Player>> sensPlayer() {
        List<List<Player>> orderPerRound = new ArrayList<>();
        for (int round = 0; round < 3; round++) {
            List<Player> orderedPlayers = new ArrayList<>(players);
            Map<Integer, Integer> commandCount = new HashMap<>();

            for (Player player : orderedPlayers) {
                int cardId = player.getCardsId().get(round);
                commandCount.put(cardId, commandCount.getOrDefault(cardId, 0) + 1);
            }

            for (Player player : orderedPlayers) {
                int cardId = player.getCardsId().get(round);
                CommandCard card = player.getCards().get(round);
                card.setPower(Math.min(commandCount.get(cardId), 3));
            }

            int finalRound = round;
            orderedPlayers.sort((p1, p2) -> {
                int c1 = p1.getCardsId().get(finalRound);
                int c2 = p2.getCardsId().get(finalRound);
                return Integer.compare(c1, c2);
            });

            orderPerRound.add(orderedPlayers);
        }
        return orderPerRound;
    }

    private void checkPlateau() {
        plateau.checkPlateau();
    }

    public void calculateScore() {
        plateau.calculateScore(players);
    }
}
