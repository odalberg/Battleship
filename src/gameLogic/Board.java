package gameLogic;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import elements.Grid;
import elements.Ship;
import configuration.BoardInfo;
import configuration.PlaceShipInfo;

public class Board {

    private Font fontBName;
    private Color COLORBNAME = new Color(24, 67, 81, 249);
    private Color MYTURN = new Color(253, 255, 242, 255);
    private Color NOTMYTURN = new Color(93, 93, 93, 255);

    public BoardInfo bInfo;
    public Boolean canContinue = false;
    public Boolean gameOver = false;
    public Boolean hideShip = false;
    private int size = 0;

    private Graphics g;

    public Map<String, Grid> boardMap;
    public ArrayList<Ship> shipList = new ArrayList<Ship>();
    public ArrayList<Grid> aIList = new ArrayList<Grid>();
    public ArrayList<Grid> gridShipList = new ArrayList<Grid>();

    public Rectangle bRect = new Rectangle();


    public Board(BoardInfo Info) {
        this.bInfo = Info;
    }

    public void create(Boolean h) {

        this.hideShip = h;
        this.gameOver = false;
        this.canContinue = false;
        shipList.add(Ship.SHIP1);
        shipList.add(Ship.SHIP2);
        shipList.add(Ship.SHIP3);
        shipList.add(Ship.SHIP4);

        bRect.x = bInfo.x;
        bRect.y = bInfo.y;
        bRect.width = bInfo.gridWidth * bInfo.rowCount + bInfo.boarder * (bInfo.rowCount + 1);
        bRect.height = bInfo.gridHeight * bInfo.columnCount + bInfo.boarder * (bInfo.columnCount + 1);

        boardMap = new Hashtable<String, Grid>();

        for (int i = 0; i < bInfo.rowCount; i++) {
            for (int j = 0; j < bInfo.columnCount; j++) {
                Rectangle rect = new Rectangle();
                rect.x = ((i + 1) * bInfo.boarder) + bInfo.x + (i * bInfo.gridWidth);
                rect.y = ((j + 1) * bInfo.boarder) + bInfo.y + (j * bInfo.gridHeight);
                rect.width = bInfo.gridWidth;
                rect.height = bInfo.gridHeight;
                Grid gd = new Grid(rect, hideShip);
                boardMap.put(i + " - " + j, gd);
                aIList.add(gd);
            }
        }
        size = aIList.size() - 1;
        placeShips();

        setTurnColor(false);
    }


    public void paint(Graphics g) {

        this.g = g;
        for (Grid gr : boardMap.values()) {
            gr.paint(g);
        }

        fontBName = new Font("Segoe Print", Font.ITALIC, 20);
        g.setFont(fontBName);
        g.setColor(COLORBNAME);
        g.drawString(bInfo.boardName
                , bRect.x
                , bRect.y + bRect.height + bInfo.gridHeight);
    }

    public void setTurnColor(Boolean mt) {
        if (this.g == null) return;
        if (mt)
            g.setColor(MYTURN);
        else
            g.setColor(NOTMYTURN);

        g.fillRect(bRect.x, bRect.y, bRect.width, bRect.height);
        paint(this.g);
    }

    public boolean isInBoardRect(int x, int y) {
        if (x >= bRect.x && x <= bRect.x + bRect.width &&
                y >= bRect.y && y <= bRect.y + bRect.height) {
            return true;
        }
        return false;
    }

    public void validateResult(int x, int y) {
        Grid g = getGridByPoint(x, y);
        validateGrid(g);
    }

    private void validateGrid(Grid g) {
        if (!g.isAttacked()) {
            g.setAttacked(true);
            canContinue = g.hasShip();
            checkResult();
        } else {
            canContinue = true;
        }

    }

    private Grid getGridByPoint(int x, int y) {
        String key = getRowByPos(x) + " - " + getColumnByPos(y);
        // System.out.print("\n" + key);
        return boardMap.get(key);
    }

    private int getRowByPos(int p) {
        return (p - bInfo.x) / (bInfo.gridWidth + bInfo.boarder);
    }

    private int getColumnByPos(int p) {
        return (p - bInfo.y) / (bInfo.gridHeight + bInfo.boarder);
    }

    private PlaceShipInfo getPlaceShipInfo(Ship s) {
        PlaceShipInfo k = new PlaceShipInfo();
        int r, c;
        Boolean got = false;
        do {
            int rMax = bInfo.rowCount - 1;
            r = suvalineArv(0, rMax);
            int cMax = bInfo.columnCount - 1;
            c = suvalineArv(0, cMax);
            switch (s.getDirect()) {
                case 1:
                    k.startIndex = r;
                    k.endIndex = r + s.getLength();
                    k.direction = 1;
                    k.index = c;
                    if (r + s.getLength() <= rMax) {
                        got = !hasShipRRange(r, r + s.getLength(), c);
                    }
                    break;
                case 2:
                    k.startIndex = c;
                    k.endIndex = c + s.getLength();
                    k.direction = 2;
                    k.index = r;
                    if (c + s.getLength() <= cMax) {
                        got = !hasShipCRange(c, c + s.getLength(), r);
                    }
                    break;
            }
        } while (!got);

        return k;
    }

    private boolean hasShipRRange(int r, int rL, int c) {
        Grid g;
        for (int i = r; i <= rL; i++) {
            g = boardMap.get(i + " - " + c);
            if (g.hasShip()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasShipCRange(int c, int cL, int r) {
        Grid g;
        for (int i = c; i <= cL; i++) {
            g = boardMap.get(r + " - " + i);
            if (g.hasShip()) {
                return true;
            }
        }
        return false;
    }

    public void aISelect() {
        int index = suvalineArv(0, size);
        Grid g = aIList.get(index);
        validateGrid(g);
        aIList.remove(index);
        size--;
    }

    /**
     * @param alates beginning of given range
     * @param kuni   end of given range
     * @return Calculates two random integers between given range.
     * Method written in workshop at School
     */
    public static int suvalineArv(int alates, int kuni) {

        int vahemik = kuni - alates + 1;
        return (int) (Math.random() * vahemik + alates);
    }

    private void placeShips() {
        for (int i = 0; i < shipList.size(); i++) {
            Ship item = shipList.get(i);
            PlaceShipInfo pi = getPlaceShipInfo(item);
            String key = "";
            Grid g;
            for (int j = pi.startIndex; j < pi.endIndex; j++) {
                switch (pi.direction) {
                    case 1:
                        key = j + " - " + pi.index;
                        break;
                    case 2:
                        key = pi.index + " - " + j;
                        break;
                }
                g = boardMap.get(key);
                g.setShip(true);
                gridShipList.add(g);
            }
        }
    }

    private void checkResult() {
        Grid gr;
        gameOver = true;
        for (int i = 0; i < gridShipList.size(); i++) {
            gr = gridShipList.get(i);
            if (!gr.isAttacked()) {
                gameOver = false;
                break;
            }
        }
    }
}