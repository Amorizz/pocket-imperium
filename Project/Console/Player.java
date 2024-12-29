package Project.Console;

import java.util.*;

public class Player {
    private String playerName;
    private List<CommandCard> cards;
    private int points;
    private Scanner scanner;
    private String color;
    private int shipNbr;

    public int getPoints() {
        return points;
    }

    public void resetPoints() {
        this.points = 0;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public Player(String playerName, int points) {
        this.playerName = playerName;
        this.cards = new ArrayList<CommandCard>();
        this.points = points;
        this.scanner = new Scanner(System.in);
        this.color = null;
        this.shipNbr = 15;
    }

    public int getShipNumber() {
        return shipNbr;
    }

    public void setShipNumber(int shipNbr) {
        this.shipNbr = shipNbr;
    }

    public String getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color.getColorName();
    }

    public void chooseOrder() {
        List<CommandCard> order = new ArrayList<>();
        Set<Integer> selectedIds = new HashSet<>(); // Pour suivre les cartes déjà sélectionnées

        while (order.size() < 3) {
            System.out.println("Sélectionnez l'ordre d'exécution pour la carte " + (order.size() + 1)
                    + " (ID de la carte entre 1 et 3) :");
            try {
                int choix = scanner.nextInt(); // Lire l'entrée de l'utilisateur
                if (choix < 1 || choix > 3) {
                    System.out.println("Erreur : L'ID de la carte doit être entre 1 et 3.");
                } else if (selectedIds.contains(choix)) { // Vérification si la carte est déjà sélectionnée
                    System.out.println("Erreur : Vous avez déjà sélectionné cette carte. Veuillez choisir un ID différent.");
                } else {
                    selectedIds.add(choix); // Ajouter l'ID à l'ensemble
                    CommandCard selectedCard = new CommandCard(choix); // Créer une nouvelle carte avec l'ID choisi
                    order.add(selectedCard); // Ajouter la carte à l'ordre
                }
            } catch (Exception e) {
                System.out.println("Erreur : Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Nettoyer l'entrée incorrecte
            }
        }
        this.cards = order; // Mettre à jour la liste des cartes du joueur
    }

    public void firstShip(Plateau plateau) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(getPlayerName() + " place son premier bateau.");
        List<Hex> availableHexes = new ArrayList<>();
        Map<Integer, Hex> hexMapping = new HashMap<>();

        // Construire la liste des hexagones disponibles pour le placement
        int index = 1;
        for (String niveau : plateau.getPlateau().keySet()) {
            for (SectorCard sector : plateau.getPlateau().get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    availableHexes.add(hex);
                    hexMapping.put(index, hex);
                    index++;
                }
            }
        }

        // Vérification des hexagones disponibles
        if (availableHexes.isEmpty()) {
            System.out.println("Aucun hexagone disponible pour placer le premier bateau.");
            return;
        }

        Hex selectedHex = null;

        // Afficher les hexagones disponibles et demander un choix
        while (selectedHex == null) {
            System.out.println("Choisissez un hexagone pour placer votre premier bateau :");
            plateau.afficherPlateau();

            int choix = -1;
            while (!hexMapping.containsKey(choix)) {
                try {
                    System.out.print("Votre choix : ");
                    choix = scanner.nextInt();
                    if (!hexMapping.containsKey(choix)) {
                        System.out.println("Hexagone invalide. Veuillez réessayer.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.next();
                }
            }

            selectedHex = hexMapping.get(choix);
            System.out.println("Vous avez choisi l'hexagone : " + selectedHex);
            System.out.print("Confirmez-vous ce choix ? (oui/non) : ");
            String confirmation = scanner.next();
            if (!confirmation.equalsIgnoreCase("oui")) {
                selectedHex = null; // Réinitialiser pour refaire le choix
            }
        }

        // Ajouter le bateau sur l'hexagone choisi
        selectedHex.addShip(this, 1);
        System.out.println(getPlayerName() + " a placé un bateau sur : " + selectedHex);
    }


    public void Card(int id, Plateau plateau){
        cards.get(id).executeCard(this,plateau.getPlateau());
    }

    private List<Hex> getAvailableHexes() {
        //renvoi la liste des hex qui sont tel que secteur est innocupé et lhex est un sys innocupé
        return null;

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

    /*
     * public static void main(String[] args) {
     * Player joueur = new Player(5, "Jean-Pierre", 10);
     * joueur.placeShips(5); // Placer 5 vaisseaux
     * System.out.println("Nombre de vaisseaux du joueur : " + joueur.getShips());
     * System.out.println("Nom du joueur : " + joueur.getPlayerName());
     * joueur.chooseOrder(); // Sélectionner l'ordre des cartes
     * System.out.println("Cartes du joueur : " + joueur.getCardsId());
     * // Tester l'utilisation d'une carte
     * joueur.useCard(1); }
     */

}