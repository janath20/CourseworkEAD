/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package janath;

import java.sql.*;
import javax.swing.JFrame;

/**
 *
 * @author gav
 */
public class ViewAllCoursesForm extends javax.swing.JFrame {

    String userid;

    /**
     * Creates new form ViewAllCoursesForm
     */
    public ViewAllCoursesForm(String id) {
        initComponents();
        this.userid = id;
        loadCourses();
        enrollButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enrollButtonActionPerformed(evt);
            }
        });
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadCourses() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/course_registration", "root", "");
            String sql = "SELECT c.id, c.course_code, c.course_name, "
                    + "(SELECT COUNT(*) FROM registrations r WHERE r.course_id = c.id) AS enroll_count "
                    + "FROM courses c";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                String code = rs.getString("course_code");
                String name = rs.getString("course_name");
                int count = rs.getInt("enroll_count");
                model.addRow(new Object[]{code, name, count});
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    

    // Handles enrollment when enrollButton is pressed
    private void enrollButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a course to enroll.", "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        String courseCode = (String) jTable1.getValueAt(selectedRow, 0);
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/course_registration", "root", "");
            // Find course id by code
            String findCourseSql = "SELECT id FROM courses WHERE course_code = ? LIMIT 1";
            PreparedStatement findStmt = conn.prepareStatement(findCourseSql);
            findStmt.setString(1, courseCode);
            ResultSet rs = findStmt.executeQuery();
            if (!rs.next()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Course not found.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                rs.close();
                findStmt.close();
                conn.close();
                return;
            }
            String courseId = rs.getString("id");
            rs.close();
            findStmt.close();
            // Check if already enrolled
            String checkSql = "SELECT * FROM registrations WHERE student_id = ? AND course_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, userid);
            checkStmt.setString(2, courseId);
            ResultSet checkRs = checkStmt.executeQuery();
            if (checkRs.next()) {
                javax.swing.JOptionPane.showMessageDialog(this, "You are already enrolled in this course.", "Already Enrolled", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                checkRs.close();
                checkStmt.close();
                conn.close();
                return;
            }
            checkRs.close();
            checkStmt.close();
            // Enroll
            String enrollSql = "INSERT INTO registrations (student_id, course_id) VALUES (?, ?)";
            PreparedStatement enrollStmt = conn.prepareStatement(enrollSql);
            enrollStmt.setString(1, userid);
            enrollStmt.setString(2, courseId);
            int rows = enrollStmt.executeUpdate();
            enrollStmt.close();
            conn.close();
            if (rows > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Enrollment successful!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                loadCourses();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to enroll.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        enrollButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("All Available Courses");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Code", "Course", "Current Enroll Count"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        enrollButton.setText("Enroll");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Select Course And");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(enrollButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enrollButton)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton enrollButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
