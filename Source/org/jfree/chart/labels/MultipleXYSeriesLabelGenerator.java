/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.labels.XYSeriesLabelGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.PublicCloneable;

public class MultipleXYSeriesLabelGenerator
implements XYSeriesLabelGenerator,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 138976236941898560L;
    public static final String DEFAULT_LABEL_FORMAT = "{0}";
    private String formatPattern;
    private String additionalFormatPattern;
    private Map seriesLabelLists;

    public MultipleXYSeriesLabelGenerator() {
        this("{0}");
    }

    public MultipleXYSeriesLabelGenerator(String format) {
        if (format == null) {
            throw new IllegalArgumentException("Null 'format' argument.");
        }
        this.formatPattern = format;
        this.additionalFormatPattern = "\n{0}";
        this.seriesLabelLists = new HashMap();
    }

    public void addSeriesLabel(int series, String label) {
        Integer key = new Integer(series);
        ArrayList<String> labelList = (ArrayList<String>)this.seriesLabelLists.get(key);
        if (labelList == null) {
            labelList = new ArrayList<String>();
            this.seriesLabelLists.put(key, labelList);
        }
        labelList.add(label);
    }

    public void clearSeriesLabels(int series) {
        Integer key = new Integer(series);
        this.seriesLabelLists.put(key, null);
    }

    public String generateLabel(XYDataset dataset, int series) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        StringBuffer label = new StringBuffer();
        label.append(MessageFormat.format(this.formatPattern, this.createItemArray(dataset, series)));
        Integer key = new Integer(series);
        List extraLabels = (List)this.seriesLabelLists.get(key);
        if (extraLabels != null) {
            Object[] temp = new Object[1];
            for (int i = 0; i < extraLabels.size(); ++i) {
                temp[0] = extraLabels.get(i);
                String labelAddition = MessageFormat.format(this.additionalFormatPattern, temp);
                label.append(labelAddition);
            }
        }
        return label.toString();
    }

    protected Object[] createItemArray(XYDataset dataset, int series) {
        Object[] result = new Object[]{dataset.getSeriesKey(series).toString()};
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardXYSeriesLabelGenerator)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }
}

