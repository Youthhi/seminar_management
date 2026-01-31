import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Register extends JFrame implements ActionListener {

    // ===== Fields =====
    JTextField studentId = new JTextField();
    JTextField name = new JTextField();
    JTextField title = new JTextField();
    JTextField supervisor = new JTextField();

    JTextArea abstractArea = new JTextArea(4, 20);

    JRadioButton oralBtn = new JRadioButton("Oral");
    JRadioButton posterBtn = new JRadioButton("Poster");

    JButton registerBtn = new JButton("Register as Student");
    JButton proceedBtn = new JButton("Proceed to Dashboard");

    JCheckBox alreadyRegisteredBox = new JCheckBox("Already Registered Student");

    Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
    Font titleFont = new Font("Segoe UI", Font.BOLD, 20);

    Register() {
        setTitle("Student Registration");
        setSize(780, 560);
        setMinimumSize(new Dimension(640, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        registerBtn.addActionListener(this);
        proceedBtn.addActionListener(this);

        // ===== BACKGROUND =====
        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(243, 246, 250));

        // ===== CARD =====
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // ===== TITLE =====
        JLabel heading = new JLabel("Student Registration");
        heading.setFont(titleFont);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(heading, gbc);

        // ===== CHECKBOX =====
        alreadyRegisteredBox.setFont(labelFont);

        gbc.gridy++;
        card.add(alreadyRegisteredBox, gbc);

        gbc.gridwidth = 1;

        // ===== FORM ROWS =====
        addRow(card, gbc, "Student ID:", studentId, 2);
        addRow(card, gbc, "Name:", name, 3);
        addRow(card, gbc, "Research Title:", title, 4);

        // ===== ABSTRACT =====
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        abstractArea.setFont(labelFont);

        JScrollPane abstractScroll = new JScrollPane(abstractArea);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.weightx = 0;
        card.add(createLabel("Abstract:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        card.add(abstractScroll, gbc);

        // ===== SUPERVISOR =====
        addRow(card, gbc, "Supervisor:", supervisor, 6);

        // ===== PRESENTATION TYPE =====
        ButtonGroup group = new ButtonGroup();
        group.add(oralBtn);
        group.add(posterBtn);
        oralBtn.setSelected(true);

        oralBtn.setFont(labelFont);
        posterBtn.setFont(labelFont);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        typePanel.setOpaque(false);
        typePanel.add(oralBtn);
        typePanel.add(posterBtn);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.weightx = 0;
        card.add(createLabel("Presentation Type:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        card.add(typePanel, gbc);

        // ===== BUTTONS =====
        registerBtn.setBackground(new Color(59, 130, 246));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);

        proceedBtn.setEnabled(false);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(registerBtn);
        btnPanel.add(proceedBtn);

        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(20, 8, 0, 8);
        card.add(btnPanel, gbc);

        background.add(card);
        add(background, BorderLayout.CENTER);

        // ===== CHECKBOX BEHAVIOR =====
        alreadyRegisteredBox.addActionListener(e -> {
            boolean enableFullForm = !alreadyRegisteredBox.isSelected();
            setRegistrationFormEnabled(enableFullForm);
        });

        setVisible(true);
    }

    // ===== ROW HELPER =====
    private void addRow(JPanel panel, GridBagConstraints gbc,
                        String labelText, JTextField field, int row) {

        field.setFont(labelFont);

        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(createLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(labelFont);
        return label;
    }

    // ===== ENABLE / DISABLE =====
    private void setRegistrationFormEnabled(boolean enabled) {
        title.setEnabled(enabled);
        abstractArea.setEnabled(enabled);
        supervisor.setEnabled(enabled);
        oralBtn.setEnabled(enabled);
        posterBtn.setEnabled(enabled);

        registerBtn.setEnabled(enabled);
        proceedBtn.setEnabled(!enabled);
    }

    // ===== LOGIC (UNCHANGED) =====
    @Override
    public void actionPerformed(ActionEvent e) {
        // your existing logic here
    }
}
