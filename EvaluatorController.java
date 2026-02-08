import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class EvaluatorController extends JFrame implements ActionListener {

    private JComboBox<String> studentIdPicker;
    private JTextField studentNameField, presentationTitleField, presentationTypeField;
    private JTextField problemClarityField, methodologyField, resultsField, presentationField;
    private JTextArea commentsArea;
    private JButton submitBtn, viewPdfBtn; // Added PDF button
    
    private List<Student> studentList;
    private Student currentStudent;
    private String currentEvaluatorId; // Add this at the top!
    private JLabel totalScoreLabel; // At the top of the class
    private JCheckBox voteCheckbox; // At the top with other fields

    public EvaluatorController(String targetStudentId, String loggedInEvalId) {

    this.currentEvaluatorId = loggedInEvalId; // Store the evaluator's ID
    List<Student> allStudents = loadStudents("student.txt");
    this.studentList = new ArrayList<>();

    for (Student s : allStudents) {
        // .trim() removes hidden spaces, .equalsIgnoreCase handles S001 vs s001
        if (s.getStudentID().trim().equalsIgnoreCase(targetStudentId.trim())) {
            this.studentList.add(s);
            this.currentStudent = s;
            break;
        }
    }

    if (currentStudent == null) {
        JOptionPane.showMessageDialog(null, "Student data not found!");
        return;
    }

        setupUI();
        // Initialize with the first student
        updateSelectedStudent(targetStudentId);    
    
}

    private void setupUI() {
        setTitle("Evaluator Panel - Review System");
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 15));

        // --- Selection & Info Section ---
        String[] ids = studentList.stream().map(Student::getStudentID).toArray(String[]::new);
        studentIdPicker = new JComboBox<>(ids);
        studentIdPicker.addActionListener(e -> updateSelectedStudent((String) studentIdPicker.getSelectedItem()));

        studentNameField = createReadOnlyField();
        presentationTitleField = createReadOnlyField();
        presentationTypeField = createReadOnlyField();

        addLabelAndComponent(formPanel, "Select Student ID:", studentIdPicker);
        addLabelAndComponent(formPanel, "Student Name:", studentNameField);
        addLabelAndComponent(formPanel, "Research Title:", presentationTitleField);
        addLabelAndComponent(formPanel, "Type:", presentationTypeField);

        // --- PDF View Button ---
        viewPdfBtn = new JButton("Open Presentation (PDF)");
        viewPdfBtn.addActionListener(e -> openStudentPdf());
        formPanel.add(new JLabel("File Attachment:"));
        formPanel.add(viewPdfBtn);

        formPanel.add(new JSeparator()); formPanel.add(new JSeparator());

        // --- Rubric Section ---
        problemClarityField = new JTextField();
        methodologyField = new JTextField();
        resultsField = new JTextField();
        presentationField = new JTextField();

        addLabelAndComponent(formPanel, "Problem Clarity (0-10):", problemClarityField);
        addLabelAndComponent(formPanel, "Methodology (0-10):", methodologyField);
        addLabelAndComponent(formPanel, "Results (0-10):", resultsField);
        addLabelAndComponent(formPanel, "Presentation Skills (0-10):", presentationField);

        commentsArea = new JTextArea(3, 20);
        commentsArea.setLineWrap(true);
        formPanel.add(new JLabel("Comments:"));
        formPanel.add(new JScrollPane(commentsArea));

        submitBtn = new JButton("Submit Evaluation");
        submitBtn.addActionListener(this);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        totalScoreLabel = new JLabel("Total Score: 0 / 40");
        totalScoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalScoreLabel.setForeground(new Color(37, 99, 235)); // Electric Blue

        formPanel.add(new JLabel("Current Standing:"));
        formPanel.add(totalScoreLabel);

        voteCheckbox = new JCheckBox("Nominate for Best Presentation?");
        voteCheckbox.setFont(new Font("Segoe UI", Font.BOLD, 12));
        voteCheckbox.setForeground(new Color(220, 38, 38)); // Red color to stand out

        formPanel.add(new JLabel("Final Vote:"));
        formPanel.add(voteCheckbox);

        // --- Navigation & Submission ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton backBtn = new JButton("⬅ Back to List");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.addActionListener(e -> {
            // Re-opens the dashboard and closes this form
            new EvaluatorDashboard(currentEvaluatorId).setVisible(true);
            this.dispose();
        });

        buttonPanel.add(backBtn);
        buttonPanel.add(submitBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Replaces the old single button line

        KeyAdapter autoMath = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { 
                calculateTotal(); 
            }
        };

        problemClarityField.addKeyListener(autoMath);
        methodologyField.addKeyListener(autoMath);
        resultsField.addKeyListener(autoMath);
        presentationField.addKeyListener(autoMath);

        add(mainPanel);
        setVisible(true);
    }

    private void updateSelectedStudent(String id) {
        for (Student s : studentList) {
            if (s.getStudentID().equals(id)) {
                this.currentStudent = s;
                studentNameField.setText(s.getName());
                presentationTitleField.setText(s.getResearchTitle());
                presentationTypeField.setText(s.getPresentationType());
                
                // Enable button only if file exists
                viewPdfBtn.setEnabled(s.getFilePath() != null && !s.getFilePath().isEmpty());
                break;
            }
        }
    }

    // This method is called by the viewPdfBtn
