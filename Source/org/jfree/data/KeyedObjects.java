/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jfree.data.KeyedObject;
import org.jfree.util.PublicCloneable;

public class KeyedObjects
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 1321582394193530984L;
    private List data = new ArrayList();

    public int getItemCount() {
        return this.data.size();
    }

    public Object getObject(int item) {
        KeyedObject kobj;
        Object result = null;
        if (item >= 0 && item < this.data.size() && (kobj = (KeyedObject)this.data.get(item)) != null) {
            result = kobj.getObject();
        }
        return result;
    }

    public Comparable getKey(int index) {
        KeyedObject item;
        Comparable result = null;
        if (index >= 0 && index < this.data.size() && (item = (KeyedObject)this.data.get(index)) != null) {
            result = item.getKey();
        }
        return result;
    }

    public int getIndex(Comparable key) {
        int result = -1;
        int i = 0;
        Iterator iterator = this.data.iterator();
        while (iterator.hasNext()) {
            KeyedObject ko = (KeyedObject)iterator.next();
            if (ko.getKey().equals(key)) {
                result = i;
            }
            ++i;
        }
        return result;
    }

    public List getKeys() {
        ArrayList<Comparable> result = new ArrayList<Comparable>();
        Iterator iterator = this.data.iterator();
        while (iterator.hasNext()) {
            KeyedObject ko = (KeyedObject)iterator.next();
            result.add(ko.getKey());
        }
        return result;
    }

    public Object getObject(Comparable key) {
        return this.getObject(this.getIndex(key));
    }

    public void addObject(Comparable key, Object object) {
        this.setObject(key, object);
    }

    public void setObject(Comparable key, Object object) {
        int keyIndex = this.getIndex(key);
        if (keyIndex >= 0) {
            KeyedObject ko = (KeyedObject)this.data.get(keyIndex);
            ko.setObject(object);
        } else {
            KeyedObject ko = new KeyedObject(key, object);
            this.data.add(ko);
        }
    }

    public void removeValue(int index) {
        this.data.remove(index);
    }

    public void removeValue(Comparable key) {
        this.removeValue(this.getIndex(key));
    }

    public Object clone() throws CloneNotSupportedException {
        KeyedObjects clone = (KeyedObjects)super.clone();
        clone.data = new ArrayList();
        Iterator iterator = this.data.iterator();
        while (iterator.hasNext()) {
            KeyedObject ko = (KeyedObject)iterator.next();
            clone.data.add(ko.clone());
        }
        return clone;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof KeyedObjects)) {
            return false;
        }
        KeyedObjects kos = (KeyedObjects)o;
        int count = this.getItemCount();
        if (count != kos.getItemCount()) {
            return false;
        }
        for (int i = 0; i < count; ++i) {
            Comparable k2;
            Comparable k1 = this.getKey(i);
            if (!k1.equals(k2 = kos.getKey(i))) {
                return false;
            }
            Object o1 = this.getObject(i);
            Object o2 = kos.getObject(i);
            if (!(o1 == null ? o2 != null : !o1.equals(o2))) continue;
            return false;
        }
        return true;
    }
}

