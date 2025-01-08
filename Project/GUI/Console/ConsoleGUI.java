package Project.GUI.Console;

import Project.GUI.Games.Plateau;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.Color;

/**
 * La classe ConsoleGUI représente l'interface graphique pour le jeu Pocket Imperium.
 * Elle permet d'afficher les informations du jeu, de gérer les entrées utilisateur,
 * et de visualiser les hexagones ainsi que les déplacements des vaisseaux.
 */
public class ConsoleGUI extends JFrame {
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;
    private Plateau plateau;

    private final HashMap<Integer, Polygon> hexagonMap = new HashMap<>();
    private final HashMap<Integer, List<Color>> hexShips = new HashMap<>();

    /**
     * Constructeur de la classe ConsoleGUI.
     *
     * @param plateau Le plateau du jeu contenant les hexagones.
     */
    public ConsoleGUI(Plateau plateau) {
        setTitle("Pocket Imperium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLayout(new BorderLayout());

        this.plateau = plateau;

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 600));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(inputPanel, BorderLayout.SOUTH);

        JPanel gameBoardPanel = createGameBoardPanel();

        add(leftPanel, BorderLayout.WEST);
        add(gameBoardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Met à jour la liste des couleurs des vaisseaux associés à un hexagone donné.
     *
     * @param hexId  L'identifiant unique de l'hexagone.
     * @param colors La liste des couleurs des vaisseaux à afficher sur cet hexagone.
     */
    public void updateHexShips(int hexId, List<Color> colors) {
        hexShips.put(hexId, colors); // Mettre à jour les bateaux sur l'hexagone
        repaint(); // Redessiner le plateau
    }

    /**
     * Convertit un nom de couleur en objet Color Java.
     *
     * @param colorName Le nom de la couleur (exemple : "rouge").
     * @return L'objet Color correspondant.
     */
    public java.awt.Color getColorFromName(String colorName) {
        return switch (colorName.toLowerCase()) {
            case "rouge" -> Color.RED;
            case "bleu" -> Color.BLUE;
            case "vert" -> Color.GREEN;
            case "jaune" -> Color.YELLOW;
            default -> Color.GRAY; // Couleur par défaut si inconnue
        };
    }

    /**
     * Crée et configure le panneau de visualisation du plateau de jeu.
     *
     * @return Un JPanel contenant le plateau de jeu dessiné.
     */
    private JPanel createGameBoardPanel() {
        JPanel gameBoardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                int margin = 20;
                int sectionWidth = (getWidth() - 2 * margin) / 3;
                int sectionHeight = (getHeight() - 2 * margin) / 3;
                int hexSize = 40;

                int hexCounter = 1;

                for (int row = 0; row < 3; row++) {
                    int xOffset = margin;
                    for (int col = 0; col < 3; col++) {
                        if (col == 1 && row == 1) {
                            // Section centrale
                            hexCounter = drawCenterSectionHexes(g2d, xOffset, margin + row * sectionHeight, hexSize, hexCounter);
                        } else if (row == 1 && col != 1) {
                            // Sections gauche et droite
                            hexCounter = drawSideSectionHexes(g2d, xOffset, margin + row * sectionHeight, hexSize, hexCounter);
                        } else {
                            // Sections haut, bas
                            if (col == 0) {
                                hexCounter = drawHexagons1(g2d, xOffset, margin + row * sectionHeight, hexSize, hexCounter);
                            } else {
                                hexCounter = drawHexagons(g2d, xOffset, margin + row * sectionHeight, hexSize, hexCounter);
                            }
                        }
                        xOffset += sectionWidth;
                    }
                }

                drawBlueLines(g2d, sectionWidth, sectionHeight, margin);
            }

            private int drawSideSectionHexes(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel) {
                int[][] positions = {
                        {1, 0}, {3, 0}, {5, 0},  // Ligne 1
                        {2, 1}, {4, 1},          // Ligne 2
                        {1, 2}, {3, 2}, {5, 2}   // Ligne 3
                };

                return drawHexagons(g2d, xOffset, yOffset, hexSize, startLevel, positions);
            }

            private int drawHexagons1(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel) {
                int[][] positions = {
                        {1, 0}, {3, 0},  // Ligne 1
                        {0, 1}, {2, 1}, {4, 1},  // Ligne 2
                        {1, 2}, {3, 2}   // Ligne 3
                };

                int horizontalSpacing = hexSize + 10;  // Ajustement pour espacer horizontalement
                int verticalSpacing = (int) (hexSize * 1.5);  // Ajustement pour espacer verticalement
                int sectionHorizontalCentering = (getWidth() / 3 - (4 * horizontalSpacing)) / 2;
                int sectionVerticalCentering = (getHeight() / 3 - (3 * verticalSpacing)) / 2;

                int level = startLevel;

                for (int[] pos : positions) {
                    int x = xOffset + sectionHorizontalCentering + pos[0] * horizontalSpacing;
                    int y = yOffset + sectionVerticalCentering + pos[1] * verticalSpacing;

                    Polygon hexagon = createHexagon(x, y, hexSize);
                    g2d.setColor(Color.GRAY);
                    g2d.fill(hexagon);

                    hexagonMap.put(level, hexagon);

                    // Dessiner les bateaux présents
                    List<Color> ships = hexShips.get(level);
                    if (ships != null) {
                        drawShipsInHex(g2d, x, y, hexSize, ships);
                    }

                    // Récupérer le niveau depuis le plateau
                    int  hexLevel = plateau.getLevel(level); // Vous devez implémenter getLevel dans Plateau

                    // Afficher l'ID et le niveau
                    g2d.setColor(Color.BLACK);
                    String hexIdText = String.valueOf(level); // ID
                    String levelText = "Lvl: " + hexLevel; // Niveau

                    FontMetrics metrics = g2d.getFontMetrics();

                    // Positionnement de l'ID
                    int idTextX = x - metrics.stringWidth(hexIdText) / 2;
                    int idTextY = y;

                    // Positionnement du niveau
                    int levelTextX = x - metrics.stringWidth(levelText) / 2;
                    int levelTextY = y + metrics.getHeight() + 5;

                    g2d.drawString(hexIdText, idTextX, idTextY); // Dessiner l'ID
                    g2d.drawString(levelText, levelTextX, levelTextY); // Dessiner le niveau

                    level++;
                }

                return level;
            }

            private int drawHexagons(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel) {
                int[][] positions = {
                        {1, 0}, {3, 0},  // Ligne 1
                        {2, 1}, {4, 1},  // Ligne 2
                        {1, 2}, {3, 2}   // Ligne 3
                };

                int horizontalSpacing = hexSize + 10;  // Ajustement pour espacer horizontalement
                int verticalSpacing = (int) (hexSize * 1.5);  // Ajustement pour espacer verticalement
                int sectionHorizontalCentering = (getWidth() / 3 - (4 * horizontalSpacing)) / 2;
                int sectionVerticalCentering = (getHeight() / 3 - (3 * verticalSpacing)) / 2;

                int level = startLevel;

                for (int[] pos : positions) {
                    int x = xOffset + sectionHorizontalCentering + pos[0] * horizontalSpacing;
                    int y = yOffset + sectionVerticalCentering + pos[1] * verticalSpacing;

                    Polygon hexagon = createHexagon(x, y, hexSize);
                    g2d.setColor(Color.GRAY);
                    g2d.fill(hexagon);

                    hexagonMap.put(level, hexagon);

                    // Dessiner les bateaux présents
                    List<Color> ships = hexShips.get(level);
                    if (ships != null) {
                        drawShipsInHex(g2d, x, y, hexSize, ships);
                    }

                    // Récupérer le niveau depuis le plateau
                    int  hexLevel = plateau.getLevel(level); // Vous devez implémenter getLevel dans Plateau

                    // Afficher l'ID et le niveau
                    g2d.setColor(Color.BLACK);
                    String hexIdText = String.valueOf(level); // ID
                    String levelText = "Lvl: " + hexLevel; // Niveau

                    FontMetrics metrics = g2d.getFontMetrics();

                    // Positionnement de l'ID
                    int idTextX = x - metrics.stringWidth(hexIdText) / 2;
                    int idTextY = y;

                    // Positionnement du niveau
                    int levelTextX = x - metrics.stringWidth(levelText) / 2;
                    int levelTextY = y + metrics.getHeight() + 5;

                    g2d.drawString(hexIdText, idTextX, idTextY); // Dessiner l'ID
                    g2d.drawString(levelText, levelTextX, levelTextY); // Dessiner le niveau

                    level++;
                }

                return level;
            }

            private int drawCenterSectionHexes(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel) {
                int[][] positions = {
                          // Ligne du haut : 2 hexagones
                        {2, 1},          // Ligne du milieu : 1 hexagone
                           // Ligne du bas : 2 hexagones
                };

                return drawHexagons(g2d, xOffset, yOffset, hexSize, startLevel, positions);
            }

            private int drawHexagons(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel, int[][] positions) {
                int horizontalSpacing = hexSize + 10;
                int verticalSpacing = (int) (hexSize * 1.5);
                int level = startLevel;

                for (int[] pos : positions) {
                    int x = xOffset + pos[0] * horizontalSpacing;
                    int y = yOffset + pos[1] * verticalSpacing;

                    Polygon hexagon = createHexagon(x, y, hexSize);
                    g2d.setColor(Color.GRAY);
                    g2d.fill(hexagon);

                    hexagonMap.put(level, hexagon);

                    // Dessiner les bateaux présents
                    List<Color> ships = hexShips.get(level);
                    if (ships != null) {
                        drawShipsInHex(g2d, x, y, hexSize, ships);
                    }

                    // Récupérer le niveau depuis le plateau
                    int hexLevel = plateau.getLevel(level); // Vous devez implémenter getLevel dans Plateau

                    // Afficher l'ID et le niveau
                    g2d.setColor(Color.BLACK);
                    String hexIdText = String.valueOf(level); // ID
                    String levelText = "Lvl: " + hexLevel; // Niveau

                    FontMetrics metrics = g2d.getFontMetrics();

                    // Positionnement de l'ID
                    int idTextX = x - metrics.stringWidth(hexIdText) / 2;
                    int idTextY = y;

                    // Positionnement du niveau
                    int levelTextX = x - metrics.stringWidth(levelText) / 2;
                    int levelTextY = y + metrics.getHeight() + 5;

                    g2d.drawString(hexIdText, idTextX, idTextY); // Dessiner l'ID
                    g2d.drawString(levelText, levelTextX, levelTextY); // Dessiner le niveau

                    level++;
                }

                return level;
            }

            private void drawShipsInHex(Graphics2D g2d, int x, int y, int hexSize, List<Color> ships) {
                int pointSize = 6;
                int padding = 3;
                int pointsPerRow = (hexSize - padding) / (pointSize + padding);
                int rowOffset = 0;

                for (int i = 0; i < ships.size(); i++) {
                    int col = i % pointsPerRow;
                    if (i > 0 && i % pointsPerRow == 0) {
                        rowOffset++;
                    }
                    int px = x - (pointsPerRow / 2 * (pointSize + padding)) + col * (pointSize + padding);
                    int py = y + rowOffset * (pointSize + padding);

                    g2d.setColor(ships.get(i));
                    g2d.fillOval(px, py, pointSize, pointSize);
                }
            }

            private Polygon createHexagon(int x, int y, int size) {
                int[] xPoints = new int[6];
                int[] yPoints = new int[6];
                for (int i = 0; i < 6; i++) {
                    xPoints[i] = (int) (x + size * Math.cos(i * Math.PI / 3));
                    yPoints[i] = (int) (y + size * Math.sin(i * Math.PI / 3));
                }
                return new Polygon(xPoints, yPoints, 6);
            }

            private void drawBlueLines(Graphics2D g2d, int sectionWidth, int sectionHeight, int margin) {
                g2d.setColor(Color.BLUE);
                for (int i = 1; i < 3; i++) {
                    int x = i * sectionWidth + margin;
                    g2d.drawLine(x, margin, x, getHeight() - margin);

                    int y = i * sectionHeight + margin;
                    g2d.drawLine(margin, y, getWidth() - margin, y);
                }
            }
        };

