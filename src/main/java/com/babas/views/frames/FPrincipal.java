package com.babas.views.frames;

import com.babas.App;
import com.babas.controllers.*;
import com.babas.custom.*;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.views.dialogs.*;
import com.babas.views.menus.*;
import com.babas.views.tabs.TabBoxSesion;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FPrincipal extends JFrame {
    private JPanel contentPane;
    private JMenu btnMenuStart;
    private JButton btnNewSale;
    private JButton btnCatalogue;
    private JButton btnActualizar;
    private JLabel lblCenter;
    private JLabel lblRigth;
    private JLabel lblLeft;
    private JSplitPane splitPane;
    private JPanel panelControles;
    private FlatToggleButton btnInventary;
    private FlatToggleButton btnSales;
    private FlatButton btnExit;
    private FlatToggleButton btnManagement;
    private FlatToggleButton btnAjustes;
    private FlatToggleButton btnAdministrador;
    private JPanel panelMenus;
    private JLabel lblUsuario;
    private TabbedPane tabbedPane;
    private JPanel cPane;
    private FlatToggleButton btnTraslades;
    private FlatToggleButton btnReserves;
    private FlatToggleButton btnRentals;
    private JMenu btnMenuBox;
    private JButton btnNewReserve;
    private JButton btnNewRental;
    private FlatToggleButton btnBoxes;
    private FlatButton btnSettings;
    private FlatButton btnNotify;
    private JButton btnSearchComprobante;
    private JMenu btnMenuSales;
    private JMenu btnMenuRentals;
    private JMenu btnMenuReserves;
    private JMenu btnMenuTransfers;
    private JMenu btnMenuManages;
    private JMenuBar menuBar;
    private FlatToggleButton btnQuotations;
    private JButton btnNewQuotation;
    private JButton btnSearchClient;
    private JMenu btnMenuQuotations;
    private JLabel lblLogo;
    private MenuSales menuSales;
    private MenuManage menuManage;
    private MenuTraslade menuTraslade;
    private MenuReserves menuReserves;
    private MenuRentals menuRentals;
    private MenuBoxes menuBoxes;
    private MenuQuotations menuQuotations;
    public static Vector<Branch> branchs = new Vector<>();
    public static Vector<Branch> branchesWithAll = new Vector<>();
    public static Vector<User> users = new Vector<>();
    public static Vector<Client> clients = new Vector<>();
    public static Vector<Product> products = new Vector<>();
    public static Vector<Category> categories = new Vector<>();
    public static Vector<Category> categoriesWithAll = new Vector<>();
    public static Vector<Color> colors = new Vector<>();
    public static Vector<Stade> stades = new Vector<>();
    public static Vector<Stade> stadesWithAll = new Vector<>();
    public static Vector<Dimention> dimentions = new Vector<>();
    public static Vector<Dimention> dimentionsWithAll = new Vector<>();
    public static Vector<Color> colorsWithAll = new Vector<>();
    public static Vector<Brand> brands = new Vector<>();
    public static Vector<Brand> brandsWithAll = new Vector<>();
    public static Vector<Size> sizes = new Vector<>();
    public static Vector<Size> sizesWithAll = new Vector<>();
    public static Vector<Sex> sexs = new Vector<>();
    public static Vector<Sex> sexsWithAll = new Vector<>();
    public static Vector<Style> styles = new Vector<>();
    public static List<Transfer> transfersOnWait = new ArrayList<>();
    public static List<Rental> rentalsActives = new ArrayList<>();
    public static List<Reserve> reservesActives = new ArrayList<>();
    public static Vector<Permission> groupPermnitions = new Vector<>();

    private JPopupMenu pop_up = new JPopupMenu();

    public FPrincipal() {
        $$$setupUI$$$();
        init();
        btnSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuSales();
            }
        });
        btnNewSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSales.loadNewSale(true, new Sale());
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
        btnRentals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuRentals();
            }
        });
        btnCatalogue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSales.loadCatalogue();
            }
        });
        btnNewRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuRentals.loadNewRental(new Rental());
            }
        });
        btnNewReserve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuReserves.loadNewReserve(new Reserve());
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitSession();
            }
        });
        btnBoxes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuBoxes();
            }
        });
        btnSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSettingUser();
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pop_up.show(btnExit, -btnExit.getX(), btnExit.getVisibleRect().y - 58);
            }
        });
        btnNotify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDialogTransfers();
            }
        });
        btnSearchComprobante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getComprobante();
            }
        });
        btnQuotations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuQuotations();
            }
        });
        btnNewQuotation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuQuotations.loadNewQuotation();
            }
        });
        btnSearchClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getClient();
            }
        });
    }

    private void getClient() {
        FlatTextField flatTextField = new FlatTextField();
        flatTextField.setPlaceholderText("DNI/RUC...");
        int option = JOptionPane.showOptionDialog(this, flatTextField, "Buscar cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Buscar", "Cancelar"}, "Buscar");
        if (option == JOptionPane.OK_OPTION) {
            boolean find = false;
            Client client = Clients.getByDNI(flatTextField.getText().trim());
            if (client != null) {
                DClient dClient = new DClient(true, client);
                dClient.setVisible(true);
                find = true;
            }
            if (!find) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "No se encontró el cliente");
                getClient();
            }
        }
    }

    private void getComprobante() {
        JPanel jPanel = new JPanel();
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("VENTA");
        comboBox.addItem("ALQUILER");
        comboBox.addItem("RESERVA");
        comboBox.addItem("COTIZACIÓN");
        SpinnerNumberModel sModel = new SpinnerNumberModel(0, 0, 99999, 1);
        JSpinner spinner = new JSpinner(sModel);
        jPanel.add(comboBox);
        jPanel.add(spinner);
        int option = JOptionPane.showOptionDialog(this, jPanel, "Buscar comprobante", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Buscar", "Cancelar"}, "Buscar");
        if (option == JOptionPane.OK_OPTION) {
            Long number = Long.valueOf((Integer) spinner.getValue());
            boolean find = false;
            switch (comboBox.getSelectedItem().toString()) {
                case "VENTA":
                    Sale sale = Sales.getByNumber(number);
                    if (sale != null) {
                        int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
                        if (index == 0) {
                            UtilitiesReports.generateTicketSale(true, sale, false);
                        } else if (index == 1) {
                            UtilitiesReports.generateTicketSale(false, sale, false);
                        }
                        find = true;
                    }
                    break;
                case "ALQUILER":
                    Rental rental = Rentals.getByNumber(number);
                    if (rental != null) {
                        if (rental.isActive()) {
                            int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
                            if (index == 0) {
                                UtilitiesReports.generateTicketRental(true, rental, false);
                            } else if (index == 1) {
                                UtilitiesReports.generateTicketRental(false, rental, false);
                            }
                        } else {
                            int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
                            if (index == 0) {
                                UtilitiesReports.generateTicketRentalFinish(true, rental, false);
                            } else if (index == 1) {
                                UtilitiesReports.generateTicketRentalFinish(false, rental, false);
                            }
                        }
                        find = true;
                    }
                    break;
                case "RESERVA":
                    Reserve reserve = Reserves.getByNumber(number);
                    if (reserve != null) {
                        int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
                        if (index == 0) {
                            UtilitiesReports.generateTicketReserve(true, reserve, false);
                        } else if (index == 1) {
                            UtilitiesReports.generateTicketReserve(false, reserve, false);
                        }
                        find = true;
                    }
                    break;
                case "COTIZACIÓN":
                    Quotation quotation = Quotations.getByNumber(number);
                    if (quotation != null) {
                        int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
                        if (index == 0) {
                            UtilitiesReports.generateTicketQuotation(true, quotation, false);
                        } else if (index == 1) {
                            UtilitiesReports.generateTicketQuotation(false, quotation, false);
                        }
                        find = true;
                    }
                    break;
            }
            if (!find) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "No se encontró el comprobante");
                getComprobante();
            }
        }
    }

    private void loadSettingUser() {
        DUser dUser = new DUser(true, Babas.user);
        dUser.setVisible(true);
    }

    private void reloadData() {
        Utilities.consult = true;
        Babas.user.getPermitions().refresh();
        btnActualizar.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        branchs.forEach(Babas::refresh);
        branchs.forEach(branch -> branch.getStocks().forEach(Babas::refresh));
        users.forEach(Babas::refresh);
        products.forEach(Babas::refresh);
        categories.forEach(Babas::refresh);
        colors.forEach(Babas::refresh);
        brands.forEach(Babas::refresh);
        sizes.forEach(Babas::refresh);
        sexs.forEach(Babas::refresh);
        styles.forEach(Babas::refresh);
        tabbedPane.updateTab();
        loadTransferOnWait();
        groupPermnitions.forEach(Babas::refresh);
        products.forEach(product -> product.getIconsx200(true));
        products.forEach(product -> product.getIconsx400(true));
        loadPermisses();
        Babas.company.refresh();
        if (Babas.company.getLogo() != null) {
            Utilities.downloadLogo(Babas.company.getLogo());
        }
        btnActualizar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void loadDialogTransfers() {
        if (!transfersOnWait.isEmpty()) {
            DTransfersOnWait dTransfersOnWait = new DTransfersOnWait(transfersOnWait);
            dTransfersOnWait.setVisible(true);
        }
    }

    private void exitSession() {
        boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Cerrar sesión", JOptionPane.YES_NO_OPTION) == 0;
        if (si) {
            FLogin fLogin = new FLogin();
            this.dispose();
            fLogin.setVisible(true);
        }
    }

    private void suspendSession() {
        setVisible(false);
        FLogin fLogin = new FLogin(this);
        fLogin.setVisible(true);
    }

    private void loadMenuItems() {
        JMenuItem menuSettings = new JMenuItem("Configuraciones", new ImageIcon(App.class.getResource("icons/x16/settings.png")));
        menuSettings.addActionListener(e -> loadSettings());
        JMenuItem menuCompany = new JMenuItem("Compañia");
        menuCompany.addActionListener(e -> loadCompany());
        btnMenuStart.setMnemonic(KeyEvent.VK_I);
        menuSettings.setMnemonic(KeyEvent.VK_C);
        menuCompany.setMnemonic(KeyEvent.VK_M);
        btnMenuStart.add(menuSettings);
        btnMenuStart.add(menuCompany);

        JMenuItem menuBox = new JMenuItem("Apertura/Cierre de caja");
        menuBox.addActionListener(e -> loadBox());
        JMenuItem menuShowBox = new JMenuItem("Ver movimientos de caja");
        menuShowBox.addActionListener(e -> loadBoxSesion());
        JMenuItem menuRecordBoxes = new JMenuItem("Historial de cajas");
        menuRecordBoxes.addActionListener(e -> menuBoxes.loadRecordBoxSessions());
        btnMenuBox.setMnemonic(KeyEvent.VK_C);
        menuBox.setMnemonic(KeyEvent.VK_A);
        menuShowBox.setMnemonic(KeyEvent.VK_V);
        menuRecordBoxes.setMnemonic(KeyEvent.VK_H);
        btnMenuBox.add(menuBox);
        btnMenuBox.add(menuShowBox);
        btnMenuBox.add(menuRecordBoxes);

        JMenuItem menuNewSale = new JMenuItem("Nueva venta");
        menuNewSale.addActionListener(e -> menuSales.loadNewSale(true, new Sale()));
        JMenuItem menuCatalogue = new JMenuItem("Catálogo");
        menuCatalogue.addActionListener(e -> menuSales.loadCatalogue());
        JMenuItem menuRecordSales = new JMenuItem("Historial de ventas");
        menuRecordSales.addActionListener(e -> menuSales.loadCatalogue());
        btnMenuSales.setMnemonic(KeyEvent.VK_V);
        menuNewSale.setMnemonic(KeyEvent.VK_N);
        menuCatalogue.setMnemonic(KeyEvent.VK_C);
        menuRecordSales.setMnemonic(KeyEvent.VK_H);
        btnMenuSales.add(menuNewSale);
        btnMenuSales.add(menuCatalogue);
        btnMenuSales.add(menuRecordSales);


        JMenuItem menuNewRental = new JMenuItem("Nuevo alquiler");
        menuNewRental.addActionListener(e -> menuRentals.loadNewRental(new Rental()));
        JMenuItem menuRentalsActives = new JMenuItem("Alquileres activos");
        menuRentalsActives.addActionListener(e -> menuRentals.loadRentalsActives());
        JMenuItem menuRecordRentals = new JMenuItem("Historial de alquileres");
        menuRecordRentals.addActionListener(e -> menuRentals.loadRecordRentals());
        btnMenuRentals.setMnemonic(KeyEvent.VK_A);
        menuNewRental.setMnemonic(KeyEvent.VK_N);
        menuRentalsActives.setMnemonic(KeyEvent.VK_A);
        menuRecordRentals.setMnemonic(KeyEvent.VK_H);
        btnMenuRentals.add(menuNewRental);
        btnMenuRentals.add(menuRentalsActives);
        btnMenuRentals.add(menuRecordRentals);


        JMenuItem menuNewReserve = new JMenuItem("Nueva reserva");
        menuNewReserve.addActionListener(e -> menuReserves.loadNewReserve(new Reserve()));
        JMenuItem menuReservesActives = new JMenuItem("Reservas activas");
        menuReservesActives.addActionListener(e -> menuReserves.loadReservesActives());
        JMenuItem menuRecordReserves = new JMenuItem("Historial de reservas");
        menuRecordReserves.addActionListener(e -> menuReserves.loadRecordRentals());
        btnMenuReserves.setMnemonic(KeyEvent.VK_R);
        menuNewReserve.setMnemonic(KeyEvent.VK_N);
        menuReservesActives.setMnemonic(KeyEvent.VK_R);
        menuRecordReserves.setMnemonic(KeyEvent.VK_H);
        btnMenuReserves.add(menuNewReserve);
        btnMenuReserves.add(menuReservesActives);
        btnMenuReserves.add(menuRecordReserves);


        JMenuItem menuNewQuotation = new JMenuItem("Nueva cotización");
        menuNewQuotation.addActionListener(e -> menuQuotations.loadNewQuotation());
        JMenuItem menuRecorQuotations = new JMenuItem("Historial de cotizaciones");
        menuRecorQuotations.addActionListener(e -> menuQuotations.loadRecordQuotations());
        btnMenuQuotations.setMnemonic(KeyEvent.VK_O);
        menuNewQuotation.setMnemonic(KeyEvent.VK_N);
        menuRecorQuotations.setMnemonic(KeyEvent.VK_H);
        btnMenuQuotations.add(menuNewQuotation);
        btnMenuQuotations.add(menuRecorQuotations);


        JMenuItem menuNewTransfer = new JMenuItem("Nuevo traslado");
        menuNewTransfer.addActionListener(e -> menuTraslade.loadNewTraslade());
        JMenuItem menuRecordTransfers = new JMenuItem("Historial de traslados");
        menuRecordTransfers.addActionListener(e -> menuTraslade.loadRecordTraslades());
        btnMenuTransfers.setMnemonic(KeyEvent.VK_T);
        menuNewTransfer.setMnemonic(KeyEvent.VK_N);
        menuRecordTransfers.setMnemonic(KeyEvent.VK_H);
        btnMenuTransfers.add(menuNewTransfer);
        btnMenuTransfers.add(menuRecordTransfers);


        JMenuItem menuProducts = new JMenuItem("Productos");
        menuProducts.addActionListener(e -> menuManage.loadProducts());
        JMenuItem menuUsers = new JMenuItem("Usuarios");
        menuUsers.addActionListener(e -> menuManage.loadUsers());
        JMenuItem menuBranchs = new JMenuItem("Sucursales");
        menuBranchs.addActionListener(e -> menuManage.loadBranchs());
        btnMenuManages.setMnemonic(KeyEvent.VK_G);
        menuProducts.setMnemonic(KeyEvent.VK_P);
        menuUsers.setMnemonic(KeyEvent.VK_U);
        menuBranchs.setMnemonic(KeyEvent.VK_S);
        btnMenuManages.add(menuProducts);
        btnMenuManages.add(menuUsers);
        btnMenuManages.add(menuBranchs);

        if (Babas.user.isGroupDefault()) {
            menuCompany.setEnabled(Babas.user.getPermissionGroup().isManageCompany());
            menuRecordBoxes.setEnabled(Babas.user.getPermissionGroup().isRecordBoxes());
            menuNewSale.setEnabled(Babas.user.getPermissionGroup().isNewSale());
            menuCatalogue.setEnabled(Babas.user.getPermissionGroup().isShowCatalogue());
            menuRecordSales.setEnabled(Babas.user.getPermissionGroup().isRecordSales());
            menuNewRental.setEnabled(Babas.user.getPermissionGroup().isNewRental());
            menuRentalsActives.setEnabled(Babas.user.getPermissionGroup().isRentalsActives());
            menuRecordRentals.setEnabled(Babas.user.getPermissionGroup().isRecordRentals());
            menuNewReserve.setEnabled(Babas.user.getPermissionGroup().isNewReserve());
            menuReservesActives.setEnabled(Babas.user.getPermissionGroup().isReservesActives());
            menuRecordReserves.setEnabled(Babas.user.getPermissionGroup().isRecordReserves());
            menuNewQuotation.setEnabled(Babas.user.getPermissionGroup().isNewQuotation());
            menuRecorQuotations.setEnabled(Babas.user.getPermissionGroup().isRecordQuotations());
            menuNewTransfer.setEnabled(Babas.user.getPermissionGroup().isNewTransfer());
            menuRecordTransfers.setEnabled(Babas.user.getPermissionGroup().isRecordTransfers());
            menuProducts.setEnabled(Babas.user.getPermissionGroup().isManageProducts());
            menuUsers.setEnabled(Babas.user.getPermissionGroup().isManageUsers());
            menuBranchs.setEnabled(Babas.user.getPermissionGroup().isManageUsers());
        } else {
            menuCompany.setEnabled(Babas.user.getPermitions().isManageCompany());
            menuRecordBoxes.setEnabled(Babas.user.getPermitions().isRecordBoxes());
            menuNewSale.setEnabled(Babas.user.getPermitions().isNewSale());
            menuCatalogue.setEnabled(Babas.user.getPermitions().isShowCatalogue());
            menuRecordSales.setEnabled(Babas.user.getPermitions().isRecordSales());
            menuNewRental.setEnabled(Babas.user.getPermitions().isNewRental());
            menuRentalsActives.setEnabled(Babas.user.getPermitions().isRentalsActives());
            menuRecordRentals.setEnabled(Babas.user.getPermitions().isRecordRentals());
            menuNewReserve.setEnabled(Babas.user.getPermitions().isNewReserve());
            menuReservesActives.setEnabled(Babas.user.getPermitions().isReservesActives());
            menuRecordReserves.setEnabled(Babas.user.getPermitions().isRecordReserves());
            menuNewQuotation.setEnabled(Babas.user.getPermitions().isNewQuotation());
            menuRecorQuotations.setEnabled(Babas.user.getPermitions().isRecordQuotations());
            menuNewTransfer.setEnabled(Babas.user.getPermitions().isNewTransfer());
            menuRecordTransfers.setEnabled(Babas.user.getPermitions().isRecordTransfers());
            menuProducts.setEnabled(Babas.user.getPermitions().isManageProducts());
            menuUsers.setEnabled(Babas.user.getPermitions().isManageUsers());
            menuBranchs.setEnabled(Babas.user.getPermitions().isManageUsers());
        }
    }

    private void loadIcons() {
        btnActualizar.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/buildLoadChanges.svg")));
        btnSettings.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/settings.svg")));
        btnExit.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/exit.svg")));
        btnNotify.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/notification.svg")));
    }

    private void loadMenuExit() {
        JMenuItem suspendSession = new JMenuItem("Suspender sesión");
        JMenuItem closeSession = new JMenuItem("Cerrar sesión");
        suspendSession.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/moon.svg")));
        closeSession.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/exit.svg")));
        pop_up.add(suspendSession);
        pop_up.add(closeSession);
        suspendSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suspendSession();
            }
        });
        closeSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitSession();
            }
        });
    }

    private void loadBoxSesion() {
        if (Babas.boxSession.getId() != null) {
            if (tabbedPane.indexOfTab("Caja actual") == -1) {
                TabBoxSesion tabBoxSesion = new TabBoxSesion(Babas.boxSession);
                tabbedPane.addTab(tabBoxSesion.getTabPane().getTitle(), tabBoxSesion.getTabPane());
            }
            tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Caja actual"));
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "Mensaje", "No aperturó una caja");
        }
    }

    private void loadBox() {
        DBoxSesion dBoxSesion = new DBoxSesion();
        dBoxSesion.setVisible(true);
    }

    private void loadSettings() {
        DSettings dSettings = new DSettings(this);
        dSettings.setVisible(true);
    }

    private void loadCompany() {
        DCompany dCompany = new DCompany();
        dCompany.setVisible(true);
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

    private void loadMenuReserves() {
        splitPane.setRightComponent(menuReserves.getContentPane());
    }

    private void loadMenuRentals() {
        splitPane.setRightComponent(menuRentals.getContentPane());
    }

    private void loadMenuBoxes() {
        splitPane.setRightComponent(menuBoxes.getContentPane());
    }

    private void loadMenuQuotations() {
        splitPane.setRightComponent(menuQuotations.getContentPane());
    }

    private void init() {
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
        lblUsuario.setText("Usuario: " + Babas.user.getFirstName());
        menuSales = new MenuSales(tabbedPane);
        menuManage = new MenuManage(tabbedPane);
        menuTraslade = new MenuTraslade(tabbedPane);
        menuReserves = new MenuReserves(tabbedPane);
        menuRentals = new MenuRentals(tabbedPane);
        menuBoxes = new MenuBoxes(tabbedPane);
        menuQuotations = new MenuQuotations(tabbedPane);
        setExtendedState(MAXIMIZED_BOTH);
        loadPermisses();
        loadMenuItems();
        loadMenuSales();
        loadMenuExit();
        loadTransferOnWait();
        loadIcons();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public MenuSales getMenuSales() {
        return menuSales;
    }

    public MenuRentals getMenuRentals() {
        return menuRentals;
    }

    public MenuReserves getMenuReserves() {
        return menuReserves;
    }

    private void loadPermisses() {
        if (Babas.user.isGroupDefault()) {
            btnCatalogue.setEnabled(Babas.user.getPermissionGroup().isShowCatalogue());
            btnNewSale.setEnabled(Babas.user.getPermissionGroup().isNewSale());
            btnNewRental.setEnabled(Babas.user.getPermissionGroup().isNewSale());
            btnNewReserve.setEnabled(Babas.user.getPermissionGroup().isNewSale());
            btnNotify.setEnabled(Babas.user.getPermissionGroup().isAceptTransfer());
            btnNewQuotation.setEnabled(Babas.user.getPermissionGroup().isNewQuotation());
        } else {
            btnCatalogue.setEnabled(Babas.user.getPermitions().isShowCatalogue());
            btnNewSale.setEnabled(Babas.user.getPermitions().isNewSale());
            btnNewRental.setEnabled(Babas.user.getPermitions().isNewSale());
            btnNewReserve.setEnabled(Babas.user.getPermitions().isNewSale());
            btnNotify.setEnabled(Babas.user.getPermitions().isAceptTransfer());
            btnNewQuotation.setEnabled(Babas.user.getPermitions().isNewQuotation());
        }

    }

    public void loadTransferOnWait() {
        transfersOnWait.clear();
        final int[] count = {0};
        Babas.user.getBranchs().forEach(branch -> branch.getTransfers().forEach(transfer -> {
            if (Objects.equals(transfer.getDestiny().getId(), branch.getId())) {
                if (transfer.getState() == 0) {
                    transfersOnWait.add(transfer);
                    count[0]++;
                }
            }
        }));
        if (count[0] > 0) {
            btnNotify.setVisible(true);
            if (count[0] > 9) {
                btnNotify.setText("+9");
            } else {
                btnNotify.setText(String.valueOf(count[0]));
            }
        } else {
            btnNotify.setVisible(false);
        }
    }

    private void loadLists() {
        users = Users.getActives();
        clients = Clients.getTodos();
        branchs = Branchs.getActives();
        stades = Stades.getActives();
        stadesWithAll = new Vector<>(stades);
        stadesWithAll.add(0, new Stade("TODAS"));
        dimentions = Dimentions.getActives();
        dimentionsWithAll = new Vector<>(dimentions);
        dimentionsWithAll.add(0, new Dimention("TODAS"));
        branchesWithAll = new Vector<>(branchs);
        branchesWithAll.add(0, new Branch("TODAS"));
        categories = Categorys.getActives();
        categoriesWithAll = new Vector<>(categories);
        categoriesWithAll.add(0, new Category("TODAS"));
        colors = Colors.getActives();
        colorsWithAll = new Vector<>(colors);
        colorsWithAll.add(0, new Color("TODOS"));
        sizes = Sizes.getActives();
        sizesWithAll = new Vector<>(sizes);
        sizesWithAll.add(0, new Size("TODAS"));
        brands = Brands.getActives();
        brandsWithAll = new Vector<>(brands);
        brandsWithAll.add(0, new Brand("TODAS"));
        sexs = Sexs.getActives();
        sexsWithAll = new Vector<>(sexs);
        sexsWithAll.add(0, new Sex("TODOS"));
        products = Products.getActives();
        products.forEach(product -> product.getIconsx200(true));
        products.forEach(product -> product.getIconsx400(true));
        styles = Styles.getTodos();
        groupPermnitions = Permissions.getGroups();
        Babas.user.getBranchs().forEach(branch -> rentalsActives.addAll(Rentals.getActives(branch)));
        Babas.user.getBranchs().forEach(branch -> reservesActives.addAll(Reserves.getActives(branch)));
    }

    private void cargarCursores() {
        btnAjustes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInventary.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSales.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdministrador.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewSale.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCatalogue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTraslades.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReserves.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRentals.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewRental.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewReserve.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBoxes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSettings.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNotify.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearchComprobante.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuotations.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewQuotation.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearchClient.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panelControles = new CustomPane(2);
        panelControles.updateUI();
        cPane = new CustomPane(2);
        cPane.updateUI();
        btnSales = new CToggleButton();
        btnInventary = new CToggleButton();
        btnManagement = new CToggleButton();
        btnAjustes = new CToggleButton();
        btnAdministrador = new CToggleButton();
        btnTraslades = new CToggleButton();
        btnReserves = new CToggleButton();
        btnRentals = new CToggleButton();
        btnBoxes = new CToggleButton();
        btnQuotations = new CToggleButton();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), 0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, 0));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        menuBar = new JMenuBar();
        menuBar.setLayout(new GridLayoutManager(1, 9, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(menuBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        menuBar.add(spacer1, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnMenuBox = new JMenu();
        btnMenuBox.setText("Caja");
        menuBar.add(btnMenuBox, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnMenuManages = new JMenu();
        btnMenuManages.setText("Gestionar");
        menuBar.add(btnMenuManages, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnMenuTransfers = new JMenu();
        btnMenuTransfers.setText("Traslados");
        menuBar.add(btnMenuTransfers, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnMenuReserves = new JMenu();
        btnMenuReserves.setText("Reservas");
        menuBar.add(btnMenuReserves, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnMenuRentals = new JMenu();
        btnMenuRentals.setText("Alquileres");
        menuBar.add(btnMenuRentals, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnMenuSales = new JMenu();
        btnMenuSales.setText("Ventas");
        menuBar.add(btnMenuSales, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnMenuStart = new JMenu();
        btnMenuStart.setText("Inicio");
        menuBar.add(btnMenuStart, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnMenuQuotations = new JMenu();
        btnMenuQuotations.setText("Cotizaciones");
        menuBar.add(btnMenuQuotations, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setFloatable(false);
        toolBar1.setMinimumSize(new Dimension(1582, 27));
        toolBar1.setPreferredSize(new Dimension(1582, 27));
        panel2.add(toolBar1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 38), null, 0, false));
        btnCatalogue = new JButton();
        btnCatalogue.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/catalogue.png")));
        btnCatalogue.setText("Catálogo");
        toolBar1.add(btnCatalogue);
        btnNewSale = new JButton();
        btnNewSale.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/nuevaVenta.png")));
        btnNewSale.setText("Nueva venta");
        toolBar1.add(btnNewSale);
        btnNewRental = new JButton();
        btnNewRental.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveSelected.png")));
        btnNewRental.setText("Nuevo alquiler");
        toolBar1.add(btnNewRental);
        btnNewReserve = new JButton();
        btnNewReserve.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveSelected.png")));
        btnNewReserve.setText("Nueva reserva");
        toolBar1.add(btnNewReserve);
        btnNewQuotation = new JButton();
        btnNewQuotation.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/quotationSelected.png")));
        btnNewQuotation.setText("Nueva cotización");
        toolBar1.add(btnNewQuotation);
        btnSearchComprobante = new JButton();
        btnSearchComprobante.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/searchComprobante.png")));
        btnSearchComprobante.setText("Buscar comprobante");
        toolBar1.add(btnSearchComprobante);
        btnSearchClient = new JButton();
        btnSearchClient.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/searchClient.png")));
        btnSearchClient.setText("Buscar cliente");
        toolBar1.add(btnSearchClient);
        final Spacer spacer2 = new Spacer();
        toolBar1.add(spacer2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setMaximumSize(new Dimension(3, 32767));
        panel3.setOpaque(false);
        toolBar1.add(panel3);
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        panel3.add(separator1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnActualizar = new JButton();
        btnActualizar.setBorderPainted(true);
        btnActualizar.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/refresh.png")));
        btnActualizar.setMargin(new Insets(0, 0, 0, 0));
        btnActualizar.setMaximumSize(new Dimension(42, 42));
        btnActualizar.setMinimumSize(new Dimension(42, 42));
        btnActualizar.setPreferredSize(new Dimension(42, 42));
        btnActualizar.setText("");
        toolBar1.add(btnActualizar);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 10, 0, 10), -1, -1));
        panel4.setBackground(new java.awt.Color(-16777216));
        contentPane.add(panel4, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 28), new Dimension(-1, 28), new Dimension(-1, 28), 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.setOpaque(false);
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(350, -1), new Dimension(350, -1), new Dimension(350, -1), 0, false));
        lblLeft = new JLabel();
        Font lblLeftFont = this.$$$getFont$$$(null, Font.BOLD, -1, lblLeft.getFont());
        if (lblLeftFont != null) lblLeft.setFont(lblLeftFont);
        lblLeft.setForeground(new java.awt.Color(-1));
        lblLeft.setText("");
        panel5.add(lblLeft, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.setOpaque(false);
        panel4.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lblCenter = new JLabel();
        Font lblCenterFont = this.$$$getFont$$$(null, Font.BOLD, -1, lblCenter.getFont());
        if (lblCenterFont != null) lblCenter.setFont(lblCenterFont);
        lblCenter.setForeground(new java.awt.Color(-1));
        lblCenter.setText("");
        panel6.add(lblCenter, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.setOpaque(false);
        panel4.add(panel7, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(350, -1), new Dimension(350, -1), new Dimension(350, -1), 0, false));
        lblRigth = new JLabel();
        Font lblRigthFont = this.$$$getFont$$$(null, Font.BOLD, -1, lblRigth.getFont());
        if (lblRigthFont != null) lblRigth.setFont(lblRigthFont);
        lblRigth.setForeground(new java.awt.Color(-1));
        lblRigth.setText("Caja cerrada");
        panel7.add(lblRigth, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), 0, 0));
        panel8.setBackground(new java.awt.Color(-4013374));
        contentPane.add(panel8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(130, -1), null, 0, false));
        splitPane = new JSplitPane();
        splitPane.setDividerSize(0);
        panel8.add(splitPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(200, 200), null, 0, false));
        panelControles.setLayout(new GridLayoutManager(11, 1, new Insets(0, 0, 0, 0), -1, 0));
        splitPane.setLeftComponent(panelControles);
        final JSeparator separator2 = new JSeparator();
        panelControles.add(separator2, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 4, new Insets(2, 0, 2, 0), -1, -1));
        panel9.setOpaque(false);
        panelControles.add(panel9, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel9.add(spacer3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel9.add(spacer4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSettings = new FlatButton();
        btnSettings.setButtonType(FlatButton.ButtonType.toolBarButton);
        btnSettings.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x16/iniciar-sesion.png")));
        btnSettings.setMaximumSize(new Dimension(38, 38));
        btnSettings.setMinimumSize(new Dimension(38, 38));
        btnSettings.setPreferredSize(new Dimension(38, 38));
        btnSettings.setText("");
        panel9.add(btnSettings, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnExit = new FlatButton();
        btnExit.setButtonType(FlatButton.ButtonType.toolBarButton);
        btnExit.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x16/iniciar-sesion.png")));
        btnExit.setMaximumSize(new Dimension(38, 38));
        btnExit.setMinimumSize(new Dimension(38, 38));
        btnExit.setPreferredSize(new Dimension(38, 38));
        btnExit.setText("");
        panel9.add(btnExit, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panelControles.add(spacer5, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 3, new Insets(2, 0, 2, 0), -1, -1));
        panel10.setOpaque(false);
        panelControles.add(panel10, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNotify = new FlatButton();
        btnNotify.setButtonType(FlatButton.ButtonType.toolBarButton);
        btnNotify.setHorizontalTextPosition(0);
        btnNotify.setText("");
        btnNotify.setVisible(true);
        panel10.add(btnNotify, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(32, 32), null, new Dimension(40, 40), 0, false));
        final Spacer spacer6 = new Spacer();
        panel10.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel10.add(spacer7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSales.setButtonType(FlatButton.ButtonType.tab);
        btnSales.setFocusable(false);
        Font btnSalesFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnSales.getFont());
        if (btnSalesFont != null) btnSales.setFont(btnSalesFont);
        btnSales.setHorizontalTextPosition(0);
        btnSales.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/ventasSelected.png")));
        btnSales.setIconTextGap(-2);
        btnSales.setMargin(new Insets(10, 5, 10, 5));
        btnSales.setRolloverEnabled(false);
        btnSales.setSelected(true);
        btnSales.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/ventasDefault.png")));
        btnSales.setTabUnderlineHeight(5);
        btnSales.setTabUnderlinePlacement(2);
        btnSales.setText("Ventas");
        btnSales.setVerticalTextPosition(3);
        panelControles.add(btnSales, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnManagement.setButtonType(FlatButton.ButtonType.tab);
        btnManagement.setFocusable(false);
        Font btnManagementFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnManagement.getFont());
        if (btnManagementFont != null) btnManagement.setFont(btnManagementFont);
        btnManagement.setHorizontalTextPosition(0);
        btnManagement.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/gestionarSelected.png")));
        btnManagement.setIconTextGap(-2);
        btnManagement.setMargin(new Insets(10, 5, 10, 5));
        btnManagement.setRolloverEnabled(false);
        btnManagement.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/gestionarDefault.png")));
        btnManagement.setTabUnderlineHeight(5);
        btnManagement.setTabUnderlinePlacement(2);
        btnManagement.setText("Gestionar");
        btnManagement.setVerticalTextPosition(3);
        panelControles.add(btnManagement, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnReserves.setButtonType(FlatButton.ButtonType.tab);
        btnReserves.setFocusable(false);
        Font btnReservesFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnReserves.getFont());
        if (btnReservesFont != null) btnReserves.setFont(btnReservesFont);
        btnReserves.setHorizontalTextPosition(0);
        btnReserves.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveSelected.png")));
        btnReserves.setIconTextGap(-2);
        btnReserves.setMargin(new Insets(10, 5, 10, 5));
        btnReserves.setRolloverEnabled(false);
        btnReserves.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveDefault.png")));
        btnReserves.setTabUnderlineHeight(5);
        btnReserves.setTabUnderlinePlacement(2);
        btnReserves.setText("Reservas");
        btnReserves.setVerticalTextPosition(3);
        panelControles.add(btnReserves, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnTraslades.setButtonType(FlatButton.ButtonType.tab);
        btnTraslades.setFocusable(false);
        Font btnTrasladesFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnTraslades.getFont());
        if (btnTrasladesFont != null) btnTraslades.setFont(btnTrasladesFont);
        btnTraslades.setHorizontalTextPosition(0);
        btnTraslades.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/transferirDefault.png")));
        btnTraslades.setIconTextGap(-2);
        btnTraslades.setMargin(new Insets(10, 5, 10, 5));
        btnTraslades.setRolloverEnabled(false);
        btnTraslades.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/transferirSelected.png")));
        btnTraslades.setTabUnderlineHeight(5);
        btnTraslades.setTabUnderlinePlacement(2);
        btnTraslades.setText("Traslados");
        btnTraslades.setVerticalTextPosition(3);
        panelControles.add(btnTraslades, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnRentals.setButtonType(FlatButton.ButtonType.tab);
        btnRentals.setFocusable(false);
        Font btnRentalsFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnRentals.getFont());
        if (btnRentalsFont != null) btnRentals.setFont(btnRentalsFont);
        btnRentals.setHorizontalTextPosition(0);
        btnRentals.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveSelected.png")));
        btnRentals.setIconTextGap(-2);
        btnRentals.setMargin(new Insets(10, 5, 10, 5));
        btnRentals.setRolloverEnabled(false);
        btnRentals.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveDefault.png")));
        btnRentals.setTabUnderlineHeight(5);
        btnRentals.setTabUnderlinePlacement(2);
        btnRentals.setText("Alquileres");
        btnRentals.setVerticalTextPosition(3);
        panelControles.add(btnRentals, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnBoxes.setButtonType(FlatButton.ButtonType.tab);
        btnBoxes.setFocusable(false);
        Font btnBoxesFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnBoxes.getFont());
        if (btnBoxesFont != null) btnBoxes.setFont(btnBoxesFont);
        btnBoxes.setHorizontalTextPosition(0);
        btnBoxes.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/boxSelected.png")));
        btnBoxes.setIconTextGap(-2);
        btnBoxes.setMargin(new Insets(10, 5, 10, 5));
        btnBoxes.setRolloverEnabled(false);
        btnBoxes.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/boxDefault.png")));
        btnBoxes.setTabUnderlineHeight(5);
        btnBoxes.setTabUnderlinePlacement(2);
        btnBoxes.setText("Cajas");
        btnBoxes.setVerticalTextPosition(3);
        panelControles.add(btnBoxes, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnQuotations.setButtonType(FlatButton.ButtonType.tab);
        btnQuotations.setFocusable(false);
        Font btnQuotationsFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnQuotations.getFont());
        if (btnQuotationsFont != null) btnQuotations.setFont(btnQuotationsFont);
        btnQuotations.setHorizontalTextPosition(0);
        btnQuotations.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/quotationSelected.png")));
        btnQuotations.setIconTextGap(-2);
        btnQuotations.setMargin(new Insets(10, 5, 10, 5));
        btnQuotations.setRolloverEnabled(false);
        btnQuotations.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/quotationDefault.png")));
        btnQuotations.setTabUnderlineHeight(5);
        btnQuotations.setTabUnderlinePlacement(2);
        btnQuotations.setText("Cotizaciones");
        btnQuotations.setVerticalTextPosition(3);
        panelControles.add(btnQuotations, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelMenus = new JPanel();
        panelMenus.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setRightComponent(panelMenus);
        cPane.setLayout(new GridLayoutManager(1, 1, new Insets(5, 10, 5, 0), -1, -1));
        panel8.add(cPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 33), new Dimension(-1, 33), new Dimension(-1, 33), 0, false));
        lblUsuario = new JLabel();
        lblUsuario.setForeground(new java.awt.Color(-1));
        lblUsuario.setText("Usuario: javier");
        cPane.add(lblUsuario, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel11, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panel11.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tabbedPane = new TabbedPane();
        tabbedPane.setTabLayoutPolicy(1);
        panel11.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(btnSales);
        buttonGroup.add(btnManagement);
        buttonGroup.add(btnTraslades);
        buttonGroup.add(btnReserves);
        buttonGroup.add(btnRentals);
        buttonGroup.add(btnBoxes);
        buttonGroup.add(btnQuotations);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
