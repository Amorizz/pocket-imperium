package Project.GUI;

import java.util.*;

/**
 * La classe {@code CommandCard} représente une carte de commande utilisée par les joueurs dans le jeu.
 * Chaque carte possède un ID, une puissance (power), et est associée à une des trois commandes principales :
 * Expand, Explore, et Invade. Ces cartes permettent aux joueurs d'effectuer différentes actions stratégiques.
 */
public class CommandCard {
    private int id; // ID unique de la carte (1 : Expand, 2 : Explore, 3 : Invade)
    private int power = 1; // Puissance de la carte (1, 2, ou 3)
    private final Random random = new Random(); // Générateur aléatoire partagé

    /**
     * Constructeur de la classe {@code CommandCard}.
     *
     * @param id l'identifiant unique de la carte (1 : Expand, 2 : Explore, 3 : Invade).
     */
    public CommandCard(int id) {
        this.id = id;
    }

    /**
     * Retourne l'ID de la carte.
     *
     * @return l'identifiant unique de la carte.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Retourne le power de de la carte par rapport au nombre de joueur ayant retourné la meme carte dans le tour.
     *
     * @return l'identifiant unique de la carte.
     */
    public int getPower() {
        return this.power;
    }

    /**
     * Définit la puissance de la carte en fonction du nombre de joueurs ayant choisi la même commande.
     *
     * @param power la nouvelle puissance de la carte (comprise entre 1 et 3).
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * Retourne une liste de couleurs correspondant aux joueurs ayant des vaisseaux sur l'hexagone donné.
     *
     * @param hex     l'hexagone pour lequel récupérer les couleurs.
     * @param console la console pour obtenir les couleurs des joueurs.
     * @return une liste de couleurs correspondant aux joueurs ayant des vaisseaux sur l'hexagone.
     */
    private List<java.awt.Color> getHexColors(Hex hex, ConsoleGUI console) {
        List<java.awt.Color> colors = new ArrayList<>();
        for (Ship ship : hex.getOccupation()) {
            colors.add(console.getColorFromName(ship.getPlayerName().getColor()));
        }
        return colors;
    }

    /**
     * Permet au joueur réel d'utiliser la commande Expand pour ajouter des vaisseaux sur les systèmes qu'il contrôle.
     *
     * @param player  le joueur qui utilise la commande.
     * @param plateau le plateau contenant les hexagones et les systèmes.
     * @param console la console pour afficher.
     */
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
                    for (Ship ship : hex.getOccupation()) {
                        if (ship.getPlayerName().equals(player)) {
                            controlledHexes.add(hex);
                        }
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

    /**
     * Permet au joueur virtuel d'utiliser la commande Expand.
     *
     * @param player  le joueur virtuel utilisant la commande.
     * @param plateau le plateau contenant les hexagones et les systèmes.
     * @param console la console pour afficher.
     */
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
                    for (Ship ship : hex.getOccupation()) {
                        if (ship.getPlayerName().equals(player)) {
                            controlledHexes.add(hex);
                        }
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

    /**
     * Permet au joueur réel d'utiliser la commande Explore pour déplacer des vaisseaux entre les hexagones.
     *
     * @param player  le joueur qui utilise la commande.
     * @param plateau le plateau contenant les hexagones.
     * @param console la console pour afficher.
     */
    public void explore(Player player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        console.println(player.getPlayerName() + " va explorer !");

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
                        for (Ship ship : hex.getOccupation()) {
                            if (ship.getPlayerName().equals(player)) {
                                playerHexes.add(hex);
                            }
                        }
                    }
                }
            }

            if (playerHexes.isEmpty()) {
                console.println("Vous n'avez aucune flotte à déplacer.");
                return;
            }

            console.println("Flotte " + (fleet + 1) + "/" + maxFleets);
            console.println("Choisissez un hexagone de départ parmi ceux que vous contrôlez :");
            for (int i = 0; i < playerHexes.size(); i++) {
                console.println((i + 1) + ". Hexagone ID: " + playerHexes.get(i).getId());
            }

            int departChoix = getValidInput(console, 1, playerHexes.size());
            Hex hexDepart = playerHexes.get(departChoix - 1);

