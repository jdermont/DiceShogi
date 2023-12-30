package com.codingame.view;

import com.codingame.game.Game;
import com.codingame.game.GameState;
import com.codingame.game.Move;
import com.codingame.game.Piece;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.toggle.ToggleModule;

import java.util.ArrayList;
import java.util.List;

public class BoardViewer {
    private GraphicEntityModule graphicEntityModule;
    private ToggleModule toggleModule;
    private Group group;
    private Rectangle[][] squares;
    private Rectangle[] columns;
    private Rectangle checkSquare;
    private Text sfenText;

    private List<List<PieceSprite>> unusedPieces;
    private List<List<PieceSprite>> usedPieces;

    private Game game;

    public BoardViewer(Game game, GraphicEntityModule graphicEntityModule, ToggleModule toggleModule) {
        this.game = game;
        this.graphicEntityModule = graphicEntityModule;
        this.toggleModule = toggleModule;
    }

    public void init() {
        group = graphicEntityModule.createGroup();
        group.setX(464);
        group.setY(60);

        initBackground();
        initSquares();
        initLines();
        initCircles();
        initPieces();
        initTexts();
    }

    private void initBackground() {
        Rectangle r = graphicEntityModule.createRectangle()
                .setWidth(960)
                .setHeight(960)
                .setX(0)
                .setY(24)
                .setFillColor(0xfbb34d)
                .setZIndex(1);
        group.add(r);
    }

    private void initLines() {
        for (int i = 1; i < GameState.SIZE; i++) {
            Line line = graphicEntityModule.createLine();
            line.setX(0);
            line.setY(192 * i + 24);
            line.setX2(960);
            line.setY2(192 * i  + 24);
            line.setLineWidth(2);
            line.setLineColor(0x000000);
            line.setZIndex(4);
            group.add(line);
        }
        for (int i = 1; i < GameState.SIZE; i++) {
            Line line = graphicEntityModule.createLine();
            line.setX(192 * i);
            line.setY(0 + 24);
            line.setX2(192 * i);
            line.setY2(960 + 24);
            line.setLineWidth(2);
            line.setLineColor(0x000000);
            line.setZIndex(4);
            group.add(line);
        }
    }

    private void initSquares() {
        {
            checkSquare = graphicEntityModule.createRectangle();
            checkSquare.setWidth(192);
            checkSquare.setHeight(192);
            checkSquare.setAlpha(0);
            checkSquare.setFillColor(0xff0000);
            checkSquare.setZIndex(4);
            group.add(checkSquare);
        }

        columns = new Rectangle[GameState.SIZE];
        for (int i=0; i < 5; i++) {
            Rectangle square = graphicEntityModule.createRectangle();
            square.setY(24);
            square.setX(i * 192);
            square.setWidth(192);
            square.setHeight(960);
            square.setAlpha(0);
            square.setFillColor(0xffff00);
            square.setZIndex(2);
            columns[i] = square;
            group.add(square);
        }
        squares = new Rectangle[GameState.SIZE][GameState.SIZE];
        for (int row=0; row < GameState.SIZE; row++) {
            for (int col=0; col < GameState.SIZE; col++) {
                Rectangle square = graphicEntityModule.createRectangle();
                square.setY(row * 192 + 24);
                square.setX(col * 192);
                square.setWidth(192);
                square.setHeight(192);
                square.setAlpha(0);
                square.setFillColor(0x00ff00);
                square.setZIndex(3);
                squares[4-row][col] = square;
                group.add(square);
            }
        }
    }

    private void initCircles() {
        int[][] pts = new int[][] {
                { 192, 192 },
                { 768, 192 },
                { 192, 768 },
                { 768, 768 }
        };
        for (int[] pt : pts) {
            group.add(graphicEntityModule.createCircle()
                    .setX(pt[0])
                    .setY(pt[1] + 24)
                    .setRadius(8)
                    .setFillColor(0x000000)
                    .setZIndex(5)
            );
        }
    }

    private void initPieces() {
        unusedPieces = new ArrayList<>();
        usedPieces = new ArrayList<>();
        for (int i=0; i < 2; i++) {
            List<PieceSprite> unusedPiecesRow = new ArrayList<>();
            for (Piece piece : Piece.values()) {
                for (int k=0; k < (piece == Piece.KING ? 1 : 2); k++) {
                    Sprite sprite = graphicEntityModule.createSprite();
                    sprite.setBaseWidth(192);
                    sprite.setBaseHeight(192);
                    sprite.setAlpha(0.0f, Curve.IMMEDIATE);
                    sprite.setImage(getImage(piece, i));
                    sprite.setZIndex(6);
                    unusedPiecesRow.add(new PieceSprite(piece, sprite));
                    group.add(sprite);
                }
            }
            unusedPieces.add(unusedPiecesRow);
            usedPieces.add(new ArrayList<>());
        }
        updatePieces();
    }

    private String getImage(Piece piece, int player) {
        if (piece == null) {
            return "empty.png";
        }
        if (player == Game.FIRST_PLAYER) {
            switch (piece) {
                case PAWN: return "pawn1.png";
                case PROMOTED_PAWN: return "ppawn1.png";
                case ROOK: return "rook1.png";
                case PROMOTED_ROOK: return "prook1.png";
                case BISHOP: return "bishop1.png";
                case PROMOTED_BISHOP: return "pbishop1.png";
                case SILVER: return "silver1.png";
                case PROMOTED_SILVER: return "psilver1.png";
                case GOLD: return "gold1.png";
                case KING: return "king1.png";
            }
        } else {
            switch (piece) {
                case PAWN: return "pawn2.png";
                case PROMOTED_PAWN: return "ppawn2.png";
                case ROOK: return "rook2.png";
                case PROMOTED_ROOK: return "prook2.png";
                case BISHOP: return "bishop2.png";
                case PROMOTED_BISHOP: return "pbishop2.png";
                case SILVER: return "silver2.png";
                case PROMOTED_SILVER: return "psilver2.png";
                case GOLD: return "gold2.png";
                case KING: return "king2.png";
            }
        }
        return "empty.png";
    }

