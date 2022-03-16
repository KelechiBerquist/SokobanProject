package sokoban;
import java.io.*;
import java.util.*;


public class Helpers {
	Helpers(){}

	/** 
	 * Get a list of Strings from a multiline String.
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
	 * Convert file to string
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
