import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class StudentDashboard extends JFrame implements ActionListener {

    // ===== Colors & Fonts =====
    private final Color PRIMARY_COLOR = new Color(44, 62, 80);    // Dark Slate
    private final Color ACCENT_COLOR = new Color(52, 152, 219);   // Bright Blue
    private final Color BG_COLOR = new Color(236, 240, 241);      // Light Gray
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // ===== Student fields =====
    private String studentId;
    JTextField nameField = createStyledTextField();
    JTextField titleField = createStyledTextField();
    JTextArea abstractArea = createStyledTextArea();
    JTextField supervisorField = createStyledTextField();
    JTextField presentationField = createStyledTextField();

    JButton uploadBtn = new JButton("Upload Presentation");
    JLabel statusLabel = new JLabel("Status: No file uploaded yet.");

    public StudentDashboard() {
        setTitle("Student Research Portal");
        setSize(600, 550); // Slightly larger for better spacing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // ==========================
        // 1. Header Panel
        // ==========================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel appTitle = new JLabel("Student Dashboard");
        appTitle.setFont(HEADER_FONT);
        appTitle.setForeground(Color.WHITE);
        appTitle.setIcon(UIManager.getIcon("FileView.computerIcon")); // Built-in java icon
        headerPanel.add(appTitle, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // ==========================
        // 2. Main Form Panel (Center)
        // ==========================
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between items
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Row 1: Name ---
        addFormRow(mainPanel, gbc, 0, "Student Name:", nameField);

        // --- Row 2: Title ---
        addFormRow(mainPanel, gbc, 1, "Research Title:", titleField);

        // --- Row 3: Abstract ---
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel absLabel = new JLabel("Abstract:");
        absLabel.setFont(LABEL_FONT);
        absLabel.setForeground(PRIMARY_COLOR);
        mainPanel.add(absLabel, gbc);

        gbc.gridx = 1; 
        gbc.weightx = 1.0;
        JScrollPane abstractScroll = new JScrollPane(abstractArea);
        abstractScroll.setBorder(new LineBorder(new Color(200,200,200), 1, true));
        abstractScroll.setPreferredSize(new Dimension(300, 100));
        mainPanel.add(abstractScroll, gbc);

        // --- Row 4: Supervisor ---
        gbc.anchor = GridBagConstraints.CENTER; // Reset anchor
        addFormRow(mainPanel, gbc, 3, "Supervisor:", supervisorField);

        // --- Row 5: Type ---
        addFormRow(mainPanel, gbc, 4, "Presentation Type:", presentationField);

        add(mainPanel, BorderLayout.CENTER);

        // ==========================
        // 3. Action Panel (Bottom)
        // ==========================
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(BG_COLOR);
        footerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Style the button
        uploadBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        uploadBtn.setBackground(ACCENT_COLOR);
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setFocusPainted(false);
        uploadBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        uploadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadBtn.addActionListener(this);

        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(Color.DARK_GRAY);

        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(uploadBtn, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== Helper to create rows =====
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent input) {
        gbc.gridy = row;
        
        // Label
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(PRIMARY_COLOR);
        panel.add(label, gbc);

        // Input
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(input, gbc);
    }

    // ===== Styling Helpers =====
    private JTextField createStyledTextField() {
        JTextField tf = new JTextField(20);
        tf.setFont(INPUT_FONT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true), 
            new EmptyBorder(5, 8, 5, 8)
        ));
        tf.setEditable(false); // Assuming this is a dashboard to VIEW info, make false. Set true if editable.
        tf.setBackground(Color.WHITE);
        return tf;
    }

    private JTextArea createStyledTextArea() {
        JTextArea ta = new JTextArea();
        ta.setFont(INPUT_FONT);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        return ta;
    }

    // ===== Set student info =====
    public void setStudentInfo(String id, String name, String title, String abstractText,
                               String supervisor, String presentationType, String Filepath) {

        this.studentId = id;
        nameField.setText(name);
        titleField.setText(title);
        abstractArea.setText(abstractText);
        supervisorField.setText(supervisor);
        presentationField.setText(presentationType);

        if (Filepath != null && !Filepath.isEmpty()) {
            File f = new File(Filepath);
            if (f.exists()) {
                statusLabel.setText("<html>Status: <font color='green'>Uploaded (" + f.getName() + ")</font></html>");
                statusLabel.setIcon(UIManager.getIcon("FileView.fileIcon"));
            } else {
                statusLabel.setText("<html>Status: <font color='red'>File not found</font></html>");
            }
        } else {
            statusLabel.setText("Status: No file uploaded yet.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String type = presentationField.getText().trim().toLowerCase();
        
        if (e.getSource() == uploadBtn) {

            // Make text editable momentarily if you want to test without a database connection
            // type = "oral"; 

            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);

            if (type.equals("oral")) {
                chooser.addChoosableFileFilter(
                        new FileNameExtensionFilter("PowerPoint Files (.ppt, .pptx)", "ppt", "pptx")
                );
            } else if (type.equals("poster")) {
                chooser.addChoosableFileFilter(
                        new FileNameExtensionFilter("PDF Files (.pdf)", "pdf")
                );
            } else {
                JOptionPane.showMessageDialog(this, "Presentation Type must be set to 'Oral' or 'Poster' to upload.", "Configuration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {

                File selectedFile = chooser.getSelectedFile();
                String fileName = selectedFile.getName().toLowerCase();

                // backend validation
                if (type.equals("oral") && !(fileName.endsWith(".ppt") || fileName.endsWith(".pptx"))) {
                    JOptionPane.showMessageDialog(this, "Oral presentations must be PPT/PPTX", "Invalid Format", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (type.equals("poster") && !fileName.endsWith(".pdf")) {
                    JOptionPane.showMessageDialog(this, "Poster presentations must be PDF", "Invalid Format", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    File uploadDir = new File("uploads" + File.separator + studentId);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    File destFile = new File(uploadDir, selectedFile.getName());

                    java.nio.file.Files.copy(
                            selectedFile.toPath(),
                            destFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );

                    // Placeholder for controller logic
                     StudentController controller = new StudentController();
                     controller.updateStudentFile(studentId, destFile.getPath());

                    statusLabel.setText("<html>Status: <font color='green'>Uploaded (" + destFile.getName() + ")</font></html>");
                    JOptionPane.showMessageDialog(this, "File uploaded successfully!");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Upload failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    // Mock Controller for compilation if you don't have the external file
    class StudentController {
        public void updateStudentFile(String id, String path) {
            System.out.println("DB Update: ID=" + id + " Path=" + path);
        }
    }
}