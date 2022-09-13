package com.babas.custom;


import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.Interpolator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImageSlide extends JComponent {
    private final List<Icon> images;
    private ImageSplit imageSplit;
    private int indexOld = -1;
    private final Animator animator;
    private float animate;

    public ImageSlide() {
        images = new ArrayList<>();
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animate = fraction;
                repaint();
            }

            @Override
            public void end() {
                imageSplit = null;
            }
        };
        animator = new Animator(2000, target);
        animator.setResolution(1);
        animator.setInterpolator(new Interpolator() {
            @Override
            public float interpolate(float f) {
                return easeOutBounce(f);
            }
        });
    }

    private float easeOutBounce(float x) {
        float n1 = 7.5625f;
        float d1 = 2.75f;
        double v;
        if (x < 1 / d1) {
            v = n1 * x * x;
        } else if (x < 2 / d1) {
            v = n1 * (x -= 1.5 / d1) * x + 0.75;
        } else if (x < 2.5 / d1) {
            v = n1 * (x -= 2.25 / d1) * x + 0.9375;
        } else {
            v = n1 * (x -= 2.625 / d1) * x + 0.984375;
        }
        return (float) v;
    }

    public void addImage(Icon image) {
        images.add(image);
        if (indexOld == -1) {
            indexOld = 0;
            repaint();
        }
    }

    private void showImage(int index) {
        if (index < images.size()) {
            if (!animator.isRunning()) {
                imageSplit = new ImageSplit(toImage(images.get(indexOld)), getSize(), getAutoSize(images.get(indexOld)));
                indexOld = index;
                animator.start();
            }
        }
    }

    public void toNext(){
        if(!images.isEmpty()){
            if(indexOld==-1||images.size()-1==indexOld){
                showImage(0);
            }else{
                showImage(indexOld+1);
            }
        }
    }

    public void toPrevious(){
        if(!images.isEmpty()){
            if(indexOld==0){
                showImage(images.size()-1);
            }else{
                showImage(indexOld-1);
            }
        }
    }
    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int width = getWidth();
        int height = getHeight();
        if (indexOld >= 0) {
            Icon image = images.get(indexOld);
            Rectangle size = getAutoSize(image);
            g2.drawImage(toImage(image), size.getLocation().x, size.getLocation().y, size.getSize().width, size.getSize().height, null);
        }
        if (imageSplit != null) {
            int x = (int) (animate * width);
            g2.drawImage(imageSplit.getImageLeft(), -x, 0, null);
            g2.drawImage(imageSplit.getImageRight(), x, 0, null);
        }
        g2.dispose();
        super.paint(grphcs);
    }

    private Rectangle getAutoSize(Icon image) {
        int w = getWidth();
        int h = getHeight();
        int iw = image.getIconWidth();
        int ih = image.getIconHeight();
        if (w > iw) {
            w = iw;
        }
        if (h > ih) {
            h = ih;
        }
        double xScale = (double) w / iw;
        double yScale = (double) h / ih;
        double scale = Math.max(xScale, yScale);
        int width = (int) (scale * iw);
        int height = (int) (scale * ih);
        int x = getWidth() / 2 - (width / 2);
        int y = getHeight() / 2 - (height / 2);
        return new Rectangle(new Point(x, y), new Dimension(width, height));
    }

    private Image toImage(Icon icon) {
        return ((ImageIcon) icon).getImage();
    }
}
