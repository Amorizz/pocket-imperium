package Project;

import java.util.*;

public class CommandCard {
    private int id;

    public CommandCard(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
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
        "  [ 1] [ 2] #  [ 8] [ 9] #  [15] [16]"+
        "[ 3][ 4][ 5]#[10][11][12]#[17][18][19]"+
        "  [ 6] [ 7] #  [13] [14] #  [20] [21]"+
        "####################"+
        "[22][23][24]#[30]   [31]#[35][36][37]"+
        "  [25] [26] #    [32]   #  [38] [39]"+
        "[27][28][29]#[33]   [34]#[40][41][42]"+
        "######################################"+
        "  [43] [44] #  [50] [51] #  [57] [58]"+
        "[45][46][47]#[52][53][54]#[59][60][61]"+
        "  [48] [49] #  [55] [56] #  [62] [63]"+
        )
    }

    public void expand(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        if (id == 1) { // Expand
            System.out.println(player.getPlayerName() + " va étendre ses forces !");
            List<Hex> availableHexes = new ArrayList<>();
            Map<Integer, Hex> hexMapping = new HashMap<>();

            // Remplir la liste des hexagones valides
            int index = 1;
            for (String niveau : plateau.keySet()) {
                for (SectorCard sector : plateau.get(niveau)) {
                    for (Hex hex : sector.getHex().values()) {
                        int currentShips = hex.getOccupation().getOrDefault(player, 0);
                        if (currentShips < hex.getMaxshipon()) {
                            availableHexes.add(hex);
                            hexMapping.put(index, hex);
                        }
                        index++;
                    }
                }
            }

            // Vérification des hexagones disponibles
            if (availableHexes.isEmpty()) {
                System.out.println("Aucun hexagone disponible pour l'expansion.");
                return;
            }

            // Afficher le plateau stylisé
            afficherPlateauStylise(plateau, player);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Choisissez un hexagone valide pour étendre un bateau :");
            int choix = -1;

            // Choix de l'utilisateur
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

            // Placer le bateau
            Hex selectedHex = hexMapping.get(choix);
            selectedHex.addShip(player, 1);
            System.out.println("Bateau ajouté sur : " + selectedHex);
        } else if (id == 2) { // Explore
            System.out.println(player.getPlayerName() + " va explorer !");
            List<Hex> playerHexes = new ArrayList<>();

            // Trouver les hexagones occupés par le joueur
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

            // Afficher les hexagones occupés par le joueur
            afficherPlateauStylise(plateau, player);

            System.out.println("Choisissez un hexagone de départ :");
            for (int i = 0; i < playerHexes.size(); i++) {
                System.out.println((i + 1) + ". " + playerHexes.get(i));
            }

            Scanner scanner = new Scanner(System.in);
            int choixDepart = -1;

            // Choix de l'hexagone de départ
            while (choixDepart < 1 || choixDepart > playerHexes.size()) {
                try {
                    System.out.print("Votre choix : ");
                    choixDepart = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.next();
                }
            }

            Hex hexDepart = playerHexes.get(choixDepart - 1);
            List<Hex> adjacentHexes = hexDepart.rexAdjacent(plateau);

            // Filtrer les hexagones adjacents valides
            adjacentHexes.removeIf(hex -> hex.getOccupation().size() > 0);

            if (adjacentHexes.isEmpty()) {
                System.out.println("Aucun hexagone adjacent valide pour explorer.");
                return;
            }

            System.out.println("Choisissez un hexagone cible :");
            for (int i = 0; i < adjacentHexes.size(); i++) {
                System.out.println((i + 1) + ". " + adjacentHexes.get(i));
            }

            int choixCible = -1;

            // Choix de l'hexagone cible
            while (choixCible < 1 || choixCible > adjacentHexes.size()) {
                try {
                    System.out.print("Votre choix : ");
                    choixCible = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.next();
                }
            }

            Hex hexCible = adjacentHexes.get(choixCible - 1);
            int shipsToMove = hexDepart.getOccupation().get(player);

            System.out.println("Combien de bateaux voulez-vous déplacer ? (max : " + shipsToMove + ")");
            int choixShips = -1;

            while (choixShips < 1 || choixShips > shipsToMove) {
                try {
                    System.out.print("Votre choix : ");
                    choixShips = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.next();
                }
            }

            // Déplacer les bateaux
            hexDepart.removeShip(player, choixShips);
            hexCible.addShip(player, choixShips);
            System.out.println("Flotte déplacée de " + hexDepart + " à " + hexCible + ".");
        } else if (id == 3) { // Invade
            System.out.println(player.getPlayerName() + " prépare une invasion !");
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
                System.out.println("Aucun hexagone ennemi trouvé pour l'invasion.");
                return;
            }

            System.out.println("Choisissez un hexagone à envahir :");
            for (int i = 0; i < enemyHexes.size(); i++) {
                System.out.println((i + 1) + ". " + enemyHexes.get(i));
            }

            Scanner scanner = new Scanner(System.in);
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

            Hex hexCible = enemyHexes.get(choix - 1);

            // Forces des deux joueurs
            int forcesAttaquantes = 0;
            int forcesDéfensives = hexCible.getShipon();

            // Demander le nombre de vaisseaux à utiliser pour l'attaque
            System.out.println("Combien de vaisseaux voulez-vous utiliser pour attaquer ? (max : 10)");
            while (forcesAttaquantes <= 0 || forcesAttaquantes > 10) {
                try {
                    System.out.print("Nombre de vaisseaux : ");
                    forcesAttaquantes = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Veuillez entrer un nombre valide !");
                    scanner.next(); // Consomme l'entrée invalide
                }
            }

            hexCible.addShip(player, forcesAttaquantes);
        }
    }

}