package Project;

import java.util.*;

public class CommandCard {
    private int id;

    public CommandCard(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
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
                System.out.println("Invasion échouée. L'hexagone reste sous le contrôle du joueur" + hexCible.getOccupation());
        }
    }

    public void executeCard(String playerColor, HashMap<String, ArrayList<SectorCard>> plateau){
        if (this.id == 0){          // Il ne faut pas qu'on ajoute un nombre de ship max par joueur ?
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
        } else if (this.id == 1){           // Probleme trouver Hexagone adjascent
            //explore
        } else {
            try (//invade
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
