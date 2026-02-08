import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CoordinatorDashboard extends JFrame {
    private JTable sessionTable;
    private DefaultTableModel tableModel;
    private CoordinatorLogic logic = new CoordinatorLogic(); // Connecting to Logic

    public CoordinatorDashboard() {
        setTitle("Faculty Coordinator Panel - Seminar Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manage Sessions", createSessionPanel());
        tabs.addTab("Evaluation Reports", createReportPanel());
        add(tabs);
    }

private JPanel createSessionPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    
    // Table Setup
    String[] columns = {"Student ID", "Evaluator", "Type", "Date", "Venue"};
    tableModel = new DefaultTableModel(columns, 0);
    sessionTable = new JTable(tableModel);
    loadSchedules();

    panel.add(new JLabel("Seminar Schedule Overview", JLabel.CENTER), BorderLayout.NORTH);
    panel.add(new JScrollPane(sessionTable), BorderLayout.CENTER);

    // 1. Define ALL buttons inside this method so they can be "resolved"
    JPanel btnPanel = new JPanel(new FlowLayout()); 

    JButton addSessionBtn = new JButton("Assign New Session");
    JButton editBtn = new JButton("Manage/Edit Session");
    JButton delBtn = new JButton("Remove Session");
    JButton refreshBtn = new JButton("🔄 Refresh List");

    // 2. Add Listeners
    addSessionBtn.addActionListener(e -> assignSession());
    editBtn.addActionListener(e -> manageSelectedSession());
    delBtn.addActionListener(e -> deleteSelectedSession());
    refreshBtn.addActionListener(e -> loadSchedules());

    // 3. Add them to the panel
    btnPanel.add(addSessionBtn);
    btnPanel.add(editBtn);
    btnPanel.add(delBtn);
    btnPanel.add(refreshBtn);

    panel.add(btnPanel, BorderLayout.SOUTH);

    return panel;
}

    private void loadSchedules() {
        tableModel.setRowCount(0);
        for (Object[] row : logic.getSessionData()) tableModel.addRow(row);
    }

private void deleteSelectedSession() {
    int selectedRow = sessionTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Select a session to remove.");
        return;
    }

    String sidToDelete = tableModel.getValueAt(selectedRow, 0).toString();
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to remove the session for Student: " + sidToDelete + "?",
        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // FIX: Surround with try-catch to handle the IOException
            logic.deleteSession(sidToDelete);
            
            loadSchedules(); 
            JOptionPane.showMessageDialog(this, "Session Removed.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error deleting from file: " + e.getMessage());
        }
    }
}

    private void assignSession() {
        JTextField sid = new JTextField();
        JTextField eid = new JTextField();
        JPanel p = new JPanel(new GridLayout(0,1));
        p.add(new JLabel("Student ID:")); p.add(sid);
        p.add(new JLabel("Evaluator ID:")); p.add(eid);
        
        if (JOptionPane.showConfirmDialog(null, p, "Assign", 2) == 0) {
            if (logic.isValidEvaluator(eid.getText())) {
                saveSession(sid.getText(), eid.getText(), "2026-02-10", "Hall A", "Oral");
            }
        }
    }

    private void saveSession(String sid, String eid, String date, String venue, String type) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("sessions.txt", true)))) {
            out.println(sid + "|" + eid + "|" + date + "|" + venue + "|" + type);
            loadSchedules();
        } catch (IOException e) { e.printStackTrace(); }
    }

private JPanel createReportPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JTextArea reportArea = new JTextArea();
    reportArea.setEditable(false);
    reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    
    JButton genBtn = new JButton("Generate Award Nominations (Analytics)");
    
genBtn.addActionListener(e -> {
    reportArea.setText(""); 
    // Show the simple status list
    reportArea.append(logic.getConsolidatedStatus()); 
    reportArea.append("\n" + "-".repeat(50) + "\n");
    // Show the awards analytics
    reportArea.append(logic.getFullReport()); 
});

    panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
    panel.add(genBtn, BorderLayout.SOUTH);
    return panel;
}

    private void manageSelectedSession() {
    int selectedRow = sessionTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Select a session first!");
        return;
    }

    // 1. Get current data from table
    String currentSid = tableModel.getValueAt(selectedRow, 0).toString();
    String currentEid = tableModel.getValueAt(selectedRow, 1).toString();
    String currentType = tableModel.getValueAt(selectedRow, 2).toString();
    String currentDate = tableModel.getValueAt(selectedRow, 3).toString();
    String currentVenue = tableModel.getValueAt(selectedRow, 4).toString();

    // 2. Setup the Form
    JTextField eidField = new JTextField(currentEid, 10);
    JTextField dateField = new JTextField(currentDate, 10);
    JTextField venueField = new JTextField(currentVenue, 10);
    JComboBox<String> typeBox = new JComboBox<>(new String[]{"Oral", "Poster"});
    typeBox.setSelectedItem(currentType);

    JPanel myPanel = new JPanel(new GridLayout(0, 1));
    myPanel.add(new JLabel("Updating Student: " + currentSid));
    myPanel.add(new JLabel("Evaluator ID:")); myPanel.add(eidField);
    myPanel.add(new JLabel("Date:")); myPanel.add(dateField);
    myPanel.add(new JLabel("Venue:")); myPanel.add(venueField);
    myPanel.add(new JLabel("Type:")); myPanel.add(typeBox);

    // 3. Show Dialog and Save via Logic
    int result = JOptionPane.showConfirmDialog(null, myPanel, "Edit Session", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        try {
            // This is the line causing the error - now wrapped in try-catch
            logic.updateSessionInFile(
                currentSid, 
                eidField.getText(), 
                dateField.getText(), 
                venueField.getText(), 
                (String)typeBox.getSelectedItem()
            );
            
            loadSchedules(); // Refresh the UI table
            JOptionPane.showMessageDialog(this, "Update Successful!");
            
        } catch (IOException e) {
            // This handles the "Unhandled Exception" error
            JOptionPane.showMessageDialog(this, "System Error: Could not save changes to sessions.txt");
            e.printStackTrace();
        }
    }

}

}