package dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;

public class PerformaceTest {

    public static void main(String[] args) throws IOException {

        testSortedArrayDictionary();
        testHashDictionary();
        //testBinaryTreeDictionary();
    }

    private static void testSortedArrayDictionary() throws IOException {
        Dictionary<String, String> dict = new SortedArrayDictionary<>();
        testDict(dict);
    }

    private static void testHashDictionary() throws IOException {
        Dictionary<String, String> dict = new HashDictionary<>(); // new HashDictionary<>(3); ?
        testDict(dict);
    }

    private static void testDict(Dictionary<String, String> dict) throws IOException {
        //----------------------------------------------------------------------------------------Test insert time
        long start = System.nanoTime(); // aktuelle Zeit in nsec
        LineNumberReader in;
        in = new LineNumberReader(new FileReader("src/dictionary/dtengl.txt"));
        String line;

        // Text einlesen und Häfigkeiten aller Wörter bestimmen:
        int counter = 0;
        while ((line = in.readLine()) != null) {
            String[] wf = line.split(" ");
            dict.insert(wf[0], wf[1]);
            counter++;
            // if (counter > 8000) break;
        }
        long end = System.nanoTime();
        double elapsedTime = (double) (end - start) / 1.0e06; // Zeit in msec
        System.out.print("Benötigte Einfügezeit für " + dict.getClass() + ": ");
        System.out.println(elapsedTime + " msec");

        //-----------------------------------------------------------------------------Test erfolgreiche Suche
        LineNumberReader in1;
        in1 = new LineNumberReader(new FileReader("src/dictionary/dtengl.txt"));
        String line1;
        List<String> deutscheWoerter = new LinkedList<>();
        while ((line1 = in1.readLine()) != null) {
            String[] wf1 = line1.split(" ");
            deutscheWoerter.add(wf1[0]);
        }
        long start1 = System.nanoTime(); // aktuelle Zeit in nsec
        for (String s: deutscheWoerter) {
            dict.search(s);
        }
        long end1 = System.nanoTime();
        double elapsedTime1 = (double) (end1 - start1) / 1.0e06;
        System.out.print("Benötigte Zeit für erfolgreiche Suche " + dict.getClass() + ": ");
        System.out.println(elapsedTime1 + " msec");

        //-----------------------------------------------------------------------------Test nicht erfolgreiche Suche
        LineNumberReader in2;
        in2 = new LineNumberReader(new FileReader("src/dictionary/dtengl.txt"));
        String line2;
        List<String> englischeWoerter = new LinkedList<>();
        while ((line2 = in2.readLine()) != null) {
            String[] wf2 = line2.split(" ");
            englischeWoerter.add(wf2[1]);
        }
        long start2 = System.nanoTime(); // aktuelle Zeit in nsec
        for (String s1: englischeWoerter) {
            dict.search(s1);
        }
        long end2 = System.nanoTime();
        double elapsedTime2 = (double) (end2 - start2) / 1.0e06;
        System.out.print("Benötigte Zeit für nicht erfolgreiche Suche " + dict.getClass() + ": ");
        System.out.println(elapsedTime2 + " msec");
        System.out.println();
    }
}
