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

    public void expand(Player player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        int maxShipsToAdd = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            case 3 -> 1;
            default -> 0;
        };

        if (maxShipsToAdd == 0) {
            console.println("Aucun bateau ne peut être ajouté en raison de la puissance de la carte.");
            return;
        }

        console.println(player.getPlayerName() + " va étendre ses forces !");
        List<Hex> controlledHexes = new ArrayList<>();
        Map<Integer, Hex> hexMapping = new HashMap<>();

        // Remplir la liste des hexagones contrôlés par le joueur
        int index = 1;
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupation().containsKey(player)) {
                        controlledHexes.add(hex);
                        hexMapping.put(index, hex);
                        index++;
                    }
                }
            }
        }

        // Vérification des hexagones disponibles
        if (controlledHexes.isEmpty()) {
            console.println("Aucun hexagone contrôlé par " + player.getPlayerName() + " pour ajouter des bateaux.");
            return;
        }

        // Ajouter des bateaux jusqu'à atteindre la limite
        while (maxShipsToAdd > 0) {
            afficherPlateau(console);
            Hex selectedHex = null;

            // Demander à l'utilisateur de sélectionner un hexagone valide
            while (selectedHex == null) {
                console.println("Vous pouvez encore ajouter " + maxShipsToAdd + " bateau(x).");
                console.println("Choisissez un hexagone parmi ceux que vous contrôlez :");
                for (int i = 0; i < controlledHexes.size(); i++) {
                    console.println((i + 1) + ". " + controlledHexes.get(i));
                }

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

            // Ajouter un bateau sur l'hexagone sélectionné
            selectedHex.addShip(player, 1);
            maxShipsToAdd--;
            console.println(player.getPlayerName() + " a ajouté un bateau sur : " + selectedHex);
        }

        console.println("Expansion terminée pour " + player.getPlayerName() + ".");
    }

    private void vExpend(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
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
        List<Hex> controlledHexes = player.getOwnedHexes(plateau);

        if (controlledHexes.isEmpty()) {
            console.println("Aucun hexagone contrôlé par " + player.getPlayerName() + " pour ajouter des bateaux.");
            return;
        }

        Random random = new Random();

        // Répartition automatique des bateaux
        while (maxShipsToAdd > 0 && !controlledHexes.isEmpty()) {
            Hex selectedHex = controlledHexes.get(random.nextInt(controlledHexes.size()));
            int currentShips = selectedHex.getOccupation().getOrDefault(player, 0);

            if (currentShips < selectedHex.getMaxshipon()) {
                selectedHex.addShip(player, 1);
                maxShipsToAdd--;
                console.println(player.getPlayerName() + " a ajouté un bateau sur : " + selectedHex);
            } else {
                controlledHexes.remove(selectedHex); // Retirer les hexagones pleins
            }
        }

        console.println("Expansion terminée pour " + player.getPlayerName() + ".");
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
                console.println((i + 1) + ". " + playerHexes.get(i));
            }

            Hex hexDepart = playerHexes.get(getValidInput(console, 1, playerHexes.size()) - 1);
            List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);
            adjacentHexes.removeIf(hex -> !hex.getOccupation().isEmpty());

            if (adjacentHexes.isEmpty()) {
                console.println("Aucun hexagone adjacent valide pour explorer.");
                continue;
            }

            console.println("Choisissez un hexagone cible parmi les suivants :");
            for (int i = 0; i < adjacentHexes.size(); i++) {
                console.println((i + 1) + ". " + adjacentHexes.get(i));
            }

            Hex hexCible = adjacentHexes.get(getValidInput(console, 1, adjacentHexes.size()) - 1);

            int shipsToMove = hexDepart.getOccupation().get(player);
            console.println(player.getPlayerName() + " déplace " + shipsToMove + " bateaux vers " + hexCible);
            hexDepart.removeShip(player, shipsToMove);
            hexCible.addShip(player, shipsToMove);
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
            Hex hexDepart = ownedHexes.get(random.nextInt(ownedHexes.size()));
            List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);
            adjacentHexes.removeIf(hex -> !hex.getOccupation().isEmpty());

            if (adjacentHexes.isEmpty()) {
                console.println("Aucun hexagone adjacent valide pour explorer.");
                continue;
            }

            Hex hexCible = adjacentHexes.get(random.nextInt(adjacentHexes.size()));
            int shipsToMove = random.nextInt(hexDepart.getOccupation().get(player)) + 1;

            hexDepart.removeShip(player, shipsToMove);
            hexCible.addShip(player, shipsToMove);
            console.println(player.getPlayerName() + " a déplacé " + shipsToMove + " bateaux de " + hexDepart + " à " + hexCible);
        }
    }

    public void invade(Player player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        console.println(player.getPlayerName() + " prépare une invasion !");

        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int invasion = 0; invasion < maxInvasions; invasion++) {
            console.println("Invasion " + (invasion + 1) + "/" + maxInvasions);
            List<Hex> enemyHexes = new ArrayList<>();

            for (String niveau : plateau.keySet()) {
                for (SectorCard sector : plateau.get(niveau)) {
                    for (Hex hex : sector.getHex().values()) {
                        if (!hex.getOccupation().isEmpty() && !hex.getOccupation().containsKey(player)) {
                            enemyHexes.add(hex);
                        }
                    }
                }
            }

            if (enemyHexes.isEmpty()) {
                console.println("Aucun hexagone ennemi trouvé pour l'invasion.");
                break;
            }

            console.println("Choisissez un hexagone à envahir :");
            for (int i = 0; i < enemyHexes.size(); i++) {
                console.println((i + 1) + ". " + enemyHexes.get(i));
            }

            Hex hexCible = enemyHexes.get(getValidInput(console, 1, enemyHexes.size()) - 1);

            console.println("Combien de vaisseaux voulez-vous envoyer ?");
            int shipsToUse = getValidInput(console, 1, player.getShipNumber());
            hexCible.addShip(player, shipsToUse);
            console.println(player.getPlayerName() + " a envahi " + hexCible + " avec " + shipsToUse + " vaisseaux.");
        }
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

    public void vInvade(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        console.println(player.getPlayerName() + " prépare une invasion !");

        // Déterminer le nombre maximum de systèmes pouvant être envahis
        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int invasion = 0; invasion < maxInvasions; invasion++) {
            List<Hex> enemyHexes = player.getEnemyHexes(plateau);

            if (enemyHexes.isEmpty()) {
                console.println(player.getPlayerName() + " n'a aucun hexagone cible à envahir.");
                break; // Arrêter si aucun hexagone à envahir
            }

            // Choisir un hexagone ennemi de manière aléatoire
            Hex hexCible = enemyHexes.get(random.nextInt(enemyHexes.size()));

            // Déterminer les forces à utiliser
            int shipsToUse = Math.min(random.nextInt(10) + 1, player.getShipNumber()); // Nombre aléatoire limité par les vaisseaux disponibles

            // Réduire les vaisseaux du joueur et attaquer l'hexagone
            player.setShipNumber(player.getShipNumber() - shipsToUse);
            hexCible.addShip(player, shipsToUse);

            console.println(player.getPlayerName() + " a attaqué " + hexCible + " avec " + shipsToUse + " vaisseaux.");
        }

        // Ajouter une notification pour la fin des invasions
        console.println("Tous les combats seront résolus à la fin du tour.");
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
