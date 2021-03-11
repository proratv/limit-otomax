/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emma.program;

import lib.DatabaseConnection;
import lib.DefaultContextMenu;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Tegar
 */
public final class LimitAddOn extends javax.swing.JFrame {

    int maxKodeLimit;
    int totalRow;
    String grupColumn;
    String resellerColumn;
    String produkColumn;
    String masaLimitColumn;
    int jumlahLimitColumn;
    int transaksiProcessColumn;
    int masaTransaksiColumn;
    int totalTransaksiColumn;
    String errorMessageColumn;
    String startDate;
    String endDate;
    String statusColumn;
    boolean isStatusColumn;
    String flagUpdateSave;
    int selectedRow;

    /**
     * Creates new form NewJFrame
     *
     * @throws java.lang.Exception
     */
    public LimitAddOn() throws Exception {
        initComponents();
        getContextMenu();
        getImageIcon();
        countLimitData();
        defaultSetVariable();
        configLimitTable();
        showLimitData();
        comboFilter();
        datePick();
        AutoCompleteDecorator.decorate(cmbByGroup);
        AutoCompleteDecorator.decorate(cmbByReseller);
        AutoCompleteDecorator.decorate(cmbProduk);
        byGroupList();
        byResellerList();
        byProdukList();
        captureSelectedRow();
    }

    private void getContextMenu() {
        DefaultContextMenu DefaultContextMenu = new DefaultContextMenu();
        DefaultContextMenu.addDefaultContextMenu(txtSearchFilter);
    }

    private void getImageIcon() {
        String iconPath = "images\\limit_dashboard.png";
        ImageIcon img = new ImageIcon(iconPath);
        setIconImage(img.getImage());
    }

