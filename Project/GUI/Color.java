package Project.GUI;

public enum Color {
    ROUGE("rouge"),
    VERT("vert"),
    BLEU("bleu"),
    JAUNE("jaune"),
    NOIR("noir"),
    BLANC("blanc");

    private final String colorName;

    Color(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }
}
