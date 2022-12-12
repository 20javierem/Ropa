package com.babas.custom;
import com.babas.App;
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

    private Double maxX=0.0;
    private Double maxY=0.0;
    private Double minX=0.0;
    private Double minY=0.0;
    private final JButton buttonEsquina=new JButton();
    private final JToolBar toolBar=new JToolBar();
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
        JMenuItem moveToLeft = new JMenuItem("Mover a la izquierda",new FlatSVGIcon(App.class.getResource("icons/svg/arrowCollapse.svg")));
        JMenuItem moveToRight = new JMenuItem("Mover a la derecha",new FlatSVGIcon(App.class.getResource("icons/svg/arrowExpand.svg")));
        JMenuItem closeTab = new JMenuItem("Cerrar Pestaña");
        JMenuItem closeOthers = new JMenuItem("Cerrar Otras Pestañas");
        JMenuItem closeAll = new JMenuItem("Cerrar Todas Las Pestañas");

        moveToLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getSelectedIndex()>0){
                    Component component=getSelectedComponent();
                    int newIndex=getSelectedIndex()-1;
                    String tittle=getTitleAt(getSelectedIndex());
                    String tooltip=getToolTipTextAt(getSelectedIndex());
                    removeTabAt(getSelectedIndex());
                    insertTab(tittle,null,component,tooltip,newIndex);
                    setSelectedIndex(newIndex);
                }
            }
        });
        moveToRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getSelectedIndex()<getTabCount()-1){
                    Component component=getSelectedComponent();
                    int newIndex=getSelectedIndex()+1;
                    String tittle=getTitleAt(getSelectedIndex());
                    String tooltip=getToolTipTextAt(getSelectedIndex());
                    removeTabAt(getSelectedIndex());
                    insertTab(tittle,null,component,tooltip,newIndex);
                    setSelectedIndex(newIndex);
                }
            }
        });
        closeTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTabAt(getSelectedIndex());
            }
        });
        closeOthers.addActionListener(new ActionListener() {
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
        closeAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
            }
        });
        pop_up.add(moveToLeft);
        pop_up.add(moveToRight);
        pop_up.add(closeTab);
        pop_up.addSeparator();
        pop_up.add(closeOthers);
        pop_up.add(closeAll);
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
        toolBar.setVisible(false);
    }


    @Override
    public void paint(Graphics g){
        if(Utilities.iconCompanyx420x420!=null){
            Image image=ImageSlide.toImage(Utilities.iconCompanyx420x420);
            int widthImage=image.getWidth(null);
            int heigthImage=image.getHeight(null);
            g.drawImage(image,(getWidth()/2)-(widthImage/2),(getHeight()/2)-(heigthImage/2),widthImage,heigthImage,null);
            setOpaque(false);
        }else{
            Image image=new ImageIcon(App.class.getResource("images/lojoJmoreno (1).png")).getImage();
            int widthImage=image.getWidth(null);
            int heigthImage=image.getHeight(null);
            g.drawImage(image,(getWidth()/2)-(widthImage/2),(getHeight()/2)-(heigthImage/2),widthImage,heigthImage,null);
            setOpaque(false);
        }
        super.paint(g);
    }
}
