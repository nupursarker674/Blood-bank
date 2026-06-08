package blood;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class BloodApp extends JFrame {

    private JTextField user, txtId, txtName, txtAge, txtPhone, txtEmail, txtAddress, txtSearch;
    private JPasswordField pass;
    private JComboBox<String> cmbBlood, cmbGender;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;
    private JPanel loginPanel, mainPanel;

    // ডেটাবেজ কানেকশন ক্রেডেন্সিয়ালস (Database Name: bloodbank)
    private static final String URL = "jdbc:mysql://localhost:3306/bloodbank"; 
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public BloodApp() {
        setTitle("Blood Bank Management System");
        setSize(1150, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        showLogin();
        setVisible(true);
    }

   
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    void showLogin() {
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(186, 24, 27)); // Modern Crimson Red

       
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBounds(375, 120, 400, 420);
        card.setBorder(BorderFactory.createLineBorder(new Color(229, 229, 229), 1));

        JLabel title = new JLabel("BLOOD BANK LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(186, 24, 27));
        title.setBounds(50, 40, 300, 40);
        card.add(title);

        JLabel l1 = new JLabel("Username");
        l1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l1.setBounds(50, 120, 100, 25);
        card.add(l1);

        user = new JTextField();
        user.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        user.setBounds(50, 150, 300, 35);
        user.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        card.add(user);

        JLabel l2 = new JLabel("Password");
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l2.setBounds(50, 200, 100, 25);
        card.add(l2);

        pass = new JPasswordField();
        pass.setBounds(50, 230, 300, 35);
        pass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        card.add(pass);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBackground(new Color(186, 24, 27));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBounds(50, 310, 140, 40);
        card.add(loginBtn);

        JButton regBtn = new JButton("Register");
        regBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        regBtn.setBackground(new Color(102, 102, 102));
        regBtn.setForeground(Color.WHITE);
        regBtn.setFocusPainted(false);
        regBtn.setBounds(210, 310, 130, 40);
        card.add(regBtn);

        loginBtn.addActionListener(e -> {
            String username = user.getText();
            String password = String.valueOf(pass.getPassword());

            if (username.equals("admin") && password.equals("1234")) {
                remove(loginPanel);
                showMain();
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        regBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Registration feature coming soon!"));

        loginPanel.add(card);
        add(loginPanel);
    }

    void showMain() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(248, 249, 250));

        // Top Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(186, 24, 27));
        headerPanel.setBounds(0, 0, 1150, 60);
        mainPanel.add(headerPanel);

        JLabel heading = new JLabel("Blood Donor Management Dashboard");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(Color.WHITE);
        heading.setBounds(30, 12, 500, 35);
        headerPanel.add(heading);

        lblTotal = new JLabel("Total Donors : 0", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(Color.WHITE);
        lblTotal.setBounds(920, 15, 200, 30);
        headerPanel.add(lblTotal);

        // Input Form Panel (Left Side)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(20, 80, 320, 580);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        mainPanel.add(formPanel);

        int y = 20;
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Dimension fieldSize = new Dimension(280, 30);

        createFormLabel(formPanel, "Donor ID *", 20, y, labelFont);
        txtId = createSubTextField(formPanel, 20, y + 25, fieldSize);
        y += 65;

        createFormLabel(formPanel, "Full Name *", 20, y, labelFont);
        txtName = createSubTextField(formPanel, 20, y + 25, fieldSize);
        y += 65;

        createFormLabel(formPanel, "Age", 20, y, labelFont);
        txtAge = createSubTextField(formPanel, 20, y + 25, fieldSize);
        y += 65;

        createFormLabel(formPanel, "Gender", 20, y, labelFont);
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cmbGender.setBounds(20, y + 25, fieldSize.width, fieldSize.height);
        cmbGender.setBackground(Color.WHITE);
        formPanel.add(cmbGender);
        y += 65;

        createFormLabel(formPanel, "Blood Group", 20, y, labelFont);
        cmbBlood = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        cmbBlood.setBounds(20, y + 25, fieldSize.width, fieldSize.height);
        cmbBlood.setBackground(Color.WHITE);
        formPanel.add(cmbBlood);
        y += 65;

        createFormLabel(formPanel, "Phone", 20, y, labelFont);
        txtPhone = createSubTextField(formPanel, 20, y + 25, fieldSize);
        y += 65;

        createFormLabel(formPanel, "Email", 20, y, labelFont);
        txtEmail = createSubTextField(formPanel, 20, y + 25, fieldSize);
        y += 65;

        createFormLabel(formPanel, "Address", 20, y, labelFont);
        txtAddress = createSubTextField(formPanel, 20, y + 25, fieldSize);

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBounds(360, 80, 755, 110);
        controlPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        mainPanel.add(controlPanel);

        JButton addBtn = createStyledButton("Add Donor", 20, 15, 120, 35, new Color(40, 167, 69));
        JButton updateBtn = createStyledButton("Update", 150, 15, 110, 35, new Color(0, 123, 255));
        JButton deleteBtn = createStyledButton("Delete", 270, 15, 110, 35, new Color(220, 53, 69));
        JButton clearBtn = createStyledButton("Clear", 390, 15, 110, 35, new Color(108, 117, 125));
        JButton logoutBtn = createStyledButton("Logout", 625, 15, 110, 35, Color.BLACK);

        controlPanel.add(addBtn);
        controlPanel.add(updateBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(clearBtn);
        controlPanel.add(logoutBtn);

        JLabel searchLabel = new JLabel("Search Blood Group:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setBounds(20, 65, 140, 30);
        controlPanel.add(searchLabel);

        txtSearch = new JTextField();
        txtSearch.setBounds(160, 65, 200, 30);
        txtSearch.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        controlPanel.add(txtSearch);

        JButton searchBtn = createStyledButton("Search", 370, 65, 100, 30, new Color(23, 162, 184));
        controlPanel.add(searchBtn);

        // Table Customization
        model = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Gender", "Blood", "Phone", "Email", "Address"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(245, 198, 203));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 230, 230));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(186, 24, 27));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHeader.setPreferredSize(new Dimension(100, 35));

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(360, 210, 755, 450);
        sp.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        mainPanel.add(sp);

        // --- BUTTON ACTIONS ---

        addBtn.addActionListener(e -> {
            if (txtId.getText().trim().isEmpty() || txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Donor ID and Name are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String query = "INSERT INTO donors VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, txtId.getText().trim());
                pst.setString(2, txtName.getText().trim());
                pst.setString(3, txtAge.getText().trim());
                pst.setString(4, cmbGender.getSelectedItem().toString());
                pst.setString(5, cmbBlood.getSelectedItem().toString());
                pst.setString(6, txtPhone.getText().trim());
                pst.setString(7, txtEmail.getText().trim());
                pst.setString(8, txtAddress.getText().trim());

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Donor Added Successfully!");
                loadDatabaseData();
                clearForm();
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1062) {
                    JOptionPane.showMessageDialog(this, "Donor ID already exists!", "Database Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a donor from the table to update!");
                return;
            }
            String id = model.getValueAt(selectedRow, 0).toString();
            String query = "UPDATE donors SET name=?, age=?, gender=?, blood_group=?, phone=?, email=?, address=? WHERE id=?";
            try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, txtName.getText().trim());
                pst.setString(2, txtAge.getText().trim());
                pst.setString(3, cmbGender.getSelectedItem().toString());
                pst.setString(4, cmbBlood.getSelectedItem().toString());
                pst.setString(5, txtPhone.getText().trim());
                pst.setString(6, txtEmail.getText().trim());
                pst.setString(7, txtAddress.getText().trim());
                pst.setString(8, id);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Donor Updated Successfully!");
                loadDatabaseData();
                clearForm();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a donor from the table to delete!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this donor?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String id = model.getValueAt(selectedRow, 0).toString();
                String query = "DELETE FROM donors WHERE id=?";
                try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
                    pst.setString(1, id);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Donor Deleted!");
                    loadDatabaseData();
                    clearForm();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        searchBtn.addActionListener(e -> {
            String key = txtSearch.getText().trim();
            String query = "SELECT * FROM donors WHERE blood_group LIKE ?";
            try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, "%" + key + "%");
                ResultSet rs = pst.executeQuery();
                model.setRowCount(0);
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("id"), rs.getString("name"), rs.getString("age"),
                            rs.getString("gender"), rs.getString("blood_group"), rs.getString("phone"),
                            rs.getString("email"), rs.getString("address")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        clearBtn.addActionListener(e -> {
            clearForm();
            loadDatabaseData();
        });

        logoutBtn.addActionListener(e -> {
            remove(mainPanel);
            showLogin();
            user.setText("");
            pass.setText("");
            revalidate();
            repaint();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtAge.setText(model.getValueAt(row, 2).toString());
                cmbGender.setSelectedItem(model.getValueAt(row, 3));
                cmbBlood.setSelectedItem(model.getValueAt(row, 4));
                txtPhone.setText(model.getValueAt(row, 5).toString());
                txtEmail.setText(model.getValueAt(row, 6).toString());
                txtAddress.setText(model.getValueAt(row, 7).toString());
            }
        });

        add(mainPanel);
        loadDatabaseData(); 
    }

    private void loadDatabaseData() {
        model.setRowCount(0);
        String query = "SELECT * FROM donors";
        int totalCounter = 0;

        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("age"),
                        rs.getString("gender"),
                        rs.getString("blood_group"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                });
                totalCounter++;
            }
            lblTotal.setText("Total Donors : " + totalCounter);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createFormLabel(JPanel panel, String text, int x, int y, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setBounds(x, y, 150, 20);
        panel.add(label);
    }

    private JTextField createSubTextField(JPanel panel, int x, int y, Dimension size) {
        JTextField field = new JTextField();
        field.setBounds(x, y, size.width, size.height);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        panel.add(field);
        return field;
    }

    private JButton createStyledButton(String text, int x, int y, int width, int height, Color bg) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, width, height);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtSearch.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(BloodApp::new);
    }
}