    private void byGroupList() throws Exception {
        try {
            String query;
            query = "select distinct kode, nama from level";  //query notice
            Connection conn = (Connection) DatabaseConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                //grupColumn = res.getString("kode");
                cmbByGroup.addItem(res.getString("kode").trim());
                //cmbByGroup.addItem(res.getString("nama").trim());
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    }

    private void byResellerList() throws Exception {
        try {
            String query;
            query = "select distinct kode, nama from reseller"; //query notice
            Connection conn = (Connection) DatabaseConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                cmbByReseller.addItem(res.getString("kode").trim());
                //cmbByReseller.addItem(res.getString("nama").trim());
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    }

    private void byProdukList() throws Exception {
        try {
            String query;
            query = "select distinct kode, nama from produk"; //query notice
            Connection conn = (Connection) DatabaseConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                cmbProduk.addItem(res.getString("kode").trim());
                //cmbProduk.addItem(res.getString("nama").trim());
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    }

    private void defaultSelectedRow() {
        selectedRow = -1;

        if (selectedRow == -1) {
            btnEditData.setEnabled(false);
            btnEditData.setEnabled(false);
            btnEditData.setToolTipText("Tidak Ada Data yang Bisa di Edit");
        } else {
            btnEditData.setEnabled(true);
            btnEditData.setToolTipText("Edit Data Limit Transaksi");
        }
    }

    private void captureSelectedRow() {
        ListSelectionModel model = tblEmmaLimitTransaction.getSelectionModel();
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if (!model.isSelectionEmpty()) {
                selectedRow = model.getMinSelectionIndex();

                System.out.println(selectedRow);

                if (selectedRow == -1) {
                    btnEditData.setEnabled(false);
                    btnEditData.setToolTipText("Tidak Ada Data yang Bisa di Edit");
                } else {
                    btnEditData.setEnabled(true);
                    btnEditData.setToolTipText("Edit Data Limit Transaksi");
                }
            }
        });
    }

    private void defaultSetVariable() throws Exception {
        cmbByGroup.setSelectedItem("");
        cmbByReseller.setSelectedItem("");
        cmbProduk.setSelectedItem("");
        cmbTransactionPeriod.setSelectedItem("");
        txtTransactionLimit.setText("0");
        txaErrorMessage.setText("");
        maxKodeLimit = 0;
        grupColumn = null;
        resellerColumn = null;
        produkColumn = null;
        masaLimitColumn = null;
        jumlahLimitColumn = 0;
        transaksiProcessColumn = 0;
        masaTransaksiColumn = 0;
        totalTransaksiColumn = 0;
        startDate = null;
        endDate = null;
        statusColumn = null;
        isStatusColumn = true;
        flagUpdateSave = null;
        dtpStartDateEvent.clear();
        dtpEndDateEvent.clear();
        selectedRow = -1;
        cmbStatus.setSelectedItem("Aktif");
        cmbStatus.setEnabled(false);
        cmbStatusFilter.setSelectedItem("Semua Status");
        cmbSearchByFilter.setSelectedItem("Semua Data");
        txtSearchFilter.setText("");

        configComponent();
    }

    private void configComponent() throws Exception {
        txtSearchFilter.requestFocus();
        scpErrorMessage.setVisible(false);
        lblSearchByFilter.setVisible(false);
        cmbSearchByFilter.setVisible(false);
        lblErrorMessage.setVisible(false);
        btnGenerate.setVisible(false);
        lblByGroup.setVisible(false);
        cmbByGroup.setVisible(false);
        cmbByGroup.setEnabled(true);
        cmbByReseller.setEnabled(true);
        cmbProduk.setEnabled(true);
        cmbTransactionPeriod.setEnabled(true);
        txtTransactionLimit.setEnabled(true);
        dtpStartDateEvent.setEnabled(true);
        dtpEndDateEvent.setEnabled(true);

        TitledBorder title = BorderFactory.createTitledBorder("Tambah Daftar Limit");
        pnlAddLimitList.setBorder(title);

        ListSelectionModel model = tblEmmaLimitTransaction.getSelectionModel();
        selectedRow = model.getMinSelectionIndex();

        countLimitData();
        if (totalRow == 0 || selectedRow == -1) {
            btnEditData.setEnabled(false);
            btnEditData.setToolTipText("Tidak Ada Data yang Bisa di Edit");
        } else {
            btnEditData.setEnabled(true);
            btnEditData.setToolTipText("Edit Data Limit Transaksi");
        }
    }

    private void datePick() {

        DatePickerSettings dateStartSetting;
        DatePickerSettings dateEndSetting;

        dateStartSetting = dtpStartDateEvent.getSettings();
        dateStartSetting.setFormatForDatesCommonEra("dd-MMM-yyyy");
        //dtpStartDateEvent.setDateToToday();

        dateEndSetting = dtpEndDateEvent.getSettings();
        dateEndSetting.setFormatForDatesCommonEra("dd-MMM-yyyy");
        //dtpEndDateEvent.setDateToToday();

        ImageIcon icon = new ImageIcon("images\\calendar_icon.png");

        JButton datePickerButtonStart = dtpStartDateEvent.getComponentToggleCalendarButton();
        datePickerButtonStart.setIcon(icon);
        datePickerButtonStart.setText("");

        JButton datePickerButtonEnd = dtpEndDateEvent.getComponentToggleCalendarButton();
        datePickerButtonEnd.setIcon(icon);
        datePickerButtonEnd.setText("");
    }

    private void countLimitData() throws Exception {
        try {
            String query;
            query = "select count(0) as total_row from emma_limit_transaksi";
            Connection conn = (Connection) DatabaseConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                totalRow = res.getInt("total_row");
            }

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    }

    private void countIndexData() throws Exception {
        grupColumn = cmbByGroup.getSelectedItem().toString();
        resellerColumn = cmbByReseller.getSelectedItem().toString();
        produkColumn = cmbProduk.getSelectedItem().toString();

        try {
            String query;
            query = "select count(0) as total_row from emma_limit_transaksi where grup = '" + grupColumn + "' and reseller = '" + resellerColumn + "' and produk = '" + produkColumn + "'";
            Connection conn = (Connection) DatabaseConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                totalRow = res.getInt("total_row");
            }

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    }

    private void showLimitData() throws Exception {
        try {
            String query;
            query = "select * from emma_limit_transaksi order by kode_limit desc";
            Connection conn = (Connection) DatabaseConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) tblEmmaLimitTransaction.getModel();
            model.setRowCount(0);

            while (res.next()) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                Date sqlDate1 = (Date) dateFormatter.parse(res.getString("tgl_mulai_event"));
                Date sqlDate2 = (Date) dateFormatter.parse(res.getString("tgl_akhir_event"));

                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                startDate = dateFormat.format(sqlDate1);
                endDate = dateFormat.format(sqlDate2);

                isStatusColumn = res.getBoolean("status");
                if (isStatusColumn == true) {
                    statusColumn = "Aktif";
                } else {
                    statusColumn = "Non Aktif";
                }
                model.addRow(new Object[]{res.getString("kode_limit"), res.getString("grup"), res.getString("reseller"), res.getString("produk"), res.getString("jumlah_limit"), res.getString("masa_limit"), res.getString("transaksi_dalam_masa"), res.getString("transaksi_total"), statusColumn, startDate, endDate});
            }
        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    }

