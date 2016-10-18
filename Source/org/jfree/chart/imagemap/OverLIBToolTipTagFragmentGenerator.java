/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.imagemap;

import org.jfree.chart.imagemap.ToolTipTagFragmentGenerator;

public class OverLIBToolTipTagFragmentGenerator
implements ToolTipTagFragmentGenerator {
    public String generateToolTipFragment(String toolTipText) {
        return " onMouseOver=\"return overlib('" + toolTipText + "');\" onMouseOut=\"return nd();\"";
    }
}

