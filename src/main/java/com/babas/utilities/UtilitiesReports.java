package com.babas.utilities;

import com.babas.App;
import com.babas.models.DetailSale;
import com.babas.models.Sale;
import com.moreno.Notify;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
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

    public static void generateTicketSale(Sale sale) {
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
