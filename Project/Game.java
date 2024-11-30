package Project;

import java.lang.reflect.Array;
import java.util.*;

public class Game {
    private static Game instance;
    private List<Player> players;
    private List<Round> rounds;
    private Map<String, ArrayList<SectorCard>> plateau;
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

    public Map<String, ArrayList<SectorCard>> placeSectorCard(){
        ArrayList<SectorCard> top = new ArrayList<>();
        for (int i = 0; i < 3;i++){
            SectorCard ctop = new SectorCard(i,false,false,true);
            top.add(ctop);
        }
        plateau.put("Top",top);

        ArrayList<SectorCard> Mid =  new ArrayList<>();
        SectorCard mid1 = new SectorCard(2,false,false,false);
        Mid.add(mid1);
        SectorCard triPri = new SectorCard(4,true,false,false);
        Mid.add(triPri);
        SectorCard mid2 = new SectorCard(5,false,false,false);
        Mid.add(mid2);
        plateau.put("Mid",Mid);

        ArrayList<SectorCard> bottom = new ArrayList<>();
        for (int i = 0; i < 3;i++){
            SectorCard cbottom = new SectorCard(i,false,false,true);
            bottom.add(cbottom);
        }
        plateau.put("Bottom",bottom);
        return plateau;
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
                game.addPlayer(new Player(nomJoueur));
            }
            game.start();
            System.out.println("État du jeu : " + game.getState());
            System.out.println("Liste des joueurs : " + game.getPlayerNames());
            
            game.createRounds(9);
            game.startNextRound();

    
        }

        game.plateau = game.placeSectorCard();

        System.out.println(game.plateau.get("Mid").get(1));


    }
}

