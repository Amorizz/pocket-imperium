package Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;

public class Game {
    private static Game instance;
    private List<Player> players;
    private List<Round> rounds;
    private static SectorCard[][] plateau;
    private int currentRoundIndex;
    private String state;

    private Game() {
        players = new ArrayList<>();
        rounds = new ArrayList<>();
        plateau = new SectorCard[3][3];
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
        if (players.size() > 1) {
            state = "in progress";
            createRounds(5); // Par exemple, créer 5 rounds
            startNextRound();
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
                game.addPlayer(new Player(0, nomJoueur, null, 0));
            }
            game.start();
            System.out.println("État du jeu : " + game.getState());
            System.out.println("Liste des joueurs : " + game.getPlayerNames());
            
            game.createRounds(9);
            game.startNextRound();

    
        }
        // Creation triPri
        SectorCard triPri = new SectorCard(4,true,false,false);
        // Creation des 3 cartes top
        for (int i = 0; i < 3;i++){
            SectorCard carteb = new SectorCard(i,false,false,true);
            plateau[0][i] = carteb;
        }
        // Creation des 3 cartes bottom
        for (int i = 0; i < 3;i++){
            SectorCard cartet = new SectorCard(i,false,true,false);
            plateau[2][i] = cartet;
        }
        // Creation des 2 cartes mid
        SectorCard cartem1 = new SectorCard(1,false,false,false);
        plateau[1][0] = cartem1;
        plateau[1][1] = triPri;
        SectorCard cartem2 = new SectorCard(2,false,false,false);
        plateau[1][2] = cartem2;

        System.out.println(plateau[1][1].getHexa(3));

        

    }
}

