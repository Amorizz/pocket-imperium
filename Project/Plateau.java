package Project;

import java.util.ArrayList;
import java.util.HashMap;

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
            SectorCard carteTop = new SectorCard(i, false, false, true);
            top.add(carteTop);
        }
        // Dupliquer les cartes Doubles
        jeux.put("Top", top);
        Hex PaT1 = jeux.get("Top").get(1).getHexa(3);
        jeux.get("Top").get(0).getHex().put(5, PaT1);
        Hex PaT2 = jeux.get("Top").get(2).getHexa(3);
        jeux.get("Top").get(1).getHex().put(5, PaT2);


        // Création Carte Mid
        ArrayList<SectorCard> mid = new ArrayList<>();
        mid.add(new SectorCard(1, false, false, false)); // Carte Mid 1
        mid.add(new SectorCard(2, true, false, false));  // Carte TriPrime
        mid.add(new SectorCard(3, false, false, false)); // Carte Mid 2
        jeux.put("Mid", mid);
        // Dupliquer les cartes Doubles
        Hex PaMi1 = jeux.get("Mid").get(1).getHexa(1);
        jeux.get("Mid").get(0).getHex().put(3, PaMi1);
        Hex PaMi2 = jeux.get("Mid").get(1).getHexa(4);
        jeux.get("Mid").get(0).getHex().put(8, PaMi2);
        Hex PaMi3 = jeux.get("Mid").get(2).getHexa(1);
        jeux.get("Mid").get(1).getHex().put(2, PaMi3);
        Hex PaMi4 = jeux.get("Mid").get(2).getHexa(6);
        jeux.get("Mid").get(1).getHex().put(5, PaMi4);

        // Création Carte Bottom
        ArrayList<SectorCard> bottom = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SectorCard carteBottom = new SectorCard(i, false, true, false);
            bottom.add(carteBottom);
        }
        jeux.put("Bottom", bottom);
        // Dupliquer les cartes Doubles
        Hex PaB1 = jeux.get("Bottom").get(1).getHexa(3);
        jeux.get("Bottom").get(0).getHex().put(5, PaB1);
        Hex PaB2 = jeux.get("Bottom").get(2).getHexa(3);
        jeux.get("Bottom").get(1).getHex().put(5, PaB2);
    }

    public HashMap<String, ArrayList<SectorCard>> getPlateau() {
        return this.jeux;
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

