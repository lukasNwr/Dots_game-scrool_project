package sk.tuke.gamestudio.game.dots.novorolnik.core;

public abstract class Tile {

    private boolean isMarked;

    public Tile() {
        this.isMarked = false;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarkedState(boolean marked) {
        if(marked) this.isMarked = true;
        else this.isMarked = false;
    }

    public boolean isGreen() { return false; }
    public boolean isBlue() { return false; }
    public boolean isYellow() { return false; }
    public boolean isRed() { return false; }

    @Override
    public String toString() {
        return this.isMarked ? "-" : "0";
    }
}
