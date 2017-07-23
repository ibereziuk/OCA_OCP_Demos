import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class RpcGame {
	private static final Console console = System.console();
	
	enum HandSign {
		Rock, Paper, Scissors
	}
	
	public void run() {
		greetings();
		mainLoop();
		bye(false);
	}
	
	private void mainLoop() {
		while (true) {
			String answer = console.readLine("Play? [yes/no]:")
					.trim()
					.toLowerCase();
			if (answer.matches("yes|y")) {
				System.out.println("Lets do it!");
				doRound();
			} else if (answer.matches("no|n")) {
				break;
			} else {
				System.out.println("Hey, type \"yes\" or \"no\"");
			}
		}
	}
	
	static String readFile(Path path, Charset encoding) 
			throws IOException {
		byte[] encoded = Files.readAllBytes(path);
		return new String(encoded, encoding);
	}
	
	private void doRound() {
		int nPlayers = 2;
		int screenWidth = 80;
		
		List<String> playerNames = getPlayerNames(nPlayers);
		displayCaption(playerNames,screenWidth);
		
		Map<String,HandSign> plNameSighMap = playerNames.stream()
				.collect(Collectors.toMap(
						name -> name,
						name -> readSigh(name)));
		
		Map<String, List<String>> rawPictures = new HashMap<>();
		for (Map.Entry<String, HandSign> entry : plNameSighMap.entrySet()) {
			String name = entry.getKey();
			HandSign sign = entry.getValue();
			
			rawPictures.put(name, getPicture(sign));
		}
		
		String pictures = mergePicts(rawPictures, playerNames, screenWidth);

		System.out.println(pictures);
		
		long nSignPresent = plNameSighMap.values().stream()
				.distinct()
				.count();
		boolean draw = nSignPresent == 1 || nSignPresent == 3;
		
		if (draw) {
			System.out.println("\n" 
					+ padCenter(">>>>>> Draw <<<<<<", screenWidth) + "\n");
		} else {
			List<String> winners = getWinnerList(plNameSighMap);
			System.out.println("\n" 
					+ padCenter(">>>>>> Player " + winners.get(0) + " won <<<<<<", screenWidth) + "\n");
		}
	}
	
	
	private List<String> getWinnerList(Map<String, HandSign> nameSignMap) {
		List<HandSign> presentSigns = nameSignMap.values().stream()
				.distinct()
				.collect(Collectors.toList());

		HandSign winnerSign = getWinnerSign(presentSigns.get(0), presentSigns.get(1));
		
		List<String> winnerList = nameSignMap.entrySet().stream()
				.filter(entry -> entry.getValue() == winnerSign)
				.map(entry -> entry.getKey())
				.collect(Collectors.toList());
		
		return winnerList;
	}
	
	private HandSign getWinnerSign(HandSign s1, HandSign s2) {
		HandSign winnerSign = null;
		switch(s1) {
			case Rock:
				winnerSign = s2 != HandSign.Paper 
						? HandSign.Rock 
						: HandSign.Paper; 
				break;
			case Paper:
				winnerSign = s2 != HandSign.Scissors
						? HandSign.Paper
						: HandSign.Scissors;
				break;
			case Scissors:
				winnerSign = s2 != HandSign.Rock
						? HandSign.Scissors
						: HandSign.Rock;
				break;
		}
		return winnerSign;
	}

	private List<String> getPlayerNames(int nPlayers){
		List<String> names = new ArrayList<>();
		names.add("Apollo");
		names.add("Quantum");
		return names;
	}
	
	private String mergePicts(
			Map<String, List<String>> rawPictures,
			List<String> playerNames,
			int screenWidth) {
		assert playerNames.size() == 2 
				: "This game now supports only 2-players mode";
		
		int pictureWidth = screenWidth / 2;
		
		int pictureHeight = rawPictures.values().stream()
				.map(imgLines -> imgLines.size())
				.collect(Collectors.maxBy(Integer::compareTo))
				.get()
				.intValue();

		List<List<String>> rectImages = playerNames.stream()
			.map(name -> rawPictures.get(name))
			.map(rawPic -> rectPicture(rawPic, pictureWidth, pictureHeight))
			.collect(Collectors.toList());
		
		StringJoiner joiner = new StringJoiner("\n");
		for (int i = 0; i < pictureHeight; i++) {
			String s = rectImages.get(0).get(i) + rectImages.get(1).get(i);
			joiner.add(s);
		}
		
		return joiner.toString();
	}
	
	
	private String mergePicts(List<String> picture1Lines, 
			List<String> picture2Lines,
			int screenWidth) {
		int pictureWidth = screenWidth / 2;
		int pictureHeight = Math.max(picture1Lines.size(), picture2Lines.size());
		
		List<String> pic1Rect = 
				rectPicture(picture1Lines, pictureWidth, pictureHeight);
		List<String> pic2Rect = 
				rectPicture(picture2Lines, pictureWidth, pictureHeight);
		
		StringJoiner joiner = new StringJoiner("\n");
		for (int i = 0; i < pictureHeight; i++) {
			String s = pic1Rect.get(i) + pic2Rect.get(i);
			joiner.add(s);
		}
		
		return joiner.toString();
	}
	
	private List<String> rectPicture(List<String> lines, int width, int height) {
		List<String> paddedLines = lines.stream()
			.map(s -> padLeft(s,width))
			.collect(Collectors.toList());
			
		String spaceLine = padLeft("",width);
		for (int i = paddedLines.size() - 1; i < height; i++) {
			paddedLines.add(spaceLine);
		}
		
		return paddedLines;
	}
	
	private List<String> getPicture(HandSign sign) {
		Path resDir = Paths.get("res");
		String fileName = sign.name().toLowerCase() + ".txt";
		
		Path filePath = resDir.resolve(Paths.get(fileName));		
		
		List<String> fileLines = null; 
		try {
			fileLines = Files.readAllLines(filePath);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return fileLines;
	}
	
	
	private void displayCaption(List<String> names, int screenWidth) {
		int halfScreenWidth = screenWidth / 2;
		
		StringBuilder caption = new StringBuilder();
		names.forEach(name -> {
			caption.append(padCenter(name,halfScreenWidth));
		});
				
		System.out.println("\n" + caption + "\n");
	}
	
	private String padLeft(String str, int width) {
		int nCellsToFill = width - str.length();
		
		String pad = "";
		for (int i = 0; i < nCellsToFill; i++) {
			pad += " "; 
		}
		return str + pad;
	}
	
	
	private String padCenter(String str, int width) {
		int nSideSpaces = (width - str.length()) / 2;
		
		String pad = "";
		for (int i = 0; i < nSideSpaces; i++) {
			pad += " ";
		}
		
		return pad + str + pad;
	}
	
	
	private HandSign readSigh(String playerName) {
		char[] p1InputChars = console.readPassword("Player " 
				+ playerName + " figure [Rock, Paper, Scissors]: ");
				
		// read input until acceptable
		HandSign p1Sign;
		for (p1Sign = parseSighInput(p1InputChars);
				p1Sign == null;
				p1Sign = parseSighInput(p1InputChars)) {
			p1InputChars = console.readPassword(
				"Just type Rock, Paper or Scissors: ");
		}
		return p1Sign;
	}
	
	
	private HandSign parseSighInput(char[] input) {
		HandSign sign = null;
		
		String s = new String(input).toLowerCase();
		if (s.matches("r|rock")) {
			sign = HandSign.Rock;
		} else if (s.matches("p|paper")) {
			sign = HandSign.Paper;
		} else if (s.matches("s|scissors")) {
			sign = HandSign.Scissors;
		}
		
		return sign;
	}
	
	
	private static void greetings() {
		StringJoiner joiner = new StringJoiner("\n");
		joiner.add("******************************************");
		joiner.add("**                                      **");
		joiner.add("**         Rock-Scissors-Paper          **");
		joiner.add("**                                      **");
		joiner.add("******************************************");
		System.out.println(joiner.toString());
	}
	
	
	private static void bye(boolean withTroll) {
		Path trollFace = Paths.get("res/troll.txt");
		if (Files.isRegularFile(trollFace) && Files.isReadable(trollFace)) {
			StringJoiner joiner = new StringJoiner("\n");
			try {
				String text = readFile(trollFace, StandardCharsets.UTF_8);
				if (withTroll) System.out.println(text);
			} catch (IOException e) {
				System.out.println("Houston we have a problem! "
						+ e.getMessage());
			}
		}
		System.out.println("\n\tGood bye!!!");
	}
}