            List<Hex> adjHexes = hexDepart.rexAdjacent(plateau);// Exclure les hexagones occupés par d'autres joueurs
            adjHexes.removeIf(hex -> hex.getOccupation().size() != 0);

            Set<String> addedCoords = new HashSet<>();
            List<Hex> adjacentHexes = new ArrayList<>();

            for (Hex firstLevelHex : adjHexes) {
                List<Hex> secondLevelAdjacents = firstLevelHex.rexAdjacent(plateau); // Niveau 2
                for (Hex secondLevelHex : secondLevelAdjacents) {
                    // Ajouter si non déjà ajouté, non occupé et différent de l'hexagone d'origine
                    String coords = secondLevelHex.getX() + "," + secondLevelHex.getY();
                    if (!addedCoords.contains(coords)
                            && secondLevelHex.getOccupation().size() == 0 // Hex vide
                            && !secondLevelHex.equals(hexDepart)) { // Pas le même que hexDepart
                        adjacentHexes.add(secondLevelHex);
                        addedCoords.add(coords);
                    }
                }
            }


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
            int shipsToMove = hexDepart.getShipCountForPlayer(player);

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

    /**
     * Permet au joueur virtuel d'utiliser la commande Explore.
     *
     * @param player  le joueur virtuel utilisant la commande.
     * @param plateau le plateau contenant les hexagones.
     * @param console la console pour afficher.
     */
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
            Integer shipsAvailable = hexDepart.getShipCountForPlayer(player);
            if (shipsAvailable == null || shipsAvailable <= 0) {
                ownedHexes.remove(hexDepart); // Retirer les hexagones sans bateaux de la liste
                continue;
            }

            // Identifier les hexagones adjacents
            List<Hex> adjHexes = hexDepart.rexAdjacent(plateau);
            adjHexes.removeIf(hex -> hex.getOccupation().size() != 0); // Exclure les hexagones occupés par d'autres joueurs
            Set<String> addedCoords = new HashSet<>();
            List<Hex> adjacentHexes = new ArrayList<>();

            for (Hex firstLevelHex : adjHexes) {
                List<Hex> secondLevelAdjacents = firstLevelHex.rexAdjacent(plateau); // Niveau 2
                for (Hex secondLevelHex : secondLevelAdjacents) {
                    // Ajouter si non déjà ajouté, non occupé et différent de l'hexagone d'origine
                    String coords = secondLevelHex.getX() + "," + secondLevelHex.getY();
                    if (!addedCoords.contains(coords)
                            && secondLevelHex.getOccupation().size() == 0 // Hex vide
                            && !secondLevelHex.equals(hexDepart)) { // Pas le même que hexDepart
                        adjacentHexes.add(secondLevelHex);
                        addedCoords.add(coords);
                    }
                }
            }

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

