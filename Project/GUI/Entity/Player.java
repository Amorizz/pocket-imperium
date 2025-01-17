package Project.GUI.Entity;

import Project.GUI.Card.CommandCard;
import Project.GUI.Card.SectorCard;
import Project.GUI.Games.Color;
import Project.GUI.Console.ConsoleGUI;
import Project.GUI.Games.Plateau;
import Project.GUI.Games.Hex;

import java.util.*;

/**
 * La classe {@code Player} représente un joueur dans le jeu Pocket Imperium.
 * Elle contient les informations du joueur telles que son nom, ses points, ses cartes,
 * la couleur attribuée, et le nombre de vaisseaux qu'il possède.
 * Cette classe gère également les actions du joueur, comme le choix des cartes
 * et le placement des vaisseaux.
 */
public class Player {
    private String playerName; // Nom du joueur
    private List<CommandCard> cards; // Liste des cartes du joueur
    private int points; // Points accumulés par le joueur
    private String color; // Couleur attribuée au joueur
    private int shipNbr; // Nombre de vaisseaux que le joueur possède

    /**
     * Constructeur de la classe {@code Player}.
     *
     * @param playerName le nom du joueur.
     * @param points     le nombre initial de points du joueur.
     */
    public Player(String playerName, int points) {
        this.playerName = playerName;
        this.cards = new ArrayList<>();
        this.points = points;
        this.color = null;
        this.shipNbr = 15;
    }

    /**
     * Retourne le nombre de points accumulés par le joueur.
     *
     * @return les points du joueur.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Réinitialise les points du joueur à zéro.
     */
    public void resetPoints() {
        this.points = 0;
    }

    /**
     * Ajoute des points au score du joueur.
     *
     * @param points le nombre de points à ajouter.
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * Retourne le nombre de vaisseaux que possède le joueur.
     *
     * @return le nombre de vaisseaux.
     */
    public int getShipNumber() {
        return shipNbr;
    }

    /**
     * Définit le nombre de vaisseaux du joueur.
     *
     * @param shipNbr le nouveau nombre de vaisseaux.
     */
    public void setShipNumber(int shipNbr) {
        this.shipNbr = shipNbr;
    }

    /**
     * Retourne la couleur attribuée au joueur.
     *
     * @return la couleur du joueur sous forme de chaîne de caractères.
     */
    public String getColor() {
        return color;
    }

    /**
     * Attribue une couleur au joueur.
     *
     * @param color l'objet {@code Color} représentant la couleur.
     */
    public void setColor(Color color) {
        this.color = color.getColorName();
    }

    /**
     * Permet au joueur de choisir l'ordre des cartes à jouer pour ce tour.
     * Le joueur doit sélectionner trois cartes parmi celles disponibles.
     */
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

    /**
     * Permet au joueur de placer un de ses premiers vaisseaux sur un hexagone disponible.
     *
     * @param plateau l'objet {@code Plateau} représentant le plateau de jeu.
     */
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

        console.placeShipInHex(selectedHex.getId(), getColor());

        selectedHex.addShip(this, 1);
        System.out.println(getColor());
        console.println(getPlayerName() + " a placé un bateau sur : " + selectedHex);
    }

    /**
     * Joue une carte spécifique pour le joueur.
     *
     * @param id      l'index de la carte dans la liste des cartes du joueur.
     * @param plateau l'objet {@code Plateau} représentant le plateau de jeu.
     */
    public void Card(int id, Plateau plateau, ConsoleGUI console) {
        cards.get(id).executeCard(this, plateau.getPlateau(), console);
    }

    /**
     * Retourne le nom du joueur.
     *
     * @return le nom du joueur.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Retourne une copie des cartes du joueur.
     *
     * @return une liste des cartes du joueur.
     */
    public List<CommandCard> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Retourne les identifiants des cartes du joueur.
     *
     * @return une liste des identifiants des cartes du joueur.
     */
    public List<Integer> getCardsId() {
        List<Integer> ids = new ArrayList<>();
        for (CommandCard card : cards) {
            ids.add(card.getId());
        }
        return ids;
    }
}
