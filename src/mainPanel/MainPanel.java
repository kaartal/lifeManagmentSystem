package mainPanel;

import accountDetails.AccountDetailsFrame;
import lifemanagmentsystem.SessionManager;
import lifemanagmentsystem.UserService;
import loginUser.LoginUser;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private JButton accountDetails;
    private JButton financeTracker;
    private JButton taskPlanner;
    private JButton mealPlanner;
    private JButton studyPlanner;
    private JButton fitnessPlanner;
    private JButton logoutButton;
    private String currentUserEmail;

    public MainPanel(String email) {
        this.currentUserEmail = email;
        UserService userService = new UserService();
        String theme = userService.getUserTheme(currentUserEmail);
setSize(950,720);
        setBackground(getColorFromTheme(theme));
        setLayout(new BorderLayout());

        //createUIComponents();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(new Color(220, 80, 80));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        topPanel.add(logoutButton);

        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.setLayout(new GridLayout(2, 3, 20, 20));
        mainButtonPanel.setOpaque(false);
        mainButtonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JButton[] featureButtons = {
                accountDetails, financeTracker, taskPlanner,
                mealPlanner, studyPlanner, fitnessPlanner
        };

        for (int i = 0; i < featureButtons.length; i++) {
            JButton button = featureButtons[i];
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(50, 100, 150), 2),
                    BorderFactory.createEmptyBorder(15, 10, 15, 10)
            ));

            final int index = i;
            button.addActionListener(e -> showFeatureInProgress(index));

            mainButtonPanel.add(button);
        }

        add(topPanel, BorderLayout.NORTH);
        add(mainButtonPanel, BorderLayout.CENTER);

        logoutButton.addActionListener(e -> logoutAction());
    }

   // private void createUIComponents() {
      //  accountDetails = new JButton("Account Details");
       // financeTracker = new JButton("Finance Tracker");
       // taskPlanner = new JButton("Task Planner");
       // mealPlanner = new JButton("Meal Planner");
       // studyPlanner = new JButton("Study Planner");
       // fitnessPlanner = new JButton("Fitness Planner");
        //logoutButton = new JButton("Odjavi se");
    //}

    private void showFeatureInProgress(int featureIndex) {
        String[] featureNames = {
                "Account Details",
                "Finance Tracker",
                "Task Planner",
                "Meal Planner",
                "Study Planner",
                "Fitness Planner"
        };

        if (featureIndex == 0) {
            AccountDetailsFrame frame = new AccountDetailsFrame(currentUserEmail);
            frame.setVisible(true);
            Window mainWindow = SwingUtilities.getWindowAncestor(this);
            mainWindow.setVisible(false);
            frame.setSize(mainWindow.getSize());
            frame.setLocation(mainWindow.getLocation());
            return;
        }

        String message = String.format(
                "<html><div style='text-align: center;'><h3>%s</h3>"
                        + "<p style='margin: 10px 0;'>Ova funkcionalnost je trenutno u izradi.</p>"
                        + "<p>Vraćamo se uskoro sa punom verzijom!</p></div></html>",
                featureNames[featureIndex]
        );

        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JOptionPane.showMessageDialog(
                this,
                messageLabel,
                "Funkcionalnost u izradi",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private Color getColorFromTheme(String theme) {
        if (theme == null) {
            return new Color(166, 244, 136);
        }

        switch (theme) {
            case "Plava":
                return new Color(173, 216, 230);
            case "Roza":
                return new Color(255, 228, 225);
            case "Narandzasta":
                return new Color(205, 162, 132);
            case "Tamna/Dark":
                return new Color(40, 44, 52);
            case "Cyberpunk":
                return new Color(10, 10, 20);
            default:
                return new Color(166, 244, 136);
        }
    }

    private void logoutAction() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "<html><div style='text-align: center;'>"
                        + "<p style='font-size: 14px; margin-bottom: 10px;'>Da li ste sigurni da se želite odjaviti?</p>"
                        + "</div></html>",
                "Potvrda odjave",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.logout();
            SwingUtilities.getWindowAncestor(this).dispose();
            new LoginUser().setVisible(true);
        }
    }
}