import javax.swing.*;
import java.awt.*;

public class ManageGradesPage extends JFrame {
    public ManageGradesPage(User admin) {
        setTitle("Manage Grades");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout and add components
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Grade Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel gradesPanel = new JPanel();
        gradesPanel.setLayout(new BoxLayout(gradesPanel, BoxLayout.Y_AXIS));

        // Placeholder content
        JLabel gradeLabel1 = new JLabel("Course 1: Student Grades");
        JLabel gradeLabel2 = new JLabel("Course 2: Student Grades");

        gradesPanel.add(gradeLabel1);
        gradesPanel.add(gradeLabel2);

        add(new JScrollPane(gradesPanel), BorderLayout.CENTER);

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
