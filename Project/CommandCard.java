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

    private void explore(String playerColor, Hex hexDepart, Hex hexCible, boolean shipLeft) {
        int nombreBateauxADeplacer = hexDepart.getShipon();
        if (shipLeft) {
            hexDepart.setShipon(1); // Laisse un bateaux sur l'hexa
            hexCible.setShipon(nombreBateauxADeplacer - 1); // Ajouter les bateaux à l'hexagone cible, sauf un
        } else {
            hexDepart.setShipon(0); // Retire tous les bateaux de l'hexa
            hexCible.setShipon(nombreBateauxADeplacer); // Ajouter tous les bateaux à l'hexagone cible
        }
        hexCible.setOccupation(playerColor); // Changer l'occupation de l'hexagone cible au joueur actuel
    }

    private void resolveCombat(String playerColor, Hex hexCible) {
        try (Scanner scanner = new Scanner(System.in)) {
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

            // Résolution du combat
            System.out.println("Résolution du combat...");
            System.out.println("Forces attaquantes : " + forcesAttaquantes);
            System.out.println("Forces défensives : " + forcesDéfensives);

            if (forcesAttaquantes > forcesDéfensives) {
                // Succès de l'invasion
                System.out.println("Invasion réussie ! Vous prenez le contrôle de l'hexagone.");
                hexCible.setOccupation(playerColor); // Changer l'occupation
                hexCible.setShipon(forcesAttaquantes); // Déplacer les forces attaquantes
            } else {
                // Échec de l'invasion
                System.out.println(
                        "Invasion échouée. L'hexagone reste sous le contrôle du joueur" + hexCible.getOccupation());
            }
        }
    }

    // private boolean isAdjacent(Hex hex1, Hex hex2) {
    // on regarde si hex cible est dans la liste des hex adjacent de hexDepart
    // ou autrrement avec les ids

    }

    public void executeCard(String playerColor, HashMap<String, ArrayList<SectorCard>> plateau) {
        if (this.id == 0) { // expand // Il ne faut pas qu'on ajoute un nombre de ship max par joueur ? :
                            // non cest a la fin du tour
            try (Scanner scanner = new Scanner(System.in)) {
                List<Hex> hexagonesJoueur = new ArrayList<>();

                // Parcours le plateau
                for (String niveau : plateau.keySet()) {
                    ArrayList<SectorCard> secteurs = plateau.get(niveau);

                    // Parcours les SectorCard
                    for (SectorCard sector : secteurs) {
                        Map<Integer, Hex> hexagones = sector.getHex();

                        // Parcours les hexagones
                        for (Hex hex : hexagones.values()) {
                            if (hex.getOccupation().equals(playerColor)) {
                                hexagonesJoueur.add(hex);
                                System.out.println("Hexagone trouvé : " + hex);
                            }
                        }
                    }
                }

                // Pas hexagone trouvé pour le joueur
                if (hexagonesJoueur.isEmpty()) {
                    System.out.println("Aucun hexagone occupé par le joueur " + playerColor + " trouvé !");
                    return;
                }

                // Demander joueur
                System.out.println("Choisissez l'hexagone où vous souhaitez ajouter un bateau :");
                for (int i = 0; i < hexagonesJoueur.size(); i++) {
                    System.out.println((i + 1) + ". " + hexagonesJoueur.get(i));
                }

                int choix = -1;
                while (choix < 1 || choix > hexagonesJoueur.size()) {
                    try {
                        System.out.print("Votre choix (1-" + hexagonesJoueur.size() + ") : ");
                        choix = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Veuillez entrer un nombre valide !");
                        scanner.next(); // Consommer l'entrée invalide
                    }
                }

                // Ajouter un bateau
                Hex hexChoisi = hexagonesJoueur.get(choix - 1);
                hexChoisi.addShip(1);
            }
        } else if (this.id == 1) { // explore
            try (Scanner scanner = new Scanner(System.in)) {
                List<Hex> hexagonesJoueur = new ArrayList<>();
                Hex hexDepart = null;
                Hex hexCible = null;

                // Demander au joueur de choisir l'hexagone de départ
                System.out.println("Choisissez l'hexagone de départ :");
                for (int i = 0; i < hexagonesJoueur.size(); i++) {
                    System.out.println((i + 1) + ". " + hexagonesJoueur.get(i));
                }

                int choixDepart = -1;
                while (choixDepart < 1 || choixDepart > hexagonesJoueur.size()) {
                    System.out.print("Votre choix (1-" + hexagonesJoueur.size() + ") : ");
                    choixDepart = scanner.nextInt();
                }
                hexDepart = hexagonesJoueur.get(choixDepart - 1);

                // Demander au joueur de choisir l'hexagone cible
                System.out.println("Choisissez l'hexagone cible :");
                List<Hex> hexagonesCibles = new ArrayList<>(); // Liste pour les hexagones cibles valides

                // Parcourir le plateau pour trouver les hexagones cibles
                for (String niveau : plateau.keySet()) {
                    ArrayList<SectorCard> secteurs = plateau.get(niveau);
                    for (SectorCard sector : secteurs) {
                        Map<Integer, Hex> hexagones = sector.getHex();
                        for (Hex hex : hexagones.values()) {
                            if (hex.getOccupation() == null /* && isAdjacent(hexDepart, hex) */) {
                                hexagonesCibles.add(hex);
                            }
                        }
                    }
                }

                for (int i = 0; i < hexagonesCibles.size(); i++) {
                    System.out.println((i + 1) + ". " + hexagonesCibles.get(i));
                }

                int choixCible = -1;
                while (choixCible < 1 || choixCible > hexagonesCibles.size()) {
                    System.out.print("Votre choix (1-" + hexagonesCibles.size() + ") : ");
                    choixCible = scanner.nextInt();
                }
                hexCible = hexagonesCibles.get(choixCible - 1);

                // Demander si le joueur souhaite laisser un bateau
                System.out.print("Voulez-vous laisser un bateau à l'hexagone de départ ? (oui/non) : ");
                String reponse = scanner.next();
                boolean shipLeft = reponse.equalsIgnoreCase("oui");

                // Effectuer l'expansion
                explore(playerColor, hexDepart, hexCible, shipLeft);
                System.out.println("Expansion effectuée avec succès !");
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre valide !");
            }
        } else {
            try (// invade
                    Scanner scanner = new Scanner(System.in)) {
                List<Hex> hexagonesCibles = new ArrayList<>();

                // Parcours Plateau
                for (String niveau : plateau.keySet()) {
                    ArrayList<SectorCard> secteurs = plateau.get(niveau);

                    // Parcourir SectorCard
                    for (SectorCard sector : secteurs) {
                        Map<Integer, Hex> hexagones = sector.getHex();

                        // Parcourir Hexagone
                        for (Hex hex : hexagones.values()) {
                            if (hex.getOccupation() != null && !hex.getOccupation().equals(playerColor)) {
                                hexagonesCibles.add(hex);
                                System.out.println("Hexagone cible trouvé : " + hex);
                            }
                        }
                    }
                }

                // Aucun hexagone
                if (hexagonesCibles.isEmpty()) {
                    System.out.println("Aucun hexagone cible trouvé pour le joueur " + playerColor + ".");
                    return;
                }

                // Afficher hexagones
                System.out.println("Choisissez un hexagone à envahir :");
                for (int i = 0; i < hexagonesCibles.size(); i++) {
                    System.out.println((i + 1) + ". " + hexagonesCibles.get(i));
                }

                // Demander joueur de choisir hexagone
                int choix = -1;
                while (choix < 1 || choix > hexagonesCibles.size()) {
                    try {
                        System.out.print("Votre choix (1-" + hexagonesCibles.size() + ") : ");
                        choix = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Veuillez entrer un nombre valide !");
                        scanner.next(); // Consomme l'entrée invalide
                    }
                }

                // Récupérer l'hexagone choisi
                Hex hexCible = hexagonesCibles.get(choix - 1);

                // Résoudre le combat
                resolveCombat(playerColor, hexCible);
            }
        }
    };

}
