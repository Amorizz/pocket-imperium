package Project.GUI;

import java.util.*;

public class VirtualPlayer extends Player {
    private Random random;
    private List<CommandCard> card;
    private int shipNbr;

    public VirtualPlayer(String playerName, int points) {
        super(playerName, points);
        this.random = new Random();
        this.card = new ArrayList<>();
        this.shipNbr = 15;
    }

    @Override
    public List<CommandCard> getCards() {
        if (card == null) {
            card = new ArrayList<>(); // Éviter un NullPointerException
        }
        return card;
    }

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

    @Override
    public List<Integer> getCardsId() {
        List<Integer> ids = new ArrayList<>();
        for (CommandCard card : card) {
            ids.add(card.getId());
        }
        return ids;
    }

    @Override
    public void Card(int id, Plateau plateau, ConsoleGUI console) {
        card.get(id).executeCard(this, plateau.getPlateau(), console);
    }

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


    public List<Hex> getValidHexes(HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> validHexes = new ArrayList<>();
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    int shipsForPlayer = hex.getOccupation().getOrDefault(this, 0);
                    if (shipsForPlayer < hex.getMaxshipon()) {
                        validHexes.add(hex);
                    }
                }
            }
        }
        return validHexes;
    }

    public List<Hex> getOwnedHexes(HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> ownedHexes = new ArrayList<>();
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupation().containsKey(this)) {
                        ownedHexes.add(hex);
                    }
                }
            }
        }
        return ownedHexes;
    }

    public List<Hex> getEnemyHexes(HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> enemyHexes = new ArrayList<>();
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (!hex.getOccupation().isEmpty() && !hex.getOccupation().containsKey(this)) {
                        enemyHexes.add(hex);
                    }
                }
            }
        }
        return enemyHexes;
    }
}
