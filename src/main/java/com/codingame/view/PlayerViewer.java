package com.codingame.view;

import com.codingame.game.Game;
import com.codingame.game.Piece;
import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.toggle.ToggleModule;

import java.util.ArrayList;
import java.util.List;

public class PlayerViewer {
    private GraphicEntityModule graphicEntityModule;
    private ToggleModule toggleModule;
    private Group[] groups;
    private RoundedRectangle[] backgrounds;
    private Text[] actionMessages;
    private Text[] messages;
    private List<List<Sprite>> hands;
    private List<List<Sprite>> westernHands;

    private Game game;
    private Player[] players;

    public PlayerViewer(Game game, GraphicEntityModule graphicEntityModule, ToggleModule toggleModule, Player player1, Player player2) {
        this.game = game;
        this.graphicEntityModule = graphicEntityModule;
        this.toggleModule = toggleModule;
        this.players = new Player[] { player1, player2 };
    }

    public void init() {
        groups = new Group[2];
        backgrounds = new RoundedRectangle[2];
        for (int i=0; i < 2; i++) {
            Group group = graphicEntityModule.createGroup();
            group.setX(24 + 1448 * i);
            group.setY(60 + 284 * (1-i));
            RoundedRectangle background = graphicEntityModule.createRoundedRectangle();
            background.setX(0);
            background.setY(0);
            background.setWidth(400);
            background.setHeight(700);
            background.setLineWidth(4);
            background.setFillColor(0xfbb34d);
            background.setLineColor(0x000000);
            background.setZIndex(1);
            group.add(background);
            groups[i] = group;
            backgrounds[i] = background;
        }

        initPlayerAvatars();
        initTexts();
        initHands();
    }

    private void initPlayerAvatars() {
        for (int i=0; i < 2; i++) {
            Rectangle square = graphicEntityModule.createRectangle()
                    .setX(104)
                    .setY(24)
                    .setZIndex(2)
                    .setWidth(192)
                    .setHeight(192)
                    .setAlpha(0.1f)
                    .setFillColor(0x000000);
            groups[i].add(square);

            Sprite avatar = graphicEntityModule.createSprite()
                    .setX(104)
                    .setY(24)
                    .setZIndex(3)
                    .setImage(players[i].getAvatarToken())
                    .setBaseHeight(192)
                    .setBaseWidth(192);
            groups[i].add(avatar);

            String nickname = players[i].getNicknameToken();
            Text text = graphicEntityModule.createText()
                    .setX(200)
                    .setY(234)
                    .setAnchorX(0.5f)
                    .setZIndex(3)
                    .setFontSize(32)
                    .setFontFamily("Verdana")
                    .setText(nickname)
                    .setFillColor(i == 0 ? 0xff1d5c : 0x22a1e4)
                    .setMaxWidth(380);
            groups[i].add(text);
        }
    }

    private void initTexts() {
        actionMessages = new Text[2];
        for (int i=0; i < 2; i++) {
            Text message = graphicEntityModule.createText()
                    .setX(200)
                    .setY(280)
                    .setAnchorX(0.5f)
                    .setZIndex(3)
                    .setFontSize(40)
                    .setFontFamily("Verdana")
                    .setFontWeight(Text.FontWeight.BOLD)
                    .setText("");
            actionMessages[i] = message;
            groups[i].add(message);
        }

        messages = new Text[2];
        for (int i=0; i < 2; i++) {
            Text message = graphicEntityModule.createText()
                    .setX(200)
                    .setY(340)
                    .setAnchorX(0.5f)
                    .setZIndex(3)
                    .setFontSize(28)
                    .setFontFamily("Verdana")
                    .setText("")
                    .setMaxWidth(380);
            messages[i] = message;
            groups[i].add(message);
        }
    }

    private void initHands() {
        hands = new ArrayList<>();
        westernHands = new ArrayList<>();
        for (int i=0; i < 2; i++) {
            List<Sprite> hand = new ArrayList<>();
            for (int j=0; j < 10; j++) {
                Sprite sprite = graphicEntityModule.createSprite();
                sprite.setImage(getImage(null, i, false));
                sprite.setBaseWidth(96);
                sprite.setBaseHeight(96);
                sprite.setX(96 * (j%4));
                sprite.setY(380 + 96 * (j/4));
                sprite.setZIndex(3);
                toggleModule.displayOnToggleState(sprite, "westernToggle", false);
                hand.add(sprite);
                groups[i].add(sprite);
            }
            hands.add(hand);
            List<Sprite> westernHand = new ArrayList<>();
            for (int j=0; j < 10; j++) {
                Sprite sprite = graphicEntityModule.createSprite();
                sprite.setImage(getImage(null, i, true));
                sprite.setBaseWidth(96);
                sprite.setBaseHeight(96);
                sprite.setX(96 * (j%4));
                sprite.setY(380 + 96 * (j/4));
                sprite.setZIndex(3);
                toggleModule.displayOnToggleState(sprite, "westernToggle", true);
                westernHand.add(sprite);
                groups[i].add(sprite);
            }
            westernHands.add(westernHand);
        }
    }

