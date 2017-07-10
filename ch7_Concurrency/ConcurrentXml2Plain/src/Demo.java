import parser.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.Integer.min;

public class Demo {
    static String[] fileNamesEnglish = {
            "res/values/strings2.xml",
            "res/values/ads.xml",
            "res/values/extra.xml",
            "res/values/strings.xml"};

    static String[] fileNamesItalian = {
            "res/values-it/ads.xml",
            "res/values-it/extra.xml",
            "res/values-it/strings.xml",
            "res/values-it/strings2.xml"};

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 5; i++) {
            System.out.println("run #" + i + ":");

            singleThreadRun();
            multiThreadRun(4);
            multiThreadRun(2);
            multiThreadRun(1);
        }
    }

    static void singleThreadRun() throws Exception {
        long startTime = System.currentTimeMillis();

        System.out.print("\t\t Single  \t");
        translateEnglishSingleThread();

        long timeElapsed = System.currentTimeMillis() - startTime;
        System.out.print("ms elapsed: " + timeElapsed + "\t");
        printBar((int)timeElapsed / 10);
        System.out.println();
    }


    static void multiThreadRun(int nThreads) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.print("\t\t Multi " + nThreads + "\t");
        translateEnglishParalel(nThreads);

        long timeElapsed = System.currentTimeMillis() - startTime;
        System.out.print("ms elapsed: " + timeElapsed + "\t");
        printBar(((int)timeElapsed + 5) / 10);
        System.out.println();
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
        String outFileNameMulti = "output/English_output_m.txt";
        String logFileName = "output/log_m.txt";

        ExecutorService service = null;
        try (PrintStream outStreamEng = new PrintStream(new FileOutputStream(outFileNameMulti, false));
             PrintStream logFile = new PrintStream(new FileOutputStream(logFileName, false))) {
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

    static void printBar(int length) {
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < min(5,length); i++) {
            builder.append(blue(" "));
        }

        for (int i = 0; i < min(5,length - 5); i++) {
            builder.append(green(" "));
        }

        for (int i = 0; i < min(5,length - 10); i++) {
            builder.append(yellow(" "));
        }

        for (int i = 0; i < length - 15; i++) {
            builder.append(red(" "));
        }

        System.out.print(builder.toString());
    }


    static String green(String str){
        return "\u001B[42m" + str + "\u001B[0m";
    }

    static String yellow(String str){
        return "\u001B[43m" + str + "\u001B[0m";
    }

    static String red(String str){
        return "\u001B[41m" + str + "\u001B[0m";
    }

    static String blue(String str){
        return "\u001B[46m" + str + "\u001B[0m";
    }
}