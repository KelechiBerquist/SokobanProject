package sokoban;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


/**
 * Class that creates and refreshes the app frame
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class AppFrame extends JFrame{
    /**
     * Default constructor to generate frame for game
     * @param frameName Title of frame
     * @param listener action listener for components of the frame
     */
    public AppFrame(String frameName, ActionListener listener){
        super(frameName);
        this.listener = listener;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(layout);
    }

    /**
     * Refrshes game after every player command
     * @param gameState array list containing current state of the game
     * @param msg feedback message for player
     */
    public void refreshFrame(ArrayList<String> gameState, String msg){
        this.getContentPane().removeAll();
        this.invalidate();
        this.revalidate();

        BoardPanel    boardPanel    =  new BoardPanel(gameState, font);
        PanelInPanel  commandPanel  =  this.getCommandPanel(msg);

        this.setLayout(layout);
        this.getContentPane().add(boardPanel, BorderLayout.CENTER);
        this.getContentPane().add(commandPanel, BorderLayout.NORTH);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Generates a panel for the commands in game
     * @param msg Feedback message to display to user
     * @return panel made up of commands panels
     */
    private PanelInPanel getCommandPanel(String msg) {
        TextPanePanel feedbackPane  =  new TextPanePanel(font, msg);
        AdminPanel    adminPanel    =  new AdminPanel(font, listener);
        JPanel        playPanel     =  new PanelInPanel(
            new JPanel[]{
                new SaveRestartPanel(font, listener),
                new MovePanel(font,   listener),
                new HelpUndoPanel(font,    listener)
            },
            new FlowLayout()
        );

        PanelInPanel commandPanel  =  new PanelInPanel(
            new JPanel[]{ adminPanel, playPanel, feedbackPane }
        );
        return commandPanel;
    }

    private ActionListener  listener;

    private static Font           font   =  new Font("Monospaced", Font.BOLD, 20);
    private static BorderLayout   layout =  new BorderLayout(50, 50);
}
