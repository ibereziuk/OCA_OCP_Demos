import java.util.function.*; // import functional interfaces
import java.time.*;
import java.util.*;
import java.util.stream.*;
import java.util.Arrays.*;

public class Play {
	public static void main(String[] args) {
		Supplier<LocalDate> s1 = LocalDate::now;
		Supplier<LocalDate> s2 = () -> LocalDate.now();
		
		LocalDate d1 = s1.get();
		LocalDate d2 = s2.get();
		
		System.out.println(d1);
		System.out.println(d2);
		
		// consumer
		
		Consumer<String> consumeString = System.out::println;
		consumeString.accept("Help Consumer!");
		
		
		BiPredicate<String, String> biPred = String::startsWith;
		String str = "johny Marshal";
		
		System.out.println(biPred.test(str, "Johny".toLowerCase()));
		
		BiFunction<String, String, String> biFun = String::concat;
		BiFunction<String, String, String> biFun2 = (s, strToAdd) -> s.concat(strToAdd);
		BiFunction<String, String, String> biFun3 = new BiFunction<String, String, String>() {
			public String apply(String s1, String s2) {
				return s1 + s2;
			}
		};
		
		System.out.println(biFun.apply("baby", " and a toy"));
		System.out.println(biFun2.apply("John", " plays basketball"));
		System.out.println(biFun3.apply("a", "b"));		
		
		
		System.out.println();
		// playWithStreams();
		// fPrimitives();
		// optionalAsStream();
		streamToMap();
	}
	
	
	static void playWithStreams() {
		Stream<String> emptyString = Stream.empty();
		Stream<Integer> streamInteger = Stream.of(11,22,33);
		
		List<String> list = Arrays.asList("Oksana", "Olya", "Jaina", "2");
		Stream<String> streamFromList = list.stream();
		
		System.out.println(streamFromList.count());
		
		System.out.println(list.stream().anyMatch(s -> s.startsWith("O")));
		System.out.println(list.stream().allMatch(s -> s.length() < 5));
		System.out.println(list.stream().noneMatch(s -> Character.isLetter(s.charAt(0))));
		
		Stream<String> wolfLettersStream = Stream.of("W", "o", "l", "f");
		String word = wolfLettersStream.reduce("", (s1,s2) -> s1.concat(s2));
		System.out.println(word);
		
		wolfLettersStream = Stream.of("W", "o", "l", "f");
		Optional<String> optStr = wolfLettersStream.reduce((s1,s2) -> s1.concat(s2));
		System.out.println(optStr);
		
		
		wolfLettersStream = Stream.of("W", "o", "l", "f");
		StringBuilder sbExt = wolfLettersStream.collect(
			StringBuilder::new,
			StringBuilder::append,
			StringBuilder::append);
		System.out.println(sbExt);
		
		
		wolfLettersStream = Stream.of("W", "o", "l", "f");
		BiConsumer<StringBuilder, StringBuilder> strBuilderStrBuilderCombiner = (strBuilder1, strBuilder2) -> {
			strBuilder1.append(strBuilder2.toString());
		};
		BiConsumer<StringBuilder, String> strAccumulator = (strBuilder, str) -> {
			strBuilder.append(str);
		};
		Supplier<StringBuilder> sbSupplier = () -> new StringBuilder();
		sbExt = wolfLettersStream.collect(
			sbSupplier,
			strAccumulator,
			strBuilderStrBuilderCombiner);
		System.out.println(sbExt);
		
		
		wolfLettersStream = Stream.of("W", "o", "l", "f");
		sbExt = wolfLettersStream.collect(
			() -> new StringBuilder(),
			(sb, str) -> sb.append(str),
			(sb1, sb2) -> sb1.append(sb2.toString()));
		System.out.println(sbExt);
		
		String[] strArr = {"W", "o", "l", "f"};
		StringBuilder sbExtC = new StringBuilder();
		for(String s : strArr) {
			sbExtC.append(s);
		}
		System.out.println(sbExtC);
	}	
	
	
	static void fPrimitives() {
		LongStream longs = LongStream.of(111,222);
		long sum = longs.sum();
		
		longs = LongStream.of(111, 222);
		double avg = longs.average().getAsDouble();
		System.out.println("sum: " + sum + " avg: " + avg);
	}
	
	static void optionalAsStream() {
		Optional<Integer> optInt = Optional.of(322);
		optInt.map(i -> "" + i)
			.filter(s -> s.length() < 4)
			.ifPresent(s -> System.out.println("value: " + s));
	}
	
	static void streamToMap() {
		Stream<String> stream = Stream.of("June", "July", "August");
		TreeMap<Integer, String> map = stream.collect(Collectors.toMap(
			String::length,
			k -> k,
			(s1,s2) -> s1 + "," + s2,	// to solve two equal keys conflict
			TreeMap::new
		));

		System.out.println(map);
		
		Stream<String> stream2 = Stream.of("June", "July", "August");
		Map<Integer, Set<String>> map2 = stream2.collect(
			Collectors.groupingBy(String::length, Collectors.toSet()));

		System.out.println(map2);
		
		
		Stream<String> stream3 = Stream.of("Java bug");
		stream3.noneMatch(s -> s.length() < 3);
		stream3.allMatch(s -> s.length() < 3);
	}
}