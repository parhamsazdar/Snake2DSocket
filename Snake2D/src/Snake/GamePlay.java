package Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class GamePlay extends JPanel implements KeyListener, ActionListener, Serializable {

    static int MIN_HEIGHT_LOC = 625;
    static int MAX_HEIGHT_LOC = 75;

    static int MIN_WIDTH_LOC = 25;
    static int MAX_WIDTH_LOC = 850;

    static boolean isFinish = false;
    static boolean isWinner = false;
    static int[] FOOD_X = {25, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625, 650,
            675, 700, 725, 750, 775, 800, 825, 850};
    static int[] FOOD_Y = {75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625};

    static ArrayList<Integer> Initialize_X_Index = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
    static ArrayList<Integer> Initialize_Y_Index = new ArrayList<>(Arrays.asList(1));

    static int[] BARRIER_X = FOOD_X.clone();
    static int[] BARRIER_Y = FOOD_Y.clone();


    private Random random = new Random();
    private int X_POS_FOOD;
    private int Y_POS_FOOD;


    Barrier barrier;
    Snake snake;
    Food food;
    Snake[] otherSnake;

    private ImageIcon FoodIcon = new ImageIcon(getClass().getResource("../enemy.png"));
    private ImageIcon Title = new ImageIcon(getClass().getResource("../snaketitle.jpg"));


    private Timer timer;
    private int Delay = 100;


    public GamePlay(Barrier barrier, Snake snake, Food food, Snake... otherSnake) {
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(Delay, this);
        timer.start();
        this.barrier = barrier;
        this.snake = snake;
        this.food = food;
        this.otherSnake = otherSnake;
        this.food.generateFoodPos(this.barrier.RandomXIndexBarrier, this.barrier.RandomYIndexBarrier, Initialize_X_Index, Initialize_Y_Index);
        X_POS_FOOD = food.returnXFoodPos();
        Y_POS_FOOD = food.returnYFoodPos();
    }

    public void setDefaultDirection(Graphics g) {
        this.snake.RightMouth = new ImageIcon(getClass().getResource("../rightmouth.png"));
        this.snake.RightMouth.paintIcon(this, g, this.snake.Snake_X_Length[0], this.snake.Snake_Y_Length[0]);
    }

    public void setDefaultDirectionOtherSnake(Graphics g) {

        for (Snake s : this.otherSnake) {
            s.RightMouth = new ImageIcon(getClass().getResource("../rightmouth.png"));
            s.RightMouth.paintIcon(this, g, s.Snake_X_Length[0], s.Snake_Y_Length[0]);
        }
    }

    public void setNewFood(Food food) {
        this.food = food;
        X_POS_FOOD = food.X_POS_FOOD;
        Y_POS_FOOD = food.Y_POS_FOOD;
    }


    public void InitializeSnakeDefaultLength(Snake snake) {
        snake.Snake_X_Length[2] = FOOD_X[snake.Initialize_X_Index.get(0)];
        snake.Snake_X_Length[1] = FOOD_X[snake.Initialize_X_Index.get(1)];
        snake.Snake_X_Length[0] = FOOD_X[snake.Initialize_X_Index.get(2)];

        snake.Snake_Y_Length[2] = FOOD_Y[snake.Initialize_Y_Index.get(0)];
        snake.Snake_Y_Length[1] = FOOD_Y[snake.Initialize_Y_Index.get(0)];
        snake.Snake_Y_Length[0] = FOOD_Y[snake.Initialize_Y_Index.get(0)];

    }


    public void FinalizedGame() {
        this.snake.right = false;
        this.snake.left = false;
        this.snake.up = false;
        this.snake.down = false;
        timer.setDelay(Delay);
    }

    public void isEatFood() {

        if (this.snake.Snake_X_Length[0] == FOOD_X[X_POS_FOOD] && this.snake.Snake_Y_Length[0] == FOOD_Y[Y_POS_FOOD]) {
            this.snake.LengthOfSnake++;
//            X_POS_FOOD = random.nextInt(FOOD_X.length);
//            Y_POS_FOOD = random.nextInt(FOOD_Y.length);
            timer.setDelay(timer.getDelay() - 5);
            this.snake.Score++;
            if (food.FOOD_X.size() >= 1) {
                X_POS_FOOD = this.food.returnXFoodPos();
                Y_POS_FOOD = this.food.returnYFoodPos();

            } else {
                isFinish = true;
            }
        }
    }

    public int getScore() {
        return this.snake.Score;
    }

    public void isCollisionToBarrier(Graphics g) {
        java.util.List<Integer> MapRandom_X = barrier.RandomXIndexBarrier.stream().map(number -> BARRIER_X[number]).collect(Collectors.toList());
        java.util.List<Integer> MapRandom_Y = barrier.RandomYIndexBarrier.stream().map(number -> BARRIER_Y[number]).collect(Collectors.toList());
        for (int i = 0; i < MapRandom_X.size(); i++) {
            if (MapRandom_X.get(i).equals(this.snake.Snake_X_Length[0]) && MapRandom_Y.get(i).equals(this.snake.Snake_Y_Length[0])) {
                FinalizedGame();
                g.setColor(Color.WHITE);
                g.setFont(new Font("arial", Font.BOLD, 50));
                g.drawString("Game Over", 300, 300);

                g.setFont(new Font("arial", Font.BOLD, 20));
                g.drawString("Press Enter To Restart", 350, 350);
            }
        }
    }

    public void isCollision(Graphics g) {
        for (int b = 1; b < this.snake.LengthOfSnake; b++) {
            if (this.snake.Snake_X_Length[0] == this.snake.Snake_X_Length[b] && this.snake.Snake_Y_Length[0] == this.snake.Snake_Y_Length[b]) {

                FinalizedGame();
                g.setColor(Color.WHITE);
                g.setFont(new Font("arial", Font.BOLD, 50));
                g.drawString("Game Over", 300, 300);

                g.setFont(new Font("arial", Font.BOLD, 20));
                g.drawString("Press Enter To Restart", 350, 350);
            }
        }
    }

    public void RestartGame() {
        FinalizedGame();
        this.snake.Score = 0;
        isFinish = false;
        isWinner = false;
        this.snake.move = 0;
        this.snake.LengthOfSnake = 3;
        this.barrier.ResetBarrierLocation();
        this.food.clearFOOD_X_FOOD_Y();
        this.food.generateFoodPos(this.barrier.RandomXIndexBarrier, this.barrier.RandomYIndexBarrier, Initialize_X_Index, Initialize_Y_Index);
        repaint();
    }


    public void IntroduceWinner(Graphics g) {
        FinalizedGame();
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("Game Finish", 300, 300);

        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Press Enter To Restart", 350, 350);
    }

    public void isWinnerOfGame(Graphics g) {
        FinalizedGame();
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("You are winner", 300, 300);

        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Press Enter To Restart", 350, 350);
    }


    public void paint(Graphics g) {
        requestFocus(true);
        // Draw a title image border
        g.setColor(Color.WHITE);
        g.drawRect(24, 10, 851, 55);

        // Fill the image icon of title
        Title.paintIcon(this, g, 25, 11);

        // draw play area
        g.setColor(Color.WHITE);
        g.drawRect(24, 74, 851, 577);

        // draw background for the gameplay
        g.setColor(Color.black);
        g.fillRect(25, 75, 850, 575);

        // set default direction of snake
//        InitializeSnakeDefaultLength(this.otherSnake[0]);


        for (int a = 0; a < this.snake.LengthOfSnake; a++) {
            if (this.snake.move == 0) {
                // Check Food location is located correctly
                InitializeSnakeDefaultLength(this.snake);
                setDefaultDirection(g);

            }


            if (a == 0 && this.snake.right) {
                this.snake.RightMouth = new ImageIcon(getClass().getResource("../rightmouth.png"));
                this.snake.RightMouth.paintIcon(this, g, this.snake.Snake_X_Length[a], this.snake.Snake_Y_Length[a]);
            } else if (a == 0 && this.snake.left) {
                this.snake.LeftMouth = new ImageIcon(getClass().getResource("../leftmouth.png"));
                this.snake.LeftMouth.paintIcon(this, g, this.snake.Snake_X_Length[a], this.snake.Snake_Y_Length[a]);
            } else if (a == 0 && this.snake.up) {
                this.snake.upMouth = new ImageIcon(getClass().getResource("../upmouth.png"));
                this.snake.upMouth.paintIcon(this, g, this.snake.Snake_X_Length[a], this.snake.Snake_Y_Length[a]);
            } else if (a == 0 && this.snake.down) {
                this.snake.DownMouth = new ImageIcon(getClass().getResource("../downmouth.png"));
                this.snake.DownMouth.paintIcon(this, g, this.snake.Snake_X_Length[a], this.snake.Snake_Y_Length[a]);
            } else if (a != 0) {
                this.snake.SnakeImage = new ImageIcon(getClass().getResource("../snakeimage.png"));
                this.snake.SnakeImage.paintIcon(this, g, this.snake.Snake_X_Length[a], this.snake.Snake_Y_Length[a]);
            }
        }


        for (Snake s : this.otherSnake) {
            for (int a = 0; a < s.LengthOfSnake; a++) {
                if (s.move == 0) {
                    InitializeSnakeDefaultLength(s);
                    setDefaultDirectionOtherSnake(g);
                }

                if (a == 0 && s.right) {
                    s.RightMouth = new ImageIcon(getClass().getResource("../rightmouth.png"));
                    s.RightMouth.paintIcon(this, g, s.Snake_X_Length[a], s.Snake_Y_Length[a]);
                } else if (a == 0 && s.left) {
                    s.LeftMouth = new ImageIcon(getClass().getResource("../leftmouth.png"));
                    s.LeftMouth.paintIcon(this, g, s.Snake_X_Length[a], s.Snake_Y_Length[a]);
                } else if (a == 0 && s.up) {
                    s.upMouth = new ImageIcon(getClass().getResource("../upmouth.png"));
                    s.upMouth.paintIcon(this, g, s.Snake_X_Length[a], s.Snake_Y_Length[a]);
                } else if (a == 0 && s.down) {
                    s.DownMouth = new ImageIcon(getClass().getResource("../downmouth.png"));
                    s.DownMouth.paintIcon(this, g, s.Snake_X_Length[a], s.Snake_Y_Length[a]);
                } else if (a != 0) {
                    s.SnakeImage = new ImageIcon(getClass().getResource("../snakeimage.png"));
                    s.SnakeImage.paintIcon(this, g, s.Snake_X_Length[a], s.Snake_Y_Length[a]);
                }
            }

        }


        // This method is for Game over
        isEatFood();
        // Check if food location is valid
        FoodIcon.paintIcon(this, g, FOOD_X[this.X_POS_FOOD], FOOD_Y[this.Y_POS_FOOD]);


        // paint barrier
        for (int i = 0; i < barrier.RandomXIndexBarrier.size(); i++) {
            ImageIcon Test = new ImageIcon(getClass().getResource("../barrier_2.jpg"));
            Test.paintIcon(this, g, BARRIER_X[barrier.RandomXIndexBarrier.get(i)],
                    BARRIER_Y[barrier.RandomYIndexBarrier.get(i)]);
        }
        // check for collision to it self
        isCollision(g);
        // check for collision to barrier
        isCollisionToBarrier(g);
        //
        if (isWinner) {
            isWinnerOfGame(g);
        }
        if (isFinish && !isWinner) {
            IntroduceWinner(g);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (this.snake.right) {
            this.snake.MoveRight();
            repaint();
        }
        if (this.snake.left) {
            this.snake.MoveLeft();
            repaint();
        }
        if (this.snake.up) {
            this.snake.MoveUp();
            repaint();
        }
        if (this.snake.down) {
            this.snake.MoveDown();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            RestartGame();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.snake.move++;
            this.snake.right = (!this.snake.left) ? true : false;
            this.snake.up = false;
            this.snake.down = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.snake.move++;
            this.snake.left = (!this.snake.right) ? true : false;
            this.snake.up = false;
            this.snake.down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            this.snake.move++;
            this.snake.up = (!this.snake.down) ? true : false;
            this.snake.right = false;
            this.snake.left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.snake.move++;
            this.snake.down = (!this.snake.up) ? true : false;
            this.snake.right = false;
            this.snake.left = false;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