private void openStudentPdf() {
    if (currentStudent == null || currentStudent.getFilePath() == null || currentStudent.getFilePath().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No file path associated with this student.");
        return;
    }

    try {
        File pdfFile = new File(currentStudent.getFilePath());
        
        // Strategy 1: The standard "open" command via CMD (More reliable on lab PCs)
        // This tells Windows: "Open this file with whatever you can find"
        new ProcessBuilder("cmd", "/c", "start", "", pdfFile.getAbsolutePath()).start();
        
    } catch (IOException ex) {
        // Strategy 2: Absolute Backup - Try to open specifically with Edge
        try {
            new ProcessBuilder("cmd", "/c", "start", "msedge", new File(currentStudent.getFilePath()).getAbsolutePath()).start();
        } catch (IOException e2) {
            JOptionPane.showMessageDialog(this, "Manual Step Required: Please open the 'uploads' folder and open the PDF manually.");
        }
    }
}
private void calculateTotal() {
    try {
        int p = getVal(problemClarityField);
        int m = getVal(methodologyField);
        int r = getVal(resultsField);
        int s = getVal(presentationField);
        
        int total = p + m + r + s;
        totalScoreLabel.setText("Total Score: " + total + " / 40");
        
        // Visual feedback: change color if it's a high score
        if (total >= 35) totalScoreLabel.setForeground(new Color(34, 197, 94)); // Green
        else totalScoreLabel.setForeground(new Color(37, 99, 235)); // Blue
        
    } catch (Exception e) {
        totalScoreLabel.setText("Total Score: -- / 40");
    }
}

// Helper to get numbers safely
private int getVal(JTextField field) {
    String text = field.getText().trim();
    if (text.isEmpty()) return 0;
    return Integer.parseInt(text);
}
    // private void openStudentPdf() {
    //     if (currentStudent == null || currentStudent.getFilePath() == null) return;

    //     try {
    //         // This looks for the file relative to your project folder
    //         File pdfFile = new File(currentStudent.getFilePath());
    //         if (pdfFile.exists()) {
    //             Desktop.getDesktop().open(pdfFile);
    //         } else {
    //             JOptionPane.showMessageDialog(this, "File not found at: " + pdfFile.getAbsolutePath());
    //         }
    //     } catch (IOException ex) {
    //         JOptionPane.showMessageDialog(this, "Error opening PDF: " + ex.getMessage());
    //     }
    // }

@Override
public void actionPerformed(ActionEvent e) {
    if (currentStudent == null) return;
    try {
        // 1. Parsing and validating rubrics
        int p = Integer.parseInt(problemClarityField.getText().trim());
        int m = Integer.parseInt(methodologyField.getText().trim());
        int r = Integer.parseInt(resultsField.getText().trim());
        int s = Integer.parseInt(presentationField.getText().trim());

        if (p < 0 || p > 10 || m < 0 || m > 10 || r < 0 || r > 10 || s < 0 || s > 10) {
            JOptionPane.showMessageDialog(this, "Scores must be 0-10.");
            return;
        }

        // 2. Logic: Compute total (Max 40)
        int totalMark = p + m + r + s;
        String comments = commentsArea.getText().replace("|", " ");

        // 3. Persistence: Save to evaluations.txt
        saveEvaluation(currentStudent.getStudentID(), totalMark, comments);

        // 4. Celebration Logic: Check for high marks (e.g., 35/40 or higher)
        if (totalMark >= 35) {
            JOptionPane.showMessageDialog(this, 
                "🎉 EXCELLENT PERFORMANCE! 🎉\n\n" + 
                "Student: " + currentStudent.getName() + "\n" +
                "Total Score: " + totalMark + "/40\n\n" +
                "This student is a top candidate for an award!", 
                "High Achievement", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Evaluation for " + currentStudent.getName() + " saved!");
            
            // --- AUTO-RETURN LOGIC ---
            new EvaluatorDashboard(currentEvaluatorId).setVisible(true);
            this.dispose(); 
        }
        
        clearInputs();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter numbers for marks.");
    }
}

    // Logic to handle saving evaluation securely 
    private void saveEvaluation(String id, int total, String comment) {
    // Sanitize input to prevent splitting issues in .txt file
String voteStatus = voteCheckbox.isSelected() ? "VOTED" : "NONE";    
try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("evaluations.txt", true)))) {        // Format: ID | Score | Sanitized Comment | Date [cite: 13, 14]
        out.println(id + "|" + total + "|" + comment.replace("|", " ") + "|" + new java.util.Date() + "|" + voteStatus);        JOptionPane.showMessageDialog(this, "Evaluation Submitted Successfully!");
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving evaluation.");
    }
}
    // private void saveEvaluation(String id, int total, String comment) {
    //     try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("evaluations.txt", true)))) {
    //         out.println(id + "|" + total + "|" + comment.replace("|", " ") + "|" + new Date());
    //     } catch (IOException e) { e.printStackTrace(); }
    // }

    private void clearInputs() {
        problemClarityField.setText("");
        methodologyField.setText("");
        resultsField.setText("");
        presentationField.setText("");
        commentsArea.setText("");
    }

    private JTextField createReadOnlyField() {
        JTextField field = new JTextField();
        field.setEditable(false);
        field.setBackground(new Color(245, 245, 245));
        return field;
    }

    private void addLabelAndComponent(JPanel panel, String label, Component comp) {
        panel.add(new JLabel(label));
        panel.add(comp);
    }

public static List<Student> loadStudents(String filename) {
    List<Student> list = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split("\\|");
            // Change from 6 to 4 (or whatever your minimum columns are)
            if (p.length >= 4) { 
                // Fill missing data with "N/A" if the file is short
                String id = p[0].trim();
                String name = p[1].trim();
                String title = p[2].trim();
                String type = p[3].trim();
                String email = (p.length > 4) ? p[4] : "N/A";
                String course = (p.length > 5) ? p[5] : "N/A";
                String file = (p.length > 6) ? p[6] : "";

                list.add(new Student(id, name, title, type, email, course, file));
            }
        }
    } catch (IOException e) { 
        System.out.println("Error reading file: " + e.getMessage()); 
    }
    return list;
}

}