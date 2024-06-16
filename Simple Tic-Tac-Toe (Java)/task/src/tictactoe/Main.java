package tictactoe;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final char TYPE_X = 'X';
    private static final char TYPE_O = 'O';
    private static final char TYPE_EMPTY = '_';
    private static final String EMPTY_BOARD_TEMPLATE = "_".repeat(9);

    public static void main(String[] args) {
        playGame(EMPTY_BOARD_TEMPLATE);
    }

    private static void playGame(final String template) {
        char player = TYPE_O;

        final char[][] board = buildSnapshotBoard(template);
        printBoard(board);

        do {
            player = nextPlayer(player);
            turnPlayer(board, player);
            printBoard(board);
        } while (!isFinishedGame(board));
    }

    private static char[][] buildSnapshotBoard(final String snapshot) {
        final char[][] board = new char[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = snapshot.charAt(3 * i + j);
            }
        }
        return board;
    }

    private static char nextPlayer(final char actualPlayer) {
        return actualPlayer == TYPE_X ? TYPE_O : TYPE_X;
    }

    private static void turnPlayer(char[][] board, char partType) {
        Scanner scanner = new Scanner(System.in);

        int x;
        int y;

        do {
            String coord;

            do {
                coord = scanner.nextLine();
            } while (!isValidInputCoord(coord));

            final String[] s = coord.split(" ");

            x = Integer.parseInt(s[0]) - 1;
            y = Integer.parseInt(s[1]) - 1;

        } while (isCellOccupied(board, x, y));

        board[x][y] = partType;
    }

    private static void printBoard(char[][] board) {
        System.out.println("-".repeat(9));

        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.printf("%c ", board[i][j]);
            }
            System.out.printf("|%n");
        }

        System.out.printf("%s%n", "-".repeat(9));
    }

    private static char[] getFlatMapBoard(char[][] board) {
        return Arrays.stream(board)
                .map(String::new)
                .collect(Collectors.joining())
                .toCharArray();
    }

    private static boolean isFinishedGame(char[][] board) {
        return analyzeStateBoard(getFlatMapBoard(board));
    }

    private static boolean analyzeStateBoard(char[] board) {
        final boolean stateImpossibleState = isStateImpossible(board);
        final boolean stateWinX = isStateWins(board, TYPE_X);
        final boolean stateWinO = isStateWins(board, TYPE_O);
        final boolean stateDrawState = isStateDraw(board);

        if (stateImpossibleState || (stateWinX && stateWinO)) {
            System.out.println("Impossible");
            return true;
        }

        if (stateWinX || stateWinO) {
            System.out.printf("%c wins", stateWinX ? TYPE_X : TYPE_O);
            return true;
        } else if (stateDrawState) {
            System.out.println("Draw");
            return true;
        }

        return false;
    }

    private static boolean isStateWins(char[] board, char partType) {
        return isSamePartType(board, partType, 0, 1, 2) ||
                isSamePartType(board, partType, 3, 4, 5) ||
                isSamePartType(board, partType, 6, 7, 8) ||
                isSamePartType(board, partType, 0, 3, 6) ||
                isSamePartType(board, partType, 1, 4, 7) ||
                isSamePartType(board, partType, 2, 5, 8) ||
                isSamePartType(board, partType, 0, 4, 8) ||
                isSamePartType(board, partType, 2, 4, 6);
    }

    private static boolean isSamePartType(char[] board, char partType, int... positions) {
        return board[positions[0]] == partType &&
                board[positions[1]] == partType &&
                board[positions[2]] == partType;
    }

    private static boolean isStateDraw(char[] board) {
        int sumSpace = 0;

        for (char c : board) {
            if (c == '_') {
                sumSpace++;
                break;
            }
        }

        return sumSpace == 0;
    }

    private static boolean isStateImpossible(char[] board) {
        int sumX = 0;
        int sumO = 0;

        for (char c : board) {
            sumX += c == TYPE_X ? 1 : 0;
            sumO += c == TYPE_O ? 1 : 0;
        }

        return Math.abs(sumX - sumO) > 1;
    }

    private static boolean isCellOccupied(char[][] board, int x, int y) {
        if (board[x][y] != TYPE_EMPTY) {
            System.out.println("This cell is occupied! Choose another one!");
            return true;
        }

        return false;
    }

    private static boolean isValidInputCoord(String value) {
        if (value == null || value.length() != 3 || !value.contains(" ")) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }

        final String[] s = value.split(" ");
        if (s.length != 2) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }

        if (!isNumeric(s[0]) && !isNumeric(s[1])) {
            System.out.println("You should enter numbers!");
            return false;
        }

        for (String string : s) {
            if (Integer.parseInt(string) > 3 || Integer.parseInt(string) < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
                return false;
            }
        }

        return true;
    }

    private static boolean isNumeric(String value) {
        return value != null && value.matches("\\d+$");
    }
}
