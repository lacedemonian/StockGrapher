/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.annotations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;

public class TextAnnotation
implements Serializable {
    private static final long serialVersionUID = 7008912287533127432L;
    public static final Font DEFAULT_FONT = new Font("SansSerif", 0, 10);
    public static final Paint DEFAULT_PAINT = Color.black;
    public static final TextAnchor DEFAULT_TEXT_ANCHOR = TextAnchor.CENTER;
    public static final TextAnchor DEFAULT_ROTATION_ANCHOR = TextAnchor.CENTER;
    public static final double DEFAULT_ROTATION_ANGLE = 0.0;
    private String text;
    private Font font;
    private transient Paint paint;
    private TextAnchor textAnchor;
    private TextAnchor rotationAnchor;
    private double rotationAngle;

    protected TextAnnotation(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Null 'text' argument.");
        }
        this.text = text;
        this.font = DEFAULT_FONT;
        this.paint = DEFAULT_PAINT;
        this.textAnchor = DEFAULT_TEXT_ANCHOR;
        this.rotationAnchor = DEFAULT_ROTATION_ANCHOR;
        this.rotationAngle = 0.0;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public TextAnchor getTextAnchor() {
        return this.textAnchor;
    }

    public void setTextAnchor(TextAnchor anchor) {
        this.textAnchor = anchor;
    }

    public TextAnchor getRotationAnchor() {
        return this.rotationAnchor;
    }

    public void setRotationAnchor(TextAnchor anchor) {
        this.rotationAnchor = anchor;
    }

    public double getRotationAngle() {
        return this.rotationAngle;
    }

    public void setRotationAngle(double angle) {
        this.rotationAngle = angle;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TextAnnotation)) {
            return false;
        }
        TextAnnotation that = (TextAnnotation)obj;
        if (!ObjectUtilities.equal(this.text, that.getText())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.font, that.getFont())) {
            return false;
        }
        if (!PaintUtilities.equal(this.paint, that.getPaint())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.textAnchor, that.getTextAnchor())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.rotationAnchor, that.getRotationAnchor())) {
            return false;
        }
        if (this.rotationAngle != that.getRotationAngle()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.text.hashCode();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.paint, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.paint = SerialUtilities.readPaint(stream);
    }
}

