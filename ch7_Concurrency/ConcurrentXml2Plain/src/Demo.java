import parser.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Demo {
    static String[] fileNamesEnglish = {
            "res/values/ads.xml",
            "res/values/extra.xml",
            "res/values/strings.xml",
            "res/values/strings2.xml"};

    static String[] fileNamesItalian = {
            "res/values-it/ads.xml",
            "res/values-it/extra.xml",
            "res/values-it/strings.xml",
            "res/values-it/strings2.xml"};

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 5; i++) {
            System.out.println("run #" + i + ":");

            singleThreadRun();
            multiThreadRun(1);
            multiThreadRun(4);
        }
    }

    static void singleThreadRun() throws Exception {
        long startTime = System.currentTimeMillis();

        System.out.print("\t\t Single  \t");
        translateEnglishSingleThread();

        long endTime = System.currentTimeMillis();

        System.out.println("ms elapsed: " + (endTime - startTime));
    }


    static void multiThreadRun(int nThreads) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.print("\t\t Multi " + nThreads + "\t");
        translateEnglishParalel(nThreads);

        long endTime = System.currentTimeMillis();

        System.out.println("ms elapsed: " + (endTime - startTime));
    }


    static void translateEnglishSingleThread() throws Exception {
        String[] inputFileNames = fileNamesEnglish;
        String outFileName = "output/English_output.txt";
        String logFileName = "output/log.txt";
        try (PrintStream outStreamEng = new PrintStream(new FileOutputStream(outFileName, false));
             PrintStream logFile = new PrintStream(new FileOutputStream(logFileName, false)))
        {
            XmlToPlainParser parser = new XmlToPlainParser();
            for (String fileName : inputFileNames) {
                parser.parse(fileName,logFile);
            }
            parser.print(outStreamEng);
        }
    }

    static void translateEnglishParalel(int nThreads) throws Exception {
        String[] inputFileNames = fileNamesEnglish;
        String outFileNameMulty = "output/English_output_m.txt";
        String logFileName = "output/log_m.txt";
        ExecutorService service = null;
        try (PrintStream outStreamEng = new PrintStream(new FileOutputStream(outFileNameMulty, false));
             PrintStream logFile = new PrintStream(new FileOutputStream(logFileName, false));) {
            XmlToPlainParserSync parser = new XmlToPlainParserSync();
            service = Executors.newFixedThreadPool(nThreads);

            List<Callable<Map<String, String>>> tasks = new ArrayList<>();

            for (String fileName : inputFileNames) {
                tasks.add(() -> parser.parse(fileName));
            }

            List<Future<Map<String, String>>> results = service.invokeAll(tasks);

            Map<String, Map<String, String>> fileToResultsMap = new HashMap<>();

            for (int i = 0; i < inputFileNames.length; i++) {
                fileToResultsMap.put(inputFileNames[i], results.get(i).get());
            }

            Map<String, String> allStringToValue = Merger.mergeLog(fileToResultsMap, logFile);

            Translator.translateAndWrite(allStringToValue, outStreamEng);
        }
        finally {
            service.shutdown();
        }
    }
}