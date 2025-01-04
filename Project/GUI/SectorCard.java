package Project.GUI;

import java.util.*;

/**
 * La classe {@code SectorCard} représente une carte secteur dans le jeu,
 * contenant plusieurs hexagones avec des niveaux variés.
 * Elle est responsable de la génération et de la gestion des hexagones d'un secteur.
 */
public class SectorCard {
    private int number;
    private int position = 0;
    private boolean triPrime;
    private boolean bottom;
    private boolean top;
    private String occupation;
    private Map<Integer, Hex> Hexa;

    /**
     * Retourne l'occupation actuelle de la carte.
     *
     * @return une chaîne décrivant l'occupation actuelle.
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Définit l'occupation actuelle de la carte.
     *
     * @param occupation la chaîne représentant la nouvelle occupation.
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * Génère trois positions uniques pour des hexagones d'un secteur dans une liste ou le premier sera l'hexagone de niveau 2 et les deux autres seront les hexagones de niveau 1.
     *
     * @param nbr le nombre total d'hexagones dans le secteur.
     * @return une liste de trois entiers uniques représentant les positions choisies.
     * @throws IllegalArgumentException si le nombre d'hexagones est inférieur à 3.
     */
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

    /**
     * Constructeur de la classe {@code SectorCard}.
     * Initialise les hexagones de la carte selon son type (TriPrime, bottom, top, ou standard).
     *
     * @param number   numéro de la carte secteur.
     * @param triPrime indique si la carte est une carte TriPrime.
     * @param bottom   indique si la carte est une carte "bottom".
     * @param top      indique si la carte est une carte "top".
     */
    public SectorCard(int number, boolean triPrime, boolean bottom, boolean top) {
        this.number = number;
        this.triPrime = triPrime;
        this.bottom = bottom;
        this.top = top;
        this.Hexa = new HashMap<Integer, Hex>();    // Les hexagones seront ajouté dans une map (dictionnaire)
        if (this.bottom || this.top) { // Création des Hexagons dans la carte secteur si c'est une carte bottom ou top
            if (number == 1){
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
            } else {
                ArrayList<Integer> LNBR = position21(6);

                for (int i = 0; i < 6; i++) {
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
            Hex HexTri = new Hex(3);
            this.Hexa.put(3,HexTri);
            HexTri.reLoc(triPrime,bottom,top,3,number);
        }
    }

    /**
     * Retourne une représentation textuelle de la carte secteur.
     *
     * @return une chaîne indiquant le nombre d'hexagones dans la carte.
     */
    public String toString(){
        return "Cette carte contient "+Hexa.size()+" Hexagone";
    }

    /**
     * Retourne un hexagone à partir de ses coordonnées.
     *
     * @param nx la coordonnée X.
     * @param ny la coordonnée Y.
     * @return l'hexagone correspondant ou {@code null} si aucun hexagone n'est trouvé.
     */
    public Hex getHex(int nx, int ny) {
        for (Hex hex : Hexa.values()) {
            if (hex.getX() == nx && hex.getY() == ny) {
                return hex;
            }
        }
        return null; // Aucun hexagone trouvé aux coordonnées données
    }

    /**
     * Retourne tous les hexagones associés à cette carte.
     *
     * @return une map contenant les hexagones avec leurs indices comme clés.
     */
    public Map<Integer, Hex> getHex() {
        return this.Hexa;
    }
}