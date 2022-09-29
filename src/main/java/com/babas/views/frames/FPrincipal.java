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
import com.babas.views.dialogs.DBoxSesion;
import com.babas.views.dialogs.DCompany;
import com.babas.views.dialogs.DSettings;
import com.babas.views.dialogs.DTransfersOnWait;
import com.babas.views.menus.*;
import com.babas.views.tabs.TabBoxSesion;
import com.formdev.flatlaf.extras.components.FlatToggleButton;
import com.moreno.Notify;

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
    private JLabel lblCenter;
    private JLabel lblRigth;
    private JLabel lblLeft;
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
    private JMenu btnMenuBox;
    private Propiedades propiedades;
    private MenuSales menuSales;
    private MenuAlmacen menuAlmacen;
    private MenuManage menuManage;
    private MenuTraslade menuTraslade;
    private MenuReserves menuReserves;
    private MenuRentals menuRentals;
    public static Vector<Branch> branchs=new Vector<>();
    public static Vector<Branch> branchesWithAll=new Vector<>();
    public static Vector<User> users=Users.getActives();
    public static Vector<User> usersWithAll=new Vector<>();
    public static Vector<Product> products=new Vector<>();
    public static Vector<Category> categories=new Vector<>();
    public static Vector<Category> categoriesWithAll=new Vector<>();
    public static Vector<Color> colors=new Vector<>();
    public static Vector<Stade> stades=new Vector<>();
    public static Vector<Stade> stadesWithAll=new Vector<>();
    public static Vector<Dimention> dimentions=new Vector<>();
    public static Vector<Dimention> dimentionsWithAll=new Vector<>();
    public static Vector<Color> colorsWithAll=new Vector<>();
    public static Vector<Brand> brands=new Vector<>();
    public static Vector<Brand> brandsWithAll=new Vector<>();
    public static Vector<Size> sizes=new Vector<>();
    public static Vector<Size> sizesWithAll=new Vector<>();
    public static Vector<Sex> sexs=new Vector<>();
    public static Vector<Sex> sexsWithAll=new Vector<>();
    public static Vector<Style> styles=new Vector<>();
    public static List<Transfer> transfersOnWait=new ArrayList<>();

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
        JMenuItem menuBox=new JMenuItem("Apertura/Cierre de caja");
        JMenuItem menuShowBox=new JMenuItem("Ver movimientos de caja");
        menuSettings.setIcon(new ImageIcon(App.class.getResource("icons/x16/settings.png")));
        menuCompany.setIcon(new ImageIcon(App.class.getResource("icons/x16/settings.png")));
        menuBox.setIcon(new ImageIcon(App.class.getResource("icons/x16/caja-registradora.png")));
        menuShowBox.setIcon(new ImageIcon(App.class.getResource("icons/x16/caja-registradora.png")));
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
        menuBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBox();
            }
        });
        menuShowBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBoxSesion();
            }
        });
        btnMenuInicio.add(menuSettings);
        btnMenuInicio.add(menuCompany);
        btnMenuBox.add(menuBox);
        btnMenuBox.add(menuShowBox);
    }
    private void loadBoxSesion(){
        if(Babas.boxSession.getId()!=null){
            if(tabbedPane.indexOfTab("Caja")==-1){
                TabBoxSesion tabBoxSesion=new TabBoxSesion();
                tabbedPane.addTab(tabBoxSesion.getTabPane().getTitle(),tabBoxSesion.getTabPane());
            }
            tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Caja"));
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"Mensaje","No aperturó una caja");
        }
    }
    private void loadBox(){
        DBoxSesion dBoxSesion =new DBoxSesion();
        dBoxSesion.setVisible(true);
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
        Utilities.setLblIzquierda(lblLeft);
        Utilities.setLblCentro(lblCenter);
        Utilities.setLblDerecha(lblRigth);
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
