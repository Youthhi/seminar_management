import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class EvaluatorUI extends JFrame {
    // Student Info (Read Only)
    protected JTextField idField = new JTextField();
    protected JTextField nameField = new JTextField();
    protected JTextField titleField = new JTextField();

    // Rubric Inputs
    protected JTextField clarityField = new JTextField();
    protected JTextField methodField = new JTextField();
    protected JTextField resultsField = new JTextField();
    protected JTextField skillsField = new JTextField();
    protected JTextArea commentsArea = new JTextArea(4, 20);

    protected JButton submitBtn = new JButton("Submit Evaluation");

    public EvaluatorUI() {
        setTitle("Evaluator Panel - Presentation Review");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Styling
        idField.setEditable(false);
        nameField.setEditable(false);
        titleField.setEditable(false);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);

        // Add components to the grid
        addComponent(mainPanel, "Student ID:", idField);
        addComponent(mainPanel, "Student Name:", nameField);
        addComponent(mainPanel, "Research Title:", titleField);
        
        mainPanel.add(new JSeparator());
        mainPanel.add(new JSeparator());

        addComponent(mainPanel, "Problem Clarity (0-10):", clarityField);
        addComponent(mainPanel, "Methodology (0-10):", methodField);
        addComponent(mainPanel, "Results (0-10):", resultsField);
        addComponent(mainPanel, "Presentation Skills (0-10):", skillsField);
        
        mainPanel.add(new JLabel("Comments:"));
        mainPanel.add(new JScrollPane(commentsArea));

        mainPanel.add(new JLabel("")); // Spacer
        mainPanel.add(submitBtn);

        add(mainPanel);
    }

    private void addComponent(JPanel panel, String label, Component comp) {
        panel.add(new JLabel(label));
        panel.add(comp);
    }
}