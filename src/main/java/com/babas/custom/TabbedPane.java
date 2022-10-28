package com.babas.custom;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTabbedPane;
import com.formdev.flatlaf.icons.FlatRevealIcon;
import com.formdev.flatlaf.icons.FlatSearchWithHistoryIcon;
import com.formdev.flatlaf.ui.FlatTitlePaneIcon;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.multi.MultiTabbedPaneUI;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static com.formdev.flatlaf.util.ColorFunctions.lighten;

public class TabbedPane extends FlatTabbedPane {

    public static final long serialVersionUID = 1L;
    private static final int LINEWIDTH = 3;
    private static final String NAME = "TabTransferData";
    private final DataFlavor FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, NAME);
    private static GhostGlassPane s_glassPane = new GhostGlassPane();
    private boolean m_isDrawRect = false;
    private final Rectangle2D m_lineRect = new Rectangle2D.Double();
    private final Color m_lineColor = new Color(0x3C7EC0);
    private TabAcceptor m_acceptor = null;
    private Double maxX=0.0;
    private Double maxY=0.0;
    private Double minX=0.0;
    private Double minY=0.0;
    private JButton buttonEsquina=new JButton();
    private JToolBar toolBar=new JToolBar();
    private JPopupMenu pop_up;

    @Override
    public Component[] getComponents() {
        if(super.getComponents().length>=1){
            Component[] components=new Component[super.getComponents().length+1];
            int i=0;
            for (Component component:super.getComponents()){
                components[i]=component;
                i++;
            }
            components[i]= pop_up;
            return components;
        }
        return super.getComponents();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        verificarSelected();
    }

    @Override
    public void removeTabAt(int index) {
        despintar();
        super.removeTabAt(index);
        setVisibleButtonEsquina(getTabCount()>0);
        pintarSeleccionado();
    }

    @Override
    public void removeAll() {
        despintar();
        setVisibleButtonEsquina(false);
        super.removeAll();
    }

    @Override
    public void addTab(String title, Icon icon,Component component) {
        super.addTab(title, icon,component);
        setSelectedComponent(component);
        setTabCloseToolTipText(indexOfComponent(component),"Cerrar Pestaña " + title);
        despintar();
        pintarSeleccionado();
        setVisibleButtonEsquina(true);
    }
    @Override
    public void addTab(String title,Component component) {
        super.addTab(title, component);
        setSelectedComponent(component);
        setTabCloseToolTipText(indexOfComponent(component),"Cerrar Pestaña " + title);
        despintar();
        pintarSeleccionado();
        setVisibleButtonEsquina(true);
    }

    @Override
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        getComponentAt(index).requestFocus();
    }

    private void setVisibleButtonEsquina(boolean istade){
        toolBar.setVisible(istade);
    }

    private void despintar(){
        for (Component component : getComponents()) {
            if(indexOfComponent(component)!=-1){
                setEnabledAt(indexOfComponent(component),true);
                if(component instanceof TabPane){
                    TabPane tabPane=(TabPane) component;
                    if(tabPane.getOption()!=null){
                        Utilities.despintarButton(tabPane.getOption());
                    }
                }
            }
        }
    }

    public void updateTab(){
        if(getSelectedIndex()!=-1){
            if(getComponentAt(getSelectedIndex()) instanceof TabPane){
                TabPane tabPane =(TabPane) getComponentAt(getSelectedIndex());
                if(tabPane.getOption()!=null){
                    Utilities.buttonSelected(tabPane.getOption());
                }
                tabPane.update();
            }
        }
    }

    public void verificarSelected(){
        despintar();
        pintarSeleccionado();
    }

    public void pintarSeleccionado(){
        if(getSelectedIndex()!=-1){
            setEnabledAt(getSelectedIndex(),false);
            if(getComponentAt(getSelectedIndex()) instanceof TabPane){
                TabPane tabPane =(TabPane) getComponentAt(getSelectedIndex());
                if(tabPane.getOption()!=null){
                    Utilities.buttonSelected(tabPane.getOption());
                }
                tabPane.update();
            }
        }
    }

    private void insertarButtons(){
        //creacion de pop_up
        pop_up = new JPopupMenu();
        pop_up.setBorder(BorderFactory.createEmptyBorder());
        JMenuItem cerrarPestaña = new JMenuItem("Cerrar Pestaña");
        JMenuItem cerrarOtras = new JMenuItem("Cerrar Otras Pestañas");
        JMenuItem cerrarTodas = new JMenuItem("Cerrar Todas Las Pestañas");

        cerrarPestaña.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTabAt(getSelectedIndex());
            }
        });
        cerrarOtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getSelectedIndex()!=-1){
                    if(getComponentAt(getSelectedIndex()) instanceof TabPane){
                        TabPane tabPane= (TabPane) getComponentAt(getSelectedIndex());
                        removeAll();
                        addTab(tabPane.getTitle(),tabPane.getIcon(),tabPane);
                    }else{
                        String tittle=getTitleAt(getSelectedIndex());
                        Component component=getComponentAt(getSelectedIndex());
                        removeAll();
                        addTab(tittle,component);
                    }

                }
            }
        });
        cerrarTodas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
            }
        });
        pop_up.add(cerrarPestaña);
        pop_up.addSeparator();
        pop_up.add(cerrarOtras);
        pop_up.add(cerrarTodas);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton()==3){
                    maxX=0.0;
                    maxY=0.0;
                    minX=10000.0;
                    minY=10000.0;
                    for (Component component : getComponents()) {
                        if(indexOfComponent(component)!=-1){
                            if(maxX<getBoundsAt(indexOfComponent(component)).getMaxX()){
                                maxX=getBoundsAt(indexOfComponent(component)).getMaxX();
                            }
                            if(maxY<getBoundsAt(indexOfComponent(component)).getMaxY()){
                                maxY=getBoundsAt(indexOfComponent(component)).getMaxY();
                            }
                            if(minX>getBoundsAt(indexOfComponent(component)).getLocation().getLocation().getX()){
                                minX=getBoundsAt(indexOfComponent(component)).getLocation().getLocation().getX();
                            }
                            if(minY>getBoundsAt(indexOfComponent(component)).getLocation().getLocation().getY()){
                                minY=getBoundsAt(indexOfComponent(component)).getLocation().getLocation().getY();
                            }
                        }
                    }
                    if(e.getY()<=maxY&&e.getY()>=minY&&e.getX()<=maxX&&e.getX()>=minX){
                        if(tabPlacement==3||tabPlacement==4){
                            pop_up.show(getComponentAt(getSelectedIndex()),e.getX(),e.getY());
                        }else{
                            pop_up.show(getComponentAt(getMousePosition()),e.getX(),e.getY());
                        }
                    }
                }
            }
        });
        buttonEsquina.setIcon(new ImageIcon(com.babas.App.class.getResource("icons/x24/menu.png")));
        buttonEsquina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pop_up.show(buttonEsquina,buttonEsquina.getVisibleRect().x,buttonEsquina.getVisibleRect().y+buttonEsquina.getHeight());
            }
        });
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(buttonEsquina);
        setTrailingComponent(toolBar);
    }

    public TabbedPane() {
        super();
        setShowTabSeparators(true);
        setTabsClosable(true);
        setTabCloseCallback(JTabbedPane::removeTabAt);
        insertarButtons();
        getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                verificarSelected();
            }
        });

        //Modificación tabPane
        final DragSourceListener dsl = new DragSourceListener() {
            @Override
            public void dragEnter(DragSourceDragEvent e) {
                e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            }

            @Override
            public void dragExit(DragSourceEvent e) {
                e.getDragSourceContext()
                        .setCursor(DragSource.DefaultMoveNoDrop);
                m_lineRect.setRect(0, 0, 0, 0);
                m_isDrawRect = false;
                s_glassPane.setPoint(new Point(-1000, -1000));
                s_glassPane.repaint();
            }

            @Override
            public void dragOver(DragSourceDragEvent e) {
                TabTransferData data = getTabTransferData(e);
                if (data == null) {
                    e.getDragSourceContext().setCursor(
                            DragSource.DefaultMoveNoDrop);
                    return;
                }
                e.getDragSourceContext().setCursor(
                        DragSource.DefaultMoveDrop);
            }

            @Override
            public void dropActionChanged(DragSourceDragEvent dsde) {

            }

            @Override
            public void dragDropEnd(DragSourceDropEvent e) {
                m_isDrawRect = false;
                m_lineRect.setRect(0, 0, 0, 0);
                // m_dragTabIndex = -1;

                if (hasGhost()) {
                    s_glassPane.setVisible(false);
                    s_glassPane.setImage(null);
                }
            }
        };

        final DragGestureListener dgl = new DragGestureListener() {
            public void dragGestureRecognized(DragGestureEvent e) {
                Point tabPt = e.getDragOrigin();
                int dragTabIndex = indexAtLocation(tabPt.x, tabPt.y);
                if (dragTabIndex < 0) {
                    return;
                } // if

                initGlassPane(e.getComponent(), e.getDragOrigin(), dragTabIndex);
                try {
                    e.startDrag(DragSource.DefaultMoveDrop,
                            new TabTransferable(TabbedPane.this, dragTabIndex), dsl);
                } catch (InvalidDnDOperationException idoe) {
                    idoe.printStackTrace();
                }
            }
        };

        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE,
                new CDropTargetListener(), true);
        new DragSource().createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_COPY_OR_MOVE, dgl);
        m_acceptor = new TabAcceptor() {
            public boolean isDropAcceptable(TabbedPane a_component, int a_index) {
                return true;
            }
        };
        setVisibleButtonEsquina(getTabCount()>0);
    }
    public TabAcceptor getAcceptor() {
        return m_acceptor;
    }

    public void setAcceptor(TabAcceptor a_value) {
        m_acceptor = a_value;
    }

    private TabTransferData getTabTransferData(DropTargetDropEvent a_event) {
        try {
            TabTransferData data = (TabTransferData) a_event.getTransferable().getTransferData(FLAVOR);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private TabTransferData getTabTransferData(DropTargetDragEvent a_event) {
        try {
            TabTransferData data = (TabTransferData) a_event.getTransferable().getTransferData(FLAVOR);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private TabTransferData getTabTransferData(DragSourceDragEvent a_event) {
        try {
            TabTransferData data = (TabTransferData) a_event.getDragSourceContext().getTransferable().getTransferData(FLAVOR);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    class TabTransferable implements Transferable {
        private TabTransferData m_data = null;

        public TabTransferable(TabbedPane a_tabbedPane, int a_tabIndex) {
            m_data = new TabTransferData(TabbedPane.this, a_tabIndex);
        }

        public Object getTransferData(DataFlavor flavor) {
            return m_data;
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] f = new DataFlavor[1];
            f[0] = FLAVOR;
            return f;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.getHumanPresentableName().equals(NAME);
        }
    }

    static class TabTransferData {
        private TabbedPane m_tabbedPane = null;
        private int m_tabIndex = -1;

        public TabTransferData() {
        }

        public TabTransferData(TabbedPane a_tabbedPane, int a_tabIndex) {
            m_tabbedPane = a_tabbedPane;
            m_tabIndex = a_tabIndex;
        }

        public TabbedPane getTabbedPane() {
            return m_tabbedPane;
        }

        public void setTabbedPane(TabbedPane pane) {
            m_tabbedPane = pane;
        }

        public int getTabIndex() {
            return m_tabIndex;
        }

        public void setTabIndex(int index) {
            m_tabIndex = index;
        }
    }

    private Point buildGhostLocation(Point a_location) {
        Point retval = new Point(a_location);

        switch (getTabPlacement()) {
            case JTabbedPane.TOP: {
                retval.y = 1;
                retval.x -= s_glassPane.getGhostWidth() / 2;
            } break;

            case JTabbedPane.BOTTOM: {
                retval.y = getHeight() - 1 - s_glassPane.getGhostHeight();
                retval.x -= s_glassPane.getGhostWidth() / 2;
            } break;

            case JTabbedPane.LEFT: {
                retval.x = 1;
                retval.y -= s_glassPane.getGhostHeight() / 2;
            } break;

            case JTabbedPane.RIGHT: {
                retval.x = getWidth() - 1 - s_glassPane.getGhostWidth();
                retval.y -= s_glassPane.getGhostHeight() / 2;
            } break;
        } // switch

        retval = SwingUtilities.convertPoint(TabbedPane.this, retval, s_glassPane);
        return retval;
    }

    class CDropTargetListener implements DropTargetListener {
        public void dragEnter(DropTargetDragEvent e) {
            if (isDragAcceptable(e)) {
                e.acceptDrag(e.getDropAction());
            } else {
                e.rejectDrag();
            } // if
        }

        public void dragExit(DropTargetEvent e) {
            m_isDrawRect = false;
        }

        public void dropActionChanged(DropTargetDragEvent e) {
        }

        public void dragOver(final DropTargetDragEvent e) {
            TabTransferData data = getTabTransferData(e);

            if (getTabPlacement() == JTabbedPane.TOP || getTabPlacement() == JTabbedPane.BOTTOM) {
                initTargetLeftRightLine(getTargetTabIndex(e.getLocation()), data);
            } else {
                initTargetTopBottomLine(getTargetTabIndex(e.getLocation()), data);
            } // if-else

            repaint();
            if (hasGhost()) {
                s_glassPane.setPoint(buildGhostLocation(e.getLocation()));
                s_glassPane.repaint();
            }
        }

        public void drop(DropTargetDropEvent a_event) {
            if (isDropAcceptable(a_event)) {
                convertTab(getTabTransferData(a_event),
                        getTargetTabIndex(a_event.getLocation()));
                a_event.dropComplete(true);
            } else {
                a_event.dropComplete(false);
            }

            m_isDrawRect = false;
            repaint();
        }

        public boolean isDragAcceptable(DropTargetDragEvent e) {
            Transferable t = e.getTransferable();
            if (t == null) {
                return false;
            } // if

            DataFlavor[] flavor = e.getCurrentDataFlavors();
            if (!t.isDataFlavorSupported(flavor[0])) {
                return false;
            }
            TabTransferData data = getTabTransferData(e);

            if (TabbedPane.this == data.getTabbedPane()
                    && data.getTabIndex() >= 0) {
                return true;
            }

            if (TabbedPane.this != data.getTabbedPane()) {
                if (m_acceptor != null) {
                    return m_acceptor.isDropAcceptable(data.getTabbedPane(), data.getTabIndex());
                }
            }

            return false;
        }

        public boolean isDropAcceptable(DropTargetDropEvent e) {
            Transferable t = e.getTransferable();
            if (t == null) {
                return false;
            }

            DataFlavor[] flavor = e.getCurrentDataFlavors();
            if (!t.isDataFlavorSupported(flavor[0])) {
                return false;
            }

            TabTransferData data = getTabTransferData(e);

            if (TabbedPane.this == data.getTabbedPane()
                    && data.getTabIndex() >= 0) {
                return true;
            }

            if (TabbedPane.this != data.getTabbedPane()) {
                if (m_acceptor != null) {
                    return m_acceptor.isDropAcceptable(data.getTabbedPane(), data.getTabIndex());
                }
            }

            return false;
        }
    }

    private boolean m_hasGhost = true;

    public void setPaintGhost(boolean flag) {
        m_hasGhost = flag;
    }

    public boolean hasGhost() {
        return m_hasGhost;
    }

    /**
     * returns potential index for drop.
     * @param a_point point given in the drop site component's coordinate
     * @return returns potential index for drop.
     */
    private int getTargetTabIndex(Point a_point) {
        boolean isTopOrBottom = getTabPlacement() == JTabbedPane.TOP
                || getTabPlacement() == JTabbedPane.BOTTOM;

        // if the pane is empty, the target index is always zero.
        if (getTabCount() == 0) {
            return 0;
        } // if

        for (int i = 0; i < getTabCount(); i++) {
            Rectangle r = getBoundsAt(i);
            if (isTopOrBottom) {
                r.setRect(r.x - r.width / 2, r.y, r.width, r.height);
            } else {
                r.setRect(r.x, r.y - r.height / 2, r.width, r.height);
            } // if-else

            if (r.contains(a_point)) {
                return i;
            } // if
        } // for

        Rectangle r = getBoundsAt(getTabCount() - 1);
        if (isTopOrBottom) {
            int x = r.x + r.width / 2;
            r.setRect(x, r.y, getWidth() - x, r.height);
        } else {
            int y = r.y + r.height / 2;
            r.setRect(r.x, y, r.width, getHeight() - y);
        } // if-else

        return r.contains(a_point) ? getTabCount() : -1;
    }

    private void convertTab(TabTransferData a_data, int a_targetIndex) {
        TabbedPane source = a_data.getTabbedPane();
        int sourceIndex = a_data.getTabIndex();
        if (sourceIndex < 0) {
            return;
        }

        Component cmp = source.getComponentAt(sourceIndex);
        String str = source.getTitleAt(sourceIndex);
        Icon icon=source.getIconAt(sourceIndex);
        if (this != source) {
            source.remove(sourceIndex);
            if (a_targetIndex == getTabCount()) {
                addTab(str, cmp);
            } else {
                if (a_targetIndex < 0) {
                    a_targetIndex = 0;
                }
                insertTab(str, icon, cmp, null, a_targetIndex);
            }

            setSelectedComponent(cmp);
            return;
        }

        if (a_targetIndex < 0 || sourceIndex == a_targetIndex) {
            return;
        }
        if (a_targetIndex == getTabCount()) {
            source.remove(sourceIndex);
            addTab(str, icon,cmp);
            setSelectedIndex(getTabCount() - 1);
        } else if (sourceIndex > a_targetIndex) {
            source.remove(sourceIndex);
            insertTab(str, icon, cmp, null, a_targetIndex);
            setSelectedIndex(a_targetIndex);
        } else {
            source.remove(sourceIndex);
            insertTab(str, icon, cmp, null, a_targetIndex - 1);
            setSelectedIndex(a_targetIndex - 1);
        }
    }

    private void initTargetLeftRightLine(int next, TabTransferData a_data) {
        if (next < 0) {
            m_lineRect.setRect(0, 0, 0, 0);
            m_isDrawRect = false;
            return;
        }
        if ((a_data.getTabbedPane() == this)
                && (a_data.getTabIndex() == next
                || next - a_data.getTabIndex() == 1)) {
            m_lineRect.setRect(0, 0, 0, 0);
            m_isDrawRect = false;
        } else if (getTabCount() == 0) {
            m_lineRect.setRect(0, 0, 0, 0);
            m_isDrawRect = false;
            return;
        } else if (next == 0) {
            Rectangle rect = getBoundsAt(0);
            m_lineRect.setRect(-LINEWIDTH, rect.y, LINEWIDTH, rect.height);
            m_isDrawRect = true;
        } else if (next == getTabCount()) {
            Rectangle rect = getBoundsAt(getTabCount() - 1);
            m_lineRect.setRect(rect.x + rect.width - LINEWIDTH , rect.y,
                    LINEWIDTH, rect.height);
            m_isDrawRect = true;
        } else {
            Rectangle rect = getBoundsAt(next - 1);
            m_lineRect.setRect(rect.x + rect.width - LINEWIDTH , rect.y,
                    LINEWIDTH, rect.height);
            m_isDrawRect = true;
        }
    }

    private void initTargetTopBottomLine(int next, TabTransferData a_data) {
        if (next < 0) {
            m_lineRect.setRect(0, 0, 0, 0);
            m_isDrawRect = false;
            return;
        }

        if ((a_data.getTabbedPane() == this)
                && (a_data.getTabIndex() == next
                || next - a_data.getTabIndex() == 1)) {
            m_lineRect.setRect(0, 0, 0, 0);
            m_isDrawRect = false;
        } else if (getTabCount() == 0) {
            m_lineRect.setRect(0, 0, 0, 0);
            m_isDrawRect = false;
            return;
        } else if (next == getTabCount()) {
            Rectangle rect = getBoundsAt(getTabCount() - 1);
            m_lineRect.setRect(rect.x, rect.y + rect.height - LINEWIDTH,
                    rect.width, LINEWIDTH);
            m_isDrawRect = true;
        } else if (next == 0) {
            Rectangle rect = getBoundsAt(0);
            m_lineRect.setRect(rect.x, -LINEWIDTH, rect.width, LINEWIDTH);
            m_isDrawRect = true;
        } else {
            Rectangle rect = getBoundsAt(next - 1);
            m_lineRect.setRect(rect.x, rect.y + rect.height - LINEWIDTH ,
                    rect.width, LINEWIDTH);
            m_isDrawRect = true;
        }
    }

    private void initGlassPane(Component c, Point tabPt, int a_tabIndex) {
        getRootPane().setGlassPane(s_glassPane);
        if (hasGhost()) {
            Rectangle rect = getBoundsAt(a_tabIndex);
            BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            c.paint(g);
            image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
            s_glassPane.setImage(image);
        } // if

        s_glassPane.setPoint(buildGhostLocation(tabPt));
        s_glassPane.setVisible(true);
    }

    private Rectangle getTabAreaBound() {
        Rectangle lastTab = getUI().getTabBounds(this, getTabCount() - 1);
        return new Rectangle(0, 0, getWidth(), lastTab.y + lastTab.height);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (m_isDrawRect) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(m_lineColor);
            g2.fill(m_lineRect);
        }
    }

    public interface TabAcceptor {
        boolean isDropAcceptable(TabbedPane a_component, int a_index);
    }
}

class GhostGlassPane extends JPanel {
    public static final long serialVersionUID = 1L;
    private final AlphaComposite m_composite;

    private Point m_location = new Point(0, 0);

    private BufferedImage m_draggingGhost = null;

    public GhostGlassPane() {
        setOpaque(false);
        m_composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
    }

    public void setImage(BufferedImage draggingGhost) {
        m_draggingGhost = draggingGhost;
    }

    public void setPoint(Point a_location) {
        m_location.x = a_location.x;
        m_location.y = a_location.y;
    }

    public int getGhostWidth() {
        if (m_draggingGhost == null) {
            return 0;
        } // if
        return m_draggingGhost.getWidth(this);
    }

    public int getGhostHeight() {
        if (m_draggingGhost == null) {
            return 0;
        } // if

        return m_draggingGhost.getHeight(this);
    }

    public void paintComponent(Graphics g) {
        if (m_draggingGhost == null) {
            return;
        } // if
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(m_composite);
        g2.drawImage(m_draggingGhost, (int) m_location.getX(), (int) m_location.getY(), null);
    }

}
