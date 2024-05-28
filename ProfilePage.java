import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePage extends JFrame {
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel addressLabel;

    public ProfilePage(@NotNull User user) {
        setTitle("Profile");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel profilePanel = new JPanel(new GridLayout(5, 2, 10, 10));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        nameLabel = new JLabel("Name: " + user.getUsername());
        emailLabel = new JLabel("Email: " + user.getEmail());
        phoneLabel = new JLabel("Phone: " + user.getPhoneNumber());
        addressLabel = new JLabel("Address: " + user.getAddress());
        profilePanel.setBackground(new Color(8, 234, 234));
        profilePanel.add(new JLabel());
        profilePanel.add(new JLabel());
        profilePanel.add(nameLabel);
        profilePanel.add(emailLabel);
        profilePanel.add(phoneLabel);
        profilePanel.add(addressLabel);

        JButton editButton = new JButton("Edit Profile");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditProfile(user);
                dispose();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomePage(user);
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        buttonPanel.add(editButton);

        add(profilePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