    /**
     * Permet au joueur réel d'utiliser la commande Invade pour envahir des systèmes adjacents.
     *
     * @param player  le joueur qui utilise la commande.
     * @param plateau le plateau contenant les hexagones et les systèmes.
     * @param console la console pour afficher.
     */
    public void invade(Player player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        console.println(player.getPlayerName() + " prépare une invasion !");

        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        for (int i = 0; i < maxInvasions; i++) {
            List<Hex> playerHexes = new ArrayList<>();
            for (String niveau : plateau.keySet()) {
                for (SectorCard sector : plateau.get(niveau)) {
                    for (Hex hex : sector.getHex().values()) {
                        for (Ship ship : hex.getOccupation()) {
                            if (ship.getPlayerName().equals(player)) {
                                playerHexes.add(hex);
                            }
                        }
                    }
                }
            }

            List<Hex> adjacentEnemyHexes = new ArrayList<>();
            Set<String> addedCoords = new HashSet<>();
            for (Hex hex : playerHexes) {
                for (Hex hexAdj : hex.rexAdjacent(plateau)) {
                    List<Hex> secondLevelAdjacents = hexAdj.rexAdjacent(plateau); // Niveau 2
                    for (Hex secondLevelHex : secondLevelAdjacents) {
                        String coords = secondLevelHex.getX() + "," + secondLevelHex.getY();
                        if (!addedCoords.contains(coords) && !secondLevelHex.equals(hex) && !secondLevelHex.getOccupation().contains(player) && secondLevelHex.getOccupation().size() != 0) {
                            adjacentEnemyHexes.add(secondLevelHex);
                            addedCoords.add(coords);
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

            // Récupérer les hexagones adjacents contrôlés par le joueur
            List<Hex> adjacentPlayerHexes = new ArrayList<>();
            for (Hex adj : hexCible.rexAdjacent(plateau)) {
                // Vérifier si un des Ship dans l'occupation de cet hexagone appartient au joueur
                for (Ship ship : adj.getOccupation()) {
                    if (ship.getPlayerName().equals(player)) {
                        adjacentPlayerHexes.add(adj);
                    }
                }
            }


            if (adjacentPlayerHexes.isEmpty()) {
                console.println("Aucun hexagone adjacent pour fournir des vaisseaux.");
                continue;
            }

            // Calculer le nombre total de vaisseaux disponibles
            int totalShipsAvailable = 0;
            for (Hex hex : adjacentPlayerHexes) {
                totalShipsAvailable += hex.getShipCountForPlayer(player);
            }

            console.println("Nombre total de vaisseaux disponibles pour l'attaque : " + totalShipsAvailable);
            console.println("Combien de bateaux voulez-vous utiliser pour attaquer ? (max : " + totalShipsAvailable + ")");

            int shipsToUse = 0;
            while (shipsToUse < 1 || shipsToUse > totalShipsAvailable) {
                try {
                    shipsToUse = Integer.parseInt(console.getInputSync().trim());
                } catch (NumberFormatException e) {
                    console.println("Entrée invalide. Veuillez entrer un nombre.");
                }
            }

            // Retirer les vaisseaux des hexagones adjacents
            int shipsRemaining = shipsToUse;
            Iterator<Hex> iterator = adjacentPlayerHexes.iterator();
            while (shipsRemaining > 0 && iterator.hasNext()) {
                Hex hex = iterator.next();
                int shipsInHex = hex.getShipCountForPlayer(player);
                int shipsToRemove = Math.min(shipsRemaining, shipsInHex);

                hex.removeShip(player, shipsToRemove);
                shipsRemaining -= shipsToRemove;

                console.removeShipsFromHex(hex.getId(), player.getColor());
            }

            // Combat avec le défenseur
            int sommedef = 0;
            for (Ship ship : hexCible.getOccupation()){
                sommedef += ship.getNbrShipy();
            }
            
            if (sommedef < shipsToUse){
                shipsToUse = shipsToUse - sommedef;
                hexCible.clearAllOccupation();
                hexCible.addShip(player,shipsToUse);
            } else if (sommedef > shipsToUse) {
                while (shipsToUse != 0){
                    for (Ship ship : hexCible.getOccupation()){
                        if (shipsToUse == 0){
                            break;
                        }
                        hexCible.removeShip(ship.getPlayerName(),1);
                        shipsToUse -= 1;
                    }
                }
            }

        }

        console.println("Invasion terminée pour " + player.getPlayerName() + ".");
    }

    /**
     * Permet au joueur virtuel d'utiliser la commande Invade.
     *
     * @param player  le joueur virtuel utilisant la commande.
     * @param plateau le plateau contenant les hexagones et les systèmes.
     * @param console la console pour afficher.
     */
    public void vInvade(VirtualPlayer player, HashMap<String, ArrayList<SectorCard>> plateau, ConsoleGUI console) {
        console.println(player.getPlayerName() + " prépare une invasion !");

        int maxInvasions = switch (this.power) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        Random random = new Random();

        for (int i = 0; i < maxInvasions; i++) {
            List<Hex> playerHexes = new ArrayList<>();
            for (String niveau : plateau.keySet()) {
                for (SectorCard sector : plateau.get(niveau)) {
                    for (Hex hex : sector.getHex().values()) {
                        for (Ship ship : hex.getOccupation()) {
                            if (ship.getPlayerName().equals(player)) {
                                playerHexes.add(hex);
                            }
                        }
                    }
                }
            }

            List<Hex> adjacentEnemyHexes = new ArrayList<>();
            Set<String> addedCoords = new HashSet<>();
            for (Hex hex : playerHexes) {
                for (Hex hexAdj : hex.rexAdjacent(plateau)) {
                    List<Hex> secondLevelAdjacents = hexAdj.rexAdjacent(plateau); // Niveau 2
                    for (Hex secondLevelHex : secondLevelAdjacents) {
                        String coords = secondLevelHex.getX() + "," + secondLevelHex.getY();
                        if (!addedCoords.contains(coords) && !secondLevelHex.equals(hex) && !secondLevelHex.getOccupation().contains(player) && secondLevelHex.getOccupation().size() != 0) {
                            adjacentEnemyHexes.add(secondLevelHex);
                            addedCoords.add(coords);
                        }
                    }
                }
            }

            if (adjacentEnemyHexes.isEmpty()) {
                console.println(player.getPlayerName() + " n'a aucun hexagone à envahir.");
                return;
            }

            Hex hexCible = adjacentEnemyHexes.get(random.nextInt(adjacentEnemyHexes.size()));
            console.println(player.getPlayerName() + " a choisi l'hexagone cible ID: " + hexCible.getId());

            // Récupérer les hexagones adjacents contrôlés par le joueur
            List<Hex> adjacentPlayerHexes = new ArrayList<>();
            for (Hex adj : hexCible.rexAdjacent(plateau)) {
                for (Ship ship : adj.getOccupation()) {
                    if (ship.getPlayerName().equals(player)) {
                        adjacentPlayerHexes.add(adj);
                    }
                }
            }

            if (adjacentPlayerHexes.isEmpty()) {
                console.println("Aucun hexagone adjacent pour fournir des vaisseaux.");
                continue;
            }

            // Calculer le nombre total de vaisseaux disponibles
            int totalShipsAvailable = 0;
            for (Hex hex : adjacentPlayerHexes) {
                totalShipsAvailable += hex.getShipCountForPlayer(player);
            }

            // Sélectionner un nombre de vaisseaux à utiliser, limité par les vaisseaux disponibles
            int shipsToUse = random.nextInt(totalShipsAvailable) + 1;
            console.println("Nombre total de vaisseaux disponibles : " + totalShipsAvailable);
            console.println(player.getPlayerName() + " envoie " + shipsToUse + " vaisseau(x) pour l'invasion.");

            // Retirer les vaisseaux des hexagones adjacents, de manière aléatoire
            int shipsRemaining = shipsToUse;
            while (shipsRemaining > 0) {
                Hex selectedHex = adjacentPlayerHexes.get(random.nextInt(adjacentPlayerHexes.size()));
                int shipsInHex = selectedHex.getShipCountForPlayer(player);
                int shipsToRemove = Math.min(shipsRemaining, shipsInHex);

                selectedHex.removeShip(player, shipsToRemove);
                shipsRemaining -= shipsToRemove;
                console.removeShipsFromHex(selectedHex.getId(), player.getColor());

                console.println(player.getPlayerName() + " a retiré " + shipsToRemove + " vaisseau(x) de l'hexagone : " + selectedHex);

                if (selectedHex.getShipCountForPlayer(player) == 0) {
                    adjacentPlayerHexes.remove(selectedHex); // Supprimer l'hexagone si plus de vaisseaux
                }

            }

            // Résoudre le combat avec le défenseur
            // Combat avec le défenseur
            int sommedef = 0;
            for (Ship ship : hexCible.getOccupation()){
                sommedef += ship.getNbrShipy();
            }

            if (sommedef < shipsToUse){
                shipsToUse = shipsToUse - sommedef;
                hexCible.clearAllOccupation();
                hexCible.addShip(player,shipsToUse);
            } else if (sommedef > shipsToUse) {
                while (shipsToUse != 0){
                    for (Ship ship : hexCible.getOccupation()){
                        if (shipsToUse == 0){
                            break;
                        }
                        hexCible.removeShip(ship.getPlayerName(),1);
                        shipsToUse -= 1;
                    }
                }
            }
        }

        console.println("Invasion terminée pour " + player.getPlayerName() + ".");
    }

    /**
     * Permet de récupérer un entier valide de l'utilisateur.
     *
     * @param console la console pour afficher.
     * @param min     la valeur minimale autorisée.
     * @param max     la valeur maximale autorisée.
     * @return l'entier valide entré par l'utilisateur.
     */
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

    /**
     * Exécute la carte de commande pour le joueur donné.
     *
     * @param player  le joueur qui utilise la carte.
     * @param plateau le plateau contenant les hexagones et les systèmes.
     * @param console la console pour afficher.
     */
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
