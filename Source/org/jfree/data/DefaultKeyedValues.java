/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.data.DefaultKeyedValue;
import org.jfree.data.KeyedValue;
import org.jfree.data.KeyedValueComparator;
import org.jfree.data.KeyedValueComparatorType;
import org.jfree.data.KeyedValues;
import org.jfree.data.UnknownKeyException;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.SortOrder;

public class DefaultKeyedValues
implements KeyedValues,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 8468154364608194797L;
    private List data = new ArrayList();

    public int getItemCount() {
        return this.data.size();
    }

    public Number getValue(int item) {
        Number result = null;
        KeyedValue kval = (KeyedValue)this.data.get(item);
        if (kval != null) {
            result = kval.getValue();
        }
        return result;
    }

    public Comparable getKey(int index) {
        Comparable result = null;
        KeyedValue item = (KeyedValue)this.data.get(index);
        if (item != null) {
            result = item.getKey();
        }
        return result;
    }

    public int getIndex(Comparable key) {
        int i = 0;
        Iterator iterator = this.data.iterator();
        while (iterator.hasNext()) {
            KeyedValue kv = (KeyedValue)iterator.next();
            if (kv.getKey().equals(key)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public List getKeys() {
        ArrayList<Comparable> result = new ArrayList<Comparable>();
        Iterator iterator = this.data.iterator();
        while (iterator.hasNext()) {
            KeyedValue kv = (KeyedValue)iterator.next();
            result.add(kv.getKey());
        }
        return result;
    }

    public Number getValue(Comparable key) {
        int index = this.getIndex(key);
        if (index < 0) {
            throw new UnknownKeyException("Key not found: " + key);
        }
        return this.getValue(index);
    }

    public void addValue(Comparable key, double value) {
        this.addValue(key, new Double(value));
    }

    public void addValue(Comparable key, Number value) {
        this.setValue(key, value);
    }

    public void setValue(Comparable key, double value) {
        this.setValue(key, new Double(value));
    }

    public void setValue(Comparable key, Number value) {
        if (key == null) {
            throw new IllegalArgumentException("Null 'key' argument.");
        }
        int keyIndex = this.getIndex(key);
        if (keyIndex >= 0) {
            DefaultKeyedValue kv = (DefaultKeyedValue)this.data.get(keyIndex);
            kv.setValue(value);
        } else {
            DefaultKeyedValue kv = new DefaultKeyedValue(key, value);
            this.data.add(kv);
        }
    }

    public void removeValue(int index) {
        this.data.remove(index);
    }

    public void removeValue(Comparable key) {
        int index = this.getIndex(key);
        if (index >= 0) {
            this.removeValue(index);
        }
    }

    public void sortByKeys(SortOrder order) {
        KeyedValueComparator comparator = new KeyedValueComparator(KeyedValueComparatorType.BY_KEY, order);
        Collections.sort(this.data, comparator);
    }

    public void sortByValues(SortOrder order) {
        KeyedValueComparator comparator = new KeyedValueComparator(KeyedValueComparatorType.BY_VALUE, order);
        Collections.sort(this.data, comparator);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof KeyedValues)) {
            return false;
        }
        KeyedValues that = (KeyedValues)obj;
        int count = this.getItemCount();
        if (count != that.getItemCount()) {
            return false;
        }
        for (int i = 0; i < count; ++i) {
            Comparable k2;
            Comparable k1 = this.getKey(i);
            if (!k1.equals(k2 = that.getKey(i))) {
                return false;
            }
            Number v1 = this.getValue(i);
            Number v2 = that.getValue(i);
            if (!(v1 == null ? v2 != null : !v1.equals(v2))) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.data != null ? this.data.hashCode() : 0;
    }

    public Object clone() throws CloneNotSupportedException {
        DefaultKeyedValues clone = (DefaultKeyedValues)super.clone();
        clone.data = (List)ObjectUtilities.deepClone(this.data);
        return clone;
    }
}