    private void configLimitTable() throws Exception {

        TableCellRenderer rendererFromHeader = tblEmmaLimitTransaction.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = tblEmmaLimitTransaction.getColumnModel();
        columnModel.getColumn(2).setPreferredWidth(75);
        columnModel.getColumn(3).setPreferredWidth(125);
        columnModel.getColumn(4).setPreferredWidth(75);
        columnModel.getColumn(5).setPreferredWidth(150);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(7).setPreferredWidth(75);
        columnModel.getColumn(8).setPreferredWidth(100);
        columnModel.getColumn(9).setPreferredWidth(125);
        columnModel.getColumn(10).setPreferredWidth(125);

        columnModel.getColumn(0).setMinWidth(0);
        columnModel.getColumn(0).setMaxWidth(0);
        columnModel.getColumn(1).setMinWidth(0);
        columnModel.getColumn(1).setMaxWidth(0);

    }

    private void getEditData() throws Exception {
        DefaultTableModel model = (DefaultTableModel) tblEmmaLimitTransaction.getModel();
        int SelectedRowIndex = tblEmmaLimitTransaction.getSelectedRow();

        try {
            String query;
            query = "select count(0) as total_row from emma_limit_transaksi where kode_limit = " + model.getValueAt(SelectedRowIndex, 0);
            Connection conn = (Connection) DatabaseConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                totalRow = res.getInt("total_row");
            }

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }

