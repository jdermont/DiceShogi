import com.codingame.game.Game;
import com.codingame.game.GameState;
import com.codingame.game.Move;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Perft {

    /*
        NOTE: those perfts are from fairy stockfish, which for some reason assumes drop pawns checkmate legal
     */
    @Test
    public static void main(String[] args) {
        GameState.ALLOW_PAWN_DROP_CHECKMATE = true;
        GameState gameState = new GameState();
        gameState.init(); // startpos
        Assert.assertEquals(14,perft(gameState, 0, 0 ));
        Assert.assertEquals(181,perft(gameState, 1, 1 ));
        Assert.assertEquals(2512,perft(gameState, 2, 2 ));
        Assert.assertEquals(35401,perft(gameState, 3, 3 ));
        Assert.assertEquals(533203,perft(gameState, 4, 4 ));
        Assert.assertEquals(8276188,perft(gameState, 5, 5 ));

        gameState = new GameState("+P2+Sb/2g2/1ksp1/R1b2/K4 w gr 98");
        Assert.assertEquals(44,perft(gameState, 0, 0 ));
        Assert.assertEquals(317,perft(gameState, 1, 1 ));
        Assert.assertEquals(8731,perft(gameState, 2, 2 ));
        Assert.assertEquals(102947,perft(gameState, 3, 3 ));

        gameState = new GameState("+P1k1S/b1b2/5/G1RRG/1KS1P b - 65");
        Assert.assertEquals(21,perft(gameState, 0, 0 ));
        Assert.assertEquals(72,perft(gameState, 1, 1 ));
        Assert.assertEquals(1395,perft(gameState, 2, 2 ));
        Assert.assertEquals(10233,perft(gameState, 3, 3 ));
        Assert.assertEquals(209599,perft(gameState, 4, 4 ));
        Assert.assertEquals(2334958,perft(gameState, 5, 5 ));

        gameState = new GameState("Kb1+S1/4g/p1k1s/1+r2+r/1+p2+b w g 134");
        Assert.assertEquals(43,perft(gameState, 0, 0 ));
        Assert.assertEquals(121,perft(gameState, 1, 1 ));
        Assert.assertEquals(4653,perft(gameState, 2, 2 ));
        Assert.assertEquals(22970,perft(gameState, 3, 3 ));

        gameState = new GameState("2k1S/B1rP1/2KG1/GS1p1/R1B2 b - 91");
        Assert.assertEquals(3,perft(gameState, 0, 0 ));
        Assert.assertEquals(16,perft(gameState, 1, 1 ));
        Assert.assertEquals(292,perft(gameState, 2, 2 ));
        Assert.assertEquals(3820,perft(gameState, 3, 3 ));
        Assert.assertEquals(74414,perft(gameState, 4, 4 ));
        Assert.assertEquals(986390,perft(gameState, 5, 5 ));

        gameState = new GameState("G2k1/Ps3/R2S1/G1B2/2KPR w B 36");
        Assert.assertEquals(6,perft(gameState, 0, 0 ));
        Assert.assertEquals(206,perft(gameState, 1, 1 ));
        Assert.assertEquals(1519,perft(gameState, 2, 2 ));
        Assert.assertEquals(37232,perft(gameState, 3, 3 ));
        Assert.assertEquals(326508,perft(gameState, 4, 4 ));

        gameState = new GameState("1bsg1/r1P2/3k1/PK3/1G1+r1 w sb 12");
        Assert.assertEquals(55,perft(gameState, 0, 0 ));
        Assert.assertEquals(294,perft(gameState, 1, 1 ));
        Assert.assertEquals(12746,perft(gameState, 2, 2 ));
        Assert.assertEquals(91522,perft(gameState, 3, 3 ));
        Assert.assertEquals(3414510,perft(gameState, 4, 4 ));

        gameState = new GameState("k3S/B1GP1/5/GS1K1/R1B2 b RP 95");
        Assert.assertEquals(49,perft(gameState, 0, 0 ));
        Assert.assertEquals(39,perft(gameState, 1, 1 )); // is less? oO
        Assert.assertEquals(1461,perft(gameState, 2, 2 ));
        Assert.assertEquals(17765,perft(gameState, 3, 3 ));
        Assert.assertEquals(451016,perft(gameState, 4, 4 ));

        // 61 with drop pawn checkmate, 60 without them
        gameState = new GameState("k2+PS/2G2/BS3/b2K1/R4 b PRg 101");
        Assert.assertEquals(61,perft(gameState, 0, 0 ));
        Assert.assertEquals(955,perft(gameState, 1, 1 ));
        Assert.assertEquals(34286,perft(gameState, 2, 2 ));
        Assert.assertEquals(301102,perft(gameState, 3, 3 ));

        // without drop pawn checkmate, from self testing
        GameState.ALLOW_PAWN_DROP_CHECKMATE = false;
        gameState = new GameState("k2+PS/2G2/BS3/b2K1/R4 b PRg 101");
        Assert.assertEquals(60,perft(gameState, 0, 0 ));
        Assert.assertEquals(955,perft(gameState, 1, 1 ));
        Assert.assertEquals(33979,perft(gameState, 2, 2 ));
        Assert.assertEquals(301102,perft(gameState, 3, 3 ));

        gameState = new GameState("2k1S/B1rP1/2KG1/GS1p1/R1B2 b - 91");
        Assert.assertEquals(3,perft(gameState, 0, 0 ));
        Assert.assertEquals(16,perft(gameState, 1, 1 ));
        Assert.assertEquals(292,perft(gameState, 2, 2 ));
        Assert.assertEquals(3820,perft(gameState, 3, 3 ));
        Assert.assertEquals(74319,perft(gameState, 4, 4 ));
        Assert.assertEquals(986388,perft(gameState, 5, 5 ));
    }

    static int perft(GameState gs, int level, int rootLevel) {
        if (level == rootLevel) {
            System.out.printf("Perft %d for %s: ",level+1,gs.toSFENString());
        }
        if (level == 0) {
            List<Move> moves = gs.generateMoves(gs.currentPlayer);
            if (level == rootLevel) {
                System.out.println(moves.size()+" nodes");
            }
            return moves.size();
        }
        List<Move> moves = gs.generateMoves(gs.currentPlayer);
        int sum = 0;
        for (Move move : moves) {
            GameState temp = gs.deepCopy();
            temp.makeMove(move);
            int s = perft(temp,level-1, rootLevel);
            sum += s;
        }
        if (level == rootLevel) {
            System.out.println(sum+" nodes");
        }
        return sum;
    }
}
