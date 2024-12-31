package Project.Console;

import java.util.*;

public class Game {
    private static Game instance;
    private List<Player> players;
    private List<Round> rounds;
    private int currentRoundIndex;
    private String state;
    private Plateau plateau;

    private Game() {
        players = new ArrayList<>();
        rounds = new ArrayList<>();
        currentRoundIndex = 0;
        state = "waiting";
        plateau = new Plateau();
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void start() {
        if (players.isEmpty()) {
            System.out.println("Not enough players to start the game.");
            return;
        }

        state = "in progress";
        assignPlayerColors();
        createRounds(9);
        System.out.println("Game started with " + players.size() + " players.");
    }

    private void assignPlayerColors() {
        List<Color> availableColors = Arrays.asList(Color.values());
        for (int i = 0; i < players.size(); i++) {
            Color color = availableColors.get(i % availableColors.size());
            players.get(i).setColor(color);
        }
    }

    private void createRounds(int numberOfRounds) {
        for (int i = 0; i < numberOfRounds; i++) {
            rounds.add(new Round(i + 1, players, plateau));
        }
    }

    public void startNextRound() {
        if (currentRoundIndex < rounds.size()) {
            Round currentRound = rounds.get(currentRoundIndex);
            currentRound.startRound();
            currentRoundIndex++;
        } else {
            System.out.println("All rounds completed.");
            state = "finished";
        }
    }

    public String getState() {
        return state;
    }

    public void calculateScore() {
        System.out.println("Calcul des scores...");
        for (Player player : players) {
            player.resetPoints();
        }
        for (Round round : rounds) {
            round.calculateScore();
        }
    }

    public static void main(String[] args) {
        Game game = Game.getInstance();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Combien de joueurs réels vont jouer ? (0 à 4)");
        int nombreJoueurs = scanner.nextInt();

        for (int i = 0; i < nombreJoueurs; i++) {
            System.out.println("Nom du joueur " + (i + 1) + " ?");
            String nomJoueur = scanner.next();
            game.addPlayer(new Player(nomJoueur, 0));
        }

        int nombreJoueursVirtuels = 4 - nombreJoueurs;
        for (int i = 0; i < nombreJoueursVirtuels; i++) {
            String nomJoueurVirtuel = "Joueur Virtuel " + (i + 1);
            game.addPlayer(new VirtualPlayer(nomJoueurVirtuel, 0));
        }

        game.start();

        while (!game.getState().equals("finished")) {
            game.startNextRound();
        }

        // Calcul des scores finaux
        game.calculateScore();
    }
}
