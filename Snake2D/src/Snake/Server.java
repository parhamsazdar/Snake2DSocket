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
    static Integer whenWin = 5;
    static Integer numCon = 2;
    static Snake s1 = new Snake("client 0", new ArrayList<Integer>(Arrays.asList(1, 2, 3)), new ArrayList<Integer>(Arrays.asList(1)));
    static Snake s2 = new Snake("client 1", new ArrayList<Integer>(Arrays.asList(1, 2, 3)), new ArrayList<Integer>(Arrays.asList(1)));

    static Vector<Snake> otherSnake = new Vector(Arrays.asList(s1, s2));

    static HashMap gamePlayObj = new HashMap() {{
        put("Food", food);
        put("Barrier", barrier);
        put("other", new ArrayList<Snake>());

    }};

    public static void finishGame() throws IOException {
        for (ClientHandler m : ar) {
            m.dos.writeUTF("finish");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(8000);

        Panel  = new ServerPanel();


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
                String Name = "client "+i;
                String ip = "IP:"+s.getInetAddress() +" Port: "+s.getPort();
                Panel.checkCountClient(i,Name,ip,"connected");
                // create a new thread object
                ClientHandler client = new ClientHandler(s, Name, dis, dos);
                Thread t = new Thread(client);
                i++;
                ar.add(client);

                // Invoking the start() method
                t.start();

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
                    Panel.Winner.setText(Panel.Winner.getText()+m.name);
                    Panel.Winner.setVisible(true);

                    System.out.println(m.name + "is Win");
                    haveWinner = true;
//                    m.dos.writeUTF("You Win");
                    m.dos.writeUTF("winner");
                    m.dos.flush();
                    finishGame();
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
        while (true) {
            try {
                objData = new ObjectInputStream(dis);
                if (!Server.isStart) {
                    // Ask user what he wants
                    dos.writeUTF("Wait until another player is going to be connect");
                    dos.flush();
                }
                // receive the answer from client
                received = (HashMap) objData.readObject();
                if (Integer.valueOf((Integer) received.get("Score")) >= 1) {
                    Score = Integer.valueOf((Integer) received.get("Score"));
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