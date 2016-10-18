/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.io;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

public class SerialUtilities {
    static /* synthetic */ Class class$java$io$Serializable;
    static /* synthetic */ Class class$java$awt$GradientPaint;
    static /* synthetic */ Class class$java$awt$BasicStroke;
    static /* synthetic */ Class class$java$awt$geom$Line2D;
    static /* synthetic */ Class class$java$awt$geom$Rectangle2D;
    static /* synthetic */ Class class$java$awt$geom$Ellipse2D;
    static /* synthetic */ Class class$java$awt$geom$Arc2D;
    static /* synthetic */ Class class$java$awt$geom$GeneralPath;

    private SerialUtilities() {
    }

    public static boolean isSerializable(Class c) {
        Class class_ = class$java$io$Serializable == null ? (SerialUtilities.class$java$io$Serializable = SerialUtilities.class$("java.io.Serializable")) : class$java$io$Serializable;
        return class_.isAssignableFrom(c);
    }

    public static Paint readPaint(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        Paint result = null;
        boolean isNull = stream.readBoolean();
        if (!isNull) {
            Class c = (Class)stream.readObject();
            if (SerialUtilities.isSerializable(c)) {
                result = (Paint)stream.readObject();
            } else {
                Class class_ = class$java$awt$GradientPaint == null ? (SerialUtilities.class$java$awt$GradientPaint = SerialUtilities.class$("java.awt.GradientPaint")) : class$java$awt$GradientPaint;
                if (c.equals(class_)) {
                    float x1 = stream.readFloat();
                    float y1 = stream.readFloat();
                    Color c1 = (Color)stream.readObject();
                    float x2 = stream.readFloat();
                    float y2 = stream.readFloat();
                    Color c2 = (Color)stream.readObject();
                    boolean isCyclic = stream.readBoolean();
                    result = new GradientPaint(x1, y1, c1, x2, y2, c2, isCyclic);
                }
            }
        }
        return result;
    }

    public static void writePaint(Paint paint, ObjectOutputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        if (paint != null) {
            stream.writeBoolean(false);
            stream.writeObject(paint.getClass());
            if (paint instanceof Serializable) {
                stream.writeObject(paint);
            } else if (paint instanceof GradientPaint) {
                GradientPaint gp = (GradientPaint)paint;
                stream.writeFloat((float)gp.getPoint1().getX());
                stream.writeFloat((float)gp.getPoint1().getY());
                stream.writeObject(gp.getColor1());
                stream.writeFloat((float)gp.getPoint2().getX());
                stream.writeFloat((float)gp.getPoint2().getY());
                stream.writeObject(gp.getColor2());
                stream.writeBoolean(gp.isCyclic());
            }
        } else {
            stream.writeBoolean(true);
        }
    }

    public static Stroke readStroke(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        Stroke result = null;
        boolean isNull = stream.readBoolean();
        if (!isNull) {
            Class c = (Class)stream.readObject();
            Class class_ = class$java$awt$BasicStroke == null ? (SerialUtilities.class$java$awt$BasicStroke = SerialUtilities.class$("java.awt.BasicStroke")) : class$java$awt$BasicStroke;
            if (c.equals(class_)) {
                float width = stream.readFloat();
                int cap = stream.readInt();
                int join = stream.readInt();
                float miterLimit = stream.readFloat();
                float[] dash = (float[])stream.readObject();
                float dashPhase = stream.readFloat();
                result = new BasicStroke(width, cap, join, miterLimit, dash, dashPhase);
            } else {
                result = (Stroke)stream.readObject();
            }
        }
        return result;
    }

    public static void writeStroke(Stroke stroke, ObjectOutputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        if (stroke != null) {
            stream.writeBoolean(false);
            if (stroke instanceof BasicStroke) {
                BasicStroke s = (BasicStroke)stroke;
                Class class_ = class$java$awt$BasicStroke == null ? (SerialUtilities.class$java$awt$BasicStroke = SerialUtilities.class$("java.awt.BasicStroke")) : class$java$awt$BasicStroke;
                stream.writeObject(class_);
                stream.writeFloat(s.getLineWidth());
                stream.writeInt(s.getEndCap());
                stream.writeInt(s.getLineJoin());
                stream.writeFloat(s.getMiterLimit());
                stream.writeObject(s.getDashArray());
                stream.writeFloat(s.getDashPhase());
            } else {
                stream.writeObject(stroke.getClass());
                stream.writeObject(stroke);
            }
        } else {
            stream.writeBoolean(true);
        }
    }

