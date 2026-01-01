package sleepTracker;

import mainPanel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SleepTracker {

    private JPanel mainPanel;
    private JTextField sleepHoursField;
    private JButton addSleepButton, backButton;
    private JTextArea sleepRecordsArea;

    private SleepInformationTransfer dbManager;
    private String userEmail;

    public SleepTracker() {
        userEmail = "test@example.com";
        dbManager = new SleepInformationTransfer();

        mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(236, 240, 241));

        backButton = createModernButton("← Nazad", new Color(127, 140, 141));
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(236, 240, 241));
        backPanel.add(backButton);
        topPanel.add(backPanel);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);

        sleepHoursField = new JTextField(10);
        addSleepButton = new JButton("Dodaj san");

        inputPanel.add(new JLabel("Sati sna:"));
        inputPanel.add(sleepHoursField);
        inputPanel.add(addSleepButton);

        sleepRecordsArea = new JTextArea(15, 30);
        sleepRecordsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(sleepRecordsArea);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        addSleepButton.addActionListener(e -> addSleepRecord());

        updateSleepRecordsDisplay();
    }

    private void addSleepRecord() {
        try {
            double hours = Double.parseDouble(sleepHoursField.getText());
            if (hours <= 0 || hours > 24) {
                JOptionPane.showMessageDialog(null, "Unesite validan broj sati (0-24)!");
                return;
            }

            String date = java.time.LocalDate.now().toString();
            SleepRecord record = new SleepRecord(userEmail, hours, date);

            dbManager.addSleepRecord(record);
            sleepHoursField.setText("");
            updateSleepRecordsDisplay();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage());
        }
    }

    private void updateSleepRecordsDisplay() {
        ArrayList<SleepRecord> records = dbManager.getAllRecords(userEmail);
        StringBuilder sb = new StringBuilder();
        double total = 0;

        for (int i = 0; i < records.size(); i++) {
            SleepRecord r = records.get(i);
            sb.append("Dan ").append(i + 1)
                    .append(" (").append(r.getDate()).append("): ")
                    .append(r.getHours()).append(" sati\n");
            total += r.getHours();
        }

        if (!records.isEmpty()) {
            double avg = total / records.size();
            sb.append("\nProsjek sati sna: ").append(String.format("%.2f", avg));
        }

        sleepRecordsArea.setText(sb.toString());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    private void goBackToMainPanel() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        frame.setContentPane(new MainPanel(userEmail));
        frame.revalidate();
        frame.repaint();
    }

    private JButton createModernButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return b;
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Sleep Tracker");
        SleepTracker tracker = new SleepTracker();
        frame.setContentPane(tracker.getMainPanel());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
