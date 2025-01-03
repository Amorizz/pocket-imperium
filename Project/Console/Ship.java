package Project.Console;

/**
 * La classe {@code Ship} représente un vaisseau dans le jeu Pocket Imperium.
 * Elle est associée à un joueur et peut contenir des informations supplémentaires
 * pour la gestion des vaisseaux dans les hexagones.
 */
public class Ship {
    private Player player; // Le joueur propriétaire du vaisseau
    private int nbrShip; // Le nombre de vaisseaux pour ce joueur

    /**
     * Constructeur pour initialiser un vaisseau avec son propriétaire et sa quantité.
     *
     * @param player le joueur propriétaire du vaisseau.
     * @param nbrShip le nombre de vaisseaux.
     */
    public Ship(Player player, int nbrShip) {
        this.player = player;
        this.nbrShip = nbrShip;
    }

    /**
     * Retourne le joueur propriétaire du vaisseau.
     *
     * @return le joueur propriétaire.
     */
    public Player getPlayerName() {
        return player;
    }

    /**
     * Définit le joueur propriétaire du vaisseau.
     *
     * @param player le joueur à associer au vaisseau.
     */
    public void setOwner(Player player) {
        this.player = player;
    }

    /**
     * Retourne la quantité de vaisseaux.
     *
     * @return le nombre de vaisseaux.
     */
    public int getNbrShipy() {
        return nbrShip;
    }

    /**
     * Modifie la quantité de vaisseaux.
     *
     * @param quantity le nouveau nombre de vaisseaux.
     */
    public void setQuantity(int quantity) {
        this.nbrShip = quantity;
    }

    /**
     * Ajoute un nombre de vaisseaux à la quantité actuelle.
     *
     * @param amount le nombre de vaisseaux à ajouter.
     */
    public void addQuantity(int amount) {
        this.nbrShip += amount;
    }

    /**
     * Retire un nombre de vaisseaux de la quantité actuelle. Si le nombre devient
     * négatif, il est automatiquement ramené à zéro.
     *
     * @param amount le nombre de vaisseaux à retirer.
     */
    public void removeQuantity(int amount) {
        this.nbrShip = Math.max(0, this.nbrShip - amount);
    }

    @Override
    public String toString() {
        return player.getPlayerName() + ": " + nbrShip + " ships";
    }
}
