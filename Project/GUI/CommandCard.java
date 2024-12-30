package Project.GUI;

import java.util.*;

public class CommandCard {
    private int id;
    private int power = 1;
    private final Random random = new Random(); // Générateur aléatoire partagé

    public CommandCard(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public int getPower() {
        return this.power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    private void afficherPlateau(ConsoleGUI console) {
        console.println(
                "  [ 1] [ 2] #  [ 8] [ 9] #  [15] [16]\n" +
                        "[ 3][ 4][ 5]#[10][11][12]#[17][18][19]\n" +
                        "  [ 6] [ 7] #  [13] [14] #  [20] [21]\n" +
                        "######################################\n" +
                        "[22][23][24]#[30]   [31]#[35][36][37]\n" +
                        "  [25] [26] #    [32]   #  [   38] [39]\n" +
                        "[27][28][29]#[33]   [34]#[40][41][42]\n" +
                        "######################################\n" +
                        "  [43] [44] #  [50] [51] #  [57] [58]\n" +
                        "[45][46][47]#[52][53][54]#[59][60][61]\n" +
                        "  [48] [49] #  [55] [56] #  [62] [63]"
        );
    }

    private List<java.awt.Color> getHexColors(Hex hex, ConsoleGUI console) {
        List<java.awt.Color> colors = new ArrayList<>();
        for (Map.Entry<Player, Integer> entry : hex.getOccupation().entrySet()) {
            Player player = entry.getKey();
            int ships = entry.getValue();
            for (int i = 0; i < ships; i++) {
                colors.add(console.getColorFromName(player.getColor()));
            }
        }
        return colors;
    }


    public void expand(Player player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        int maxShipsToAdd = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            case 3 -> 1;
            default -> 0;
        };

        if (maxShipsToAdd == 0) {
            console.println(player.getPlayerName() + " ne peut pas ajouter de bateaux en raison de la puissance de la carte.");
            return;
        }

        console.println(player.getPlayerName() + " va étendre ses forces !");
        List<Hex> controlledHexes = new ArrayList<>();
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupation().containsKey(player)) {
                        controlledHexes.add(hex);
                    }
                }
            }
        }

        if (controlledHexes.isEmpty()) {
            console.println("Aucun hexagone contrôlé pour ajouter des bateaux.");
            return;
        }

        while (maxShipsToAdd > 0) {
            console.println("Vous avez encore " + maxShipsToAdd + " bateau(x) à placer.");
            console.println("Choisissez un hexagone contrôlé parmi la liste suivante :");
            for (int i = 0; i < controlledHexes.size(); i++) {
                console.println((i + 1) + ". Hexagone ID: " + controlledHexes.get(i).getId());
            }

            int choix = getValidInput(console, 1, controlledHexes.size());
            Hex selectedHex = controlledHexes.get(choix - 1);
            selectedHex.addShip(player, 1);
            console.placeShipInHex(selectedHex.getId(), player.getColor());

            console.println("Un bateau a été ajouté à l'hexagone ID: " + selectedHex.getId());
            maxShipsToAdd--;
        }

        console.println("Extension terminée pour " + player.getPlayerName() + ".");
    }

    public void vExpend(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        int maxShipsToAdd = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            case 3 -> 1;
            default -> 0;
        };

        if (maxShipsToAdd == 0) {
            console.println(player.getPlayerName() + " ne peut pas ajouter de bateaux en raison de la puissance de la carte.");
            return;
        }

        console.println(player.getPlayerName() + " va étendre ses forces !");
        List<Hex> controlledHexes = new ArrayList<>();
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupation().containsKey(player)) {
                        controlledHexes.add(hex);
                    }
                }
            }
        }

        if (controlledHexes.isEmpty()) {
            console.println("Aucun hexagone contrôlé pour ajouter des bateaux.");
            return;
        }

        Random random = new Random();

        while (maxShipsToAdd > 0 && !controlledHexes.isEmpty()) {
            Hex selectedHex = controlledHexes.get(random.nextInt(controlledHexes.size()));

            selectedHex.addShip(player, 1);
            console.placeShipInHex(selectedHex.getId(), player.getColor());
            console.println(player.getPlayerName() + " a ajouté un bateau à l'hexagone ID: " + selectedHex.getId());

            maxShipsToAdd--;
        }

        console.println("Extension terminée pour " + player.getPlayerName() + ".");
    }

    public void explore(Player player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        console.println(player.getPlayerName() + " va explorer !");

        List<Hex> playerHexes = new ArrayList<>();
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupation().containsKey(player)) {
                        playerHexes.add(hex);
                    }
                }
            }
        }

        if (playerHexes.isEmpty()) {
            console.println("Vous n'avez aucune flotte à déplacer.");
            return;
        }

        int maxFleets = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int fleet = 0; fleet < maxFleets; fleet++) {
            console.println("Flotte " + (fleet + 1) + "/" + maxFleets);
            console.println("Choisissez un hexagone de départ parmi ceux que vous contrôlez :");
            for (int i = 0; i < playerHexes.size(); i++) {
                console.println((i + 1) + ". Hexagone ID: " + playerHexes.get(i).getId());
            }

            int departChoix = getValidInput(console, 1, playerHexes.size());
            Hex hexDepart = playerHexes.get(departChoix - 1);
            List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);
            adjacentHexes.removeIf(hex -> !hex.getOccupation().isEmpty());

            if (adjacentHexes.isEmpty()) {
                console.println("Aucun hexagone adjacent valide pour explorer.");
                continue;
            }

            console.println("Choisissez un hexagone cible parmi les suivants :");
            for (int i = 0; i < adjacentHexes.size(); i++) {
                console.println((i + 1) + ". Hexagone ID: " + adjacentHexes.get(i).getId());
            }

            int cibleChoix = getValidInput(console, 1, adjacentHexes.size());
            Hex hexCible = adjacentHexes.get(cibleChoix - 1);

            // Nombre de bateaux à déplacer
            int shipsToMove = hexDepart.getOccupation().get(player);

            // Mise à jour des hexagones dans les données du jeu
            hexDepart.removeShip(player, shipsToMove);
            hexCible.addShip(player, shipsToMove);

            // Mise à jour graphique
            for (int i = 0; i < shipsToMove; i++) {
                console.removeShipsFromHex(hexDepart.getId(), player.getColor()); // Retire un point du départ
                console.placeShipInHex(hexCible.getId(), player.getColor()); // Ajoute un point à la cible
            }

            console.println(player.getPlayerName() + " a déplacé " + shipsToMove + " bateau(x) vers l'hexagone ID: " + hexCible.getId());
        }
    }

    public void vExplore(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        console.println(player.getPlayerName() + " va explorer !");
        List<Hex> ownedHexes = player.getOwnedHexes(plateau);

        if (ownedHexes.isEmpty()) {
            console.println(player.getPlayerName() + " n'a aucune flotte à déplacer.");
            return;
        }

        int maxFleets = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        Random random = new Random();

        for (int fleet = 0; fleet < maxFleets; fleet++) {
            if (ownedHexes.isEmpty()) break;

            // Sélectionner un hexagone de départ aléatoire
            Hex hexDepart = ownedHexes.get(random.nextInt(ownedHexes.size()));

            // Vérifier si le joueur a des bateaux sur cet hexagone
            Integer shipsAvailable = hexDepart.getOccupation().get(player);
            if (shipsAvailable == null || shipsAvailable <= 0) {
                ownedHexes.remove(hexDepart); // Retirer les hexagones sans bateaux de la liste
                continue;
            }

            List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);

            // Filtrer les hexagones adjacents non occupés
            adjacentHexes.removeIf(hex -> !hex.getOccupation().isEmpty());

            if (adjacentHexes.isEmpty()) {
                console.println("Aucun hexagone adjacent valide pour explorer à partir de " + hexDepart.getId() + ".");
                continue;
            }

            // Choisir un hexagone cible aléatoire
            Hex hexCible = adjacentHexes.get(random.nextInt(adjacentHexes.size()));

            // Déterminer combien de bateaux déplacer
            int shipsToMove = random.nextInt(shipsAvailable) + 1;

            // Mettre à jour les occupations
            hexDepart.removeShip(player, shipsToMove);
            hexCible.addShip(player, shipsToMove);

            // Mettre à jour la carte graphique
            console.updateHexShips(hexDepart.getId(), getHexColors(hexDepart, console));
            console.updateHexShips(hexCible.getId(), getHexColors(hexCible, console));

            console.println(player.getPlayerName() + " a déplacé " + shipsToMove + " bateau(x) de l'hexagone " +
                    hexDepart.getId() + " à l'hexagone " + hexCible.getId() + ".");
        }
    }

    public void invade(Player player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        console.println(player.getPlayerName() + " prépare une invasion !");

        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int i = 0; i < maxInvasions; i++) {
            List<Hex> adjacentEnemyHexes = new ArrayList<>();

            // Identifier les hexagones ennemis adjacents
            for (String niveau : plateau.keySet()) {
                for (SectorCard sector : plateau.get(niveau)) {
                    for (Hex hex : sector.getHex().values()) {
                        if (hex.getOccupation().containsKey(player)) {
                            // Ajouter les hexagones ennemis adjacents
                            for (Hex adjacentHex : hex.rexAdjacent(plateau)) {
                                if (!adjacentHex.getOccupation().isEmpty() && !adjacentHex.getOccupation().containsKey(player)) {
                                    adjacentEnemyHexes.add(adjacentHex);
                                }
                            }
                        }
                    }
                }
            }

            if (adjacentEnemyHexes.isEmpty()) {
                console.println("Aucun hexagone ennemi adjacent à envahir.");
                return;
            }

            console.println("Choisissez un hexagone à envahir parmi les suivants :");
            for (int j = 0; j < adjacentEnemyHexes.size(); j++) {
                console.println((j + 1) + ". Hexagone ID: " + adjacentEnemyHexes.get(j).getId());
            }

            int choix = -1;
            try {
                choix = Integer.parseInt(console.getInputSync().trim());
                if (choix < 1 || choix > adjacentEnemyHexes.size()) {
                    console.println("Choix invalide. Veuillez réessayer.");
                    continue;
                }
            } catch (NumberFormatException e) {
                console.println("Entrée invalide. Veuillez entrer un nombre.");
                continue;
            }

            Hex hexCible = adjacentEnemyHexes.get(choix - 1);

            console.println("Combien de bateaux voulez-vous utiliser pour attaquer ? Max disponible : " + player.getShipNumber());
            int shipsToUse = Math.min(Integer.parseInt(console.getInputSync().trim()), player.getShipNumber());

            if (shipsToUse > 0) {
                // Résoudre le combat
                Player defender = hexCible.getOccupation().keySet().iterator().next();
                int defenderShips = hexCible.getOccupation().get(defender);

                int minShips = Math.min(shipsToUse, defenderShips);
                shipsToUse -= minShips;
                defenderShips -= minShips;

                // Mise à jour des hexagones
                if (shipsToUse > 0) {
                    hexCible.addShip(player, shipsToUse);
                    console.placeShipInHex(hexCible.getId(), player.getColor());
                }

                if (defenderShips == 0) {
                    hexCible.getOccupation().remove(defender);
                } else {
                    hexCible.getOccupation().put(defender, defenderShips);
                    console.removeShipsFromHex(hexCible.getId(), defender.getColor());
                }

                console.println(player.getPlayerName() + " a envahi l'hexagone ID: " + hexCible.getId());
            }
        }

        console.println("Invasion terminée pour " + player.getPlayerName() + ".");
    }

    public void vInvade(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        console.println(player.getPlayerName() + " prépare une invasion !");

        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        Random random = new Random();

        for (int i = 0; i < maxInvasions; i++) {
            List<Hex> adjacentEnemyHexes = new ArrayList<>();

            // Identifier les hexagones ennemis adjacents
            for (String niveau : plateau.keySet()) {
                for (SectorCard sector : plateau.get(niveau)) {
                    for (Hex hex : sector.getHex().values()) {
                        if (hex.getOccupation().containsKey(player)) {
                            // Ajouter les hexagones ennemis adjacents
                            for (Hex adjacentHex : hex.rexAdjacent(plateau)) {
                                if (!adjacentHex.getOccupation().isEmpty() && !adjacentHex.getOccupation().containsKey(player)) {
                                    adjacentEnemyHexes.add(adjacentHex);
                                }
                            }
                        }
                    }
                }
            }

            if (adjacentEnemyHexes.isEmpty()) {
                console.println(player.getPlayerName() + " n'a aucun hexagone à envahir.");
                return;
            }

            Hex hexCible = adjacentEnemyHexes.get(random.nextInt(adjacentEnemyHexes.size()));
            int shipsToUse = Math.min(player.getShipNumber(), random.nextInt(5) + 1);

            if (shipsToUse > 0) {
                // Résoudre le combat
                Player defender = hexCible.getOccupation().keySet().iterator().next();
                int defenderShips = hexCible.getOccupation().get(defender);

                int minShips = Math.min(shipsToUse, defenderShips);
                shipsToUse -= minShips;
                defenderShips -= minShips;

                // Mise à jour des hexagones
                if (shipsToUse > 0) {
                    hexCible.addShip(player, shipsToUse);
                    console.placeShipInHex(hexCible.getId(), player.getColor());
                }

                if (defenderShips == 0) {
                    hexCible.getOccupation().remove(defender);
                } else {
                    hexCible.getOccupation().put(defender, defenderShips);
                    console.removeShipsFromHex(hexCible.getId(), defender.getColor());
                }

                console.println(player.getPlayerName() + " a envahi l'hexagone ID: " + hexCible.getId());
            }
        }

        console.println("Invasion terminée pour " + player.getPlayerName() + ".");
    }

    private int getValidInput(ConsoleGUI console, int min, int max) {
        int input = -1;
        while (input < min || input > max) {
            try {
                String rawInput = console.getInputSync();
                input = Integer.parseInt(rawInput.trim());
                if (input < min || input > max) {
                    console.println("Erreur : Veuillez entrer un nombre entre " + min + " et " + max + ".");
                }
            } catch (NumberFormatException e) {
                console.println("Erreur : Entrée invalide. Veuillez entrer un nombre.");
            }
        }
        return input;
    }


    public void executeCard(Player player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        System.out.println("Exécution de la carte " + id + " pour " + player.getPlayerName() + " avec le power " + power);
        if (player instanceof VirtualPlayer) {
            if (id == 1) { // Expand
                vExpend((VirtualPlayer) player, plateau, console);
            } else if (id == 2) { // Explore
                vExplore((VirtualPlayer) player, plateau, console);
            } else if (id == 3) { // Invade
                vInvade((VirtualPlayer) player, plateau, console);
            }else {
                System.out.println("Carte inconnue. Veuillez vérifier l'ID de la carte.");
            }
        } else {
            if (id == 1) { // Expand
                expand(player, plateau, console);
            } else if (id == 2) { // Explore
                explore(player, plateau, console);
            } else if (id == 3) { // Invade
                invade(player, plateau, console);
            } else {
                System.out.println("Carte inconnue. Veuillez vérifier l'ID de la carte.");
            }

        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CommandCard that = (CommandCard) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
