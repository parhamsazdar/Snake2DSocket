package Snake;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

class ServerPanel
{

    static JLabel ip = new JLabel("IP");
    static JLabel Name = new JLabel("Name");
    static JLabel State = new JLabel("State");


    // client 1
    public JLabel name_1 = new JLabel();
    public JLabel ip_1  = new JLabel();
    public JLabel connected_1 = new JLabel();

    // client 2
    public JLabel name_2 = new JLabel();
    public JLabel ip_2  = new JLabel();
    public JLabel connected_2 = new JLabel();

    // Game is ready
    public JLabel gameState = new JLabel("Game is ready for client");

    // Introduction Winner
    public JLabel Winner = new JLabel("Winner is : ");

    // Wait message
    static JLabel waitMsg = new JLabel("Server is waiting for client . . .");


    public ArrayList<JLabel> poolLabel = new ArrayList<>(Arrays.asList(
            name_1,
            name_2
    ));

    public static void InitializedPanel(){

        Name.setBounds(50,20,100,50);
        ip.setBounds(150,20,100,50);
        State.setBounds(350,20,100,50);
        waitMsg.setBounds(200,150,250,100);
    }

    public void checkCountClient(int i,String Name,String ip,String state){
        switch (i){
            case 0 :
                System.out.println("Yes");
                this.setClient_1(Name,ip,state);
                break;
            case 1 :
                this.setClient_2(Name,ip,state);
                break;
        }
    }



    public ServerPanel(){

        // Set Header of table
        InitializedPanel();
        addComponent(f,ip,Name,State);






        // client 1
        name_1.setBounds(50,50,100,50);

        ip_1.setBounds(150,50,250,50);

        connected_1.setBounds(350,50,80,50);

        // client 2
        name_2.setBounds(50,80,100,50);

        ip_2.setBounds(150,80,250,50);

        connected_2.setBounds(350,80,80,50);

        addComponent(f,name_1,ip_1,connected_1,name_2,ip_2,connected_2);

        //Game State
        gameState.setBounds(200,150,250,100);
        gameState.setVisible(false);
        f.add(gameState);

        // Winner of the Game
        Winner.setBounds(200,200,250,100);
        Winner.setVisible(false);
        f.add(Winner);

        //add wait msg
        f.add(waitMsg);

        f.setSize(700,500);
        f.setLayout(null);
        f.setVisible(true);
        f.setResizable(false);

    }

    public void setClient_1(String Name , String ip, String state){
        name_1.setText(Name);
        ip_1.setText(ip);
        connected_1.setText(state);
    }

    public void setClient_2(String Name , String ip, String state){
        name_2.setText(Name);
        ip_2.setText(ip);
        connected_2.setText(state);
    }

    public static void addComponent(JFrame f,JLabel ...J){
        for (JLabel j : J){
            f.add(j);
        }
    }

    static  JFrame f= new JFrame("Server Panel");

    public static void main(String args[]) throws InterruptedException {
        ServerPanel server=new ServerPanel();
        server.setClient_1("parham","javaPoint.com","connected");
        server.setClient_2("Ali","youtube.com","connected");
//        for (int i = 0 ; i<5;i++){
//            Thread.sleep(1000);
//            ServerPanel.name_1.setText(String.valueOf(i));
//
//        }
//        Thread.sleep(2000);
//        server.gameState.setVisible(true);


    }
}