        cmbByGroup.setSelectedItem(model.getValueAt(SelectedRowIndex, 1));
        cmbByReseller.setSelectedItem(model.getValueAt(SelectedRowIndex, 2));
        cmbProduk.setSelectedItem(model.getValueAt(SelectedRowIndex, 3));
        txtTransactionLimit.setText(model.getValueAt(SelectedRowIndex, 4).toString());
        cmbTransactionPeriod.setSelectedItem(model.getValueAt(SelectedRowIndex, 5));
        cmbStatus.setSelectedItem(model.getValueAt(SelectedRowIndex, 8));
        dtpStartDateEvent.setText(model.getValueAt(SelectedRowIndex, 9).toString());
        dtpEndDateEvent.setText(model.getValueAt(SelectedRowIndex, 10).toString());
    }

    private void getMaxKodeLimit() throws Exception {
        try {
            String query;
            query = "select max(kode_limit) as max_kode_limit from emma_limit_transaksi";
            Connection conn = (Connection) DatabaseConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                maxKodeLimit = res.getInt("max_kode_limit");
            }

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    }

    private void comboFilter() {
        if (cmbStatusFilter.getSelectedItem() == "Semua Status") {
            statusColumn = "status";
        } else if (cmbStatusFilter.getSelectedItem() == "Aktif") {
            statusColumn = "1";
        } else if (cmbStatusFilter.getSelectedItem() == "Non Aktif") {
            statusColumn = "0";
        } else {
            statusColumn = "";
        }

        if (cmbSearchByFilter.getSelectedItem() == "Semua Data") {
            grupColumn = "grup";
            resellerColumn = "reseller";
        } else if (cmbSearchByFilter.getSelectedItem() == "Grup") {
            grupColumn = "grup";
            resellerColumn = "null";
        } else if (cmbSearchByFilter.getSelectedItem() == "Reseller") {
            grupColumn = "null";
            resellerColumn = "reseller";
        } else {
            grupColumn = "null";
            resellerColumn = "null";
        }

        try {
            String query;
            query = "select * from emma_limit_transaksi where status = " + statusColumn + " and grup = CASE WHEN " + grupColumn + " = grup THEN grup ELSE '' END and reseller = CASE WHEN " + resellerColumn + " = reseller THEN reseller ELSE '' END order by kode_limit desc";
            Connection conn = (Connection) DatabaseConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) tblEmmaLimitTransaction.getModel();
            model.setRowCount(0);

            while (res.next()) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                Date sqlDate1 = (Date) dateFormatter.parse(res.getString("tgl_mulai_event"));
                Date sqlDate2 = (Date) dateFormatter.parse(res.getString("tgl_akhir_event"));

                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                startDate = dateFormat.format(sqlDate1);
                endDate = dateFormat.format(sqlDate2);

                isStatusColumn = res.getBoolean("status");
                if (isStatusColumn == true) {
                    statusColumn = "Aktif";
                } else {
                    statusColumn = "Non Aktif";
                }
                model.addRow(new Object[]{res.getString("kode_limit"), res.getString("grup"), res.getString("reseller"), res.getString("produk"), res.getString("jumlah_limit"), res.getString("masa_limit"), res.getString("transaksi_dalam_masa"), res.getString("transaksi_total"), statusColumn, startDate, endDate});
            }
        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void searchTableFilter() {
        DefaultTableModel table = (DefaultTableModel) tblEmmaLimitTransaction.getModel();
        String search;
        search = txtSearchFilter.getText();
        TableRowSorter<DefaultTableModel> tr;
        tr = new TableRowSorter<>(table);
        tblEmmaLimitTransaction.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + search, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    private void errorLimitMessage() {
        errorMessageColumn = "Reseller " + cmbByReseller.getSelectedItem().toString() + " dengan produk " + cmbProduk.getSelectedItem().toString() + " telah mencapai limit " + cmbTransactionPeriod.getSelectedItem().toString() + " sebanyak " + txtTransactionLimit.getText();
        txaErrorMessage.setText(errorMessageColumn);
    }

    private void saveData() {
        DefaultTableModel model;
        model = (DefaultTableModel) tblEmmaLimitTransaction.getModel();
        try {
            String query;
            query = "INSERT INTO emma_limit_transaksi (kode_limit, grup, reseller, produk, masa_limit, jumlah_limit, transaksi_dalam_masa, transaksi_total, status, pesan_gagal, tgl_mulai_event, tgl_akhir_event, date_create, date_update) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Connection conn = (Connection) DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            getMaxKodeLimit();
            maxKodeLimit += 1;
            grupColumn = cmbByGroup.getSelectedItem().toString();
            resellerColumn = cmbByReseller.getSelectedItem().toString();
            produkColumn = cmbProduk.getSelectedItem().toString();
            masaLimitColumn = cmbTransactionPeriod.getSelectedItem().toString();
            jumlahLimitColumn = Integer.parseInt(txtTransactionLimit.getText());
            masaTransaksiColumn = 0;
            totalTransaksiColumn = 0;
            errorLimitMessage();
            startDate = dtpStartDateEvent.getText();
            endDate = dtpEndDateEvent.getText();
            isStatusColumn = true;

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

            Date sqlDate1 = (Date) dateFormatter.parse(startDate);
            Date sqlDate2 = (Date) dateFormatter.parse(endDate);

            java.sql.Date startEventDate = new java.sql.Date(sqlDate1.getTime());
            java.sql.Date endEventDate = new java.sql.Date(sqlDate2.getTime());

            pst.setInt(1, maxKodeLimit);
            pst.setString(2, grupColumn);
            pst.setString(3, resellerColumn);
            pst.setString(4, produkColumn);
            pst.setString(5, masaLimitColumn);
            pst.setInt(6, jumlahLimitColumn);
            pst.setInt(7, masaTransaksiColumn);
            pst.setInt(8, totalTransaksiColumn);
            pst.setBoolean(9, isStatusColumn);
            pst.setString(10, errorMessageColumn);
            pst.setDate(11, startEventDate);
            pst.setDate(12, endEventDate);
            pst.setDate(13, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setDate(14, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data telah berhasil di simpan");
            showLimitData();
            defaultSetVariable();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateData() throws Exception {
        try {
            String query;
            query = "update emma_limit_transaksi set masa_limit = ?, jumlah_limit = ?, status=?, tgl_mulai_event = ?, tgl_akhir_event = ?, date_update = ? where grup = ? and reseller = ? and produk = ?";
            Connection conn = (Connection) DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            masaLimitColumn = cmbTransactionPeriod.getSelectedItem().toString();
            jumlahLimitColumn = Integer.parseInt(txtTransactionLimit.getText());
            isStatusColumn = cmbStatus.getSelectedItem().toString() == "Aktif";
            startDate = dtpStartDateEvent.getText();
            endDate = dtpEndDateEvent.getText();
            grupColumn = cmbByGroup.getSelectedItem().toString();
            resellerColumn = cmbByReseller.getSelectedItem().toString();
            produkColumn = cmbProduk.getSelectedItem().toString();

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMMMM-yyyy");

            Date sqlDate1 = (Date) dateFormatter.parse(startDate);
            Date sqlDate2 = (Date) dateFormatter.parse(endDate);

            java.sql.Date startEventDate = new java.sql.Date(sqlDate1.getTime());
            java.sql.Date endEventDate = new java.sql.Date(sqlDate2.getTime());

            pst.setString(1, masaLimitColumn);
            pst.setInt(2, jumlahLimitColumn);
            pst.setBoolean(3, isStatusColumn);
            pst.setDate(4, startEventDate);
            pst.setDate(5, endEventDate);
            pst.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setString(7, grupColumn);
            pst.setString(8, resellerColumn);
            pst.setString(9, produkColumn);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data telah berhasil di ubah");
            showLimitData();
            defaultSetVariable();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tbdProgram = new javax.swing.JTabbedPane();
        pnlMonitoring = new javax.swing.JPanel();
        lblStatusFilter = new javax.swing.JLabel();
        lblSearchByFilter = new javax.swing.JLabel();
        cmbStatusFilter = new javax.swing.JComboBox<>();
        cmbSearchByFilter = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEmmaLimitTransaction = new javax.swing.JTable();
        txtSearchFilter = new javax.swing.JTextField();
        btnEditData = new javax.swing.JButton();
        btnRefreshData = new javax.swing.JButton();
        pnlAddLimitList = new javax.swing.JPanel();
        lblByReseller = new javax.swing.JLabel();
        lblTransactionPeriod = new javax.swing.JLabel();
        lblProduk = new javax.swing.JLabel();
        lblErrorMessage = new javax.swing.JLabel();
        lblTransactionLimit = new javax.swing.JLabel();
        cmbByGroup = new javax.swing.JComboBox<>();
        cmbByReseller = new javax.swing.JComboBox<>();
        cmbProduk = new javax.swing.JComboBox<>();
        cmbTransactionPeriod = new javax.swing.JComboBox<>();
        lblCount = new javax.swing.JLabel();
        scpErrorMessage = new javax.swing.JScrollPane();
        txaErrorMessage = new javax.swing.JTextArea();
        btnClearData = new javax.swing.JButton();
        btnGenerate = new javax.swing.JButton();
        btnUpdateSave = new javax.swing.JButton();
        lblByGroup = new javax.swing.JLabel();
        txtTransactionLimit = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        lblStartEvent = new javax.swing.JLabel();
        lblEndEvent = new javax.swing.JLabel();
        dtpStartDateEvent = new com.github.lgooddatepicker.components.DatePicker();
        dtpEndDateEvent = new com.github.lgooddatepicker.components.DatePicker();
        lblStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        lblTitleHeader = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Limit Transaction v1.0.0");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(new java.awt.Color(153, 255, 255));
        setResizable(false);
        setSize(new java.awt.Dimension(0, 0));

        tbdProgram.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tbdProgram.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        pnlMonitoring.setBackground(new java.awt.Color(51, 204, 255));
        pnlMonitoring.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblStatusFilter.setText("Status");
        lblStatusFilter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        lblSearchByFilter.setText("Cari Berdasarkan");
        lblSearchByFilter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        cmbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Status", "Aktif", "Non Aktif" }));
        cmbStatusFilter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbStatusFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStatusFilterActionPerformed(evt);
            }
        });

        cmbSearchByFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Data", "Grup", "Reseller" }));
        cmbSearchByFilter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbSearchByFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchByFilterActionPerformed(evt);
            }
        });

        tblEmmaLimitTransaction.setBackground(new java.awt.Color(153, 255, 255));
        tblEmmaLimitTransaction.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        tblEmmaLimitTransaction.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblEmmaLimitTransaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Limit", "Group", "Reseller", "Produk", "Limit Trx", "Limit Masa Berlaku", "Transaksi", "Total Trx", "Status", "Tgl Mulai Event", "Tgl Akhir Event"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmmaLimitTransaction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblEmmaLimitTransaction.setGridColor(new java.awt.Color(51, 51, 255));
        tblEmmaLimitTransaction.setRowHeight(20);
        tblEmmaLimitTransaction.setShowGrid(true);
        jScrollPane2.setViewportView(tblEmmaLimitTransaction);
        if (tblEmmaLimitTransaction.getColumnModel().getColumnCount() > 0) {
            tblEmmaLimitTransaction.getColumnModel().getColumn(0).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(1).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(2).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(3).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(4).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(5).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(6).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(7).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(8).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(9).setResizable(false);
            tblEmmaLimitTransaction.getColumnModel().getColumn(10).setResizable(false);
        }

        txtSearchFilter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSearchFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchFilterActionPerformed(evt);
            }
        });
        txtSearchFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchFilterKeyReleased(evt);
            }
        });

        btnEditData.setText("Edit Data");
        btnEditData.setBackground(new java.awt.Color(0, 0, 255));
        btnEditData.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEditData.setForeground(new java.awt.Color(255, 255, 255));
        btnEditData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditDataActionPerformed(evt);
            }
        });

        btnRefreshData.setText("Refresh");
        btnRefreshData.setBackground(new java.awt.Color(0, 0, 255));
        btnRefreshData.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRefreshData.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMonitoringLayout = new javax.swing.GroupLayout(pnlMonitoring);
        pnlMonitoring.setLayout(pnlMonitoringLayout);
        pnlMonitoringLayout.setHorizontalGroup(
            pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMonitoringLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE)
                    .addGroup(pnlMonitoringLayout.createSequentialGroup()
                        .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMonitoringLayout.createSequentialGroup()
                                .addComponent(lblStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)
                                .addComponent(lblSearchByFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbSearchByFilter, 0, 92, Short.MAX_VALUE))
                            .addComponent(txtSearchFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addComponent(btnRefreshData, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditData, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlMonitoringLayout.setVerticalGroup(
            pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMonitoringLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblSearchByFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                        .addComponent(cmbSearchByFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchFilter)
                    .addComponent(btnEditData)
                    .addComponent(btnRefreshData))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tbdProgram.addTab("Monitoring", pnlMonitoring);

        pnlAddLimitList.setBackground(new java.awt.Color(51, 204, 255));
        pnlAddLimitList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlAddLimitList.setLayout(null);

        lblByReseller.setText("By Reseller");
        lblByReseller.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblByReseller);
        lblByReseller.setBounds(70, 50, 96, 23);

        lblTransactionPeriod.setText("Masa Transaksi");
        lblTransactionPeriod.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblTransactionPeriod);
        lblTransactionPeriod.setBounds(70, 110, 96, 23);

        lblProduk.setText("Produk");
        lblProduk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblProduk);
        lblProduk.setBounds(70, 80, 96, 23);

        lblErrorMessage.setText("Pesan Gagal");
        lblErrorMessage.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblErrorMessage);
        lblErrorMessage.setBounds(70, 230, 96, 23);

        lblTransactionLimit.setText("Limit Transaksi");
        lblTransactionLimit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblTransactionLimit);
        lblTransactionLimit.setBounds(70, 140, 96, 23);

        cmbByGroup.setEditable(true);
        cmbByGroup.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbByGroup.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbByGroupItemStateChanged(evt);
            }
        });
        cmbByGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbByGroupActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(cmbByGroup);
        cmbByGroup.setBounds(170, 320, 200, 23);

        cmbByReseller.setEditable(true);
        cmbByReseller.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbByReseller.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbByResellerItemStateChanged(evt);
            }
        });
        cmbByReseller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbByResellerActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(cmbByReseller);
        cmbByReseller.setBounds(170, 50, 200, 23);

        cmbProduk.setEditable(true);
        cmbProduk.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProdukActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(cmbProduk);
        cmbProduk.setBounds(170, 80, 200, 23);

        cmbTransactionPeriod.setEditable(true);
        cmbTransactionPeriod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HARIAN", "MINGGUAN", "BULANAN" }));
        cmbTransactionPeriod.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbTransactionPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTransactionPeriodActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(cmbTransactionPeriod);
        cmbTransactionPeriod.setBounds(170, 110, 130, 23);

        lblCount.setText("Kali");
        lblCount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblCount);
        lblCount.setBounds(210, 140, 30, 23);

        txaErrorMessage.setColumns(20);
        txaErrorMessage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txaErrorMessage.setRows(5);
        scpErrorMessage.setViewportView(txaErrorMessage);

        pnlAddLimitList.add(scpErrorMessage);
        scpErrorMessage.setBounds(170, 230, 240, 81);

        btnClearData.setText("Clear Data");
        btnClearData.setBackground(new java.awt.Color(0, 0, 255));
        btnClearData.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClearData.setForeground(new java.awt.Color(255, 255, 255));
        btnClearData.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnClearData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearDataActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(btnClearData);
        btnClearData.setBounds(500, 220, 130, 30);

        btnGenerate.setText("Generate");
        btnGenerate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(btnGenerate);
        btnGenerate.setBounds(420, 280, 130, 30);

        btnUpdateSave.setText("Simpan");
        btnUpdateSave.setBackground(new java.awt.Color(0, 0, 255));
        btnUpdateSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUpdateSave.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSaveActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(btnUpdateSave);
        btnUpdateSave.setBounds(640, 220, 110, 30);

        lblByGroup.setText("By Group");
        lblByGroup.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblByGroup);
        lblByGroup.setBounds(70, 320, 96, 23);

        txtTransactionLimit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTransactionLimit.setText("0");
        txtTransactionLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTransactionLimitActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(txtTransactionLimit);
        txtTransactionLimit.setBounds(170, 140, 36, 23);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblStartEvent.setText("Tgl Mulai Event");
        lblStartEvent.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        lblEndEvent.setText("Tgl Akhir Event");
        lblEndEvent.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        dtpStartDateEvent.setBackground(new java.awt.Color(51, 153, 255));
        dtpStartDateEvent.setInheritsPopupMenu(true);
        dtpStartDateEvent.setMinimumSize(new java.awt.Dimension(135, 15));
        dtpStartDateEvent.setSettings(null);

        dtpEndDateEvent.setBackground(new java.awt.Color(51, 153, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStartEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEndEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dtpStartDateEvent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtpEndDateEvent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(dtpStartDateEvent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dtpEndDateEvent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(lblStartEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lblEndEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))))
        );

        pnlAddLimitList.add(jPanel3);
        jPanel3.setBounds(470, 50, 270, 77);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setText("Status");
        pnlAddLimitList.add(lblStatus);
        lblStatus.setBounds(480, 140, 80, 23);

        cmbStatus.setEditable(true);
        cmbStatus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aktif", "Non Aktif" }));
        cmbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStatusActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(cmbStatus);
        cmbStatus.setBounds(580, 140, 110, 23);

        tbdProgram.addTab("Daftar Limit", pnlAddLimitList);

        lblTitleHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleHeader.setText("Limit Transaction Program");
        lblTitleHeader.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbdProgram)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitleHeader)
                .addGap(243, 243, 243))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(lblTitleHeader)
                .addGap(18, 18, 18)
                .addComponent(tbdProgram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        lblTitleHeader.getAccessibleContext().setAccessibleName("jTitle");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbByGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbByGroupActionPerformed
        // TODO add your handling code here:
//        if (cmbByGroup.getSelectedItem() != "") {
//            cmbByReseller.setEnabled(false);
//        } else {
//            cmbByReseller.setEnabled(true);
//        }
    }//GEN-LAST:event_cmbByGroupActionPerformed

    private void cmbByResellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbByResellerActionPerformed
        // TODO add your handling code here:
