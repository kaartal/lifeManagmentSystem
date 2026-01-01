package habitTracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import mainPanel.MainPanel;
import lifemanagmentsystem.SessionManager;

public class HabitTracker {

    private JPanel mainPanel;
    private JTextField habitField;
    private JCheckBox completedCheckBox;
    private JButton addButton, backButton;
    private JTextArea habitArea;

    private final HabitInformationTransfer dbManager;
    private final String userEmail;


    public HabitTracker(String userEmail) {
        this.userEmail = userEmail;
        dbManager = new HabitInformationTransfer();
        Color themeColor = SessionManager.getThemeColor();


        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(themeColor);


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(236, 240, 241));


        backButton = createModernButton("← Nazad", new Color(127, 140, 141));
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(236, 240, 241));
        backPanel.add(backButton);
        topPanel.add(backPanel);


        habitField = new JTextField();
        completedCheckBox = new JCheckBox("Završeno");
        addButton = createModernButton("Dodaj naviku", new Color(72, 201, 176));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setBackground(new Color(236, 240, 241));
        inputPanel.add(new JLabel("Navika:"));
        habitField.setPreferredSize(new Dimension(200, 28));
        inputPanel.add(habitField);
        inputPanel.add(completedCheckBox);
        inputPanel.add(addButton);
        topPanel.add(inputPanel);



        habitArea = new JTextArea(20, 30);
        habitArea.setEditable(false);
        habitArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(habitArea);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);



        addButton.addActionListener(e -> addHabit());
        backButton.addActionListener(e -> goBackToMainPanel());

        updateDisplay();
    }

    private void addHabit() {
        String habit = habitField.getText().trim();
        boolean completed = completedCheckBox.isSelected();

        if (habit.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Unesite naziv navike!");
            return;
        }

        String date = java.time.LocalDate.now().toString();
        dbManager.addHabitRecord(new HabitRecord(userEmail, habit, completed, date));

        habitField.setText("");
        completedCheckBox.setSelected(false);
        updateDisplay();
    }

    private void updateDisplay() {
        ArrayList<String> habits = dbManager.getUniqueHabits(userEmail);
        StringBuilder sb = new StringBuilder();
        for (String habit : habits) {
            double percentage = dbManager.getCompletionPercentage(userEmail, habit);
            sb.append(habit).append(": ").append(String.format("%.2f", percentage)).append("% izvršeno\n");
        }
        habitArea.setText(sb.toString());
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

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
