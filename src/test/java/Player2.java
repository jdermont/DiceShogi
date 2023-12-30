import com.codingame.game.Move;

import java.util.Random;
import java.util.Scanner;

public class Player2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();
        int size = in.nextInt();
//        System.err.println(id);

        while (true) {

//            System.err.println(size);
            for (int i=0; i < size; i++) {
                String row = in.next();
                System.err.println(row);
            }
            String hand1 = in.next();
            System.err.println(hand1);
            String hand2 = in.next();
            System.err.println(hand2);
            int diceRoll = in.nextInt();
//            System.err.println(diceRoll);
            String lastOpponentMove = in.next();
//            System.err.println(lastOpponentMove);
            int movesSize = in.nextInt();
//            System.err.println(movesSize);
            String action = null;
            for (int i=0; i < movesSize; i++) {
                String move = in.next();
                if (action == null || Math.random() > 0.5) action = move;
//                System.err.println(move);
            }
            System.out.println(action+" Take this!");

        }
    }
}
