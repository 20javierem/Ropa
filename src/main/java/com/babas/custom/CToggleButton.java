package com.babas.custom;

import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;

public class CToggleButton extends FlatToggleButton {
    @Override
    public void updateUI() {
        super.updateUI();
        setTabSelectedBackground(new JPanel().getBackground().darker());
    }
}
