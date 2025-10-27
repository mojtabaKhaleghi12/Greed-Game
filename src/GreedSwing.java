import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GreedSwing extends JFrame implements KeyListener {
    private  int rows;
    private  int cols;
    private GameStatus gameStatus;
    private GamePanel gamePanel;

    public GreedSwing(int width) {
        this.rows = width;
        this.cols = width * 3;

        char[][] playGround = new char[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                playGround[i][j] = (char) (random.nextInt(1, 10) + '0');

        int m = rows / 2, n = cols / 2;
        playGround[m][n] = '@';
        gameStatus = new GameStatus(m, n, playGround, 0);

        gamePanel = new GamePanel();
        add(gamePanel);
        addKeyListener(this);
        setTitle("Greed Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int cellWidth = getWidth() / cols;
            int cellHeight = (getHeight() - 40) / rows;

            g.setColor(Color.gray);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + gameStatus.score, 10, 25);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    char ch = gameStatus.playGround[i][j];
                    Color color = getColorForChar(ch);
                    g.setColor(color);
                    g.fillRect(j * cellWidth, 40 + i * cellHeight, cellWidth, cellHeight);

                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 16));
                    if (ch != '-')
                        g.drawString(String.valueOf(ch), j * cellWidth + cellWidth / 4,
                                40 + i * cellHeight + 3 * cellHeight / 4);
                }
            }
        }

        private Color getColorForChar(char ch) {
            switch (ch) {
                case '@': return Color.WHITE;
                case '-': return Color.LIGHT_GRAY;
                case '1': return Color.gray;
                case '2': return Color.RED;
                case '3': return Color.ORANGE;
                case '4': return Color.YELLOW;
                case '5': return Color.GREEN;
                case '6': return Color.CYAN;
                case '7': return Color.BLUE;
                case '8': return new Color(128, 0, 128);
                case '9': return Color.PINK;
                default: return Color.GRAY;
            }
        }
    }

    private boolean isStuck(GameStatus status) {
        return (moveUp(status) == -1 &&
                moveDown(status) == -1 &&
                moveLeft(status) == -1 &&
                moveRight(status) == -1);
    }

    private int moveUp(GameStatus status) {
        int m = status.currentRow;
        int n = status.currentCol;
        if (m - 1 < 0 || status.playGround[m - 1][n] == '-') return -1;
        int step = Character.getNumericValue(status.playGround[m - 1][n]);
        if (step > m) return -1;
        return m - step;
    }

    private int moveDown(GameStatus status) {
        int m = status.currentRow;
        int n = status.currentCol;
        if (m + 1 >= rows || status.playGround[m + 1][n] == '-') return -1;
        int step = Character.getNumericValue(status.playGround[m + 1][n]);
        if (step >= rows - m) return -1;
        return m + step;
    }

    private int moveLeft(GameStatus status) {
        int m = status.currentRow;
        int n = status.currentCol;
        if (n - 1 < 0 || status.playGround[m][n - 1] == '-') return -1;
        int step = Character.getNumericValue(status.playGround[m][n - 1]);
        if (step > n) return -1;
        return n - step;
    }

    private int moveRight(GameStatus status) {
        int m = status.currentRow;
        int n = status.currentCol;
        if (n + 1 >= cols || status.playGround[m][n + 1] == '-') return -1;
        int step = Character.getNumericValue(status.playGround[m][n + 1]);
        if (step >= cols - n) return -1;
        return n + step;
    }

    private void movePlayer(String key) {
        int m = gameStatus.currentRow;
        int n = gameStatus.currentCol;
        int newM = m, newN = n;
        char[][] playGround = gameStatus.playGround;
        int gained = 0;

        switch (key.toLowerCase()) {
            case "w": newM = moveUp(gameStatus); break;
            case "s": newM = moveDown(gameStatus); break;
            case "a": newN = moveLeft(gameStatus); break;
            case "d": newN = moveRight(gameStatus); break;
            default: return;
        }

        if (newM == -1) newM = m;
        if (newN == -1) newN = n;

        if (newM != m || newN != n) {
            if (Character.isDigit(playGround[newM][newN]))
                gained = Character.getNumericValue(playGround[newM][newN]);

            modifyBoard(playGround, m, n, newM, newN);

            updateGreekLoc(playGround, m, n, newM, newN);
        }

        gameStatus = new GameStatus(newM, newN, playGround, gameStatus.score + gained);

        gamePanel.repaint();

        if (gameStatus.score >= 100) {
            JOptionPane.showMessageDialog(this, "You won! Final score: " + gameStatus.score);
            System.exit(0);
        }

        if (isStuck(gameStatus)) {
            JOptionPane.showMessageDialog(this, "You lose! Final score: " + gameStatus.score);
            System.exit(0);
        }
    }

    private void modifyBoard(char[][] playGround, int startX, int startY, int endX, int endY) {
        if (endX < startX) {
            int tmp = startX; startX = endX; endX = tmp;
        }
        if (endY < startY) {
            int tmp = startY; startY = endY; endY = tmp;
        }
        if (startY == endY)
            for (int i = startX; i < endX; i++) playGround[i][startY] = '-';
        else if (startX == endX)
            for (int j = startY; j < endY; j++) playGround[startX][j] = '-';
    }

    private void updateGreekLoc(char[][] playGround, int oldM, int oldN, int newM, int newN) {
        playGround[oldM][oldN] = '-';
        playGround[newM][newN] = '@';
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: movePlayer("w"); break;
            case KeyEvent.VK_A: movePlayer("a"); break;
            case KeyEvent.VK_S: movePlayer("s"); break;
            case KeyEvent.VK_D: movePlayer("d"); break;
        }
    }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog("Enter width of game:");
            int width = Integer.parseInt(input);
            new GreedSwing(width);
        });
    }
}


