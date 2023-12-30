package com.codingame.game;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

public class Player extends AbstractMultiplayerPlayer {

    public Move lastMove;
    public String lastMessage;

    @Override
    public int getExpectedOutputLines() {
        return 1;
    }
}
