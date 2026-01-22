import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class StudentDashboard extends JFrame implements ActionListener{

    // ===== Student fields =====
    private String studentId;
    JTextField nameField = new JTextField(25);
    JTextField titleField = new JTextField(25);
    JTextArea abstractArea = new JTextArea(4, 25);
    JTextField supervisorField = new JTextField(25);
    JTextField presentationField = new JTextField(10);

    JButton uploadBtn = new JButton("Upload Presentation");
    JLabel statusLabel = new JLabel("No file uploaded yet.");

    public StudentDashboard() {
        setTitle("Student Dashboard");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Main card panel =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // ===== Name row =====
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        namePanel.setOpaque(false);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setPreferredSize(new Dimension(130, 25));
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        mainPanel.add(namePanel);

        // ===== Research Title row =====
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Research Title:");
        titleLabel.setPreferredSize(new Dimension(130, 25));
        titlePanel.add(titleLabel);
        titlePanel.add(titleField);
        mainPanel.add(titlePanel);

        // ===== Abstract row =====
        JPanel abstractPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        abstractPanel.setOpaque(false);
        JLabel abstractLabel = new JLabel("Abstract:");
        abstractLabel.setPreferredSize(new Dimension(130, 25));
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        JScrollPane abstractScroll = new JScrollPane(abstractArea);
        abstractScroll.setPreferredSize(new Dimension(300, 80));
        abstractPanel.add(abstractLabel);
        abstractPanel.add(abstractScroll);
        mainPanel.add(abstractPanel);

        // ===== Supervisor row =====
        JPanel supervisorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        supervisorPanel.setOpaque(false);
        JLabel supervisorLabel = new JLabel("Supervisor:");
        supervisorLabel.setPreferredSize(new Dimension(130, 25));
        supervisorPanel.add(supervisorLabel);
        supervisorPanel.add(supervisorField);
        mainPanel.add(supervisorPanel);

        // ===== Presentation Type row =====
        JPanel presentationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        presentationPanel.setOpaque(false);
        JLabel presentationLabel = new JLabel("Presentation Type:");
        presentationLabel.setPreferredSize(new Dimension(130, 25));
        presentationPanel.add(presentationLabel);
        presentationPanel.add(presentationField);
        mainPanel.add(presentationPanel);

        // ===== Vertical space =====
        mainPanel.add(Box.createVerticalStrut(20));

        // ===== Upload button row =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        uploadBtn.addActionListener(this);
        btnPanel.add(uploadBtn);
        mainPanel.add(btnPanel);

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(statusLabel);

        // ===== Center wrapper =====
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null); // optional, cleaner look
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== Set student info =====
   

    public void setStudentInfo(String id,String name, String title, String abstractText,
                               String supervisor, String presentationType,String Filepath) {
        
        this.studentId = id;                      
        nameField.setText(name);
        titleField.setText(title);
        abstractArea.setText(abstractText);
        supervisorField.setText(supervisor);
        presentationField.setText(presentationType);

        if (Filepath != null && !Filepath.isEmpty()) {
        File f = new File(Filepath);
        if (f.exists()) {
            statusLabel.setText("Uploaded: " + f.getName());
        } else {
            statusLabel.setText("File not found");
        }
    } else {
        statusLabel.setText("No file uploaded yet.");
    }
    }

  
  
    
 @Override
public void actionPerformed(ActionEvent e) {
    String type = presentationField.getText().trim().toLowerCase();
    if (e.getSource() == uploadBtn) {

        
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);

         if (type.equals("oral")) {
        chooser.addChoosableFileFilter(
            new FileNameExtensionFilter("PowerPoint Files", "ppt", "pptx")
        );
       } 
        else if (type.equals("poster")) {
        chooser.addChoosableFileFilter(
            new FileNameExtensionFilter("PDF Files", "pdf")
        );
       } 
       else {
        JOptionPane.showMessageDialog(this, "Invalid presentation type");
        return;
        }

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION){

        File selectedFile = chooser.getSelectedFile();
        String fileName = selectedFile.getName().toLowerCase();

    // backend validation
     if (type.equals("oral") && !(fileName.endsWith(".ppt") || fileName.endsWith(".pptx"))) {
        JOptionPane.showMessageDialog(this, "Oral presentations must be PPT/PPTX");
        return;
    }

    if (type.equals("poster") && !fileName.endsWith(".pdf")) {
        JOptionPane.showMessageDialog(this, "Poster presentations must be PDF");
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

                // tell controller to update the file path for this student
                StudentController controller = new StudentController();
                controller.updateStudentFile(studentId, destFile.getPath());

                statusLabel.setText("Uploaded: " + destFile.getName());
                JOptionPane.showMessageDialog(this, "File uploaded successfully!");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Upload failed!");
            }
        }
        else{
            System.out.println("Better luck next time....coding issue");
        }
    }
}
  

}

