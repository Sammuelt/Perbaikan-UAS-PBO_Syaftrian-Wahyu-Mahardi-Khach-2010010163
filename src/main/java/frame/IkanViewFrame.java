package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class IkanViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public IkanViewFrame(){
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        hapusButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin?",
                    "Konfirmasi hapus data",
                    JOptionPane.YES_NO_OPTION
            );

            if (pilihan == 0 ){
                TableModel tm = viewTable.getModel();
                String idString = tm.getValueAt(barisTerpilih,0).toString();
                int id = Integer.parseInt(idString);

                String deleteSQL = "DELETE FROM ikan WHERE id = ?";
                Connection c = Koneksi.getConnection();
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        cariButton.addActionListener(e -> {
            String keyword = "%" + cariTextField.getText() + "%";
            String keyword1 = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT ikan.id, ikan.nama, jenis_ikan.jenis " +
                    "FROM ikan INNER JOIN jenis_ikan ON ikan.id_jenis = jenis_ikan.id " +
                    "WHERE jenis like ? OR nama like ? ";

            Connection c = Koneksi.getConnection();
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ps.setString(2, keyword1);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[3];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    row[2] = rs.getString("jenis");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        tambahButton.addActionListener(e->{
            IkanInputFrame InputFrame= new IkanInputFrame();
            InputFrame.setVisible(true);
        });
        ubahButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            TableModel tm = viewTable.getModel();
            String idString = tm.getValueAt(barisTerpilih,0).toString();
            int id = Integer.parseInt(idString);

            IkanInputFrame inputFrame = new IkanInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        isiTable();
        init();
    }

    public void init(){
        setTitle("Data Ikan");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isiTable(){
        String selectSQL = "SELECT ikan.id, ikan.nama, jenis_ikan.jenis " +
                "FROM ikan INNER JOIN jenis_ikan ON ikan.id_jenis = jenis_ikan.id ";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String header[] = {"Id","Nama ikan","Jenis"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
            viewTable.getColumnModel().getColumn(0).setMinWidth(32);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);

            Object[] row = new Object[3];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("jenis");
                dtm.addRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
