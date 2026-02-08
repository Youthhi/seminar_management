import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EvaluatorDashboard extends JFrame {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private String currentEvaluatorId; // The ID of the person logged in

    public EvaluatorDashboard(String evaluatorId) {
        this.currentEvaluatorId = evaluatorId;
        setTitle("Evaluator Panel - Logged in as: " + evaluatorId);
        setSize(600, 400);
        
        tableModel = new DefaultTableModel(new String[]{"Student ID", "Date", "Venue", "Type"}, 0);
        studentTable = new JTable(tableModel);
        
        loadMyAssignedStudents(); // Only load what belongs to this ID

        add(new JLabel("Students Assigned to You:", JLabel.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);
        
        JButton evaluateBtn = new JButton("Grade Selected Student");
        evaluateBtn.addActionListener(e -> openGradingForm());
        add(evaluateBtn, BorderLayout.SOUTH);
    }

    private void loadMyAssignedStudents() {
        tableModel.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("sessions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                // Check if the Evaluator ID in the file (d[1]) matches the logged-in ID
                if (d.length >= 5 && d[1].equals(currentEvaluatorId)) {
                    // Only show SID, Date, Venue, Type to the evaluator
                    tableModel.addRow(new Object[]{d[0], d[2], d[3], d[4]});
                }
            }
        } catch (IOException e) {
            System.out.println("No sessions assigned yet.");
        }
    }
    
    private void openGradingForm() {
        
    int row = studentTable.getSelectedRow();
    if (row != -1) {
        // 1. Get the ID of the student you clicked on
        String sid = tableModel.getValueAt(row, 0).toString();
        
        // 2. OPEN THE CONTROLLER (This is where your rubrics are!)
        // We pass the Student ID and the Evaluator ID
        new EvaluatorController(sid, currentEvaluatorId).setVisible(true);
        // We pass the Student ID and the Evaluator's own ID
        EvaluatorController gradingForm = new EvaluatorController(sid, currentEvaluatorId);
        gradingForm.setVisible(true);
        
        // 3. Close the list so you can focus on grading
        this.dispose(); 
    } else {
        JOptionPane.showMessageDialog(this, "Please select a student from the table first!");
    }
}
}