//        if (cmbByReseller.getSelectedItem() != "") {
//            cmbByGroup.setEnabled(false);
//        } else {
//            cmbByGroup.setEnabled(true);
//        }
    }//GEN-LAST:event_cmbByResellerActionPerformed

    private void cmbProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbProdukActionPerformed

    private void cmbTransactionPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTransactionPeriodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTransactionPeriodActionPerformed

    private void btnClearDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearDataActionPerformed
        // TODO add your handling code here:
        if ("U".equals(flagUpdateSave)) {
            try {
                defaultSetVariable();
            } catch (Exception ex) {
                Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
            }
            tbdProgram.setSelectedIndex(0);
            btnUpdateSave.setText("Simpan");
            btnClearData.setText("Clear Data");

            TitledBorder title = BorderFactory.createTitledBorder("Tambah Daftar Limit");
            pnlAddLimitList.setBorder(title);

            flagUpdateSave = "";
        } else {
            try {
                defaultSetVariable();
            } catch (Exception ex) {
                Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnClearDataActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGenerateActionPerformed

    private void btnUpdateSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSaveActionPerformed
        // TODO add your handling code here:
        if (cmbByReseller.getSelectedItem().equals("") || cmbProduk.getSelectedItem().equals("") || cmbTransactionPeriod.getSelectedItem().equals("") || txtTransactionLimit.getText().trim().equals("0") || dtpStartDateEvent.getText().equals("") || dtpEndDateEvent.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Data yang anda masukan tidak lengkap", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if ("U".equals(flagUpdateSave)) {
                try {
                    updateData();
                    countLimitData();
                    btnClearData.setText("Clear Data");
                    btnUpdateSave.setText("Simpan");
                } catch (Exception ex) {
                    Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    countIndexData();
                } catch (Exception ex) {
                    Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (totalRow == 0) {
                    saveData();
                } else {
                    JOptionPane.showMessageDialog(null, "Data telah di input sebelumnya, silahkan cek Monitoring", "Error", JOptionPane.ERROR_MESSAGE);
                    //JOptionPane.showMessageDialog(null, "Data telah di input sebelumnya, silahkan cek Monitoring");
                }
                try {
                    countLimitData();
                } catch (Exception ex) {
                    Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (totalRow == 0) {
                btnEditData.setEnabled(false);
                btnEditData.setToolTipText("Tidak Ada Data yang Bisa di Edit");
            } else {
                btnEditData.setEnabled(true);
                btnEditData.setToolTipText("Edit Data Limit Transaksi");
            }
            flagUpdateSave = "";
        }
    }//GEN-LAST:event_btnUpdateSaveActionPerformed

    private void cmbStatusFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusFilterActionPerformed
        try {
            // TODO add your handling code here:
            showLimitData();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            comboFilter();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }

        defaultSelectedRow();
    }//GEN-LAST:event_cmbStatusFilterActionPerformed

    private void txtSearchFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchFilterKeyReleased
        // TODO add your handling code here:
        searchTableFilter();
        defaultSelectedRow();
        try {
            showLimitData();
            comboFilter();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtSearchFilterKeyReleased

    private void txtTransactionLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTransactionLimitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTransactionLimitActionPerformed

    private void cmbSearchByFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchByFilterActionPerformed
        try {
            // TODO add your handling code here:
            showLimitData();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            comboFilter();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }

        defaultSelectedRow();
    }//GEN-LAST:event_cmbSearchByFilterActionPerformed

    private void btnEditDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditDataActionPerformed
        // TODO add your handling code here:
        tbdProgram.setSelectedIndex(1);
        TitledBorder title = BorderFactory.createTitledBorder("Ubah Daftar Limit");
        pnlAddLimitList.setBorder(title);
        cmbStatus.setEnabled(true);
        try {
            getEditData();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
        flagUpdateSave = "U";
        cmbByGroup.setEnabled(false);
        cmbByReseller.setEnabled(false);
        cmbProduk.setEnabled(false);
        btnClearData.setText("Batal");
        btnUpdateSave.setText("Ubah");
    }//GEN-LAST:event_btnEditDataActionPerformed

    private void cmbByGroupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbByGroupItemStateChanged
        // TODO add your handling code here:
        //System.out.println(cmbByGroup.getSelectedItem());
        if (cmbByGroup.getSelectedItem() != "") {
            cmbByReseller.setEnabled(false);
        } else {
            cmbByReseller.setEnabled(true);
        }
    }//GEN-LAST:event_cmbByGroupItemStateChanged

    private void cmbByResellerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbByResellerItemStateChanged
        // TODO add your handling code here:
        if (cmbByReseller.getSelectedItem() != "") {
            cmbByGroup.setEnabled(false);
        } else {
            cmbByGroup.setEnabled(true);
        }
    }//GEN-LAST:event_cmbByResellerItemStateChanged

    private void btnRefreshDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDataActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            //defaultSetVariable();
            showLimitData();
            comboFilter();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }

        defaultSelectedRow();
    }//GEN-LAST:event_btnRefreshDataActionPerformed

    private void txtSearchFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchFilterActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatusActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LimitAddOn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new LimitAddOn().setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearData;
    private javax.swing.JButton btnEditData;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JButton btnRefreshData;
    private javax.swing.JButton btnUpdateSave;
    private javax.swing.JComboBox<String> cmbByGroup;
    private javax.swing.JComboBox<String> cmbByReseller;
    private javax.swing.JComboBox<String> cmbProduk;
    private javax.swing.JComboBox<String> cmbSearchByFilter;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JComboBox<String> cmbStatusFilter;
    private javax.swing.JComboBox<String> cmbTransactionPeriod;
    private com.github.lgooddatepicker.components.DatePicker dtpEndDateEvent;
    private com.github.lgooddatepicker.components.DatePicker dtpStartDateEvent;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblByGroup;
    private javax.swing.JLabel lblByReseller;
    private javax.swing.JLabel lblCount;
    private javax.swing.JLabel lblEndEvent;
    private javax.swing.JLabel lblErrorMessage;
    private javax.swing.JLabel lblProduk;
    private javax.swing.JLabel lblSearchByFilter;
    private javax.swing.JLabel lblStartEvent;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusFilter;
    private javax.swing.JLabel lblTitleHeader;
    private javax.swing.JLabel lblTransactionLimit;
    private javax.swing.JLabel lblTransactionPeriod;
    private javax.swing.JPanel pnlAddLimitList;
    private javax.swing.JPanel pnlMonitoring;
    private javax.swing.JScrollPane scpErrorMessage;
    private javax.swing.JTabbedPane tbdProgram;
    private javax.swing.JTable tblEmmaLimitTransaction;
    private javax.swing.JTextArea txaErrorMessage;
    private javax.swing.JTextField txtSearchFilter;
    private javax.swing.JTextField txtTransactionLimit;
    // End of variables declaration//GEN-END:variables
}
