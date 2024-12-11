package Project;

import java.util.*;

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

    public int getShipNumber() {
        return this.shipNumber;
    }

    public void setShipNumber(int number) {
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
            System.out.println("Sélectionnez l'ordre d'exécution pour la carte " + (order.size() + 1)
                    + " (ID de la carte entre 1 et 3) :");
            try {
                int choix = scanner.nextInt(); // Utiliser un Scanner pour lire l'entrée de l'utilisateur
                if (choix < 1 || choix > 3) {
                    System.out.println("Erreur : L'ID de la carte doit être entre 1 et 3.");
                } else {
                    CommandCard selectedCard = new CommandCard(choix - 1); // Créer une nouvelle carte avec l'ID choisi
                    if (order.contains(selectedCard)) { // Vérification de la carte déjà sélectionnée
                        System.out.println(
                                "Erreur : Vous avez déjà sélectionné cette carte. Veuillez choisir un ID différent.");
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

    private List<Hex> getAvailableHexes() {
        //renvoi la liste des hex qui sont tel que secteur est innocupé et lhex est un sys innocupé
        return null;
        
    }

    public void placeFirstShips() {
        List<Hex> availableHexes = getAvailableHexes(); // Méthode pour obtenir les hexagones disponibles
        try (Scanner scanner = new Scanner(System.in)) { // Utilisation de try-with-resources
            int shipsToPlace = 2; // Nombre de bateaux à placer
            int placedShips = 0;

            while (placedShips < shipsToPlace) {
                System.out.println("Choisissez un hexagone parmi les suivants :");
                for (int i = 0; i < availableHexes.size(); i++) {
                    System.out.println((i + 1) + ". " + availableHexes.get(i));
                }

                int choix = -1;
                while (choix < 1 || choix > availableHexes.size()) {
                    System.out.print("Votre choix (1-" + availableHexes.size() + ") : ");
                    choix = scanner.nextInt();
                    if (choix < 1 || choix > availableHexes.size()) {
                        System.out.println("Choix invalide, veuillez réessayer.");
                    }
                }

                Hex selectedHex = availableHexes.get(choix - 1);
                if (selectedHex.getShipon() < selectedHex.getMaxshipon()) {
                    selectedHex.addShip(1); // Ajouter un bateau à l'hexagone
                    selectedHex.setOccupation(this.color); // Marquer l'hexagone comme occupé par la couleur du joueur
                    /*int sectorNumber = selectedHex.getSectorNumber();
                     SectorCard sector = getSectorByNumber(sectorNumber);
                     if (sector != null) {
                         sector.setOccupation(this.color);
                     }*/
                    placedShips++;
                    this.shipNumber++; // Augmenter le nombre de bateaux du joueur
                    System.out.println("Bateau placé sur " + selectedHex);
                } else {
                    System.out.println("Cet hexagone est déjà plein. Veuillez choisir un autre hexagone.");
                }
            }

            this.shipNumber += shipsToPlace; // Mettre à jour le nombre de bateaux du joueur
            System.out.println("Tous les bateaux ont été placés.");
        }
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

    public String isValide(Hex hex, Player player, int index) {
        // Vérifier si l'hexagone est déjà occupé
        if (hex.getOccupation() != null) {
            return "X"; // Hexagone non valide
        }

        // Vérifier si le joueur peut placer un bateau
        if (hex.getShipon() < hex.getMaxshipon()) {
            return Integer.toString(index); // Retourner l'ID valide de l'hexagone
        }

        return "X"; // Sinon, hexagone non valide
    }


    public void afficherPlateauStylise(HashMap<String, ArrayList<SectorCard>> plateau, Player player) {
        StringBuilder affichage = new StringBuilder();
        int hexCount = 1; // Compteur pour numérotation des hexagones

        // Parcourir les niveaux du plateau
        for (String niveau : plateau.keySet()) {
            ArrayList<SectorCard> sectors = plateau.get(niveau);

            for (SectorCard sector : sectors) {
                // Créer une ligne pour chaque secteur
                affichage.append("| ");

                Map<Integer, Hex> hexes = sector.getHex();
                for (Hex hex : hexes.values()) {
                    String resultat = isValide(hex, player,hexCount);
                    if (resultat.equals("X")) {
                        affichage.append("[X]  "); // Hexagone non valide
                    } else {
                        affichage.append("[").append(hexCount).append("]  "); // Hexagone valide avec numéro
                    }
                    hexCount++;
                }

                affichage.append("| "); // Fin de la ligne du secteur
            }

            // Séparation visuelle entre les niveaux
            affichage.append("\n").append("#############################################################").append("\n");
        }

        // Afficher le résultat complet
        System.out.println(affichage.toString());
    }


    public void placeFirstShips(HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> availableHexes = new ArrayList<>();
        Map<Integer, Hex> hexMapping = new HashMap<>(); // Map pour associer les numéros aux hexagones

        // Remplir la liste des hexagones valides et créer une map associant les indices
        int index = 1; // Indice affiché pour l'utilisateur
        for (String niveau : plateau.keySet()) {
            ArrayList<SectorCard> sectors = plateau.get(niveau);
            for (SectorCard sector : sectors) {
                Map<Integer, Hex> hexes = sector.getHex();
                for (Hex hex : hexes.values()) {
                    if (isValide(hex, this, index).equals(Integer.toString(index))) {
                        availableHexes.add(hex);
                        hexMapping.put(index, hex);
                    }
                    index++;
                }
            }
        }

        // Vérifier s'il y a des hexagones valides
        if (availableHexes.isEmpty()) {
            System.out.println("Aucun hexagone disponible pour placer les bateaux.");
            return;
        }

        // Afficher le plateau stylisé avec les hexagones valides ou non
        afficherPlateauStylise(plateau, this);

        // Placement des bateaux
        System.out.println("Placement des premiers bateaux pour " + this.playerName);
        int shipsToPlace = 2; // Nombre de bateaux à placer
        Scanner scanner = new Scanner(System.in);

        while (shipsToPlace > 0) {
            System.out.println("Choisissez un hexagone valide (affiché avec un numéro) pour placer un bateau :");

            int choix = -1;
            while (!hexMapping.containsKey(choix)) {
                try {
                    System.out.print("Votre choix : ");
                    choix = scanner.nextInt();
                    if (!hexMapping.containsKey(choix)) {
                        System.out.println("Choix invalide ou hexagone occupé. Veuillez réessayer.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : veuillez entrer un nombre valide.");
                    scanner.next(); // Nettoyer l'entrée incorrecte
                }
            }

            // Placer un bateau sur l'hexagone choisi
            Hex selectedHex = hexMapping.get(choix);
            selectedHex.addShip(1); // Ajouter un bateau
            selectedHex.setOccupation(this.color); // Définir l'occupation par le joueur
            shipsToPlace--;
            System.out.println("Bateau placé sur l'hexagone : " + selectedHex);

            // Mettre à jour l'affichage après le placement
            afficherPlateauStylise(plateau, this);
        }

        System.out.println("Tous les bateaux ont été placés pour " + this.playerName + ".");
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