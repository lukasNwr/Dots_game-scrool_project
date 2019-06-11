package sk.tuke.gamestudio.game.dots.novorolnik.core;

import java.util.Random;
import java.util.Formatter;


public class Field {
    private final int rowCount;
    private final int colCount;
    private GameState gameState;
    private final Tile[][] tiles;
    private Tile markedTile;
    private int moves;
    private int scoreValue;

    public Field(int rowCount, int colCount, int moves) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.gameState = GameState.PLAYING;
        this.tiles = new Tile[rowCount][colCount];
        this.markedTile = null;
        this.moves = moves;
        generate();
    }

    private Tile generateTile() {
        Random random = new Random();
        int randomColor = random.nextInt(4);

        switch (randomColor) {
            case 0:
                return new BlueTile();
            case 1:
                return new RedTile();
            case 2:
                return new YellowTile();
            case 3:
                return new GreenTile();

                default: return null;
        }
    }

    public void generate() {
        for(int row = 0; row < getRowCount(); row++) {
            for(int col = 0; col < getColCount(); col++) {
                if(tiles[row][col] == null) {
                    tiles[row][col] = generateTile();
                }
            }
        }
    }

    private void checkAndDestroy() {
        for(int row = 0; row < rowCount; row++) {
            for(int col = 0; col < colCount; col++) {
                if(tiles[row][col].isMarked()) {
                    scoreValue++;
                    tiles[row][col] = null;
                }
            }
        }
    }

    private void refill() {
        for(int row = 0; row < rowCount; row++) {
            for(int col = 0; col < colCount; col++) {
                if(tiles[row][col] == null) tiles[row][col] = generateTile();
            }
        }
    }

    private void shiftTiles() {
        for(int row = rowCount-1; row >= 0; row--) {
            for(int col = 0; col < colCount; col++) {
                if(tiles[row][col] == null) {
                    for(int row2 = row; row2 >= 0; row2--) {
                        if(tiles[row2][col] != null) {
                            tiles[row][col] = tiles[row2][col];
                            tiles[row2][col] = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    public void finish() {
        checkCombo();
        countScore(scoreValue);
        checkAndDestroy();
        shiftTiles();
        refill();
        moves--;
        if(moves <= 0) {
            gameState = GameState.END;
        }
    }

    public void markTile(int row, int col) {
        if(gameState == GameState.PLAYING) {
            final Tile tile = tiles[row][col];
            if(markedTile == null) markedTile = tile;
            if(!tile.isMarked()) {
                if (tile.getClass().equals(markedTile.getClass()) && checkNeightbor(row, col)) {
                    tile.setMarkedState(true);
                    markedTile = tile;
                } else {
                    tile.setMarkedState(false);
                }
            }
        }
    }

    public void resetTile(int row, int col) {
        if(tiles[row][col].isMarked()) {
            tiles[row][col].setMarkedState(false);
        }
    }

    private boolean isMarkedTile(int row, int col) {
        return row >= 0 && row < rowCount && col >=0 && col < colCount
                && tiles[row][col] == markedTile;

    }

    private boolean checkNeightbor(int row, int col) {
        if(isMarkedTile(row+1, col)) return true;
        else if(isMarkedTile(row-1, col)) return true;
        else if(isMarkedTile(row, col+1)) return true;
        else if(isMarkedTile(row, col-1)) return true;
        else if(isMarkedTile(row, col)) return true;
        else return false;
    }

    private void performCombo(int row, int col) {
        Tile tile = tiles[row][col];
        for(int rowIdx = 0; rowIdx < rowCount; rowIdx++) {
            for (int colIdx = 0; colIdx < colCount; colIdx++) {
                if(tiles[rowIdx][colIdx].getClass().equals(tile.getClass())) {
                    tiles[rowIdx][colIdx].setMarkedState(true);
                }
            }
        }
    }

    public void checkCombo() {
        for(int row = 0; row < rowCount-1; row++) {
            for(int col = 0; col < colCount-1; col++) {
                if(tiles[row][col].isMarked() && tiles[row][col+1].isMarked()) {
                    if(tiles[row+1][col].isMarked() && tiles[row+1][col+1].isMarked()) {
                        System.out.println("performihg combo!");
                        performCombo(row, col);
                    }
                }
            }
        }
    }

    public int countScore(int scoreValue) {
        for(int row = 0; row < rowCount; row ++) {
            for(int col = 0; col < colCount; col++) {
                if(tiles[row][col].isMarked()) {
                    scoreValue++;
                }
            }
        }
        if(scoreValue == 1) scoreValue = 0;
        return scoreValue;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void resetMarkedTile() {
        markedTile = null;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public int getMoves() {
        return this.moves;
    }

    @Override
    public String toString() {
        Formatter sb = new Formatter();
//        String s = "";
        for (int row = 0; row < rowCount; row++) {
            sb.format("%3s", (char)(row + 'A'));
//            s += (char)(row + 'A');
            for (int column = 0; column < colCount; column++) {
                final Tile tile = getTile(row, column);
                sb.format("%3s", tile);
            }
            sb.format("%n");
        }
        return sb.toString();
    }

}
