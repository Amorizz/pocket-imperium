package Project.GUI;

import java.util.*;

/**
 * La classe {@code Round} représente un round de jeu dans Pocket Imperium.
 * Elle gère les choix des joueurs, l'exécution des actions, et le calcul des scores.
 */
public class Round {
    private final int roundNumber; // Le numéro du round
    private final List<Player> players; // La liste des joueurs
    private final ConsoleGUI console; // L'interface utilisateur graphique
    private final Plateau plateau; // Le plateau de jeu

    /**
     * Constructeur pour la classe {@code Round}.
     *
     * @param roundNumber le numéro du round.
     * @param players la liste des joueurs participant.
     * @param console l'interface utilisateur graphique.
     * @param plateau le plateau de jeu.
     */
    public Round(int roundNumber, List<Player> players, ConsoleGUI console, Plateau plateau) {
        this.roundNumber = roundNumber;
        this.players = players;
        this.console = console;
        this.plateau = plateau;
    }

    /**
     * Démarre le round, gère les choix des cartes, les actions des joueurs, et les scores.
     */
    public void start() {
        console.println("Round " + roundNumber + " commence !");
        playersChooseCards();
        executeActions();
        plateau.checkPlateau(console);
        calculateScores();
        console.println("Round " + roundNumber + " terminé.");
    }

    /**
     * Permet aux joueurs de choisir leurs cartes pour le round.
     */
    private void playersChooseCards() {
        console.println("Les joueurs doivent choisir leurs cartes :");
        for (Player player : players) {
            console.println("C'est au tour de " + player.getPlayerName() + " de choisir.");
            player.chooseOrder(console);
        }
    }

    /**
     * Permet aux joueurs de placer leurs premiers bateaux sur le plateau.
     */
    public void placeFirstShips() {
        for (Player player : players) {
            console.println("C'est à " + player.getPlayerName() + " de placer ses deux premiers bateaux.");
            player.firstShip(plateau, console);
            player.firstShip(plateau, console);
        }
    }

    /**
     * Exécute les actions des joueurs dans l'ordre déterminé.
     */
    private void executeActions() {
        List<List<Player>> turnOrder = determineTurnOrder();

        for (int i = 0; i < 3; i++) {
            console.println("Tour " + (i + 1) + " des joueurs.");
            for (Player player : turnOrder.get(i)) {
                console.println("C'est au tour de " + player.getPlayerName() + ".");
                player.Card(i, plateau, console);
            }
        }
    }

    /**
     * Détermine l'ordre des joueurs pour chaque tour en fonction des cartes choisies.
     *
     * @return une liste d'ordres pour chaque tour.
     */
    private List<List<Player>> determineTurnOrder() {
        List<List<Player>> orderPerRound = new ArrayList<>();

        // Itérer sur les 3 cartes (ou rounds)
        for (int round = 0; round < 3; round++) {
            List<Player> orderedPlayers = new ArrayList<>(players);
            Map<Integer, Integer> cardUsageCount = new HashMap<>();

            // Compter combien de fois chaque carte est utilisée
            for (Player player : players) {
                int cardId = player.getCardsId().get(round);
                cardUsageCount.put(cardId, cardUsageCount.getOrDefault(cardId, 0) + 1);
            }

            // Ajuster la puissance des cartes en fonction du nombre d'utilisations
            for (Player player : players) {
                int cardId = player.getCardsId().get(round);
                CommandCard card = player.getCards().get(round);
                int usageCount = cardUsageCount.get(cardId);
                if (usageCount == 1) {
                    card.setPower(1); // Un seul joueur : power = 1
                } else if (usageCount == 2) {
                    card.setPower(2); // Deux joueurs : power = 2
                } else {
                    card.setPower(3); // Trois joueurs ou plus : power = 3
                }
            }

            // Trier les joueurs selon leurs cartes pour ce round
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


    /**
     * Calcule les scores pour le round actuel.
     */
    private void calculateScores() {
        Game.getInstance().calculateScore(plateau.getPlateau(), players, console);
    }
}
