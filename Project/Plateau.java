package Project;

import java.util.*;

public class Plateau {

    private HashMap<String, ArrayList<SectorCard>> jeux;

    public Plateau() {
        this.jeux = new HashMap<>();
        initialiserPlateau();
    }

    private void initialiserPlateau() {
        // Création Carte Top
        ArrayList<SectorCard> top = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SectorCard carteTop = new SectorCard(i+1, false, false, true);
            top.add(carteTop);
        }
        jeux.put("Top", top);

        // Création Carte Mid
        ArrayList<SectorCard> mid = new ArrayList<>();
        mid.add(new SectorCard(1, false, false, false)); // Carte Mid 1
        mid.add(new SectorCard(2, true, false, false));  // Carte TriPrime
        mid.add(new SectorCard(3, false, false, false)); // Carte Mid 2
        jeux.put("Mid", mid);

        // Création Carte Bottom
        ArrayList<SectorCard> bottom = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SectorCard carteBottom = new SectorCard(i+1, false, true, false);
            bottom.add(carteBottom);
        }
        jeux.put("Bottom", bottom);
    }

    public HashMap<String, ArrayList<SectorCard>> getPlateau() {
        return this.jeux;
    }

    public void checkPlateau() {
        for (String niveau : jeux.keySet()) {
            ArrayList<SectorCard> sectors = jeux.get(niveau);
            for (SectorCard sector : sectors) {
                Map<Integer, Hex> hexes = sector.getHex();
                for (Hex hex : hexes.values()) {
                    int totalShips = hex.getShipon();
                    int maxShips = hex.getMaxshipon();
                    List<Player> occupants = hex.getOccupants();

                    // Vérifier si le nombre de ships dépasse la capacité maximale
                    if (totalShips > maxShips) {
                        System.out.println("Trop de ships sur l'hexagone : " + hex);
                        int excessShips = totalShips - maxShips;

                        // Réduire les ships de manière équitable parmi les occupants
                        for (Player player : new ArrayList<>(occupants)) {
                            int shipsToRemove = Math.min(excessShips, hex.getShipon());
                            hex.removeShips(player, shipsToRemove);
                            excessShips -= shipsToRemove;

                            if (excessShips <= 0) {
                                break;
                            }
                        }
                    }

                    // Vérifier si plusieurs couleurs occupent l'hexagone
                    if (occupants.size() > 1) {
                        System.out.println("Conflit de couleurs sur l'hexagone : " + hex);

                        // Déterminer la couleur dominante
                        Map<Player, Integer> shipCountByPlayer = new HashMap<>();
                        for (Player player : occupants) {
                            shipCountByPlayer.put(player, Collections.frequency(occupants, player));
                        }

                        Player dominantPlayer = shipCountByPlayer.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse(null);

                        // Éliminer les ships des autres joueurs
                        for (Player player : new ArrayList<>(occupants)) {
                            if (!player.equals(dominantPlayer)) {
                                hex.removeShips(player, hex.getShipon());
                                hex.removeOccupation(player);
                            }
                        }

                        System.out.println("Couleur dominante : " + dominantPlayer.getColor());
                    }
                }
            }
        }
    }



    public void afficherPlateau() {
        for (String niveau : jeux.keySet()) {
            System.out.println("Niveau : " + niveau);
            ArrayList<SectorCard> sectors = jeux.get(niveau);
            for (SectorCard sector : sectors) {
                System.out.println("  " + sector);
            }
        }
    }
}