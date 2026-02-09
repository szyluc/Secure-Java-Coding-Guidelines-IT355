package jd.rule1;

import java.io.FileReader;

public class Sample {
    public static void main (String[] args) {
        try {
            FileReader in = new FileReader("sample.txt");
            int inbuff;
            char data;
            while ((inbuff = in.read()) != -1) {
                data = (char) inbuff;
                System.out.print(data);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
