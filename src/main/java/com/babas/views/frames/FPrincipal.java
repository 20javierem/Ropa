package com.babas.views.frames;

import com.babas.App;
import com.babas.controllers.Categorys;
import com.babas.controllers.Colors;
import com.babas.controllers.Products;
import com.babas.controllers.Sizes;
import com.babas.custom.CToggleButton;
import com.babas.custom.CustomPane;
import com.babas.custom.FondoPanel;
import com.babas.custom.TabbedPane;
import com.babas.models.Category;
import com.babas.models.Color;
import com.babas.models.Product;
import com.babas.models.Size;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DCompany;
import com.babas.views.dialogs.DSettings;
import com.babas.views.menus.MenuAlmacen;
import com.babas.views.menus.MenuInventory;
import com.babas.views.menus.MenuManage;
import com.babas.views.menus.MenuSales;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

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
    private FlatToggleButton btnGestionar;
    private FlatToggleButton btnAjustes;
    private FlatToggleButton btnAdministrador;
    private JPanel panelMenus;
    private JLabel lblUsuario;
    private TabbedPane tabbedPane;
    private JPanel paneNotify;
    private JLabel lblNotify;
    private JPanel cPane;
    private Propiedades propiedades;
    private MenuSales menuSales;
    private MenuInventory menuInventory;
    private MenuAlmacen menuAlmacen;
    private MenuManage menuManage;
    public static Vector<Product> products;
    public static Vector<Category> categories;
    public static Vector<Color> colors;
    public static Vector<Size> sizes;

    public FPrincipal(){
        initComponents();
        btnSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuSales();
            }
        });
        btnInventary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuInventory();
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
                lblNotify.setVisible(!lblNotify.isVisible());
            }
        });
        btnAlmacen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuAlmacen();
            }
        });
        btnGestionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuManage();
            }
        });
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
    private void loadMenuInventory() {
        splitPane.setRightComponent(menuInventory.getContentPane());
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
    private void initComponents(){
        setContentPane(contentPane);
        setTitle("Software-Tienda");
        Utilities.setJFrame(this);
        Utilities.setTabbedPane(tabbedPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cargarCursores();
        pack();
        setLocationRelativeTo(null);
        loadLists();
        menuSales=new MenuSales(tabbedPane);
        menuInventory=new MenuInventory(tabbedPane);
        menuAlmacen=new MenuAlmacen(tabbedPane);
        menuManage=new MenuManage(tabbedPane);
        setExtendedState(MAXIMIZED_BOTH);
        loadMenuItems();
        loadMenuSales();
        menuSales.loadNewSale();
    }

    private void loadLists(){
        categories= Categorys.getActives();
        colors= Colors.getActives();
        sizes= Sizes.getActives();
        products= Products.getActives();
    }
    private void cargarCursores(){
        btnAjustes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInventary.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSales.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAlmacen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGestionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdministrador.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewSale.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevoInventario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistorialTransfers.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistorialVentas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        paneNotify.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        paneNotify=new FondoPanel("Icons/x32/notificacion.png",new Dimension(32,32));
        panelControles=new CustomPane(2);
        panelControles.updateUI();
        cPane=new CustomPane(2);
        cPane.updateUI();
        btnSales=new CToggleButton();
        btnInventary=new CToggleButton();
        btnAlmacen=new CToggleButton();
        btnGestionar=new CToggleButton();
        btnAjustes=new CToggleButton();
        btnAdministrador=new CToggleButton();
    }
}
