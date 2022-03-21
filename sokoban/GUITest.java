package sokoban;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test class for GUI class
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class GUITest {
	/**
	 * Test set up steps
	 */
	@Before
	public void setUp(){
		rootDir          =   System.getProperty("user.dir");
		testScreenPath   =   rootDir + "/snapshot/test_screen";
		testInstance     =   new GUI();
		testInstance.setScreen();
		testInstance.setPuzzle();
	}

	/**
	 * Tests Default class constructor
	 */
	@Test
	public void testDefaultConstructorAndState(){
		GUI anInstance     =   new GUI();
		assertNotNull(anInstance);

		// Test game state
		anInstance.setScreen();
		anInstance.setPuzzle();
		anInstance.setState();
		ArrayList<String> gameState = anInstance.getState();
		assertNotNull(gameState);
	}

	/**
	 * Tests AppFrame constructor
	 */
	@Test
	public void testAppFrame(){
		AppFrame aFrame = new AppFrame("Test Frame", testInstance);
		// Tests constructor
		assertNotNull(aFrame);
	}

	/**
	 * Tests ButtonFactory constructor
	 */
	@Test
	public void testButtonFactory(){
		ButtonFactory aBtn = new ButtonFactory(new String[]{"Test Button", "Test Button Tooltip"});
		assertNotNull(aBtn);
		assertTrue(aBtn.getText().equalsIgnoreCase("Test Button"));
		assertTrue(aBtn.getToolTipText().equalsIgnoreCase("Test Button Tooltip"));
	}

	/**
	 * Tests AdminPanel Component content
	 */
	@Test
	public void testAdminPanel(){
		AdminPanel aPanel = new AdminPanel(font, testInstance);
		// Tests constructor
		assertNotNull(aPanel);

		// Retrieve component of the panel
		Component[] components = aPanel.getComponents();

		// Assert that this component contains the expected component.
		assertEquals(components.length, 3);

		// Assert that the content of the component is as expected.
		Set<String> textInButtons = Set.of("New", "Load", "Quit");
		for (Component aComp: components){
			ButtonFactory aBtn = (ButtonFactory) aComp;
			assertTrue(textInButtons.contains(aBtn.getText()));
		}
	}

	/**
	 * Tests BoardPanel Component content
	 */
	@Test
	public void testBoardPanel(){
		GUI anInstance     =   new GUI();
		anInstance.setScreen();
		anInstance.setPuzzle();
		anInstance.setState();

		ArrayList<String> gameState = anInstance.getState();

		BoardPanel aPanel = new BoardPanel(gameState, font);
		// Tests constructor
		assertNotNull(aPanel);

		// Retrieve component of the panel
		Component[] components = aPanel.getComponents();

		// Assert that this component contains the expected component.
		assertEquals(components.length, 30*30);


		// Assert that the content of the component is as expected.
		Set<String> textInButtons = Set.of("");
		for (Component aComp: components){
			ButtonFactory aBtn = (ButtonFactory) aComp;
			assertTrue(textInButtons.contains(aBtn.getText()));
			assertNull(aBtn.getToolTipText());
		}
	}

	/**
	 * Tests HelpUndoPanel Component content
	 */
	@Test
	public void testHelpUndoPanel(){
		HelpUndoPanel aPanel = new HelpUndoPanel(font, testInstance);
		// Tests constructor
		assertNotNull(aPanel);

		// Retrieve component of the panel
		Component[] components = aPanel.getComponents();

		// Assert that this component contains the expected component.
		assertEquals(components.length, 2);

		// Assert that the content of the component is as expected.
		Set<String> textInButtons = Set.of("Undo", "Help");
		Set<String> tipInButtons = Set.of("Undo Last Move", "Get Help!");
		for (Component aComp: components){
			ButtonFactory aBtn = (ButtonFactory) aComp;
			assertTrue(textInButtons.contains(aBtn.getText()));
			if (aBtn.getText().equals("")) {
				assertNull(aBtn.getToolTipText());
			} else {
				assertTrue(tipInButtons.contains(aBtn.getToolTipText()));
			}
		}
	}

	/**
	 * Tests MovePanel Component content
	 */
	@Test
	public void testMovePanel(){
		MovePanel aPanel = new MovePanel(font, testInstance);
		// Tests constructor
		assertNotNull(aPanel);

		// Retrieve component of the panel
		Component[] components = aPanel.getComponents();

		// Assert that this component contains the expected component.
		assertEquals(components.length, 9);

		// Assert that the content of the component is as expected.
		Set<String> textInButtons = Set.of("N", "W", "E", "S", "P", "");
		Set<String> tipInButtons = Set.of(
			"Move North", "Move South", "Move East",
			"Move West", "Random Move", ""
		);
		for (Component aComp: components){
			ButtonFactory aBtn = (ButtonFactory) aComp;
			assertTrue(textInButtons.contains(aBtn.getText()));
			if (aBtn.getText().equals("")) {
				assertNull(aBtn.getToolTipText());
			} else {
				assertTrue(tipInButtons.contains(aBtn.getToolTipText()));
			}
		}
	}

	/**
	 * Tests SaveRestartPanel Component content
	 */
	@Test
	public void testSaveRestartPanel(){
		SaveRestartPanel aPanel = new SaveRestartPanel(font, testInstance);
		// Tests constructor
		assertNotNull(aPanel);

		// Retrieve component of the panel
		Component[] components = aPanel.getComponents();

		// Assert that this component contains the expected component.
		assertEquals(components.length, 2);

		// Assert that the content of the component is as expected.
		Set<String> textInButtons = Set.of("Save", "Restart");
		Set<String> tipInButtons = Set.of("Save Game", "Restart Game");
		for (Component aComp: components){
			ButtonFactory aBtn = (ButtonFactory) aComp;
			assertTrue(textInButtons.contains(aBtn.getText()));
			if (aBtn.getText().equals("")) {
				assertNull(aBtn.getToolTipText());
			} else {
				assertTrue(tipInButtons.contains(aBtn.getToolTipText()));
			}
		}
	}

	/**
	 * Tests PanelInPanel Component content
	 */
	@Test
	public void testPanelInPanel(){
		PanelInPanel aPanel = new PanelInPanel(new JPanel[]{new JPanel(), new JPanel()});
		// Tests constructor
		assertNotNull(aPanel);

		// Retrieve component of the panel
		Component[] components = aPanel.getComponents();

		// Assert that this component contains the expected component.
		assertEquals(components.length, 2);
	}

	/**
	 * Tests TextPanePanel Component content
	 */
	@Test
	public void testTextPanePanel(){
		TextPanePanel aPanel = new TextPanePanel(font, "Test String");
		// Tests constructor
		assertNotNull(aPanel);

		// Retrieve component of the panel
		Component[] components = aPanel.getComponents();

		// Assert that this component contains the expected component.
		assertEquals(components.length, 1);

		JTextPane aPane = (JTextPane) components[0];
		assertEquals(aPane.getText(),  "Test String");
	}


	private GUI testInstance;
	String testScreenPath;
	String rootDir;
	private static Font  font  =  new Font("Monospaced", Font.BOLD, 20);
}
