import javax.swing.*;
import java.awt.*;

public class CourseDetailPage extends JFrame {
    private int courseId;
    private String courseName;
    private String courseDescription;

    public CourseDetailPage(int courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;

        setTitle("Course Details - " + courseName);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Course Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel("Name: " + courseName);
        detailsPanel.add(nameLabel);
        add(detailsPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
