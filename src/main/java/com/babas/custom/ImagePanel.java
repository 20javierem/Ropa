package com.babas.custom;

import com.babas.views.dialogs.DCrop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage bufferedImage;
    private Shape shape = null;
    private Shape shapeImage;
    private Point startDrag, endDrag;
    private int diferenceX,diferenceY;
    private Shape rectangule1;
    private Shape rectangule2;
    private Shape rectangule3;
    private Shape rectangule4;
    private boolean isRelesed=false;
    private int width,height;
    private boolean nw=false;
    private boolean ne=false;
    private boolean sw=false;
    private boolean se=true;

    public ImagePanel() throws IOException {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(isInside(e.getPoint(),shapeImage)){
                    if(isInside(e.getPoint(),rectangule1)){
                        nw=true;
                        ne=false;
                        sw=false;
                        se=false;
                        isRelesed=false;
                        startDrag=new Point((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMinY());
                        endDrag=new Point((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMaxY());
                    }else if(isInside(e.getPoint(),rectangule2)){
                        nw=false;
                        ne=true;
                        sw=false;
                        se=false;
                        isRelesed=false;
                        startDrag=new Point((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMinY());
                        endDrag=new Point((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMaxY());
                    }else if(isInside(e.getPoint(),rectangule3)){
                        nw=false;
                        ne=false;
                        sw=true;
                        se=false;
                        isRelesed=false;
                        startDrag=new Point((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMinY());
                        endDrag=new Point((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMaxY());
                    } else if(isInside(e.getPoint(),rectangule4)) {
                        nw=false;
                        ne=false;
                        sw=false;
                        se=true;
                        isRelesed=false;
                        startDrag=new Point((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMinY());
                        endDrag=new Point((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMaxY());
                    } else if(isRelesed&&isInside(e.getPoint(),shape)){
                        diferenceX=e.getX()-shape.getBounds().x;
                        diferenceY=e.getY()-shape.getBounds().y;
                    }else{
                        nw=false;
                        ne=false;
                        sw=false;
                        se=true;
                        isRelesed=false;
                        startDrag = new Point(e.getX(), e.getY());
                        endDrag = startDrag;
                        repaint();
                    }
                }else{
                    shape=null;
                    repaint();
                }
            }
            public void mouseReleased(MouseEvent e) {
                isRelesed=true;
                if (isInside(e.getPoint(), shapeImage)) {
                    if(endDrag!=null && startDrag!=null) {
                        try {
                            startDrag = null;
                            endDrag = null;
                            repaint();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }else{
                    startDrag = null;
                    endDrag = null;
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if(isRelesed&&endDrag==null){
                    if(isInside(e.getPoint(),shape)){
                        moveRectangle(e.getPoint());
                        repaint();
                    }
                }else{
                    if(nw){
                        startDrag = getEndDrag(e.getPoint());
                    }else if(ne||sw){
                        startDrag =getEndDrag(e.getPoint());
                        endDrag =getEndDrag(e.getPoint());
                    }else if(se){
                        endDrag = getEndDrag(e.getPoint());
                    }
                    repaint();
                }
            }
            public void mouseMoved(MouseEvent e) {
                if(isInside(e.getPoint(),rectangule1)){
                    setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                }else if(isInside(e.getPoint(),rectangule2)){
                    setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
                }else if(isInside(e.getPoint(),rectangule3)){
                    setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
                }else if(isInside(e.getPoint(),rectangule4)){
                    setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                }else if(isInside(e.getPoint(),shape)){
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }else{
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(bufferedImage!=null){
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.drawImage(bufferedImage,(getWidth()-bufferedImage.getWidth())/2,(getHeight()-bufferedImage.getHeight())/2,null);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            if (shape != null) {
                graphics2D.setPaint(Color.RED);
                graphics2D.draw(shape);
                graphics2D.setPaint(Color.LIGHT_GRAY);
                graphics2D.fill(shape);
            }
            if (startDrag != null && endDrag != null) {
                graphics2D.setPaint(Color.LIGHT_GRAY);
                shape = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                graphics2D.draw(shape);
            }
            if(shape!=null){
                rectangule1=new Rectangle((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMinY(),8,8);
                rectangule2=new Rectangle((int) shape.getBounds().getMaxX()-8, (int) shape.getBounds().getMinY(),8,8);
                rectangule3=new Rectangle((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMaxY()-8,8,8);
                rectangule4=new Rectangle((int) shape.getBounds().getMaxX()-8, (int) shape.getBounds().getMaxY()-8,8,8);
                graphics2D.setPaint(Color.black);
                graphics2D.draw(rectangule1);
                graphics2D.draw(rectangule2);
                graphics2D.draw(rectangule3);
                graphics2D.draw(rectangule4);
            }
        }
        setBackground(new Color(87, 236, 38));
    }

    private void moveRectangle(Point point) {
        int startX= (int) (point.getX()-diferenceX);
        int startY= (int) (point.getY()-diferenceY);
        Shape shape1 = new Rectangle2D.Double(startX, startY, shape.getBounds().width, shape.getBounds().height);
        if(shape1.getBounds().getMinX()<shapeImage.getBounds().getMinX()||shape1.getBounds().getMaxX()>shapeImage.getBounds().getMaxX()) {
            startX=shape.getBounds().x;
        }
        if(shape1.getBounds().getMinY()<shapeImage.getBounds().getMinY()||shape1.getBounds().getMaxY()>shapeImage.getBounds().getMaxY()) {
            startY=shape.getBounds().y;
        }
        shape=new Rectangle2D.Double(startX,startY,shape.getBounds().width,shape.getBounds().height);
    }

    private Point getEndDrag(Point point){
        if(shapeImage!=null&&shape!=null){
            if(point.x<shapeImage.getBounds().getMinX()) {
                point.setLocation(shape.getBounds().getMinX(),point.getY());
            }else if(point.x>shapeImage.getBounds().getMaxX()){
                point.setLocation(shape.getBounds().getMaxX(),point.getY());
            }
            if(point.y<shapeImage.getBounds().getMinY()) {
                point.setLocation(point.getX(),shape.getBounds().getMinY());
            }else if(point.y>shapeImage.getBounds().getMaxY()) {
                point.setLocation(point.getX(),shape.getBounds().getMaxY());
            }
        }
        return point;
    }

    private boolean isInside(Point point,Shape shape){
        if(shape!=null){
            return shape.contains(point);
        }else{
            return false;
        }
    }
    public BufferedImage getImageSelected(){
        if(shape!=null) {
            return bufferedImage.getSubimage(shape.getBounds().x-(getWidth()-bufferedImage.getWidth())/2, shape.getBounds().y-(getHeight()-bufferedImage.getHeight())/2, shape.getBounds().width, shape.getBounds().height);
        }
        return null;
    }
    private Shape makeRectangle(int x1, int y1, int x2, int y2) {
        int startX = 0;
        int startY = 0;
        if(shape!=null){
            startX=shape.getBounds().x;
            startY=shape.getBounds().y;
        }
        int width1=Math.abs(x1 - x2);
        int height1=Math.abs(y1 - y2);

        if(width1>width&&shape!=null){
            width1=shape.getBounds().width;
        }else{
            startX=Math.min(x1,x2);
        }
        if(height1>height&&shape!=null){
            height1=shape.getBounds().height;
        }else{
            startY=Math.min(y1,y2);
        }
        return new Rectangle2D.Float(startX,startY,width1,height1);
    }

    public void loadImage(String inputImage){
        try {
            Image image = ImageIO.read(new File(inputImage));
            width=image.getWidth(this);
            height=image.getHeight(this);
            if(width>542||height>542){
                double percen= Math.min(542.00/width,542.00/height);
                width= (int) (percen*width);
                height=(int) (percen*height);
            }
            image=image.getScaledInstance(width, height,  Image.SCALE_SMOOTH);
            bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = bufferedImage.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            bGr.dispose();
            repaint();
            shapeImage=new Rectangle2D.Double(((double) getWidth()-bufferedImage.getWidth())/2,((double)getHeight()-bufferedImage.getHeight())/2,bufferedImage.getWidth(),bufferedImage.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