    public static Shape readShape(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        Shape result = null;
        boolean isNull = stream.readBoolean();
        if (!isNull) {
            Class c = (Class)stream.readObject();
            Class class_ = class$java$awt$geom$Line2D == null ? (SerialUtilities.class$java$awt$geom$Line2D = SerialUtilities.class$("java.awt.geom.Line2D")) : class$java$awt$geom$Line2D;
            if (c.equals(class_)) {
                double x1 = stream.readDouble();
                double y1 = stream.readDouble();
                double x2 = stream.readDouble();
                double y2 = stream.readDouble();
                result = new Line2D.Double(x1, y1, x2, y2);
            } else {
                Class class_2 = class$java$awt$geom$Rectangle2D == null ? (SerialUtilities.class$java$awt$geom$Rectangle2D = SerialUtilities.class$("java.awt.geom.Rectangle2D")) : class$java$awt$geom$Rectangle2D;
                if (c.equals(class_2)) {
                    double x = stream.readDouble();
                    double y = stream.readDouble();
                    double w = stream.readDouble();
                    double h = stream.readDouble();
                    result = new Rectangle2D.Double(x, y, w, h);
                } else {
                    Class class_3 = class$java$awt$geom$Ellipse2D == null ? (SerialUtilities.class$java$awt$geom$Ellipse2D = SerialUtilities.class$("java.awt.geom.Ellipse2D")) : class$java$awt$geom$Ellipse2D;
                    if (c.equals(class_3)) {
                        double x = stream.readDouble();
                        double y = stream.readDouble();
                        double w = stream.readDouble();
                        double h = stream.readDouble();
                        result = new Ellipse2D.Double(x, y, w, h);
                    } else {
                        Class class_4 = class$java$awt$geom$Arc2D == null ? (SerialUtilities.class$java$awt$geom$Arc2D = SerialUtilities.class$("java.awt.geom.Arc2D")) : class$java$awt$geom$Arc2D;
                        if (c.equals(class_4)) {
                            double x = stream.readDouble();
                            double y = stream.readDouble();
                            double w = stream.readDouble();
                            double h = stream.readDouble();
                            double as = stream.readDouble();
                            double ae = stream.readDouble();
                            int at = stream.readInt();
                            result = new Arc2D.Double(x, y, w, h, as, ae, at);
                        } else {
                            Class class_5 = class$java$awt$geom$GeneralPath == null ? (SerialUtilities.class$java$awt$geom$GeneralPath = SerialUtilities.class$("java.awt.geom.GeneralPath")) : class$java$awt$geom$GeneralPath;
                            if (c.equals(class_5)) {
                                GeneralPath gp = new GeneralPath();
                                float[] args = new float[6];
                                boolean hasNext = stream.readBoolean();
                                while (!hasNext) {
                                    int type = stream.readInt();
                                    for (int i = 0; i < 6; ++i) {
                                        args[i] = stream.readFloat();
                                    }
                                    switch (type) {
                                        case 0: {
                                            gp.moveTo(args[0], args[1]);
                                            break;
                                        }
                                        case 1: {
                                            gp.lineTo(args[0], args[1]);
                                            break;
                                        }
                                        case 3: {
                                            gp.curveTo(args[0], args[1], args[2], args[3], args[4], args[5]);
                                            break;
                                        }
                                        case 2: {
                                            gp.quadTo(args[0], args[1], args[2], args[3]);
                                            break;
                                        }
                                        case 4: {
                                            break;
                                        }
                                        default: {
                                            throw new RuntimeException("JFreeChart - No path exists");
                                        }
                                    }
                                    gp.setWindingRule(stream.readInt());
                                    hasNext = stream.readBoolean();
                                }
                                result = gp;
                            } else {
                                result = (Shape)stream.readObject();
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void writeShape(Shape shape, ObjectOutputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        if (shape != null) {
            stream.writeBoolean(false);
            if (shape instanceof Line2D) {
                Line2D line = (Line2D)shape;
                Class class_ = class$java$awt$geom$Line2D == null ? (SerialUtilities.class$java$awt$geom$Line2D = SerialUtilities.class$("java.awt.geom.Line2D")) : class$java$awt$geom$Line2D;
                stream.writeObject(class_);
                stream.writeDouble(line.getX1());
                stream.writeDouble(line.getY1());
                stream.writeDouble(line.getX2());
                stream.writeDouble(line.getY2());
            } else if (shape instanceof Rectangle2D) {
                Rectangle2D rectangle = (Rectangle2D)shape;
                Class class_ = class$java$awt$geom$Rectangle2D == null ? (SerialUtilities.class$java$awt$geom$Rectangle2D = SerialUtilities.class$("java.awt.geom.Rectangle2D")) : class$java$awt$geom$Rectangle2D;
                stream.writeObject(class_);
                stream.writeDouble(rectangle.getX());
                stream.writeDouble(rectangle.getY());
                stream.writeDouble(rectangle.getWidth());
                stream.writeDouble(rectangle.getHeight());
            } else if (shape instanceof Ellipse2D) {
                Ellipse2D ellipse = (Ellipse2D)shape;
                Class class_ = class$java$awt$geom$Ellipse2D == null ? (SerialUtilities.class$java$awt$geom$Ellipse2D = SerialUtilities.class$("java.awt.geom.Ellipse2D")) : class$java$awt$geom$Ellipse2D;
                stream.writeObject(class_);
                stream.writeDouble(ellipse.getX());
                stream.writeDouble(ellipse.getY());
                stream.writeDouble(ellipse.getWidth());
                stream.writeDouble(ellipse.getHeight());
            } else if (shape instanceof Arc2D) {
                Arc2D arc = (Arc2D)shape;
                Class class_ = class$java$awt$geom$Arc2D == null ? (SerialUtilities.class$java$awt$geom$Arc2D = SerialUtilities.class$("java.awt.geom.Arc2D")) : class$java$awt$geom$Arc2D;
                stream.writeObject(class_);
                stream.writeDouble(arc.getX());
                stream.writeDouble(arc.getY());
                stream.writeDouble(arc.getWidth());
                stream.writeDouble(arc.getHeight());
                stream.writeDouble(arc.getAngleStart());
                stream.writeDouble(arc.getAngleExtent());
                stream.writeInt(arc.getArcType());
            } else if (shape instanceof GeneralPath) {
                Class class_ = class$java$awt$geom$GeneralPath == null ? (SerialUtilities.class$java$awt$geom$GeneralPath = SerialUtilities.class$("java.awt.geom.GeneralPath")) : class$java$awt$geom$GeneralPath;
                stream.writeObject(class_);
                PathIterator pi = shape.getPathIterator(null);
                float[] args = new float[6];
                stream.writeBoolean(pi.isDone());
                while (!pi.isDone()) {
                    int type = pi.currentSegment(args);
                    stream.writeInt(type);
                    for (int i = 0; i < 6; ++i) {
                        stream.writeFloat(args[i]);
                    }
                    stream.writeInt(pi.getWindingRule());
                    pi.next();
                    stream.writeBoolean(pi.isDone());
                }
            } else {
                stream.writeObject(shape.getClass());
                stream.writeObject(shape);
            }
        } else {
            stream.writeBoolean(true);
        }
    }

    public static Point2D readPoint2D(ObjectInputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        Point2D.Double result = null;
        boolean isNull = stream.readBoolean();
        if (!isNull) {
            double x = stream.readDouble();
            double y = stream.readDouble();
            result = new Point2D.Double(x, y);
        }
        return result;
    }

    public static void writePoint2D(Point2D p, ObjectOutputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        if (p != null) {
            stream.writeBoolean(false);
            stream.writeDouble(p.getX());
            stream.writeDouble(p.getY());
        } else {
            stream.writeBoolean(true);
        }
    }

    public static AttributedString readAttributedString(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        AttributedString result = null;
        boolean isNull = stream.readBoolean();
        if (!isNull) {
            String plainStr = (String)stream.readObject();
            result = new AttributedString(plainStr);
            char c = stream.readChar();
            int start = 0;
            while (c != '\uffff') {
                int limit = stream.readInt();
                Map atts = (Map)stream.readObject();
                result.addAttributes(atts, start, limit);
                start = limit;
                c = stream.readChar();
            }
        }
        return result;
    }

    public static void writeAttributedString(AttributedString as, ObjectOutputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Null 'stream' argument.");
        }
        if (as != null) {
            stream.writeBoolean(false);
            AttributedCharacterIterator aci = as.getIterator();
            StringBuffer plainStr = new StringBuffer();
            char current = aci.first();
            while (current != '\uffff') {
                plainStr = plainStr.append(current);
                current = aci.next();
            }
            stream.writeObject(plainStr.toString());
            current = aci.first();
            int begin = aci.getBeginIndex();
            while (current != '\uffff') {
                stream.writeChar(current);
                int limit = aci.getRunLimit();
                stream.writeInt(limit - begin);
                HashMap<AttributedCharacterIterator.Attribute, Object> atts = new HashMap<AttributedCharacterIterator.Attribute, Object>(aci.getAttributes());
                stream.writeObject(atts);
                current = aci.setIndex(limit);
            }
            stream.writeChar(65535);
        } else {
            stream.writeBoolean(true);
        }
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}

