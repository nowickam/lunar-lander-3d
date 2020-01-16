import javax.swing.*;
import java.awt.*;

public class EndView extends JPanel implements Observer {
    private JLabel text;

    public EndView(){
        setVisible(false);
        setBackground(new Color(0,0,0));
        setSize(800,150);
        Font c150=new Font("Consolas", Font.PLAIN, 150);

        text=new JLabel();
        text.setFont(c150);
        text.setForeground(new Color(255,255,255));
        add(text,SwingConstants.CENTER);

    }

    @Override
    public void update(Object o, int type) {
        if(type==3){
            text.setText("YOU WON!");
            setVisible(true);
        }
        else if(type==4){
            text.setText("YOU LOST!");
            setVisible(true);
        }

    }

}