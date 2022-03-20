package sokoban;

import java.io.*;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test class for TUI class
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class TUITest {
	/**
	 * Test set up steps
	 */
	@Before
	public void setUp(){
		rootDir          =   System.getProperty("user.dir");
		testScreenPath   =   rootDir + "/snapshot/test_screen";
		testInstance     =   new TUI();
		testInstance2    =   new TUI(testScreenPath);
	}

	/**
	 * Tests Default class constructor
	 */
	@Test
	public void testDefaultConstructor(){
		assertNotNull(testInstance);
	}

	/**
	 * Tests alaternative class constructor
	 */
	@Test
	public void testConstructor1(){
		assertNotNull(testInstance2);
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

		String fileContent = Helpers.fileAsString(savedFile);
		assertTrue(fileContent.equals(afterMove));
		assertFalse(fileContent.equals(initialState));
	}

	private TUI testInstance;
	private TUI testInstance2;
	String testScreenPath;
	String rootDir;
}
