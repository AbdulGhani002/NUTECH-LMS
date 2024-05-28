import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManageCoursesPage extends JFrame {
    private User admin;
    private JPanel coursesPanel;

    public ManageCoursesPage(User admin) {
        this.admin = admin;

        setTitle("Manage Courses");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Course Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

        loadCoursesFromDatabase(coursesPanel);

        add(new JScrollPane(coursesPanel), BorderLayout.CENTER);

        JButton addButton = new JButton("Add Course");
        addButton.addActionListener(e -> {
            new AddCourseDialog();
        });

        JButton backButton = new JButton("Back to Admin Home");
        backButton.addActionListener(e -> {
            new AdminHomePage(admin);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadCoursesFromDatabase(JPanel coursesPanel) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university_management_system",
                    "root", "12345");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM courses");

            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");

                JButton courseButton = new JButton(courseName);
                courseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new CourseDetailPage(courseId, courseName);
                    }
                });

                coursesPanel.add(courseButton);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private class AddCourseDialog extends JDialog {
        private JTextField nameField;
        private JTextArea descriptionArea;

        public AddCourseDialog() {
            setTitle("Add New Course");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JPanel formPanel = new JPanel(new GridLayout(2, 2));
            JLabel nameLabel = new JLabel("Name:");
            nameField = new JTextField();

            formPanel.add(nameLabel);
            formPanel.add(nameField);

            JButton addButton = new JButton("Add");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String courseName = nameField.getText();
                    addCourseToDatabase(courseName);

                    coursesPanel.removeAll();
                    loadCoursesFromDatabase(coursesPanel);
                    revalidate();
                    repaint();


                    dispose();
                }
            });

            add(formPanel, BorderLayout.CENTER);
            add(addButton, BorderLayout.SOUTH);

            setVisible(true);
        }

        private void addCourseToDatabase(String name) {

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university_management_system",
                        "root", "12345");
                PreparedStatement statement = connection.prepareStatement("INSERT INTO courses (course_name) VALUES (?)");
                statement.setString(1, name);
                statement.executeUpdate();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
