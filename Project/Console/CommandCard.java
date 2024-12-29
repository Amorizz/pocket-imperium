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

    public int getPower() {
        return this.power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    private void afficherPlateauStylise(HashMap<String, ArrayList<SectorCard>> plateau, Player player) {
        int index = 1;
        System.out.println("Affichage du plateau :");

        for (String niveau : plateau.keySet()) {
            ArrayList<SectorCard> sectors = plateau.get(niveau);

            for (int row = 0; row < sectors.size(); row++) {
                SectorCard sector = sectors.get(row);
                Map<Integer, Hex> hexes = sector.getHex();

                // Afficher la première ligne avec décalage
                if (row % 2 == 0) {
                    System.out.print(" ");
                }

                // Afficher les hexagones
                for (Hex hex : hexes.values()) {
                    if (hex.getOccupation() == null || hex.getOccupation().containsKey(player)) {
                        System.out.print("[" + index + "] ");
                    } else {
                        System.out.print("[X] ");
                    }
                    index++;
                }

                // Ajouter les séparateurs entre les secteurs
                if ((row + 1) % 3 == 0) {
                    System.out.println("\n###########"); // Ligne de séparation
                } else {
                    System.out.println();
                }
            }
        }
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
            System.out.println("Aucun hexagone contrôlé par " + player.getPlayerName() + " pour ajouter des bateaux.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Ajouter des bateaux jusqu'à atteindre la limite
        while (maxShipsToAdd > 0) {
            afficherPlateau();
            Hex selectedHex = null;

            // Demander à l'utilisateur de sélectionner un hexagone valide
            while (selectedHex == null) {
                System.out.println("Vous pouvez encore ajouter " + maxShipsToAdd + " bateau(x).");
                System.out.println("Choisissez un hexagone parmi ceux que vous contrôlez :");
                for (int i = 0; i < controlledHexes.size(); i++) {
                    System.out.println((i + 1) + ". " + controlledHexes.get(i));
                }

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
                    selectedHex = null;
                }
            }

            // Ajouter un bateau sur l'hexagone sélectionné
            selectedHex.addShip(player, 1);
            maxShipsToAdd--;
            System.out.println(player.getPlayerName() + " a ajouté un bateau sur : " + selectedHex);
        }

        System.out.println("Expansion terminée pour " + player.getPlayerName() + ".");
    }

    private void vExpend(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau) {
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
        List<Hex> controlledHexes = player.getOwnedHexes(plateau);

        if (controlledHexes.isEmpty()) {
            System.out.println("Aucun hexagone contrôlé par " + player.getPlayerName() + " pour ajouter des bateaux.");
            return;
        }

        Random random = new Random();

        // Répartition automatique des bateaux
        while (maxShipsToAdd > 0 && !controlledHexes.isEmpty()) {
            Hex selectedHex = controlledHexes.get(random.nextInt(controlledHexes.size())); // Choix aléatoire
            int currentShips = selectedHex.getOccupation().getOrDefault(player, 0);

            if (currentShips < selectedHex.getMaxshipon()) {
                selectedHex.addShip(player, 1);
                maxShipsToAdd--;
                System.out.println(player.getPlayerName() + " a ajouté un bateau sur : " + selectedHex);
            } else {
                // Si l'hexagone est plein, on le retire des hexagones contrôlés pour éviter de le rechoisir
                controlledHexes.remove(selectedHex);
            }
        }

        System.out.println("Expansion terminée pour " + player.getPlayerName() + ".");
    }

    public void explore(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(player.getPlayerName() + " va explorer !");

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

        // Déterminer le nombre maximum de flottes pouvant être déplacées
        int maxFleets = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int fleet = 0; fleet < maxFleets; fleet++) {
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

        for (int fleet = 0; fleet < maxFleets; fleet++) {
            if (ownedHexes.isEmpty()) break;

            Hex hexDepart = ownedHexes.get(random.nextInt(ownedHexes.size())); // Choix aléatoire de départ
            List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);

            // Filtrer les hexagones adjacents non occupés
            adjacentHexes.removeIf(hex -> !hex.getOccupation().isEmpty());

            if (adjacentHexes.isEmpty()) {
                System.out.println("Aucun hexagone adjacent valide pour explorer.");
                continue;
            }

            Hex hexCible = adjacentHexes.get(random.nextInt(adjacentHexes.size())); // Choix aléatoire de cible
            int shipsToMove = random.nextInt(hexDepart.getOccupation().get(player)) + 1; // Déplacer 1 à tous les bateaux disponibles

            hexDepart.removeShip(player, shipsToMove);
            hexCible.addShip(player, shipsToMove);

            System.out.println(player.getPlayerName() + " a déplacé " + shipsToMove + " bateau(x) de " + hexDepart + " à " + hexCible);
        }
    }

    public void invade(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(player.getPlayerName() + " prépare une invasion !");

        // Déterminer le nombre maximum de systèmes pouvant être envahis
        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int invasion = 0; invasion < maxInvasions; invasion++) {
            List<Hex> enemyHexes = new ArrayList<>();

            // Trouver les hexagones ennemis
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
                System.out.println("Aucun hexagone ennemi trouvé pour l'invasion.");
                break; // Arrêter si aucun hexagone à envahir
            }

            Hex hexCible = null;
            while (hexCible == null) {
                System.out.println("Choisissez un hexagone à envahir :");
                for (int i = 0; i < enemyHexes.size(); i++) {
                    System.out.println((i + 1) + ". " + enemyHexes.get(i));
                }

                int choix = -1;
                while (choix < 1 || choix > enemyHexes.size()) {
                    try {
                        System.out.print("Votre choix : ");
                        choix = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                        scanner.next();
                    }
                }

                hexCible = enemyHexes.get(choix - 1);
                System.out.println("Vous avez choisi d'envahir l'hexagone : " + hexCible);
                System.out.print("Confirmez-vous ce choix ? (oui/non) : ");
                String confirmation = scanner.next();
                if (!confirmation.equalsIgnoreCase("oui")) {
                    hexCible = null;
                }
            }

            // Déterminer les forces à utiliser
            int shipsToUse = 0;
            int shipsAvailable = player.getShipNumber();
            System.out.println("Combien de vaisseaux voulez-vous utiliser pour l'attaque ? (max : " + shipsAvailable + ")");
            while (shipsToUse < 1 || shipsToUse > shipsAvailable) {
                try {
                    System.out.print("Votre choix : ");
                    shipsToUse = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.next();
                }
            }

            // Ajouter les forces attaquantes à l'hexagone cible
            hexCible.addShip(player, shipsToUse);
            player.setShipNumber(player.getShipNumber() - shipsToUse);
            System.out.println(player.getPlayerName() + " a envoyé " + shipsToUse + " vaisseaux pour envahir " + hexCible);
        }

        // La résolution du combat sera gérée par la méthode `checkPlateau`
        System.out.println("Tous les combats seront résolus à la fin du tour.");
    }

    public void vInvade(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(player.getPlayerName() + " prépare une invasion !");

        // Déterminer le nombre maximum de systèmes pouvant être envahis
        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int invasion = 0; invasion < maxInvasions; invasion++) {
            List<Hex> enemyHexes = player.getEnemyHexes(plateau);

            if (enemyHexes.isEmpty()) {
                System.out.println(player.getPlayerName() + " n'a aucun hexagone cible à envahir.");
                break; // Arrêter si aucun hexagone à envahir
            }

            // Choisir un hexagone ennemi de manière aléatoire
            Hex hexCible = enemyHexes.get(random.nextInt(enemyHexes.size()));

            // Déterminer les forces à utiliser
            int shipsToUse = random.nextInt(10) + 1; // Nombre aléatoire entre 1 et 10

            // Ajouter les forces attaquantes à l'hexagone cible
            hexCible.addShip(player, shipsToUse);
            System.out.println(player.getPlayerName() + " a attaqué " + hexCible + " avec " + shipsToUse + " vaisseaux.");
        }

        // La résolution du combat sera gérée par la méthode `checkPlateau`
        System.out.println("Tous les combats seront résolus à la fin du tour.");
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