    private String getImage(Piece piece, int player, boolean western) {
        if (piece == null) {
            return "empty.png";
        }
        if (western) {
            if (player == Game.FIRST_PLAYER) {
                switch (piece) {
                    case PAWN: return "wpawn1.png";
                    case PROMOTED_PAWN: return "wppawn1.png";
                    case ROOK: return "wrook1.png";
                    case PROMOTED_ROOK: return "wprook1.png";
                    case BISHOP: return "wbishop1.png";
                    case PROMOTED_BISHOP: return "wpbishop1.png";
                    case SILVER: return "wsilver1.png";
                    case PROMOTED_SILVER: return "wpsilver1.png";
                    case GOLD: return "wgold1.png";
                    case KING: return "wking1.png";
                }
            } else {
                switch (piece) {
                    case PAWN: return "wpawn2.png";
                    case PROMOTED_PAWN: return "wppawn2.png";
                    case ROOK: return "wrook2.png";
                    case PROMOTED_ROOK: return "wprook2.png";
                    case BISHOP: return "wbishop2.png";
                    case PROMOTED_BISHOP: return "wpbishop2.png";
                    case SILVER: return "wsilver2.png";
                    case PROMOTED_SILVER: return "wpsilver2.png";
                    case GOLD: return "wgold2.png";
                    case KING: return "wking2.png";
                }
            }
        } else {
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
        }
        return "empty.png";
    }

    public void updateFrame() {
        int player = game.gameState.currentPlayer;
        for (int i=0; i < 2; i++) {
            if (i != player) {
                backgrounds[i].setLineColor(0xffffff,Curve.IMMEDIATE);
                groups[i].setAlpha(1.0f, Curve.EASE_OUT);
            } else {
                backgrounds[i].setLineColor(0x000000,Curve.IMMEDIATE);
                groups[i].setAlpha(0.75f, Curve.EASE_OUT);
            }
        }
        updateActions();
        updateMessages();
        updateHands();
    }

    public void fadePlayer(int player) {
        backgrounds[player].setLineColor(0x000000,Curve.IMMEDIATE);
        groups[player].setAlpha(0.5f, Curve.EASE_OUT);
    }

    public void unfadePlayer(int player) {
        backgrounds[player].setLineColor(0xffffff,Curve.IMMEDIATE);
        groups[player].setAlpha(1.0f, Curve.EASE_OUT);
    }

    public void updateActions() {
        for (int i=0; i < 2; i++) {
            Text actionMessage = actionMessages[i];
            actionMessage.setText(players[i].lastMove == null ? "" : players[i].lastMove.toString());
            graphicEntityModule.commitEntityState(0, actionMessage);
        }
    }

    public void updateMessages() {
        for (int i=0; i < 2; i++) {
            Text message = messages[i];
            message.setText(players[i].lastMessage == null ? "" : players[i].lastMessage);
            graphicEntityModule.commitEntityState(0, message);
        }
    }

    public void updateHands() {
        for (int i=0; i < 2; i++) {
            List<Piece> playerHand = game.gameState.hands.get(i);
            for (int j=0; j < playerHand.size(); j++) {
                Sprite sprite = hands.get(i).get(j);
                sprite.setImage(getImage(playerHand.get(j), i, false));
                graphicEntityModule.commitEntityState(0, sprite);
            }
            for (int j=playerHand.size(); j < 10; j++) {
                Sprite sprite = hands.get(i).get(j);
                sprite.setImage(getImage(null, i, false));
                graphicEntityModule.commitEntityState(0, sprite);
            }
            for (int j=0; j < playerHand.size(); j++) {
                Sprite sprite = westernHands.get(i).get(j);
                sprite.setImage(getImage(playerHand.get(j), i, true));
                graphicEntityModule.commitEntityState(0, sprite);
            }
            for (int j=playerHand.size(); j < 10; j++) {
                Sprite sprite = westernHands.get(i).get(j);
                sprite.setImage(getImage(null, i, true));
                graphicEntityModule.commitEntityState(0, sprite);
            }
        }
    }
}
