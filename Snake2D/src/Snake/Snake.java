package Snake;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Snake implements Serializable {
    public int[] Snake_X_Length = new int[750];
    public int[] Snake_Y_Length = new int[750];

    boolean up = false;
    boolean down = false;
    boolean right = false;
    boolean left = false;

    public ImageIcon RightMouth;
    public ImageIcon LeftMouth;
    public ImageIcon DownMouth;
    public ImageIcon upMouth;
    public ImageIcon SnakeImage;

    public String name;

    int Score = 0;
    int SnakeBodyLength = 25;
    int LengthOfSnake = 3;

    int move = 0;

    ArrayList<Integer> Initialize_X_Index;
    ArrayList<Integer> Initialize_Y_Index;

    public Snake(String name,ArrayList<Integer> Initialize_X_Index, ArrayList<Integer> Initialize_Y_Index) {
        this.name = name;
        this.Initialize_X_Index = Initialize_X_Index;
        this.Initialize_Y_Index = Initialize_Y_Index;
    }

    public void MoveRight() {

        for (int r = this.LengthOfSnake - 1; r >= 0; r--) {
            this.Snake_Y_Length[r + 1] = this.Snake_Y_Length[r];
        }
        for (int r = this.LengthOfSnake; r >= 0; r--) {
            if (r == 0) {
                this.Snake_X_Length[r] += SnakeBodyLength;
            } else {
                this.Snake_X_Length[r] = this.Snake_X_Length[r - 1];
            }
            if (this.Snake_X_Length[r] > GamePlay.MAX_WIDTH_LOC) {
                this.Snake_X_Length[r] = GamePlay.MIN_WIDTH_LOC;
            }
        }

    }


    public void MoveLeft() {
        for (int r = this.LengthOfSnake - 1; r >= 0; r--) {
            this.Snake_Y_Length[r + 1] = this.Snake_Y_Length[r];
        }
        for (int r = this.LengthOfSnake; r >= 0; r--) {
            if (r == 0) {
                this.Snake_X_Length[r] -= SnakeBodyLength;
            } else {
                this.Snake_X_Length[r] = this.Snake_X_Length[r - 1];
            }
            if (this.Snake_X_Length[r] < GamePlay.MIN_WIDTH_LOC) {
                this.Snake_X_Length[r] = GamePlay.MAX_WIDTH_LOC;
            }
        }
    }

    public void MoveUp() {
        for (int r = this.LengthOfSnake - 1; r >= 0; r--) {
            this.Snake_X_Length[r + 1] = this.Snake_X_Length[r];
        }
        for (int r = this.LengthOfSnake; r >= 0; r--) {
            if (r == 0) {
                this.Snake_Y_Length[r] -= this.SnakeBodyLength;
            } else {
                this.Snake_Y_Length[r] = this.Snake_Y_Length[r - 1];
            }
            if (this.Snake_Y_Length[r] < GamePlay.MAX_HEIGHT_LOC) {
                this.Snake_Y_Length[r] = GamePlay.MIN_HEIGHT_LOC;
            }
        }
    }

    public void MoveDown() {
        for (int r = this.LengthOfSnake - 1; r >= 0; r--) {
            this.Snake_X_Length[r + 1] = this.Snake_X_Length[r];
        }
        for (int r = this.LengthOfSnake; r >= 0; r--) {
            if (r == 0) {
                this.Snake_Y_Length[r] += SnakeBodyLength;
            } else {
                this.Snake_Y_Length[r] = this.Snake_Y_Length[r - 1];
            }
            if (this.Snake_Y_Length[r] > GamePlay.MIN_HEIGHT_LOC) {
                this.Snake_Y_Length[r] = GamePlay.MAX_HEIGHT_LOC;
            }
        }
    }


}
