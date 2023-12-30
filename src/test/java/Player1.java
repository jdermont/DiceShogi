import java.util.Scanner;

public class Player1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();
        int size = in.nextInt();

        while (true) {
            for (int i=0; i < size; i++) {
                String row = in.next();
                System.err.println(row);
            }
            String hand1 = in.next();
            String hand2 = in.next();
            int diceRoll = in.nextInt();
            String lastOpponentMove = in.next();
            int movesSize = in.nextInt();
            for (int i=0; i < movesSize; i++) {
                String move = in.next();
            }
            System.out.println("random");
        }
    }
}
