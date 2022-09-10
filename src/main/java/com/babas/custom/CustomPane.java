package com.babas.custom;

import javax.swing.*;

public class CustomPane extends JPanel {
    private int dark=1;

    public CustomPane(int dark){
        this.dark=dark;
    }

    public CustomPane(){

    }

    @Override
    public void updateUI() {
        JPanel panel=new JPanel();
        super.updateUI();
        for (int i=0;i<dark;i++){
            panel.setBackground(panel.getBackground().darker());
        }
        setBackground(panel.getBackground());
    }
}
