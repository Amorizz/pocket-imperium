package Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private int shipNumber;
    private String playerName;
    private List<CommandCard> cards;
    private int points;
    private Scanner scanner;
    private String color;

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getShipNumber(){
        return this.shipNumber;
    }

    public void setShipNumber(int number){
        this.shipNumber = number;
    }

    public Player(int shipNumber, String playerName, int points) {
        this.shipNumber = shipNumber;
        this.playerName = playerName;
        this.cards = new ArrayList<CommandCard>();
        this.points = points;
        this.scanner = new Scanner(System.in);
        this.color = null;
    }

    public String getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color.getColorName();
    }

    public void chooseOrder() {
        List<CommandCard> order = new ArrayList<CommandCard>();
        while (order.size() < 3) {
            System.out.println("Sélectionnez l'ordre d'exécution pour la carte " + (order.size() + 1) + " (ID de la carte entre 1 et 3) :");
            try {
                int choix = scanner.nextInt(); // Utiliser un Scanner pour lire l'entrée de l'utilisateur
                if (choix < 1 || choix > 3) {
                    System.out.println("Erreur : L'ID de la carte doit être entre 1 et 3.");
                } else {
                    CommandCard selectedCard = new CommandCard(choix - 1); // Créer une nouvelle carte avec l'ID choisi
                    if (order.contains(selectedCard)) { // Vérification de la carte déjà sélectionnée
                        System.out.println("Erreur : Vous avez déjà sélectionné cette carte. Veuillez choisir un ID différent.");
                    } else {
                        order.add(selectedCard); // Ajouter la carte sélectionnée à l'ordre
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur : Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Nettoyer l'entrée incorrecte
            }
        }
        this.cards = order; // Mettre à jour la liste des cartes du joueur
    }

    public void useCard(int cardId) {

    }

    public void placeFirstShips() {
        /*demande au joueur sur quel hexagone placer et cmb de bateaxu 
        - verifie tout les critère :
            - sector innocupé 
            - l'hex est bien un system

        - place les bateaux et addshipNumber pour player
        - doit gerer les erreur et intergait avec l'utilisateur
        - utilisation de while pour etre sur que les bateaux ont été placé
        */
    }

    public int getShips() {
        return shipNumber;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<CommandCard> getCards() {
        return new ArrayList<>(cards);
    }
    public List<Integer> getCardsId() {
        List<Integer> ids = new ArrayList<>();
        for (CommandCard card : cards) {
            ids.add(card.getId());
        }
        return ids;
    }

    /*public static void main(String[] args) {
        Player joueur = new Player(5, "Jean-Pierre", 10);
        joueur.placeShips(5); // Placer 5 vaisseaux
        System.out.println("Nombre de vaisseaux du joueur : " + joueur.getShips());
        System.out.println("Nom du joueur : " + joueur.getPlayerName());
        joueur.chooseOrder(); // Sélectionner l'ordre des cartes
        System.out.println("Cartes du joueur : " + joueur.getCardsId());
        // Tester l'utilisation d'une carte
        joueur.useCard(1);     }*/

}