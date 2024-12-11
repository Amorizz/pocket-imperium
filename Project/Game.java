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

    public List<List<Player>> sensplayer(Game game) {
        List<List<Player>> orderPerRound = new ArrayList<>();

        for (int round = 0; round < 3; round++) { // 3 tours correspondant à 3 cartes
            List<Player> playersForRound = new ArrayList<>(game.getPlayers());

            // Trier les joueurs selon la carte choisie pour le tour
            int finalRound = round;
            playersForRound.sort((player1, player2) -> {
                int card1 = player1.getCardsId().get(finalRound);
                int card2 = player2.getCardsId().get(finalRound);
                return Integer.compare(card1, card2);
            });

            orderPerRound.add(playersForRound);
        }

        return orderPerRound;
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

            List<List<Player>> SensPlayer = game.sensplayer(game);               // Occuper des cartes et calculer ordre joueur

            for (int i = 0; i < SensPlayer.size()-1; i++) {
                if (i == 0){
                    System.out.println("C'est donc au tour de "+SensPlayer.getFirst().getFirst().getPlayerName());
                } else {
                    System.out.println(" puis de "+SensPlayer.getFirst().get(i).getPlayerName());
                }
            }

            for (Player player : game.getPlayers()) {                       // Placer le first ship de chaque joueur
                System.out.println("C'est a "+player.getPlayerName()+" de placer ces deux premiers bateaux :");
                CommandCard c1 = new CommandCard(1);
                c1.expand(player.getColor(), jeux.getPlateau());
                CommandCard c2 = new CommandCard(2);
                c2.expand(player.getColor(), jeux.getPlateau());
            }

            System.out.println("Le jeux peut mantenant commencer");

        }
        game.startNextRound();
        jeux.afficherPlateau();
    }
}
