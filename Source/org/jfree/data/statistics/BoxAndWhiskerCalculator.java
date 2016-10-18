/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.Statistics;

public abstract class BoxAndWhiskerCalculator {
    public static BoxAndWhiskerItem calculateBoxAndWhiskerStatistics(List values) {
        Collections.sort(values);
        double mean = Statistics.calculateMean(values);
        double median = Statistics.calculateMedian(values, false);
        double q1 = BoxAndWhiskerCalculator.calculateQ1(values);
        double q3 = BoxAndWhiskerCalculator.calculateQ3(values);
        double interQuartileRange = q3 - q1;
        double upperOutlierThreshold = q3 + interQuartileRange * 1.5;
        double lowerOutlierThreshold = q1 - interQuartileRange * 1.5;
        double upperFaroutThreshold = q3 + interQuartileRange * 2.0;
        double lowerFaroutThreshold = q1 - interQuartileRange * 2.0;
        double minRegularValue = Double.POSITIVE_INFINITY;
        double maxRegularValue = Double.NEGATIVE_INFINITY;
        double minOutlier = Double.POSITIVE_INFINITY;
        double maxOutlier = Double.NEGATIVE_INFINITY;
        ArrayList<Number> outliers = new ArrayList<Number>();
        Iterator iterator = values.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object == null || !(object instanceof Number)) continue;
            Number number = (Number)object;
            double value = number.doubleValue();
            if (value > upperOutlierThreshold) {
                outliers.add(number);
                if (value <= maxOutlier || value > upperFaroutThreshold) continue;
                maxOutlier = value;
                continue;
            }
            if (value < lowerOutlierThreshold) {
                outliers.add(number);
                if (value >= minOutlier || value < lowerFaroutThreshold) continue;
                minOutlier = value;
                continue;
            }
            minRegularValue = minRegularValue == Double.NaN ? value : Math.min(minRegularValue, value);
            if (maxRegularValue == Double.NaN) {
                maxRegularValue = value;
                continue;
            }
            maxRegularValue = Math.max(maxRegularValue, value);
        }
        minOutlier = Math.min(minOutlier, minRegularValue);
        maxOutlier = Math.max(maxOutlier, maxRegularValue);
        return new BoxAndWhiskerItem(new Double(mean), new Double(median), new Double(q1), new Double(q3), new Double(minRegularValue), new Double(maxRegularValue), new Double(minOutlier), new Double(maxOutlier), outliers);
    }

    public static double calculateQ1(List values) {
        double result = Double.NaN;
        int count = values.size();
        if (count > 0) {
            result = count % 2 == 1 ? (count > 1 ? Statistics.calculateMedian(values, 0, count / 2) : Statistics.calculateMedian(values, 0, 0)) : Statistics.calculateMedian(values, 0, count / 2 - 1);
        }
        return result;
    }

    public static double calculateQ3(List values) {
        double result = Double.NaN;
        int count = values.size();
        if (count > 0) {
            result = count % 2 == 1 ? (count > 1 ? Statistics.calculateMedian(values, count / 2, count - 1) : Statistics.calculateMedian(values, 0, 0)) : Statistics.calculateMedian(values, count / 2, count - 1);
        }
        return result;
    }
}

