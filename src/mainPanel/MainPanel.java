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

    private void createUIComponents() {
        accountDetails = new JButton("Account Details");
        financeTracker = new JButton("Finance Tracker");
        taskPlanner = new JButton("Task Planner");
        mealPlanner = new JButton("Meal Planner");
        studyPlanner = new JButton("Study Planner");
        fitnessPlanner = new JButton("Fitness Planner");
        logoutButton = new JButton("Logout");
    }

    public MainPanel(String email) {
        this.currentUserEmail = email;
        UserService userService = new UserService();
        String theme = userService.getUserTheme(currentUserEmail);
        setBackground(getColorFromTheme(theme));

        setLayout(null);
        setLayout(null);

        accountDetails.setBounds(50, 20, 200, 30);
        add(accountDetails);

        financeTracker.setBounds(50, 60, 200, 30);
        add(financeTracker);

        taskPlanner.setBounds(50, 100, 200, 30);
        add(taskPlanner);

        mealPlanner.setBounds(50, 140, 200, 30);
        add(mealPlanner);

        studyPlanner.setBounds(50, 180, 200, 30);
        add(studyPlanner);

        fitnessPlanner.setBounds(50, 220, 200, 30);
        add(fitnessPlanner);

        logoutButton.setBounds(350, 20, 150, 30);
        add(logoutButton);

        logoutButton.addActionListener(e -> logoutAction());

        // OPEN ACCOUNT DETAILS INFORMATION
        accountDetails.addActionListener(e -> {
            AccountDetailsFrame frame =
                    new AccountDetailsFrame(currentUserEmail);
            frame.setVisible(true);

            SwingUtilities.getWindowAncestor(this).setVisible(false);
        });
    }
    private Color getColorFromTheme(String theme) {
        if (theme == null) {
            return Color.GREEN;
        }

        switch (theme) {
            case "Plava":
                return Color.BLUE;
            case "Roza":
                return Color.PINK;
            case "Narančasta":
                return Color.ORANGE;
            case "Tamna/Dark":
                return Color.DARK_GRAY;
            case "Cyberpunk":
                return new java.awt.Color(20, 20, 30);
            default:
                return java.awt.Color.GREEN;
        }
    }


    private void logoutAction() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Da li ste sigurni da se želite odjaviti?",
                "Odjava",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            SessionManager.logout();

            SwingUtilities.getWindowAncestor(this).dispose();

            new LoginUser().setVisible(true);
        }

    }

}
