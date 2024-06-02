import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManageGradesPage extends JFrame {
    private User admin;
    ResultSet courseResult;
    ResultSet studentResult;
    private JTextField[][][] gradeFields;

    public ManageGradesPage(User admin) {
        this.admin = admin;

        setTitle("Manage Grades");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Grade Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel gradesPanel = new JPanel(new GridLayout(0, 6));
        gradesPanel.setBackground(new Color(8, 234, 234));
        mainPanel.add(gradesPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save Grades");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGradesToDatabase();
            }
        });
        mainPanel.add(saveButton, BorderLayout.SOUTH);

        try {
            loadGradesFromDatabase(gradesPanel);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Unable to retrieve data from the database");
        }

        add(mainPanel);
        setVisible(true);
    }

    private void loadGradesFromDatabase(JPanel gradesPanel) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/university_management_system?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&resultSetType=TYPE_SCROLL_INSENSITIVE", "root", "12345");

        PreparedStatement courseStatement = conn.prepareStatement("SELECT course_name FROM courses");
        ResultSet courseResult = courseStatement.executeQuery();

        PreparedStatement studentStatement = conn.prepareStatement("SELECT username FROM users");
        ResultSet studentResult = studentStatement.executeQuery();

        int courseCount = getResultSetRowCount(courseResult);
        int studentCount = getResultSetRowCount(studentResult);

        gradeFields = new JTextField[courseCount][][];
        for (int i = 0; i < courseCount; i++) {
            gradeFields[i] = new JTextField[studentCount][4];

            // Add course label
            courseResult.beforeFirst();
            JLabel courseLabel = new JLabel(courseResult.getString("course_name"));
            gradesPanel.add(courseLabel);

            // Add student labels and grade fields
            for (int j = 0; j < studentCount; j++) {
                studentResult.next();
                String studentName = studentResult.getString("username");
                JLabel studentLabel = new JLabel(studentName);
                gradesPanel.add(studentLabel);

                for (int k = 0; k < 4; k++) {
                    JTextField gradeField = new JTextField(5);
                    gradesPanel.add(gradeField);
                    gradeFields[i][j][k] = gradeField;
                }
            }
        }

        courseResult = courseStatement.executeQuery(); // Re-execute to position the cursor at the beginning
        studentResult = studentStatement.executeQuery();

        conn.close();
    }

    private void saveGradesToDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/university_management_system?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&resultSetType=TYPE_SCROLL_INSENSITIVE", "root", "12345");

            for (int i = 0; i < gradeFields.length; i++) {
                for (int j = 0; j < gradeFields[i].length; j++) {
                    for (int k = 0; k < gradeFields[i][j].length; k++) {
                        String courseName = courseResult.getString("course_name");
                        String studentName = studentResult.getString("username");
                        String grade = gradeFields[i][j][k].getText();
                        String assessmentType = "Assignment " + (k + 1);
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO grades (user_id, course_id, assessment_type, grade) VALUES (?, ?, ?, ?)");
                        statement.setString(1, studentName);
                        statement.setInt(2, getCourseId(courseName, conn, courseResult, studentResult));
                        statement.setString(3, assessmentType);
                        statement.setString(4, grade);
                        statement.executeUpdate();
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Grades saved successfully to the database");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Unable to save grades to the database");
        }
    }

    private int getResultSetRowCount(@NotNull ResultSet resultSet) throws SQLException {
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }
        return rowCount;
    }

    private int getCourseId(String courseName, Connection conn, ResultSet courseResult, ResultSet studentResult) throws SQLException {
        courseResult.beforeFirst();
        while (courseResult.next()) {
            if (courseResult.getString("course_name").equals(courseName)) {
                return courseResult.getInt("course_id");
            }
        }
        throw new SQLException("Course not found: " + courseName);
    }
}