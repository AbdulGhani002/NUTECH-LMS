import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GradesPage extends JFrame {
    private JTable gradesTable;

    public GradesPage(@NotNull User user) {
        setTitle("Grades");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel gradesLabel = new JLabel("Grades for " + user.getUsername(), SwingConstants.CENTER);
        add(gradesLabel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        gradesTable = new JTable(tableModel);
        tableModel.addColumn("Course");
        tableModel.addColumn("Assessment");
        tableModel.addColumn("Score");

        JScrollPane scrollPane = new JScrollPane(gradesTable);
        add(scrollPane, BorderLayout.CENTER);
        fetchAndCalculateGrades(user);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomePage(user);
                dispose();
            }
        });
        add(backButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void fetchAndCalculateGrades(User user) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/university_management_system";
            String dbUsername = "root";
            String dbPassword = "12345";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            String courseQuery = "SELECT course_id, course_name FROM courses";
            PreparedStatement courseStatement = connection.prepareStatement(courseQuery);
            ResultSet courseResultSet = courseStatement.executeQuery();

            while (courseResultSet.next()) {
                int courseId = courseResultSet.getInt("course_id");
                String courseName = courseResultSet.getString("course_name");

                double quizGrade = fetchGrade(connection, user.getUserId(), courseId, "quiz");
                double assignmentGrade = fetchGrade(connection, user.getUserId(), courseId, "assignment");
                double midtermGrade = fetchGrade(connection, user.getUserId(), courseId, "midterm");
                double finalGrade = fetchGrade(connection, user.getUserId(), courseId, "final");

                double overallScore = (quizGrade * 0.1) + (assignmentGrade * 0.2) + (midtermGrade * 0.3) + (finalGrade * 0.4);

                Object[][] rowData = {
                        {courseName, "Quiz", quizGrade},
                        {courseName, "Assignment", assignmentGrade},
                        {courseName, "Midterm", midtermGrade},
                        {courseName, "Final", finalGrade}
                };
                for (Object[] row : rowData) {
                    ((DefaultTableModel) gradesTable.getModel()).addRow(row);
                }
                ((DefaultTableModel) gradesTable.getModel()).addRow(new Object[]{"Overall Score", "", overallScore});
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private double fetchGrade(Connection connection, int userId, int courseId, String assessmentType) throws SQLException {
        String query = "SELECT AVG(grade) AS avg_grade " +
                "FROM grades g " +
                "WHERE g.user_id = ? AND g.course_id = ? AND g.assessment_type = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setInt(2, courseId);
        statement.setString(3, assessmentType);
        ResultSet resultSet = statement.executeQuery();

        double grade = 0.0;
        if (resultSet.next()) {
            grade = resultSet.getDouble("avg_grade");
        }

        resultSet.close();
        statement.close();

        return grade;
    }
}
