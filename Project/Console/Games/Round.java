package Project.Console.Games;

import Project.Console.Card.CommandCard;
import Project.Console.Entity.Player;

import java.util.*;

/**
 * La classe {@code Round} représente un tour de jeu dans le cadre d'une partie.
 * Elle gère les actions des joueurs, le placement initial des bateaux, et le calcul des scores
 * à la fin du round.
 */
public class Round {
    private int roundNumber; // Le numéro du round
    private List<Player> players; // La liste des joueurs participant au round
    private Plateau plateau; // Le plateau de jeu utilisé durant le round

    /**
     * Constructeur de la classe {@code Round}.
     *
     * @param roundNumber le numéro du round.
     * @param players     la liste des joueurs participant au round.
     * @param plateau     le plateau de jeu utilisé durant le round.
     */
    public Round(int roundNumber, List<Player> players, Plateau plateau) {
        this.roundNumber = roundNumber;
        this.players = players;
        this.plateau = plateau;
    }

    /**
     * Démarre le round en suivant les étapes :
     * - Placement initial des bateaux.
     * - Exécution des actions des joueurs.
     * - Vérification des règles sur le plateau.
     */
    public void startRound() {
        System.out.println("Round " + roundNumber + " started.");
        placeFirstShips();
        executePlayerActions();
        checkPlateau();
        System.out.println("Round " + roundNumber + " completed.");
    }

    /**
     * Permet à chaque joueur de placer leurs deux premiers bateaux sur le plateau.
     */
    private void placeFirstShips() {
        for (Player player : players) {
            System.out.println("C'est à " + player.getPlayerName() + " de placer ses deux premiers bateaux.");
            player.firstShip(plateau);
            player.firstShip(plateau);
        }
    }

    /**
     * Gère les actions des joueurs durant le round :
     * - Les joueurs choisissent leurs cartes.
     * - Les actions des joueurs sont exécutées dans l'ordre défini par leurs cartes.
     */
    private void executePlayerActions() {
        System.out.println("Les joueurs doivent choisir leurs cartes.");
        for (Player player : players) {
            player.chooseOrder();
        }

        List<List<Player>> orderedPlayers = sensPlayer();

        for (int i = 0; i < 3; i++) { // Trois cartes à jouer
            System.out.println("Tour " + (i + 1) + " des actions.");
            for (Player player : orderedPlayers.get(i)) {
                System.out.println("C'est à " + player.getPlayerName() + " de jouer.");
                player.Card(i, plateau);
            }
        }
    }

    /**
     * Détermine l'ordre des joueurs pour chaque tour d'action en fonction de leurs cartes choisies.
     *
     * @return une liste de listes représentant l'ordre des joueurs pour chaque tour.
     */
    private List<List<Player>> sensPlayer() {
        List<List<Player>> orderPerRound = new ArrayList<>();
        for (int round = 0; round < 3; round++) {
            List<Player> orderedPlayers = new ArrayList<>(players);
            Map<Integer, Integer> commandCount = new HashMap<>();

            for (Player player : orderedPlayers) {
                int cardId = player.getCardsId().get(round);
                commandCount.put(cardId, commandCount.getOrDefault(cardId, 0) + 1);
            }

            for (Player player : orderedPlayers) {
                int cardId = player.getCardsId().get(round);
                CommandCard card = player.getCards().get(round);
                card.setPower(Math.min(commandCount.get(cardId), 3));
            }

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
     * Vérifie l'état du plateau après l'exécution des actions des joueurs.
     */
    private void checkPlateau() {
        plateau.checkPlateau();
    }

    /**
     * Calcule les scores des joueurs à la fin du round en fonction de leur domination sur le plateau.
     */
    public void calculateScore() {
        plateau.calculateScore(players);
    }
}
