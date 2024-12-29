package Project.GUI;

import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import java.awt.geom.Path2D;

public class TestIntGraphique extends JFrame {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pocket Imperium");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Écran d'accueil
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Pocket Imperium", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        JButton playButton = new JButton("Play");
        startPanel.add(titleLabel, BorderLayout.CENTER);
        startPanel.add(playButton, BorderLayout.SOUTH);

        // Plateau de jeu
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

                // Dessiner les hexagones
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int xOffset = col * sectionWidth + margin + internalMarginX;
                        int yOffset = row * sectionHeight + margin + internalMarginY;

                        if (row == 0 || row == 2) {
                            drawHexagonsTopBottom(g2d, xOffset, yOffset, hexSize);
                        } else {
                            if (col == 0 || col == 2) {
                                drawHexagonsMiddleSides(g2d, xOffset, yOffset, hexSize);
                            } else {
                                drawHexagonsMiddleCenter(g2d, xOffset, yOffset, hexSize);
                            }
                        }
                    }
                }
            }

            private void drawHexagonsTopBottom(Graphics2D g2d, int xOffset, int yOffset, int hexSize) {
                g2d.setColor(Color.RED);
                int[][] positions = {
                        {1, 0}, {3, 0}, // Ligne 1 : 2 hexagones
                        {0, 1}, {2, 1}, {4, 1}, // Ligne 2 : 3 hexagones
                        {1, 2}, {3, 2}  // Ligne 3 : 2 hexagones
                };

                for (int[] pos : positions) {
                    int x = xOffset + pos[0] * (hexSize + 5);
                    int y = yOffset + pos[1] * (int) (hexSize * 1.5);
                    g2d.fill(createHexagon(x, y, hexSize));
                }
            }

            private void drawHexagonsMiddleSides(Graphics2D g2d, int xOffset, int yOffset, int hexSize) {
                g2d.setColor(Color.RED);
                int[][] positions = {
                        {0, 0}, {2, 0}, {4, 0}, // Ligne 1 : 3 hexagones
                        {1, 1}, {3, 1},        // Ligne 2 : 2 hexagones
                        {0, 2}, {2, 2}, {4, 2} // Ligne 3 : 3 hexagones
                };

                for (int[] pos : positions) {
                    int x = xOffset + pos[0] * (hexSize + 5);
                    int y = yOffset + pos[1] * (int) (hexSize * 1.5);
                    g2d.fill(createHexagon(x, y, hexSize));
                }
            }

            private void drawHexagonsMiddleCenter(Graphics2D g2d, int xOffset, int yOffset, int hexSize) {
                g2d.setColor(Color.RED);
                int[][] positions = {
                        {0, 0}, {4, 0}, // Haut-gauche et haut-droite
                        {2, 1},        // Centre
                        {0, 2}, {4, 2}  // Bas-gauche et bas-droite
                };

                for (int[] pos : positions) {
                    int x = xOffset + pos[0] * (hexSize + 5);
                    int y = yOffset + pos[1] * (int) (hexSize * 1.5);
                    g2d.fill(createHexagon(x, y, hexSize));
                }
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

        // Zone de texte et bouton "Send"
        JPanel controlPanel = new JPanel(new BorderLayout());
        JTextField textField = new JTextField();
        JButton sendButton = new JButton("Send");
        controlPanel.add(textField, BorderLayout.CENTER);
        controlPanel.add(sendButton, BorderLayout.EAST);

        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(gameBoardPanel, BorderLayout.CENTER);
        gamePanel.add(controlPanel, BorderLayout.SOUTH);

        // Gestion des transitions entre les panneaux
        frame.setLayout(new CardLayout());
        frame.add(startPanel, "start");
        frame.add(gamePanel, "game");

        playButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
            cl.show(frame.getContentPane(), "game");
        });

        frame.setVisible(true);
    }
}
