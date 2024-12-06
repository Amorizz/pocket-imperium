package Project;

import java.util.List;

public class Round {
    private int roundNumber;
    private List<Player> players;
    private Player startPlayer;

    public Round(int roundNumber, List<Player> players) {
        this.roundNumber = roundNumber;
        this.players = players;
    }

    public void startRound(Player startPlayer) {
        this.startPlayer = startPlayer;
        System.out.println("Round " + roundNumber +" started with player: " + startPlayer.getPlayerName());
        // Logique pour démarrer le tour
    }

    public void stopRound() {
        System.out.println("Round " + roundNumber + " ended.");

        // Logique pour terminer le tour
    }

    public void changeStartPlayer() {
        int index = players.indexOf(startPlayer);
        startPlayer = players.get((index + 1) % players.size());
        System.out.println("Next starting player: " + startPlayer.getPlayerName());
    }

    public void executeOrder() {
        // Logique pour exécuter les ordres des joueurs
    }

    public void countScore() {
        // Logique pour compter les scores des joueurs
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}