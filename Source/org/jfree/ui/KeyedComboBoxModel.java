/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class KeyedComboBoxModel
implements ComboBoxModel {
    private int selectedItem;
    private ArrayList data = new ArrayList();
    private ArrayList listdatalistener = new ArrayList();
    private transient ListDataListener[] tempListeners;

    public KeyedComboBoxModel() {
    }

    public KeyedComboBoxModel(Object[] keys, Object[] values) {
        this();
        this.setData(keys, values);
    }

    public void setData(Object[] keys, Object[] values) {
        if (values.length != keys.length) {
            throw new IllegalArgumentException("Values and text must have the same length.");
        }
        this.data.clear();
        this.data.ensureCapacity(keys.length);
        for (int i = 0; i < values.length; ++i) {
            this.add(keys[i], values[i]);
        }
        this.selectedItem = -1;
        ListDataEvent evt = new ListDataEvent(this, 0, 0, this.data.size() - 1);
        this.fireListDataEvent(evt);
    }

    protected synchronized void fireListDataEvent(ListDataEvent evt) {
        if (this.tempListeners == null) {
            this.tempListeners = this.listdatalistener.toArray(new ListDataListener[this.listdatalistener.size()]);
        }
        for (int i = 0; i < this.tempListeners.length; ++i) {
            ListDataListener l = this.tempListeners[i];
            l.contentsChanged(evt);
        }
    }

    public Object getSelectedItem() {
        if (this.selectedItem >= this.data.size()) {
            return null;
        }
        if (this.selectedItem < 0) {
            return null;
        }
        ComboBoxItemPair item = (ComboBoxItemPair)this.data.get(this.selectedItem);
        return item.getValue();
    }

    public void setSelectedKey(Object anItem) {
        int newSelectedItem;
        this.selectedItem = anItem == null ? -1 : (newSelectedItem = this.findDataElementIndex(anItem));
        this.fireListDataEvent(new ListDataEvent(this, 0, -1, -1));
    }

    public void setSelectedItem(Object anItem) {
        int newSelectedItem;
        this.selectedItem = anItem == null ? -1 : (newSelectedItem = this.findElementIndex(anItem));
        this.fireListDataEvent(new ListDataEvent(this, 0, -1, -1));
    }

    public synchronized void addListDataListener(ListDataListener l) {
        this.listdatalistener.add(l);
        this.tempListeners = null;
    }

    public Object getElementAt(int index) {
        if (index >= this.data.size()) {
            return null;
        }
        ComboBoxItemPair datacon = (ComboBoxItemPair)this.data.get(index);
        if (datacon == null) {
            return null;
        }
        return datacon.getValue();
    }

    public Object getKeyAt(int index) {
        if (index >= this.data.size()) {
            return null;
        }
        if (index < 0) {
            return null;
        }
        ComboBoxItemPair datacon = (ComboBoxItemPair)this.data.get(index);
        if (datacon == null) {
            return null;
        }
        return datacon.getKey();
    }

    public Object getSelectedKey() {
        return this.getKeyAt(this.selectedItem);
    }

    public int getSize() {
        return this.data.size();
    }

    public void removeListDataListener(ListDataListener l) {
        this.listdatalistener.remove(l);
        this.tempListeners = null;
    }

    private int findDataElementIndex(Object anItem) {
        if (anItem == null) {
            throw new NullPointerException("Item to find must not be null");
        }
        for (int i = 0; i < this.data.size(); ++i) {
            ComboBoxItemPair datacon = (ComboBoxItemPair)this.data.get(i);
            if (!anItem.equals(datacon.getKey())) continue;
            return i;
        }
        return -1;
    }

    public int findElementIndex(Object key) {
        if (key == null) {
            throw new NullPointerException("Item to find must not be null");
        }
        for (int i = 0; i < this.data.size(); ++i) {
            ComboBoxItemPair datacon = (ComboBoxItemPair)this.data.get(i);
            if (!key.equals(datacon.getValue())) continue;
            return i;
        }
        return -1;
    }

    public void removeDataElement(Object key) {
        int idx = this.findDataElementIndex(key);
        if (idx == -1) {
            return;
        }
        this.data.remove(idx);
        ListDataEvent evt = new ListDataEvent(this, 2, idx, idx);
        this.fireListDataEvent(evt);
    }

    public void add(Object key, Object cbitem) {
        ComboBoxItemPair con = new ComboBoxItemPair(key, cbitem);
        this.data.add(con);
        ListDataEvent evt = new ListDataEvent(this, 1, this.data.size() - 2, this.data.size() - 2);
        this.fireListDataEvent(evt);
    }

    public void clear() {
        int size = this.getSize();
        this.data.clear();
        ListDataEvent evt = new ListDataEvent(this, 2, 0, size - 1);
        this.fireListDataEvent(evt);
    }

    private static class ComboBoxItemPair {
        private Object key;
        private Object value;

        public ComboBoxItemPair(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

}

