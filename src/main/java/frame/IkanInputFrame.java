package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class IkanInputFrame extends JFrame {
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JComboBox classCB;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public IkanInputFrame() {
        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            ComboBoxItem cls = (ComboBoxItem) classCB.getSelectedItem();
            int id_jenis = cls.getValue();

            if(nama.equals("")){
                JOptionPane.showMessageDialog(
                        null,
                        "Lengkapi Nama",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (this.id == 0) {
                    String cekSQL = "SELECT * FROM ikan WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Ikan Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO ikan SET nama = ?, id_jenis = ?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id_jenis);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM ikan WHERE nama=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "sepeda Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    String updateSQL = "UPDATE sepeda SET nama=?, id_jenis=? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id_jenis);
                    ps.setInt(3, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        classcb();
        init();
    }

    public void init() {
        setTitle("Input Data");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isiKomponen() {
        idTextField.setText(String.valueOf(this.id));

        String findSQL = "SELECT * FROM jenis WHERE id = ?";

        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                namaTextField.setText(rs.getString("nama"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void classcb() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM jenis_ikan ORDER by jenis";

        Statement s = null;
        try {
            s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            classCB.addItem(new ComboBoxItem(0, "Pilih Jenis"));
            while (rs.next()){
                classCB.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("jenis")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
