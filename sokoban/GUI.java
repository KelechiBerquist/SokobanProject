package sokoban;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * A graphical user interface for a Sokoban puzzle.
 *
 * @author Dr Kelechi Berquist
 * @author Dr Mark C. Sinclair
 * @version March 2022
 */
public class GUI implements ActionListener  {
    /**
     * Default constructor
     */
    public GUI() {
        this.frame = new AppFrame("Sokoban Game", this);
    }

    /**
     * Controls events of components within the game frame
     */
    public void actionPerformed(ActionEvent theEvnt) {
        if (theEvnt.getActionCommand().equals("Quit")) {
            this.quitOptions();
        } else if (theEvnt.getActionCommand().equals("New")){
            this.newGame();
        } else if (theEvnt.getActionCommand().equals("N")){
            this.move(Direction.NORTH);
        } else if (theEvnt.getActionCommand().equals("S")){
            this.move(Direction.SOUTH);
        } else if (theEvnt.getActionCommand().equals("E")){
            this.move(Direction.EAST);
        } else if (theEvnt.getActionCommand().equals("W")){
            this.move(Direction.WEST);
        } else if (theEvnt.getActionCommand().equals("P")){
            this.randomMove();
        } else if (theEvnt.getActionCommand().equals("Load")){
            this.loadSavedPuzzle();
        } else if (theEvnt.getActionCommand().equals("Save")){
            this.savePuzzle();
        } else if (theEvnt.getActionCommand().equals("Restart")){
            this.restartPuzzle();
        } else if (theEvnt.getActionCommand().equals("Help")){
            this.showHelp();
        } else if (theEvnt.getActionCommand().equals("Undo")){
            this.undoMove();
        }
    }

    /**
     * Set up new game
     */
    public void newGame() {
        System.out.println("\n\nBeginning new game");
        this.msg = "Beginning new game.";
        setScreen();
        setPuzzle();
        this.refreshFrame();
    }

    /**
     * Initialise game attributes
     */
    public void setScreen() {
        Integer randomNum   =   ThreadLocalRandom.current().nextInt(minScreen, maxScreen + 1);
        screenFile          =   "screen." + randomNum.toString();
        screenPath          =   workDir + "/screens/" + screenFile;
        loadFile            =   new File(screenPath);
    }

    /**
     * Generate a new game from current game attribute
     */
    public void setPuzzle() {
        puzzle   =  new Sokoban(loadFile);
        player   =  new RandomPlayer();
        moves    =  new ArrayList<String>();
    }

    /**
     * Refresh frame and clear output message
     */
    public void setState() {
        gameState = Helpers.listFromString(puzzle.toString());
    }

    /**
     * Refresh frame and clear output message
     */
    public ArrayList<String> getState() {
       return gameState;
    }

    /**
     * Refresh frame and clear output message
     */
    private void refreshFrame() {
        this.setState();
        this.frame.refreshFrame(gameState, this.msg);
        this.msg = "";
    }

    /**
     * Move the actor according to the computer player's choice
     */
    private void randomMove() {
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
        String dirString   =  dir.toString();

        if (puzzle.onTarget()){
            System.out.println("Game Won!");
            this.msg = "Game Won!";
        }

        if (!puzzle.canMove(dir)) {
            System.out.println(dirString + " is an invalid move.");
            this.msg = dirString + " is an invalid move.";
        } else {
            this.msg = "Moving " + dirString;
            System.out.println("Moving " + dirString);
            puzzle.move(dir);
            moves.add(dirString);
        }

        if (puzzle.onTarget()){
            System.out.println("Game Won!");
            this.msg = "Game Won!";
        }
        this.refreshFrame();
    }

    /**
     * Undo last player move if move is E, W, N, S, P
     */
    private void undoMove() {
        this.msg = null;
        String beforeUndo = puzzle.toString();
        try {
            puzzle.undo();
        } catch (Exception ex) {

        } finally {
            String afterUndo = puzzle.toString();

            if (beforeUndo.equalsIgnoreCase(afterUndo)){
                System.out.println("Cannot undo move.");
                this.msg = "Cannot undo move.";
            } else {
                System.out.println("Command '" + moves.get((moves.size()-1)) + "' undone.");
                this.msg = "Command '" + moves.get((moves.size()-1)) + "' undone.";
            }
            this.refreshFrame();
        }
    }

