package sokoban;

import java.awt.*;
import javax.swing.*;

/**
 * Creates buttons for app
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class ButtonFactory extends JButton {
    public ButtonFactory(String[] args){
        super(args[0]);
        this.setToolTipText(args[1]);
        this.setPreferredSize(new Dimension(50, 30));
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setBorder(null);
    }
}
