package Project.GUI.Entity;

import Project.GUI.Card.CommandCard;
import Project.GUI.Card.SectorCard;
import Project.GUI.Console.ConsoleGUI;
import Project.GUI.Games.Plateau;
import Project.GUI.Games.Hex;

import java.util.*;

/**
 * La classe {@code VirtualPlayer} représente un joueur virtuel dans le jeu.
 * Elle hérite de la classe {@code Player} et implémente des comportements spécifiques
 * pour gérer les actions du joueur contrôlé par l'ordinateur, tels que le placement de bateaux
 * et le choix des cartes de commande.
 */
public class VirtualPlayer extends Player {
    private Random random;
    private List<CommandCard> card;
    private int shipNbr;

    /**
     * Constructeur de la classe {@code VirtualPlayer}.
     *
     * @param playerName le nom du joueur virtuel.
     * @param points     les points initiaux du joueur virtuel.
     */
    public VirtualPlayer(String playerName, int points) {
        super(playerName, points);
        this.random = new Random();
        this.card = new ArrayList<>();
        this.shipNbr = 15;
    }

    /**
     * Retourne la liste des cartes de commande du joueur.
     * Si la liste est nulle, elle est initialisée pour éviter une exception.
     *
     * @return la liste des cartes de commande.
     */
    @Override
    public List<CommandCard> getCards() {
        if (card == null) {
            card = new ArrayList<>(); // Éviter un NullPointerException
        }
        return card;
    }

    /**
     * Permet au joueur virtuel de choisir aléatoirement un ordre de cartes de commande.
     * L'ordre est composé de trois cartes uniques parmi trois options.
     */
    @Override
    public void chooseOrder(ConsoleGUI console) {
        List<CommandCard> order = new ArrayList<>();
        Set<Integer> selectedIds = new HashSet<>();

        while (order.size() < 3) {
            int randomChoice = random.nextInt(3) + 1; // Choix aléatoire entre 1 et 3

            if (!selectedIds.contains(randomChoice)) {
                selectedIds.add(randomChoice);
                CommandCard selectedCard = new CommandCard(randomChoice);
                order.add(selectedCard);
            }
        }

        this.card = order; // Mettre à jour l'attribut `card`
        console.println(getPlayerName() + " a choisi l'ordre suivant pour ses cartes : " + getCardsId());
    }

    /**
     * Retourne les identifiants des cartes de commande du joueur.
     *
     * @return une liste des identifiants des cartes.
     */
    @Override
    public List<Integer> getCardsId() {
        List<Integer> ids = new ArrayList<>();
        for (CommandCard card : card) {
            ids.add(card.getId());
        }
        return ids;
    }

    /**
     * Exécute une carte de commande spécifique pour le joueur virtuel.
     *
     * @param id      l'indice de la carte à exécuter.
     * @param plateau le plateau de jeu.
     */
    @Override
    public void Card(int id, Plateau plateau, ConsoleGUI console) {
        card.get(id).executeCard(this, plateau.getPlateau(), console);
    }

    /**
     * Permet au joueur virtuel de placer son premier bateau sur le plateau.
     * Le placement se fait aléatoirement parmi les hexagones valides.
     *
     * @param plateau le plateau de jeu.
     */
    @Override
    public void firstShip(Plateau plateau, ConsoleGUI console) {
        console.println(getPlayerName() + " place un bateau aléatoirement dans un hexagone.");

        List<Hex> availableHexes = new ArrayList<>();
        for (String niveau : plateau.getPlateau().keySet()) {
            for (SectorCard sector : plateau.getPlateau().get(niveau)) {
                availableHexes.addAll(sector.getHex().values());
            }
        }

        if (availableHexes.isEmpty()) {
            console.println("Aucun hexagone disponible pour placer un bateau.");
            return;
        }

        // Sélectionner un hexagone aléatoire
        Hex selectedHex = availableHexes.get(new Random().nextInt(availableHexes.size()));
        selectedHex.addShip(this, 1);

        // Ajouter visuellement un point dans l'hexagone
        console.placeShipInHex(selectedHex.getId(), getColor());

        console.println(getPlayerName() + " a placé un bateau sur l'hexagone : " + selectedHex.getId());
    }

    /**
     * Retourne une liste des hexagones contrôlés par le joueur virtuel.
     *
     * @param plateau le plateau de jeu.
     * @return une liste des hexagones contrôlés.
     */
    public List<Hex> getOwnedHexes(HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> ownedHexes = new ArrayList<>();
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    for (Ship ship : hex.getOccupation()) {
                        if (ship.getPlayerName().equals(this)) {
                            ownedHexes.add(hex);
                        }
                    }
                }
            }
        }
        return ownedHexes;
    }
}
