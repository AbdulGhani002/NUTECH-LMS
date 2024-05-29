import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminHomePage extends JFrame {
    public AdminHomePage(User admin) {
        setTitle("NUTECH SMS Admin Home");
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome Admin " + admin.getUsername() + "!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel adminPage = new JPanel(new GridLayout(10, 1, 10, 10));
        adminPage.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        adminPage.setBackground(new Color(8, 234, 234));
        JButton manageUsersButton = new JButton("Manage Students");
        JButton manageCoursesButton = new JButton("Manage Courses");
        JButton manageGradesButton = new JButton("Manage Grades");
        JButton viewReportsButton = new JButton("View Reports");
        JButton logoutButton = new JButton("Logout");

        adminPage.add(manageUsersButton);
        adminPage.add(manageCoursesButton);
        adminPage.add(manageGradesButton);
        adminPage.add(viewReportsButton);
        adminPage.add(logoutButton);

        add(adminPage, BorderLayout.CENTER);

        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageUsersPage(admin);
                dispose();
            }
        });

        manageCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageCoursesPage(admin);
                dispose();
            }
        });

        manageGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageGradesPage(admin);
                dispose();
            }
        });

        viewReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewReportsPage(admin);
                dispose();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Main();
                dispose();
            }
        });

        setVisible(true);
    }
}
