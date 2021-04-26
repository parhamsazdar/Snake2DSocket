package Snake;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        JFrame obj = new JFrame();
        Barrier barrier = new Barrier();
        Snake snake = new Snake("parham",new ArrayList<Integer>(Arrays.asList(1, 2, 3)), new ArrayList<Integer>(Arrays.asList(1)));
        Snake s = new Snake("Armin",new ArrayList<Integer>(Arrays.asList(7, 8, 9)), new ArrayList<Integer>(Arrays.asList(2)));
        Snake s2 = new Snake("Ali Reza",new ArrayList<Integer>(Arrays.asList(15, 16, 17)), new ArrayList<Integer>(Arrays.asList(10)));



        Food food = new Food();
        GamePlay gamePlay = new GamePlay(barrier, snake, food, s,s2);

        obj.setBounds(10, 10, 905, 700);
        obj.setBackground(Color.DARK_GRAY);
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);

    }
}
