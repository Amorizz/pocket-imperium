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

            // Demander le nombre de joueurs réels
            while (nombreJoueurs < 0 || nombreJoueurs > 4) {
                System.out.println("Combien de joueurs réels vont jouer ? (0 à 4)");
                try {
                    nombreJoueurs = scanner.nextInt();
                    if (nombreJoueurs < 0 || nombreJoueurs > 4) {
                        System.out.println("Erreur : Veuillez entrer un nombre compris entre 0 et 4.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : Veuillez entrer un nombre entier.");
                    scanner.next(); // Consomme l'entrée invalide
                }
            }

            // Ajouter les joueurs réels
            for (int i = 0; i < nombreJoueurs; i++) {
                System.out.println("Nom du joueur " + (i + 1) + " ?");
                String nomJoueur = scanner.next();
                game.addPlayer(new Player(0, nomJoueur, 0));
            }

            // Ajouter des joueurs virtuels pour compléter jusqu'à 4 joueurs
            int nombreJoueursVirtuels = 4 - nombreJoueurs;
            for (int i = 0; i < nombreJoueursVirtuels; i++) {
                String nomJoueurVirtuel = "Joueur Virtuel " + (i + 1);
                System.out.println("Ajout du joueur virtuel : " + nomJoueurVirtuel);
                game.addPlayer(new VirtualPlayer(0, nomJoueurVirtuel, 0));
            }

            // Démarrer le jeu
            game.start();

            System.out.println("Le jeux peut maintenant commencer");
            System.out.println("Nous avons bien "+game.getPlayers().size()+" joueurs");

            // Afficher l'état et la liste des joueurs
            System.out.println("État du jeu : " + game.getState());
            System.out.println("Liste des joueurs et leurs couleurs :");
            for (Player player : game.getPlayers()) {
                System.out.println(player.getPlayerName() + " - Couleur : " + player.getColor());
            }

        for (Player player : game.getPlayers()) {                       // Placer le first ship de chaque joueur
                System.out.println("C'est a "+player.getPlayerName()+" de placer ces deux premiers bateaux :");
                player.Card(1, jeux);
                player.Card(1, jeux);
            }

            System.out.println("Le jeux peut mantenant commencer");

            for (int i = 0; i < 9;i++){
                System.out.println("Les joueurs doivent choisir leur carte :");
                for (Player player : game.getPlayers()) {
                    System.out.println(player.getPlayerName() + " - Couleur : " + player.getColor());
                    player.chooseOrder();
                }

                List<List<Player>> SensPlayer = game.sensplayer(game);               // Occuper des cartes et calculer ordre joueur

                for (int j = 0; j < SensPlayer.size()-1; j++) {
                    if (j == 0){
                        System.out.println("C'est donc au tour de "+SensPlayer.getFirst().getFirst().getPlayerName());
                    } else {
                        System.out.println(" puis de "+SensPlayer.getFirst().get(j).getPlayerName());
                    }
                }

                for (int j = 0;j < 3;j++){
                    for (Player player : SensPlayer.get(i)) {
                        System.out.println("C'est à "+player.getPlayerName()+" de jouer :");
                        System.out.print(player.getPlayerName()+" joue avec la carte numero : "+SensPlayer.getFirst().get(j).getCardsId().get(j));
                        player.Card(player.getCardsId().get(j), jeux);
                    }
                }
            }

        }
        game.startNextRound();
        jeux.afficherPlateau();
    }
}