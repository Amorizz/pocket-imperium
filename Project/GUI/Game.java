package Project.GUI;

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

    public void start(ConsoleGUI console, Plateau plateau) {
        if (players.isEmpty()) {
            console.println("Not enough players to start the game.");
            return;
        }

        state = "in progress";
        assignPlayerColors();
        initializeRounds(9, console, plateau);
        rounds.get(0).placeFirstShips(plateau, console);
        console.println("Le jeu peut maintenant commencer !");
    }

    private void assignPlayerColors() {
        List<Color> availableColors = Arrays.asList(Color.values());
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setColor(availableColors.get(i % availableColors.size()));
        }
    }

    private void initializeRounds(int numberOfRounds, ConsoleGUI console, Plateau plateau) {
        for (int i = 0; i < numberOfRounds; i++) {
            rounds.add(new Round(i + 1, players, console, plateau));
        }
    }

    public void play() {
        while (currentRoundIndex < rounds.size()) {
            Round currentRound = rounds.get(currentRoundIndex);
            currentRound.start();
            currentRoundIndex++;
        }

        state = "finished";
        System.out.println("Tous les rounds sont terminés. Le jeu est terminé.");
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void calculateScore(HashMap<String, ArrayList<SectorCard>> plateau, List<Player> players, ConsoleGUI console) {
        console.println("Calcul des scores...");

        // Réinitialiser les scores des joueurs avant chaque calcul
        for (Player player : players) {
            player.resetPoints(); // Supposons que Player possède une méthode resetPoints()
        }

        // Parcourir tous les hexagones du plateau
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    // Identifier le joueur dominant sur cet hexagone
                    Player dominantPlayer = null;
                    int maxShips = 0;

                    for (Map.Entry<Player, Integer> entry : hex.getOccupation().entrySet()) {
                        if (entry.getValue() > maxShips) {
                            dominantPlayer = entry.getKey();
                            maxShips = entry.getValue();
                        } else if (entry.getValue() == maxShips) {
                            dominantPlayer = null; // Égalité, aucun joueur ne domine
                        }
                    }

                    if (dominantPlayer != null) {
                        // Ajouter 1 point par hexagone contrôlé
                        dominantPlayer.addPoints(1);

                        // Vérifier si l'hexagone est stratégique (par exemple, TriPrime)
                        if (hex.getLevel() == 3) { // Supposons que les hexagones stratégiques ont un niveau de 3
                            dominantPlayer.addPoints(2); // Bonus de 2 points pour un hexagone stratégique
                        }
                    }
                }
            }
        }

        // Afficher les scores de tous les joueurs
        console.println("Scores après calcul :");
        for (Player player : players) {
            console.println(player.getPlayerName() + " : " + player.getPoints() + " points.");
        }
    }


    public static void main(String[] args) {
        Game game = Game.getInstance();
        Plateau jeux = new Plateau();
        ConsoleGUI console = new ConsoleGUI(jeux);

        jeux.assignHexIds(); // Attribuer des identifiants uniques à chaque hexagone

        int nombreJoueurs = -1;
        while (nombreJoueurs < 0 || nombreJoueurs > 4) {
            console.println("Combien de joueurs réels vont jouer ? (0 à 4)");
            try {
                String input = console.getInputSync();
                nombreJoueurs = Integer.parseInt(input.trim());
                if (nombreJoueurs < 0 || nombreJoueurs > 4) {
                    console.println("Erreur : Veuillez entrer un nombre compris entre 0 et 4.");
                }
            } catch (NumberFormatException e) {
                console.println("Erreur : Veuillez entrer un nombre entier.");
            }
        }

        // Ajouter les joueurs réels
        for (int i = 0; i < nombreJoueurs; i++) {
            console.println("Nom du joueur " + (i + 1) + " ?");
            String nomJoueur = console.getInputSync();
            game.addPlayer(new Player(nomJoueur, 0));
        }

        // Ajouter des joueurs virtuels pour compléter jusqu'à 4 joueurs
        int nombreJoueursVirtuels = 4 - nombreJoueurs;
        for (int i = 0; i < nombreJoueursVirtuels; i++) {
            String nomJoueurVirtuel = "Joueur Virtuel " + (i + 1);
            console.println("Ajout du joueur virtuel : " + nomJoueurVirtuel);
            game.addPlayer(new VirtualPlayer(nomJoueurVirtuel, 0));
        }

        game.start(console, jeux); // Lancer le jeu
        game.play(); // Jouer tous les rounds
    }
}
