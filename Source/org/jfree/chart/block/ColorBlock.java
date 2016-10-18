/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.block.AbstractBlock;
import org.jfree.chart.block.Block;

public class ColorBlock
extends AbstractBlock
implements Block {
    private Paint paint;

    public ColorBlock(Paint paint, double width, double height) {
        this.paint = paint;
        this.setWidth(width);
        this.setHeight(height);
    }

    public void draw(Graphics2D g2, Rectangle2D area) {
        Rectangle2D bounds = this.getBounds();
        g2.setPaint(this.paint);
        g2.fill(bounds);
    }

    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        this.draw(g2, area);
        return null;
    }
}

