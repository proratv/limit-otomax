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
    int totalIndex;
    String kodeLimitColumn;
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

    String dbName1 = "Otomax";
    String dbName2 = "EmmaDB";

    /**
     * Creates new form NewJFrame
     *
     * @throws java.lang.Exception
     */
    public LimitAddOn() throws Exception {
        initComponents();
        getContextMenu();
        getImageIcon();
        defaultSetVariable();
        configComponent();
        configLimitTable();
        showLimitHeader();
        //comboFilter();
        datePick();
        AutoCompleteDecorator.decorate(cmbGrup);
        AutoCompleteDecorator.decorate(cmbProduk);
        byGrupList();
        byProdukList();
        captureSelectedRow();
    }

    private void getContextMenu() {
        DefaultContextMenu DefaultContextMenu = new DefaultContextMenu();
        DefaultContextMenu.addDefaultContextMenu(txtSearchFilterHeader);
    } // done

    private void getImageIcon() {
        String iconPath = "images\\limit_dashboard.png";
        ImageIcon img = new ImageIcon(iconPath);
        setIconImage(img.getImage());
    } // done

    private void byGrupList() throws Exception {
        try {
            String query;
            query = "select distinct kode, nama from level";
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName1);
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                cmbGrup.addItem(res.getString("kode").trim());
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    } // done

    private void byProdukList() throws Exception {
        try {
            String query;
            query = "select distinct kode, nama from produk";
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName1);
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                cmbProduk.addItem(res.getString("kode").trim());
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    } // done

    private void defaultSelectedRow() {
        selectedRow = -1;

        if (selectedRow == -1) {
            btnEditData.setEnabled(false);
            btnEditData.setToolTipText("Tidak Ada Data yang Bisa di Edit");
        } else {
            btnEditData.setEnabled(true);
            btnEditData.setToolTipText("Edit Data Limit Transaksi");
        }
    } // done

    private void captureSelectedRow() {
        ListSelectionModel model = tblLimitHeader.getSelectionModel();
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if (!model.isSelectionEmpty()) {
                selectedRow = model.getMinSelectionIndex();

                //System.out.println(selectedRow);
                if (selectedRow == -1) {
                    btnEditData.setEnabled(false);
                    btnEditData.setToolTipText("Tidak Ada Data yang Bisa di Edit");
                } else {
                    btnEditData.setEnabled(true);
                    btnEditData.setToolTipText("Edit Data Limit Transaksi");
                }
            }
        });
    } // done

    private void defaultSetVariable() throws Exception {
        cmbGrup.setSelectedItem("");
        cmbProduk.setSelectedItem("");
        cmbTransactionPeriod.setSelectedItem("");
        txtTransactionLimit.setText("0");
        txaErrorMessage.setText("");
        maxKodeLimit = 0;
        totalIndex = 0;
        kodeLimitColumn = null;
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
        txtSearchFilterHeader.setText("");

        configComponent();
    } // done

    private void configComponent() throws Exception {
        txtSearchFilterHeader.requestFocus();
        cmbGrup.setEnabled(true);
        cmbProduk.setEnabled(true);
        cmbTransactionPeriod.setEnabled(true);
        txtTransactionLimit.setEnabled(true);
        dtpStartDateEvent.setEnabled(true);
        dtpEndDateEvent.setEnabled(true);

        TitledBorder title = BorderFactory.createTitledBorder("Tambah Daftar Limit");
        pnlAddLimitList.setBorder(title);

        ListSelectionModel model = tblLimitDetail.getSelectionModel();
        selectedRow = model.getMinSelectionIndex();

        if (selectedRow == -1) {
            btnEditData.setEnabled(false);
            btnEditData.setToolTipText("Tidak Ada Data yang Bisa di Edit");
        } else {
            btnEditData.setEnabled(true);
            btnEditData.setToolTipText("Edit Data Limit Transaksi");
        }
    } // done

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
    } // done

    private void countIndexData() throws Exception {
        grupColumn = cmbGrup.getSelectedItem().toString();
        produkColumn = cmbProduk.getSelectedItem().toString();

        try {
            String query;
            query = "select count(0) as total_index from limit_hd where grup = '" + grupColumn + "' and produk = '" + produkColumn + "'";
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName2);
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                totalIndex = res.getInt("total_index");
                System.out.println(totalIndex);
            }

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    } /// done

    private void showLimitHeader() throws Exception {
        try {
            String query;
            query = "select * from limit_hd order by kode_limit desc";
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName2);
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) tblLimitHeader.getModel();
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
                model.addRow(new Object[]{res.getString("kode_limit"), res.getString("grup"), res.getString("produk"), res.getString("masa_limit"), res.getString("jumlah_limit"), statusColumn, res.getString("pesan_gagal"), startDate, endDate});
            }
        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    } // done

    private void showLimitDetail() throws Exception {
        DefaultTableModel modelHeader = (DefaultTableModel) tblLimitHeader.getModel();
        int SelectedRowIndex = tblLimitHeader.getSelectedRow();

        String kodeLimit;
        if (SelectedRowIndex == -1) {
            kodeLimit = "-1";
        } else {
            kodeLimit = (String) modelHeader.getValueAt(SelectedRowIndex, 0);
        }

        try {
            String query;
            query = "select aLimit.kode_limit, aLimit.reseller, reseller.nama, aLimit.transaksi_dalam_masa, \n" +
                    "aLimit.transaksi_sedang_proses, aLimit.transaksi_total, aLimit.status\n" +
                    "from EmmaDB.dbo.limit_dt aLimit join reseller on aLimit.reseller = reseller.kode\n" +
                    "where aLimit.kode_limit = "+ kodeLimit +" order by kode_limit desc";
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName1);
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) tblLimitDetail.getModel();
            model.setRowCount(0);

            while (res.next()) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

                isStatusColumn = res.getBoolean("status");
                if (isStatusColumn == true) {
                    statusColumn = "Aktif";
                } else {
                    statusColumn = "Non Aktif";
                }
                model.addRow(new Object[]{res.getString("kode_limit"), res.getString("reseller"), res.getString("nama"), res.getString("transaksi_sedang_proses"), res.getString("transaksi_dalam_masa"), res.getString("transaksi_total"), statusColumn});
            }
        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    }

    private void configLimitTable() throws Exception {

        TableCellRenderer rendererFromHeader = tblLimitHeader.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);

        TableCellRenderer rendererFromDetail = tblLimitDetail.getTableHeader().getDefaultRenderer();
        JLabel detailLabel = (JLabel) rendererFromDetail;
        detailLabel.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModelHeader = tblLimitHeader.getColumnModel();
        columnModelHeader.getColumn(0).setPreferredWidth(40);
        columnModelHeader.getColumn(1).setPreferredWidth(50);
        columnModelHeader.getColumn(2).setPreferredWidth(50);
        columnModelHeader.getColumn(3).setPreferredWidth(40);
        columnModelHeader.getColumn(4).setPreferredWidth(75);
        columnModelHeader.getColumn(5).setPreferredWidth(50);
        columnModelHeader.getColumn(6).setPreferredWidth(200);
        columnModelHeader.getColumn(7).setPreferredWidth(70);
        columnModelHeader.getColumn(8).setPreferredWidth(70);

        TableColumnModel columnModelDetail = tblLimitDetail.getColumnModel();
        columnModelDetail.getColumn(1).setPreferredWidth(50);
        columnModelDetail.getColumn(2).setPreferredWidth(150);
        columnModelDetail.getColumn(3).setPreferredWidth(50);
        columnModelDetail.getColumn(4).setPreferredWidth(50);
        columnModelDetail.getColumn(5).setPreferredWidth(50);

        columnModelDetail.getColumn(0).setMinWidth(0);
        columnModelDetail.getColumn(0).setMaxWidth(0);
        columnModelDetail.getColumn(6).setMinWidth(0);
        columnModelDetail.getColumn(6).setMaxWidth(0);
    } // done

    private void getEditData() throws Exception {
        DefaultTableModel model = (DefaultTableModel) tblLimitHeader.getModel();
        int SelectedRowIndex = tblLimitHeader.getSelectedRow();

        try {
            String query;
            query = "select count(0) as total_row from limit_hd where kode_limit = " + model.getValueAt(SelectedRowIndex, 0);
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName2);
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                totalIndex = res.getInt("total_row");
            }

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }

        kodeLimitColumn = (String) (model.getValueAt(SelectedRowIndex, 0));
        cmbGrup.setSelectedItem(model.getValueAt(SelectedRowIndex, 1));
        cmbProduk.setSelectedItem(model.getValueAt(SelectedRowIndex, 2));
        cmbTransactionPeriod.setSelectedItem(model.getValueAt(SelectedRowIndex, 3));
        txtTransactionLimit.setText(model.getValueAt(SelectedRowIndex, 4).toString());
        cmbStatus.setSelectedItem(model.getValueAt(SelectedRowIndex, 5));
        dtpStartDateEvent.setText(model.getValueAt(SelectedRowIndex, 7).toString());
        dtpEndDateEvent.setText(model.getValueAt(SelectedRowIndex, 8).toString());
        txaErrorMessage.append((String) model.getValueAt(SelectedRowIndex, 6));
    } // done

    private void getMaxKodeLimit() throws Exception {
        try {
            String query;
            query = "select max(kode_limit) as max_kode_limit from limit_hd";
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName2);
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                maxKodeLimit = res.getInt("max_kode_limit");
            }

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error Message: " + e.getMessage());
        }
    } // done

    private void comboFilter() {
        try {
            String query;
            query = "select * from limit_hd where grup = CASE WHEN " + grupColumn + " = grup THEN grup ELSE '' END order by kode_limit desc";
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName2);
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) tblLimitHeader.getModel();
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
    } // please check for filtering limit header table

    private void searchTableHeaderFilter() {
        DefaultTableModel table = (DefaultTableModel) tblLimitHeader.getModel();
        String search;
        search = txtSearchFilterHeader.getText();
        TableRowSorter<DefaultTableModel> tr;
        tr = new TableRowSorter<>(table);
        tblLimitHeader.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + search, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    } // please check for filtering limit header table
    
    private void searchTableDetailFilter() {
        DefaultTableModel table = (DefaultTableModel) tblLimitDetail.getModel();
        String search;
        search = txtSearchFilterDetail.getText();
        TableRowSorter<DefaultTableModel> tr;
        tr = new TableRowSorter<>(table);
        tblLimitDetail.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + search, 1, 2, 3, 4));
    } // please check for filtering limit header table

    private void saveData() {
        DefaultTableModel model;
        model = (DefaultTableModel) tblLimitDetail.getModel();
        try {
            String query;
            query = "INSERT INTO limit_hd (kode_limit, grup, produk, masa_limit, jumlah_limit, status, pesan_gagal, tgl_mulai_event, tgl_akhir_event, date_create, date_update) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName2);
            PreparedStatement pst = conn.prepareStatement(query);
            getMaxKodeLimit();
            maxKodeLimit += 1;
            grupColumn = cmbGrup.getSelectedItem().toString();
            produkColumn = cmbProduk.getSelectedItem().toString();
            masaLimitColumn = cmbTransactionPeriod.getSelectedItem().toString();
            jumlahLimitColumn = Integer.parseInt(txtTransactionLimit.getText());
            errorMessageColumn = txaErrorMessage.getText();
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
            pst.setString(3, produkColumn);
            pst.setString(4, masaLimitColumn);
            pst.setInt(5, jumlahLimitColumn);
            pst.setBoolean(6, isStatusColumn);
            pst.setString(7, errorMessageColumn);
            pst.setDate(8, startEventDate);
            pst.setDate(9, endEventDate);
            pst.setDate(10, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setDate(11, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.executeUpdate();
            showLimitHeader();
            defaultSetVariable();
            JOptionPane.showMessageDialog(null, "Data telah berhasil di simpan");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // done

    private void updateData() throws Exception {
        try {
            String query;
            query = "update limit_hd set masa_limit = ?, jumlah_limit = ?, status=?, pesan_gagal=?, tgl_mulai_event = ?, tgl_akhir_event = ?, date_update = ? where kode_limit = ?";
            Connection conn = (Connection) DatabaseConnection.getConnection(dbName2);
            PreparedStatement pst = conn.prepareStatement(query);
            masaLimitColumn = cmbTransactionPeriod.getSelectedItem().toString();
            jumlahLimitColumn = Integer.parseInt(txtTransactionLimit.getText());
            isStatusColumn = cmbStatus.getSelectedItem().toString() == "Aktif";
            errorMessageColumn = txaErrorMessage.getText();
            startDate = dtpStartDateEvent.getText();
            endDate = dtpEndDateEvent.getText();
            grupColumn = cmbGrup.getSelectedItem().toString();
            produkColumn = cmbProduk.getSelectedItem().toString();

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMMMM-yyyy");

            Date sqlDate1 = (Date) dateFormatter.parse(startDate);
            Date sqlDate2 = (Date) dateFormatter.parse(endDate);

            java.sql.Date startEventDate = new java.sql.Date(sqlDate1.getTime());
            java.sql.Date endEventDate = new java.sql.Date(sqlDate2.getTime());

            pst.setString(1, masaLimitColumn);
            pst.setInt(2, jumlahLimitColumn);
            pst.setBoolean(3, isStatusColumn);
            pst.setString(4, errorMessageColumn);
            pst.setDate(5, startEventDate);
            pst.setDate(6, endEventDate);
            pst.setDate(7, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setString(8, kodeLimitColumn);
            pst.executeUpdate();
            showLimitHeader();
            defaultSetVariable();
            JOptionPane.showMessageDialog(null, "Data telah berhasil di ubah");
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
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLimitDetail = new javax.swing.JTable();
        txtSearchFilterHeader = new javax.swing.JTextField();
        btnEditData = new javax.swing.JButton();
        btnRefreshData = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblLimitHeader = new javax.swing.JTable();
        txtSearchFilterDetail = new javax.swing.JTextField();
        lblHeader = new javax.swing.JLabel();
        lblDetail = new javax.swing.JLabel();
        pnlAddLimitList = new javax.swing.JPanel();
        lblTransactionPeriod = new javax.swing.JLabel();
        lblProduk = new javax.swing.JLabel();
        lblErrorMessage = new javax.swing.JLabel();
        lblTransactionLimit = new javax.swing.JLabel();
        cmbGrup = new javax.swing.JComboBox<>();
        cmbProduk = new javax.swing.JComboBox<>();
        cmbTransactionPeriod = new javax.swing.JComboBox<>();
        scpErrorMessage = new javax.swing.JScrollPane();
        txaErrorMessage = new javax.swing.JTextArea();
        btnClearData = new javax.swing.JButton();
        btnUpdateSave = new javax.swing.JButton();
        lblGrup = new javax.swing.JLabel();
        txtTransactionLimit = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        dtpEndDateEvent = new com.github.lgooddatepicker.components.DatePicker();
        lblEndEvent = new javax.swing.JLabel();
        lblStartEvent = new javax.swing.JLabel();
        dtpStartDateEvent = new com.github.lgooddatepicker.components.DatePicker();
        txtParamReseller = new javax.swing.JTextField();
        txtParamKodeTransaksi = new javax.swing.JTextField();
        txtParamProduk = new javax.swing.JTextField();
        txtParamTujuan = new javax.swing.JTextField();
        txtParamHarga = new javax.swing.JTextField();
        txtParamHargaBeli = new javax.swing.JTextField();
        txtParamSaldoAwal = new javax.swing.JTextField();
        jNullPanel = new javax.swing.JPanel();
        lblLimitCount = new javax.swing.JLabel();
        lblRequiredGrup = new javax.swing.JLabel();
        lblRequiredProduk = new javax.swing.JLabel();
        lblRequiredPeriod = new javax.swing.JLabel();
        lblRequiredTransaction = new javax.swing.JLabel();
        lblRequiredStartDate = new javax.swing.JLabel();
        lblRequiredEndDate = new javax.swing.JLabel();
        lblRequiredErrorMessage = new javax.swing.JLabel();
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

        tblLimitDetail.setBackground(new java.awt.Color(153, 255, 255));
        tblLimitDetail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        tblLimitDetail.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblLimitDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Limit", "Kode Reseller", "Reseller", "Transaksi Sedang Proses", "Transaksi Dalam Masa", "Total Transaksi", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLimitDetail.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblLimitDetail.setGridColor(new java.awt.Color(51, 51, 255));
        tblLimitDetail.setRowHeight(20);
        jScrollPane2.setViewportView(tblLimitDetail);
        if (tblLimitDetail.getColumnModel().getColumnCount() > 0) {
            tblLimitDetail.getColumnModel().getColumn(0).setResizable(false);
            tblLimitDetail.getColumnModel().getColumn(1).setResizable(false);
            tblLimitDetail.getColumnModel().getColumn(2).setResizable(false);
            tblLimitDetail.getColumnModel().getColumn(3).setResizable(false);
            tblLimitDetail.getColumnModel().getColumn(4).setResizable(false);
            tblLimitDetail.getColumnModel().getColumn(5).setResizable(false);
            tblLimitDetail.getColumnModel().getColumn(6).setResizable(false);
        }

        txtSearchFilterHeader.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSearchFilterHeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchFilterHeaderActionPerformed(evt);
            }
        });
        txtSearchFilterHeader.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchFilterHeaderKeyReleased(evt);
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

        tblLimitHeader.setBackground(new java.awt.Color(153, 255, 255));
        tblLimitHeader.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        tblLimitHeader.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblLimitHeader.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Limit", "Grup", "Produk", "Masa Limit", "Jumlah Limit", "Status", "Pesan Gagal", "Tgl Awal Trx", "Tgl Akhir Trx"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLimitHeader.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblLimitHeader.setGridColor(new java.awt.Color(51, 51, 255));
        tblLimitHeader.setRowHeight(20);
        tblLimitHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLimitHeaderMouseClicked(evt);
            }
        });
        tblLimitHeader.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblLimitHeaderKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblLimitHeader);
        if (tblLimitHeader.getColumnModel().getColumnCount() > 0) {
            tblLimitHeader.getColumnModel().getColumn(0).setResizable(false);
            tblLimitHeader.getColumnModel().getColumn(1).setResizable(false);
            tblLimitHeader.getColumnModel().getColumn(2).setResizable(false);
            tblLimitHeader.getColumnModel().getColumn(3).setResizable(false);
            tblLimitHeader.getColumnModel().getColumn(4).setResizable(false);
            tblLimitHeader.getColumnModel().getColumn(5).setResizable(false);
            tblLimitHeader.getColumnModel().getColumn(6).setResizable(false);
            tblLimitHeader.getColumnModel().getColumn(7).setResizable(false);
            tblLimitHeader.getColumnModel().getColumn(8).setResizable(false);
        }

        txtSearchFilterDetail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSearchFilterDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchFilterDetailActionPerformed(evt);
            }
        });
        txtSearchFilterDetail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchFilterDetailKeyReleased(evt);
            }
        });

        lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader.setText("Header");
        lblHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        lblDetail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDetail.setText("Detail");
        lblDetail.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout pnlMonitoringLayout = new javax.swing.GroupLayout(pnlMonitoring);
        pnlMonitoring.setLayout(pnlMonitoringLayout);
        pnlMonitoringLayout.setHorizontalGroup(
            pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMonitoringLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 960, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 960, Short.MAX_VALUE)
                    .addGroup(pnlMonitoringLayout.createSequentialGroup()
                        .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearchFilterHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDetail)
                            .addComponent(txtSearchFilterDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlMonitoringLayout.createSequentialGroup()
                        .addComponent(lblHeader)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshData, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditData, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlMonitoringLayout.setVerticalGroup(
            pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMonitoringLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnEditData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshData, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblHeader))
                .addGap(6, 6, 6)
                .addComponent(txtSearchFilterHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDetail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearchFilterDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        tbdProgram.addTab("Monitoring", pnlMonitoring);

        pnlAddLimitList.setBackground(new java.awt.Color(51, 204, 255));
        pnlAddLimitList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlAddLimitList.setLayout(null);

        lblTransactionPeriod.setText("Masa Transaksi");
        lblTransactionPeriod.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblTransactionPeriod);
        lblTransactionPeriod.setBounds(30, 90, 96, 30);

        lblProduk.setText("Produk");
        lblProduk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblProduk);
        lblProduk.setBounds(30, 60, 96, 30);

        lblErrorMessage.setText("Pesan Gagal");
        lblErrorMessage.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblErrorMessage);
        lblErrorMessage.setBounds(30, 240, 96, 23);

        lblTransactionLimit.setText("Limit Transaksi");
        lblTransactionLimit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblTransactionLimit);
        lblTransactionLimit.setBounds(30, 120, 96, 30);

        cmbGrup.setEditable(true);
        cmbGrup.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbGrup.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbGrupItemStateChanged(evt);
            }
        });
        cmbGrup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGrupActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(cmbGrup);
        cmbGrup.setBounds(130, 30, 200, 30);

        cmbProduk.setEditable(true);
        cmbProduk.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProdukActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(cmbProduk);
        cmbProduk.setBounds(130, 60, 200, 30);

        cmbTransactionPeriod.setEditable(true);
        cmbTransactionPeriod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HARIAN", "MINGGUAN", "BULANAN" }));
        cmbTransactionPeriod.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbTransactionPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTransactionPeriodActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(cmbTransactionPeriod);
        cmbTransactionPeriod.setBounds(130, 90, 130, 30);

        txaErrorMessage.setColumns(20);
        txaErrorMessage.setLineWrap(true);
        txaErrorMessage.setRows(5);
        txaErrorMessage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        scpErrorMessage.setViewportView(txaErrorMessage);

        pnlAddLimitList.add(scpErrorMessage);
        scpErrorMessage.setBounds(130, 240, 380, 110);

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
        btnClearData.setBounds(250, 510, 130, 30);

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
        btnUpdateSave.setBounds(390, 510, 110, 30);

        lblGrup.setText("Grup");
        lblGrup.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblGrup);
        lblGrup.setBounds(30, 30, 96, 30);

        txtTransactionLimit.setText("0");
        txtTransactionLimit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTransactionLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTransactionLimitActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(txtTransactionLimit);
        txtTransactionLimit.setBounds(130, 120, 36, 30);

        lblStatus.setText("Status");
        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblStatus);
        lblStatus.setBounds(30, 150, 90, 30);

        cmbStatus.setEditable(true);
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aktif", "Non Aktif" }));
        cmbStatus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStatusActionPerformed(evt);
            }
        });
        pnlAddLimitList.add(cmbStatus);
        cmbStatus.setBounds(130, 150, 190, 30);

        dtpEndDateEvent.setBackground(new java.awt.Color(51, 153, 255));
        pnlAddLimitList.add(dtpEndDateEvent);
        dtpEndDateEvent.setBounds(130, 210, 191, 30);

        lblEndEvent.setText("Tgl Akhir Event");
        lblEndEvent.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblEndEvent);
        lblEndEvent.setBounds(30, 210, 96, 20);

        lblStartEvent.setText("Tgl Mulai Event");
        lblStartEvent.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pnlAddLimitList.add(lblStartEvent);
        lblStartEvent.setBounds(30, 180, 96, 30);

        dtpStartDateEvent.setBackground(new java.awt.Color(51, 153, 255));
        dtpStartDateEvent.setInheritsPopupMenu(true);
        dtpStartDateEvent.setMinimumSize(new java.awt.Dimension(135, 15));
        dtpStartDateEvent.setSettings(null);
        pnlAddLimitList.add(dtpStartDateEvent);
        dtpStartDateEvent.setBounds(130, 180, 191, 30);

        txtParamReseller.setEditable(false);
        txtParamReseller.setText("$reseller$");
        txtParamReseller.setBackground(new java.awt.Color(255, 255, 255));
        txtParamReseller.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtParamReseller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParamResellerActionPerformed(evt);
            }
        });
        txtParamReseller.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtParamResellerKeyReleased(evt);
            }
        });
        pnlAddLimitList.add(txtParamReseller);
        txtParamReseller.setBounds(130, 450, 190, 30);

        txtParamKodeTransaksi.setEditable(false);
        txtParamKodeTransaksi.setText("$kode_transaksi$");
        txtParamKodeTransaksi.setBackground(new java.awt.Color(255, 255, 255));
        txtParamKodeTransaksi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtParamKodeTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParamKodeTransaksiActionPerformed(evt);
            }
        });
        txtParamKodeTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtParamKodeTransaksiKeyReleased(evt);
            }
        });
        pnlAddLimitList.add(txtParamKodeTransaksi);
        txtParamKodeTransaksi.setBounds(130, 360, 190, 30);

        txtParamProduk.setEditable(false);
        txtParamProduk.setText("$produk$");
        txtParamProduk.setBackground(new java.awt.Color(255, 255, 255));
        txtParamProduk.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtParamProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParamProdukActionPerformed(evt);
            }
        });
        txtParamProduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtParamProdukKeyReleased(evt);
            }
        });
        pnlAddLimitList.add(txtParamProduk);
        txtParamProduk.setBounds(130, 390, 190, 30);

        txtParamTujuan.setEditable(false);
        txtParamTujuan.setText("$tujuan$");
        txtParamTujuan.setBackground(new java.awt.Color(255, 255, 255));
        txtParamTujuan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtParamTujuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParamTujuanActionPerformed(evt);
            }
        });
        txtParamTujuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtParamTujuanKeyReleased(evt);
            }
        });
        pnlAddLimitList.add(txtParamTujuan);
        txtParamTujuan.setBounds(130, 420, 190, 30);

        txtParamHarga.setEditable(false);
        txtParamHarga.setText("$harga$");
        txtParamHarga.setBackground(new java.awt.Color(255, 255, 255));
        txtParamHarga.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtParamHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParamHargaActionPerformed(evt);
            }
        });
        txtParamHarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtParamHargaKeyReleased(evt);
            }
        });
        pnlAddLimitList.add(txtParamHarga);
        txtParamHarga.setBounds(320, 360, 190, 30);

        txtParamHargaBeli.setEditable(false);
        txtParamHargaBeli.setText("$harga_beli$");
        txtParamHargaBeli.setBackground(new java.awt.Color(255, 255, 255));
        txtParamHargaBeli.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtParamHargaBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParamHargaBeliActionPerformed(evt);
            }
        });
        txtParamHargaBeli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtParamHargaBeliKeyReleased(evt);
            }
        });
        pnlAddLimitList.add(txtParamHargaBeli);
        txtParamHargaBeli.setBounds(320, 390, 190, 30);

        txtParamSaldoAwal.setEditable(false);
        txtParamSaldoAwal.setText("$saldo_awal$");
        txtParamSaldoAwal.setBackground(new java.awt.Color(255, 255, 255));
        txtParamSaldoAwal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtParamSaldoAwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParamSaldoAwalActionPerformed(evt);
            }
        });
        txtParamSaldoAwal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtParamSaldoAwalKeyReleased(evt);
            }
        });
        pnlAddLimitList.add(txtParamSaldoAwal);
        txtParamSaldoAwal.setBounds(320, 420, 190, 30);

        javax.swing.GroupLayout jNullPanelLayout = new javax.swing.GroupLayout(jNullPanel);
        jNullPanel.setLayout(jNullPanelLayout);
        jNullPanelLayout.setHorizontalGroup(
            jNullPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
        jNullPanelLayout.setVerticalGroup(
            jNullPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );

        pnlAddLimitList.add(jNullPanel);
        jNullPanel.setBounds(540, 0, 450, 630);

        lblLimitCount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLimitCount.setText("Kali");
        pnlAddLimitList.add(lblLimitCount);
        lblLimitCount.setBounds(170, 120, 30, 30);

        lblRequiredGrup.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRequiredGrup.setForeground(new java.awt.Color(255, 51, 51));
        lblRequiredGrup.setText("*");
        lblRequiredGrup.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlAddLimitList.add(lblRequiredGrup);
        lblRequiredGrup.setBounds(340, 30, 10, 30);

        lblRequiredProduk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRequiredProduk.setForeground(new java.awt.Color(255, 51, 51));
        lblRequiredProduk.setText("*");
        lblRequiredProduk.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlAddLimitList.add(lblRequiredProduk);
        lblRequiredProduk.setBounds(340, 60, 10, 30);

        lblRequiredPeriod.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRequiredPeriod.setForeground(new java.awt.Color(255, 51, 51));
        lblRequiredPeriod.setText("*");
        lblRequiredPeriod.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlAddLimitList.add(lblRequiredPeriod);
        lblRequiredPeriod.setBounds(270, 90, 10, 30);

        lblRequiredTransaction.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRequiredTransaction.setForeground(new java.awt.Color(255, 51, 51));
        lblRequiredTransaction.setText("*");
        lblRequiredTransaction.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlAddLimitList.add(lblRequiredTransaction);
        lblRequiredTransaction.setBounds(200, 120, 10, 30);

        lblRequiredStartDate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRequiredStartDate.setForeground(new java.awt.Color(255, 51, 51));
        lblRequiredStartDate.setText("*");
        lblRequiredStartDate.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlAddLimitList.add(lblRequiredStartDate);
        lblRequiredStartDate.setBounds(330, 180, 10, 30);

        lblRequiredEndDate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRequiredEndDate.setForeground(new java.awt.Color(255, 51, 51));
        lblRequiredEndDate.setText("*");
        lblRequiredEndDate.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlAddLimitList.add(lblRequiredEndDate);
        lblRequiredEndDate.setBounds(330, 210, 10, 30);

        lblRequiredErrorMessage.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRequiredErrorMessage.setForeground(new java.awt.Color(255, 51, 51));
        lblRequiredErrorMessage.setText("*");
        lblRequiredErrorMessage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlAddLimitList.add(lblRequiredErrorMessage);
        lblRequiredErrorMessage.setBounds(520, 240, 8, 30);

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
            .addGroup(layout.createSequentialGroup()
                .addGap(359, 359, 359)
                .addComponent(lblTitleHeader)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tbdProgram)
                .addContainerGap())
        );

        lblTitleHeader.getAccessibleContext().setAccessibleName("jTitle");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbGrupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGrupActionPerformed
        // TODO add your handling code here:
