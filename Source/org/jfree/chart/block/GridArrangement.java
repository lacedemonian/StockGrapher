/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.block.Arrangement;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.data.Range;
import org.jfree.ui.Size2D;

public class GridArrangement
implements Arrangement,
Serializable {
    private static final long serialVersionUID = -2563758090144655938L;
    private int rows;
    private int columns;

    public GridArrangement(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public void add(Block block, Object key) {
    }

    public Size2D arrange(BlockContainer container, Graphics2D g2, RectangleConstraint constraint) {
        LengthConstraintType w = constraint.getWidthConstraintType();
        LengthConstraintType h = constraint.getHeightConstraintType();
        if (w == LengthConstraintType.NONE) {
            if (h == LengthConstraintType.NONE) {
                return this.arrangeNN(container, g2);
            }
            if (h == LengthConstraintType.FIXED) {
                throw new RuntimeException("Not yet implemented.");
            }
            if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not yet implemented.");
            }
        } else if (w == LengthConstraintType.FIXED) {
            if (h == LengthConstraintType.NONE) {
                return this.arrangeFN(container, g2, constraint);
            }
            if (h == LengthConstraintType.FIXED) {
                return this.arrangeFF(container, g2, constraint);
            }
            if (h == LengthConstraintType.RANGE) {
                return this.arrangeFR(container, g2, constraint);
            }
        } else if (w == LengthConstraintType.RANGE) {
            if (h == LengthConstraintType.NONE) {
                throw new RuntimeException("Not yet implemented.");
            }
            if (h == LengthConstraintType.FIXED) {
                throw new RuntimeException("Not yet implemented.");
            }
            if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not yet implemented.");
            }
        }
        return new Size2D();
    }

    protected Size2D arrangeNN(BlockContainer container, Graphics2D g2) {
        double maxW = 0.0;
        double maxH = 0.0;
        List blocks = container.getBlocks();
        Iterator iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Block b = (Block)iterator.next();
            Size2D s = b.arrange(g2, RectangleConstraint.NONE);
            maxW = Math.max(maxW, s.width);
            maxH = Math.max(maxH, s.height);
        }
        double width = (double)this.columns * maxW;
        double height = (double)this.rows * maxH;
        RectangleConstraint c = new RectangleConstraint(width, height);
        return this.arrangeFF(container, g2, c);
    }

    protected Size2D arrangeFF(BlockContainer container, Graphics2D g2, RectangleConstraint constraint) {
        double width = constraint.getWidth() / (double)this.columns;
        double height = constraint.getHeight() / (double)this.rows;
        List blocks = container.getBlocks();
        for (int c = 0; c < this.columns; ++c) {
            int index;
            for (int r = 0; r < this.rows && (index = r * this.columns + c) != blocks.size(); ++r) {
                Block b = (Block)blocks.get(index);
                b.setBounds(new Rectangle2D.Double((double)c * width, (double)r * height, width, height));
            }
        }
        return new Size2D((double)this.columns * width, (double)this.rows * height);
    }

    protected Size2D arrangeFR(BlockContainer container, Graphics2D g2, RectangleConstraint constraint) {
        RectangleConstraint c1 = constraint.toUnconstrainedHeight();
        Size2D size1 = this.arrange(container, g2, c1);
        if (constraint.getHeightRange().contains(size1.getHeight())) {
            return size1;
        }
        double h = constraint.getHeightRange().constrain(size1.getHeight());
        RectangleConstraint c2 = constraint.toFixedHeight(h);
        return this.arrange(container, g2, c2);
    }

    protected Size2D arrangeFN(BlockContainer container, Graphics2D g2, RectangleConstraint constraint) {
        double width = constraint.getWidth() / (double)this.columns;
        RectangleConstraint constraint2 = constraint.toFixedWidth(width);
        List blocks = container.getBlocks();
        double maxH = 0.0;
        for (int r = 0; r < this.rows; ++r) {
            int index;
            for (int c = 0; c < this.columns && (index = r * this.columns + c) != blocks.size(); ++c) {
                Block b = (Block)blocks.get(index);
                Size2D s = b.arrange(g2, constraint2);
                maxH = Math.max(maxH, s.getHeight());
            }
        }
        RectangleConstraint constraint3 = constraint.toFixedHeight(maxH * (double)this.rows);
        return this.arrange(container, g2, constraint3);
    }

    public void clear() {
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GridArrangement)) {
            return false;
        }
        GridArrangement that = (GridArrangement)obj;
        if (this.columns != that.columns) {
            return false;
        }
        if (this.rows != that.rows) {
            return false;
        }
        return true;
    }
}

