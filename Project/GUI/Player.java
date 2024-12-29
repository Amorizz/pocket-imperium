package Project.GUI;

import java.util.*;

public class Player {
    private String playerName;
    private List<CommandCard> cards;
    private int points;
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
        this.cards = new ArrayList<>();
        this.points = points;
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

    public void chooseOrder(ConsoleGUI console) {
        List<CommandCard> order = new ArrayList<>();
        Set<Integer> selectedIds = new HashSet<>();

        while (order.size() < 3) {
            console.println("Sélectionnez l'ordre d'exécution pour la carte " + (order.size() + 1) + " (ID de la carte entre 1 et 3) :");
            try {
                int choix = Integer.parseInt(console.getInputSync().trim());
                if (choix < 1 || choix > 3) {
                    console.println("Erreur : L'ID de la carte doit être entre 1 et 3.");
                } else if (selectedIds.contains(choix)) {
                    console.println("Erreur : Vous avez déjà sélectionné cette carte. Veuillez choisir un ID différent.");
                } else {
                    selectedIds.add(choix);
                    CommandCard selectedCard = new CommandCard(choix);
                    order.add(selectedCard);
                }
            } catch (NumberFormatException e) {
                console.println("Erreur : Entrée invalide. Veuillez entrer un nombre entier.");
            }
        }
        this.cards = order;
        console.println(getPlayerName() + " a choisi l'ordre suivant : " + getCardsId());
    }

    public void firstShip(Plateau plateau, ConsoleGUI console) {
        console.println(getPlayerName() + " place son premier bateau.");
        List<Hex> availableHexes = new ArrayList<>();
        Map<Integer, Hex> hexMapping = new HashMap<>();

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

        if (availableHexes.isEmpty()) {
            console.println("Aucun hexagone disponible pour placer le premier bateau.");
            return;
        }

        Hex selectedHex = null;

        while (selectedHex == null) {
            console.println("Choisissez un hexagone pour placer votre premier bateau :");
            plateau.afficherPlateau(console);

            try {
                int choix = Integer.parseInt(console.getInputSync().trim());
                if (!hexMapping.containsKey(choix)) {
                    console.println("Hexagone invalide. Veuillez réessayer.");
                    continue;
                }
                selectedHex = hexMapping.get(choix);
                console.println("Vous avez choisi l'hexagone : " + selectedHex);
                console.println("Confirmez-vous ce choix ? (oui/non)");
                String confirmation = console.getInputSync().trim();
                if (!confirmation.equalsIgnoreCase("oui")) {
                    selectedHex = null;
                }
            } catch (NumberFormatException e) {
                console.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        }

        selectedHex.addShip(this, 1);
        console.println(getPlayerName() + " a placé un bateau sur : " + selectedHex);
    }

    public void Card(int id, Plateau plateau, ConsoleGUI console) {
        cards.get(id).executeCard(this, plateau.getPlateau(), console);
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
}
