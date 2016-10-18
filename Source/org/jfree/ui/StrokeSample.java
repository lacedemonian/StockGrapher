/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class StrokeSample
extends JComponent
implements ListCellRenderer {
    private Stroke stroke;
    private Dimension preferredSize;

    public StrokeSample(Stroke stroke) {
        this.stroke = stroke;
        this.preferredSize = new Dimension(80, 18);
    }

    public Stroke getStroke() {
        return this.stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        this.repaint();
    }

    public Dimension getPreferredSize() {
        return this.preferredSize;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension size = this.getSize();
        Insets insets = this.getInsets();
        double xx = insets.left;
        double yy = insets.top;
        double ww = size.getWidth() - (double)insets.left - (double)insets.right;
        double hh = size.getHeight() - (double)insets.top - (double)insets.bottom;
        Point2D.Double one = new Point2D.Double(xx + 6.0, yy + hh / 2.0);
        Point2D.Double two = new Point2D.Double(xx + ww - 6.0, yy + hh / 2.0);
        Ellipse2D.Double circle1 = new Ellipse2D.Double(one.getX() - 5.0, one.getY() - 5.0, 10.0, 10.0);
        Ellipse2D.Double circle2 = new Ellipse2D.Double(two.getX() - 6.0, two.getY() - 5.0, 10.0, 10.0);
        g2.draw(circle1);
        g2.fill(circle1);
        g2.draw(circle2);
        g2.fill(circle2);
        Line2D.Double line = new Line2D.Double(one, two);
        if (this.stroke != null) {
            g2.setStroke(this.stroke);
        } else {
            g2.setStroke(new BasicStroke(0.0f));
        }
        g2.draw(line);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof StrokeSample) {
            StrokeSample in = (StrokeSample)value;
            this.setStroke(in.getStroke());
        }
        return this;
    }
}

