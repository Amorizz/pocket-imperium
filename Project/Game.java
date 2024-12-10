package Project;

import java.util.*;

public class Game {
    private static Game instance;
    private List<Player> players;
    private List<Round> rounds;
    private int currentRoundIndex;
    private String state;

    private Game() {
        players = new ArrayList<>();
        rounds = new ArrayList<>();
        currentRoundIndex = 0;
        state = "waiting";
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
        if (players.size() > 0) {
            state = "in progress";
            List<Color> availableColors = Arrays.asList(Color.values()); // Récupérer toutes les couleurs de l'énum
            for (int i = 0; i < players.size(); i++) {
                Color couleurJoueur = availableColors.get(i % availableColors.size()); // Assigner une couleur unique
                players.get(i).setColor(couleurJoueur); // Méthode à ajouter pour définir la couleur
            }
            createRounds(9); // Par exemple, créer rounds
        } else {
            System.out.println("Not enough players to start the game.");
        }
    }

    private void createRounds(int numberOfRounds) {
        for (int i = 0; i < numberOfRounds; i++) {
            rounds.add(new Round(i + 1, players));
        }
    }

    private void startNextRound() {
        if (currentRoundIndex < rounds.size()) {
            Round currentRound = rounds.get(currentRoundIndex);
            currentRound.startRound(players.get(0)); // Par exemple, commencer avec le premier joueur
            currentRoundIndex++;
        } else {
            System.out.println("All rounds completed.");
            state = "finished";
        }
    }

    public String getState() {
        return state;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : players) {
            if (player != null) {
                System.out.println(player.getPlayerName());
                playerNames.add(player.getPlayerName());
            }
        }
        return playerNames;
    }

    public ArrayList<Player> sensplayer(Game game) {
        ArrayList<Player> playersList = new ArrayList<>(game.getPlayers());

        playersList.sort((player1, player2) -> {
            List<Integer> cards1 = player1.getCardsId();
            List<Integer> cards2 = player2.getCardsId();

            for (int i = 0; i < 3; i++) {
                int card1 = cards1.get(i);
                int card2 = cards2.get(i);

                if (card1 != card2) {
                    return Integer.compare(card1, card2);
                }
            }
            return 0;
        });

        return playersList;
    }

    public static void main(String[] args) {
        Game game = Game.getInstance();
        Plateau jeux = new Plateau();
        try (Scanner scanner = new Scanner(System.in)) {
            int nombreJoueurs = -1;
            while (nombreJoueurs < 0) {
                System.out.println("Combien de joueurs vont jouer ?");
                try {
                    nombreJoueurs = scanner.nextInt();
                    if (nombreJoueurs < 0) {
                        System.out.println("Erreur : Veuillez entrer un nombre entier positif.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : Veuillez entrer un nombre entier.");
                    scanner.next(); // Consomme l'entrée invalide
                }
            }
            for (int i = 0; i < nombreJoueurs; i++) {
                System.out.println("Nom du joueur " + (i + 1) + " ?");
                String nomJoueur = scanner.next();
                game.addPlayer(new Player(0, nomJoueur, 0)); // Utilisation de l'énum pour la couleur
            }
            game.start();
            System.out.println("État du jeu : " + game.getState());
            System.out.println("Liste des joueurs et leurs couleurs :");
            for (Player player : game.getPlayers()) {
                System.out.println(player.getPlayerName() + " - Couleur : " + player.getColor());
            }

            System.out.println("Les joueurs doivent choisir leur carte :");
            for (Player player : game.getPlayers()) {
                System.out.println(player.getPlayerName() + " - Couleur : " + player.getColor());
                player.chooseOrder();
            }

            ArrayList<Player> SensPlayer = new ArrayList<>();
            SensPlayer = game.sensplayer(game);
            for (int i = 0; i < SensPlayer.size(); i++) {
                if (i == 0){
                    System.out.println("C'est donc au tour de "+SensPlayer.get(0).getPlayerName());
                }
                System.out.println(" puis de "+SensPlayer.get(i).getPlayerName());
            }
        }
        game.startNextRound();
        jeux.afficherPlateau();
    }
}
