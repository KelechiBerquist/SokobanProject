package sokoban;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.util.concurrent.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import static java.util.Map.entry;

/**
 * A graphical user interface for a Sokoban puzzle.
 *
 * @author Dr Kelechi Berquist
 * @author Dr Mark C. Sinclair
 * @version March 2022
 */
public class GUI  {
    /**
     * Default constructor
     */
    public GUI() {
        Image brickImg         = new ImageIcon(workDir + "/image/brick-wall.png")
            .getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
        Image boxImg           = new ImageIcon(workDir + "/image/icons8-box-16.png")
            .getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
        Image humanImg         = new ImageIcon(workDir + "/image/icons8-human-51.png")
            .getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
        Image targetImg        = new ImageIcon(workDir + "/image/icons8-target-48.png")
            .getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
        Image humanTargetImg   = new ImageIcon(workDir + "/image/icons8-target-64.png")
            .getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
        Image bullseyeImg      = new ImageIcon(workDir + "/image/icons8-bullseye-48.png")
            .getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);

        symbolMap = new HashMap<String,ImageIcon>();
        symbolMap.put("#", new ImageIcon(brickImg));
        symbolMap.put("$", new ImageIcon(boxImg));
        symbolMap.put("@", new ImageIcon(humanImg));
        symbolMap.put(".", new ImageIcon(targetImg));
        symbolMap.put("+", new ImageIcon(humanTargetImg));
        symbolMap.put("*", new ImageIcon(bullseyeImg));

        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setLayout(new BorderLayout(10, 10));
    }

    /**
     * Set up new game
     */
    public void newGame() {
        System.out.println("\n\nBeginning new game");
        String msg = "Beginning new game.";
        setScreen();
        setPuzzle();
        refreshFrame(msg);
    }

    /**
     * Initialise game attributes
     */
    private void setScreen() {
        Integer randomNum   =   ThreadLocalRandom.current().nextInt(minScreen, maxScreen + 1);
        screenFile          =   "screen." + randomNum.toString();
        screenPath          =   workDir + "/screens/" + screenFile;
        loadFile            =   new File(screenPath);
    }

    /**
     * Generate a new game from current game attribute
     */
    private void setPuzzle() {
        puzzle = new Sokoban(loadFile);
        player = new RandomPlayer();
        moves    = new ArrayList<String>();
    }

    /**
     * Generate the GUI frame
     */
    private void getFrame() {
        appFrame.getContentPane().removeAll();
        appFrame.invalidate();
        appFrame.revalidate();

        genBoard();
        footerPane();

        BorderLayout layout = new BorderLayout();
        layout.setHgap(0);
        layout.setVgap(0);

        appFrame.setLayout(layout);        
        appFrame.getContentPane().add(boardPanel, BorderLayout.CENTER);
        appFrame.getContentPane().add(footerPanel, BorderLayout.NORTH); 
        appFrame.pack();
        appFrame.setVisible(true);
    }

    /**
     * Generate a frame for footer
     */
    private void footerPane() {
        genOutMsgPane();
        genAdminBtns();
        genPlayBtns();

        footerPanel  =  new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.PAGE_AXIS));
        footerPanel.add(adminPanel);
        footerPanel.add(playPanel);
        footerPanel.add(outputPane);
    }

    /**
     * Generate a frame for output message
     */
    private void genOutMsgPane() {
        outputPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(outputPane);
        outputPane.setEditable(false);
        if (!(outMsg).equals("")){
            outputPane.setMargin( new Insets(10,10,10,10) );
            outputPane.setBackground(new Color(224, 255, 255));
            outputPane.setForeground(new Color(43, 27, 23, 255));
            outputPane.setFont(appFont);
            outputPane.setText(outMsg);
        } else {
            outputPane.setMargin( new Insets(10,10,10,10) );
            outputPane.setBackground(new Color(224, 255, 255));
            outputPane.setForeground(outputPane.getBackground());
        }
    }

    /**
     * Generate a frame for the Sokoban game board
     */
    private void genBoard() {
        ArrayList<String> currGameState = Helpers.listFromString(puzzle.toString());
        boardPanel = new JPanel();
        boardPanel.setSize(frameHeight-10, frameWidth-10);
        boardPanel.setVisible(true);
        boardPanel.setLayout(new GridLayout(numGrid, numGrid));

        Border paneEdge = BorderFactory.createEmptyBorder(50,100,0,0);
        boardPanel.setBorder(paneEdge);

        for (Integer i = 0; i < numGrid; i++){
            Integer  countBox = 0;
            String   row      = null;
            if ( i < currGameState.size()){
                row = currGameState.get(i);
                if (row.length() > 0){
                    countBox = row.length();
                }
            }

            for (Integer j = 0; j < numGrid; j++){
                JButton button = null;
                button = new JButton();
                button.setPreferredSize(new Dimension(20, 20));

                if (countBox != 0 && j >= 0 && j < countBox) {
                    String theChar =  Character.toString(row.charAt(j));
                    if (!(theChar.equalsIgnoreCase(" "))){
                        button.setIcon(symbolMap.get(theChar));
                        button.setMargin(new Insets(0, 0, 0, 0));
                    }
                }
                button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.setBorder(null);
                boardPanel.add(button);
            }
        }
    }

    /**
     * Generate the admin buttons to save, load and quit game
     */
    private void genAdminBtns() {
        JButton quitBtn     = new JButton("Quit");
        JButton newBtn      = new JButton("New");
        JButton loadBtn     = new JButton("Load");

        quitBtn.addActionListener(e ->    {quitPuzzle();});
        newBtn.addActionListener(e ->     {newGame();});
        loadBtn.addActionListener(e ->    {loadSavedPuzzle();});

        quitBtn.setToolTipText("Quit this game");
        newBtn.setToolTipText("Start a new game");
        loadBtn.setToolTipText("Load previously saved game");

        JButton[] buttonList = {quitBtn, newBtn, loadBtn};

        for (JButton eachButton: buttonList){
            eachButton.setPreferredSize(new Dimension(50, 30));
            eachButton.setOpaque(false);
            eachButton.setContentAreaFilled(false);
            eachButton.setBorderPainted(false);
            eachButton.setBorder(null);
            eachButton.setFont(appFont);
        }

        adminPanel  =  new JPanel();
        adminPanel.setLayout(new GridLayout(1, 4));
        adminPanel.add(quitBtn);
        adminPanel.add(newBtn);
        adminPanel.add(loadBtn);
    }

    /**
     * Generate the move north, south, east and west buttons
     */
    private void genPlayBtns() {
        JButton nMoveBtn     =  new JButton("N");
        JButton eMoveBtn     =  new JButton("E");
        JButton wMoveBtn     =  new JButton("W");
        JButton sMoveBtn     =  new JButton("S");
        JButton rMoveBtn     =  new JButton("P");
        JButton uMoveBtn     =  new JButton("Undo");
        JButton hMoveBtn     =  new JButton("Help");
        JButton saveBtn      =  new JButton("Save");
        JButton restartBtn   =  new JButton("Restart");
        JButton invisible1   =  new JButton("");
        JButton invisible2   =  new JButton("");
        JButton invisible3   =  new JButton("");
        JButton invisible4   =  new JButton("");

        nMoveBtn.setToolTipText("Move north");
        eMoveBtn.setToolTipText("Move east");
        wMoveBtn.setToolTipText("Move west");
        sMoveBtn.setToolTipText("Move south");
        rMoveBtn.setToolTipText("Computer move");
        uMoveBtn.setToolTipText("Undo last move");
        hMoveBtn.setToolTipText("Get help on how to play");
        saveBtn.setToolTipText("Save this game");
        restartBtn.setToolTipText("Restart this game");

        nMoveBtn.addActionListener(e ->   {move(Direction.NORTH);});
        wMoveBtn.addActionListener(e ->   {move(Direction.WEST);});
        eMoveBtn.addActionListener(e ->   {move(Direction.EAST);});
        sMoveBtn.addActionListener(e ->   {move(Direction.SOUTH);});
        saveBtn.addActionListener(e ->    {savePuzzle();});
        restartBtn.addActionListener(e -> {clearPuzzle();});
        uMoveBtn.addActionListener(e ->   {undoMove();});
        rMoveBtn.addActionListener(e ->   {playerMove();});
        hMoveBtn.addActionListener(e ->   {showHelp();});

        JButton[] buttonList = {
            nMoveBtn, eMoveBtn, wMoveBtn, sMoveBtn, uMoveBtn,
            rMoveBtn, hMoveBtn, saveBtn, restartBtn,
            invisible1, invisible2, invisible3, invisible4
        };

        for (JButton eachButton: buttonList){
            if (
                eachButton.getText().equals("Help") ||
                eachButton.getText().equals("Undo") ||
                eachButton.getText().equals("Save") ||
                eachButton.getText().equals("Restart")
            ){
                eachButton.setPreferredSize(new Dimension(100, 30));
            } else {
                eachButton.setPreferredSize(new Dimension(50, 30));
            }

            eachButton.setOpaque(false);
            eachButton.setContentAreaFilled(false);
            eachButton.setBorderPainted(false);
            eachButton.setBorder(null);
            eachButton.setFont(appFont);
        }


        JPanel nPanel = new JPanel(new FlowLayout());
        JPanel sPanel = new JPanel(new FlowLayout());
        nPanel.add(invisible1);
        nPanel.add(nMoveBtn);
        nPanel.add(invisible2);
        sPanel.add(invisible3);
        sPanel.add(sMoveBtn);
        sPanel.add(invisible4);


        JPanel movePanel = new JPanel();
        movePanel.setLayout(new BorderLayout(10,10));
        movePanel.add(nPanel,   BorderLayout.NORTH);
        movePanel.add(sPanel,   BorderLayout.SOUTH);
        movePanel.add(eMoveBtn, BorderLayout.EAST);
        movePanel.add(wMoveBtn, BorderLayout.WEST);
        movePanel.add(rMoveBtn, BorderLayout.CENTER);

        Border paneEdge = BorderFactory.createEmptyBorder(30,30,30,30);
        movePanel.setBorder(paneEdge);


        /**
         * Set play button panels.
         */
        playPanel  =  new JPanel();
        playPanel.add(saveBtn);
        playPanel.add(restartBtn);
        playPanel.add(movePanel);
        playPanel.add(uMoveBtn);
        playPanel.add(hMoveBtn);
    }

    /**
     * Refresh frame and clear output message
     *
     * @param command the user command string
     */
    private void refreshFrame(String command) {
        outMsg = command;
        getFrame();
        outMsg = "";
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
        String msg       =   "";
        String dirString =   dir.toString();

        if (!puzzle.canMove(dir)) {
            System.out.println(dirString + " is an invalid move.");
            msg = dirString + " is an invalid move.";
        } else {
            msg = "Moving " + dirString;
            System.out.println("Moving " + dirString);
            puzzle.move(dir);
            moves.add(dirString);
        }
        if (puzzle.onTarget()){
            System.out.println("Game Won!");
            msg = "Game Won!";
        }
        refreshFrame(msg);
    }

    /**
     * Undo last player move if move is E, W, N, S, P
     */
    private void undoMove() {
        String msg = null;
        String beforeUndo = puzzle.toString();
        try {
            puzzle.undo();
        } catch (Exception ex) {

        } finally {
            String afterUndo = puzzle.toString();

            if (beforeUndo.equalsIgnoreCase(afterUndo)){
                System.out.println("Cannot undo move.");
                msg = "Cannot undo move.";
            } else {
                System.out.println("Command '" + moves.get((moves.size()-1)) + "' undone.");
                msg = "Command '" + moves.get((moves.size()-1)) + "' undone.";
            }
            refreshFrame(msg);
        }
    }

    /**
     * Reset the puzzle state to the initial puzzle screen
     */
    private void clearPuzzle() {
        String msg  = "";
        try {
            puzzle.clear();
            System.out.println("\n\nResetting puzzle to initial state.");
            msg = "Resetting puzzle to initial state.";
        } catch (IllegalStateException ex) {
            System.out.println("No initial game state available!");
            msg = "No initial game state available!";
        }
        refreshFrame(msg);
    }

    /**
     * Save puzzle state to file
     */
    private void savePuzzle() {
        String msg  = "";
        if (puzzle == null) {
            System.out.println("No game available to save.");
            msg = "No game available to save.";
        } else if (puzzle.onTarget()){
            System.out.println("Cannot save completed game.");
            msg = "Cannot save completed game.";
        } else {
            DateTimeFormatter dtf   =  DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
            LocalDateTime     now   =  LocalDateTime.now();
            String filename = "puzzle-" + dtf.format(now);
            String filepath = workDir + "/snapshot/" + filename;
            String relFilepath = "./snapshot/" + filename;

            try (PrintStream out = new PrintStream(new FileOutputStream(filepath))) {
                out.print(puzzle.toString());
                System.out.println("Puzzle saved in: " + filepath);
                msg = "Puzzle saved in: \n" + relFilepath;
            } catch (FileNotFoundException e){
                throw new SokobanException("File not found" + e);
            }
        }
        refreshFrame(msg);
    }

    /**
     * 
     * Load saved puzzle from file
     * 
     */
    private void loadSavedPuzzle() {
        String msg  = "";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int fileChoice = fileChooser.showOpenDialog(appFrame);
        if(fileChoice == JFileChooser.APPROVE_OPTION){
            loadFile  =  fileChooser.getSelectedFile();
            setPuzzle();
            System.out.println("Loaded saved game from \n" + loadFile.getAbsolutePath() + "\n");
            msg = "Loaded saved game from " + loadFile.getAbsolutePath();
        } else{
            System.out.println("Unable to load saved game");
            msg = "Unable to load saved game";
        }
        refreshFrame(msg);
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
        JTextPane helpPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(helpPane);
        helpPane.setEditable(false);
        helpPane.setMargin( new Insets(10,10,10,10) );
        helpPane.setBackground(new Color(224, 255, 255));
        helpPane.setForeground(new Color(43, 27, 23, 255));
        helpPane.setFont(appFont);
        helpPane.setText(helpText);

        JOptionPane.showMessageDialog(new JFrame(), helpPane);
    }

    /**
     * Quit game
     * 
     */
    private void quitPuzzle() {
        System.out.println("Program shutting down.");
        String msg = "Program shutting down.";
        refreshFrame(msg);
        appFrame.setVisible(false);
        appFrame.dispose();
        System.exit(0);
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

    private Sokoban puzzle     = null;
    private Player  player     = null;
    private File    loadFile   = null;

    private JTextPane   outputPane     = null;
    private JPanel      boardPanel     = null;
    private JPanel      adminPanel     = null;
    private JPanel      playPanel      = null;
    private JPanel      footerPanel    = null;
    private JFrame      appFrame       = new JFrame("Sokoban Game");

    private String  outMsg          = "";
    private String  screenFile      = null;
    private String  screenPath      = null;

    private static Integer imgHeight     = 20;
    private static Integer imgWidth      = 20;
    private static Integer frameHeight   = 900;
    private static Integer frameWidth    = 900;
    private static Integer numGrid       = 30;
    private static Integer minScreen     = 1;
    private static Integer maxScreen     = 90;
    private static boolean traceOn       = true; // for debugging
    private static String  workDir       = System.getProperty("user.dir");
    private static Font    appFont       = new Font("Monospaced", Font.BOLD, 20);
    private ArrayList<String>  moves     = null;

    private static HashMap<String,ImageIcon> symbolMap  =  null;
    private static Map<String,String> commandMap  =  null;
}
