import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CoursesPage extends JFrame {
    private JList<String> enrolledCoursesList;
    private JList<String> availableCoursesList;
    private DefaultListModel<String> enrolledCoursesModel;
    private DefaultListModel<String> availableCoursesModel;
    private User user;

    public CoursesPage(User user) {
        this.user = user;
        setTitle("Courses");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel coursesLabel = new JLabel("Courses for " + user.getUsername(), SwingConstants.CENTER);
        add(coursesLabel, BorderLayout.NORTH);

        enrolledCoursesModel = new DefaultListModel<>();
        availableCoursesModel = new DefaultListModel<>();

        enrolledCoursesList = new JList<>(enrolledCoursesModel);
        availableCoursesList = new JList<>(availableCoursesModel);

        loadCourses();

        JPanel listsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        listsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel enrolledPanel = new JPanel(new BorderLayout());
        enrolledPanel.add(new JLabel("Enrolled Courses:"), BorderLayout.NORTH);
        enrolledPanel.add(new JScrollPane(enrolledCoursesList), BorderLayout.CENTER);

        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.add(new JLabel("Available Courses:"), BorderLayout.NORTH);
        availablePanel.add(new JScrollPane(availableCoursesList), BorderLayout.CENTER);

        listsPanel.add(enrolledPanel);
        listsPanel.add(availablePanel);

        add(listsPanel, BorderLayout.CENTER);

        JButton enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = availableCoursesList.getSelectedValue();
                if (selectedCourse != null && enrollInCourse(selectedCourse)) {
                    enrolledCoursesModel.addElement(selectedCourse);
                    availableCoursesModel.removeElement(selectedCourse);
                    JOptionPane.showMessageDialog(CoursesPage.this, "Enrolled in " + selectedCourse + " successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(CoursesPage.this, "Failed to enroll in " + selectedCourse, "Error", JOptionPane.ERROR_MESSAGE);
                }
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(enrollButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadCourses() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/university_management_system";
            String dbUsername = "root";
            String dbPassword = "12345";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Load enrolled courses
            String enrolledQuery = "SELECT course_name FROM courses c JOIN enrollments e ON c.course_id = e.course_id WHERE e.user_id = ?";
            PreparedStatement enrolledStatement = connection.prepareStatement(enrolledQuery);
            enrolledStatement.setInt(1, user.getUserId());
            ResultSet enrolledResultSet = enrolledStatement.executeQuery();
            while (enrolledResultSet.next()) {
                enrolledCoursesModel.addElement(enrolledResultSet.getString("course_name"));
            }
            enrolledResultSet.close();
            enrolledStatement.close();

            // Load available courses
            String availableQuery = "SELECT course_name FROM courses WHERE course_id NOT IN (SELECT course_id FROM enrollments WHERE user_id = ?)";
            PreparedStatement availableStatement = connection.prepareStatement(availableQuery);
            availableStatement.setInt(1, user.getUserId());
            ResultSet availableResultSet = availableStatement.executeQuery();
            while (availableResultSet.next()) {
                availableCoursesModel.addElement(availableResultSet.getString("course_name"));
            }
            availableResultSet.close();
            availableStatement.close();

            connection.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private boolean enrollInCourse(String courseName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/university_management_system";
            String dbUsername = "root";
            String dbPassword = "12345";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            String courseQuery = "SELECT course_id FROM courses WHERE course_name = ?";
            PreparedStatement courseStatement = connection.prepareStatement(courseQuery);
            courseStatement.setString(1, courseName);
            ResultSet courseResultSet = courseStatement.executeQuery();

            if (courseResultSet.next()) {
                int courseId = courseResultSet.getInt("course_id");

                String enrollQuery = "INSERT INTO enrollments (user_id, course_id) VALUES (?, ?)";
                PreparedStatement enrollStatement = connection.prepareStatement(enrollQuery);
                enrollStatement.setInt(1, user.getUserId());
                enrollStatement.setInt(2, courseId);

                int rowsInserted = enrollStatement.executeUpdate();

                enrollStatement.close();
                courseStatement.close();
                connection.close();

                return rowsInserted > 0;
            }

            courseResultSet.close();
            courseStatement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
