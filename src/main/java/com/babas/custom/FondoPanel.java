package com.babas.custom;


import com.babas.App;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public class FondoPanel extends JPanel {
    private String fondo;
    private Image imagen;

    public FondoPanel(String fondo){
        this.fondo=fondo;
    }

    @Override
    public void paint(Graphics g){
        imagen=new FlatSVGIcon(App.class.getResource("icons/svg/"+fondo)).getImage();
        g.drawImage(imagen,0,0,getWidth(),getHeight(),this);
        setOpaque(false);
        super.paint(g);
    }
}
