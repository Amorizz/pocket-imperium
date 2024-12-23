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

    public void expand(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        if (id == 1) { // Expend
            expend(player, plateau);
        } else if (id == 2) { // Explore
            explore(player, plateau);
        } else if (id == 3) { // Invade
            invade(player, plateau);
        }
    }

    private void expend(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(player.getPlayerName() + " va étendre ses forces !");
        List<Hex> availableHexes = new ArrayList<>();
        Map<Integer, Hex> hexMapping = new HashMap<>();

        int index = 1;
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupants().isEmpty() || hex.getOccupants().contains(player)) {
                        availableHexes.add(hex);
                        hexMapping.put(index, hex);
                    }
                    index++;
                }
            }
        }

        if (availableHexes.isEmpty()) {
            System.out.println("Aucun hexagone disponible pour l'expansion.");
            return;
        }

        afficherPlateauStylise(plateau, player);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choisissez un hexagone valide (affiché avec un numéro) pour étendre un bateau :");
        int choix = -1;

        while (!hexMapping.containsKey(choix)) {
            try {
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();
                if (!hexMapping.containsKey(choix)) {
                    System.out.println("Hexagone invalide. Veuillez réessayer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre valide !");
                scanner.next();
            }
        }

        Hex selectedHex = hexMapping.get(choix);
        if (selectedHex.getShipon() < selectedHex.getMaxshipon()) {
            selectedHex.addShip(1);
            selectedHex.setOccupation(player);
            System.out.println("Bateau étendu sur : " + selectedHex);
        } else {
            System.out.println("Cet hexagone est déjà plein.");
        }
    }

    private void explore(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(player.getPlayerName() + " va explorer !");
        List<Hex> hexagonesJoueur = new ArrayList<>();
        Hex hexDepart = null;
        Hex hexCible = null;

        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupants().contains(player)) {
                        hexagonesJoueur.add(hex);
                    }
                }
            }
        }

        if (hexagonesJoueur.isEmpty()) {
            System.out.println("Vous n'avez aucune flotte à déplacer.");
            return;
        }

        afficherHexagones(hexagonesJoueur, "Choisissez un hexagone de départ :");
        Scanner scanner = new Scanner(System.in);
        int choixDepart = lireChoix(scanner, hexagonesJoueur.size());

        hexDepart = hexagonesJoueur.get(choixDepart - 1);
        List<Hex> hexagonesAdjacents = hexDepart.rexAdjacent(plateau);
        hexagonesAdjacents.removeIf(hex -> hex.getOccupants().contains(player));

        if (hexagonesAdjacents.isEmpty()) {
            System.out.println("Aucun hexagone adjacent valide pour déplacer la flotte.");
            return;
        }

        afficherHexagones(hexagonesAdjacents, "Choisissez un hexagone cible :");
        int choixCible = lireChoix(scanner, hexagonesAdjacents.size());

        hexCible = hexagonesAdjacents.get(choixCible - 1);

        int shipsToMove = hexDepart.getShipon();
        System.out.println("Combien de bateaux voulez-vous déplacer ? (Max : " + shipsToMove + ")");
        int choixShips = lireChoix(scanner, shipsToMove);

        hexDepart.removeShip(choixShips);
        hexCible.addShip(choixShips);
        hexCible.setOccupation(player);

        if (hexDepart.getShipon() == 0) {
            hexDepart.clearOccupation();
        }
        System.out.println("Flotte déplacée de " + hexDepart + " à " + hexCible + ".");
    }

    private void invade(Player player, HashMap<String, ArrayList<SectorCard>> plateau) {
        System.out.println(player.getPlayerName() + " prépare une invasion !");
        List<Hex> hexagonesCibles = new ArrayList<>();

        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (!hex.getOccupants().isEmpty() && !hex.getOccupants().contains(player)) {
                        hexagonesCibles.add(hex);
                    }
                }
            }
        }

        if (hexagonesCibles.isEmpty()) {
            System.out.println("Aucun hexagone cible trouvé pour l'invasion.");
            return;
        }

        afficherHexagones(hexagonesCibles, "Choisissez un hexagone à envahir :");
        Scanner scanner = new Scanner(System.in);
        int choix = lireChoix(scanner, hexagonesCibles.size());

        Hex hexCible = hexagonesCibles.get(choix - 1);
        resolveCombat(player, hexCible);
    }

    private void afficherPlateauStylise(HashMap<String, ArrayList<SectorCard>> plateau, Player player) {
        int index = 1;
        System.out.println("Affichage du plateau :");
        for (String niveau : plateau.keySet()) {
            for (SectorCard sector : plateau.get(niveau)) {
                for (Hex hex : sector.getHex().values()) {
                    if (hex.getOccupants().contains(player)) {
                        System.out.print("[" + index + "] ");
                    } else if (hex.getOccupants().isEmpty()) {
                        System.out.print("[ ] ");
                    } else {
                        System.out.print("[X] ");
                    }
                    index++;
                }
                System.out.println();
            }
        }
    }

    private void afficherHexagones(List<Hex> hexagones, String message) {
        System.out.println(message);
        for (int i = 0; i < hexagones.size(); i++) {
            System.out.println((i + 1) + ". " + hexagones.get(i));
        }
    }

    private int lireChoix(Scanner scanner, int max) {
        int choix = -1;
        while (choix < 1 || choix > max) {
            try {
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre valide !");
                scanner.next();
            }
        }
        return choix;
    }

    private void resolveCombat(Player attacker, Hex hexCible) {
        // À compléter avec une logique de combat
        System.out.println("Résolution du combat entre " + attacker.getPlayerName() + " et les occupants de " + hexCible);
    }
}
