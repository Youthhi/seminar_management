import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame implements ActionListener{

    JButton loginButton = new JButton("Login");

    JCheckBox studentBox = new JCheckBox("Student");
    JCheckBox coordinatorBox = new JCheckBox("Coordinator");
    JCheckBox evaluatorBox = new JCheckBox("Evaluator");

    Login() {
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        loginButton.addActionListener(this);

        // ===== MAIN CENTER CONTAINER =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        // ===== TOP PANEL (LOGIN BUTTON) =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        topPanel.add(loginButton);

        // ===== BOTTOM PANEL (CHECKBOXES) =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        bottomPanel.add(studentBox);
        bottomPanel.add(coordinatorBox);
        bottomPanel.add(evaluatorBox);

        // spacing
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(bottomPanel);

        // ===== CENTER IT =====
        JPanel wrapper = new JPanel(new GridBagLayout()); // centers mainPanel
        wrapper.add(mainPanel);

        this.add(wrapper, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()==loginButton){
           if(studentBox.isSelected()){
            new Register();
            dispose();
           }
           else{

           }


        }
        else{
            System.out.println("Error occured due to button not existing");
        }
    }

}

