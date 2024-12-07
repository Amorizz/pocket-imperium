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
            this.hexagones = new Hex[3][2];
            Hex hex = new Hex(3, 5);
            this.hexagones[0][0] = hex;
            this.hexagones[0][1] = null;
            this.hexagones[1][0] = hex;
            this.hexagones[1][1] = hex;
            this.hexagones[2][0] = hex;
            this.hexagones[2][1] = null;

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

                        if (j == 2 && (i == 0 || i == 2)) {
                            this.hexagones[i][j] = null;
                        } else if (i == 1 && (j == 0 || j == 2)) {
                            this.hexagones[i][j] = new Hex(0, number);
                        }

                        else {
                            this.hexagones[i][j] = hexList.remove(0);
                        }

                    }
                }
            } else if (number == 1 || number == 7) {
                this.hexagones = new Hex[3][2];
                List<Hex> hexList = new ArrayList<>();
                hexList.add(new Hex(2, number));
                hexList.add(new Hex(1, number));
                hexList.add(new Hex(1, number));
                hexList.add(new Hex(0, number));
                hexList.add(new Hex(0, number));
                Collections.shuffle(hexList);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (j == 1 && i == 1) {
                            this.hexagones[i][j] = null;
                        } else {
                            this.hexagones[i][j] = hexList.remove(0);
                        }

                    }
                }

            } else if (number == 3 || number == 9) {
                this.hexagones = new Hex[3][2];
                List<Hex> hexList = new ArrayList<>();
                hexList.add(new Hex(2, number));
                hexList.add(new Hex(1, number));
                hexList.add(new Hex(1, number));
                hexList.add(new Hex(0, number));
                hexList.add(new Hex(0, number));
                Collections.shuffle(hexList);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (j == 0 && i == 1) {
                            this.hexagones[i][j] = null;
                        } else {
                            this.hexagones[i][j] = hexList.remove(0);
                        }

                    }
                }

            } else if (number == 4) {
                this.hexagones = new Hex[3][2];
                List<Hex> hexList = new ArrayList<>();
                hexList.add(new Hex(2, number));
                hexList.add(new Hex(1, number));
                hexList.add(new Hex(1, number));
                Collections.shuffle(hexList);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (j == 1) {
                            this.hexagones[i][j] = new Hex(0, number);
                        } else {
                            this.hexagones[i][j] = hexList.remove(0);
                        }

                    }
                }

            } else if (number == 6) {
                this.hexagones = new Hex[3][2];
                List<Hex> hexList = new ArrayList<>();
                hexList.add(new Hex(2, number));
                hexList.add(new Hex(1, number));
                hexList.add(new Hex(1, number));
                Collections.shuffle(hexList);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (j == 0) {
                            this.hexagones[i][j] = new Hex(0, number);
                        } else {
                            this.hexagones[i][j] = hexList.remove(0);
                        }

                    }
                }

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