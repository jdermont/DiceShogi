package com.codingame.game;

import java.io.*;
import java.util.*;

public class GameState implements Serializable {
    public static final int SIZE = 5;

    public Piece[][][] board;
    public List<List<Piece>> hands;
    public int currentPlayer;
    public int rounds;

    public GameState() {

    }

    public GameState(String sfen) {
        String[] parts = sfen.split(" ");
        currentPlayer = parts[1].equals("b") ? 0 : 1;
        rounds = Integer.parseInt(parts[3])-1;

        board = new Piece[2][SIZE][SIZE];
        hands = new ArrayList<>();
        for (int i=0; i < 2; i++) {
            hands.add(new ArrayList<>());
        }
        if (!parts[2].equals("-")) {
            char[] chars = parts[2].toCharArray();
            for (int j=0; j < chars.length; j++) {
                if (chars[j] == '2') {
                    switch (chars[j+1]) {
                        case 'R': hands.get(0).add(Piece.ROOK); break;
                        case 'B': hands.get(0).add(Piece.BISHOP); break;
                        case 'P': hands.get(0).add(Piece.PAWN); break;
                        case 'S': hands.get(0).add(Piece.SILVER); break;
                        case 'G': hands.get(0).add(Piece.GOLD); break;
                        case 'r': hands.get(1).add(Piece.ROOK); break;
                        case 'b': hands.get(1).add(Piece.BISHOP); break;
                        case 'p': hands.get(1).add(Piece.PAWN); break;
                        case 's': hands.get(1).add(Piece.SILVER); break;
                        case 'g': hands.get(1).add(Piece.GOLD); break;
                    }
                    switch (chars[j+1]) {
                        case 'R': hands.get(0).add(Piece.ROOK); break;
                        case 'B': hands.get(0).add(Piece.BISHOP); break;
                        case 'P': hands.get(0).add(Piece.PAWN); break;
                        case 'S': hands.get(0).add(Piece.SILVER); break;
                        case 'G': hands.get(0).add(Piece.GOLD); break;
                        case 'r': hands.get(1).add(Piece.ROOK); break;
                        case 'b': hands.get(1).add(Piece.BISHOP); break;
                        case 'p': hands.get(1).add(Piece.PAWN); break;
                        case 's': hands.get(1).add(Piece.SILVER); break;
                        case 'g': hands.get(1).add(Piece.GOLD); break;
                    }
                    j++;
                } else {
                    switch (chars[j]) {
                        case 'R': hands.get(0).add(Piece.ROOK); break;
                        case 'B': hands.get(0).add(Piece.BISHOP); break;
                        case 'P': hands.get(0).add(Piece.PAWN); break;
                        case 'S': hands.get(0).add(Piece.SILVER); break;
                        case 'G': hands.get(0).add(Piece.GOLD); break;
                        case 'r': hands.get(1).add(Piece.ROOK); break;
                        case 'b': hands.get(1).add(Piece.BISHOP); break;
                        case 'p': hands.get(1).add(Piece.PAWN); break;
                        case 's': hands.get(1).add(Piece.SILVER); break;
                        case 'g': hands.get(1).add(Piece.GOLD); break;
                    }
                }
            }
        }
        String[] rows = parts[0].split("/");
        int row = SIZE-1;
        for (String r : rows) {
            int col = 0;
            char[] chars = r.toCharArray();
            for (int j=0; j < chars.length; j++) {
                boolean promote = false;
                char c;
                if (chars[j] == '+') {
                    c = chars[j+1];
                    promote = true;
                    j++;
                } else {
                    c = chars[j];
                }
                if (c >= '1' && c <= '5') {
                    int n = (c - '1') + 1;
                    col += n;
                } else {
                    switch (c) {
                        case 'R': board[0][row][col] = promote ? Piece.ROOK.promote() : Piece.ROOK; break;
                        case 'B': board[0][row][col] = promote ? Piece.BISHOP.promote() : Piece.BISHOP; break;
                        case 'P': board[0][row][col] = promote ? Piece.PAWN.promote() : Piece.PAWN; break;
                        case 'S': board[0][row][col] = promote ? Piece.SILVER.promote() : Piece.SILVER; break;
                        case 'G': board[0][row][col] = Piece.GOLD; break;
                        case 'K': board[0][row][col] = Piece.KING; break;
                        case 'r': board[1][row][col] = promote ? Piece.ROOK.promote() : Piece.ROOK; break;
                        case 'b': board[1][row][col] = promote ? Piece.BISHOP.promote() : Piece.BISHOP; break;
                        case 'p': board[1][row][col] = promote ? Piece.PAWN.promote() : Piece.PAWN; break;
                        case 's': board[1][row][col] = promote ? Piece.SILVER.promote() : Piece.SILVER; break;
                        case 'g': board[1][row][col] = Piece.GOLD;; break;
                        case 'k': board[1][row][col] = Piece.KING; break;
                    }
                    col++;
                }
            }
            row--;
        }
    }

