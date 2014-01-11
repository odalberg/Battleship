package elements;

public class Ship {

    private int direct = 1;
    private int length = 1;

    public int getDirect() {
        return direct;
    }

    public int getLength() {
        return length;
    }

    /**
     * Constructor for Ship
     *
     * @param len defines the length of the ship
     * @param dir defines which direction ship is placed
     *            from left to right(1) or up do down(2)
     */
    public Ship(int len, int dir) {
        this.direct = dir;
        this.length = len;
    }

    public static Ship SHIP1 = new Ship(1, 1);
    public static Ship SHIP2 = new Ship(2, 2);
    public static Ship SHIP3 = new Ship(3, 2);
    public static Ship SHIP4 = new Ship(4, 1);
}
