package Project.GUI;

import java.util.*;

public class Round {
    private int roundNumber;
    private List<Player> players;
    private Player startPlayer;
    private ConsoleGUI console;
    private Plateau plateau;

    public Round(int roundNumber, List<Player> players, ConsoleGUI console, Plateau plateau) {
        this.roundNumber = roundNumber;
        this.players = players;
        this.console = console;
        this.plateau = plateau;
    }

    public void start() {
        console.println("Round " + roundNumber + " commence !");
        playersChooseCards();
        executeActions();
        plateau.checkPlateau(console); // Vérifie les règles à la fin du round
        calculateScores();
        console.println("Round " + roundNumber + " terminé.");
    }

    private void playersChooseCards() {
        console.println("Les joueurs doivent choisir leurs cartes :");
        for (Player player : players) {
            console.println("C'est au tour de " + player.getPlayerName() + " de choisir.");
            player.chooseOrder(console);
        }
    }

    public void placeFirstShips(Plateau plateau, ConsoleGUI console) {
        for (Player player : players) {
            System.out.println("C'est à " + player.getPlayerName() + " de placer ses deux premiers bateaux.");
            player.firstShip(plateau, console);
            player.firstShip(plateau, console);
        }
    }

    private void executeActions() {
        List<List<Player>> turnOrder = sensPlayer();

        for (int i = 0; i < 3; i++) { // Chaque joueur exécute ses 3 cartes
            console.println("Tour " + (i + 1) + " des joueurs.");
            for (Player player : turnOrder.get(i)) {
                console.println("C'est au tour de " + player.getPlayerName() + ".");
                player.Card(i, plateau, console);
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

    private Map<Integer, Integer> calculateCommandCounts(int cardIndex) {
        Map<Integer, Integer> commandCount = new HashMap<>();

        for (Player player : players) {
            int cardId = player.getCardsId().get(cardIndex);
            commandCount.put(cardId, commandCount.getOrDefault(cardId, 0) + 1);
        }

        return commandCount;
    }

    private void calculateScores() {
        Game.getInstance().calculateScore(plateau.getPlateau(), players, console);
    }
}
