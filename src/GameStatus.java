import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class GameStatus {
    int currentRow;
    int currentCol;
    char[][] playGround;
    int score;

    GameStatus(int currentRow, int currentCol, char[][] playGround, int score) {
        this.currentRow = currentRow;
        this.currentCol = currentCol;
        this.playGround = playGround;
        this.score = score;
    }
}

