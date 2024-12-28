package Project;

import java.util.*;

public class VirtualPlayer extends Player {
    private Random random;
    private List<CommandCard> card;

    public VirtualPlayer(String playerName, int points) {
        super(playerName, points);
        this.random = new Random();
    }

    @Override
    public void chooseOrder() {
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

        this.card = order; // Assurez-vous de mettre à jour l'attribut cards
        if (this.card.isEmpty()) {
            System.out.println(getPlayerName() + " n'a pas pu sélectionner de cartes."); // Debug
        } else {
            System.out.println(getPlayerName() + " a choisi l'ordre suivant pour ses cartes : " + getCardsId());
        }
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
    public void Card(int id, Plateau plateau) {
        card.get(id).executeCard(this, plateau.getPlateau());
    }

    @Override
    public void firstShip(Plateau plateau) {
        System.out.println(getPlayerName() + " place son premier bateau.");
        List<Hex> validHexes = getValidHexes(plateau.getPlateau());

        if (validHexes.isEmpty()) {
            System.out.println("Aucun hexagone disponible pour placer le premier bateau.");
            return;
        }

        Hex selectedHex = validHexes.get(random.nextInt(validHexes.size())); // Choix aléatoire
        selectedHex.addShip(this, 1); // Ajouter un bateau pour le joueur
        System.out.println(getPlayerName() + " a placé un bateau sur : " + selectedHex);
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
