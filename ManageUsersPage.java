import javax.swing.*;
import java.awt.*;

public class ManageUsersPage extends JFrame {
    public ManageUsersPage(User admin) {
        setTitle("Manage Users");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout and add components
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("User Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));

        // Placeholder content
        JLabel userLabel1 = new JLabel("User 1: John Doe");
        JLabel userLabel2 = new JLabel("User 2: Jane Smith");

        usersPanel.add(userLabel1);
        usersPanel.add(userLabel2);

        add(new JScrollPane(usersPanel), BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Admin Home");
        backButton.addActionListener(e -> {
            new AdminHomePage(admin);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
