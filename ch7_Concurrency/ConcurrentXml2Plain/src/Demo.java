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
        System.out.println("run #1:");
//        singleThreadRun();
        multiThreadRun();

    }

    static void singleThreadRun() throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("\t\t Single thread");
        translateEnglish();
        translateItalian();

        long endTime = System.currentTimeMillis();

        System.out.println("time left: " + (endTime - startTime));
    }

    static void multiThreadRun() throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("\t\t Multi thread");
        translateEnglishParalel();
//        translateItalianParalel();

        long endTime = System.currentTimeMillis();

        System.out.println("time left: " + (endTime - startTime));
    }

    static void translateEnglish() throws Exception {
        XmlToPlainParser parser = new XmlToPlainParser();

        PrintStream outStreamEng = null;
        try {
            outStreamEng = new PrintStream(new FileOutputStream("output/English_output.txt", false));
            for (String fileName : fileNamesEnglish) {
                parser.parse(fileName);
            }
            parser.print(outStreamEng);
        } finally {
            outStreamEng.close();
        }
    }

    static void translateEnglishParalel() throws Exception {
        XmlToPlainParserSync parser = new XmlToPlainParserSync();

        PrintStream outStreamEng = null;
        ExecutorService service = null;
        try {
            outStreamEng = new PrintStream(new FileOutputStream("output/English_output_m.txt", false));
            int nCores = Runtime.getRuntime().availableProcessors();
            service = Executors.newFixedThreadPool(nCores);

            List<Callable<Map<String,String>>> tasks = new ArrayList<>();

//            Arrays.asList(fileNamesEnglish)
//                    .forEach(fileName -> tasks.add(() -> parser.parse(fileName)));

            for (String fileName : fileNamesEnglish) {
                tasks.add(() -> parser.parse(fileName));
            }

            List<Future<Map<String,String>>> results = service.invokeAll(tasks);

            Map<String, Map<String,String>> fileToResultsMap = new HashMap<>();

            for (int i = 0; i < fileNamesEnglish.length; i++) {
                fileToResultsMap.put(fileNamesEnglish[i], results.get(i).get());
            }

            Merger merger = new Merger();
            merger.mergeLog(fileToResultsMap);


        } finally {
            if (service != null) {
                service.shutdown();
            }
            outStreamEng.close();
        }
    }


    static void translateItalian() throws Exception {
        PrintStream outStreamItalian = null;
        try {
            outStreamItalian = new PrintStream(new FileOutputStream("output/Italian_output.txt", false));

            XmlToPlainParser parser = new XmlToPlainParser();
            for (String filename : fileNamesItalian) {
                parser.parse(filename);
            }
            parser.print(outStreamItalian);
        } finally {
            outStreamItalian.close();
        }
    }

}
