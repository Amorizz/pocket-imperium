package Project;

import java.util.*;

public class SectorCard {
    private int number;
    private boolean triPrime;
    private String occupation;
    private Hex[][] hexagones;

    public SectorCard(int number, boolean triPrime) {
        this.number = number;
        this.triPrime = triPrime;

        if (triPrime) {
            this.hexagones = new Hex[1][1];
            Hex hex = new Hex(3, 5);
            this.hexagones[0][0] = hex;
        } else {
            if (number == 2 || number == 8) {
                this.hexagones = new Hex[3][3];
                List<Hex> hexList = new ArrayList<>();
                hexList.add(new Hex(2, number));
                hexList.add(new Hex(1, number));
                hexList.add(new Hex(1, number));

                // Mélanger la liste des hexagones
                Collections.shuffle(hexList);

                // Remplir la matrice avec les hexagones mélangés
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {

                        if (i!=)
                        this.hexagones[i][j] = hexList.remove(0);
                    }
                }
            } else {
                this.hexagones = new Hex[3][3];
                // Autres cas à gérer ici
            }
        }
    }

    public Map<Integer, Hex> getHex() {
        return null;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String toString() {
        return "";
    }

    public Hex getHexa(int nbr) {
        return null;
    }

    public Hex getHex(int nx, int ny) {
        return hexagones[nx][ny];
    }

}