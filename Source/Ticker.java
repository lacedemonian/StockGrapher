/*
 * Decompiled with CFR 0_115.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;

public class Ticker {
    private String[][] tickerArray = new String[252][];

    public Ticker(String fileName) {
        int lines = 251;
        try {
            String fileText;
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((fileText = br.readLine()) != null) {
                System.out.println(fileText);
                this.tickerArray[lines] = fileText.split(",");
                int x = 0;
                while (x < this.tickerArray[lines].length) {
                    System.out.println(this.tickerArray[lines][x]);
                    ++x;
                }
                --lines;
            }
            System.out.println("---------------------------------------------");
            int i = 0;
            while (i < 252) {
                System.out.println(Arrays.toString(this.tickerArray[i]));
                ++i;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[][] getTickerArray() {
        return this.tickerArray;
    }
}

