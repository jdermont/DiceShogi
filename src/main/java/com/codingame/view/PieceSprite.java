package com.codingame.view;

import com.codingame.game.Piece;
import com.codingame.gameengine.module.entities.Sprite;

public class PieceSprite {
    public Piece piece;
    public Sprite sprite1;
    public Sprite sprite2;

    public PieceSprite(Piece piece, Sprite sprite1, Sprite sprite2) {
        this.piece = piece;
        this.sprite1 = sprite1;
        this.sprite2 = sprite2;
    }
}