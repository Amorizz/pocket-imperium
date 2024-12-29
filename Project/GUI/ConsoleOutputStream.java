package Project.GUI;

import javax.swing.*;
import java.io.OutputStream;

public class ConsoleOutputStream extends OutputStream {
    private JTextArea textArea;

    public ConsoleOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        appendText(String.valueOf((char) b));
    }

    @Override
    public void write(byte[] b, int off, int len) {
        appendText(new String(b, off, len));
    }

    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text));
    }


}
