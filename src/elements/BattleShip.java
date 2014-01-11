package elements;

import java.applet.Applet;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import configuration.BoardInfo;
import gameLogic.Board;

public class BattleShip extends Applet {

    public Graphics graph;
    private Board playerBoard;
    private Board computerBoard;
    private Boolean playerTurn;
    private Timer timer;

    public void init() {
        setSize(725, 400);
        create();
    }

    /**
     * http://enos.itcollege.ee/~jpoial/docs/tutorial/essential/threads/timer.html
     */
    class RemindTask extends TimerTask {

        /**
         * Logic of the general gameflow Between player and Computer
         */
        public void run() {
            if (!playerTurn) {
                playerBoard.aISelect();
                if (computerBoard.gameOver) {
                    //todo
                } else {
                    if (!playerBoard.canContinue) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        setPlayerTurn(!playerTurn);
                    } else if (!computerBoard.gameOver) {
                        startAI();
                    }
                    updateTurn();
                }
                repaint();
            }
        }
    }

    /**
     * Timer to simulate opponents delay
     * http://enos.itcollege.ee/~jpoial/docs/tutorial/essential/threads/timer.html
     */
    private void startAI() {
        timer = new Timer();
        timer.schedule(new RemindTask(), 1500);
    }

    /**
     * This method defines whose turn it is to shoot
     *
     * @param b to set turn
     */
    private void setPlayerTurn(Boolean b) {
        playerTurn = b;
        if (!playerTurn) {
            startAI();
        }
    }

    /**
     * Change the color of the gametable when turn changes
     */
    private void updateTurn() {
        playerBoard.setTurnColor(!playerTurn);
        computerBoard.setTurnColor(playerTurn);
    }

    /**
     * Logic to define player actions and handle gameover
     *
     * @param x coordinate of the gametable
     * @param y coordinate of the gametable
     */
    private void checkPlayerAction(int x, int y) {
        if (playerTurn) {
            if (computerBoard.isInBoardRect(x, y)) {
                computerBoard.validateResult(x, y);
                if (computerBoard.gameOver) {
                    //todo
                } else {
                    if (!computerBoard.canContinue) {
                        setPlayerTurn(!playerTurn);
                    }
                    updateTurn();
                }
            }
            repaint();
        }
    }

    @Override
    public boolean mouseDown(Event evt, int x, int y) {
        checkPlayerAction(x, y);
        return true;
    }

    /**
     * Create gametables, set position they in applet,
     */
    public void create() {
        BoardInfo playerInfo = new BoardInfo();
        playerInfo.x = 50;
        playerInfo.y = 50;
        playerInfo.boardName = "PLAYER";
        playerBoard = new Board(playerInfo);
        playerBoard.create(false);

        BoardInfo computerInfo = new BoardInfo();
        computerInfo.x = 400;
        computerInfo.y = 50;
        computerInfo.boardName = "COMPUTER";
        computerBoard = new Board(computerInfo);
        computerBoard.create(true);

        setPlayerTurn(true);
        updateTurn();
    }

    public void paint(Graphics graph) {
        this.graph = graph;
        playerBoard.paint(graph);
        computerBoard.paint(graph);
        updateTurn();
    }
}


