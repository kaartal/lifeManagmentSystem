package studyPlanner;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import org.bson.Document;

public class StudyPlanner {

    private JPanel mainPanel;
    private JTextField subjectField;
    private JTextField hoursField;
    private JButton addStudyButton;
    private JTextArea studyRecordsArea;
    private JTextArea studyChartArea;

    private StudyInformationTransfer dbManager;
    private String userEmail;

    public StudyPlanner() {
        userEmail = "";
        dbManager = new StudyInformationTransfer();

        mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        subjectField = new JTextField(10);
        hoursField = new JTextField(5);
        addStudyButton = new JButton("Dodaj zapis");

        inputPanel.add(new JLabel("Predmet:"));
        inputPanel.add(subjectField);
        inputPanel.add(new JLabel("Sati:"));
        inputPanel.add(hoursField);
        inputPanel.add(addStudyButton);


        studyRecordsArea = new JTextArea(10,30);
        studyRecordsArea.setEditable(false);
        JScrollPane scrollRecords = new JScrollPane(studyRecordsArea);


        studyChartArea = new JTextArea(10,30);
        studyChartArea.setEditable(false);
        JScrollPane scrollChart = new JScrollPane(studyChartArea);

        JPanel displayPanel = new JPanel(new GridLayout(2,1,5,5));
        displayPanel.add(scrollRecords);
        displayPanel.add(scrollChart);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(displayPanel, BorderLayout.CENTER);

        addStudyButton.addActionListener(e -> addStudyRecord());

        updateDisplay();
    }

    private void addStudyRecord() {
        try {
            String subject = subjectField.getText().trim();
            double hours = Double.parseDouble(hoursField.getText().trim());

            if (subject.isEmpty() || hours <= 0 || hours > 24) {
                JOptionPane.showMessageDialog(null, "Unesite validan predmet i broj sati (0-24)!");
                return;
            }

            String date = java.time.LocalDate.now().toString();
            StudyRecord record = new StudyRecord(userEmail, subject, hours, date);
            dbManager.addStudyRecord(record);

            subjectField.setText("");
            hoursField.setText("");

            updateDisplay();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage());
        }
    }

    private void updateDisplay() {
        ArrayList<StudyRecord> records = dbManager.getAllRecords(userEmail);
        StringBuilder sb = new StringBuilder();
        for (StudyRecord r : records) {
            sb.append(r.getDate())
                    .append(" - ").append(r.getSubject())
                    .append(": ").append(r.getHours()).append(" sati\n");
        }
        studyRecordsArea.setText(sb.toString());

        ArrayList<Document> summary = dbManager.getStudySummary(userEmail);
        StringBuilder chart = new StringBuilder();
        for (Document d : summary) {
            String subject = d.getString("_id");
            double total = d.getDouble("totalHours");
            chart.append(subject).append(": ");
            int stars = (int) total;
            for (int i=0;i<stars;i++) chart.append("█");
            chart.append(" (").append(total).append(" sati)\n");
        }
        studyChartArea.setText(chart.toString());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Study Planner");
        StudyPlanner planner = new StudyPlanner();
        frame.setContentPane(planner.getMainPanel());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
