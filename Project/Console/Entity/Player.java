package Project.Console.Entity;

import Project.Console.Card.CommandCard;
import Project.Console.Card.SectorCard;
import Project.Console.Games.Color;
import Project.Console.Games.Hex;
import Project.Console.Games.Plateau;

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
    private Scanner scanner; // Scanner pour les entrées utilisateur
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
        this.cards = new ArrayList<CommandCard>();
        this.points = points;
        this.scanner = new Scanner(System.in);
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

    /**
     * Permet au joueur de placer un de ses premiers vaisseaux sur un hexagone disponible.
     *
     * @param plateau l'objet {@code Plateau} représentant le plateau de jeu.
     */
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

    /**
     * Joue une carte spécifique pour le joueur.
     *
     * @param id      l'index de la carte dans la liste des cartes du joueur.
     * @param plateau l'objet {@code Plateau} représentant le plateau de jeu.
     */
    public void Card(int id, Plateau plateau){
        cards.get(id).executeCard(this,plateau.getPlateau());
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