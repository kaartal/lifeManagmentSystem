package fitnesTracker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FitnessPlanner {

    private JPanel mainPanel;
    private JTextField caloriesField;
    private JButton addButton;
    private JTextArea recordsArea;

    private FitnessDBManager dbManager;
    private String userEmail;

    public FitnessPlanner() {
        userEmail = "test@example.com";
        dbManager = new FitnessDBManager();

        mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);

        caloriesField = new JTextField(10);
        addButton = new JButton("Dodaj kalorije");

        inputPanel.add(new JLabel("Kalorije:"));
        inputPanel.add(caloriesField);
        inputPanel.add(addButton);

        recordsArea = new JTextArea(15, 30);
        recordsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(recordsArea);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addFitnessRecord());

        updateDisplay();
    }

    private void addFitnessRecord() {
        try {
            double calories = Double.parseDouble(caloriesField.getText());
            if (calories <= 0 || calories > 10000) {
                JOptionPane.showMessageDialog(null, "Unesite validan broj kalorija!");
                return;
            }

            String date = java.time.LocalDate.now().toString();
            FitnessRecord record = new FitnessRecord(userEmail, calories, date);
            dbManager.addFitnessRecord(record);

            caloriesField.setText("");
            updateDisplay();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage());
        }
    }

    private void updateDisplay() {
        ArrayList<FitnessRecord> records = dbManager.getAllRecords(userEmail);
        StringBuilder sb = new StringBuilder();
        for (FitnessRecord r : records) {
            sb.append(r.getDate()).append(": ")
                    .append(r.getCalories()).append(" kcal\n");
        }

        double avg = dbManager.getAverageCalories(userEmail);
        sb.append("\nProsjek kalorija: ").append(String.format("%.2f", avg));

        recordsArea.setText(sb.toString());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fitness Planner");
        FitnessPlanner planner = new FitnessPlanner();
        frame.setContentPane(planner.getMainPanel());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