        gameBoardPanel.setPreferredSize(new Dimension(600, 600));
        return gameBoardPanel;
    }

    /**
     * Place un vaisseau dans un hexagone et redessine le plateau.
     *
     * @param hexId     L'identifiant unique de l'hexagone.
     * @param colorName Le nom de la couleur du vaisseau à ajouter.
     */
    public void placeShipInHex(int hexId, String colorName) {
        Color color;
        switch (colorName.toLowerCase()) {
            case "rouge":
                color = Color.RED;
                break;
            case "bleu":
                color = Color.BLUE;
                break;
            case "vert":
                color = Color.GREEN;
                break;
            case "jaune":
                color = Color.YELLOW;
                break;
            default:
                color = Color.GRAY;
                break;
        }

        hexShips.computeIfAbsent(hexId, k -> new ArrayList<>()).add(color);
        repaint();
    }

    /**
     * Supprime tous les vaisseaux d'une couleur donnée d'un hexagone.
     *
     * @param hexId     L'identifiant unique de l'hexagone.
     * @param colorName Le nom de la couleur des vaisseaux à supprimer.
     */
    public void removeShipsFromHex(int hexId, String colorName) {
        // Convertir le nom de la couleur en objet Color
        Color color;
        switch (colorName.toLowerCase()) {
            case "rouge":
                color = Color.RED;
                break;
            case "bleu":
                color = Color.BLUE;
                break;
            case "vert":
                color = Color.GREEN;
                break;
            case "jaune":
                color = Color.YELLOW;
                break;
            default:
                color = Color.GRAY; // Couleur par défaut si non reconnue
                break;
        }

        // Vérifier si des bateaux existent sur cet hexagone
        if (hexShips.containsKey(hexId)) {
            List<Color> ships = hexShips.get(hexId);
            ships.removeIf(shipColor -> shipColor.equals(color)); // Supprimer tous les bateaux de la couleur donnée

            if (ships.isEmpty()) {
                hexShips.remove(hexId); // Supprimer l'entrée si plus de bateaux sur l'hexagone
            }
        }

        repaint(); // Redessiner le plateau
    }

    /**
     * Affiche un message dans la console de l'interface graphique.
     *
     * @param text Le texte à afficher.
     */
    public void println(String text) {
        textArea.append(text + "\n");
    }

    /**
     * Récupère une entrée utilisateur de manière synchrone.
     *
     * @return La chaîne de texte saisie par l'utilisateur.
     */
    public String getInputSync() {
        final Object lock = new Object();
        final String[] input = new String[1];
        input[0] = null;

        sendButton.addActionListener(e -> {
            synchronized (lock) {
                input[0] = inputField.getText().trim();
                inputField.setText("");
                lock.notify();
            }
        });

        synchronized (lock) {
            try {
                while (input[0] == null) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        return input[0];
    }
}