    public String toSFENString() {
        StringBuilder sb = new StringBuilder();
        for (int row=0; row < SIZE; row++) {
            int c = 0;
            for (int col=0; col < SIZE; col++) {
                int player = 0;
                Piece piece = board[player][4-row][col];
                if (piece == null) {
                    player = 1;
                    piece = board[player][4-row][col];
                }
                if (piece == null) {
                    c++;

                } else {
                    if (c > 0) {
                        sb.append(c);
                        c = 0;
                    }
                    sb.append(piece.getChr(player));
                }
            }
            if (c > 0) {
                sb.append(c);
            }
            sb.append('/');
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(' ');
        sb.append(currentPlayer == Game.FIRST_PLAYER ? 'b' : 'w');
        if (hands.get(0).isEmpty() && hands.get(1).isEmpty()) {
            sb.append(" - ");
        } else {
            sb.append(' ');
            Map<Piece,Integer> pieces = new HashMap<>();
            for (Piece piece : hands.get(0)) {
                pieces.put(piece,pieces.getOrDefault(piece,0)+1);
            }
            for (Piece piece : pieces.keySet()) {
                if (pieces.get(piece) > 1) {
                    sb.append(pieces.get(piece)).append(piece.getChr(Game.FIRST_PLAYER));
                } else {
                    sb.append(piece.getChr(Game.FIRST_PLAYER));
                }
            }
            pieces.clear();
            for (Piece piece : hands.get(1)) {
                pieces.put(piece,pieces.getOrDefault(piece,0)+1);
            }
            for (Piece piece : pieces.keySet()) {
                if (pieces.get(piece) > 1) {
                    sb.append(pieces.get(piece)).append(piece.getChr(Game.SECOND_PLAYER));
                } else {
                    sb.append(piece.getChr(Game.SECOND_PLAYER));
                }
            }
            sb.append(' ');
        }
        sb.append(rounds+1);
        return sb.toString();
    }

    public GameState deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return (GameState) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException("deepCopy fail, should not happen wtf", e);
        }
    }

    public void init() {
        board = new Piece[2][SIZE][SIZE];
        hands = new ArrayList<>();
        for (int i=0; i < 2; i++) {
            hands.add(new ArrayList<>());
        }
        currentPlayer = 0;
        rounds = 0;
        initBoard();
    }

    private void initBoard() {
        board[0][0][0] = Piece.KING;
        board[0][0][1] = Piece.GOLD;
        board[0][0][2] = Piece.SILVER;
        board[0][0][3] = Piece.BISHOP;
        board[0][0][4] = Piece.ROOK;
        board[0][1][0] = Piece.PAWN;

        board[1][4][4] = Piece.KING;
        board[1][4][3] = Piece.GOLD;
        board[1][4][2] = Piece.SILVER;
        board[1][4][1] = Piece.BISHOP;
        board[1][4][0] = Piece.ROOK;
        board[1][3][4] = Piece.PAWN;
    }

    public void makeMove(Move move) {
        int opponent = currentPlayer^1;
        if (move.rowFrom == -1) {
            board[currentPlayer][move.rowTo][move.colTo] = move.piece;
            hands.get(currentPlayer).remove(move.piece);
        } else {
            Piece piece = board[currentPlayer][move.rowFrom][move.colFrom];
            board[currentPlayer][move.rowFrom][move.colFrom] = null;
            board[currentPlayer][move.rowTo][move.colTo] = move.promote ? piece.promote() : piece;
            if (board[opponent][move.rowTo][move.colTo] != null) {
                hands.get(currentPlayer).add(board[opponent][move.rowTo][move.colTo].demote());
                board[opponent][move.rowTo][move.colTo] = null;
            }
        }
        currentPlayer = opponent;
        rounds++;
    }

    public boolean isKingInCheck() {
        return isKingInCheck(currentPlayer);
    }

    public boolean isKingInCheck(int player) {
        for (int row=0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Piece piece = board[player][row][col];
                if (piece == Piece.KING) {
                    return checkedByGold(row,col,player) || checkedBySilver(row,col,player) || checkedByPawn(row,col,player)
                            || checkedByKing(row,col,player) || checkedByRook(row,col,player) || checkedByBishop(row,col,player);
                }
            }
        }
        return false;
    }

    public boolean isOver() {
        return generateMoves(currentPlayer).isEmpty();
    }

    private boolean checkedByRook(int row, int col, int player) {
        int opponent = player^1;
        int r,c;
        r = row-1;
        c = col;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] != null) {
                    if (board[opponent][r][c].doesRookMoves()) return true;
                    break;
                }
                r--;
            } else {
                break;
            }
        }
        r = row+1;
        c = col;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] != null) {
                    if (board[opponent][r][c].doesRookMoves()) return true;
                    break;
                }
                r++;
            } else {
                break;
            }
        }
        r = row;
        c = col-1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] != null) {
                    if (board[opponent][r][c].doesRookMoves()) return true;
                    break;
                }
                c--;
            } else {
                break;
            }
        }
        r = row;
        c = col+1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] != null) {
                    if (board[opponent][r][c].doesRookMoves()) return true;
                    break;
                }
                c++;
            } else {
                break;
            }
        }
        return false;
    }

    private boolean checkedByBishop(int row, int col, int player) {
        int opponent = player^1;
        int r,c;
        r = row-1;
        c = col-1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] != null) {
                    if (board[opponent][r][c].doesBishopMoves()) return true;
                    break;
                }
                r--;
                c--;
            } else {
                break;
            }
        }
        r = row+1;
        c = col-1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] != null) {
                    if (board[opponent][r][c].doesBishopMoves()) return true;
                    break;
                }
                r++;
                c--;
            } else {
                break;
            }
        }
        r = row-1;
        c = col+1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] != null) {
                    if (board[opponent][r][c].doesBishopMoves()) return true;
                    break;
                }
                r--;
                c++;
            } else {
                break;
            }
        }
        r = row+1;
        c = col+1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] != null) {
                    if (board[opponent][r][c].doesBishopMoves()) return true;
                    break;
                }
                r++;
                c++;
            } else {
                break;
            }
        }
        return false;
    }

    private boolean checkedByPawn(int row, int col, int player) {
        int opponent = player^1;
        int r = player == Game.FIRST_PLAYER ? (row+1) : (row-1);
        int c = col;
        if (isValid(r) && isValid(c)) {
            return board[opponent][r][c] == Piece.PAWN;
        }
        return false;
    }

    private boolean checkedByGold(int row, int col, int player) {
        int opponent = player^1;
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (i == (player == Game.FIRST_PLAYER ? -1 : 1) && j != 0) continue;
                int r = row+i;
                int c = col+j;
                if (isValid(r) && isValid(c)) {
                    if (board[opponent][r][c] != null && board[opponent][r][c].doesGoldMoves()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkedBySilver(int row, int col, int player) {
        int opponent = player^1;
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                if (i == 0) continue;
                if (j == 0 && i == (player == Game.FIRST_PLAYER ? -1 : 1)) continue;
                int r = row+i;
                int c = col+j;
                if (isValid(r) && isValid(c)) {
                    if (board[opponent][r][c] == Piece.SILVER) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkedByKing(int row, int col, int player) {
        int opponent = player^1;
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int r = row+i;
                int c = col+j;
                if (isValid(r) && isValid(c)) {
                    if (board[opponent][r][c] != null && board[opponent][r][c].doesKingMoves()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Move> generateMoves() {
        return generateMoves(currentPlayer);
    }

    public List<Move> generateMoves(int player) {
        List<Move> moves = new ArrayList<>();
        for (int row=0; row < SIZE; row++) {
            for (int col=0; col < SIZE; col++) {
                Piece piece = board[player][row][col];
                if (piece != null) {
                    switch (piece) {
                        case KING:
                            moves.addAll(getKingMoves(row,col,player));
                            break;
                        case GOLD:
                        case PROMOTED_PAWN:
                        case PROMOTED_SILVER:
                            moves.addAll(getGoldMoves(row,col,player, piece));
                            break;
                        case SILVER:
                            moves.addAll(getSilverMoves(row,col,player));
                            break;
                        case BISHOP:
                        case PROMOTED_BISHOP:
                            moves.addAll(getBishopMoves(row,col,player, piece));
                            break;
                        case ROOK:
                        case PROMOTED_ROOK:
                            moves.addAll(getRookMoves(row,col,player, piece));
                            break;
                        case PAWN:
                            moves.addAll(getPawnMoves(row,col,player));
                            break;
                    }
                }
            }
        }
        moves.addAll(getDropMoves(player));
        for (int i=0; i < moves.size(); i++) {
            Move move = moves.get(i);
            GameState temp = this.deepCopy();
            temp.makeMove(move);
            if (temp.isKingInCheck(player)) {
                moves.remove(i);
                i--;
            }
        }
        return moves;
    }

    private List<Move> getDropMoves(int player) {
        List<Move> dropMoves = new ArrayList<>();
        Set<Piece> alreadyAdded = new HashSet<>();
        for (Piece piece : hands.get(player)) {
            if (alreadyAdded.contains(piece)) {
                continue;
            }
            alreadyAdded.add(piece);
            if (piece == Piece.PAWN) {
                for (int row=0; row < SIZE; row++) {
                    for (int col=0; col < SIZE; col++) {
                        if ((player == Game.FIRST_PLAYER && row == 4) || (player == Game.SECOND_PLAYER && row == 0)) continue;
                        if (board[0][row][col] == null && board[1][row][col] == null) {
                            boolean alreadyPawn = false;
                            for (int r=0; r < SIZE; r++) {
                                if (board[player][r][col] == Piece.PAWN) {
                                    alreadyPawn = true;
                                    break;
                                }
                            }
                            if (!alreadyPawn) {
                                // don't forget to make pawn drop checkmate illegal,
                                // fairy stockfish and minishogilib seem to allow that
                                // but that spoils perft :s
                                GameState temp = this.deepCopy();
                                temp.makeMove(new Move(player,row,col,piece));
                                if (!temp.isOver()) {
                                    dropMoves.add(new Move(player,row,col,piece));
                                }
                            }
                        }
                    }
                }
            } else {
                for (int row=0; row < SIZE; row++) {
                    for (int col=0; col < SIZE; col++) {
                        if (board[0][row][col] == null && board[1][row][col] == null) {
                            dropMoves.add(new Move(player,row,col,piece));
                        }
                    }
                }
            }
        }
        return dropMoves;
    }

    private List<Move> getKingMoves(int row, int col, int player) {
        List<Move> kingMoves = new ArrayList<>();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int r = row+i;
                int c = col+j;
                if (isValid(r) && isValid(c)) {
                    if (board[player][r][c] == null) {
                        kingMoves.add(new Move(player,row,col,r,c,Piece.KING));
                    }
                }
            }
        }
        return kingMoves;
    }

    private List<Move> getRookMoves(int row, int col, int player, Piece piece) {
        int opponent = player^1;
        List<Move> rookMoves = new ArrayList<>();
        int r,c;
        r = row-1;
        c = col;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] == null) {
                    rookMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        rookMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                } else {
                    rookMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        rookMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                    break;
                }
                r--;
            } else {
                break;
            }
        }
        r = row+1;
        c = col;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] == null) {
                    rookMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        rookMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                } else {
                    rookMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        rookMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                    break;
                }
                r++;
            } else {
                break;
            }
        }
        r = row;
        c = col-1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] == null) {
                    rookMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        rookMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                } else {
                    rookMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        rookMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                    break;
                }
                c--;
            } else {
                break;
            }
        }
        r = row;
        c = col+1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] == null) {
                    rookMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        rookMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                } else {
                    rookMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        rookMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                    break;
                }
                c++;
            } else {
                break;
            }
        }
        if (piece == Piece.PROMOTED_ROOK) {
            for (int i=-1; i <= 1; i+=2) {
                for (int j=-1; j <= 1; j+=2) {
                    r = row+i;
                    c = col+j;
                    if (isValid(r) && isValid(c)) {
                        if (board[player][r][c] == null) {
                            rookMoves.add(new Move(player,row,col,r,c,piece));
                        }
                    }
                }
            }
        }
        return rookMoves;
    }

    private List<Move> getBishopMoves(int row, int col, int player, Piece piece) {
        int opponent = player^1;
        List<Move> bishopMoves = new ArrayList<>();
        int r,c;
        r = row-1;
        c = col-1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] == null) {
                    bishopMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                } else {
                    bishopMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                    break;
                }
                r--;
                c--;
            } else {
                break;
            }
        }
        r = row+1;
        c = col-1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] == null) {
                    bishopMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                } else {
                    bishopMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                    break;
                }
                r++;
                c--;
            } else {
                break;
            }
        }
        r = row-1;
        c = col+1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] == null) {
                    bishopMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                } else {
                    bishopMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                    break;
                }
                r--;
                c++;
            } else {
                break;
            }
        }
        r = row+1;
        c = col+1;
        while (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                if (board[opponent][r][c] == null) {
                    bishopMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                } else {
                    bishopMoves.add(new Move(player,row,col,r,c,piece));
                    boolean canPromote = piece.canPromote() && (player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0));
                    if (canPromote) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece,true));
                    }
                    break;
                }
                r++;
                c++;
            } else {
                break;
            }
        }
        if (piece == Piece.PROMOTED_BISHOP) {
            for (int i=-1; i <= 1; i+=2) {
                r = row+i;
                c = col;
                if (isValid(r) && isValid(c)) {
                    if (board[player][r][c] == null) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece));
                    }
                }
                r = row;
                c = col+i;
                if (isValid(r) && isValid(c)) {
                    if (board[player][r][c] == null) {
                        bishopMoves.add(new Move(player,row,col,r,c,piece));
                    }
                }
            }
        }
        return bishopMoves;
    }

    private List<Move> getGoldMoves(int row, int col, int player, Piece piece) {
        List<Move> goldMoves = new ArrayList<>();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (i == (player == Game.FIRST_PLAYER ? -1 : 1) && j != 0) continue;
                int r = row+i;
                int c = col+j;
                if (isValid(r) && isValid(c)) {
                    if (board[player][r][c] == null) {
                        goldMoves.add(new Move(player,row,col,r,c,piece));
                    }
                }
            }
        }
        return goldMoves;
    }

    private List<Move> getSilverMoves(int row, int col, int player) {
        List<Move> silverMoves = new ArrayList<>();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                if (i == 0) continue;
                if (j == 0 && i == (player == Game.FIRST_PLAYER ? -1 : 1)) continue;
                int r = row+i;
                int c = col+j;
                if (isValid(r) && isValid(c)) {
                    if (board[player][r][c] == null) {
                        silverMoves.add(new Move(player,row,col,r,c,Piece.SILVER,false));
                        boolean canPromote = player == Game.FIRST_PLAYER ? (r == 4 || row == 4) : (r == 0 || row == 0);
                        if (canPromote) {
                            silverMoves.add(new Move(player,row,col,r,c,Piece.SILVER,true));
                        }
                    }
                }
            }
        }
        return silverMoves;
    }

    private List<Move> getPawnMoves(int row, int col, int player) {
        List<Move> pawnMoves = new ArrayList<>();
        int r = player == Game.FIRST_PLAYER ? (row+1) : (row-1);
        int c = col;
        if (isValid(r) && isValid(c)) {
            if (board[player][r][c] == null) {
                boolean promote = player == Game.FIRST_PLAYER ? (r == 4) : (r == 0);
                pawnMoves.add(new Move(player,row,col,r,c,Piece.PAWN,promote));
            }
        }
        return pawnMoves;
    }

    private boolean isValid(int x) {
        return x >= 0 && x < SIZE;
    }

    public String getBoardString() {
        StringBuilder sb = new StringBuilder();
        for (int row=0; row < SIZE; row++) {
            for (int col=0; col < SIZE; col++) {
                int player = 0;
                Piece piece = board[player][4-row][col];
                if (piece == null) {
                    player = 1;
                    piece = board[player][4-row][col];
                }
                if (piece == null) {
                    sb.append('.');
                } else {
                    sb.append(piece.getChr(player));
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row=0; row < SIZE; row++) {
            for (int col=0; col < SIZE; col++) {
                int player = 0;
                Piece piece = board[player][4-row][col];
                if (piece == null) {
                    player = 1;
                    piece = board[player][4-row][col];
                }
                if (piece == null) {
                    sb.append('.');
                } else {
                    sb.append(piece.getChr(player));
                }
            }
            sb.append('\n');
        }
        sb.append("hand 1: ");
        for (Piece piece : hands.get(Game.FIRST_PLAYER)) {
            sb.append(piece.getChr(Game.FIRST_PLAYER)).append(' ');
        }
        sb.append('\n');
        sb.append("hand 2: ");
        for (Piece piece : hands.get(Game.SECOND_PLAYER)) {
            sb.append(piece.getChr(Game.SECOND_PLAYER)).append(' ');
        }
        sb.append('\n');
        return sb.toString();
    }
}
