//processing 2
//program arguments - name of the PApplet class Run->Edit conf -> Program argument
//include 3 jars in the libraries RM on proj -> Open module sett -> Lib -> add core, gluegen, jogl

import javax.swing.*;
import java.awt.*;

public class App extends JFrame{
    App(){
        setDefaultCloseOperation(3);
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);
        setUndecorated(true);

        JLayeredPane pane=new JLayeredPane();
        this.setBackground(new Color(0,0,0));
        pane.setBackground(new Color(0,0,0));

        LunarModel lunarModel=new LunarModel();
        SpaceshipModel spaceshipModel=new SpaceshipModel(lunarModel);
        StatusView gameStatus=new StatusView();
        EndView endStatus=new EndView();
        LunarViewControl viewControl=new LunarViewControl(lunarModel,spaceshipModel,Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);

        viewControl.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);
        endStatus.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-400,Toolkit.getDefaultToolkit().getScreenSize().height/2-80);

        lunarModel.addObserver(gameStatus);
        lunarModel.addObserver(endStatus);

        pane.add(endStatus);
        pane.add(gameStatus);
        pane.add(viewControl);
        add(pane);

        viewControl.init();

        setVisible(true);
        setDefaultCloseOperation(3);

    }

    public static void main(String[] args){
        new App();
    }
}
