import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class StegoForm {
    private JPanel mainPanel;
    private JButton openButton, saveMessageButton, readMessageButton, closeButton;
    private JTextArea messageArea;
    private JTextField keyField;
    private JLabel statusLabel;
    private JFileChooser fileChooser;
    private File currentBmpFile;

    public StegoForm() {
        mainPanel = new JPanel();
        openButton = new JButton("Open BMP");
        saveMessageButton = new JButton("Save Message");
        readMessageButton = new JButton("Read Message");
        closeButton = new JButton("Close BMP");
        messageArea = new JTextArea(10, 30);
        keyField = new JTextField(20);
        statusLabel = new JLabel("Status: Ready");
        fileChooser = new JFileChooser();

        mainPanel.add(openButton);
        mainPanel.add(saveMessageButton);
        mainPanel.add(readMessageButton);
        mainPanel.add(closeButton);
        mainPanel.add(new JScrollPane(messageArea));
        mainPanel.add(keyField);
        mainPanel.add(statusLabel);

        openButton.addActionListener(this::openBmp);
        saveMessageButton.addActionListener(this::saveMessage);
        readMessageButton.addActionListener(this::readMessage);
        closeButton.addActionListener(this::closeBmp);
    }

    private void openBmp(ActionEvent e) {
        if (fileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
            currentBmpFile = fileChooser.getSelectedFile();
            statusLabel.setText("Opened: " + currentBmpFile.getName());
        }
    }

    private void saveMessage(ActionEvent e) {
        if (currentBmpFile != null && !keyField.getText().isEmpty()) {
            String message = messageArea.getText();
            String key = keyField.getText();
            StegoMessenger.writeMessage(currentBmpFile, message, key);
            statusLabel.setText("Message saved in " + currentBmpFile.getName());
        }
    }

    private void readMessage(ActionEvent e) {
        if (currentBmpFile != null && !keyField.getText().isEmpty()) {
            String key = keyField.getText();
            String message = StegoMessenger.readMessage(currentBmpFile, key);
            messageArea.setText(message);
            statusLabel.setText("Message read from " + currentBmpFile.getName());
        }
    }

    private void closeBmp(ActionEvent e) {
        currentBmpFile = null;
        messageArea.setText("");
        statusLabel.setText("Closed BMP file.");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
