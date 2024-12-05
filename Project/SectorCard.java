package Project;

import java.util.*;

public class SectorCard {
    private int number;
    private int position = 0;
    private boolean triPrime;
    private boolean bottom;
    private boolean top;
    private String occupation;

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    private Map<Integer, Hex> Hexa;

    public ArrayList<Integer> position21(int nbr) {
        Set<Integer> uniquePositions = new HashSet<>();
        Random random = new Random();
        while (uniquePositions.size() < 3) {
            uniquePositions.add(random.nextInt(nbr) + 1); // +1 pour éviter l'index 0
        }
        return new ArrayList<>(uniquePositions);
    }

    public SectorCard(int number, boolean triPrime, boolean bottom, boolean top) {
        System.out.println(triPrime);
        this.number = number;
        this.triPrime = triPrime;
        this.bottom = bottom;
        this.top = top;
        this.Hexa = new HashMap<Integer, Hex>();    // Les hexagones seront ajouté dans une map (dictionnaire)
        if (this.bottom || this.top) {              // Création des Hexagons dans la carte secteur si c'est une carte bottom ou top
            ArrayList<Integer> LNBR = position21(7);
            for (int i = 0;i<7;i++){
                Hex Hex1 = new Hex(0);
                this.Hexa.put(i+1, Hex1);
                Hex1.reLoc(triPrime,bottom,top,i,number);
            }
            Hex Hex2 = new Hex(2);
            this.Hexa.put(LNBR.get(0), Hex2);
            Hex2.reLoc(triPrime,bottom,top,LNBR.get(0),number);
            Hex Hex11 = new Hex(1);
            this.Hexa.put(LNBR.get(1), Hex11);
            Hex11.reLoc(triPrime,bottom,top, LNBR.get(1), number);
            Hex Hex12 = new Hex(1);
            this.Hexa.put(LNBR.get(2), Hex12);
            Hex12.reLoc(triPrime,bottom,top, LNBR.get(2), number);
        } else if (!this.bottom && !this.top && !this.triPrime) {     // Création des Hexagons dans la carte secteur si ce n'est pas une carte bottom ou top ou triPri
            ArrayList<Integer> LNBR = position21(8);
            for (int i = 0;i<8;i++){
                Hex Hex1 = new Hex(0);
                this.Hexa.put(i+1, Hex1);
                Hex1.reLoc(triPrime,bottom,top,i,number);
            }
            Hex Hex2 = new Hex(2);
            this.Hexa.put(LNBR.getFirst(), Hex2);
            Hex2.reLoc(triPrime,bottom,top, LNBR.getFirst(), number);
            Hex Hex11 = new Hex(1);
            this.Hexa.put(LNBR.get(1), Hex11);
            Hex11.reLoc(triPrime,bottom,top, LNBR.get(1), number);
            Hex Hex12 = new Hex(1);
            this.Hexa.put(LNBR.get(2), Hex12);
            Hex12.reLoc(triPrime,bottom,top,LNBR.get(2),number);
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

    public Map<Integer, Hex> getHex() {
        return this.Hexa;
    }
}