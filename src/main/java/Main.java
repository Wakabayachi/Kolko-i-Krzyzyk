import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.play();
    }
}

class TicTacToe {
    private char[][] board;
    private static final int SIZE = 3;
    private static final char EMPTY = '-';
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';

    public TicTacToe() {
        board = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        int currentPlayer = 1;
        boolean gameOver = false;

        printBoard();

        while (!gameOver) {
            char playerSymbol = (currentPlayer == 1) ? PLAYER_X : PLAYER_O;

            if (currentPlayer == 1) {
                System.out.print("Gracz X, podaj numer wiersza (0-2): ");
                int row = scanner.nextInt();
                System.out.print("Gracz X, podaj numer kolumny (0-2): ");
                int col = scanner.nextInt();
                if (isValidMove(row, col)) {
                    makeMove(row, col, playerSymbol);
                    printBoard();
                    if (isWinningMove(row, col, playerSymbol)) {
                        System.out.println("Gracz X wygrał!");
                        gameOver = true;
                    }
                    currentPlayer = 2;
                } else {
                    System.out.println("To pole jest już zajęte lub niepoprawne.");
                }
            } else {
                System.out.println("Ruch sztucznej inteligencji (Gracz O):");
                int[] move = getBestMove();
                makeMove(move[0], move[1], playerSymbol);
                printBoard();
                if (isWinningMove(move[0], move[1], playerSymbol)) {
                    System.out.println("Sztuczna inteligencja wygrała!");
                    gameOver = true;
                }
                currentPlayer = 1;
            }
        }
        scanner.close();
    }

    private void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == EMPTY;
    }

    private void makeMove(int row, int col, char playerSymbol) {
        board[row][col] = playerSymbol;
    }

    private boolean isWinningMove(int row, int col, char playerSymbol) {
        return (board[row][0] == playerSymbol && board[row][1] == playerSymbol && board[row][2] == playerSymbol)
                || (board[0][col] == playerSymbol && board[1][col] == playerSymbol && board[2][col] == playerSymbol)
                || (row == col && board[0][0] == playerSymbol && board[1][1] == playerSymbol && board[2][2] == playerSymbol)
                || (row + col == SIZE - 1 && board[0][2] == playerSymbol && board[1][1] == playerSymbol && board[2][0] == playerSymbol);
    }

    private boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private int evaluateBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (isWinningMove(row, col, PLAYER_X)) {
                    return 10;
                } else if (isWinningMove(row, col, PLAYER_O)) {
                    return -10;
                }
            }
        }
        return 0; // Remis
    }

    private int minimax(int depth, boolean isMaximizer) {
        int score = evaluateBoard();

        if (score == 10 || score == -10) {
            return score;
        }

        if (isBoardFull()) {
            return 0;
        }

        if (isMaximizer) {
            int best = Integer.MIN_VALUE;
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (board[row][col] == EMPTY) {
                        board[row][col] = PLAYER_X;
                        best = Math.max(best, minimax(depth + 1, !isMaximizer));
                        board[row][col] = EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (board[row][col] == EMPTY) {
                        board[row][col] = PLAYER_O;
                        best = Math.min(best, minimax(depth + 1, !isMaximizer));
                        board[row][col] = EMPTY;
                    }
                }
            }
            return best;
        }
    }

    private int[] getBestMove() {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    board[row][col] = PLAYER_X;
                    int moveVal = minimax(0, false);
                    board[row][col] = EMPTY;
                    if (moveVal > bestVal) {
                        bestMove[0] = row;
                        bestMove[1] = col;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }
}