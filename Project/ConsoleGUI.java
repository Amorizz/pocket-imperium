package Project;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class ConsoleGUI {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;
    private String userInput;
    private boolean inputReady;

    public ConsoleGUI() {
        frame = new JFrame("Game Console");
        textArea = new JTextArea(20, 50);
        inputField = new JTextField(40);
        sendButton = new JButton("Send");
        userInput = null;
        inputReady = false;

        setupGUI();
        redirectSystemStreams();
    }

    private void setupGUI() {
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(inputField);
        inputPanel.add(sendButton);

        sendButton.addActionListener(e -> {
            userInput = inputField.getText();
            inputField.setText("");
            inputReady = true;
            synchronized (this) {
                this.notify(); // Notifier les threads en attente
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void println(String text) {
        textArea.append(text + "\n");
    }


    private void redirectSystemStreams() {
        PrintStream printStream = new PrintStream(new ConsoleOutputStream(textArea));
        System.setOut(printStream);
        System.setErr(printStream);
    }

    public synchronized String getInput() {
        while (!inputReady) {
            try {
                this.wait(); // Attendre que l'utilisateur fournisse une entrée
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        inputReady = false;
        return userInput;
    }
}
