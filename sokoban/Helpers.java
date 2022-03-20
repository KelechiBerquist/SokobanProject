package sokoban;

import java.io.*;
import java.util.*;

/**
 * File and string io handling class
 * @author Dr Kelechi Berquist
 * @author Dr Mark C. Sinclair
 * @version March 2022
 */
public final class Helpers {
	/**
	 * Get a list of Strings from a multiline String.
	 */
	/**
	 * Reads string into an array list
	 * @param str String to be read
	 * @return ArrayList output of string with newline as line break
	 */
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

	/**
	 * Converts file content to string
	 * @param file File with content about to be read
	 * @return String content of file
	 */
	public static String fileAsString(File file){
		if (file == null)
		throw new IllegalArgumentException("file cannot be null");
		Scanner      fscnr = null;
		StringBuffer sb    = new StringBuffer();
		try {
			fscnr = new Scanner(file);
			while (fscnr.hasNextLine())
				sb.append(fscnr.nextLine()+"\n");
		} catch(IOException e) {
			throw new SokobanException(""+e);
		} finally {
			if (fscnr != null)
				fscnr.close();
		}
		return sb.toString();
	}
}
