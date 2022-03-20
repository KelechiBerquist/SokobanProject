package sokoban;

import java.awt.*;
import javax.swing.*;

/**
 * Creates a panel for text pane output
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class TextPanePanel extends JPanel{
    /**
     * Constructor for feedback panel
     * @param font font to apply to components within the panel
     * @param msg message to display within the text pane
     */
    public TextPanePanel (Font font, String msg){
        this.msg = msg;
        this.font = font;
        this.createTextPane();
    }

    /**
     * Creates the text pane within this panel
     */
    private void createTextPane() {
        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        textPane.setEditable(false);
        if (!(this.msg).equals("")){
            textPane.setMargin( new Insets(10,10,10,10) );
            textPane.setBackground(new Color(224, 255, 255));
            textPane.setForeground(new Color(43, 27, 23, 255));
            textPane.setFont(this.font);
            textPane.setText(this.msg);
        } else {
            textPane.setMargin( new Insets(10,10,10,10) );
            textPane.setBackground(new Color(224, 255, 255));
            textPane.setForeground(this.getBackground());
        }
        this.add(textPane);
    }

    /**
     * Get the message within the text pane
     * @return message within the text pane
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * Sets the message within the text pane
     * @param msg message to be displayed in the text pane
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    private Font       font       =  null;
    private String     msg        =  "";
    private JTextPane  textPane   =  null;
}
