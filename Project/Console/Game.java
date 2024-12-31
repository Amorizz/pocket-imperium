package Project.Console;

import java.util.*;

/**
 * La classe {@code Game} gère le déroulement d'une partie de jeu, incluant la gestion des joueurs,
 * des rounds, du plateau, et des scores.
 * Elle utilise le pattern Singleton pour garantir qu'une seule instance du jeu soit active à la fois.
 */
public class Game {
    private static Game instance;
    private List<Player> players; // Liste des joueurs participant au jeu
    private List<Round> rounds; // Liste des rounds de la partie
    private int currentRoundIndex; // Indice du round actuel
    private String state; // État actuel du jeu (e.g., "waiting", "in progress", "finished")
    private Plateau plateau; // Plateau de jeu contenant les hexagones et les secteurs

    /**
     * Constructeur privé pour garantir l'utilisation du pattern Singleton.
     */
    private Game() {
        players = new ArrayList<>();
        rounds = new ArrayList<>();
        currentRoundIndex = 0;
        state = "waiting";
        plateau = new Plateau();
    }

    /**
     * Retourne l'instance unique de la classe {@code Game}.
     *
     * @return l'instance unique de {@code Game}.
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     * Ajoute un joueur à la partie.
     *
     * @param player le joueur à ajouter.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Démarre la partie en assignant les couleurs aux joueurs et en créant les rounds.
     * Vérifie qu'il y ait au moins un joueur avant de commencer.
     */
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

    /**
     * Assigne une couleur unique à chaque joueur en utilisant un cycle sur les couleurs disponibles.
     */
    private void assignPlayerColors() {
        List<Color> availableColors = Arrays.asList(Color.values());
        for (int i = 0; i < players.size(); i++) {
            Color color = availableColors.get(i % availableColors.size());
            players.get(i).setColor(color);
        }
    }

    /**
     * Crée une série de rounds pour la partie.
     *
     * @param numberOfRounds le nombre de rounds à créer.
     */
    private void createRounds(int numberOfRounds) {
        for (int i = 0; i < numberOfRounds; i++) {
            rounds.add(new Round(i + 1, players, plateau));
        }
    }

    /**
     * Démarre le prochain round si des rounds restent à jouer.
     * Met l'état du jeu à "finished" une fois que tous les rounds ont été joués.
     */
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

    /**
     * Retourne l'état actuel du jeu.
     *
     * @return l'état actuel du jeu sous forme de chaîne de caractères.
     */
    public String getState() {
        return state;
    }

    /**
     * Calcule les scores de tous les joueurs en fonction de leur domination sur les hexagones du plateau.
     */
    public void calculateScore() {
        System.out.println("Calcul des scores...");
        for (Player player : players) {
            player.resetPoints(); // Réinitialise les points avant le calcul
        }
        for (Round round : rounds) {
            round.calculateScore(); // Calcule les scores pour chaque round
        }
    }

    /**
     * Point d'entrée principal pour démarrer une nouvelle partie.
     *
     * @param args arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        Game game = Game.getInstance();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Combien de joueurs réels vont jouer ? (0 à 4)");
        int nombreJoueurs = scanner.nextInt();

        // Ajout des joueurs réels
        for (int i = 0; i < nombreJoueurs; i++) {
            System.out.println("Nom du joueur " + (i + 1) + " ?");
            String nomJoueur = scanner.next();
            game.addPlayer(new Player(nomJoueur, 0));
        }

        // Compléter avec des joueurs virtuels jusqu'à un total de 4 joueurs
        int nombreJoueursVirtuels = 4 - nombreJoueurs;
        for (int i = 0; i < nombreJoueursVirtuels; i++) {
            String nomJoueurVirtuel = "Joueur Virtuel " + (i + 1);
            game.addPlayer(new VirtualPlayer(nomJoueurVirtuel, 0));
        }

        // Démarrage de la partie
        game.start();

        // Boucle principale du jeu
        while (!game.getState().equals("finished")) {
            game.startNextRound();
        }

        // Calcul des scores finaux
        game.calculateScore();
    }
}
