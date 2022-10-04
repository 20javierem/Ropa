package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Rental;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.DetailRentalCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailRentalAbstractModel;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class TabFinishRental {
    private TabPane tabPane;
    private FlatTable table;
    private FlatTextField txtDocument;
    private FlatTextField txtNameClient;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private JDateChooser jDateFinish;
    private FlatSpinner spinnerPenalty;
    private JLabel lblSubTotal;
    private JLabel lblDiscount;
    private JLabel lblWarranty;
    private JLabel lblTotal;
    private JLabel lblTotalCurrent;
    private JButton btnSaleWithTrasnfer;
    private JButton btnSaleWithCash;
    private JLabel lblLogo;
    private JLabel lblPenalty;
    private JLabel lblTotalWithPenalty;
    private JLabel lblWarranty2;
    private Rental rental;
    private DetailRentalAbstractModel model;

    public TabFinishRental(Rental rental){
        this.rental=rental;
        init();
    }
    private void init(){
        tabPane.setTitle("Finalización alquiler Nro. "+rental.getNumberRental());
        load();
        loadTotals();
    }

    private void load(){
        model=new DetailRentalAbstractModel(rental.getDetailRentals());
        table.setModel(model);
        DetailRentalCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        txtDocument.setText(rental.getClient().getDni());
        txtNameClient.setText(rental.getClient().getNames());
        txtPhone.setText(rental.getClient().getPhone());
        txtMail.setText(rental.getClient().getMail());
    }

    private void loadTotals(){
        rental.calculateTotal();
        lblSubTotal.setText(Utilities.moneda.format(rental.getTotal()));
        lblWarranty.setText(Utilities.moneda.format(rental.getWarranty()));
        lblWarranty2.setText(Utilities.moneda.format(rental.getWarranty()));
        lblTotal.setText(Utilities.moneda.format(rental.getTotalCurrent()+rental.getDiscount()));
        lblDiscount.setText(Utilities.moneda.format(rental.getDiscount()));
        lblTotalCurrent.setText(Utilities.moneda.format(rental.getTotalCurrent()));
        table.removeColumn(table.getColumn(""));
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerPenalty=new FlatSpinner();
        spinnerPenalty.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerPenalty.setEditor(Utilities.getEditorPrice(spinnerPenalty));
        jDateFinish=new JDateChooser(new Date());
        jDateFinish.setDateFormatString(Utilities.getFormatoFecha());
        jDateFinish.setEnabled(false);
    }
}
