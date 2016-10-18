/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.urls;

import java.io.Serializable;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.general.PieDataset;

public class StandardPieURLGenerator
implements PieURLGenerator,
Serializable {
    private static final long serialVersionUID = 1626966402065883419L;
    private String prefix = "index.html";
    private String categoryParameterName = "category";
    private String indexParameterName = "pieIndex";

    public StandardPieURLGenerator() {
    }

    public StandardPieURLGenerator(String prefix) {
        this.prefix = prefix;
    }

    public StandardPieURLGenerator(String prefix, String categoryParameterName) {
        this.prefix = prefix;
        this.categoryParameterName = categoryParameterName;
    }

    public StandardPieURLGenerator(String prefix, String categoryParameterName, String indexParameterName) {
        this.prefix = prefix;
        this.categoryParameterName = categoryParameterName;
        this.indexParameterName = indexParameterName;
    }

    public String generateURL(PieDataset data, Comparable key, int pieIndex) {
        String url = this.prefix;
        url = url.indexOf("?") > -1 ? url + "&amp;" + this.categoryParameterName + "=" + key.toString() : url + "?" + this.categoryParameterName + "=" + key.toString();
        if (this.indexParameterName != null) {
            url = url + "&amp;" + this.indexParameterName + "=" + String.valueOf(pieIndex);
        }
        return url;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardPieURLGenerator)) {
            return false;
        }
        StandardPieURLGenerator generator = (StandardPieURLGenerator)obj;
        return this.categoryParameterName.equals(generator.categoryParameterName) && this.prefix.equals(generator.prefix);
    }
}

