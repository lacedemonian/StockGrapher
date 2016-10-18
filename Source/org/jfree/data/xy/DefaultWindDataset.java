/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.xy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.WindDataItem;
import org.jfree.data.xy.WindDataset;

public class DefaultWindDataset
extends AbstractXYDataset
implements WindDataset {
    private List seriesKeys;
    private List allSeriesData;

    public DefaultWindDataset() {
        this.seriesKeys = new ArrayList();
        this.allSeriesData = new ArrayList();
    }

    public DefaultWindDataset(Object[][][] data) {
        this(DefaultWindDataset.seriesNameListFromDataArray((Object[][])data), data);
    }

    public DefaultWindDataset(String[] seriesNames, Object[][][] data) {
        this(Arrays.asList(seriesNames), data);
    }

    public DefaultWindDataset(List seriesKeys, Object[][][] data) {
        this.seriesKeys = seriesKeys;
        int seriesCount = data.length;
        this.allSeriesData = new ArrayList(seriesCount);
        for (int seriesIndex = 0; seriesIndex < seriesCount; ++seriesIndex) {
            ArrayList<WindDataItem> oneSeriesData = new ArrayList<WindDataItem>();
            int maxItemCount = data[seriesIndex].length;
            for (int itemIndex = 0; itemIndex < maxItemCount; ++itemIndex) {
                Number xNumber;
                Object xObject = data[seriesIndex][itemIndex][0];
                if (xObject == null) continue;
                if (xObject instanceof Number) {
                    xNumber = (Number)xObject;
                } else if (xObject instanceof Date) {
                    Date xDate = (Date)xObject;
                    xNumber = new Long(xDate.getTime());
                } else {
                    xNumber = new Integer(0);
                }
                Number windDir = (Number)data[seriesIndex][itemIndex][1];
                Number windForce = (Number)data[seriesIndex][itemIndex][2];
                oneSeriesData.add(new WindDataItem(xNumber, windDir, windForce));
            }
            Collections.sort(oneSeriesData);
            this.allSeriesData.add(seriesIndex, oneSeriesData);
        }
    }

    public int getSeriesCount() {
        return this.allSeriesData.size();
    }

    public int getItemCount(int series) {
        List oneSeriesData = (List)this.allSeriesData.get(series);
        return oneSeriesData.size();
    }

    public Comparable getSeriesKey(int series) {
        return this.seriesKeys.get(series).toString();
    }

    public Number getX(int series, int item) {
        List oneSeriesData = (List)this.allSeriesData.get(series);
        WindDataItem windItem = (WindDataItem)oneSeriesData.get(item);
        return windItem.getX();
    }

    public Number getY(int series, int item) {
        return this.getWindForce(series, item);
    }

    public Number getWindDirection(int series, int item) {
        List oneSeriesData = (List)this.allSeriesData.get(series);
        WindDataItem windItem = (WindDataItem)oneSeriesData.get(item);
        return windItem.getWindDirection();
    }

    public Number getWindForce(int series, int item) {
        List oneSeriesData = (List)this.allSeriesData.get(series);
        WindDataItem windItem = (WindDataItem)oneSeriesData.get(item);
        return windItem.getWindForce();
    }

    public static List seriesNameListFromDataArray(Object[][] data) {
        int seriesCount = data.length;
        ArrayList<String> seriesNameList = new ArrayList<String>(seriesCount);
        for (int i = 0; i < seriesCount; ++i) {
            seriesNameList.add("Series " + (i + 1));
        }
        return seriesNameList;
    }
}

