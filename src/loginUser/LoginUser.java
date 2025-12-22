package loginUser;

import javax.swing.*;

import mainPanel.MainPanel;
import lifemanagmentsystem.SessionManager;
import lifemanagmentsystem.UserService;
import registerUser.RegisterUser;

public class LoginUser extends JFrame {
    private JPanel passwordLabel;
    private JTextField inputEmail;
    private JPasswordField inputPassword;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel titleLabel;
    private JLabel emailLabel;


    private final UserService userService;

    public LoginUser() {
        userService = new UserService();

        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 30, 80, 25);
        panel.add(emailLabel);

        inputEmail = new JTextField(20);
        inputEmail.setBounds(150, 30, 165, 25);
        panel.add(inputEmail);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 70, 80, 25);
        panel.add(passwordLabel);

        inputPassword = new JPasswordField(20);
        inputPassword.setBounds(150, 70, 165, 25);
        panel.add(inputPassword);

        loginButton = new JButton("Login");
        loginButton.setBounds(50, 120, 120, 25);
        panel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(195, 120, 120, 25);
        panel.add(registerButton);

        add(panel);

        loginButton.addActionListener(e -> loginAction());
        registerButton.addActionListener(e -> {
            this.dispose();

            new RegisterUser().setVisible(true);

        });
    }

    private void loginAction() {

        String email = inputEmail.getText();

        String password = new String(inputPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Unesite email i password!");

            return;


        }

        if (userService.loginUser(email, password)) {


            SessionManager.login(email);



            JOptionPane.showMessageDialog(this,
                    "Login uspješan! Dobrodošao " + email);

            this.dispose();

            JFrame frame = new JFrame("Life Management System");
            frame.setSize(600, 400);


            frame.setLocationRelativeTo(null);


            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new MainPanel(email));
            frame.setVisible(true);
        }

         else {
            JOptionPane.showMessageDialog(this, "Pogrešan email ili password!");
        }
    }
}
