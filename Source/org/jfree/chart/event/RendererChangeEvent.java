/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.event;

import org.jfree.chart.event.ChartChangeEvent;

public class RendererChangeEvent
extends ChartChangeEvent {
    private Object renderer;

    public RendererChangeEvent(Object renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    public Object getRenderer() {
        return this.renderer;
    }
}

