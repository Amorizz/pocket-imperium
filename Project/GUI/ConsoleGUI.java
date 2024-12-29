package Project.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.Color;

public class ConsoleGUI extends JFrame {
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;

    public ConsoleGUI() {
        // Configuration de la fenêtre principale
        setTitle("Pocket Imperium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

        // Zone de texte pour afficher les messages
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 600));

        // Zone de saisie et bouton "Send"
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Ajouter la zone de texte et la zone de saisie à gauche
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(inputPanel, BorderLayout.SOUTH);

        // Plateau de jeu à droite
        JPanel gameBoardPanel = createGameBoardPanel();

        // Ajouter les panneaux gauche et droit
        add(leftPanel, BorderLayout.WEST);
        add(gameBoardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createGameBoardPanel() {
        JPanel gameBoardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Fond noir
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                int margin = 20; // Marge autour du plateau
                int sectionWidth = (getWidth() - 2 * margin) / 3;
                int sectionHeight = (getHeight() - 2 * margin) / 3;
                int hexSize = 30; // Taille des hexagones
                int internalMarginX = (sectionWidth - 5 * hexSize) / 2; // Ajustement horizontal
                int internalMarginY = (sectionHeight - 3 * (int) (1.5 * hexSize)) / 2 + 10; // Ajustement vertical

                // Lignes bleues pour diviser les sections
                g2d.setColor(Color.BLUE);
                for (int i = 1; i < 3; i++) {
                    int x = i * sectionWidth + margin;
                    g2d.drawLine(x, margin, x, getHeight() - margin);

                    int y = i * sectionHeight + margin;
                    g2d.drawLine(margin, y, getWidth() - margin, y);
                }

                // Dessiner les hexagones et leurs niveaux
                int hexCounter = 1; // Pour donner un niveau unique à chaque hexagone
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int xOffset = col * sectionWidth + margin + internalMarginX;
                        int yOffset = row * sectionHeight + margin + internalMarginY;

                        if (row == 0 || row == 2) {
                            hexCounter = drawHexagonsTopBottom(g2d, xOffset, yOffset, hexSize, hexCounter);
                        } else {
                            if (col == 0 || col == 2) {
                                hexCounter = drawHexagonsMiddleSides(g2d, xOffset, yOffset, hexSize, hexCounter);
                            } else {
                                hexCounter = drawHexagonsMiddleCenter(g2d, xOffset, yOffset, hexSize, hexCounter);
                            }
                        }
                    }
                }
            }

            private int drawHexagonsTopBottom(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel) {
                g2d.setColor(Color.RED);
                int[][] positions = {
                        {1, 0}, {3, 0}, // Ligne 1 : 2 hexagones
                        {0, 1}, {2, 1}, {4, 1}, // Ligne 2 : 3 hexagones
                        {1, 2}, {3, 2}  // Ligne 3 : 2 hexagones
                };

                return drawHexagons(g2d, xOffset, yOffset, hexSize, startLevel, positions);
            }

            private int drawHexagonsMiddleSides(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel) {
                g2d.setColor(Color.RED);
                int[][] positions = {
                        {0, 0}, {2, 0}, {4, 0}, // Ligne 1 : 3 hexagones
                        {1, 1}, {3, 1},        // Ligne 2 : 2 hexagones
                        {0, 2}, {2, 2}, {4, 2} // Ligne 3 : 3 hexagones
                };

                return drawHexagons(g2d, xOffset, yOffset, hexSize, startLevel, positions);
            }

            private int drawHexagonsMiddleCenter(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel) {
                g2d.setColor(Color.RED);
                int[][] positions = {
                        {0, 0}, {4, 0}, // Haut-gauche et haut-droite
                        {2, 1},        // Centre
                        {0, 2}, {4, 2}  // Bas-gauche et bas-droite
                };

                return drawHexagons(g2d, xOffset, yOffset, hexSize, startLevel, positions);
            }

            private int drawHexagons(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel, int[][] positions) {
                int level = startLevel;
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.setColor(Color.RED);

                for (int[] pos : positions) {
                    int x = xOffset + pos[0] * (hexSize + 5);
                    int y = yOffset + pos[1] * (int) (hexSize * 1.5);

                    // Dessiner l'hexagone
                    Path2D hexagon = createHexagon(x, y, hexSize);
                    g2d.fill(hexagon);

                    // Afficher le niveau au centre
                    g2d.setColor(Color.WHITE);
                    String levelText = ""+level;
                    FontMetrics metrics = g2d.getFontMetrics();
                    int textX = x - metrics.stringWidth(levelText) / 2;
                    int textY = y + metrics.getHeight() / 4;
                    g2d.drawString(levelText, textX, textY);

                    g2d.setColor(Color.RED); // Remet la couleur pour le prochain hexagone
                    level++;
                }
                return level;
            }

            private Path2D createHexagon(int x, int y, int size) {
                Path2D hexagon = new Path2D.Double();
                double angle = Math.PI / 3;
                for (int i = 0; i < 6; i++) {
                    double x1 = x + size * Math.cos(angle * i);
                    double y1 = y + size * Math.sin(angle * i);
                    if (i == 0) {
                        hexagon.moveTo(x1, y1);
                    } else {
                        hexagon.lineTo(x1, y1);
                    }
                }
                hexagon.closePath();
                return hexagon;
            }
        };

        gameBoardPanel.setPreferredSize(new Dimension(600, 600));
        return gameBoardPanel;
    }

    public void println(String text) {
        textArea.append(text + "\n");
    }

    public String getInput() {
        return inputField.getText();
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public String getInputSync() {
        final String[] input = new String[1];
        final Object lock = new Object();

        sendButton.addActionListener(e -> {
            synchronized (lock) {
                input[0] = inputField.getText();
                inputField.setText(""); // Réinitialiser le champ
                lock.notify(); // Notifie le thread principal que l'entrée est prête
            }
        });

        try {
            synchronized (lock) {
                lock.wait(); // Attend que l'utilisateur entre une valeur
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return input[0];
    }

}
