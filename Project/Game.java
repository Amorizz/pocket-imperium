package Project;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game instance;
    private List<String> players;
    private String state;

    private Game() {
        // Initialisation de l'instance
        players = new ArrayList<>();
        state = "waiting";
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void addPlayer(String player) {
        players.add(player);
    }

    public void start() {
        if (players.size() > 1) {
            state = "in progress";
        } else {
            System.out.println("Not enough players to start the game.");
        }
    }

    public String getState() {
        return state;
    }

    public List<String> getPlayers() {
        return players;
    }

    public static void main(String[] args) {
    Game game = Game.getInstance();
    game.addPlayer("Joueur 1");
    game.addPlayer("Joueur 2");
    game.start();
    System.out.println("État du jeu : " + game.getState());
    System.out.println("Liste des joueurs : " + game.getPlayers());
    }
}

