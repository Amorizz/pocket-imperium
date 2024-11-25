package Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

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
                game.addPlayer(nomJoueur);
            }
            game.start();
            System.out.println("État du jeu : " + game.getState());
            System.out.println("Liste des joueurs : " + game.getPlayers());

            //creer le round, les bateaux et toutes les autres classes ...
        }
    }
}

