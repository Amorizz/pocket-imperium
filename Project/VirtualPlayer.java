package Project;

import java.util.*;

public class VirtualPlayer extends Player {
    private Random random;

    public VirtualPlayer(int shipNumber, String playerName, int points) {
        super(shipNumber, playerName, points);
        this.random = new Random();
    }

    @Override
    public void chooseOrder() {
        List<CommandCard> order = new ArrayList<>();
        Set<Integer> selectedIds = new HashSet<>(); // Suivi des cartes déjà sélectionnées

        while (order.size() < 3) {
            int randomChoice = random.nextInt(3) + 1; // Choix aléatoire entre 1 et 3

            if (!selectedIds.contains(randomChoice)) { // Vérification si la carte a déjà été sélectionnée
                selectedIds.add(randomChoice);
                CommandCard selectedCard = new CommandCard(randomChoice);
                order.add(selectedCard);
            }
        }
        this.cards = order;
        System.out.println(getPlayerName() + " a choisi l'ordre suivant pour ses cartes : " + getCardsId());
    }

    @Override
    public void Card(int id, Plateau plateau) {
        switch (id) {
            case 1 -> expend(plateau.getPlateau());
            case 2 -> explore(plateau.getPlateau());
            case 3 -> invade(plateau.getPlateau());
        }
    }

    private void expend(HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(getPlayerName() + " va étendre ses forces !");
        List<Hex> validHexes = getValidHexes(plateau);

        if (validHexes.isEmpty()) {
            System.out.println("Aucun hexagone disponible pour l'expansion.");
            return;
        }

        Hex selectedHex = validHexes.get(random.nextInt(validHexes.size())); // Choix aléatoire
        int shipsOnHex = selectedHex.getOccupation().getOrDefault(this, 0);

        if (shipsOnHex < selectedHex.getMaxshipon()) {
            selectedHex.addShip(this, 1); // Ajouter un bateau pour le joueur
            System.out.println(getPlayerName() + " a étendu un bateau sur : " + selectedHex);
        } else {
            System.out.println("Cet hexagone est déjà plein.");
        }
    }

    private void explore(HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(getPlayerName() + " va explorer !");
        List<Hex> ownedHexes = getOwnedHexes(plateau);

        if (ownedHexes.isEmpty()) {
            System.out.println(getPlayerName() + " n'a aucune flotte à déplacer.");
            return;
        }

        Hex hexDepart = ownedHexes.get(random.nextInt(ownedHexes.size())); // Choix aléatoire de départ
        List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);

        // Filtrer les hexagones adjacents non occupés
        adjacentHexes.removeIf(hex -> !hex.getOccupation().isEmpty());

        if (adjacentHexes.isEmpty()) {
            System.out.println("Aucun hexagone adjacent valide pour explorer.");
            return;
        }

        Hex hexCible = adjacentHexes.get(random.nextInt(adjacentHexes.size())); // Choix aléatoire de cible
        int shipsToMove = random.nextInt(hexDepart.getOccupation().get(this)) + 1; // Déplacer 1 à tous les bateaux disponibles

        hexDepart.removeShip(this, shipsToMove);
        hexCible.addShip(this, shipsToMove);

        System.out.println(getPlayerName() + " a déplacé " + shipsToMove + " bateau(x) de " + hexDepart + " à " + hexCible);
    }

    private void invade(HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(getPlayerName() + " prépare une invasion !");
        List<Hex> enemyHexes = getEnemyHexes(plateau);

        if (enemyHexes.isEmpty()) {
            System.out.println(getPlayerName() + " n'a aucun hexagone cible à envahir.");
            return;
        }

        Hex hexCible = enemyHexes.get(random.nextInt(enemyHexes.size())); // Choix aléatoire de cible
        hexCible.addShip(this, random.nextInt(10)); // Ajouter un bateau pour le joueur
    }


    private List<Hex> getValidHexes(HashMap<String, ArrayList<SectorCard>> plateau) {
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

    private List<Hex> getOwnedHexes(HashMap<String, ArrayList<SectorCard>> plateau) {
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

    private List<Hex> getEnemyHexes(HashMap<String, ArrayList<SectorCard>> plateau) {
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
