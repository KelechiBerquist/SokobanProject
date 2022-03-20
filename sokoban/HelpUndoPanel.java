package sokoban;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * Class that creates the help and undo panel
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class HelpUndoPanel extends JPanel {
    /**
     * Default constructor for class
     * @param font font to be applied to the components
     * @param listener listener for the components
     */
    public HelpUndoPanel(Font font, ActionListener listener){
        super();
        this.setLayout(new GridLayout(3,3));
        this.createButtons(font, listener);
    }

    /**
     * Creates the buttons within the panel
     * @param font font to be applied to the components
     * @param listener listener for the components
     */
    private void createButtons(Font font, ActionListener listener){
        ArrayList <String[]> wantedButtons = new ArrayList<String[]>();
        wantedButtons.add(new String[]{"Undo", "Undo Last Move"});
        wantedButtons.add(new String[]{"Help", "Get Help!"});

        // Use a button factory to create and decorate buttons
        for (String[] btnParam : wantedButtons){
            JButton aBtn = new ButtonFactory(btnParam);
            aBtn.setFont(font);
            aBtn.setPreferredSize(new Dimension(150, 30));
            aBtn.addActionListener(listener);
            this.add(aBtn);
        }
    }
}
