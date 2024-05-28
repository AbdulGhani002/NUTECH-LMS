import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HomePage extends JFrame {
    public HomePage(User user) {
        setTitle("NUTECH LMS Home");
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        double cgpa = calculateCGPA(user);

        JLabel cgpaLabel = new JLabel("CGPA: " + cgpa + " out of 4.0");
        cgpaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(cgpaLabel, BorderLayout.NORTH);

        JPanel homePage = new JPanel(new GridLayout(10, 1, 10, 10));
        homePage.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        homePage.setBackground(new Color(8, 234, 234));
        JLabel welcomeLabel = new JLabel("Welcome " + user.getUsername() + "!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homePage.add(welcomeLabel);

        JButton viewCoursesButton = new JButton("View Courses");
        JButton viewGradesButton = new JButton("View Grades");
        JButton profileButton = new JButton("Profile");
        JButton logoutButton = new JButton("Logout");

        homePage.add(viewCoursesButton);
        homePage.add(viewGradesButton);
        homePage.add(profileButton);
        homePage.add(logoutButton);

        add(homePage, BorderLayout.CENTER);

        viewCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CoursesPage(user);
                dispose();
            }
        });

        viewGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GradesPage(user);
                dispose();
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfilePage(user);
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

    private double calculateCGPA(User user) {
        double totalOverallScore = 0.0;
        int enrolledCourses = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/university_management_system";
            String dbUsername = "root";
            String dbPassword = "12345";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            String query = "SELECT course_id FROM enrollments WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getUserId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                double overallScore = fetchOverallScore(connection, user.getUserId(), courseId);
                totalOverallScore += overallScore;
                enrolledCourses++;
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        if (enrolledCourses == 0) {
            return 0.0;
        } else {
            return (totalOverallScore / enrolledCourses) / 25.0;
        }
    }

    private double fetchOverallScore(Connection connection, int userId, int courseId) throws SQLException {
        double overallScore = 0.0;
        try {
            String query = "SELECT AVG(grade) AS avg_grade FROM grades WHERE user_id = ? AND course_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, courseId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                overallScore = resultSet.getDouble("avg_grade");
            }

            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return overallScore;
    }
}
