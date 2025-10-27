import java.lang.management.MemoryType;
import java.util.Random;
import java.util.Scanner;



public class Greed {

    public static void main(String[] args) {
        System.out.println("Menu" + "\n1_PlayGame/change width" + "\n2_Exit");
        Scanner Menu = new Scanner(System.in);
        int Me = Menu.nextInt();

        menu:
        switch (Me) {
            case 1:
                System.out.println("Enter Width game:... ");
                Scanner input = new Scanner(System.in);
                int x = input.nextInt();
                int y = x * 3;
                System.out.println("You Can Move With wasd");

                char[][] playGround = new char[x][y];
                Random random = new Random();
                for (int i = 0; i < x; i++) {
                    for (int j = 0; j < y; j++) {
                        playGround[i][j] = (char) (random.nextInt(1, 10) + '0'); // '1'..'9'
                    }
                }

                int m = x / 2, n = y / 2;
                playGround[m][n] = '@';
                Scanner scanner = new Scanner(System.in);
                int score = 0;

                GameStatus gameStatus = new GameStatus(m, n, playGround, score);

                while (true) {
                    displayBorad(gameStatus.playGround);
                    System.out.println("Score: " + gameStatus.score);
                    System.out.print("Enter move (w/a/s/d): ");
                    String command = scanner.next();

                    GameStatus newStatus = move(command, gameStatus);

                    if (newStatus.currentRow == gameStatus.currentRow &&
                            newStatus.currentCol == gameStatus.currentCol) {
                        System.out.println("Invalid move or blocked!");
                    }

                    gameStatus = newStatus;

                    if (gameStatus.score >= 100) {
                        displayBorad(gameStatus.playGround);
                        System.out.println("You won! Final score: " + gameStatus.score);
                        break;
                    }

                    if (isStuck(gameStatus.playGround, gameStatus.currentRow, gameStatus.currentCol)) {
                        displayBorad(gameStatus.playGround);
                        System.out.println("You lose! Final score: " + gameStatus.score);
                        break;
                    }
                }
                break;

            case 2:
                System.out.println("Good Bye");
                break menu;

            default:
                System.out.println("Enter Valid Key.");
                break;
        }
    }

    public static boolean isStuck(char[][] playGround, int m, int n) {
        return (moveUp(playGround, m, n) == -1 &&
                moveDown(playGround, m, n) == -1 &&
                moveLeft(playGround, m, n) == -1 &&
                moveRight(playGround, m, n) == -1);
    }

    public static char[][] modifyBorad(char[][] playGround, int startX, int startY,
                                       int endX, int endY) {
        if (endX < startX) {
            int tmpInx = startX;
            startX = endX;
            endX = tmpInx;
        }
        if (endY < startY) {
            int tmpInx = startY;
            startY = endY;
            endY = tmpInx;
        }
        if (startY == endY) {
            for (int i = startX; i < endX; i++)
                playGround[i][startY] = '-';
        } else if (startX == endX) {
            for (int j = startY; j < endY; j++)
                playGround[startX][j] = '-';
        }
        return playGround;
    }

    public static void displayBorad(char[][] playGround) {
        for (int i = 0; i < playGround.length; i++) {
            for (int j = 0; j < playGround[0].length; j++) {
                if (playGround[i][j] == '-') {
                    System.out.print(' ');
                    continue;
                }
                System.out.print(playGround[i][j]);
            }
            System.out.println();
        }
    }

    public static char[][] updateGreekLoc(char[][] playGround, int oldM, int oldN,
                                          int newM, int newN) {
        playGround[oldM][oldN] = '-';
        playGround[newM][newN] = '@';
        return playGround;
    }

    public static GameStatus move(String command, GameStatus status) {
        int m = status.currentRow;
        int n = status.currentCol;
        char[][] playGround = status.playGround;
        int newM = m;
        int newN = n;
        char[][] newPlayGround = playGround;
        int gained = 0;

        switch (command) {
            case "w":
                newM = moveUp(playGround, m, n);
                if (newM == -1) {
                    newM = m;
                    break;
                }
                if (Character.isDigit(playGround[newM][newN]))
                    gained = Character.getNumericValue(playGround[newM][newN]);
                newPlayGround = modifyBorad(playGround, m, n, newM, newN);
                break;
            case "s":
                newM = moveDown(playGround, m, n);
                if (newM == -1) {
                    newM = m;
                    break;
                }
                if (Character.isDigit(playGround[newM][newN]))
                    gained = Character.getNumericValue(playGround[newM][newN]);
                newPlayGround = modifyBorad(playGround, m, n, newM, newN);
                break;
            case "a":
                newN = moveLeft(playGround, m, n);
                if (newN == -1) {
                    newN = n;
                    break;
                }
                if (Character.isDigit(playGround[newM][newN]))
                    gained = Character.getNumericValue(playGround[newM][newN]);
                newPlayGround = modifyBorad(playGround, m, n, newM, newN);
                break;
            case "d":
                newN = moveRight(playGround, m, n);
                if (newN == -1) {
                    newN = n;
                    break;
                }
                if (Character.isDigit(playGround[newM][newN]))
                    gained = Character.getNumericValue(playGround[newM][newN]);
                newPlayGround = modifyBorad(playGround, m, n, newM, newN);
                break;
            default:
                System.out.println("Enter Valid Key.");
                break;
        }

        newPlayGround = updateGreekLoc(newPlayGround, m, n, newM, newN);
        int newScore = status.score + gained;
        return new GameStatus(newM, newN, newPlayGround, newScore);
    }

    public static int moveUp(char[][] playGround, int m, int n) {
        if (m - 1 < 0 || playGround[m - 1][n] == '-')
            return -1;
        int step = Character.getNumericValue(playGround[m - 1][n]);
        if (step > m) return -1;
        return m - step;
    }

    public static int moveDown(char[][] playGround, int m, int n) {
        if (m + 1 >= playGround.length || playGround[m + 1][n] == '-')
            return -1;
        int step = Character.getNumericValue(playGround[m + 1][n]);
        if (step >= playGround.length - m) return -1;
        return m + step;
    }

    public static int moveLeft(char[][] playGround, int m, int n) {
        if (n - 1 < 0 || playGround[m][n - 1] == '-')
            return -1;
        int step = Character.getNumericValue(playGround[m][n - 1]);
        if (step > n) return -1;
        return n - step;
    }

    public static int moveRight(char[][] playGround, int m, int n) {
        if (n + 1 >= playGround[0].length || playGround[m][n + 1] == '-')
            return -1;
        int step = Character.getNumericValue(playGround[m][n + 1]);
        if (step >= playGround[0].length - n) return -1;
        return n + step;
    }
}