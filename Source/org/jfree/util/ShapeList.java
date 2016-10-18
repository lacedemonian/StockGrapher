/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.util;

import java.awt.Shape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.jfree.io.SerialUtilities;
import org.jfree.util.AbstractObjectList;

public class ShapeList
extends AbstractObjectList {
    public Shape getShape(int index) {
        return (Shape)this.get(index);
    }

    public void setShape(int index, Shape shape) {
        this.set(index, shape);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof ShapeList) {
            return super.equals(o);
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        int count = this.size();
        stream.writeInt(count);
        for (int i = 0; i < count; ++i) {
            Shape shape = this.getShape(i);
            if (shape != null) {
                stream.writeInt(i);
                SerialUtilities.writeShape(shape, stream);
                continue;
            }
            stream.writeInt(-1);
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int count = stream.readInt();
        for (int i = 0; i < count; ++i) {
            int index = stream.readInt();
            if (index == -1) continue;
            this.setShape(index, SerialUtilities.readShape(stream));
        }
    }
}

