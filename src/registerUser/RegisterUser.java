package registerUser;

import loginUser.LoginUser;
import lifemanagmentsystem.SessionManager;
import lifemanagmentsystem.UserService;

import javax.swing.*;

public class RegisterUser extends JFrame { private JTextField inputName; private JLabel titleLabel; private JPanel nameLabel; private JTextField inputLastname; private JLabel lastnameLabel; private JLabel emailLabel; private JTextField inputEmail; private JLabel passwordLabel; private JPasswordField inputPassword; private JPasswordField inputRepeatPassword; private JLabel labelRepeatPassword; private JButton registerButton; private JButton backLogin; private final UserService userService;
    private JComboBox<String> themeComboBox;


    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    public RegisterUser() {
        userService = new UserService();

        setTitle("Registracija");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);


        JLabel nameLabel = new JLabel("Ime:");
        nameLabel.setBounds(50, 20, 100, 25);
        panel.add(nameLabel);

        inputName = new JTextField();
        inputName.setBounds(150, 20, 165, 25);
        panel.add(inputName);


        JLabel lastnameLabel = new JLabel("Prezime:");
        lastnameLabel.setBounds(50, 60, 100, 25);
        panel.add(lastnameLabel);

        inputLastname = new JTextField();
        inputLastname.setBounds(150, 60, 165, 25);
        panel.add(inputLastname);


        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 100, 100, 25);
        panel.add(emailLabel);

        inputEmail = new JTextField();
        inputEmail.setBounds(150, 100, 165, 25);
        panel.add(inputEmail);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 140, 100, 25);
        panel.add(passwordLabel);

        inputPassword = new JPasswordField();
        inputPassword.setBounds(150, 140, 165, 25);
        panel.add(inputPassword);



        JLabel repeatLabel = new JLabel("Ponovi password:");
        repeatLabel.setBounds(50, 180, 120, 25);
        panel.add(repeatLabel);

        inputRepeatPassword = new JPasswordField();
        inputRepeatPassword.setBounds(150, 180, 165, 25);
        panel.add(inputRepeatPassword);


        JLabel themeLabel = new JLabel("Odaberi temu:");
        themeLabel.setBounds(50, 220, 100, 25);
        panel.add(themeLabel);

        themeComboBox = new JComboBox<>(new String[]{"Zelena", "Plava", "Roza", "Narančasta", "Tamna/Dark", "Cyberpunk"});
        themeComboBox.setBounds(150, 220, 165, 25);
        panel.add(themeComboBox);


        registerButton = new JButton("Registruj se");
        registerButton.setBounds(150, 260, 165, 25);
        panel.add(registerButton);


        backLogin = new JButton("Nazad na Login");
        backLogin.setBounds(120, 300, 150, 30);
        panel.add(backLogin);

        add(panel);

        registerButton.addActionListener(e -> registerAction());
        backLogin.addActionListener(e -> {
            SessionManager.logout();
            this.dispose();
            new LoginUser().setVisible(true);
        });
    }

    private void registerAction() {
        String name = inputName.getText();
        String lastname = inputLastname.getText();
        String email = inputEmail.getText();
        String password = new String(inputPassword.getPassword());
        String repeatPassword = new String(inputRepeatPassword.getPassword());
        String theme = (String) themeComboBox.getSelectedItem();


        if (name.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Popunite sve podatke u registraciji!");
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Unesite ispravan email format!");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Minimalan broj karaktera za šifru je 6");
            return;
        }

        if (!password.equals(repeatPassword)) {
            JOptionPane.showMessageDialog(this, "Šifre se ne poklapaju!");
            return;
        }



        if (userService.registerUser(name, lastname, email, password, theme)) {
            JOptionPane.showMessageDialog(this, "Registracija uspješna!");
            this.dispose();
            new LoginUser().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Korisnik već postoji!");
        }
    }
}
