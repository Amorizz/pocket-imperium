package Project.GUI;

import java.util.*;

/**
 * La classe {@code Game} gère le déroulement d'une partie de jeu, y compris la gestion des joueurs,
 * des rounds, du plateau et des scores. Les interactions avec l'utilisateur sont gérées via {@code ConsoleGUI}.
 */
public class Game {
    private static Game instance;
    private final List<Player> players;
    private final List<Round> rounds;
    private int currentRoundIndex;
    private String state;
    private Plateau plateau;

    /**
     * Constructeur privé pour garantir l'utilisation du pattern Singleton.
     */
    private Game() {
        players = new ArrayList<>();
        rounds = new ArrayList<>();
        currentRoundIndex = 0;
        state = "waiting";
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
     * Démarre la partie en assignant les couleurs, en initialisant les rounds,
     * et en permettant aux joueurs de placer leurs premiers bateaux.
     *
     * @param console interface utilisateur graphique pour afficher les informations et gérer les entrées.
     * @param plateau le plateau de jeu.
     */
    public void start(ConsoleGUI console, Plateau plateau) {
        if (players.isEmpty()) {
            console.println("Pas assez de joueurs pour commencer la partie.");
            return;
        }

        state = "in progress";
        this.plateau = plateau;
        assignPlayerColors(console);
        initializeRounds(9, console, plateau);
        rounds.get(0).placeFirstShips();
        console.println("Le jeu peut maintenant commencer !");
    }

    /**
     * Assigne une couleur unique à chaque joueur et affiche les couleurs assignées.
     *
     * @param console interface utilisateur graphique.
     */
    private void assignPlayerColors(ConsoleGUI console) {
        List<Color> availableColors = Arrays.asList(Color.values());
        for (int i = 0; i < players.size(); i++) {
            Color color = availableColors.get(i % availableColors.size());
            players.get(i).setColor(color);
            console.println(players.get(i).getPlayerName() + " a reçu la couleur : " + color.getColorName());
        }
    }

    /**
     * Initialise les rounds de la partie.
     *
     * @param numberOfRounds le nombre de rounds à créer.
     * @param console interface utilisateur graphique.
     * @param plateau le plateau de jeu.
     */
    private void initializeRounds(int numberOfRounds, ConsoleGUI console, Plateau plateau) {
        for (int i = 0; i < numberOfRounds; i++) {
            rounds.add(new Round(i + 1, players, console, plateau));
        }
    }

    /**
     * Joue tous les rounds de la partie jusqu'à leur achèvement.
     */
    public void play() {
        while (currentRoundIndex < rounds.size()) {
            rounds.get(currentRoundIndex).start();
            currentRoundIndex++;
        }
        state = "finished";
        System.out.println("Tous les rounds sont terminés. Le jeu est terminé.");
    }

    /**
     * Calcule les scores de tous les joueurs en fonction de leur domination sur les hexagones du plateau.
     *
     * @param plateau la carte du plateau, contenant les secteurs et leurs hexagones.
     * @param players la liste des joueurs participant.
     * @param console l'interface utilisateur graphique pour afficher les résultats.
     */
    public void calculateScore(HashMap<String, ArrayList<SectorCard>> plateau, List<Player> players, ConsoleGUI console) {
        console.println("Calcul des scores...");

        // Réinitialiser les scores des joueurs avant chaque calcul
        for (Player player : players) {
            player.resetPoints(); // Réinitialise les points des joueurs
        }

        // Parcourir tous les hexagones du plateau
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    // Identifier le joueur dominant sur cet hexagone
                    Player dominantPlayer = null;
                    int maxShips = 0;

                    // Compter le nombre total de vaisseaux pour chaque joueur
                    Map<Player, Integer> shipCounts = new HashMap<>();
                    for (Ship ship : hex.getOccupation()) {
                        Player owner = ship.getPlayerName();
                        shipCounts.put(owner, shipCounts.getOrDefault(owner, 0) + ship.getNbrShipy());
                    }

                    // Déterminer le joueur dominant
                    for (Map.Entry<Player, Integer> entry : shipCounts.entrySet()) {
                        int ships = entry.getValue();
                        if (ships > maxShips) {
                            dominantPlayer = entry.getKey();
                            maxShips = ships;
                        } else if (ships == maxShips) {
                            dominantPlayer = null; // Égalité, aucun joueur dominant
                        }
                    }

                    // Attribuer des points au joueur dominant
                    if (dominantPlayer != null) {
                        // Ajouter 1 point par hexagone contrôlé
                        dominantPlayer.addPoints(1);

                        // Ajouter un bonus pour les hexagones stratégiques (niveau 3)
                        if (hex.getLevel() == 3) {
                            dominantPlayer.addPoints(2);
                        }

                        console.println(dominantPlayer.getPlayerName() + " gagne " +
                                (hex.getLevel() == 3 ? "3" : "1") + " points pour l'hexagone " + hex);
                    } else {
                        console.println("Aucun joueur ne gagne de points pour l'hexagone " + hex);
                    }
                }
            }
        }

        // Afficher les scores finaux
        console.println("\nScores après calcul :");
        for (Player player : players) {
            console.println(player.getPlayerName() + " : " + player.getPoints() + " points.");
        }
    }

    /**
     * Demande le nombre de joueurs réels via l'interface utilisateur graphique.
     *
     * @param console interface utilisateur graphique.
     * @return le nombre de joueurs réels.
     */
    private static int getNumberOfPlayers(ConsoleGUI console) {
        int nombreJoueurs = -1;
        while (nombreJoueurs < 0 || nombreJoueurs > 4) {
            console.println("Combien de joueurs réels vont jouer ? (0 à 4)");
            try {
                nombreJoueurs = Integer.parseInt(console.getInputSync().trim());
                if (nombreJoueurs < 0 || nombreJoueurs > 4) {
                    console.println("Erreur : Veuillez entrer un nombre compris entre 0 et 4.");
                }
            } catch (NumberFormatException e) {
                console.println("Erreur : Veuillez entrer un nombre entier.");
            }
        }
        return nombreJoueurs;
    }

    /**
     * Méthode principale pour démarrer le jeu.
     *
     * @param args les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        Game game = Game.getInstance();
        Plateau plateau = new Plateau();
        ConsoleGUI console = new ConsoleGUI(plateau);
        plateau.assignHexIds();

        int nombreJoueurs = getNumberOfPlayers(console);

        for (int i = 0; i < nombreJoueurs; i++) {
            console.println("Nom du joueur " + (i + 1) + " ?");
            String nomJoueur = console.getInputSync();
            game.addPlayer(new Player(nomJoueur, 0));
        }

        int nombreJoueursVirtuels = 4 - nombreJoueurs;
        for (int i = 0; i < nombreJoueursVirtuels; i++) {
            String nomJoueurVirtuel = "Joueur Virtuel " + (i + 1);
            console.println("Ajout du joueur virtuel : " + nomJoueurVirtuel);
            game.addPlayer(new VirtualPlayer(nomJoueurVirtuel, 0));
        }

        game.start(console, plateau);
        game.play();
    }
}
