/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.encoders;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.jfree.chart.encoders.ImageEncoder;

public class SunJPEGEncoderAdapter
implements ImageEncoder {
    private float quality = 0.75f;

    public float getQuality() {
        return this.quality;
    }

    public void setQuality(float quality) {
        this.quality = quality;
    }

    public boolean isEncodingAlpha() {
        return false;
    }

    public void setEncodingAlpha(boolean encodingAlpha) {
    }

    public byte[] encode(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.encode(bufferedImage, outputStream);
        return outputStream.toByteArray();
    }

    public void encode(BufferedImage bufferedImage, OutputStream outputStream) throws IOException {
        if (bufferedImage == null) {
            throw new IllegalArgumentException("Null 'image' argument.");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("Null 'outputStream' argument.");
        }
        ImageIO.write((RenderedImage)bufferedImage, "jpeg", outputStream);
    }
}

