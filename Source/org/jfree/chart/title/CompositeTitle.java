/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.title;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.block.Arrangement;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.title.Title;
import org.jfree.ui.Size2D;

public class CompositeTitle
extends Title
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -6770854036232562290L;
    private BlockContainer container;

    public CompositeTitle() {
        this(new BlockContainer(new BorderArrangement()));
    }

    public CompositeTitle(BlockContainer container) {
        if (container == null) {
            throw new IllegalArgumentException("Null 'container' argument.");
        }
        this.container = container;
    }

    public BlockContainer getContainer() {
        return this.container;
    }

    public void setTitleContainer(BlockContainer container) {
        if (container == null) {
            throw new IllegalArgumentException("Null 'container' argument.");
        }
        this.container = container;
    }

    public Size2D arrange(Graphics2D g2, RectangleConstraint constraint) {
        RectangleConstraint contentConstraint = this.toContentConstraint(constraint);
        Size2D contentSize = this.container.arrange(g2, contentConstraint);
        return new Size2D(this.calculateTotalWidth(contentSize.getWidth()), this.calculateTotalHeight(contentSize.getHeight()));
    }

    public void draw(Graphics2D g2, Rectangle2D area) {
        area = this.trimMargin(area);
        this.drawBorder(g2, area);
        area = this.trimBorder(area);
        area = this.trimPadding(area);
        this.container.draw(g2, area);
    }

    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        this.draw(g2, area);
        return null;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CompositeTitle)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        CompositeTitle that = (CompositeTitle)obj;
        if (!this.container.equals(that.container)) {
            return false;
        }
        return true;
    }
}

