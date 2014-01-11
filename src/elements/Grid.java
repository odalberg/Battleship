package elements;

import java.awt.*;

public class Grid {

    public Rectangle rect;
    public Boolean hideShip = false;

    private Boolean ship = false;
    private Boolean attacked = false;
    private Graphics g;

    private Color SHIP = new Color(24, 67, 81, 249);
    private Color HIT = new Color(156, 1, 0, 255);
    private Color MISS = new Color(246, 241, 246, 255);
    private Color EMPTY = new Color(64, 165, 197, 255);

    public void setShip(Boolean ship) {
        this.ship = ship;
        updateColor();
    }

    public void setAttacked(Boolean attacked) {
        this.attacked = attacked;
        updateColor();
    }

    private void setColor(Color co) {
        if (this.g == null) return;
        this.g.setColor(co);
        this.g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Constructor for Grid
     *
     * @param rect rectangle object http://docs.oracle.com/javase/tutorial/java/javaOO/objectcreation.html
     * @param hide to define if ship is hidden or not
     */
    public Grid(Rectangle rect, Boolean hide) {
        this.rect = rect;
        this.hideShip = hide;
    }

    public void paint(Graphics g) {
        this.g = g;
        updateColor();
    }

    /**
     * To define and update colors of the Grid
     */
    private void updateColor() {
        if (!this.attacked && this.ship && !hideShip)
            setColor(SHIP);
        else if (this.attacked && this.ship)
            setColor(HIT);
        else if (this.attacked)
            setColor(MISS);
        else
            setColor(EMPTY);
    }

    /**
     * For validation and coloring
     *
     * @return if part of grid is attacked or not
     */
    public Boolean isAttacked() {
        return attacked;
    }

    /**
     * For validation and coloring
     *
     * @return if part of grid already has ship on it or not
     */
    public Boolean hasShip() {
        return ship;
    }
}