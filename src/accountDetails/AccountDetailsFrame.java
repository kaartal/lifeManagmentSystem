package accountDetails;

import lifemanagmentsystem.UserService;
import mainPanel.MainPanel;

import javax.swing.*;

public class AccountDetailsFrame extends JFrame {

    private JTextField editName;
    private JTextField editLastname;
    private JTextField editMail;

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField editPassword;
    private JLabel titleLabel;
    private JLabel nameLabel;
    private JLabel lastnameLabel;
    private JLabel mailLabel;
    private JLabel oldPasswordLabel;
    private JLabel newPasswordLabel;

    private JButton saveChanges;
    private JButton backMain;
    private JComboBox themeComboBox;
    private JLabel themeLabel;

    private final UserService userService;
    private final String currentUserEmail;

    public AccountDetailsFrame(String email) {
        this.currentUserEmail = email;
        this.userService = new UserService();

        setTitle("Account Details");
        setSize(420, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(null);

        titleLabel = new JLabel("Profil korisnika");
        titleLabel.setBounds(150, 10, 200, 25);
        panel.add(titleLabel);

        nameLabel = new JLabel("Ime:");
        nameLabel.setBounds(50, 50, 120, 25);
        panel.add(nameLabel);

        editName = new JTextField();
        editName.setBounds(180, 50, 165, 25);
        panel.add(editName);

        lastnameLabel = new JLabel("Prezime:");
        lastnameLabel.setBounds(50, 90, 120, 25);
        panel.add(lastnameLabel);

        editLastname = new JTextField();
        editLastname.setBounds(180, 90, 165, 25);
        panel.add(editLastname);

        mailLabel = new JLabel("Email:");
        mailLabel.setBounds(50, 130, 120, 25);
        panel.add(mailLabel);

        editMail = new JTextField();
        editMail.setBounds(180, 130, 165, 25);
        editMail.setEditable(false);
        panel.add(editMail);

        oldPasswordLabel = new JLabel("Stari password:");
        oldPasswordLabel.setBounds(50, 170, 120, 25);
        panel.add(oldPasswordLabel);

        oldPasswordField = new JPasswordField();
        oldPasswordField.setBounds(180, 170, 165, 25);
        panel.add(oldPasswordField);

        newPasswordLabel = new JLabel("Novi password:");
        newPasswordLabel.setBounds(50, 210, 120, 25);
        panel.add(newPasswordLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(180, 210, 165, 25);
        panel.add(newPasswordField);

        saveChanges = new JButton("Sačuvaj");
        saveChanges.setBounds(50, 270, 130, 25);
        panel.add(saveChanges);

        backMain = new JButton("Back");
        backMain.setBounds(215, 270, 130, 25);
        panel.add(backMain);

        add(panel);

        loadUserData();
        themeLabel = new JLabel("Tema:");
        themeLabel.setBounds(50, 210, 100, 25);
        panel.add(themeLabel);
        themeComboBox = new JComboBox<>(new String[]{"Zelena", "Plava", "Roza", "Narančasta", "Cyberpunk"});
        themeComboBox.setBounds(150, 210, 165, 25);
        panel.add(themeComboBox);
        saveChanges.addActionListener(e -> saveChangesAction());

        backMain.addActionListener(e -> {
            this.dispose();
            JFrame frame = new JFrame("Life Management System");
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new MainPanel(currentUserEmail));
            frame.setVisible(true);
        });
    }

    private void loadUserData() {
        var user = userService.getUserByEmail(currentUserEmail);
        if (user != null) {
            editName.setText(user.getString("name"));
            editLastname.setText(user.getString("lastname"));
            editMail.setText(user.getString("email"));
        }
    }

    private void saveChangesAction() {

        String name = editName.getText();
        String lastname = editLastname.getText();
        String oldInputPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String selectedTheme = (String) themeComboBox.getSelectedItem();

        var user = userService.getUserByEmail(currentUserEmail);
        if (user == null) return;

        boolean isChanged = false;


        if (!name.equals(user.getString("name"))) {
            isChanged = true;
        }


        if (!lastname.equals(user.getString("lastname"))) {
            isChanged = true;
        }


        if (!selectedTheme.equals(user.getString("theme"))) {
            isChanged = true;
        }


        String finalPassword = user.getString("password");
        if (!newPassword.isEmpty()) {
            if (oldInputPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite stari password da biste promijenili šifru!");
                return;
            }
            if (!userService.loginUser(currentUserEmail, oldInputPassword)) {
                JOptionPane.showMessageDialog(this, "Stari password nije tačan!");
                return;
            }
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this, "Novi password mora imati najmanje 6 karaktera!");
                return;
            }
            finalPassword = newPassword;
            isChanged = true;
        }

        if (!isChanged) {
            JOptionPane.showMessageDialog(this, "Nema promjena za sačuvati.");
            return;
        }

        boolean updated = userService.updateUserTheme(currentUserEmail, name, lastname, finalPassword, selectedTheme);

        if (updated) {
            JOptionPane.showMessageDialog(this, "Podaci uspješno ažurirani!");
            oldPasswordField.setText("");
            newPasswordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Greška pri ažuriranju podataka!");
        }
    }

}
