package Project;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private int shipNumber;
    private String playerName;
    private List<Integer> cards;
    private int points;
    private String name;
    private Scanner scanner;
    /* private enum Color */

    public Player(int shipNumber, String playerName, int points) {
        this.shipNumber = shipNumber;
        this.playerName = playerName;
        this.cards = new ArrayList<Integer>();
        this.points = points;
        this.scanner = new Scanner(System.in);
    }

    public void chooseOrder() { //
        List<Integer> order = new ArrayList<Integer>();
        while (order.size() < 3) {
            System.out.println("Sélectionnez l'ordre d'exécution pour la carte " + (order.size() + 1) + " (ID de la carte entre 1 et 3) :");
            try {
                int choix = scanner.nextInt(); // Utiliser un Scanner pour lire l'entrée de l'utilisateur
                if (choix < 1 || choix > 3) {
                    System.out.println("Erreur : L'ID de la carte doit être entre 1 et 3.");
                } else if (order.contains(choix - 1)) { // Vérification de l'ID de carte similaire
                    System.out.println("Erreur : Vous avez déjà sélectionné cette carte. Veuillez choisir un ID différent.");
                } else {
                    order.add(choix - 1); // Ajouter l'ID de carte avec soustraction pour l'indice
                }
            } catch (Exception e) {
                System.out.println("Erreur : Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Nettoyer l'entrée incorrecte
            }
        }
        this.cards = order;
    }

    public void useCard(int cardId) {

    }

    public void placeShips(int numberOfShips) {
        // Logique pour placer un certain nombre de vaisseaux
    }

    public int getShips() {
        return shipNumber;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<Integer> getCards() {
        return new ArrayList<>(cards);
    }

    public static void main(String[] args) {
        Player joueur = new Player(5, "Jean-Pierre", 10);
        joueur.placeShips(5); // Placer 5 vaisseaux
        System.out.println("Nombre de vaisseaux du joueur : " + joueur.getShips());
        System.out.println("Nom du joueur : " + joueur.getPlayerName());
        joueur.chooseOrder(); // Sélectionner l'ordre des cartes
        System.out.println("Cartes du joueur : " + joueur.getCards());
        // Tester l'utilisation d'une carte
        joueur.useCard(1);     }

}