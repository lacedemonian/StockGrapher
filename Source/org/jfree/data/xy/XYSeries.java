/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.xy;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jfree.data.general.Series;
import org.jfree.data.general.SeriesException;
import org.jfree.data.xy.XYDataItem;
import org.jfree.util.ObjectUtilities;

public class XYSeries
extends Series
implements Cloneable,
Serializable {
    static final long serialVersionUID = -5908509288197150436L;
    protected List data = new ArrayList();
    private int maximumItemCount = Integer.MAX_VALUE;
    private boolean autoSort;
    private boolean allowDuplicateXValues;

    public XYSeries(Comparable key) {
        this(key, true, true);
    }

    public XYSeries(Comparable key, boolean autoSort) {
        this(key, autoSort, true);
    }

    public XYSeries(Comparable key, boolean autoSort, boolean allowDuplicateXValues) {
        super(key);
        this.autoSort = autoSort;
        this.allowDuplicateXValues = allowDuplicateXValues;
    }

    public boolean getAutoSort() {
        return this.autoSort;
    }

    public boolean getAllowDuplicateXValues() {
        return this.allowDuplicateXValues;
    }

    public int getItemCount() {
        return this.data.size();
    }

    public List getItems() {
        return Collections.unmodifiableList(this.data);
    }

    public int getMaximumItemCount() {
        return this.maximumItemCount;
    }

    public void setMaximumItemCount(int maximum) {
        this.maximumItemCount = maximum;
        boolean dataRemoved = false;
        while (this.data.size() > maximum) {
            this.data.remove(0);
            dataRemoved = true;
        }
        if (dataRemoved) {
            this.fireSeriesChanged();
        }
    }

    public void add(XYDataItem item) {
        this.add(item, true);
    }

    public void add(double x, double y) {
        this.add(new Double(x), (Number)new Double(y), true);
    }

    public void add(double x, double y, boolean notify) {
        this.add(new Double(x), (Number)new Double(y), notify);
    }

    public void add(double x, Number y) {
        this.add(new Double(x), y);
    }

    public void add(double x, Number y, boolean notify) {
        this.add(new Double(x), y, notify);
    }

    public void add(Number x, Number y) {
        this.add(x, y, true);
    }

    public void add(Number x, Number y, boolean notify) {
        if (x == null) {
            throw new IllegalArgumentException("Null 'x' argument.");
        }
        XYDataItem item = new XYDataItem(x, y);
        this.add(item, notify);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void add(XYDataItem item, boolean notify) {
        if (item == null) {
            throw new IllegalArgumentException("Null 'item' argument.");
        }
        if (this.autoSort) {
            int index = Collections.binarySearch(this.data, item);
            if (index < 0) {
                this.data.add(- index - 1, item);
            } else {
                if (!this.allowDuplicateXValues) throw new SeriesException("X-value already exists.");
                int size = this.data.size();
                while (index < size && item.compareTo(this.data.get(index)) == 0) {
                    ++index;
                }
                if (index < this.data.size()) {
                    this.data.add(index, item);
                } else {
                    this.data.add(item);
                }
            }
        } else {
            int index;
            if (!this.allowDuplicateXValues && (index = this.indexOf(item.getX())) >= 0) {
                throw new SeriesException("X-value already exists.");
            }
            this.data.add(item);
        }
        if (this.getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
        if (!notify) return;
        this.fireSeriesChanged();
    }

    public void delete(int start, int end) {
        for (int i = start; i <= end; ++i) {
            this.data.remove(start);
        }
        this.fireSeriesChanged();
    }

    public XYDataItem remove(int index) {
        XYDataItem result = (XYDataItem)this.data.remove(index);
        this.fireSeriesChanged();
        return result;
    }

    public XYDataItem remove(Number x) {
        return this.remove(this.indexOf(x));
    }

    public void clear() {
        if (this.data.size() > 0) {
            this.data.clear();
            this.fireSeriesChanged();
        }
    }

    public XYDataItem getDataItem(int index) {
        return (XYDataItem)this.data.get(index);
    }

    public Number getX(int index) {
        return this.getDataItem(index).getX();
    }

    public Number getY(int index) {
        return this.getDataItem(index).getY();
    }

    public void update(int index, Number y) {
        XYDataItem item = this.getDataItem(index);
        item.setY(y);
        this.fireSeriesChanged();
    }

    public void updateByIndex(int index, Number y) {
        this.update(index, y);
    }

    public void update(Number x, Number y) {
        int index = this.indexOf(x);
        if (index < 0) {
            throw new SeriesException("No observation for x = " + x);
        }
        XYDataItem item = this.getDataItem(index);
        item.setY(y);
        this.fireSeriesChanged();
    }

    public XYDataItem addOrUpdate(Number x, Number y) {
        if (x == null) {
            throw new IllegalArgumentException("Null 'x' argument.");
        }
        XYDataItem overwritten = null;
        int index = this.indexOf(x);
        if (index >= 0) {
            XYDataItem existing = (XYDataItem)this.data.get(index);
            try {
                overwritten = (XYDataItem)existing.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new SeriesException("Couldn't clone XYDataItem!");
            }
            existing.setY(y);
        } else {
            if (this.autoSort) {
                this.data.add(- index - 1, new XYDataItem(x, y));
            } else {
                this.data.add(new XYDataItem(x, y));
            }
            if (this.getItemCount() > this.maximumItemCount) {
                this.data.remove(0);
            }
        }
        this.fireSeriesChanged();
        return overwritten;
    }

    public int indexOf(Number x) {
        if (this.autoSort) {
            return Collections.binarySearch(this.data, new XYDataItem(x, null));
        }
        for (int i = 0; i < this.data.size(); ++i) {
            XYDataItem item = (XYDataItem)this.data.get(i);
            if (!item.getX().equals(x)) continue;
            return i;
        }
        return -1;
    }

    public Object clone() throws CloneNotSupportedException {
        XYSeries clone = this.createCopy(0, this.getItemCount() - 1);
        return clone;
    }

    public XYSeries createCopy(int start, int end) throws CloneNotSupportedException {
        XYSeries copy = (XYSeries)super.clone();
        copy.data = new ArrayList();
        if (this.data.size() > 0) {
            for (int index = start; index <= end; ++index) {
                XYDataItem item = (XYDataItem)this.data.get(index);
                XYDataItem clone = (XYDataItem)item.clone();
                try {
                    copy.add(clone);
                    continue;
                }
                catch (SeriesException e) {
                    System.err.println("Unable to add cloned data item.");
                }
            }
        }
        return copy;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYSeries)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        XYSeries that = (XYSeries)obj;
        if (this.maximumItemCount != that.maximumItemCount) {
            return false;
        }
        if (this.autoSort != that.autoSort) {
            return false;
        }
        if (this.allowDuplicateXValues != that.allowDuplicateXValues) {
            return false;
        }
        if (!ObjectUtilities.equal(this.data, that.data)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (this.data != null ? this.data.hashCode() : 0);
        result = 29 * result + this.maximumItemCount;
        result = 29 * result + (this.autoSort ? 1 : 0);
        result = 29 * result + (this.allowDuplicateXValues ? 1 : 0);
        return result;
    }
}

