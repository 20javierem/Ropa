package com.babas.custom;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

public class ImageSplit {
    private BufferedImage imageLeft;
    private BufferedImage imageRight;

    public ImageSplit(Image image, Dimension size, Rectangle imageRec) {
        imageLeft = createImage(image, size, imageRec, false);
        imageRight = createImage(image, size, imageRec, true);
    }

    public BufferedImage getImageLeft() {
        return imageLeft;
    }

    public void setImageLeft(BufferedImage imageLeft) {
        this.imageLeft = imageLeft;
    }

    public BufferedImage getImageRight() {
        return imageRight;
    }

    public void setImageRight(BufferedImage imageRight) {
        this.imageRight = imageRight;
    }

    private BufferedImage createImage(Image image, Dimension size, Rectangle imageRec, boolean right) {
        BufferedImage img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.fill(createShape(right, size));
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(image, imageRec.getLocation().x, imageRec.getLocation().y, imageRec.getSize().width, imageRec.getSize().height, null);
        g2.dispose();
        return img;
    }

    private Shape createShape(boolean right, Dimension size) {
        Area area = new Area(new Rectangle(new Point(0, 0), size));
        int[] x = {0, size.width, 0};
        int[] y = {0, 0, size.height};
        Area sp = new Area(new Polygon(x, y, x.length));
        if (right) {
            area.subtract(sp);
        } else {
            area.intersect(sp);
        }
        return area;
    }
}
