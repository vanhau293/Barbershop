/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MAIN;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author NHAT
 */
public class THONGTIN extends javax.swing.JDialog {

    /**
     * Creates new form THONGTIN
     */
   
    private String maNV;
    
    Connection ketNoi = KNCSDL.layKetNoi();

    public THONGTIN(String maNV) {
        initComponents();
        this.setModal(true);
        this.maNV = maNV;
        txt_loiMK1.setVisible(false);
        txt_loiMK2.setVisible(false);
        txt_loiMK3.setVisible(false);
        load_ThongTin();
        txt_NameDMK.setText("Tài khoản: "+ layTenDN(maNV));
    }
    String layTenDN(String maNV){
        String sql = "select tendangnhap from TAIKHOAN WHERE manv='" + maNV + "'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            System.err.println("Error layMKC()");
        }
        return "DEMO";
    }
    void load_ThongTin(){
        String sql = "SELECT * FROM NHANVIEN WHERE MANV = '"+maNV+"'";
        try{
            PreparedStatement p = ketNoi.prepareStatement(sql);
            ResultSet m = p.executeQuery();
            while (m.next()) {
                lb_MaNV.setText("Mã NV: "+m.getString(1));
                lb_TenDangNhap.setText("Tên đăng nhập: "+ layTenDN(m.getString(1)));
                lb_Ten.setText("Tên: "+ m.getString(3));
                lb_GioiTinh.setText("Giới tính: "+ m.getString(5));
                lb_SoCMND.setText("Số CMND/CCCD: " + m.getString(2) );
                lb_SoDT.setText("Số điện thoại: " + m.getString(4));
                lb_DiaChi.setText("Địa chỉ: " + m.getString(6));
                lb_NgaySinh.setText("Ngày sinh: "+ chuyenNgay(m.getString(7)));
                
            }
            m.close();
            p.close();
        } catch(SQLException e){
            System.err.println("Loi load_ThongTin()");
        }
    }
    String ma_Hoa_MK(String s) {
        String sql = "SELECT CONVERT(VARCHAR(32), HashBytes('MD5','" + s + "' ), 2) as md5";

        try {
            PreparedStatement p = ketNoi.prepareStatement(sql);
            ResultSet m = p.executeQuery();
            while (m.next()) {
                s = m.getString(1);
            }
            m.close();
            p.close();
        } catch (SQLException e) {
            System.err.println("Loi ma hoa mk");
        }
        return s;
    }

    private String layMKC(String maNV) {
        String sql = "select matkhau from dbo.TAIKHOAN WHERE manv='" + maNV + "'";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            System.err.println("Error layMKC()");
        }
        return "DEMO";
    }

    boolean checkMK(String s) {
        String pattern = "^[a-zA-Z0-9+!@#$%^&*()_-]+$";
        if (!s.matches(pattern)) {
            return false;
        }
        return true;
    }

    private void capNhatMK() {
        if (String.valueOf(pw_MatKhauDMK0.getPassword()).equals("")
                || String.valueOf(pw_MatKhauDMK1.getPassword()).equals("") || String.valueOf(pw_MatKhauDMK2.getPassword()).equals("")
                || !checkMK((String.valueOf(pw_MatKhauDMK1.getPassword()))) || !checkMK(String.valueOf(pw_MatKhauDMK2.getPassword()))) {
            txt_loiMK1.setVisible(true);
            txt_loiMK2.setVisible(false);
            txt_loiMK3.setVisible(false);
            return;
        } else if (!layMKC(maNV).equals(ma_Hoa_MK(String.valueOf(pw_MatKhauDMK0.getPassword())))) {
            txt_loiMK1.setVisible(false);
            txt_loiMK3.setVisible(true);
            return;
        } else if (!String.valueOf(pw_MatKhauDMK1.getPassword()).equals(String.valueOf(pw_MatKhauDMK2.getPassword()))) {
            txt_loiMK2.setVisible(true);
            txt_loiMK1.setVisible(false);
            txt_loiMK3.setVisible(false);
            return;
        }
        txt_loiMK1.setVisible(false);
        txt_loiMK2.setVisible(false);
        txt_loiMK3.setVisible(false);
        Connection kn = KNCSDL.layKetNoi();
        String sql = "update [taikhoan] set matkhau=? where manv=?";
        try {
            PreparedStatement ps = kn.prepareStatement(sql);
            ps.setString(1, ma_Hoa_MK(String.valueOf(pw_MatKhauDMK1.getPassword())));
            ps.setString(2, maNV);
            ps.executeUpdate();
            ps.close();
            kn.close();
            JOptionPane.showMessageDialog(this, "Cập nhật mật khẩu thành công!!");
            pw_MatKhauDMK0.setText("");
            pw_MatKhauDMK1.setText("");
            pw_MatKhauDMK2.setText("");
        } catch (SQLException e) {
            System.out.println("ERROR capNhatMK()");
        }
        DoiMatKhau.dispose();
    }
    String chuyenNgay(String d) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        f1.setLenient(false);
        try {
            date = f1.parse(d);
            return f.format(date);

        } catch (ParseException ex) {
            Logger.getLogger(ADMIN.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private THONGTIN() {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DoiMatKhau = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        pw_MatKhauDMK1 = new javax.swing.JPasswordField();
        pw_MatKhauDMK2 = new javax.swing.JPasswordField();
        btn_LuuMK = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txt_NameDMK = new javax.swing.JLabel();
        txt_loiMK1 = new javax.swing.JLabel();
        txt_loiMK2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        pw_MatKhauDMK0 = new javax.swing.JPasswordField();
        txt_loiMK3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lb_Ten = new javax.swing.JLabel();
        lb_SoCMND = new javax.swing.JLabel();
        lb_GioiTinh = new javax.swing.JLabel();
        lb_SoDT = new javax.swing.JLabel();
        lb_DiaChi = new javax.swing.JLabel();
        lb_TenDangNhap = new javax.swing.JLabel();
        lb_NgaySinh = new javax.swing.JLabel();
        bt_Dong = new javax.swing.JButton();
        bt_DoiMatKhau = new javax.swing.JButton();
        lb_MaNV = new javax.swing.JLabel();

        DoiMatKhau.setMinimumSize(new java.awt.Dimension(500, 300));
        DoiMatKhau.setModal(true);

        jPanel5.setPreferredSize(new java.awt.Dimension(502, 400));

        btn_LuuMK.setText("Lưu Mật Khẩu");
        btn_LuuMK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_LuuMKMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("ĐỔI MẬT KHẨU");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel10.setText("Mật khẩu mới:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel13.setText("Nhập lại mật khẩu mới:");

        txt_NameDMK.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        txt_NameDMK.setForeground(new java.awt.Color(0, 0, 102));
        txt_NameDMK.setText("Tênnnnnnnnnnnnnnnn");

        txt_loiMK1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_loiMK1.setForeground(new java.awt.Color(255, 0, 0));
        txt_loiMK1.setText("Mật khẩu không hợp lệ!");

        txt_loiMK2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_loiMK2.setForeground(new java.awt.Color(255, 0, 0));
        txt_loiMK2.setText("Mật khẩu bạn đã nhập không trùng nhau!");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel11.setText("Mật khẩu hiện tại:");

        txt_loiMK3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_loiMK3.setForeground(new java.awt.Color(255, 0, 0));
        txt_loiMK3.setText("Mật khẩu hiện tại không đúng!");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_NameDMK, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pw_MatKhauDMK0, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                                    .addComponent(pw_MatKhauDMK1)
                                    .addComponent(pw_MatKhauDMK2)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(txt_loiMK1)))))
                        .addContainerGap(73, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(txt_loiMK3)
                                .addGap(84, 84, 84))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btn_LuuMK, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_loiMK2))
                                .addGap(50, 50, 50))))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txt_NameDMK)
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(pw_MatKhauDMK0, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(pw_MatKhauDMK1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(pw_MatKhauDMK2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_loiMK1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_loiMK2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_loiMK3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_LuuMK, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout DoiMatKhauLayout = new javax.swing.GroupLayout(DoiMatKhau.getContentPane());
        DoiMatKhau.getContentPane().setLayout(DoiMatKhauLayout);
        DoiMatKhauLayout.setHorizontalGroup(
            DoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        DoiMatKhauLayout.setVerticalGroup(
            DoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(500, 400));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("THÔNG TIN NHÂN VIÊN");

        lb_Ten.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lb_Ten.setText("Tên:");

        lb_SoCMND.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lb_SoCMND.setText("Số CMND/CCCD:");

        lb_GioiTinh.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lb_GioiTinh.setText("Giới tính:");

        lb_SoDT.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lb_SoDT.setText("Số điện thoại:");

        lb_DiaChi.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lb_DiaChi.setText("Địa chỉ:");

        lb_TenDangNhap.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lb_TenDangNhap.setText("Tên đăng nhập:");

        lb_NgaySinh.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lb_NgaySinh.setText("Ngày sinh:");

        bt_Dong.setText("Đóng");
        bt_Dong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_DongMouseClicked(evt);
            }
        });

        bt_DoiMatKhau.setText("Đổi Mật Khẩu");
        bt_DoiMatKhau.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_DoiMatKhauMouseClicked(evt);
            }
        });

        lb_MaNV.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lb_MaNV.setText("Mã NV:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(bt_DoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_TenDangNhap)
                            .addComponent(lb_Ten))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(192, 192, 192)
                                .addComponent(bt_Dong, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lb_MaNV)
                                    .addComponent(lb_GioiTinh))
                                .addGap(54, 54, 54))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb_SoDT))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb_SoCMND))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb_NgaySinh))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb_DiaChi))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(76, 89, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_TenDangNhap)
                        .addGap(18, 18, 18)
                        .addComponent(lb_Ten))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_MaNV)
                        .addGap(18, 18, 18)
                        .addComponent(lb_GioiTinh)))
                .addGap(18, 18, 18)
                .addComponent(lb_SoCMND)
                .addGap(18, 18, 18)
                .addComponent(lb_SoDT)
                .addGap(18, 18, 18)
                .addComponent(lb_NgaySinh)
                .addGap(18, 18, 18)
                .addComponent(lb_DiaChi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_DoiMatKhau)
                    .addComponent(bt_Dong))
                .addGap(58, 58, 58))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_DongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_DongMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_bt_DongMouseClicked

    private void bt_DoiMatKhauMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_DoiMatKhauMouseClicked
        // TODO add your handling code here:
        DoiMatKhau.setVisible(true);
    }//GEN-LAST:event_bt_DoiMatKhauMouseClicked

    private void btn_LuuMKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LuuMKMouseClicked
        // TODO add your handling code here:
        capNhatMK();

    }//GEN-LAST:event_btn_LuuMKMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(THONGTIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(THONGTIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(THONGTIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(THONGTIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new THONGTIN().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog DoiMatKhau;
    private javax.swing.JButton bt_DoiMatKhau;
    private javax.swing.JButton bt_Dong;
    private javax.swing.JButton btn_LuuMK;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lb_DiaChi;
    private javax.swing.JLabel lb_GioiTinh;
    private javax.swing.JLabel lb_MaNV;
    private javax.swing.JLabel lb_NgaySinh;
    private javax.swing.JLabel lb_SoCMND;
    private javax.swing.JLabel lb_SoDT;
    private javax.swing.JLabel lb_Ten;
    private javax.swing.JLabel lb_TenDangNhap;
    private javax.swing.JPasswordField pw_MatKhauDMK0;
    private javax.swing.JPasswordField pw_MatKhauDMK1;
    private javax.swing.JPasswordField pw_MatKhauDMK2;
    private javax.swing.JLabel txt_NameDMK;
    private javax.swing.JLabel txt_loiMK1;
    private javax.swing.JLabel txt_loiMK2;
    private javax.swing.JLabel txt_loiMK3;
    // End of variables declaration//GEN-END:variables
}
