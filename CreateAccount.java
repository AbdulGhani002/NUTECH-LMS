import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreateAccount extends JFrame {
    public CreateAccount() {
        setTitle("Create Account");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel signupPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        signupPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();

        JButton createAccountButton = new JButton("Create Account");
        JButton loginButton = new JButton("Login");

        signupPanel.add(usernameLabel);
        signupPanel.add(usernameField);
        signupPanel.add(passwordLabel);
        signupPanel.add(passwordField);
        signupPanel.add(confirmPasswordLabel);
        signupPanel.add(confirmPasswordField);
        signupPanel.add(emailLabel);
        signupPanel.add(emailField);
        signupPanel.add(phoneLabel);
        signupPanel.add(phoneField);
        signupPanel.add(addressLabel);
        signupPanel.add(addressField);
        signupPanel.add(loginButton);
        signupPanel.add(createAccountButton);

        add(signupPanel, BorderLayout.CENTER);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                char[] confirmPassword = confirmPasswordField.getPassword();
                String email = emailField.getText();
                String phoneNumber = phoneField.getText();
                String address = addressField.getText();

                // Check if any field is empty
                if (username.isEmpty() || String.valueOf(password).isEmpty() || String.valueOf(confirmPassword).isEmpty() ||
                        email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateAccount.this, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if passwords match
                if (!String.valueOf(password).equals(String.valueOf(confirmPassword))) {
                    JOptionPane.showMessageDialog(CreateAccount.this, "Passwords do not match.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (createNewUser(username, String.valueOf(password), email, phoneNumber, address)) {
                    JOptionPane.showMessageDialog(CreateAccount.this, "Account created successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    new Main();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(CreateAccount.this, "Failed to create account.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Main();
                dispose();
            }
        });

        setVisible(true);
    }

    private boolean createNewUser(String username, String password, String email, String phone, String address) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/university_management_system";
            String dbUsername = "root";
            String dbPassword = "12345";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            String query = "INSERT INTO users (username, password, email, phone_number, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phone);
            statement.setString(5, address);

            int rowsInserted = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsInserted > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
