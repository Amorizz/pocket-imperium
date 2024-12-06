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
            SectorCard carteBottom = new SectorCard(i, false, true, false);
            bottom.add(carteBottom);
        }
        jeux.put("Bottom", bottom);
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