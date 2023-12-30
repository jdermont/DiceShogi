import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int id = in.nextInt(); // id of your player: 0 - first, 1 - second
        int boardSize = in.nextInt(); // size of the board, always 5

        // game loop
        while (true) {
            for (int i = 0; i < boardSize; i++) {
                String line = in.next(); // rows from top to bottom (viewer perspective)
            }
            String player1hand = in.next(); // pieces in player 1's hand or '-' if none
            String player2hand = in.next(); // pieces in player 2's hand or '-' if none
            int rolledDice = in.nextInt(); // number on the rolled dice
            String lastOpponentAction = in.next(); // last opponent's move or 'none' if first turn
            int actionCount = in.nextInt(); // number of legal actions for this turn.
            for (int i = 0; i < actionCount; i++) {
                String action = in.next(); // the action
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // colFrom rowFrom colTo rowTo i.e. 5142, with + for promotion; piece*colToRowTo for drop; you can append message after space
            System.out.println("random");
        }
    }
}