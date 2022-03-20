package sokoban;

import java.awt.*;
import javax.swing.*;

/**
 * Class that creates the panels within panel
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class PanelInPanel extends JPanel {
    /**
     * Default constructor for class with border layout
     * @param font font to be applied to the components
     * @param listener listener for the components
     */
    public PanelInPanel(JPanel[] innerPanels){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        for (JPanel panel : innerPanels){
            this.add(panel);
        }
    }

    /**
     * Constructor for panel in panel with flow layout
     * @param font font to be applied to the components
     * @param listener listener for the components
     * @param layout defined flow layout for container panel
     */
    public PanelInPanel(JPanel[] innerPanels, FlowLayout layout){
        super();
        this.setLayout(layout);
        for (JPanel panel : innerPanels){
            this.add(panel);
        }
    }
}
