package test;

import org.junit.jupiter.api.Test;

import parser.XmlToPlainParser;

import java.io.FileOutputStream;
import java.io.PrintStream;


public class XmlToPlainTest {
    @Test
    public void oneFileTest() throws Exception {
        String[] fileNamesEnglish = {"res/values/ads.xml"};
        XmlToPlainParser parser = new XmlToPlainParser();

        PrintStream outStreamEng = null;
        try {
            outStreamEng = new PrintStream(new FileOutputStream("output/English_output.txt", false));
        } finally {
            outStreamEng.close();
        }
    }
}
