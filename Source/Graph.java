/*
 * Decompiled with CFR 0_115.
 */
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

public class Graph
extends JFrame
implements ActionListener {
    double yLow = 0.0;
    Ticker ticker;
    String tickerFile;
    static String input;

    public Graph(String applicationTitle, String chartTitle, String xAxis, String tickerFileIn, int column) {
        super(applicationTitle);
        this.tickerFile = tickerFileIn;
        this.ticker = new Ticker(tickerFileIn);
        JFreeChart lineChart = ChartFactory.createLineChart(String.valueOf(chartTitle) + " " + xAxis, "Date", xAxis, this.createDataset(column), PlotOrientation.VERTICAL, true, true, false);
        lineChart.getCategoryPlot().getRangeAxis().setLowerBound(Math.floor(this.yLow / 10.0) * 10.0);
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(560, 367));
        this.setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset(int column) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] dates = new String[251];
        double[] stock = new double[251];
        double[] rollAvg = new double[251];
        String series = "unknown";
        Double low = Double.parseDouble(this.ticker.getTickerArray()[0][column]);
        int i = 0;
        while (i < 251) {
            dates[i] = this.ticker.getTickerArray()[i][0];
            stock[i] = Double.parseDouble(this.ticker.getTickerArray()[i][column]);
            if (stock[i] < low) {
                low = stock[i];
            }
            rollAvg[i] = i > 0 ? ((double)(i - 1) * rollAvg[i - 1] + stock[i]) / (double)i : stock[i];
            System.out.println(rollAvg[i]);
            ++i;
        }
        if (column == 4) {
            series = "Closing Price";
        }
        if (column == 5) {
            series = "Sale Volume";
        }
        this.yLow = low;
        int j = 0;
        while (j < 251) {
            dataset.addValue(stock[j], (Comparable)((Object)series), (Comparable)((Object)dates[j]));
            dataset.addValue(rollAvg[j], (Comparable)((Object)"Rolling Average"), (Comparable)((Object)dates[j]));
            ++j;
        }
        return dataset;
    }

    private DefaultCategoryDataset createVolumeSet() {
        DefaultCategoryDataset volumeset = new DefaultCategoryDataset();
        return volumeset;
    }

    public static void main(String[] args) {
        JFrame inputFrame = new JFrame();
        Container inputCont = inputFrame.getContentPane();
        JLabel inputText = new JLabel();
        inputText.setText("Company Code: ");
        JTextField inputField = new JTextField(4);
        inputCont.setLayout(new FlowLayout());
        inputCont.add(inputText);
        inputCont.add(inputField);
        JButton submit = new JButton("Get Graphs");
        inputFrame.getRootPane().setDefaultButton(submit);
        inputFrame.setDefaultCloseOperation(3);
        submit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Graph.input = JTextField.this.getText();
                Graph chart = new Graph("NASDAQ:" + Graph.input.toUpperCase() + " 1 YR", "NASDAQ:" + Graph.input.toUpperCase(), "Stock Price", "src/" + Graph.input.toLowerCase() + ".csv", 4);
                chart.pack();
                RefineryUtilities.positionFrameOnScreen(chart, 0.0, 0.0);
                chart.setVisible(true);
                System.out.println("Price Chart Created");
                Graph volChart = new Graph("NASDAQ:" + Graph.input.toUpperCase() + " 1 YR", "NASDAQ:" + Graph.input.toUpperCase(), "Trading Volume", "src/" + Graph.input.toLowerCase() + ".csv", 5);
                volChart.pack();
                RefineryUtilities.positionFrameOnScreen(volChart, 0.0, 0.5);
                volChart.setVisible(true);
                System.out.println("Volume Chart Created");
            }
        });
        inputCont.add(submit);
        inputFrame.pack();
        RefineryUtilities.centerFrameOnScreen(inputFrame);
        inputFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
    }

}

