package test;

import java.io.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
// import static org.mockito.plugins.*;


import sokoban.TUI;
import sokoban.Helpers;

public class TestTextUI {
	private TUI testInstance;
	private String rootDir;
	private String testScreenPath;

	@Before
	public void setUp(){
		testInstance     =   new TUI();
		rootDir          =   System.getProperty("user.dir");
		testScreenPath   =   rootDir + "/snapshot/test_screen";
	}

	/**
	 * Tests the new game method of the game
	 */

	@Test
	public void testNewGame(){
		String initialState = testInstance.getPuzzleState();
		testInstance.execute("A");
		String afterNewGame = testInstance.getPuzzleState();
		assertFalse(initialState.equals(afterNewGame));
	}


	/**
	 * Tests the undo method of the game
	 */
	@Test
	public void testUndoMove(){
		String initialState = testInstance.getPuzzleState();
		testInstance.execute("P");
		String afterMove = testInstance.getPuzzleState();
		testInstance.execute("U");
		String afterUndo = testInstance.getPuzzleState();
		assertTrue(initialState.equals(afterUndo));
		assertFalse(initialState.equals(afterMove));
	}

	/**
	 * Tests the clear method of the game
	 */
	@Test
	public void testClearPuzzle(){
		String initialState = testInstance.getPuzzleState();
		testInstance.execute("P");
		testInstance.execute("P");
		testInstance.execute("P");
		String afterMove = testInstance.getPuzzleState();
		testInstance.execute("R");
		String afterClear = testInstance.getPuzzleState();
		assertTrue(initialState.equals(afterClear));
		assertFalse(initialState.equals(afterMove));
	}

	/**
	 * Tests the save method of the game
	 */
	@Test
	public void testSavePuzzle(){
		String initialState = testInstance.getPuzzleState();
		testInstance.execute("P");
		testInstance.execute("P");
		testInstance.execute("P");
		String afterMove = testInstance.getPuzzleState();
		String savedPath = testInstance.execute("V");
		File savedFile = new File(savedPath);
		assertTrue(savedFile.exists());

		TUI savedGame = new TUI(savedPath);
		String savedState = savedGame.getPuzzleState();
		assertTrue(savedState.equals(afterMove));
		assertFalse(savedState.equals(initialState));
	}


	/**
	 * Tests the load saved game method of the game
	 */
	@Test
	public void testLoadSavedPuzzle(){
		TUI mockInstance = spy(new TUI());
		when(mockInstance.chooseSavedPuzzle()).thenReturn("test_screen");

		InputStream sysInBackup = System.in; // backup System.in to restore it later
		ByteArrayInputStream in = new ByteArrayInputStream("My string".getBytes());
		System.setIn(in);

		mockInstance.execute("L");
		String fileContent = Helpers.fileAsString(new File(testScreenPath));
		String initialState = mockInstance.getPuzzleState();

		System.out.println("File Content:\n" + fileContent + "\n\nInitial State:\n" + initialState);

		System.setIn(sysInBackup);

		assertTrue(initialState.equals(fileContent));
	}



}