//        if (cmbByGroup.getSelectedItem() != "") {
//            cmbByReseller.setEnabled(false);
//        } else {
//            cmbByReseller.setEnabled(true);
//        }
    }//GEN-LAST:event_cmbGrupActionPerformed

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

    private void btnUpdateSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSaveActionPerformed
        // TODO add your handling code here:
        if (cmbGrup.getSelectedItem().equals("") || cmbProduk.getSelectedItem().equals("") || cmbTransactionPeriod.getSelectedItem().equals("") || txtTransactionLimit.getText().trim().equals("0")
                || txaErrorMessage.getText().equals("") || dtpStartDateEvent.getText().equals("") || dtpEndDateEvent.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Data yang anda masukan tidak lengkap", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if ("U".equals(flagUpdateSave)) {
                try {
                    updateData();
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
                if (totalIndex == 0) {
                    saveData();
                } else {
                    JOptionPane.showMessageDialog(null, "Data telah di input sebelumnya, silahkan cek Monitoring", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        flagUpdateSave = "";
    }//GEN-LAST:event_btnUpdateSaveActionPerformed

    private void txtSearchFilterHeaderKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchFilterHeaderKeyReleased
        // TODO add your handling code here:
        searchTableHeaderFilter();
        defaultSelectedRow();
        try {
            showLimitHeader();
            //comboFilter();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtSearchFilterHeaderKeyReleased

    private void txtTransactionLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTransactionLimitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTransactionLimitActionPerformed

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
        cmbGrup.setEnabled(false);
        cmbProduk.setEnabled(false);
        btnClearData.setText("Batal");
        btnUpdateSave.setText("Ubah");
    }//GEN-LAST:event_btnEditDataActionPerformed

    private void cmbGrupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbGrupItemStateChanged
        // TODO add your handling code here:
        //System.out.println(cmbByGroup.getSelectedItem());

    }//GEN-LAST:event_cmbGrupItemStateChanged

    @SuppressWarnings("empty-statement")
    private void btnRefreshDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDataActionPerformed
        // TODO add your handling code here:
        txtSearchFilterHeader.setText(null);
        txtSearchFilterDetail.setText(null);
        searchTableHeaderFilter();
        defaultSelectedRow();

        try {
            showLimitDetail();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            showLimitHeader();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRefreshDataActionPerformed

    private void txtSearchFilterHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchFilterHeaderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchFilterHeaderActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void txtSearchFilterDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchFilterDetailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchFilterDetailActionPerformed

    private void txtSearchFilterDetailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchFilterDetailKeyReleased
        // TODO add your handling code here:
        searchTableDetailFilter();
        defaultSelectedRow();
        try {
            showLimitDetail();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtSearchFilterDetailKeyReleased

    private void txtParamResellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParamResellerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamResellerActionPerformed

    private void txtParamResellerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtParamResellerKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamResellerKeyReleased

    private void txtParamKodeTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParamKodeTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamKodeTransaksiActionPerformed

    private void txtParamKodeTransaksiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtParamKodeTransaksiKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamKodeTransaksiKeyReleased

    private void txtParamProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParamProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamProdukActionPerformed

    private void txtParamProdukKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtParamProdukKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamProdukKeyReleased

    private void txtParamTujuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParamTujuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamTujuanActionPerformed

    private void txtParamTujuanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtParamTujuanKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamTujuanKeyReleased

    private void txtParamHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParamHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamHargaActionPerformed

    private void txtParamHargaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtParamHargaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamHargaKeyReleased

    private void txtParamHargaBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParamHargaBeliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamHargaBeliActionPerformed

    private void txtParamHargaBeliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtParamHargaBeliKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamHargaBeliKeyReleased

    private void txtParamSaldoAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParamSaldoAwalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamSaldoAwalActionPerformed

    private void txtParamSaldoAwalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtParamSaldoAwalKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParamSaldoAwalKeyReleased

    private void tblLimitHeaderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblLimitHeaderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblLimitHeaderKeyPressed

    private void tblLimitHeaderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLimitHeaderMouseClicked
        try {
            showLimitDetail();
        } catch (Exception ex) {
            Logger.getLogger(LimitAddOn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblLimitHeaderMouseClicked

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
    private javax.swing.JButton btnRefreshData;
    private javax.swing.JButton btnUpdateSave;
    private javax.swing.JComboBox<String> cmbGrup;
    private javax.swing.JComboBox<String> cmbProduk;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JComboBox<String> cmbTransactionPeriod;
    private com.github.lgooddatepicker.components.DatePicker dtpEndDateEvent;
    private com.github.lgooddatepicker.components.DatePicker dtpStartDateEvent;
    private javax.swing.JPanel jNullPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblDetail;
    private javax.swing.JLabel lblEndEvent;
    private javax.swing.JLabel lblErrorMessage;
    private javax.swing.JLabel lblGrup;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblLimitCount;
    private javax.swing.JLabel lblProduk;
    private javax.swing.JLabel lblRequiredEndDate;
    private javax.swing.JLabel lblRequiredErrorMessage;
    private javax.swing.JLabel lblRequiredGrup;
    private javax.swing.JLabel lblRequiredPeriod;
    private javax.swing.JLabel lblRequiredProduk;
    private javax.swing.JLabel lblRequiredStartDate;
    private javax.swing.JLabel lblRequiredTransaction;
    private javax.swing.JLabel lblStartEvent;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitleHeader;
    private javax.swing.JLabel lblTransactionLimit;
    private javax.swing.JLabel lblTransactionPeriod;
    private javax.swing.JPanel pnlAddLimitList;
    private javax.swing.JPanel pnlMonitoring;
    private javax.swing.JScrollPane scpErrorMessage;
    private javax.swing.JTabbedPane tbdProgram;
    private javax.swing.JTable tblLimitDetail;
    private javax.swing.JTable tblLimitHeader;
    private javax.swing.JTextArea txaErrorMessage;
    private javax.swing.JTextField txtParamHarga;
    private javax.swing.JTextField txtParamHargaBeli;
    private javax.swing.JTextField txtParamKodeTransaksi;
    private javax.swing.JTextField txtParamProduk;
    private javax.swing.JTextField txtParamReseller;
    private javax.swing.JTextField txtParamSaldoAwal;
    private javax.swing.JTextField txtParamTujuan;
    private javax.swing.JTextField txtSearchFilterDetail;
    private javax.swing.JTextField txtSearchFilterHeader;
    private javax.swing.JTextField txtTransactionLimit;
    // End of variables declaration//GEN-END:variables
}