    /**
     * Reset the puzzle state to the initial puzzle screen
     */
    private void restartPuzzle() {
        try {
            puzzle.clear();
            System.out.println("\n\nResetting puzzle to initial state.");
            this.msg = "Resetting puzzle to initial state.";
        } catch (IllegalStateException ex) {
            System.out.println("No initial game state available!");
            this.msg = "No initial game state available!";
        }
        this.refreshFrame();
    }

    /**
     * Save puzzle state to file
     */
    private void savePuzzle() {
        if (puzzle == null) {
            System.out.println("No game available to save.");
            this.msg = "No game available to save.";
        } else if (puzzle.onTarget()){
            System.out.println("Cannot save completed game.");
            this.msg = "Cannot save completed game.";
        } else {
            DateTimeFormatter dtf   =  DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
            LocalDateTime     now   =  LocalDateTime.now();
            String filename = "puzzle-" + dtf.format(now);
            String filepath = workDir + "/snapshot/" + filename;
            String relFilepath = "./snapshot/" + filename;

            try (PrintStream out = new PrintStream(new FileOutputStream(filepath))) {
                out.print(puzzle.toString());
                System.out.println("Puzzle saved in: " + filepath);
                this.msg = "Puzzle saved in: \n" + relFilepath;
            } catch (FileNotFoundException e){
                throw new SokobanException("File not found" + e);
            }
        }
        this.refreshFrame();
    }

    /**
     * Load saved puzzle from file
     */
    private void loadSavedPuzzle() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int fileChoice = fileChooser.showOpenDialog(this.frame);
        if(fileChoice == JFileChooser.APPROVE_OPTION){
            loadFile  =  fileChooser.getSelectedFile();
            setPuzzle();
            String absPath = loadFile.getAbsolutePath();
            String relPath = ".".concat(absPath.substring(workDir.length()));
            System.out.println("Loaded saved game from \n" + relPath + "\n");
            this.msg = "Loaded saved game from " + relPath;
        } else{
            System.out.println("Unable to load saved game");
            this.msg = "Unable to load saved game";
        }
        this.refreshFrame();
    }

    /**
     * Show game help
     */
    private void showHelp() {
        String helpText = String.join(
            "\n",
            "Read up on how to play the game: \n    https://en.wikipedia.org/wiki/Sokoban",
            "Here are the options within this application:",
                "     [Quit]       Quit the current game",
                "     [New]        Start a new game",
                "     [Load]       Load from a previously saved game",
                "     [Save]       Save the current game",
                "     [Restart]    Restart this game from the beginning",
                "     [Undo]       Undo the previous player move",
                "     [Help]       Opens this help panel",
                "     [N]          Move actor north",
                "     [P]          Make computer move",
                "     [W]          Move actor west",
                "     [E]          Move actor east",
                "     [S]          Move actor south"
        );
        TextPanePanel feedbackPane  =  new TextPanePanel(font, helpText);
        JOptionPane.showMessageDialog(new JFrame(), feedbackPane);
    }

    /**
     * Give player option to quit game (with or without saving)
     */
    private void quitOptions() {
        Integer saveOnQuit = JOptionPane.showConfirmDialog(null, "Would you like to save before quitting?");
        switch (saveOnQuit) {
            case (JOptionPane.YES_OPTION):
                savePuzzle();
                quitPuzzle();
                break;

            case (JOptionPane.NO_OPTION):
                quitPuzzle();
                break;
            case (JOptionPane.CANCEL_OPTION):
                return;
        }
    }

    /**
     * Quit game
     */
    private void quitPuzzle() {
        System.out.println("Program shutting down.");
        this.msg = "Program shutting down.";
        this.refreshFrame();
        this.frame.setVisible(false);
        this.frame.dispose();
        System.exit(0);
    }

    /**
     * Function for executing this game application
     *  via gui without initialising instance
     * @param args provided arguments
     */
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.newGame();
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

    private Sokoban  puzzle     =  null;
    private Player   player     =  null;
    private File     loadFile   =  null;
    private AppFrame frame      =  null;
    private String   msg        =  "";
    private String   screenFile =  null;
    private String   screenPath =  null;

    private ArrayList<String>  moves     = null;
    private ArrayList<String> gameState  = null;

    private static Font    font          =  new Font("Monospaced", Font.BOLD, 20);
    private static Integer minScreen     = 1;
    private static Integer maxScreen     = 90;
    private static boolean traceOn       = true; // for debugging
    private static String  workDir       = System.getProperty("user.dir");
}
