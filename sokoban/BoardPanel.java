package sokoban;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

/**
 * Constructs a panel for the game board
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class BoardPanel extends JPanel {
    /**
     * Default constructor for game board panel
     * @param currGameState array list of board state
     * @param font font for displaying conmponents
     */
    public BoardPanel (ArrayList<String> currGameState, Font font){
        this.setSymbolMap();
        this.setSize(frameHeight-10, frameWidth-10);
        this.setVisible(true);
        this.setLayout(new GridLayout(numGrid, numGrid));
        this.gameState = currGameState;

        Border paneEdge = BorderFactory.createEmptyBorder(50,100,0,0);
        this.setBorder(paneEdge);
        this. createBoard();
    }

    /**
     * Maps symbol to image displayed on the board
     */
    private void setSymbolMap(){
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
    }

    /**
     * Creates panel for display of game components
     */
    private void createBoard(){
        for (Integer i = 0; i < numGrid; i++){
            Integer  countBox = 0;
            String   row      = null;
            if ( i < this.gameState.size()){
                row = this.gameState.get(i);
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
                this.add(button);
            }

        }

    }


    private ArrayList<String> gameState;
    private static String  workDir       = System.getProperty("user.dir");
    private static Integer imgHeight     = 20;
    private static Integer imgWidth      = 20;
    private static Integer frameHeight   = 900;
    private static Integer frameWidth    = 900;
    private static Integer numGrid       = 30;
    private static HashMap<String,ImageIcon> symbolMap  =  null;
}
