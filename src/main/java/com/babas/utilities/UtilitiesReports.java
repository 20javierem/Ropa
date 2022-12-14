package com.babas.utilities;

import com.babas.App;
import com.babas.models.*;
import com.babas.modelsFacture.Comprobante;
import com.babas.modelsFacture.Detalle;
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

    public static void generateTicketQuotation(boolean a4,Quotation quotation,boolean print) {
        InputStream pathReport;
        if(a4){
            pathReport = App.class.getResourceAsStream("jasperReports/sheetTicket-quotation.jasper");
        }else{
            pathReport = App.class.getResourceAsStream("jasperReports/ticket-quotation.jasper");
        }
        File file= new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<DetailQuotation> list=new ArrayList<>(new Vector<>(quotation.getDetailQuotations()));
                list.add(0,new DetailQuotation());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("ruc",Babas.company.getRuc());
                parameters.put("direccion",quotation.getBranch().getDirection());
                parameters.put("telefono",quotation.getBranch().getPhone());
                parameters.put("email",quotation.getBranch().getEmail());
                parameters.put("logo",logo);
                parameters.put("message",Babas.company.getDetails().isBlank()?"Gracias por su compra":Babas.company.getDetails());
                parameters.put("nameTicket","TICKET DE COTIZACI??N");
                parameters.put("numberTicket",quotation.getNumberQuotation());
                parameters.put("detalles",sp);
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("nameComercial",Babas.company.getTradeName());
                parameters.put("fechaEmision", Utilities.formatoFechaHora.format(quotation.getCreated()));
                parameters.put("subtotal",Utilities.moneda.format(quotation.getTotal()));
                parameters.put("nombreCliente",quotation.getClient()!=null?quotation.getClient().getNames():"");
                parameters.put("clienteDni",quotation.getClient()!=null?quotation.getClient().getDni():"");
                parameters.put("descuento",Utilities.moneda.format(quotation.getDiscount()));
                parameters.put("total",Utilities.moneda.format(quotation.getTotalCurrent()));
                parameters.put("validoHasta",Utilities.formatoFecha.format(quotation.getEnded()));
                parameters.put("vendedor",quotation.getUser().getUserName());
                parameters.put("observacion",quotation.getObservation());
                if(print){
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,sp);
                    JasperPrintManager.printReport(jasperPrint,true);
                }else{
                    JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                    if(viewer!=null){
                        viewer.setTitle("Cotizaci??n Nro. "+quotation.getNumberQuotation());
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
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateTicketReserve(boolean a4,Reserve reserve, boolean print) {
        InputStream pathReport;
        if(a4){
            pathReport = App.class.getResourceAsStream("jasperReports/sheetTicket-reserve.jasper");
        }else{
            pathReport = App.class.getResourceAsStream("jasperReports/ticket-reserve.jasper");
        }
        File file= new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo());
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
                parameters.put("message",Babas.company.getDetails().isBlank()?"Gracias por su compra":Babas.company.getDetails());
                parameters.put("nameTicket","TICKET DE RESERVA");
                parameters.put("numberTicket",reserve.getNumberReserve());
                parameters.put("detalles",sp);
                parameters.put("stade",reserve.getStringStade());
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("nameComercial",Babas.company.getTradeName());
                parameters.put("fechaEmision", Utilities.formatoFechaHora.format(reserve.getCreated()));
                parameters.put("subtotal",Utilities.moneda.format(reserve.getTotal()));
                parameters.put("nombreCliente",reserve.getClient()!=null?reserve.getClient().getNames():"");
                parameters.put("clienteDni",reserve.getClient()!=null?reserve.getClient().getDni():"");
                parameters.put("advance",Utilities.moneda.format(reserve.getAdvance()));
                parameters.put("total",Utilities.moneda.format(reserve.getToCancel()));
                parameters.put("formaDePago",reserve.isCash()?"EFECTIVO":"TRANSFERENCIA");
                parameters.put("vendedor",reserve.getUser().getUserName());
                parameters.put("observacion",reserve.getObservation());
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
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateTicketRental(boolean a4,Rental rental, boolean print) {
        InputStream pathReport;
        if(a4){
            pathReport = App.class.getResourceAsStream("jasperReports/sheetTicket-rental.jasper");
        }else{
            pathReport = App.class.getResourceAsStream("jasperReports/ticket-rental.jasper");
        }
        File file= new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo());
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
                parameters.put("message",Babas.company.getDetails().isBlank()?"Gracias por su compra":Babas.company.getDetails());
                parameters.put("nameTicket","TICKET DE ALQUILER");
                parameters.put("numberTicket",rental.getId());
                parameters.put("detalles",sp);
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("nameComercial",Babas.company.getTradeName());
                parameters.put("fechaEmision", Utilities.formatoFechaHora.format(rental.getCreated()));
                parameters.put("nombreCliente",rental.getClient()!=null?rental.getClient().getNames():"");
                parameters.put("clienteDni",rental.getClient()!=null?rental.getClient().getDni():"");
                parameters.put("subtotal",Utilities.moneda.format(rental.getTotalWithDiscount()));
                parameters.put("advance",rental.getReserve()!=null?Utilities.moneda.format(rental.getReserve().getAdvance()):Utilities.moneda.format(0.0));
                parameters.put("subtotal2",Utilities.moneda.format(rental.getTotalCurrent() - rental.getWarranty()));
                parameters.put("warranty",Utilities.moneda.format(rental.getWarranty()));
                parameters.put("total",Utilities.moneda.format(rental.getTotalCurrent()));
                parameters.put("stade",rental.getStringStade());
                parameters.put("totalRental",Utilities.moneda.format(rental.getTotal()));
                parameters.put("descuento",Utilities.moneda.format(rental.getDiscount()));
                parameters.put("formaDePago",rental.isCash()?"EFECTIVO":"TRANSFERENCIA");
                parameters.put("vendedor",rental.getUser().getUserName());
                parameters.put("observacion",rental.getObservation());
                if(print){
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,sp);
                    JasperPrintManager.printReport(jasperPrint,true);
                }else{
                    JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                    if(viewer!=null){
                        viewer.setTitle("Alquiler Nro. "+rental.getId());
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
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateComprobanteOfRental(boolean a4,Rental rental,boolean print){
        InputStream pathReport;
        if(a4){
            pathReport = App.class.getResourceAsStream("jasperReports/a4-comprobante.jasper");
        }else{
            pathReport = App.class.getResourceAsStream("jasperReports/ticket-comprobante.jasper");
        }
        String logo=new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo()).getAbsolutePath();
        Utilities.createQrCode(rental.getContentQR());
        String qr=new File(System.getProperty("user.home")+"/.clothes/contentQr.png").getAbsolutePath();
        try {
            if(pathReport!=null){
                List<DetailRental> list=new ArrayList<>(new Vector<>(rental.getDetailRentals()));
                list.add(0,new DetailRental());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("logo",logo);
                parameters.put("ruc",Babas.company.getRuc());
                parameters.put("direccion",rental.getBranch().getDirection());
                parameters.put("telefono",rental.getBranch().getPhone());
                parameters.put("email",rental.getBranch().getEmail());
                parameters.put("numberTicket",rental.getNumberRental());
                parameters.put("subtotal",Utilities.moneda.format(rental.getTotal()));
                parameters.put("total",Utilities.moneda.format(rental.getTotalWithDiscount()));
                parameters.put("importeEnLetras",Utilities.moneda.format(rental.getTotalWithDiscount()));
                parameters.put("fechaEmision", Utilities.formatoFechaHora.format(rental.getCreated()));
                parameters.put("nombreCliente",rental.getStringClient());
                parameters.put("vendedor",rental.getUser().getUserName());
                parameters.put("clienteDni",rental.getClientDni());
                parameters.put("detalles",sp);
                parameters.put("tipoDocumentoCliente",rental.getClientType()==6?"R.U.C.":"D.N.I.");
                parameters.put("message",Babas.company.getDetails().isBlank()?"Gracias por su compra":Babas.company.getDetails());
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("descuento",Utilities.moneda.format(rental.getDiscount()));
                parameters.put("observacion",rental.getObservation());
                parameters.put("ubigeo",rental.getBranch().getUbigeo());
                parameters.put("webSite",Babas.company.getWebSite());
                parameters.put("contentQR",qr);
                parameters.put("montoEnLetras", NumberToText.toText(rental.getTotalCurrent()));
                parameters.put("clienteDireccion",rental.getClientAdress());
                parameters.put("igv",Utilities.moneda.format(0));
                parameters.put("detailTicket",rental.getDetailTicket());
                parameters.put("fechaVencimiento", Utilities.formatoFechaHora.format(rental.getCreated()));
                parameters.put("typeTicket",rental.getStringTypeDocument());
                if(print){
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,sp);
                    JasperPrintManager.printReport(jasperPrint,true);
                }else{
                    JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                    if(viewer!=null){
                        viewer.setTitle("Comprobante "+rental.getNumberRental());
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
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateComprobanteOfSale(boolean a4,Sale sale,boolean print){
        InputStream pathReport;
        if(a4){
            pathReport = App.class.getResourceAsStream("jasperReports/a4-comprobante.jasper");
        }else{
            pathReport = App.class.getResourceAsStream("jasperReports/ticket-comprobante.jasper");
        }
        String logo=new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo()).getAbsolutePath();
        Utilities.createQrCode(sale.getContentQR());
        String qr=new File(System.getProperty("user.home")+"/.clothes/contentQr.png").getAbsolutePath();
        try {
            if(pathReport!=null){
                List<DetailSale> list=new ArrayList<>(new Vector<>(sale.getDetailSales()));
                list.add(0,new DetailSale());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("logo",logo);
                parameters.put("ruc",Babas.company.getRuc());
                parameters.put("direccion",sale.getBranch().getDirection());
                parameters.put("telefono",sale.getBranch().getPhone());
                parameters.put("email",sale.getBranch().getEmail());
                parameters.put("numberTicket",sale.getSerie()+" - "+sale.getCorrelativo());
                parameters.put("subtotal",Utilities.moneda.format(sale.getTotal()));
                parameters.put("total",Utilities.moneda.format(sale.getTotalCurrent()));
                parameters.put("importeEnLetras",Utilities.moneda.format(sale.getTotalCurrent()));
                parameters.put("fechaEmision", Utilities.formatoFechaHora.format(sale.getCreated()));
                parameters.put("nombreCliente",sale.getStringClient());
                parameters.put("vendedor",sale.getUser().getUserName());
                parameters.put("clienteDni",sale.getClientDni());
                parameters.put("detalles",sp);
                parameters.put("tipoDocumentoCliente",sale.getClientType()==6?"R.U.C.":"D.N.I.");
                parameters.put("message",Babas.company.getDetails().isBlank()?"Gracias por su compra":Babas.company.getDetails());
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("descuento",Utilities.moneda.format(sale.getDiscount()));
                parameters.put("observacion",sale.getObservation());
                parameters.put("ubigeo",sale.getBranch().getUbigeo());
                parameters.put("webSite",Babas.company.getWebSite());
                parameters.put("contentQR",qr);
                parameters.put("montoEnLetras", NumberToText.toText(sale.getTotalCurrent()));
                parameters.put("clienteDireccion",sale.getClientAdress());
                parameters.put("igv",Utilities.moneda.format(0));
                parameters.put("detailTicket",sale.getDetailTicket());
                parameters.put("fechaVencimiento", Utilities.formatoFechaHora.format(sale.getCreated()));
                parameters.put("typeTicket",sale.getStringTypeDocument());
                if(print){
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,sp);
                    JasperPrintManager.printReport(jasperPrint,true);
                }else{
                    JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                    if(viewer!=null){
                        viewer.setTitle("Comprobante "+sale.getSerie()+"-"+sale.getCorrelativo());
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
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateReportSales(List<Sale> sales,Date dateStart,Date dateEnd,Double totalSaleCash,Double totalSaleTransfer) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/reportSales.jasper");
        File file= new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo());
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
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateReportRentals(List<Rental> sales,Date dateStart,Date dateEnd,Double totalSaleCash,Double totalSaleTransfer) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/reportRentals.jasper");
        File file= new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo());
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
                    viewer.setTitle("Reporte de alquileres: "+Utilities.formatoFecha.format(dateStart)+" a "+Utilities.formatoFecha.format(dateEnd));
                    if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                        Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }
                    Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                    Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateReportReserves(List<Reserve> sales,Date dateStart,Date dateEnd,Double totalSaleCash,Double totalSaleTransfer) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/reportReserves.jasper");
        File file= new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<Reserve> list=new ArrayList<>(sales);
                list.add(0,new Reserve());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("date",Utilities.formatoFechaHora.format(new Date()));
                parameters.put("logo",logo);
                parameters.put("reserves",sp);
                parameters.put("dateStart", Utilities.formatoFecha.format(dateStart));
                parameters.put("dateEnd", Utilities.formatoFecha.format(dateEnd));
                parameters.put("totalSaleCash",Utilities.moneda.format(totalSaleCash));
                parameters.put("totalSaleTransfer",Utilities.moneda.format(totalSaleTransfer));
                JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                if(viewer!=null){
                    viewer.setTitle("Reporte de reservas: "+Utilities.formatoFecha.format(dateStart)+" a "+Utilities.formatoFecha.format(dateEnd));
                    if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                        Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }
                    Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                    Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateReportTransfer(Transfer transfer) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/reportTransfer.jasper");
        File file= new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<DetailTransfer> list=new ArrayList<>(transfer.getDetailTransfers());
                list.add(0,new DetailTransfer());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("totalProducts",transfer.getProductsTransfers());
                parameters.put("numberTransfer",transfer.getNumberTransfer());
                parameters.put("source",transfer.getStringSource());
                parameters.put("destiny",transfer.getStringDestiny());
                parameters.put("stade",transfer.getStringStade());
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("logo",logo);
                parameters.put("details",sp);
                JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                if(viewer!=null){
                    viewer.setTitle("Reporte de transferencia N??: "+transfer.getNumberTransfer());
                    if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                        Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }
                    Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                    Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateReportTransfers(List<Transfer> transfers,Date dateStart,Date dateEnd) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/reportTransfers.jasper");
        File file= new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                List<Transfer> list=new ArrayList<>(transfers);
                list.add(0,new Transfer());
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                JRBeanArrayDataSource sp=new JRBeanArrayDataSource(list.toArray());
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("date",Utilities.formatoFechaHora.format(new Date()));
                parameters.put("logo",logo);
                parameters.put("transfers",sp);
                parameters.put("dateStart", Utilities.formatoFecha.format(dateStart));
                parameters.put("dateEnd", Utilities.formatoFecha.format(dateEnd));
                JasperViewer viewer = getjasperViewer(report,parameters,sp,true);
                if(viewer!=null){
                    viewer.setTitle("Reporte de transferencias: "+Utilities.formatoFecha.format(dateStart)+" a "+Utilities.formatoFecha.format(dateEnd));
                    if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                        Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }
                    Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                    Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static void generateReportBoxSesssion(BoxSession boxSession) {
        InputStream pathReport = App.class.getResourceAsStream("jasperReports/reportBoxSession.jasper");
        File file= new File(System.getProperty("user.home") + "/.clothes" + "/" + Babas.company.getLogo());
        String logo=file.getAbsolutePath();
        try {
            if(pathReport!=null){
                JasperReport report=(JasperReport) JRLoader.loadObject(pathReport);
                Map<String,Object> parameters=new HashMap<>();
                parameters.put("nameCompany",Babas.company.getBusinessName());
                parameters.put("date",Utilities.formatoFechaHora.format(new Date()));
                parameters.put("nameBranch",boxSession.getBox().getBranch().getName());
                parameters.put("logo",logo);
                parameters.put("dateStart", Utilities.formatoFechaHora.format(boxSession.getCreated()));
                parameters.put("dateEnd",Utilities.formatoFechaHora.format(boxSession.getUpdated()!=null?boxSession.getUpdated():new Date()));
                parameters.put("amountInitial",Utilities.moneda.format(boxSession.getAmountInitial()));
                parameters.put("totalSales",Utilities.moneda.format(boxSession.getTotalSales()));
                parameters.put("totalSalesCash",Utilities.moneda.format(boxSession.getTotalSalesCash()));
                parameters.put("totalSalesTransfer",Utilities.moneda.format(boxSession.getTotalSalesTransfer()));
                parameters.put("totalReserves",Utilities.moneda.format(boxSession.getTotalReserves()));
                parameters.put("totalReservesCash",Utilities.moneda.format(boxSession.getTotalReservesCash()));
                parameters.put("totalReservesTransfer",Utilities.moneda.format(boxSession.getTotalReservesTransfer()));
                parameters.put("totalRentals",Utilities.moneda.format(boxSession.getTotalRentals()));
                parameters.put("totalRentalsCash",Utilities.moneda.format(boxSession.getTotalRentalsCash()));
                parameters.put("totalRentalsTransfer",Utilities.moneda.format(boxSession.getTotalRentalsTransfer()));
                parameters.put("totalRetiros",Utilities.moneda.format(boxSession.getTotalRetiros()));
                parameters.put("totalIngresos",Utilities.moneda.format(boxSession.getTotalIngresos()));
                parameters.put("totalCash",Utilities.moneda.format(boxSession.getTotalCash()));
                parameters.put("totalTransfer",Utilities.moneda.format(boxSession.getTotalTransfers()));
                parameters.put("total",Utilities.moneda.format(boxSession.getAmountTotal()));
                JasperViewer viewer = getjasperViewer(report,parameters,null,true);
                if(viewer!=null){
                    viewer.setTitle("Reporte sesi??n de caja: "+boxSession.getBox().getBranch().getName()+" "+Utilities.formatoFecha.format(boxSession.getCreated()));
                    if(Utilities.getTabbedPane().indexOfTab(viewer.getTitle())!=-1){
                        Utilities.getTabbedPane().remove(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                    }
                    Utilities.getTabbedPane().addTab(viewer.getTitle(), viewer.getContentPane());
                    Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(viewer.getTitle()));
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedio un error inesperado");
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la plantilla");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    public static JasperViewer getjasperViewer(JasperReport report, Map<String, Object> parameters, JRBeanArrayDataSource sp, boolean isExitOnClose){
        try {
            JasperPrint jasperPrint=JasperFillManager.fillReport(report,parameters,sp);
            JasperViewer jasperViewer=new JasperViewer(jasperPrint,isExitOnClose);
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
