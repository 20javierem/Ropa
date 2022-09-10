package com.babas.custom;

import javax.swing.*;
import java.awt.*;

public class JPanelGradiente extends JPanel {
    private Color color1;
    private Color color2;
    public JPanelGradiente(Color color1,Color color2){
        this.color1=color1;
        this.color2=color2;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth(), h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
