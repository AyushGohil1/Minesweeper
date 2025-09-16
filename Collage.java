import java.awt.Color;
import java.awt.Font;

/**
 * Uses libraries and content from the Introduction to Computer Science book.   
 * Displays board interactively through rendering images with StdDraw.
 * @author Jeremy Hui
 **/
public class Collage {
    private static boolean UTF = true; // Change this to false if your computer does not support UTF-8 characters
    private static String FLAGGEDSYMBOL = UTF ? "⚑" : "F";
    private static String MINESYMBOL = UTF ? "✸" : "X";
    
    private int n;
    private int m;
    private Font font;
    private boolean flagOn;
    private static final Color[] COLORS = {Color.BLACK, Color.WHITE, new Color(25, 118, 209), 
        new Color(56, 142, 60), Color.RED, new Color(124, 32, 161), new Color(239, 149, 46), 
        new Color(0, 153, 171), new Color(64, 64, 64), new Color(163, 163, 163)};

    /**
     * Board collage constructor with default values.
     */
    public Collage() {
        this(4, 4, false);
    }

    /**
     * Board collage constructor with an m x n size.
     * 
     * @param size the number of rows and columns in the board
     */
    public Collage(int size, int ySize, boolean flagOn) {
        n = size + 1;
        m = ySize;
        StdDraw.setCanvasSize(700 / Math.max(n, m) * m, 700 / Math.max(n, m) * n);
        StdDraw.setXscale(0, m);
        StdDraw.setYscale(0, n);
        font = new Font("Arial", Font.BOLD, 400 / Math.max(n, m));
        this.flagOn = flagOn;
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Sets the flag to be the given flagOn value.
     * @param flagOn the given flagOn value
     */
    public void setFlagOn(boolean flagOn) {
        this.flagOn = flagOn;
    }

    /**
     * Sets the canvas size to be a viewable size using
     * the n and m values.
     */
    public void setCanvasSize() {
        StdDraw.setCanvasSize(700 / Math.max(n, m) * m, 700 / Math.max(n, m) * n);
    }

    /**
     * Updates and refreshes the given PlayMinesweeper object
     * and current time.
     * @param play the given PlayMinesweeper object
     * @param currTime the given time
     */
    public void updateBoard(PlayMinesweeper play, int currTime) {
        Square[][] grid = play.getGrid();
        StdDraw.clear();
        StdDraw.setXscale(0, m);
        StdDraw.setYscale(0, n);
        StdDraw.setPenRadius();
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.filledRectangle(m / 2.0, (n - 1) + .5, m / 2.0, 0.5);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.rectangle(m / 2.0, (n - 1) + .5, m / 2.0, 0.5);
        StdDraw.rectangle(1, (n - 1) + .5, 1, 0.5);
        StdDraw.rectangle(m - 1, (n - 1) + .5, 1, 0.5);
        for (int j = 0; j < grid[0].length; j++) {
            double centerX = j + .5;
            double centerY = (n - 1) + .5;
            StdDraw.setFont(font);
            if (j == 0) {
                if (flagOn) StdDraw.setPenColor(Color.RED);
                else StdDraw.setPenColor(Color.BLACK);
                StdDraw.text(centerX, centerY, FLAGGEDSYMBOL);
                StdDraw.setPenColor(Color.BLACK);
            } else if (j == 1)
                StdDraw.text(centerX, centerY, Integer.toString(play.getFlagCount()));
            else if (j == grid[0].length - 2)
                StdDraw.text(centerX, centerY, "T");
            else if (j == grid[0].length - 1)
                StdDraw.text(centerX, centerY, Integer.toString(currTime));
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                replaceTile(i + 1, j, grid[i][j]);
            }
        }
        StdDraw.show();
    }

    /**
     * Replaces and draws tiles that need replacing.
     * 
     * @param collageCol   the column on collage's axis
     * @param collageRow   the row on collage's axis
     * @param numToReplace the value of the tile
     */
    public void replaceTile(int collageCol, int collageRow, Square sqToReplace) {
        double centerX = collageRow + .5;
        double centerY = ((n - 1) - collageCol) + .5;
        StdDraw.setPenRadius();
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.square(centerX, centerY, 0.5);
        switch (sqToReplace.getSqState()) {
            case CLOSED:
                StdDraw.setPenColor(Color.LIGHT_GRAY);
                StdDraw.filledSquare(centerX, centerY, 0.5);
                break;
            case OPEN:
                if (sqToReplace.getSqNum() == -1) StdDraw.setPenColor(Color.RED);
                else StdDraw.setPenColor(Color.WHITE);
                StdDraw.filledSquare(centerX, centerY, 0.5);
                StdDraw.setPenColor(COLORS[sqToReplace.getSqNum() + 1]);
                StdDraw.setFont(font);
                if (sqToReplace.getSqNum() == -1) StdDraw.text(centerX, centerY, MINESYMBOL);
                else if (sqToReplace.getSqNum() > 0) StdDraw.text(centerX, centerY, Integer.toString(sqToReplace.getSqNum()));   
                break;             
            case FLAGGED:
                StdDraw.setPenColor(Color.LIGHT_GRAY);
                StdDraw.filledSquare(centerX, centerY, 0.5);
                StdDraw.setPenColor(Color.RED);
                StdDraw.setFont(font);
                StdDraw.text(centerX, centerY, FLAGGEDSYMBOL);
                break;
        }

    }
    
}
