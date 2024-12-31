package Project.Console;

/**
 * L'énumération {@code Color} représente les différentes couleurs disponibles dans le jeu.
 * Chaque couleur est associée à un nom sous forme de chaîne de caractères.
 */
public enum Color {
    /**
     * Représente la couleur rouge.
     */
    ROUGE("rouge"),

    /**
     * Représente la couleur verte.
     */
    VERT("vert"),

    /**
     * Représente la couleur bleue.
     */
    BLEU("bleu"),

    /**
     * Représente la couleur jaune.
     */
    JAUNE("jaune"),

    /**
     * Représente la couleur noire.
     */
    NOIR("noir"),

    /**
     * Représente la couleur blanche.
     */
    BLANC("blanc");

    // Nom de la couleur sous forme de chaîne de caractères.
    private final String colorName;

    /**
     * Constructeur de l'énumération {@code Color}.
     *
     * @param colorName le nom de la couleur en tant que chaîne de caractères.
     */
    Color(String colorName) {
        this.colorName = colorName;
    }

    /**
     * Retourne le nom de la couleur sous forme de chaîne de caractères.
     *
     * @return le nom de la couleur.
     */
    public String getColorName() {
        return colorName;
    }
}
