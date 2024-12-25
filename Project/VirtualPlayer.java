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
        while (order.size() < 3) {
            int randomChoice = random.nextInt(3) + 1; // Choix aléatoire entre 1 et 3
            CommandCard selectedCard = new CommandCard(randomChoice);

            if (!order.contains(selectedCard)) { // Vérification que la carte n'est pas déjà sélectionnée
                order.add(selectedCard);
            }
        }
        this.cards = order;
        System.out.println(getPlayerName() + " a choisi l'ordre suivant pour ses cartes : " + getCardsId());
    }

    public void VCard(CommandCard card, HashMap<String, ArrayList<SectorCard>> plateau) {
        switch (card.getId()) {
            case 1 -> expend(plateau);
            case 2 -> explore(plateau);
            case 3 -> invade(plateau);
        }
    }

    private void expend(HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(getPlayerName() + " va étendre ses forces !");
        List<Hex> availableHexes = getValidHexes(plateau);

        if (availableHexes.isEmpty()) {
            System.out.println("Aucun hexagone disponible pour l'expansion.");
            return;
        }

        Hex selectedHex = availableHexes.get(random.nextInt(availableHexes.size())); // Choix aléatoire
        if (selectedHex.getShipon() < selectedHex.getMaxshipon()) {
            selectedHex.addShip(1);
            selectedHex.setOccupation(this.getColor());
            System.out.println(getPlayerName() + " a étendu un bateau sur : " + selectedHex);
        } else {
            System.out.println("Cet hexagone est déjà plein.");
        }
    }

    private void explore(HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(getPlayerName() + " va explorer !");
        List<Hex> hexesOwned = getOwnedHexes(plateau);

        if (hexesOwned.isEmpty()) {
            System.out.println(getPlayerName() + " n'a aucune flotte à déplacer.");
            return;
        }

        Hex hexDepart = hexesOwned.get(random.nextInt(hexesOwned.size())); // Choix aléatoire de départ
        List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);
        adjacentHexes.removeIf(hex -> hex.getOccupation() != null); // Exclure les hexagones occupés

        if (adjacentHexes.isEmpty()) {
            System.out.println("Aucun hexagone adjacent valide pour déplacer la flotte.");
            return;
        }

        Hex hexCible = adjacentHexes.get(random.nextInt(adjacentHexes.size())); // Choix aléatoire de cible
        int shipsToMove = random.nextInt(hexDepart.getShipon()) + 1; // Déplacer 1 à tous les bateaux

        hexDepart.removeShip(shipsToMove);
        hexCible.addShip(shipsToMove);
        hexCible.setOccupation(this.getColor());

        if (hexDepart.getShipon() == 0) {
            hexDepart.setOccupation(null); // Réinitialiser si aucun bateau
        }

        System.out.println(getPlayerName() + " a déplacé " + shipsToMove + " bateau(x) de " + hexDepart + " à " + hexCible);
    }

    private void invade(HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(getPlayerName() + " prépare une invasion !");
        List<Hex> targets = getEnemyHexes(plateau);

        if (targets.isEmpty()) {
            System.out.println(getPlayerName() + " n'a aucun hexagone cible à envahir.");
            return;
        }

        Hex hexCible = targets.get(random.nextInt(targets.size())); // Choix aléatoire de cible
        resolveCombat(this, hexCible);
    }

    private List<Hex> getValidHexes(HashMap<String, ArrayList<SectorCard>> plateau) {
        List<Hex> validHexes = new ArrayList<>();
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupation() == null || hex.getOccupation().equals(this)) {
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
                    if (this.equals(hex.getOccupation())) {
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
                    if (hex.getOccupation() != null && !hex.getOccupation().equals(this)) {
                        enemyHexes.add(hex);
                    }
                }
            }
        }
        return enemyHexes;
    }
}
