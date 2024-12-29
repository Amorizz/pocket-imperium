package Project.GUI;

import java.util.*;

public class SectorCard {
    private int number;
    private int position = 0;
    private boolean triPrime;
    private boolean bottom;
    private boolean top;
    private String occupation;
    private Map<Integer, Hex> Hexa;

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public ArrayList<Integer> position21(int nbr) {
        if (nbr < 3) {
            throw new IllegalArgumentException("Le nombre d'hexagones doit être au moins 3.");
        }

        Set<Integer> uniquePositions = new HashSet<>();
        Random random = new Random();

        // Générer 3 indices uniques valides
        while (uniquePositions.size() < 3) {
            int generated = random.nextInt(nbr) + 1; // Générer un indice entre 1 et nbr inclus
            if (generated <= nbr) { // Vérification explicite des limites
                uniquePositions.add(generated);
            }
        }
        return new ArrayList<>(uniquePositions);
    }

    public SectorCard(int number, boolean triPrime, boolean bottom, boolean top) {
        this.number = number;
        this.triPrime = triPrime;
        this.bottom = bottom;
        this.top = top;
        this.Hexa = new HashMap<Integer, Hex>();    // Les hexagones seront ajouté dans une map (dictionnaire)
        if (this.bottom || this.top) { // Création des Hexagons dans la carte secteur si c'est une carte bottom ou top
            ArrayList<Integer> LNBR = position21(7);

            for (int i = 0; i < 7; i++) {
                Hex hex;

                if (i + 1 == LNBR.get(0)) {
                    hex = new Hex(2); // Hex de niveau 2
                } else if (i + 1 == LNBR.get(1) || i + 1 == LNBR.get(2)) {
                    hex = new Hex(1); // Hex de niveau 1
                } else {
                    hex = new Hex(0); // Hex de niveau 0
                }

                // Ajout dans la map
                this.Hexa.put(i + 1, hex);

                // Calcul des coordonnées et affectation
                hex.reLoc(triPrime, bottom, top, i, number);
            }

        } else if (!this.bottom && !this.top && !this.triPrime) {     // Création des Hexagons dans la carte secteur si ce n'est pas une carte bottom ou top ou triPri
            ArrayList<Integer> LNBR = position21(8);

            for (int i = 0; i < 8; i++) {
                Hex hex;

                if (i + 1 == LNBR.get(0)) {
                    hex = new Hex(2); // Hex de niveau 2
                } else if (i + 1 == LNBR.get(1) || i + 1 == LNBR.get(2)) {
                    hex = new Hex(1); // Hex de niveau 1
                } else {
                    hex = new Hex(0); // Hex de niveau 0
                }

                // Ajout dans la map
                this.Hexa.put(i + 1, hex);

                // Calcul des coordonnées et affectation
                hex.reLoc(triPrime, bottom, top, i, number);
            }
        } else if (this.triPrime){                                    // Création des Hexagons dans la carte secteur si c'est la carte triPri
            Hex Hex1 = new Hex(0);
            this.Hexa.put(1, Hex1);
            Hex1.reLoc(triPrime,bottom,top,1,number);
            Hex Hex2 = new Hex(0);
            this.Hexa.put(2, Hex2);
            Hex2.reLoc(triPrime,bottom,top,2,number);
            Hex HexTri = new Hex(3);
            this.Hexa.put(3,HexTri);
            HexTri.reLoc(triPrime,bottom,top,3,number);
            Hex Hex3 = new Hex(0);
            this.Hexa.put(4, Hex3);
            Hex3.reLoc(triPrime,bottom,top,4,number);
            Hex Hex4 = new Hex(0);
            this.Hexa.put(5, Hex4);
            Hex4.reLoc(triPrime,bottom,top,5,number);
        }
    }

    public String toString(){
        return "Cette carte contient "+Hexa.size()+" Hexagone";
    }

    public Hex getHexa(int nbr){
        return Hexa.get(nbr);
    }

    public Hex getHex(int nx, int ny) {
        for (Hex hex : Hexa.values()) {
            if (hex.getX() == nx && hex.getY() == ny) {
                return hex;
            }
        }
        return null; // Aucun hexagone trouvé aux coordonnées données
    }

    public Map<Integer, Hex> getHex() {
        return this.Hexa;
    }
}