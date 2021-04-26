package Snake;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class Food implements Serializable {
    Vector<Integer> FOOD_X = new Vector<>();
    Vector<Integer> FOOD_Y = new Vector<>();

    Random random = new Random();

    static int[] FOOD_X_POSSIBLE = {25, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625, 650,
            675, 700, 725, 750, 775, 800, 825, 850};
    static int[] FOOD_Y_POSSIBLE = {75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625};

    public int X_POS_FOOD;
    public int Y_POS_FOOD;

    static int NUMBER_FOOD = 10;

    public Food() {

    }


    public boolean isValidLocationForFood_1(int x, int y, ArrayList<Integer> barrier_x, ArrayList<Integer> barrier_y) {
        int Index_x = barrier_x.indexOf(x);
        int Index_y = barrier_y.indexOf(y);
        if (Index_x == Index_y && Index_x != -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidLocationForFood_2(int x, int y, ArrayList<Integer> SnakeDefault_x, ArrayList<Integer> SnakeDefault_y) {
        if (SnakeDefault_x.contains(x) && y == 1) {
            return false;
        } else {
            return true;
        }
    }


    public void generateFoodPos(ArrayList<Integer> barrier_x, ArrayList<Integer> barrier_y, ArrayList<Integer> SnakeDefault_x, ArrayList<Integer> SnakeDefault_y) {
        for (int i = 0; i < NUMBER_FOOD; i++) {
            while (true) {
                X_POS_FOOD = random.nextInt(FOOD_X_POSSIBLE.length);
                Y_POS_FOOD = random.nextInt(FOOD_Y_POSSIBLE.length);
                if (isValidLocationForFood_1(X_POS_FOOD, Y_POS_FOOD, barrier_x, barrier_y) && isValidLocationForFood_2(X_POS_FOOD, Y_POS_FOOD, SnakeDefault_x, SnakeDefault_y)) {
                    this.FOOD_X.add(X_POS_FOOD);
                    this.FOOD_Y.add(Y_POS_FOOD);
                    break;
                }
            }
        }
    }

    public int returnXFoodPos() {
        int x = FOOD_X.get(FOOD_X.size() - 1);
        FOOD_X.remove(FOOD_X.size() - 1);
        this.X_POS_FOOD = x;
        return x;
    }

    public int returnYFoodPos() {
        int Y = FOOD_Y.get(FOOD_Y.size() - 1);
        FOOD_X.removeElement(FOOD_Y.size() - 1);
        this.Y_POS_FOOD = Y;
        return Y;
    }

    public void clearFOOD_X_FOOD_Y() {
        FOOD_X.clear();
        FOOD_Y.clear();
    }

    public static void main(String[] args) {
        ArrayList<Integer> a = new ArrayList<>();
        ArrayList<Integer> b = new ArrayList<>();
        ArrayList<Integer> c = new ArrayList<>();
        ArrayList<Integer> d = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            a.add(i);
            b.add(i);
            c.add(i);
            d.add(i);
        }
        HashMap<String, String> h = new HashMap<>();
        h.get("parham");


//        Food food = new Food();
//        System.out.println(a);
//        System.out.println(b);
//        food.generateFoodPos(a, b, c, d);
//        System.out.println(food.FOOD_X);
//        System.out.println(food.FOOD_Y);
//
//        System.out.println(food.returnXFoodPos());
//        System.out.println(food.returnXFoodPos());
//        System.out.println(food.returnXFoodPos());
//
//        System.out.println(food.FOOD_X);
//
        System.out.println(a.indexOf(10));


    }
}
