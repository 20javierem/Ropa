package com.babas.utilities;

import com.babas.App;
import com.babas.models.*;
import com.moreno.Notify;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.swing.JRViewerPanel;
import net.sf.jasperreports.swing.JRViewerToolbar;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.*;

public class UtilitiesReports {

    public static void generateTicketSale(Sale sale,boolean print) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/ticket-sale.jasper");
        File file= new File(System.getProperty("user.home") + "/.Tienda-Ropa" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<DetailSale> list=new ArrayList<>(new Vector<>(sale.getDetailSales()));
                list.add(0,new DetailSale());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("ruc",Babas.company.getRuc());
                parameters.put("direccion",sale.getBranch().getDirection());
                parameters.put("telefono",sale.getBranch().getPhone());
                parameters.put("email",sale.getBranch().getEmail());
                parameters.put("logo",logo);
                parameters.put("message",Babas.company.getSlogan().isBlank()?"Gracias por su compra":Babas.company.getSlogan());
                parameters.put("nameTicket","Ticket de venta");
                parameters.put("numberTicket",sale.getNumberSale());
                parameters.put("detalles",sp);
                parameters.put("nameCompany",Babas.company.getTradeName());
                parameters.put("fechaEmision", sale.getCreated());
                parameters.put("subtotal",Utilities.moneda.format(sale.getTotal()));
                parameters.put("nombreCliente",sale.getClient()!=null?sale.getClient().getNames():"");
                parameters.put("descuento",Utilities.moneda.format(sale.getDiscount()));
                parameters.put("total",Utilities.moneda.format(sale.getTotalCurrent()));
                parameters.put("formaDePago",sale.isCash()?"EFECTIVO":"TRANSFERENCIA");
                parameters.put("vendedor",sale.getUser().getUserName());
                if(print){
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,sp);
                    JasperPrintManager.printReport(jasperPrint,true);
                }else{
                    JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                    if(viewer!=null){
                        viewer.setTitle("Venta Nro. "+sale.getNumberSale());
                        if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                            Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                        }
                        Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                        Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                    }
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontró la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateTicketReserve(Reserve reserve, boolean print) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/ticket-reserve.jasper");
        File file= new File(System.getProperty("user.home") + "/.Tienda-Ropa" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<DetailReserve> list=new ArrayList<>(new Vector<>(reserve.getDetailReserves()));
                list.add(0,new DetailReserve());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("ruc",Babas.company.getRuc());
                parameters.put("direccion",reserve.getBranch().getDirection());
                parameters.put("telefono",reserve.getBranch().getPhone());
                parameters.put("email",reserve.getBranch().getEmail());
                parameters.put("logo",logo);
                parameters.put("message",Babas.company.getSlogan().isBlank()?"Gracias por su compra":Babas.company.getSlogan());
                parameters.put("nameTicket","Ticket de reserva");
                parameters.put("numberTicket",reserve.getNumberReserve());
                parameters.put("detalles",sp);
                parameters.put("nameCompany",Babas.company.getTradeName());
                parameters.put("fechaEmision", reserve.getCreated());
                parameters.put("subtotal",Utilities.moneda.format(reserve.getTotal()));
                parameters.put("nombreCliente",reserve.getClient()!=null?reserve.getClient().getNames():"");
                parameters.put("advance",Utilities.moneda.format(reserve.getAdvance()));
                parameters.put("total",Utilities.moneda.format(reserve.getToCancel()));
                parameters.put("formaDePago",reserve.isCash()?"EFECTIVO":"TRANSFERENCIA");
                parameters.put("vendedor",reserve.getUser().getUserName());
                if(print){
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,sp);
                    JasperPrintManager.printReport(jasperPrint,true);
                }else{
                    JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                    if(viewer!=null){
                        viewer.setTitle("Reserva Nro. "+reserve.getNumberReserve());
                        if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                            Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                        }
                        Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                        Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                    }
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontró la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public static void generateTicketRental(Rental rental, boolean print) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/ticket-rental.jasper");
        File file= new File(System.getProperty("user.home") + "/.Tienda-Ropa" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<DetailRental> list=new ArrayList<>(new Vector<>(rental.getDetailRentals()));
                list.add(0,new DetailRental());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("ruc",Babas.company.getRuc());
                parameters.put("direccion",rental.getBranch().getDirection());
                parameters.put("telefono",rental.getBranch().getPhone());
                parameters.put("email",rental.getBranch().getEmail());
                parameters.put("logo",logo);
                parameters.put("message",Babas.company.getSlogan().isBlank()?"Gracias por su compra":Babas.company.getSlogan());
                parameters.put("nameTicket","Ticket de alquiler");
                parameters.put("numberTicket",rental.getNumberRental());
                parameters.put("detalles",sp);
                parameters.put("nameCompany",Babas.company.getTradeName());
                parameters.put("fechaEmision", rental.getCreated());
                parameters.put("subtotal",Utilities.moneda.format(rental.getTotal()+rental.getWarranty()));
                parameters.put("nombreCliente",rental.getClient()!=null?rental.getClient().getNames():"");
                parameters.put("advance",rental.getReserve()!=null?Utilities.moneda.format(rental.getReserve().getAdvance()):Utilities.moneda.format(0.0));
                parameters.put("total",Utilities.moneda.format(rental.getTotalCurrent()));
                parameters.put("totalRental",Utilities.moneda.format(rental.getTotal()));
                parameters.put("warranty",Utilities.moneda.format(rental.getWarranty()));
                parameters.put("descuento",Utilities.moneda.format(rental.getDiscount()));
                parameters.put("formaDePago",rental.isCash()?"EFECTIVO":"TRANSFERENCIA");
                parameters.put("vendedor",rental.getUser().getUserName());
                if(print){
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,sp);
                    JasperPrintManager.printReport(jasperPrint,true);
                }else{
                    JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                    if(viewer!=null){
                        viewer.setTitle("Alquiler Nro. "+rental.getNumberRental());
                        if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                            Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                        }
                        Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                        Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                    }
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontró la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public static void generateTicketRentalFinish(Rental rental, boolean print) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/ticket-rental-finish.jasper");
        File file= new File(System.getProperty("user.home") + "/.Tienda-Ropa" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<DetailRental> list=new ArrayList<>(new Vector<>(rental.getDetailRentals()));
                list.add(0,new DetailRental());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("ruc",Babas.company.getRuc());
                parameters.put("direccion",rental.getBranch().getDirection());
                parameters.put("telefono",rental.getBranch().getPhone());
                parameters.put("email",rental.getBranch().getEmail());
                parameters.put("logo",logo);
                parameters.put("message",Babas.company.getSlogan().isBlank()?"Gracias por su compra":Babas.company.getSlogan());
                parameters.put("nameTicket","Ticket de alquiler");
                parameters.put("numberTicket",rental.getNumberRental());
                parameters.put("detalles",sp);
                parameters.put("nameCompany",Babas.company.getTradeName());
                parameters.put("fechaEmision", rental.getCreated());
                parameters.put("subtotal",Utilities.moneda.format(rental.getTotal()+rental.getWarranty()));
                parameters.put("nombreCliente",rental.getClient()!=null?rental.getClient().getNames():"");
                parameters.put("advance",rental.getReserve()!=null?Utilities.moneda.format(rental.getReserve().getAdvance()):Utilities.moneda.format(0.0));
                parameters.put("total",Utilities.moneda.format(rental.getTotalCurrent()));
                parameters.put("totalRental",Utilities.moneda.format(rental.getTotal()));
                parameters.put("warranty",Utilities.moneda.format(rental.getWarranty()));
                parameters.put("descuento",Utilities.moneda.format(rental.getDiscount()));
                parameters.put("multa",Utilities.moneda.format(rental.getPenalty()));
                parameters.put("totalDevolucion",Utilities.moneda.format(rental.getWarranty()-rental.getPenalty()));
                parameters.put("formaDePago",rental.isCash()?"EFECTIVO":"TRANSFERENCIA");
                parameters.put("vendedor",rental.getUser().getUserName());
                if(print){
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,sp);
                    JasperPrintManager.printReport(jasperPrint,true);
                }else{
                    JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                    if(viewer!=null){
                        viewer.setTitle("Alquiler Nro. "+rental.getNumberRental());
                        if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                            Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                        }
                        Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                        Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                    }
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontró la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public static void generateReportSales(List<Sale> sales,Date dateStart,Date dateEnd,Double totalSaleCash,Double totalSaleTransfer) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/reportSales.jasper");
        File file= new File(System.getProperty("user.home") + "/.Tienda-Ropa" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<Sale> list=new ArrayList<>(sales);
                list.add(0,new Sale());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("date",Utilities.formatoFechaHora.format(new Date()));
                parameters.put("logo",logo);
                parameters.put("sales",sp);
                parameters.put("dateStart", Utilities.formatoFecha.format(dateStart));
                parameters.put("dateEnd", Utilities.formatoFecha.format(dateEnd));
                parameters.put("totalSaleCash",Utilities.moneda.format(totalSaleCash));
                parameters.put("totalSaleTransfer",Utilities.moneda.format(totalSaleTransfer));
                JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                if(viewer!=null){
                    viewer.setTitle("Reporte de ventas: "+Utilities.formatoFecha.format(dateStart)+" a "+Utilities.formatoFecha.format(dateEnd));
                    if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                        Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }
                    Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                    Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontró la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateReportRentals(List<Rental> sales,Date dateStart,Date dateEnd,Double totalSaleCash,Double totalSaleTransfer) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/reportRentals.jasper");
        File file= new File(System.getProperty("user.home") + "/.Tienda-Ropa" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<Rental> list=new ArrayList<>(sales);
                list.add(0,new Rental());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("date",Utilities.formatoFechaHora.format(new Date()));
                parameters.put("logo",logo);
                parameters.put("rentals",sp);
                parameters.put("dateStart", Utilities.formatoFecha.format(dateStart));
                parameters.put("dateEnd", Utilities.formatoFecha.format(dateEnd));
                parameters.put("totalSaleCash",Utilities.moneda.format(totalSaleCash));
                parameters.put("totalSaleTransfer",Utilities.moneda.format(totalSaleTransfer));
                JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                if(viewer!=null){
                    viewer.setTitle("Reporte de ventas: "+Utilities.formatoFecha.format(dateStart)+" a "+Utilities.formatoFecha.format(dateEnd));
                    if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                        Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }
                    Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                    Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontró la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public static JasperViewer getjasperViewer(JasperReport report, Map<String, Object> parameters, JRBeanArrayDataSource sp, boolean isExitOnClose){
        try {
            JasperViewer jasperViewer=new JasperViewer(JasperFillManager.fillReport(report,parameters,sp),isExitOnClose);
            JPanel panel= (JPanel) jasperViewer.getRootPane().getContentPane().getComponent(0);
            JRViewer visor= (JRViewer) panel.getComponent(0);
            JRViewerToolbar toolbar= (JRViewerToolbar) visor.getComponent(0);
            toolbar.setLayout(new FlowLayout(FlowLayout.CENTER,2,3));
            JRViewerPanel jrViewer= (JRViewerPanel) visor.getComponent(1);
            JScrollPane jScrollPane= (JScrollPane) jrViewer.getComponent(0);
            jScrollPane.setBorder(BorderFactory.createEmptyBorder());
            ((JPanel)visor.getComponent(2)).setLayout(new FlowLayout(FlowLayout.CENTER));
            Font font=new Font(new JTextField().getFont().getFontName(),Font.PLAIN,14);
            ((JPanel)visor.getComponent(2)).getComponent(0).setFont(font);
            for (Component component: toolbar.getComponents()){
                if(component instanceof JComboBox){
                    component.setMaximumSize(new Dimension(component.getMaximumSize().width+5,40));
                    component.setPreferredSize(new Dimension(component.getPreferredSize().width+5,40));
                    component.setMinimumSize(new Dimension(component.getMinimumSize().width+5,40));
                }else if(component instanceof JTextField){
                    component.setMaximumSize(new Dimension(component.getMaximumSize().width,40));
                    component.setPreferredSize(new Dimension(component.getPreferredSize().width,40));
                    component.setMinimumSize(new Dimension(component.getMinimumSize().width,40));
                }else if(component instanceof JPanel){
                    component.setMaximumSize(new Dimension(component.getMaximumSize().width,50));
                    component.setPreferredSize(new Dimension(component.getPreferredSize().width,50));
                    component.setMinimumSize(new Dimension(component.getMinimumSize().width,50));
                } else {
                    component.setMaximumSize(new Dimension(40,40));
                    component.setPreferredSize(new Dimension(40,40));
                    component.setMinimumSize(new Dimension(40,40));
                }
            }
            JButton mrZoom=(JButton)toolbar.getComponent(14);
            JButton mnZoom=(JButton)toolbar.getComponent(15);
            jScrollPane.addMouseWheelListener(e -> {
                if(e.isControlDown()){
                    if (e.getWheelRotation() < 0) {
                        mrZoom.doClick();
                    } else {
                        mnZoom.doClick();
                    }
                }
            });
            return jasperViewer;
        } catch (JRException e) {
            e.printStackTrace();
        }
        return null;
    }
}
