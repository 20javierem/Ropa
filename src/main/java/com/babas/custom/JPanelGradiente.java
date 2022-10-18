package com.babas.custom;

import javax.swing.*;
import java.awt.*;

public class JPanelGradiente extends JPanel {
    private Color color1;
    private Color color2;
    private Color color3;

    public JPanelGradiente(Color color1,Color color2,Color color3){
        this.color1=color1;
        this.color2=color2;
        this.color3=color3;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
//        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth(), h = getHeight();
//        GradientPaint gp = new GradientPaint(0, 0, color1, w, 0, Color.BLACK);
//        g2d.setPaint(gp);
//        g2d.fillRect(0, 0, w, h);
//        GradientPaint shade = new GradientPaint(0, 0, color1, 0, h, color3);
//        g2d.setPaint(shade);
//        g2d.fillRect(0, 0, w, h);
        GradientPaint primary = new GradientPaint(
                0f, 0f, color1, (w/2), 0f, color2);
        GradientPaint shade = new GradientPaint(
                (w/2), 0f, color2,
                w, 0f, color3);
        g2d.setPaint(primary);
        g.fillRect(0, 0, (w/2), h);
        g2d.setPaint(shade);
        g2d.fillRect((w/2), 0, w, h);
    }
}
