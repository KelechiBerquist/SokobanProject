package sokoban.ui;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import sokoban.actions.Sokoban;
import sokoban.actions.SokobanException;
import sokoban.actions.RandomPlayer;
import sokoban.actions.Player;
import sokoban.actions.Direction;

/**
 * A text-based user interface for a Sokoban puzzle.
 *
 * @author Dr Mark C. Sinclair
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class SokobanTextUI {
	/**
	 * Default constructor
	 */
	public SokobanTextUI() {
		scnr   = new Scanner(System.in);
		String filename = getScreenFileName();
		puzzle = new Sokoban(new File(filename));
		player = new RandomPlayer();
	}

	/**
	 * Main control loop.  This displays the puzzle, then enters a loop displaying a menu,
	 * getting the user command, executing the command, displaying the puzzle and checking
	 * if further moves are possible
	 */
	public void menu() {
		String command = "";
		System.out.print("\n\n" + puzzle);
		while (!command.equalsIgnoreCase("Quit") && !puzzle.onTarget())  {
			displayMenu();
			command = getCommand();
			execute(command);
			System.out.print("\n\n" + puzzle);
			if (puzzle.onTarget())
				System.out.println("puzzle is complete");
			trace("onTarget: "+puzzle.numOnTarget());
		}
	}

	/**
	 * Display the user menu
	 */
	private void displayMenu()  {
		System.out.println("Commands are:");
		System.out.println("   Move North         [N]");
		System.out.println("   Move South         [S]");
		System.out.println("   Move East          [E]");
		System.out.println("   Move West          [W]");
		System.out.println("   Player move        [P]");
		System.out.println("   Undo move          [U]");
		System.out.println("   Restart puzzle [Clear]");
		System.out.println("   Save to file    [Save]");
		System.out.println("   Load from file  [Load]");
		System.out.println("   To end program  [Quit]");
	}

	/**
	 * Get the user command
	 *
	 * @return the user command string
	 */
	private String getCommand() {
		System.out.print ("Enter command: ");
		return scnr.nextLine();
	}

	/**
	 * Execute the user command string
	 *
	 * @param command the user command string
	 */
	private void execute(String command) {
		if (command.equalsIgnoreCase("Quit")) {
			System.out.println("Program closing down");
			System.exit(0);
		} else if (command.equalsIgnoreCase("N")) {
			north();
		} else if (command.equalsIgnoreCase("S")) {
			south();
		} else if (command.equalsIgnoreCase("E")) {
			east();
		} else if (command.equalsIgnoreCase("W")) {
			west();
		} else if (command.equalsIgnoreCase("P")) {
			playerMove();
		} else if (command.equalsIgnoreCase("U")) {
			System.out.println("not implemented yet");
		} else if (command.equalsIgnoreCase("Clear")) {
			System.out.println("not implemented yet");
		} else if (command.equalsIgnoreCase("Save")) {
			savePuzzle(puzzle);
		} else if (command.equalsIgnoreCase("Load")) {
			System.out.println("not implemented yet");
		} else {
			System.out.println("Unknown command (" + command + ")");
		}
	}

	/**
	 * Move the actor north
	 */
	private void north() {
		move(Direction.NORTH);
	}

	/**
	 * Move the actor south
	 */
	private void south() {
		move(Direction.SOUTH);
	}

	/**
	 * Move the actor east
	 */
	private void east() {
		move(Direction.EAST);
	}

	/**
	 * Move the actor west
	 */
	private void west() {
		move(Direction.WEST);
	}

	/**
	 * Move the actor according to the computer player's choice
	 */
	private void playerMove() {
		Vector<Direction> choices = puzzle.canMove();
		Direction         choice  = player.move(choices);
		move(choice);
	}

	/**
	 * If it is safe, move the actor to the next cell in a given direction
	 *
	 * @param dir the direction to move
	 */
	private void move(Direction dir) {
		if (!puzzle.canMove(dir)) {
			System.out.println("invalid move");
			return;
		}
		puzzle.move(dir);
		if (puzzle.onTarget())
			System.out.println("game won!");
	}

	/**
	 * Dynamically returns a screen file name for the game
	 *
	 */
	private String getScreenFileName() {
		Integer randomNum = ThreadLocalRandom.current().nextInt(minScreen, maxScreen + 1);
		String screenName = "screen." + randomNum.toString();
		System.out.println("Genraring puzzle from screen:  " + screenName);
		return System.getenv("WORKDIR") + "/screens/" + screenName;
	}

	/*
	 * Save Puzzle to file
	 *
	 * @param file the file
	 * @return the file as a string
	 */
	public static void savePuzzle(Sokoban game) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
		LocalDateTime now = LocalDateTime.now();
		String filename = System.getenv("WORKDIR") + "/snapshot/puzzle-" + dtf.format(now);

		try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
			out.print(game.toString());
			System.out.println("Puzzle saved in: " + filename + "\n");
		} catch (FileNotFoundException e){
			throw new SokobanException("File not found" + e);
		}

	}

	public static void main(String[] args) {
		SokobanTextUI ui = new SokobanTextUI();
		ui.menu();
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

	private Scanner scnr      = null;
	private Sokoban puzzle    = null;
	private Player  player    = null;

	private static Integer minScreen    = 1;
	private static Integer maxScreen    = 90;
	private static boolean traceOn      = false; // for debugging
}
