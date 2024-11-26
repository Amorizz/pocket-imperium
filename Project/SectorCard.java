package Project;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SectorCard {
    private int number;
    private int position = 0;
    private boolean triPrime;
    private boolean bottom;
    private boolean top;
    private Map<Integer, Hex> Hexa;

    public SectorCard(int number, boolean triPrime, boolean bottom, boolean top) {
        this.number = number;
        this.triPrime = triPrime;
        this.bottom = bottom;
        this.top = top;
        this.Hexa = new HashMap<Integer, Hex>();    // Les hexagones seront ajouté dans une map (dictionnaire)
        if (this.bottom || this.top) {              // Création des Hexagons dans la carte secteur si c'est une carte bottom ou top
            Random randomNumbers = new Random();
            Hex Hex1 = new Hex(randomNumbers.nextInt(3));
            this.Hexa.put(1, Hex1);
            Hex Hex2 = new Hex(randomNumbers.nextInt(3));
            this.Hexa.put(2, Hex2);
            Hex Hex3 = new Hex(0);
            this.Hexa.put(3, Hex3);
            Hex Hex4 = new Hex(randomNumbers.nextInt(3));
            this.Hexa.put(4, Hex4);
            Hex Hex5 = new Hex(0);
            this.Hexa.put(5, Hex5);
            Hex Hex6 = new Hex(randomNumbers.nextInt(3));
            this.Hexa.put(6, Hex6);
            Hex Hex7 = new Hex(randomNumbers.nextInt(3));
            this.Hexa.put(7,Hex7);
        } else if (!this.bottom && !this.top) {     // Création des Hexagons dans la carte secteur si ce n'est pas une carte bottom ou top ou triPri
            Random randomNumbers = new Random();
            Hex Hex1 = new Hex(0);
            this.Hexa.put(1, Hex1);
            Hex Hex2 = new Hex(randomNumbers.nextInt(3));
            this.Hexa.put(2, Hex2);
            Hex Hex3 = new Hex(0);
            this.Hexa.put(3,Hex3);
            Hex Hex4 = new Hex(randomNumbers.nextInt(3));
            this.Hexa.put(4, Hex4);
            Hex Hex5 = new Hex(randomNumbers.nextInt(3));
            this.Hexa.put(5, Hex5);
            Hex Hex6 = new Hex(0);
            this.Hexa.put(6, Hex6);
            Hex Hex7 = new Hex(randomNumbers.nextInt(3));
            this.Hexa.put(7, Hex7);
            Hex Hex8 = new Hex(0);
            this.Hexa.put(8, Hex8);
        } else {                                    // Création des Hexagons dans la carte secteur si c'est la carte triPri
            Random randomNumbers = new Random();
            Hex Hex1 = new Hex(0);
            this.Hexa.put(1, Hex1);
            Hex Hex2 = new Hex(0);
            this.Hexa.put(2, Hex2);
            Hex HexTri = new Hex(3);
            this.Hexa.put(3,HexTri);
            Hex Hex3 = new Hex(0);
            this.Hexa.put(4, Hex3);
            Hex Hex4 = new Hex(0);
            this.Hexa.put(5, Hex4);
        }
    }

    public String toString(){
        return "Cette carte contient "+Hexa.size()+" Hexagone";
    }

    public Hex getHexa(int nbr){
        return Hexa.get(nbr);
    }
}
