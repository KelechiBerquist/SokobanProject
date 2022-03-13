package sokoban;

import java.util.ArrayList;
import java.util.Scanner;

public class Helpers {
	Helpers(){

	}
	public static ArrayList<String> listFromString(String str){
		Scanner           scnr = null;
		ArrayList<String> lines = new ArrayList<>();
		scnr = new Scanner(str);
		while (scnr.hasNextLine()) {
			String line = scnr.nextLine();
			if (line.length() > 0)
				lines.add(line);
		}
		scnr.close();
		return lines;
	}

}
