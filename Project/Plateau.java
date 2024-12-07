package Project;

import java.util.ArrayList;
import java.util.HashMap;

public class Plateau {

    private HashMap<String, SectorCard> jeux;

    public Plateau() {
        this.jeux = new HashMap<>();
        initialiserPlateau();
    }

    private void initialiserPlateau() {
        HashMap<String, SectorCard> jeux = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            jeux.put("sector" + i, new SectorCard(i, false));
        }

        for (int i = 4; i <= 6; i++) {

            if (i == 5) {
                jeux.put("sector5", new SectorCard(i, true));
            } else {
                jeux.put("sector" + i, new SectorCard(i, false));
            }
        }

        for (int i = 7; i <= 9; i++) {
            jeux.put("sector" +i, new SectorCard(i, false));
        }

        //il faut maintenant parcourir chaque sector card ligne par ligne
            //si hex == null alors on passe auprochain 
            //sinon on ajoute dans la geante matrice qui est le plateau lhex est on attribue ces coordonnee 
            

    }

    public HashMap<String, SectorCard> getPlateau() {
        return this.jeux;
    }

    public void afficherPlateau() {

    }
}