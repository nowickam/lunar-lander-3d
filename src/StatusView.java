import javax.swing.*;
import java.awt.*;


public class StatusView extends JPanel implements Observer {
    private JLabel fuel,distance;

    public StatusView(){
        setSize(200,80);
        setLayout(new GridLayout(2,2));
        Font c14=new Font("Consolas", Font.PLAIN, 15);

        JLabel fuelLabel=new JLabel("FUEL: ");
        fuel=new JLabel();
        fuelLabel.setFont(c14);
        fuel.setFont(c14);
        fuelLabel.setForeground(Color.white);
        fuel.setForeground(Color.white);

        JLabel distLabel=new JLabel("DISTANCE: ");
        distance=new JLabel();
        distLabel.setFont(c14);
        distance.setFont(c14);
        distLabel.setForeground(Color.white);
        distance.setForeground(Color.white);

        setBackground(new Color(0,0,0,255));
        this.add(fuelLabel);
        this.add(fuel);
        this.add(distLabel);
        this.add(distance);
        setVisible(true);
    }

    @Override
    public void update(Object o, int type) {
        if(type==0)
            fuel.setText(String.format("%.2f",((LunarModel)o).getFuel()));
        else if(type==1)
            distance.setText(String.format("%.2f",((LunarModel)o).getDistance()));
    }
}
