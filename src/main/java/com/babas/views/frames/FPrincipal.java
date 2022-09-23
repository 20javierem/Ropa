package com.babas.views.frames;

import com.babas.App;
import com.babas.controllers.*;
import com.babas.custom.CToggleButton;
import com.babas.custom.CustomPane;
import com.babas.custom.FondoPanel;
import com.babas.custom.TabbedPane;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Babas;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DCompany;
import com.babas.views.dialogs.DSettings;
import com.babas.views.dialogs.DTransfersOnWait;
import com.babas.views.menus.*;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class FPrincipal extends JFrame{
    private JPanel contentPane;
    private JMenu btnMenuInicio;
    private JButton btnNewSale;
    private JButton btnHistorialVentas;
    private JButton btnNuevoInventario;
    private JButton btnHistorialTransfers;
    private JButton btnActualizar;
    private JLabel lblCentro;
    private JLabel lblDerecha;
    private JLabel lblSucursal;
    private JSplitPane splitPane;
    private JPanel panelControles;
    private FlatToggleButton btnInventary;
    private FlatToggleButton btnSales;
    private JButton btnExit;
    private FlatToggleButton btnAlmacen;
    private FlatToggleButton btnManagement;
    private FlatToggleButton btnAjustes;
    private FlatToggleButton btnAdministrador;
    private JPanel panelMenus;
    private JLabel lblUsuario;
    private TabbedPane tabbedPane;
    private JPanel paneNotify;
    private JLabel lblNotify;
    private JPanel cPane;
    private FlatToggleButton btnTraslades;
    private FlatToggleButton btnReserves;
    private FlatToggleButton btnRentals;
    private Propiedades propiedades;
    private MenuSales menuSales;
    private MenuAlmacen menuAlmacen;
    private MenuManage menuManage;
    private MenuTraslade menuTraslade;
    private MenuReserves menuReserves;
    private MenuRentals menuRentals;
    public static Vector<Branch> branchs;
    public static Vector<Branch> branchesWithAll;
    public static Vector<User> users;
    public static Vector<User> usersWithAll;
    public static Vector<Product> products;
    public static Vector<Category> categories;
    public static Vector<Category> categoriesWithAll;
    public static Vector<Color> colors;
    public static Vector<Stade> stades;
    public static Vector<Stade> stadesWithAll;
    public static Vector<Dimention> dimentions;
    public static Vector<Dimention> dimentionsWithAll;
    public static Vector<Color> colorsWithAll;
    public static Vector<Brand> brands;
    public static Vector<Brand> brandsWithAll;
    public static Vector<Size> sizes;
    public static Vector<Size> sizesWithAll;
    public static Vector<Sex> sexs;
    public static Vector<Sex> sexsWithAll;
    public static Vector<Style> styles;
    public static List<Transfer> transfersOnWait=new ArrayList<>();
    public static List<Transfer> transfers;

    public FPrincipal(){
        init();
        btnSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuSales();
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        btnNewSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSales.loadNewSale();
            }
        });
        btnHistorialVentas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSales.loadRecordSales();
            }
        });
        paneNotify.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadDialogTransfers();
            }
        });
        btnAlmacen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuAlmacen();
            }
        });
        btnManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuManage();
            }
        });
        btnTraslades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuTransfer();
            }
        });
        btnReserves.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuReserves();
            }
        });
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        btnHistorialTransfers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuTraslade.loadRecordTraslades();
            }
        });
        btnRentals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuRentals();
            }
        });
    }
    private void reloadData(){
        btnActualizar.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        branchs.forEach(Babas::refresh);
        users.forEach(Babas::refresh);
        products.forEach(Babas::refresh);
        categories.forEach(Babas::refresh);
        colors.forEach(Babas::refresh);
        brands.forEach(Babas::refresh);
        sizes.forEach(Babas::refresh);;
        sexs.forEach(Babas::refresh);
        styles.forEach(Babas::refresh);
        btnActualizar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        tabbedPane.updateTab();
        loadTransferOnWait();
    }
    private void loadDialogTransfers(){
        if(!transfersOnWait.isEmpty()){
            DTransfersOnWait dTransfersOnWait=new DTransfersOnWait(transfersOnWait);
            dTransfersOnWait.setVisible(true);
        }
    }
    private void exit(){
        FLogin fLogin =new FLogin();
        dispose();
        fLogin.setVisible(true);
    }
    private void loadMenuItems(){
        JMenuItem menuSettings=new JMenuItem("Configuraciones");
        JMenuItem menuCompany=new JMenuItem("Compañia");
        menuSettings.setIcon(new ImageIcon(App.class.getResource("Icons/x16/settings.png")));
        menuCompany.setIcon(new ImageIcon(App.class.getResource("Icons/x16/settings.png")));
        menuCompany.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCompany();
            }
        });
        menuSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSettings();
            }
        });
        btnMenuInicio.add(menuSettings);
        btnMenuInicio.add(menuCompany);
    }
    private void loadSettings(){
        DSettings dSettings=new DSettings(this);
        dSettings.setVisible(true);
    }
    private void loadCompany(){
        DCompany dCompany=new DCompany();
        dCompany.setVisible(true);
    }
    private void loadMenuAlmacen() {
        splitPane.setRightComponent(menuAlmacen.getContentPane());
    }
    private void loadMenuSales() {
        splitPane.setRightComponent(menuSales.getContentPane());
    }
    private void loadMenuManage() {
        splitPane.setRightComponent(menuManage.getContentPane());
    }
    private void loadMenuTransfer() {
        splitPane.setRightComponent(menuTraslade.getContentPane());
    }
    private void loadMenuReserves(){
        splitPane.setRightComponent(menuReserves.getContentPane());
    }
    private void loadMenuRentals(){
        splitPane.setRightComponent(menuRentals.getContentPane());
    }
    private void init(){
        setContentPane(contentPane);
        setTitle("Software-Tienda");
        Utilities.setJFrame(this);
        Utilities.setTabbedPane(tabbedPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cargarCursores();
        pack();
        setLocationRelativeTo(null);
        loadLists();
        lblUsuario.setText(Babas.user.getFirstName());
        menuSales=new MenuSales(tabbedPane);
        menuAlmacen=new MenuAlmacen(tabbedPane);
        menuManage=new MenuManage(tabbedPane);
        menuTraslade=new MenuTraslade(tabbedPane);
        menuReserves=new MenuReserves(tabbedPane);
        menuRentals=new MenuRentals(tabbedPane);
        setExtendedState(MAXIMIZED_BOTH);
        loadMenuItems();
        loadMenuSales();
        menuSales.loadNewSale();
        loadTransferOnWait();
    }

    public void loadTransferOnWait(){
        transfersOnWait.clear();
        final int[] count = {0};
        Babas.user.getBranchs().forEach(branch -> branch.getTransfers().forEach(transfer -> {
            if(Objects.equals(transfer.getDestiny().getId(), branch.getId())){
                if(transfer.getState()==0){
                    transfersOnWait.add(transfer);
                    count[0]++;
                }
            }
        }));
        if(count[0]>0){
            lblNotify.setVisible(true);
            if(count[0]>9){
                lblNotify.setText("+9");
            }else{
                lblNotify.setText(String.valueOf(count[0]));
            }
        }else{
            lblNotify.setVisible(false);
        }
    }
    private void loadLists(){
        users=Users.getActives();
        branchs=Branchs.getActives();
        stades=Stades.getActives();
        stadesWithAll=new Vector<>(stades);
        stadesWithAll.add(0,new Stade("TODAS"));
        dimentions=Dimentions.getActives();
        dimentionsWithAll=new Vector<>(dimentions);
        dimentionsWithAll.add(0,new Dimention("TODAS"));
        branchesWithAll=new Vector<>(branchs);
        branchesWithAll.add(0,new Branch("TODAS"));
        categories= Categorys.getActives();
        categoriesWithAll=new Vector<>(categories);
        categoriesWithAll.add(0,new Category("TODAS"));
        colors= Colors.getActives();
        colorsWithAll=new Vector<>(colors);
        colorsWithAll.add(0,new Color("TODOS"));
        sizes= Sizes.getActives();
        sizesWithAll=new Vector<>(sizes);
        sizesWithAll.add(0,new Size("TODAS"));
        brands=Brands.getActives();
        brandsWithAll=new Vector<>(brands);
        brandsWithAll.add(0,new Brand("TODAS"));
        sexs= Sexs.getActives();
        sexsWithAll=new Vector<>(sexs);
        sexsWithAll.add(0,new Sex("TODOS"));
        products= Products.getActives();
        styles=Styles.getTodos();
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.DATE,1);
        transfers=Transfers.getAfter(calendar.getTime());
    }
    private void cargarCursores(){
        btnAjustes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInventary.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSales.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAlmacen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdministrador.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewSale.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevoInventario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistorialTransfers.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistorialVentas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTraslades.setCursor(new Cursor(Cursor.HAND_CURSOR));
        paneNotify.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReserves.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRentals.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        paneNotify=new FondoPanel("icons/x32/notificacion.png",new Dimension(32,32));
        panelControles=new CustomPane(2);
        panelControles.updateUI();
        cPane=new CustomPane(2);
        cPane.updateUI();
        btnSales=new CToggleButton();
        btnInventary=new CToggleButton();
        btnAlmacen=new CToggleButton();
        btnManagement =new CToggleButton();
        btnAjustes=new CToggleButton();
        btnAdministrador=new CToggleButton();
        btnTraslades=new CToggleButton();
        btnReserves=new CToggleButton();
        btnRentals =new CToggleButton();
    }
}
