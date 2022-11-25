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
    private Shape rectfalse1;
    private Shape rectfalse2;
    private Shape rectfalse3;
    private Shape rectfalse4;

    public ImagePanel() throws IOException {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(isInside(e.getPoint(),rectangule1)){
                    isRelesed=false;
                    startDrag=new Point((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMaxY());
                    endDrag=new Point((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMinY());
                }else if(isInside(e.getPoint(),rectangule2)){
                    isRelesed=false;
                    startDrag=new Point((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMaxY());
                    endDrag=new Point((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMinY());
                }else if(isInside(e.getPoint(),rectangule3)){
                    isRelesed=false;
                    startDrag=new Point((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMinY());
                    endDrag=new Point((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMaxY());
                } else if(isInside(e.getPoint(),rectangule4)) {
                    isRelesed=false;
                    startDrag=new Point((int) shape.getBounds().getMinX(), (int) shape.getBounds().getMinY());
                    endDrag=new Point((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMaxY());
                } else if(isRelesed&&isInside(e.getPoint(),shape)){
                    diferenceX=e.getX()-shape.getBounds().x;
                    diferenceY=e.getY()-shape.getBounds().y;
                }else  if(isInside(e.getPoint(),shapeImage)){
                    isRelesed=false;
                    startDrag = new Point(e.getX(), e.getY());
                    endDrag = startDrag;
                    repaint();
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
                    endDrag = getEndDrag(e.getPoint());
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
            graphics2D.setPaint(Color.BLACK);
            graphics2D.draw(shapeImage);
            graphics2D.setPaint(Color.LIGHT_GRAY);
            if (startDrag != null && endDrag != null) {
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                graphics2D.setPaint(new Color(0x1B87F8));
                shape = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                graphics2D.draw(shape);
            }
            if(shape!=null){
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                graphics2D.setPaint(new Color(0x1B87F8));
                graphics2D.draw(shape);
                rectangule1=new Rectangle((int) shape.getBounds().getMinX()-8, (int) shape.getBounds().getMinY()-8,8,8);
                rectangule2=new Rectangle((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMinY()-8,8,8);
                rectangule3=new Rectangle((int) shape.getBounds().getMinX()-8, (int) shape.getBounds().getMaxY(),8,8);
                rectangule4=new Rectangle((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMaxY(),8,8);
                graphics2D.setPaint(new Color(0x1B87F8));
                graphics2D.draw(rectangule1);
                graphics2D.draw(rectangule2);
                graphics2D.draw(rectangule3);
                graphics2D.draw(rectangule4);
                graphics2D.fill(rectangule1);
                graphics2D.fill(rectangule2);
                graphics2D.fill(rectangule3);
                graphics2D.fill(rectangule4);

                loadRectanglesFalses();
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                graphics2D.setPaint(new Color(0x000000));
                graphics2D.fill(rectfalse1);
                graphics2D.fill(rectfalse2);
                graphics2D.fill(rectfalse3);
                graphics2D.fill(rectfalse4);
            }
        }
    }
    private void loadRectanglesFalses(){
        rectfalse1=makeRectangle2((int)shapeImage.getBounds().getMinX(),(int)shapeImage.getBounds().getMinY(), (int)shapeImage.getBounds().getMaxX(), (int) shape.getBounds().getMinY());
        rectfalse4=makeRectangle2((int)shapeImage.getBounds().getMinX(), (int) shape.getBounds().getMaxY(), (int) shapeImage.getBounds().getMaxX(), (int) shapeImage.getBounds().getMaxY());
        rectfalse2=makeRectangle2((int)shapeImage.getBounds().getMinX(), (int) shape.getBounds().getMinY(), (int) shape.getBounds().getMinX(), (int) shape.getBounds().getMaxY());
        rectfalse3=makeRectangle2((int) shape.getBounds().getMaxX(), (int) shape.getBounds().getMinY(), (int) shapeImage.getBounds().getMaxX(), (int) shape.getBounds().getMaxY());
    }
    private void moveRectangle(Point point) {
        int startX= (int) (point.getX()-diferenceX);
        int startY= (int) (point.getY()-diferenceY);
        Shape shape1 = new Rectangle2D.Double(startX, startY, shape.getBounds().width, shape.getBounds().height);
        if(shape1.getBounds().getMinX()<shapeImage.getBounds().getMinX()) {
            startX=shapeImage.getBounds().x;
        }
        if(shape1.getBounds().getMaxX()>shapeImage.getBounds().getMaxX()){
            startX=(int) shape.getBounds().getMinX();
        }

        if(shape1.getBounds().getMinY()<shapeImage.getBounds().getMinY()) {
            startY=shapeImage.getBounds().y;
        }
        if(shape1.getBounds().getMaxY()>shapeImage.getBounds().getMaxY()){
            startY= (int) shape.getBounds().getMinY();
        }
        shape=new Rectangle2D.Double(startX,startY,shape.getBounds().width,shape.getBounds().height);
    }

    private Point getEndDrag(Point point){
        if(shapeImage!=null){
            if(point.x<(int)shapeImage.getBounds().getMinX()) {
                point.setLocation(shapeImage.getBounds().getMinX(),point.getY());
            }
            if(point.x>(int) shapeImage.getBounds().getMaxX()){
                point.setLocation(shapeImage.getBounds().getMaxX(),point.getY());
            }
            if(point.y<(int)shapeImage.getBounds().getMinY()) {
                point.setLocation(point.getX(),shapeImage.getBounds().getMinY());
            }
            if(point.y>(int)shapeImage.getBounds().getMaxY()) {
                point.setLocation(point.getX(),shapeImage.getBounds().getMaxY());
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
    private Shape makeRectangle2(int x1, int y1, int x2, int y2){
        return new Rectangle2D.Float(x1,y1,x2-x1,y2-y1);
    }

    private Shape makeRectangle(int x1, int y1, int x2, int y2) {
        int startX = 0;
        int startY = 0;

        if(shape!=null){
            startX= (int) shape.getBounds().getMinX();
            startY= (int) shape.getBounds().getMinY();
        }

        int width1=Math.abs(x1 - x2);
        int height1=Math.abs(y1 - y2);

        if(width1>width&&shape!=null){
            width1= width;
        }else{
            startX=Math.min(x1,x2);
        }

        if(height1>height&&shape!=null){
            height1= height;
        }else{
            startY=Math.min(y1,y2);
        }
        return new Rectangle2D.Float(startX,startY,width1,height1);
    }

    public void loadImage(String inputImage){
        try {
            Image image = ImageIO.read(new File(inputImage));
            if(image!=null){
                width=image.getWidth(this);
                height=image.getHeight(this);
                if(width>534.00||height>534.00){
                    double percen= Math.min(534.00/width,534.00/height);
                    width = (int) ((int) (percen*width));
                    height = (int) ((int) (percen*height));
                }
                image=image.getScaledInstance(width, height,  Image.SCALE_SMOOTH);
                bufferedImage = new BufferedImage(image.getWidth(this), image.getHeight(this), BufferedImage.TYPE_INT_ARGB);
                Graphics2D bGr = bufferedImage.createGraphics();
                bGr.drawImage(image, 0, 0, this);
                bGr.dispose();
                repaint();
                shapeImage=new Rectangle2D.Double(((double) getWidth()-bufferedImage.getWidth())/2,((double)getHeight()-bufferedImage.getHeight())/2,bufferedImage.getWidth(),bufferedImage.getHeight());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
