import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame implements ActionListener {
    // --- COLOR PALETTE ---
    Color bgSoft = new Color(240, 244, 248);     // Soft light blue-gray
    Color primaryBlue = new Color(37, 99, 235);   // Electric Blue
    Color textDark = new Color(30, 41, 59);      // Slate Dark

    JButton loginButton = new JButton("Login");
    JRadioButton studentBox = new JRadioButton("Student");
    JRadioButton coordinatorBox = new JRadioButton("Coordinator");
    JRadioButton evaluatorBox = new JRadioButton("Evaluator");
    Login() {
        this.setTitle("System Login");
        this.setSize(450, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(bgSoft);
        this.setLayout(new GridBagLayout()); // Perfectly centers the card

        // --- MAIN WHITE CARD ---
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(320, 380));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(30, 30, 30, 30)
        ));

        // Header
        JLabel title = new JLabel("Welcome");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(textDark);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitle = new JLabel("Select your role");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(studentBox);
        roleGroup.add(coordinatorBox);
        roleGroup.add(evaluatorBox);

        studentBox.setSelected(true);

        // Style the Checkboxes
        styleRoleBox(studentBox);
        styleRoleBox(coordinatorBox);
        styleRoleBox(evaluatorBox);

        // Style the Button
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(primaryBlue);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setMaximumSize(new Dimension(200, 45));
        loginButton.addActionListener(this);

        // Add to card
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));
        card.add(studentBox);
        card.add(coordinatorBox);
        card.add(evaluatorBox);
        card.add(Box.createVerticalGlue());
        card.add(loginButton);

        this.add(card);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

private void styleRoleBox(JRadioButton rb) {
        rb.setBackground(Color.WHITE);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rb.setFocusPainted(false);
        rb.setAlignmentX(Component.CENTER_ALIGNMENT);
        rb.setBorder(new EmptyBorder(5, 0, 5, 0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
        if (coordinatorBox.isSelected()) {
            new CoordinatorDashboard().setVisible(true);
            this.dispose();
        } 
        else if (evaluatorBox.isSelected()) {
            // 1. Ask for the Evaluator's ID (to satisfy Hemaraj's requirement)
            String evalId = JOptionPane.showInputDialog(this, "Enter Evaluator ID (e.g., E001):");
            
            if (evalId != null && !evalId.trim().isEmpty()) {
                // 2. Open the Dashboard FIRST (to see the list of assigned students)
                // This matches the logic we discussed earlier!
                new EvaluatorDashboard(evalId.trim()).setVisible(true);
                this.dispose();
            }
        } 
        else if (studentBox.isSelected()) {
            new Register().setVisible(true);
            this.dispose();
        }
    }
}
    // @Override
    // public void actionPerformed(ActionEvent e) {
    //     if (e.getSource() == loginButton) {
    //         if (studentBox.isSelected()) {
    //             new Register(); // Open Register Screen
    //             dispose();      // Close Login Screen
    //         } else {
    //             JOptionPane.showMessageDialog(this, "Please select 'Student' to Register.");
    //         }
    //     }
    // }
}