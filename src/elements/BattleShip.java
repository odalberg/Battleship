package elements;

import java.applet.Applet;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import configuration.BoardInfo;
import gameLogic.Board;

public class BattleShip extends Applet {

    private Graphics graph;
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

        @Override
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

    // Kelle kord on
    private void setPlayerTurn(Boolean b) {
        playerTurn = b;
        if (!playerTurn) {
            startAI();
        }
    }

    //Kummal laual midagi teha saab
    private void updateTurn() {
        playerBoard.setTurnColor(!playerTurn);
        computerBoard.setTurnColor(playerTurn);
    }


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

    //Laudade loomine
    public void create() {
        BoardInfo pInfo = new BoardInfo();
        pInfo.x = 50;
        pInfo.y = 50;
        pInfo.boardName = "PLAYER";
        playerBoard = new Board(pInfo);
        playerBoard.create(false);

        BoardInfo cInfo = new BoardInfo();
        cInfo.x = 400;
        cInfo.y = 50;
        cInfo.boardName = "COMPUTER";
        computerBoard = new Board(cInfo);
        computerBoard.create(true);

        setPlayerTurn(true);
        updateTurn();
    }

    //Joonista reaalselt
    public void paint(Graphics graph) {
        this.graph = graph;
        playerBoard.paint(graph);
        computerBoard.paint(graph);
        updateTurn();
    }
}


