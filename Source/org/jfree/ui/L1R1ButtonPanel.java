/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JPanel;

public class L1R1ButtonPanel
extends JPanel {
    private JButton left;
    private JButton right;

    public L1R1ButtonPanel(String leftLabel, String rightLabel) {
        this.setLayout(new BorderLayout());
        this.left = new JButton(leftLabel);
        this.right = new JButton(rightLabel);
        this.add((Component)this.left, "West");
        this.add((Component)this.right, "East");
    }

    public JButton getLeftButton() {
        return this.left;
    }

    public JButton getRightButton() {
        return this.right;
    }
}

