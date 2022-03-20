package sokoban;

import java.util.*;

/**
 * Entry point into playing this application for Sokoban puzzle.
 *
 * @author Dr Kelechi Berquist
 * @author Dr Mark C. Sinclair
 * @version March 2022
 */
public class AppEntrypoint  {
	/**
	 * Default constructor
	 */
	public AppEntrypoint() {
		scnr  = new Scanner(System.in);
		tUI   = new TUI();
		gUI   = new GUI();
	}

	/**
	 * Generate game ui per user preference
	 */
	private void genPlayOptions(){
		System.out.println(String.join(
			"\n",
			"Enter command using the keyboard.",
			"Possible commands:",
			"     Use Text UI           [T]",
			"     Use Graphical UI      [G]",
			"     Quit Game             [Q]",
			"Enter command: "
		));
		String command = scnr.nextLine();

		if (command.equalsIgnoreCase("Q")) {
			System.out.println("Program shutting down.");
			System.exit(0);
		} else if (command.equalsIgnoreCase("T")) {
			tUI.newGame();
			tUI.menu();
		} else if (command.equalsIgnoreCase("G")) {
			gUI.newGame();
		} else {
			System.out.println("Unknown command (" + command + ")");
		}
	}

	/**
	 * Main entry point into app
	 */
	public static void main(String[] args) {
		AppEntrypoint play = new AppEntrypoint();
		play.genPlayOptions();
	}

	/**
	 * A trace method for debugging (active when traceOn is true)
	 *
	 * @param s the string to output
	 */
	public static void trace(String s) {
		if (traceOn)
			System.out.println("trace: " + s);
	}

	private TUI     tUI   = null;
	private GUI     gUI   = null;
	private Scanner scnr  = null;
	private static  boolean traceOn       = true; // for debugging
}
