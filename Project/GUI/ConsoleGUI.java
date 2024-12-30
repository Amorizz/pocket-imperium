package Project.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.awt.Color;

public class ConsoleGUI extends JFrame {
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;

    private final HashMap<Integer, Polygon> hexagonMap = new HashMap<>();
    private final HashMap<Integer, List<Color>> hexShips = new HashMap<>();

    public ConsoleGUI() {
        setTitle("Pocket Imperium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

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
                int yOffset = margin;

                int hexCounter = 1;

                for (int row = 0; row < 3; row++) {
                    int xOffset = margin;
                    for (int col = 0; col < 3; col++) {
                        hexCounter = drawHexagons(g2d, xOffset, yOffset, hexSize, hexCounter);
                        xOffset += sectionWidth; // Décalage horizontal
                    }
                    yOffset += sectionHeight; // Décalage vertical
                }

                drawBlueLines(g2d, sectionWidth, sectionHeight, margin);
            }

            private int drawHexagons(Graphics2D g2d, int xOffset, int yOffset, int hexSize, int startLevel) {
                int[][] positions = {
                        {1, 0}, {3, 0},
                        {0, 1}, {2, 1}, {4, 1},
                        {1, 2}, {3, 2}
                };

                int level = startLevel;
                for (int[] pos : positions) {
                    int x = xOffset + pos[0] * (hexSize + 5);
                    int y = yOffset + pos[1] * (int) (hexSize * 0.75);

                    Polygon hexagon = createHexagon(x, y, hexSize);
                    g2d.setColor(Color.GRAY);
                    g2d.fill(hexagon);

                    hexagonMap.put(level, hexagon);

                    // Dessiner les bateaux présents
                    List<Color> ships = hexShips.get(level);
                    if (ships != null) {
                        drawShipsInHex(g2d, x, y, hexSize, ships);
                    }

                    // Afficher l'ID au centre
                    g2d.setColor(Color.BLACK);
                    String levelText = String.valueOf(level);
                    FontMetrics metrics = g2d.getFontMetrics();
                    int textX = x - metrics.stringWidth(levelText) / 2;
                    int textY = y + metrics.getHeight() / 4;
                    g2d.drawString(levelText, textX, textY);

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
                Polygon hexagon = new Polygon();
                double angle = Math.PI / 3;
                for (int i = 0; i < 6; i++) {
                    int x1 = (int) (x + size * Math.cos(angle * i));
                    int y1 = (int) (y + size * Math.sin(angle * i));
                    hexagon.addPoint(x1, y1);
                }
                return hexagon;
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

    public void println(String text) {
        textArea.append(text + "\n");
    }

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
