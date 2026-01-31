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

    public EvaluatorController() {
        this.studentList = loadStudents("student.txt");

        if (studentList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No students found!");
            return;
        }

        setupUI();
        // Initialize with the first student
        updateSelectedStudent((String) studentIdPicker.getSelectedItem());
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
        mainPanel.add(submitBtn, BorderLayout.SOUTH);

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

    private void openStudentPdf() {
        if (currentStudent == null || currentStudent.getFilePath() == null) return;

        try {
            // This looks for the file relative to your project folder
            File pdfFile = new File(currentStudent.getFilePath());
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(this, "File not found at: " + pdfFile.getAbsolutePath());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error opening PDF: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int p = Integer.parseInt(problemClarityField.getText().trim());
            int m = Integer.parseInt(methodologyField.getText().trim());
            int r = Integer.parseInt(resultsField.getText().trim());
            int s = Integer.parseInt(presentationField.getText().trim());

            if (isInvalid(p) || isInvalid(m) || isInvalid(r) || isInvalid(s)) {
                JOptionPane.showMessageDialog(this, "Scores must be 0-10.");
                return;
            }

            saveEvaluation(currentStudent.getStudentID(), (p+m+r+s), commentsArea.getText());
            JOptionPane.showMessageDialog(this, "Evaluation saved for " + currentStudent.getName());
            clearInputs();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers.");
        }
    }

    private boolean isInvalid(int val) { return val < 0 || val > 10; }

    private void saveEvaluation(String id, int total, String comment) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("evaluations.txt", true)))) {
            out.println(id + "|" + total + "|" + comment.replace("|", " ") + "|" + new Date());
        } catch (IOException e) { e.printStackTrace(); }
    }

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
                if (p.length >= 6) {
                    list.add(new Student(p[0], p[1], p[2], p[3], p[4], p[5], p.length > 6 ? p[6] : null));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }
}