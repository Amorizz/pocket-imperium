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
        Scanner scanner = new Scanner(System.in);
        System.out.println(player.getPlayerName() + " va étendre ses forces !");
        List<Hex> availableHexes = new ArrayList<>();
        Map<Integer, Hex> hexMapping = new HashMap<>();

        // Remplir la liste des hexagones valides
        int index = 1;
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    availableHexes.add(hex);
                    hexMapping.put(index, hex);
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
        afficherPlateau();

        Hex selectedHex = null;
        while (selectedHex == null) {
            System.out.println("Choisissez un hexagone valide pour effectuer une expansion :");
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
                selectedHex = null; // Réinitialiser pour refaire le choix
            }
        }

        // Ajouter un bateau pour le joueur dans l'hexagone
        selectedHex.addShip(player, 1);
        System.out.println(player.getPlayerName() + " a ajouté un bateau sur : " + selectedHex);
    }

    public void explore(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        Scanner scanner = new Scanner(System.in);
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

        if (adjacentHexes.isEmpty()) {
            System.out.println("Aucun hexagone adjacent valide pour explorer.");
            return;
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

        System.out.println(player.getPlayerName() + " a décidé d'explorer depuis " + hexDepart + " vers " + hexCible);
    }

    public void invade(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        Scanner scanner = new Scanner(System.in);
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

        System.out.println(player.getPlayerName() + " a décidé d'envahir " + hexCible);
    }

    public void executeCard(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
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