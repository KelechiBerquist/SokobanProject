package sokoban;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * Class that creates the move buttons panel for player
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class MovePanel extends JPanel {
    /**
     * Default constructor for class
     * @param font font to be applied to the components
     * @param listener listener for the components
     */
    public MovePanel(Font font, ActionListener listener){
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
        wantedButtons.add(new String[]{null, null});
        wantedButtons.add(new String[]{"N", "Move North"});
        wantedButtons.add(new String[]{null, null});
        wantedButtons.add(new String[]{"W", "Move West"});
        wantedButtons.add(new String[]{"P", "Random Move"});
        wantedButtons.add(new String[]{"E", "Move East"});
        wantedButtons.add(new String[]{null, null});
        wantedButtons.add(new String[]{"S", "Move South"});
        wantedButtons.add(new String[]{null, null});

        // Use a button factory to create and decorate buttons
        for (String[] btnParam : wantedButtons){
            JButton aBtn = new ButtonFactory(btnParam);
            aBtn.setFont(font);
            aBtn.setPreferredSize(new Dimension(50, 30));
            aBtn.addActionListener(listener);
           this.add(aBtn);
        }
        this.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
    }
}
