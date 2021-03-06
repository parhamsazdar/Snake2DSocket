// Java implementation of  Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java
package Snake;


// Java implementation of  Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java


import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server {

    static ServerPanel Panel;

    static Vector<ClientHandler> ar = new Vector<>();

    //    static GamePlay gamePlay= new GamePlay(new Barrier(),new Snake());
    static Barrier barrier = new Barrier();
    static Food food = new Food();

    // counter for clients
    static int i = 0;
    static boolean isStart = false;
    static boolean haveWinner = false;
    static Integer whenWin = 2;
    static Integer numCon = 2;

    //Create Snake By default name
    static Snake s1 = new Snake("client 0", new ArrayList<>(Arrays.asList(1, 2, 3)), new ArrayList<>(Arrays.asList(1)));
    static Snake s2 = new Snake("client 1", new ArrayList<>(Arrays.asList(1, 2, 3)), new ArrayList<>(Arrays.asList(1)));

    // vector of Default Snake
    static Vector<Snake> otherSnake = new Vector(Arrays.asList(s1, s2));

    // the Same object that we have to share with game play
    static HashMap gamePlayObj = new HashMap() {{
        put("Food", food);
        put("Barrier", barrier);
        put("other", new ArrayList<Snake>());
    }};

    // send alarm finish game for the looser client
    public static void finishGame(String winner) throws IOException {
        for (ClientHandler m : ar) {
            m.dos.writeUTF(winner + "#" + "finish");
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        // server is listening on port 8000
        ServerSocket ss = new ServerSocket(8000);

        // Gui server
        Panel = new ServerPanel();


        // running infinite loop for getting
        // client request
        while (true) {
            Socket s = null;

            try {
                // socket object to receive incoming client requests

                s = ss.accept();

                //remove wait msg from Panel
                Panel.waitMsg.setVisible(false);

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");
                //Name for the client
                String Name = "client " + i;
                String ip = "IP:" + s.getInetAddress() + " Port: " + s.getPort();
                Panel.checkCountClient(i, Name, ip, "connected");
                // create a new thread object
                ClientHandler client = new ClientHandler(s, Name, dis, dos);
                Thread t = new Thread(client);
                i++;
                ar.add(client);

                // Invoking the start() method
                t.start();

                // Broadcast to other user that I come .
                ArrayList<String> otherName = new ArrayList<>();

                Thread.sleep(100);
                for (int i = 0; i < ar.size() - 1; i++) {
                    ClientHandler otherClient = ar.get(i);
                    otherClient.dos.writeUTF(client.name + "#" + "name");
                    otherClient.dos.flush();

                    otherName.add(otherClient.name);
                }
                // catch other user data and send it to me
                if (otherName.size() > 0) {
                    client.dos.writeUTF("otherName");
                    client.dos.flush();

                    ObjectOutputStream objOther = new ObjectOutputStream(dos);
                    objOther.writeObject(otherName);
                    objOther.flush();
                }


                // check for num of connections and when break from the while
                if (ar.size() == numCon) {
                    System.out.println("Game is ready to start");
                    break;
                }


            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
        // send alarm that you can Start the Game
        int counter = 0;
        for (ClientHandler m : ar) {
            m.dos.writeUTF("Enjoy Playing Snake Game");
            m.dos.flush();
            // set msg in the Panel
            Panel.gameState.setVisible(true);

            Snake s = (Snake) otherSnake.get(counter);
            ArrayList<Snake> otherSnakeArray = (ArrayList<Snake>) gamePlayObj.get("other");


            // create other snake
            for (Snake snake : otherSnake) {
                if (!snake.name.equals(m.name)) {
                    otherSnakeArray.add(snake);
                } else {
                    gamePlayObj.put("mainSnake", s);
                }
            }

            // send objOther snake
            ObjectOutputStream objGame = new ObjectOutputStream(m.dos);
            objGame.writeObject(gamePlayObj);
            objGame.flush();
            // clear part
            otherSnakeArray.clear();
            gamePlayObj.remove("mainSnake");
            isStart = true;
            counter++;
        }
        // Who is Winner
        while (!haveWinner) {
            Thread.sleep(100);
            for (ClientHandler m : ar) {
                if (m.Score == whenWin) {
                    // Introduction of winner in Panel
                    Panel.Winner.setText(Panel.Winner.getText() + m.name);
                    Panel.Winner.setVisible(true);

                    System.out.println(m.name + "is Win");
                    haveWinner = true;
//                    m.dos.writeUTF("You Win");
                    m.dos.writeUTF("winner");
                    m.dos.flush();
                    finishGame(m.name);
                    break;
                }
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    String name;
    Integer Score = 0;
    Food food;

    // Constructor
    public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.name = name;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        ObjectInputStream objData;
        ObjectOutputStream objSendData;
        HashMap received;
        String receivedChangeName;

        try {
            // set this user name in server panel
            String Name = dis.readUTF();
            String previousName = this.name;
            String newName = Name;
            for (int i = 0; i < Server.Panel.poolLabel.size(); i++) {
                if (Server.Panel.poolLabel.get(i).getText().equals(previousName)) {
                    Server.Panel.poolLabel.get(i).setText(newName);
                    Server.ar.get(i).name = newName;
                    Server.otherSnake.get(i).name = newName;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true) {
            try {
                // set player Name
//                receivedChangeName = dis.readUTF();


                objData = new ObjectInputStream(dis);
                received = (HashMap) objData.readObject();
                if (!Server.isStart) {
                    // Ask user what he wants
                    dos.writeUTF("Wait until another player is going to be connect");
                    dos.flush();
                }

                // set Obj food that is same between other client for sharing this obj
                if (Integer.valueOf((Integer) received.get("Score")) >= 1) {
                    this.Score = Integer.valueOf((Integer) received.get("Score"));
                    food = (Food) received.get("Food");
                }


                // Send other client data
                for (ClientHandler m : Server.ar) {
                    if (!m.name.equals(this.name)) {
                        m.dos.writeUTF("Data");
                        m.dos.flush();
                        objSendData = new ObjectOutputStream(m.dos);
                        objSendData.writeObject(received);
                        objSendData.flush();
                    }
                }


                if (received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    Server.ar.remove(Server.ar.indexOf(this));
                    break;
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}