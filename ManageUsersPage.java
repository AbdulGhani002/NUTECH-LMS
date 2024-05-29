import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ManageUsersPage extends JFrame {
    private JPanel usersPanel;

    public ManageUsersPage(User admin) {
        setTitle("Manage Students");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        usersPanel.setBackground(new Color(8, 234, 234));

        getUsers();
        add(usersPanel);


        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new AdminHomePage(admin);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void getUsers() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/university_management_system";
            String dbUsername = "root";
            String dbPassword = "12345";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            String studentsQuery;
            studentsQuery = "SELECT * FROM users";

            PreparedStatement studentStatement = connection.prepareStatement(studentsQuery);
            ResultSet studentResultSet = studentStatement.executeQuery();

            while (studentResultSet.next()) {
                String studentName = studentResultSet.getString("username");
                String studentId = studentResultSet.getString("user_id");

                JPanel studentPanel = new JPanel();
                studentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
                studentPanel.add(new JLabel(studentId + " - " + studentName));
                studentPanel.setBackground(new Color(8, 234, 234));


                usersPanel.add(studentPanel);

            }

            studentResultSet.close();
            studentStatement.close();
            connection.close();

        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching students: " + ex.getMessage());
        }
    }
}
