import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Register extends JFrame implements ActionListener{

    // ===== Fields =====
    JTextField studentId = new JTextField(15);
    JTextField name = new JTextField(20);
    JTextField title = new JTextField(20);
    JTextField supervisor = new JTextField(20);

    JTextArea abstractArea = new JTextArea(4, 20);

    JRadioButton oralBtn = new JRadioButton("Oral");
    JRadioButton posterBtn = new JRadioButton("Poster");

    JButton registerBtn = new JButton("Register as Student");
    
    JButton proceedBtn = new JButton("Proceed to Dashboard");

    JCheckBox alreadyRegisteredBox = new JCheckBox("Already Registered Student");
    
   

    Register() {
        setTitle("Student Registration");
        setSize(620, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        registerBtn.addActionListener(this);
        proceedBtn.addActionListener(this);


        // ===== MAIN CARD =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        // ===== CHECKBOX =====
        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkPanel.setOpaque(false);
        checkPanel.add(alreadyRegisteredBox);

        // ===== STUDENT ID =====
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.add(new JLabel("Student ID:"));
        idPanel.add(studentId);

        // ===== NAME =====
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        namePanel.add(name);

        // ===== TITLE =====
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(new JLabel("Research Title:"));
        titlePanel.add(title);

        // ===== ABSTRACT =====
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        JScrollPane abstractScroll = new JScrollPane(abstractArea);
        abstractScroll.setPreferredSize(new Dimension(250, 80));

        JPanel abstractPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        abstractPanel.add(new JLabel("Abstract:"));
        abstractPanel.add(abstractScroll);

        // ===== SUPERVISOR =====
        JPanel supervisorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        supervisorPanel.add(new JLabel("Supervisor:"));
        supervisorPanel.add(supervisor);

        // ===== PRESENTATION TYPE =====
        ButtonGroup group = new ButtonGroup();
        group.add(oralBtn);
        group.add(posterBtn);
        oralBtn.setSelected(true);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Presentation Type:"));
        typePanel.add(oralBtn);
        typePanel.add(posterBtn);

        // ===== BUTTONS =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        proceedBtn.setEnabled(false);
        btnPanel.add(registerBtn);
        btnPanel.add(proceedBtn);

        // ===== ADD ALL =====
        mainPanel.add(checkPanel);
        mainPanel.add(idPanel);
        mainPanel.add(namePanel);
        mainPanel.add(titlePanel);
        mainPanel.add(abstractPanel);
        mainPanel.add(supervisorPanel);
        mainPanel.add(typePanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(btnPanel);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(mainPanel);
        add(wrapper, BorderLayout.CENTER);

       


        // ===== CHECKBOX BEHAVIOR =====
        alreadyRegisteredBox.addActionListener(e -> {
            boolean enableFullForm = !alreadyRegisteredBox.isSelected();
            setRegistrationFormEnabled(enableFullForm);
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== ENABLE / DISABLE REGISTRATION FIELDS =====
    private void setRegistrationFormEnabled(boolean enabled) {
        title.setEnabled(enabled);
        abstractArea.setEnabled(enabled);
        supervisor.setEnabled(enabled);
        oralBtn.setEnabled(enabled);
        posterBtn.setEnabled(enabled);

        registerBtn.setEnabled(enabled);
        proceedBtn.setEnabled(!enabled);
    }

    @Override
    public void actionPerformed(ActionEvent e){

        String id = studentId.getText().trim();
        String studentName = name.getText().trim();

        if(e.getSource()==registerBtn){
            if(id.isEmpty() || studentName.isEmpty()){

                JOptionPane.showMessageDialog(this,"StudentID and name are required");
                return;
            }

             boolean exists = false;
          try (BufferedReader br = new BufferedReader(new FileReader("student.txt"))) {
          String line;
          while ((line = br.readLine()) != null) {
            String[] data = line.split("\\|");
            if(data[0].equals(id)) {
                exists = true;
                break;
            }
        }
    }  catch(FileNotFoundException ex){
        System.out.println("Student.txt doesnt exist yet. creating student.txt...");
    }
       catch(IOException ex) {
        ex.printStackTrace();
    }

    if(exists){
        JOptionPane.showMessageDialog(this, "StudentID already registered!");
        return; // stop registration
    }

            String line = studentId.getText() + "|" +
                      name.getText() + "|" +
                      title.getText() + "|" +
                      abstractArea.getText() + "|" +
                      supervisor.getText() + "|" +
                      (oralBtn.isSelected() ? "Oral" : "Poster") + "|" +
                      ""; 
            
            try {
            
            File file = new File("student.txt");
            if (!file.exists()) {
                file.createNewFile(); 
            }

            
            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                out.println(line);
                JOptionPane.showMessageDialog(this, "Registration successful!");
                setRegistrationFormEnabled(false);
                proceedBtn.setEnabled(true);

            }

            } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data!");
        }

        
           StudentController controller = new StudentController();
           Student student = controller.getStudentInfo(studentId.getText());

        if (student != null) {
          StudentDashboard dashboard = new StudentDashboard();
          dashboard.setStudentInfo(
          student.getStudentID(),  
          student.getName(),
          student.getResearchTitle(),
          student.getAbstractText(),
          student.getSupervisor(),
          student.getPresentationType(),
          student.getFilePath()
    );
    dashboard.setVisible(true);
    JOptionPane.showMessageDialog(dashboard, "Redirected to StudentDashboard");

    
    this.dispose();
} else {
    JOptionPane.showMessageDialog(this, "Error directing to studentdashboard!");
}



        }

        else if(e.getSource()==proceedBtn){
          
            

            StudentController controller = new StudentController();
            Student student = controller.getStudentInfo(id);

            if (student == null) {
               JOptionPane.showMessageDialog(this, "Student not found!");
               return;
            }

            StudentDashboard dashboard = new StudentDashboard();
            dashboard.setStudentInfo(
            student.getStudentID(),    
            student.getName(),
            student.getResearchTitle(),
            student.getAbstractText(),
            student.getSupervisor(),
            student.getPresentationType(),
            student.getFilePath()
            );
            dashboard.setVisible(true);
            JOptionPane.showMessageDialog(dashboard,"Redirected to StudentDashboard");
            
            this.dispose();
    




            //J//OptionPane.showMessageDialog(this,"Redirecting to StudentDashboard");
            //dispose();
        }

        else{
            //nothing
        }

    }
}
