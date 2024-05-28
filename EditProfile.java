import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class EditProfile extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private User user;

    public EditProfile(@NotNull User user) {
        this.user = user;

        setTitle("Edit Profile");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel editPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        editPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        nameField = new JTextField(user.getUsername(), 20);
        emailField = new JTextField(user.getEmail(), 20);
        phoneField = new JTextField(user.getPhoneNumber(), 20);
        addressField = new JTextField(user.getAddress(), 20);

        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Email:"));
        editPanel.add(emailField);
        editPanel.add(new JLabel("Phone:"));
        editPanel.add(phoneField);
        editPanel.add(new JLabel("Address:"));
        editPanel.add(addressField);
        editPanel.add(new JLabel());
        editPanel.add(new JLabel());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                user.setUsername(nameField.getText());
                user.setEmail(emailField.getText());
                user.setPhoneNumber(phoneField.getText());
                user.setAddress(addressField.getText());

                updateUserInDatabase(user);

                JOptionPane.showMessageDialog(EditProfile.this,
                        "Profile updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                new ProfilePage(user);
                dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfilePage(user);
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        add(editPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateUserInDatabase(User user) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/university_management_system";
            String dbUsername = "root";
            String dbPassword = "12345";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            String query = "UPDATE users SET username=?, email=?, phone_number=?, address=? WHERE user_id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhoneNumber());
            statement.setString(4, user.getAddress());
            statement.setInt(5, user.getUserId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User data updated successfully in the database.");
            }

            statement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
