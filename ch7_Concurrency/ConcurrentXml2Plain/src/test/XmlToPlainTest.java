package test;

import org.junit.jupiter.api.Test;
import parser.Merger;
import parser.Translator;
import parser.XmlToPlainParser;
import parser.XmlToPlainParserSync;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class XmlToPlainTest {
    @Test
    public void oneFileTest() throws Exception {
        String[] inputFileNames = {
            "res/values/ads.xml",
            "res/values/extra.xml",
            "res/values/strings.xml",
            "res/values/strings2.xml"};

        // single threaded version
        String outFileName = "output/English_output.txt";
        String logFileName = "output/log.txt";
        try (PrintStream outStreamEng =
                     new PrintStream(
                             new BufferedOutputStream(
                                     new FileOutputStream(outFileName, false)));
             PrintStream logFile =
                     new PrintStream(
                             new BufferedOutputStream(
                                     new FileOutputStream(logFileName, false))))
        {
            XmlToPlainParser parser = new XmlToPlainParser();
            for (String fileName : inputFileNames) {
                parser.parse(fileName,logFile);
            }
            parser.print(outStreamEng);
        }

        // multithreaded version
        String outFileNameMulti = "output/English_output_m.txt";
        String logFileNameMulti = "output/log_m.txt";
        ExecutorService service = null;
        try (PrintStream outStreamEng = new PrintStream(new FileOutputStream(outFileNameMulti, false));
             PrintStream logFile = new PrintStream(new FileOutputStream(logFileNameMulti, false)))
        {
            XmlToPlainParserSync parser = new XmlToPlainParserSync();
            service = Executors.newFixedThreadPool(4);

            List<Callable<Map<String,String>>> tasks = new ArrayList<>();

            for (String fileName : inputFileNames) {
                tasks.add(() -> parser.parse(fileName));
            }

            List<Future<Map<String,String>>> results = service.invokeAll(tasks);

            Map<String, Map<String,String>> fileToResultsMap = new HashMap<>();

            for (int i = 0; i < inputFileNames.length; i++) {
                fileToResultsMap.put(inputFileNames[i], results.get(i).get());
            }

            Map<String,String> allStringToValue = Merger.mergeLog(fileToResultsMap, logFile);

            Translator.translateAndWrite(allStringToValue, outStreamEng);
        }
        finally {
            service.shutdown();
        }


        Path toSingleResult = Paths.get(outFileName);
        Path toMultiResult = Paths.get(outFileNameMulti);
        List<String> singleList = Files.readAllLines(toSingleResult);
        List<String> multiList = Files.readAllLines(toMultiResult);
        singleList.sort(String::compareTo);
        multiList.sort(String::compareTo);

//        for (int i = 0; i < singleList.size(); i++) {
//            if (!singleList.get(i).equals(multiList.get(i))) {
//                String single = singleList.get(i);
//                String multy = multiList.get(i);
//                System.out.println("GOTCHA: " + i + " : \n\t" + single + "\n != \n\t" + multy);
//            }
//        }

        assert singleList.size() == multiList.size()
                : "number of entries differs: " + singleList.size() + " != "  + multiList.size();

        assert singleList.equals(multiList)
                : "singlethreaded and multithreaded outputs differ";
    }
}