    private void initTexts() {
        for (int i=0; i < GameState.SIZE; i++) {
            Text text = graphicEntityModule.createText();
            text.setFontFamily("Verdana");
            text.setFontSize(20);
            text.setFillColor(0xe0e0e0);
            text.setText(String.valueOf(5-i));
            text.setX(96 + 192 * i);
            text.setY(0);
            text.setZIndex(10);
            group.add(text);
        }
        for (int i=0; i < GameState.SIZE; i++) {
            Text text = graphicEntityModule.createText();
            text.setFontFamily("Verdana");
            text.setFontSize(20);
            text.setFillColor(0xe0e0e0);
            text.setText(String.valueOf(i+1));
            text.setX(960 + 10);
            text.setY(96 + 192 * i);
            text.setZIndex(10);
            group.add(text);
        }
        sfenText = graphicEntityModule.createText();
        sfenText.setFontFamily("Verdana");
        sfenText.setFontSize(32);
        sfenText.setFillColor(0xe0e0e0);
        sfenText.setX(480);
        sfenText.setY(-50);
        sfenText.setZIndex(10);
        sfenText.setAnchorX(0.5f);
        sfenText.setText(game.gameState.toSFENString());
        toggleModule.displayOnToggleState(sfenText, "sfenToggle", true);
        group.add(sfenText);
    }

    public void updateFrame() {
        updateColumns();
        updateSquares();
        updatePieces();
        sfenText.setText(game.gameState.toSFENString());
        graphicEntityModule.commitEntityState(0, sfenText);
    }

    public void updateColumns() {
        for (int i=0; i < 5; i++) {
            Rectangle column = columns[i];
            int index = game.getLastDiceRoll()-1;
            index = 4 - index; // TODO: or should be the other way
            if (!game.allMovesWereLegal() && index == i) {
                column.setAlpha(0.4f,Curve.IMMEDIATE);
            } else {
                column.setAlpha(0,Curve.IMMEDIATE);
            }
            graphicEntityModule.commitEntityState(1, column);
        }
    }

    private void updateSquares() {
        int currentKingRow = 0;
        int currentKingCol = 0;
        Move move = game.getLastMove();
        for (int row=0; row < GameState.SIZE; row++) {
            for (int col=0; col < GameState.SIZE; col++) {
                if (game.gameState.board[game.gameState.currentPlayer][row][col] == Piece.KING) {
                    currentKingRow = row;
                    currentKingCol = col;
                }
                Rectangle square = squares[4-row][col];
                if (move == null) {
                    square.setAlpha(0, Curve.IMMEDIATE);
                } else {
                    if (move.colFrom != -1) {
                        if (4-row == move.rowFrom && col == move.colFrom) {
                            square.setAlpha(0.25f, Curve.IMMEDIATE);
                        } else if (4-row == move.rowTo && col == move.colTo) {
                            square.setAlpha(0.25f, Curve.IMMEDIATE);
                        } else {
                            square.setAlpha(0, Curve.IMMEDIATE);
                        }
                    } else {
                        if (4-row == move.rowTo && col == move.colTo) {
                            square.setAlpha(0.25f, Curve.IMMEDIATE);
                        } else {
                            square.setAlpha(0, Curve.IMMEDIATE);
                        }
                    }
                }
            }
        }
        boolean isKingInCheck = game.gameState.isKingInCheck();
        if (isKingInCheck) {
            checkSquare.setAlpha(0.75f, Curve.IMMEDIATE);
            checkSquare.setX(192 * currentKingCol, Curve.IMMEDIATE);
            checkSquare.setY(24 + 192 * (4-currentKingRow), Curve.IMMEDIATE);
        } else {
            checkSquare.setAlpha(0.0f, Curve.IMMEDIATE);
        }
    }

    private void updatePieces() {
        for (int i=0; i < 2; i++) {
            for (PieceSprite pieceSprite : usedPieces.get(i)) {
                pieceSprite.sprite.setAlpha(0.0f, Curve.IMMEDIATE);
            }
            unusedPieces.get(i).addAll(usedPieces.get(i));
            usedPieces.get(i).clear();
        }
        for (int row=0; row < GameState.SIZE; row++) {
            for (int col=0; col < GameState.SIZE; col++) {
                int player = 0;
                Piece piece = game.gameState.board[Game.FIRST_PLAYER][4-row][col];
                if (piece == null) {
                    player = 1;
                    piece = game.gameState.board[Game.SECOND_PLAYER][4-row][col];
                }
                if (piece != null) {
                    for (PieceSprite pieceSprite : unusedPieces.get(player)) {
                        if (pieceSprite.piece == piece) {
                            Sprite sprite = pieceSprite.sprite;
                            sprite.setX(col * 192,Curve.IMMEDIATE);
                            sprite.setY(row * 192 + 24,Curve.IMMEDIATE);
                            sprite.setAlpha(1.0f,Curve.IMMEDIATE);
                            usedPieces.get(player).add(pieceSprite);
                            unusedPieces.get(player).remove(pieceSprite);
                            break;
                        }
                    }
                }
            }
        }
    }
}
