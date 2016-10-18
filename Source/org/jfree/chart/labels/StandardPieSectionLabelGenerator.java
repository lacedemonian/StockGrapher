/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import java.text.AttributedString;
import java.text.NumberFormat;
import org.jfree.chart.labels.AbstractPieItemLabelGenerator;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;
import org.jfree.util.ObjectList;

public class StandardPieSectionLabelGenerator
extends AbstractPieItemLabelGenerator
implements PieSectionLabelGenerator,
Cloneable,
Serializable {
    private static final long serialVersionUID = 3064190563760203668L;
    public static final String DEFAULT_SECTION_LABEL_FORMAT = "{0} = {1}";
    private ObjectList attributedLabels = new ObjectList();

    public StandardPieSectionLabelGenerator() {
        this("{0} = {1}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance());
    }

    public StandardPieSectionLabelGenerator(String labelFormat) {
        this(labelFormat, NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance());
    }

    public StandardPieSectionLabelGenerator(String labelFormat, NumberFormat numberFormat, NumberFormat percentFormat) {
        super(labelFormat, numberFormat, percentFormat);
    }

    public AttributedString getAttributedLabel(int section) {
        return (AttributedString)this.attributedLabels.get(section);
    }

    public void setAttributedLabel(int section, AttributedString label) {
        this.attributedLabels.set(section, label);
    }

    public String generateSectionLabel(PieDataset dataset, Comparable key) {
        return super.generateSectionLabel(dataset, key);
    }

    public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
        return this.getAttributedLabel(dataset.getIndex(key));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardPieSectionLabelGenerator)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

