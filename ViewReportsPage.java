import javax.swing.*;
import java.awt.*;

public class ViewReportsPage extends JFrame {
    public ViewReportsPage(User admin) {
        setTitle("View Reports");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Reports Overview", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel reportsPanel = new JPanel();
        reportsPanel.setLayout(new BoxLayout(reportsPanel, BoxLayout.Y_AXIS));

        // Placeholder for actual reports
        JLabel reportLabel1 = new JLabel("Report 1: Course Enrollment Statistics");
        JLabel reportLabel2 = new JLabel("Report 2: Average Grades per Course");
        JLabel reportLabel3 = new JLabel("Report 3: User Activity Logs");

        reportsPanel.add(reportLabel1);
        reportsPanel.add(reportLabel2);
        reportsPanel.add(reportLabel3);

        add(new JScrollPane(reportsPanel), BorderLayout.CENTER);

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
