package com.babas.custom;

import com.babas.App;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTabbedPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.formdev.flatlaf.util.ColorFunctions.lighten;

public class TabbedPane extends FlatTabbedPane {

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
                setBackgroundAt(indexOfComponent(component),getBackground());
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
            setBackgroundAt(getSelectedIndex(),lighten(getBackground(),0.05f));
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
        Point point=new Point();
        JMenuItem moveToLeft = new JMenuItem("Mover a la izquierda",new FlatSVGIcon(App.class.getResource("icons/svg/arrowCollapse.svg")));
        JMenuItem moveToRight = new JMenuItem("Mover a la derecha",new FlatSVGIcon(App.class.getResource("icons/svg/arrowExpand.svg")));
        JMenuItem closeTab = new JMenuItem("Cerrar Pestaña");
        JMenuItem closeOthers = new JMenuItem("Cerrar Otras Pestañas");
        JMenuItem closeAll = new JMenuItem("Cerrar Todas Las Pestañas");

        moveToLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y)>0){
                    Component componentSelected=getSelectedComponent();
                    Component component=getComponentAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
                    int newIndex=getUI().tabForCoordinate(TabbedPane.this,point.x,point.y)-1;
                    String tittle=getTitleAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
                    String tooltip=getToolTipTextAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
                    removeTabAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
                    insertTab(tittle,null,component,tooltip,newIndex);
                    setSelectedComponent(componentSelected);
                }
            }
        });
        moveToRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y)<getTabCount()-1){
                    Component componentSelected=getSelectedComponent();
                    Component component=getComponentAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
                    int newIndex=getUI().tabForCoordinate(TabbedPane.this,point.x,point.y)+1;
                    String tittle=getTitleAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
                    String tooltip=getToolTipTextAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
                    removeTabAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
                    insertTab(tittle,null,component,tooltip,newIndex);
                    setSelectedComponent(componentSelected);
                }
            }
        });
        closeTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTabAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
            }
        });
        closeOthers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getSelectedIndex()!=-1){
                    if(getComponentAt(getSelectedIndex()) instanceof TabPane){
                        TabPane tabPane= (TabPane) getComponentAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
                        removeAll();
                        addTab(tabPane.getTitle(),tabPane.getIcon(),tabPane);
                    }else{
                        String tittle=getTitleAt(getSelectedIndex());
                        Component component=getComponentAt(getUI().tabForCoordinate(TabbedPane.this,point.x,point.y));
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
                    if(getUI().tabForCoordinate(TabbedPane.this,e.getX(),e.getY())!=-1){
                        point.setLocation(e.getPoint());
                        pop_up.show(getComponentAt(getMousePosition()),e.getX(),e.getY());
                    }
                }
            }
        });
        buttonEsquina.setIcon(new ImageIcon(App.class.getResource("icons/x24/menu.png")));
        buttonEsquina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                point.setLocation(getUI().getTabBounds(TabbedPane.this,getSelectedIndex()).getLocation());
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
