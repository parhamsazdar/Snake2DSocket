package Snake;


import javax.lang.model.element.Name;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;


class ClientPanel {

    public JFrame f = new JFrame("Client Panel");
    public boolean validIp = false;
    public ClientPanel c = this;
    public JLabel serverIP = new JLabel("Server IP :");
    public JLabel port = new JLabel("Port : ");
    public JButton connect = new JButton("Connect");
    public JLabel state = new JLabel("Connecting ...");
    public JLabel name = new JLabel("Name");
    public JTextField serverIPText = new JTextField("127.0.0.1");
    public JTextField portText = new JTextField("8000");
    public JTextField nameText = new JTextField();

    public String clientName;

    public String ip_text;
    public Integer port_text;


    public JLabel clientNameLabel = new JLabel();

    // client 1
    public JLabel name_1 = new JLabel();
    public JLabel ip_1 = new JLabel();
    public JLabel connected_1 = new JLabel();

    // client 2
    public JLabel name_2 = new JLabel();
    public JLabel ip_2 = new JLabel();
    public JLabel connected_2 = new JLabel();

    // Game is ready
    public JLabel gameState = new JLabel("Game is ready for client");

    // Introduction Winner
    public JLabel Winner = new JLabel("Winner is : ");

    // Wait message
    public JLabel waitMsg = new JLabel("please Wait for other client . . .");

    // client Jlabel if wi or lose
    public JLabel youAreWinner = new JLabel("You are Winner");
    public JLabel youAreLoser = new JLabel("You lose the game");


    public static void InitializedPanel() {
        // setBounds Default


        // set Visible false


    }


    public void setVisibleComponentInitial() {
        serverIPText.setVisible(false);
        portText.setVisible(false);
        connect.setVisible(false);
    }


    public void checkCountClient(int i, String Name, String ip, String state) {


    }


    public ClientPanel() {
        name.setBounds(50, 50, 200, 30);
        nameText.setBounds(150, 50, 200, 30);
        serverIP.setBounds(50, 100, 200, 30);
        serverIPText.setBounds(150, 100, 200, 30);
        port.setBounds(50, 150, 200, 30);
        portText.setBounds(150, 150, 200, 30);
        connect.setBounds(50, 200, 100, 30);
        state.setBounds(200, 200, 400, 30);
        waitMsg.setBounds(100, 280, 300, 30);
        clientNameLabel.setBounds(100, 250, 300, 30);
//        youAreWinner.setBounds();

        f.add(name);
        f.add(nameText);
        f.add(serverIP);
        f.add(serverIPText);
        f.add(port);
        f.add(portText);
        f.add(connect);
        f.add(state);
        f.add(clientNameLabel);
        f.add(youAreWinner);
        f.add(youAreLoser);

        state.setVisible(false);
        waitMsg.setVisible(false);
        clientNameLabel.setVisible(false);


        //Game State
        gameState.setBounds(200, 150, 250, 100);
        gameState.setVisible(false);
        f.add(gameState);

        // Winner of the Game
        Winner.setBounds(200, 200, 250, 100);
        Winner.setVisible(false);
        f.add(Winner);

        //add wait msg
        f.add(waitMsg);

        f.setSize(700, 500);
        f.setLayout(null);
        f.setVisible(true);
        f.setResizable(false);


        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                state.setVisible(true);
                state.setText("Connecting ...");

                InetAddress address = null;
                Integer port;
                boolean reachable = false;
                try {
                    address = InetAddress.getByName(serverIPText.getText());
                    reachable = address.isReachable(Integer.valueOf(portText.getText()));
                } catch (Exception exception) {
                    state.setText("Invalid ip or port");
                }
                if (reachable) {
                    validIp = true;
                    ip_text = serverIPText.getText();
                    port_text = Integer.valueOf(portText.getText());

                    //set client Name
                    clientName = nameText.getText();
                    clientNameLabel.setText("Welcome " + clientName);


                    state.setText("Connected");

                    clientNameLabel.setVisible(true);
                    waitMsg.setVisible(true);

                    serverIPText.setEnabled(false);
                    portText.setEnabled(false);
                    connect.setEnabled(false);
                    nameText.setEnabled(false);

                }


            }
        });


    }

    public void setVisibleLabelFalse(JLabel ...J){
        for (JLabel j : J){
            j.setVisible(false);
        }
    }


    public void setClient_1(String Name, String ip, String state) {
        name_1.setText(Name);
        ip_1.setText(ip);
        connected_1.setText(state);
    }

    public void setClient_2(String Name, String ip, String state) {
        name_2.setText(Name);
        ip_2.setText(ip);
        connected_2.setText(state);
    }

    public static void addComponent(JFrame f, JLabel... J) {
        for (JLabel j : J) {
            f.add(j);
        }
    }


    public static void main(String args[]) throws InterruptedException {
        ClientPanel client = new ClientPanel();


    }
}