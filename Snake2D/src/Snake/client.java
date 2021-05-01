// Java implementation for a client
// Save file as Client.java
package Snake;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

// Client class
public class client {
    static boolean isStart = false;
    static GamePlay gamePlay;
    static String oldNAme;

    public static void main(String[] args) throws IOException, InterruptedException {


        ClientPanel c = new ClientPanel();
        while (true) {
            Thread.sleep(100);
            if (c.validIp) {
                System.out.println("Your IP and Port are valid");
                try {
                    // getting localhost ip
                    String ip = c.serverIPText.getText();
                    Integer port = Integer.parseInt(c.portText.getText());

                    // establish the connection with server port 5056
                    Socket s = new Socket(ip, port);


                    // obtaining input and out streams
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    dos.writeUTF(c.clientName);
                    dos.flush();

                    // the following loop performs the exchange of
                    // information between client and client handler


                    while (true) {
                        String receive = dis.readUTF();

                        // set other user name that connected after this client .
                        if (receive.contains("name")) {
                            StringTokenizer stringTok = new StringTokenizer(receive, "#");
                            String otherName = stringTok.nextToken();
                            // add in panel Client
                            c.setClient_1(otherName, "connected");

                        }

                        if (receive.equals("otherName")) {
                            ObjectInputStream objOtherName = new ObjectInputStream(dis);
                            ArrayList<String> otherName = (ArrayList<String>) objOtherName.readObject();
                            // set in client panel
                            for (String i : otherName) {
                                c.setClient_1(i, "connected");
                            }
                        }

                        if (receive.equals("Enjoy Playing Snake Game")) {
                            c.setVisibleLabelFalse(c.waitMsg);

                            ObjectInputStream objGame = new ObjectInputStream(dis);
                            HashMap gamePlayObj = (HashMap) objGame.readObject();

                            Snake snake = new Snake("client 1", new ArrayList<Integer>(Arrays.asList(1, 2, 3)), new ArrayList<Integer>(Arrays.asList(1)));

                            Food food_game = (Food) gamePlayObj.get("Food");

                            ArrayList<Snake> otherSnakeArray = (ArrayList<Snake>) gamePlayObj.get("other");

                            Snake mainSnake = (Snake) gamePlayObj.get("mainSnake");

                            oldNAme = mainSnake.name;

                            System.out.println(mainSnake.name);
                            gamePlay = new GamePlay((Barrier) gamePlayObj.get("Barrier"), mainSnake, food_game, otherSnakeArray.get(0));


                            JFrame obj = new JFrame();

                            obj.setBounds(10, 10, 905, 700);
                            obj.setBackground(Color.DARK_GRAY);
                            obj.setResizable(false);
                            obj.setVisible(true);
                            obj.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            obj.add(gamePlay);
                            isStart = true;
                            System.out.println(receive);

                        }
                        if (receive.equals("Data")) {
                            ObjectInputStream objData = new ObjectInputStream(dis);
                            HashMap DataHashMap = (HashMap) objData.readObject();
                            Food food = (Food) DataHashMap.get("Food");
                            Snake snake = (Snake) DataHashMap.get("Snake");
                            // set other Snake
                            for (int i = 0; i < gamePlay.otherSnake.length; i++) {
                                Snake otherSnake = gamePlay.otherSnake[i];
                                if (otherSnake.name.equals(snake.name)) {
                                    gamePlay.otherSnake[i] = snake;
                                }
                            }
                            // set food of Snake
                            if (food.FOOD_X.size() < gamePlay.food.FOOD_X.size()) {
                                gamePlay.setNewFood(food);
                            }
                        }
                        if (receive.equals("winner")) {
                            c.Winner.setVisible(true);
                            c.Winner.setText("You are winner");
                            gamePlay.isWinner = true;
                        }
                        if (receive.contains("finish")) {
                            if (!gamePlay.isWinner) {
                                StringTokenizer commandFinish = new StringTokenizer(receive, "#");
                                String winner = commandFinish.nextToken();
                                c.Winner.setVisible(true);
                                c.Winner.setText(winner + " is winner");
                            }
                            gamePlay.isFinish = true;
                        }
                        if (isStart) {
                            Thread.sleep(100);
                            Integer Score = gamePlay.getScore();
                            String Name = oldNAme + "#" + c.clientName;
                            HashMap Data = new HashMap() {{
                                put("Score", Score);
                                put("Food", gamePlay.food);
                                put("Snake", gamePlay.snake);
                                put("Name", Name);
                            }};
                            ObjectOutputStream objData = new ObjectOutputStream(dos);
                            objData.writeObject(Data);
                            objData.flush();
//                    dos.writeUTF(String.valueOf(Score));
                        }


                        if (receive.equals("Exit")) {
                            System.out.println("Closing this connection : " + s);
                            s.close();
                            System.out.println("Connection closed");
                            break;
                        }

                    }

                    // closing resources
                    dis.close();
                    dos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}