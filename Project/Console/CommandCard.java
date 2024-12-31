package Project.Console;

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

    public void setPower(int power) {
        this.power = power;
    }

    private void afficherPlateau(){
        System.out.println(""+
        "  [ 1] [ 2] #  [ 8] [ 9] #  [15] [16]"+"\n"+
        "[ 3][ 4][ 5]#[10][11][12]#[17][18][19]"+"\n"+
        "  [ 6] [ 7] #  [13] [14] #  [20] [21]"+"\n"+
        "######################################"+"\n"+
        "[22][23][24]#[30]   [31]#[35][36][37]"+"\n"+
        "  [25] [26] #    [32]   #  [   38] [39]"+"\n"+
        "[27][28][29]#[33]   [34]#[40][41][42]"+"\n"+
        "######################################"+"\n"+
        "  [43] [44] #  [50] [51] #  [57] [58]"+"\n"+
        "[45][46][47]#[52][53][54]#[59][60][61]"+"\n"+
        "  [48] [49] #  [55] [56] #  [62] [63]"
        );
    }

    public void expand(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        int maxShipsToAdd = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            case 3 -> 1;
            default -> 0;
        };

        if (maxShipsToAdd == 0) {
            System.out.println("Aucun bateau ne peut être ajouté en raison de la puissance de la carte.");
            return;
        }

        System.out.println(player.getPlayerName() + " va étendre ses forces !");
        List<Hex> controlledSystemHexes = new ArrayList<>();
        Map<Integer, Hex> hexMapping = new HashMap<>();

        int index = 1;
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupation().containsKey(player)) { // Vérification des systèmes contrôlés
                        controlledSystemHexes.add(hex);
                        hexMapping.put(index, hex);
                        index++;
                    }
                }
            }
        }

        if (controlledSystemHexes.isEmpty()) {
            System.out.println("Aucun système contrôlé par " + player.getPlayerName() + " pour ajouter des bateaux.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (maxShipsToAdd > 0) {
            System.out.println("Vous avez encore " + maxShipsToAdd + " bateau(x) à ajouter.");
            System.out.println("Choisissez un système contrôlé parmi les suivants :");
            for (int i = 0; i < controlledSystemHexes.size(); i++) {
                System.out.println((i + 1) + ". " + controlledSystemHexes.get(i));
            }

            int choix = -1;
            try {
                choix = Integer.parseInt(scanner.nextLine().trim());
                if (choix < 1 || choix > controlledSystemHexes.size()) {
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                continue;
            }

            Hex selectedHex = controlledSystemHexes.get(choix - 1);
            int currentShips = selectedHex.getOccupation().get(player);

            selectedHex.addShip(player, 1);
            maxShipsToAdd--;
            System.out.println("Un bateau a été ajouté au système : " + selectedHex);
        }

        System.out.println("Extension terminée pour " + player.getPlayerName() + ".");
    }

    public void vExpend(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau) {
        int maxShipsToAdd = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            case 3 -> 1;
            default -> 0;
        };

        if (maxShipsToAdd == 0) {
            System.out.println(player.getPlayerName() + " ne peut pas ajouter de bateaux en raison de la puissance de la carte.");
            return;
        }

        System.out.println(player.getPlayerName() + " va étendre ses forces !");
        List<Hex> controlledSystemHexes = new ArrayList<>();

        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupation().containsKey(player)) { // Vérification des systèmes contrôlés
                        controlledSystemHexes.add(hex);
                    }
                }
            }
        }

        if (controlledSystemHexes.isEmpty()) {
            System.out.println("Aucun système contrôlé par " + player.getPlayerName() + " pour ajouter des bateaux.");
            return;
        }

        Random random = new Random();

        while (maxShipsToAdd > 0 && !controlledSystemHexes.isEmpty()) {
            Hex selectedHex = controlledSystemHexes.get(random.nextInt(controlledSystemHexes.size()));
            int currentShips = selectedHex.getOccupation().get(player);

            selectedHex.addShip(player, 1);
            maxShipsToAdd--;
            System.out.println(player.getPlayerName() + " a ajouté un bateau au système : " + selectedHex);
        }

        System.out.println("Extension terminée pour " + player.getPlayerName() + ".");
    }

    public void explore(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(player.getPlayerName() + " va explorer !");

        // Déterminer le nombre maximum de flottes pouvant être déplacées
        int maxFleets = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int fleet = 0; fleet < maxFleets; fleet++) {
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
                System.out.println("Vous n'avez aucune flotte à déplacer.");
                return;
            }
            afficherPlateau();

            Hex hexDepart = null;
            while (hexDepart == null) {
                System.out.println("Choisissez un hexagone de départ :");
                for (int i = 0; i < playerHexes.size(); i++) {
                    System.out.println((i + 1) + ". " + playerHexes.get(i));
                }

                int choixDepart = -1;
                while (choixDepart < 1 || choixDepart > playerHexes.size()) {
                    try {
                        System.out.print("Votre choix : ");
                        choixDepart = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                        scanner.next();
                    }
                }

                hexDepart = playerHexes.get(choixDepart - 1);
                System.out.println("Vous avez choisi l'hexagone de départ : " + hexDepart);
                System.out.print("Confirmez-vous ce choix ? (oui/non) : ");
                String confirmation = scanner.next();
                if (!confirmation.equalsIgnoreCase("oui")) {
                    hexDepart = null;
                }
            }

            List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);
            adjacentHexes.removeIf(hex -> !hex.getOccupation().isEmpty()); // Exclure les hexagones occupés par d'autres joueurs

            if (adjacentHexes.isEmpty()) {
                System.out.println("Aucun hexagone adjacent valide pour explorer.");
                continue;
            }

            Hex hexCible = null;
            while (hexCible == null) {
                System.out.println("Choisissez un hexagone cible :");
                for (int i = 0; i < adjacentHexes.size(); i++) {
                    System.out.println((i + 1) + ". " + adjacentHexes.get(i));
                }

                int choixCible = -1;
                while (choixCible < 1 || choixCible > adjacentHexes.size()) {
                    try {
                        System.out.print("Votre choix : ");
                        choixCible = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                        scanner.next();
                    }
                }

                hexCible = adjacentHexes.get(choixCible - 1);
                System.out.println("Vous avez choisi l'hexagone cible : " + hexCible);
                System.out.print("Confirmez-vous ce choix ? (oui/non) : ");
                String confirmation = scanner.next();
                if (!confirmation.equalsIgnoreCase("oui")) {
                    hexCible = null;
                }
            }

            // Demander combien de ships déplacer
            int shipsToMove = 0;
            int shipsAvailable = hexDepart.getOccupation().get(player);
            System.out.println("Combien de bateaux voulez-vous déplacer ? (max : " + shipsAvailable + ")");
            while (shipsToMove < 1 || shipsToMove > shipsAvailable) {
                try {
                    System.out.print("Votre choix : ");
                    shipsToMove = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.next();
                }
            }

            // Déplacer les ships
            hexDepart.removeShip(player, shipsToMove);
            hexCible.addShip(player, shipsToMove);

            System.out.println(player.getPlayerName() + " a déplacé " + shipsToMove + " bateau(x) de " + hexDepart + " à " + hexCible);
        }
    }

    public void vExplore(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(player.getPlayerName() + " va explorer !");
        List<Hex> ownedHexes = player.getOwnedHexes(plateau);

        if (ownedHexes.isEmpty()) {
            System.out.println(player.getPlayerName() + " n'a aucune flotte à déplacer.");
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

            Hex hexDepart = ownedHexes.get(random.nextInt(ownedHexes.size())); // Choix aléatoire de départ

            // Vérifier que le joueur contrôle l'hexagone
            Integer playerShips = hexDepart.getOccupation().get(player);
            if (playerShips == null || playerShips <= 0) {
                System.out.println(player.getPlayerName() + " ne contrôle pas de vaisseaux dans " + hexDepart);
                ownedHexes.remove(hexDepart); // Retirer l'hexagone des options disponibles
                continue;
            }

            List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);

            // Filtrer les hexagones adjacents non occupés
            adjacentHexes.removeIf(hex -> !hex.getOccupation().isEmpty());

            if (adjacentHexes.isEmpty()) {
                System.out.println("Aucun hexagone adjacent valide pour explorer.");
                continue;
            }

            Hex hexCible = adjacentHexes.get(random.nextInt(adjacentHexes.size())); // Choix aléatoire de cible

            // Calculer les vaisseaux à déplacer
            int shipsToMove = random.nextInt(playerShips) + 1;

            hexDepart.removeShip(player, shipsToMove);
            hexCible.addShip(player, shipsToMove);

            System.out.println(player.getPlayerName() + " a déplacé " + shipsToMove + " bateau(x) de " + hexDepart + " à " + hexCible);
        }
    }

    public void invade(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(player.getPlayerName() + " prépare une invasion !");

        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int invasion = 0; invasion < maxInvasions; invasion++) {

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

            List<Hex> enemyHexes = new ArrayList<>();
            for (Hex hex : playerHexes) {
                for (Hex hexAdj : hex.rexAdjacent(plateau)) {
                    if (hexAdj.getOccupation().size() != 0 && !hexAdj.getOccupation().containsKey(player)) {
                        enemyHexes.add(hexAdj);
                    }
                }
            }

            if (enemyHexes.isEmpty()) {
                System.out.println("Aucun hexagone ennemi adjacent trouvé pour l'invasion.");
                break;
            }

            Hex hexCible = null;
            while (hexCible == null) {
                System.out.println("Choisissez un hexagone à envahir :");
                for (int i = 0; i < enemyHexes.size(); i++) {
                    System.out.println((i + 1) + ". " + enemyHexes.get(i));
                }

                int choix = -1;
                try {
                    System.out.print("Votre choix : ");
                    choix = scanner.nextInt();
                    if (choix < 1 || choix > enemyHexes.size()) {
                        System.out.println("Choix invalide. Veuillez réessayer.");
                        continue;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.next();
                    continue;
                }

                hexCible = enemyHexes.get(choix - 1);
            }

            System.out.println("Combien de bateaux voulez-vous envoyer pour attaquer ? Max disponible : " + player.getShipNumber());
            int shipsToUse = 0;
            while (shipsToUse < 1 || shipsToUse > player.getShipNumber()) {
                try {
                    System.out.print("Votre choix : ");
                    shipsToUse = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.next();
                }
            }

            player.setShipNumber(player.getShipNumber() - shipsToUse); // Réduire les bateaux disponibles
            hexCible.addShip(player, shipsToUse); // Ajouter les bateaux à l'hexagone cible
            System.out.println(player.getPlayerName() + " a envoyé " + shipsToUse + " vaisseaux pour envahir " + hexCible);
        }

        System.out.println("Invasion terminée pour " + player.getPlayerName() + ".");
    }

    public void vInvade(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(player.getPlayerName() + " prépare une invasion !");

        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        Random random = new Random();

        for (int invasion = 0; invasion < maxInvasions; invasion++) {
            List<Hex> enemyHexes = new ArrayList<>();

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

            for (Hex hex : playerHexes) {
                for (Hex hexAdj : hex.rexAdjacent(plateau)) {
                    if (hexAdj.getOccupation().size() != 0 && !hexAdj.getOccupation().containsKey(player)) {
                        enemyHexes.add(hexAdj);
                    }
                }
            }

            if (enemyHexes.isEmpty()) {
                System.out.println(player.getPlayerName() + " n'a aucun hexagone adjacent à envahir.");
                break;
            }

            Hex hexCible = enemyHexes.get(random.nextInt(enemyHexes.size())); // Choisir une cible aléatoire
            int shipsToUse = Math.min(random.nextInt(5) + 1, player.getShipNumber());

            player.setShipNumber(player.getShipNumber() - shipsToUse); // Réduire les bateaux disponibles
            hexCible.addShip(player, shipsToUse); // Ajouter les bateaux à l'hexagone cible
            System.out.println(player.getPlayerName() + " a envoyé " + shipsToUse + " vaisseaux pour envahir " + hexCible);
        }

        System.out.println("Invasion terminée pour " + player.getPlayerName() + ".");
    }

    public void executeCard(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println("Exécution de la carte " + id + " pour " + player.getPlayerName() + " avec le power " + power);
        if (player instanceof VirtualPlayer) {
            if (id == 1) { // Expand
                vExpend((VirtualPlayer) player, plateau);
            } else if (id == 2) { // Explore
                vExplore((VirtualPlayer) player, plateau);
            } else if (id == 3) { // Invade
                vInvade((VirtualPlayer) player, plateau);
            }else {
                System.out.println("Carte inconnue. Veuillez vérifier l'ID de la carte.");
            }
        } else {
            if (id == 1) { // Expand
                expand(player, plateau);
            } else if (id == 2) { // Explore
                explore(player, plateau);
            } else if (id == 3) { // Invade
                invade(player, plateau);
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