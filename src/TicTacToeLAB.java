import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author :Ersin Aydoğmuş
 * @since :26.11.2021
 */
public class TicTacToeLAB {

    public static double fitness(Board board) { // Kazanma olasılığını hesaplar.
        return (double) board.countO / (board.countX + board.countO);
    }

    public static void main(String[] Args) throws IllegalAccessException {
        double temp;
        Board best;
        Board game1 = new Board();
        game1.printBoard();
        do {
            game1.getPlayerMove();
            game1.calculateComputerMove();

            temp = Double.NEGATIVE_INFINITY;
            best = null;
            for (Board child : game1.children) {
                double ratio = fitness(child);
                System.out.println(ratio);
                if (ratio > temp) {
                    temp = ratio;
                    best = child;
                }
            }
            System.out.println();
            best.printBoard();
            game1 = best;
        } while (game1.winner() == 0 && game1.children.size() > 0);

    }
}

class Board { //Tahtayı arraylist ve int Arrayde tutar.
    public static final int X = 1, O = -1, EMPTY = 0;
    private int[][] boardArray = new int[3][3];
    private int player; // if player=1 X will play else if =-1 O will play
    public ArrayList<Board> children;
    public Board parent;
    public int countX = 0, countO = 0;

    Board() {
        children = new ArrayList<>(9);
        clearBoard();
    }

    public void moveFromUser(int i, int j) {
        this.boardArray[i][j] = X;
    }

    public void clearBoard() { // Clear Board.
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[0].length; j++) {
                boardArray[i][j] = EMPTY;
            }
        }
        player = X;
    }

    public void printBoard() { // Print Board to Terminal.
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[0].length; j++) {
                if (boardArray[i][j] == 1)
                    System.out.print("X  ");
                else if (boardArray[i][j] == -1)
                    System.out.print("O  ");
                else
                    System.out.print(".  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isWin(int a) { //Kazanıp kazanmadığını kontrol eder.
        if (boardArray[0][0] + boardArray[0][1] + boardArray[0][2] == 3 * a) {
            return true;
        } else if (boardArray[1][0] + boardArray[1][1] + boardArray[1][2] == 3 * a) {
            return true;
        } else if (boardArray[2][0] + boardArray[2][1] + boardArray[2][2] == 3 * a) {
            return true;
        } else if (boardArray[0][0] + boardArray[1][0] + boardArray[2][0] == 3 * a) {
            return true;
        } else if (boardArray[0][1] + boardArray[1][1] + boardArray[2][1] == 3 * a) {
            return true;
        } else if (boardArray[0][2] + boardArray[1][2] + boardArray[2][2] == 3 * a) {
            return true;
        } else if (boardArray[0][0] + boardArray[1][1] + boardArray[2][2] == 3 * a) {
            return true;
        } else if (boardArray[2][0] + boardArray[1][1] + boardArray[0][2] == 3 * a) {
            return true;
        } else
            return false;
    }

    public void putMark(int i, int j) throws IllegalAccessException { // X or O koyar.
        if (i < 0 || i > 2 || j < 0 || j > 2)
            throw new IllegalAccessException("Invalid Board position");
        if (boardArray[i][j] != EMPTY)
            throw new IllegalAccessException("Board position occupied");
        boardArray[i][j] = player;
        player = -player;
    }

    public void getPlayerMove() throws IllegalAccessException { // Kullanıcıdan girdi alıp i ve j deki konum boşsa X Koyar.
        int i, j;
        do {
            Scanner s = new Scanner(System.in);
            System.out.print("Please enter i = ");
            i = s.nextInt();
            System.out.print("Please enter j = ");
            j = s.nextInt();
        } while (i < 0 || i > 2 || j < 0 || j > 2 || (boardArray[i][j] != EMPTY));

        putMark(i, j);
    }

    public void printTree() { // Tree yi terminala yazdırır.
        if (this.winner() != 0)
            this.printBoard();
        for (Board child : this.children) {
            child.printTree();
        }
    }

    public void calculateComputerMove() { // Bilgisayar Hesaplamalarını yapar.
        this.children.clear();
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[0].length; j++) {
                if (boardArray[i][j] == EMPTY) {
                    Board tic = this.cloneBoard();
                    tic.parent = this;
                    try {
                        tic.putMark(i, j);
                        this.children.add(tic);
                        int winner = tic.winner();
                        if (winner == EMPTY) {
                            tic.calculateComputerMove();
                        } else {
                            if (winner == X) {// X
                                this.countX++;
                            } else if (winner == O) {
                                this.countO++; // O
                            }

                            Board temp = tic;
                            while (temp.parent != null) {
                                temp = temp.parent;
                                if (winner == X) {// X
                                    temp.countX++;
                                } else { // O
                                    temp.countO++;
                                }
                            }
                        }
                    } catch (IllegalAccessException iae) {
                    }
                }
            }
        }
    }

    public int winner() { // Kimin kazanıp kazanmadığını döndürür.
        if (isWin(X))
            return (X);
        else if (isWin(O))
            return (O);
        else
            return (0);
    }

    public Board cloneBoard() { // Mevcut olan oyunu kopyalar.
        Board newBoard = new Board();
        for (int i = 0; i < this.boardArray.length; i++) {
            for (int j = 0; j < this.boardArray[0].length; j++) {
                newBoard.boardArray[i][j] = this.boardArray[i][j];
            }
        }
        newBoard.player = this.player;
        return newBoard;
    }
}
