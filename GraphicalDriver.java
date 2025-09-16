import java.util.*;
import java.awt.*;
import java.io.File;

/**
 * Graphical Driver -- tests students' play minesweeper through an interactive image
 * screen.
 * GraphicalDriver.java is equivalent to TextDriver.java
 * 
 * - Follow the numbered prompts on the StdDraw window to test individual methods.
 * - When you see a board, press any key to test and any key again or click to test another method or quit.
 * - When playing the entire game, click to open tiles and progress through the game. 
 * - Close window or select the quit option in the menu to quit.
 * 
 * @author Jessica de Brito
 * @author Jeremy Hui
 **/
public class GraphicalDriver {
    /**
     * Enables students to test commands interactively; calls methods to obtain
     * methods and play the game.
     **/
    private static PlayMinesweeper play;
    private static Collage collage;
    private static int n = 4;
    private static boolean gridFilled = false;
    private static boolean allowPlayRandom = false;
    private static String difficulty = null;
    private static long start = 0;
    private static int currTime = 0;
    private static int SEED = 2024; // Used by the autograder to ensure the same random values are always generated
    public static void main(String[] args) {
        String[] actions = { "Test individual methods", "Play full game", "Quit" };
        StdDraw.setCanvasSize(700, 700);
        StdDraw.setScale(0, 4);
        StdDraw.enableDoubleBuffering();
        Font font = new Font("Arial", Font.PLAIN, 24);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(font);
        int action = getActionChoice(actions);
        switch (action) {
            case 1: // Test individual methods
                testIndividualMethods();
            break;
            case 2: // Plays full game
                playFullGame();
            break;
        }
        System.exit(0);
    }

    /**
     * Tests individual methods.
     */
    private static void testIndividualMethods() {
        String[] methods = { "placeMines", "fillGrid", "openSquare", "placeFlag", 
            "checkWinCondition", "chooseDifficulty", "playRandom", "quit" };

        while (true) {
            int method = getMethodChoice(methods);
            if (method == methods.length) break;
            testMethod(method);
        }
    }

    /**
     * Obtains and displays the action to take after student tests a method,
     * allowing students to choose the action.
     * 
     * @param actions all possible actions to take
     * @return the number of the control action choice to take
     * 
     **/
    private static int getActionChoice(String[] actions) {
        StdDraw.clear();
        while (StdDraw.hasNextKeyTyped())
            StdDraw.nextKeyTyped();

        ArrayList<String> prompt = new ArrayList<>();
        prompt.add("What do you want to do?");

        for (int i = 0; i < actions.length; i++)
            prompt.add(String.format("%d. %s", i + 1, actions[i]));

        for (int line = 0; line < actions.length + 1; line++)
            StdDraw.textLeft(0.5, 3.6 - 0.3 * line, prompt.get(line));

        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if ('1' <= key && key <= '3')
                    return key - '0';
            }
            StdDraw.pause(20);
        }
    }

    /**
     * Obtains and displays the methods that can be tested, allowing students to
     * choose a method.
     * 
     * @param methods all possible methods to test
     * @return the number of the method to test
     * 
     **/
    private static int getMethodChoice(String[] methods) {
        Font font = new Font("Arial", Font.PLAIN, 24);
        StdDraw.setFont(font);
        StdDraw.clear();

        ArrayList<String> prompt = new ArrayList<>();
        prompt.add("What method would you like to test?");

        for (int i = 0; i < methods.length; i++)
            prompt.add(String.format("%d. %s", i + 1, methods[i]));

        for (int line = 0; line < prompt.size(); line++)
            StdDraw.textLeft(0.5, 3.6 - 0.3 * line, prompt.get(line));

        StdDraw.show();
        boolean warningGiven = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (play == null && ('2' <= key && key <= '5' || key == '7')) {
                    if (!warningGiven) {
                        StdDraw.setPenColor(Color.RED);
                        StdDraw.textLeft(0.5, 3.6 - 0.3 * prompt.size(), "Please setup the board first!");
                        StdDraw.setPenColor();
                        StdDraw.show();
                        warningGiven = true;
                    }
                }
                else if ('3' <= key && key <= '5' && !gridFilled) {
                    if (!warningGiven) {
                        StdDraw.setPenColor(Color.RED);
                        StdDraw.textLeft(0.5, 3.6 - 0.3 * prompt.size(), "Please call fillGrid first!");
                        StdDraw.setPenColor();
                        StdDraw.show();
                        warningGiven = true;
                    }
                }
                else if (key == '7' && !allowPlayRandom) {
                    if (!warningGiven) {
                        StdDraw.setPenColor(Color.RED);
                        StdDraw.textLeft(0.5, 3.6 - 0.3 * prompt.size(), "playRandom has already been called, ");
                        StdDraw.textLeft(0.5, 3.6 - 0.3 * (prompt.size() + 1), "or you used a file to setup the board!");
                        StdDraw.setPenColor();
                        StdDraw.show();
                        warningGiven = true;
                    }
                }
                else if ('1' <= key && key <= '8')
                    return key - '0';
            }

            StdDraw.pause(20);
        }
    }

    /**
     * Obtains all files and lets user pick a file to be tested.
     * 
     * @return the file name to be tested
     **/
    private static String getFileChoice() {
        StdDraw.clear();

        File[] allFiles = new File(".").listFiles();
        ArrayList<String> choices = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();
        choices.add("What file do you want to test on?");
        choices.add("Type only characters and backspace.");
        choices.add("Press enter to submit.");
        for (File f : allFiles) {
            if (f.getName().endsWith(".in")) {
                files.add(f.getName());
            }
        }
        Font font = new Font("Arial", Font.PLAIN, 24);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(font);
        double x = 0.25;
        for (int line = 0; line < choices.size(); line++) {
            StdDraw.textLeft(x, 3.6 - 0.21 * line, choices.get(line));
        }

        StdDraw.show();

        String selection = "";
        char key;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                key = StdDraw.nextKeyTyped();
                if (key == '\n') {
                    if (files.contains(selection)) {
                        font = new Font("Arial", Font.PLAIN, 30);
                        StdDraw.setPenColor(Color.BLACK);
                        StdDraw.setFont(font);
                        return selection;
                    }
                    else {
                        StdDraw.setPenColor(Color.BLACK);
                        StdDraw.filledRectangle(2, 2.5, 1.9, .4);
                        StdDraw.setPenColor(Color.RED);
                        font = new Font("Arial", Font.PLAIN, 32);
                        StdDraw.setFont(font);
                        StdDraw.text(2, 2.5, "Could not open " + selection);
                        StdDraw.show();
                        selection = "";
                        continue;
                    }
                }
                else if (key == '\b') {
                    if (selection.length() != 0)
                        selection = selection.substring(0, selection.length() - 1);
                }
                // Must be an ASCII character
                else if (key >= 32 && key <= 127) {
                    selection += key;
                }
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(2, 2.5, 1.9, .4);
                StdDraw.setPenColor(Color.WHITE);
                font = new Font("Arial", Font.PLAIN, 32);
                StdDraw.setFont(font);
                StdDraw.text(2, 2.5, selection);
                StdDraw.show();

            }

            StdDraw.pause(20);
        }
    }

    /**
     * Obtains the difficulty to be tested
     * 
     * @param difficulties all possible methods to test
     * @return the number of the method to test
     **/
    private static String getDifficulty(String[] difficulties) {
        Font font = new Font("Arial", Font.PLAIN, 24);
        StdDraw.setFont(font);
        StdDraw.clear();

        ArrayList<String> prompt = new ArrayList<>();
        prompt.add("What difficulty would you like to play?");
        prompt.add("Select from below, then click the first square.");

        for (int i = 0; i < difficulties.length; i++)
            prompt.add(String.format("%d. %s", i + 1, difficulties[i]));

        for (int line = 0; line < prompt.size(); line++)
            StdDraw.textLeft(0.5, 3.6 - 0.3 * line, prompt.get(line));

        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if ('1' <= key && key <= '3')
                    return difficulties[key - '1'];
            }
            StdDraw.pause(20);
        }
    }

    /**
     * Routes methods according to the corresponding action and displays an updated
     * board to test.
     * 
     * @param methodNumber the number of the method to test
     * 
     **/
    private static void testMethod(int methodNumber) {
        // Square[][] grid;
        Font font = new Font("Arial", Font.PLAIN, 24);
        int[] coords;

        switch (methodNumber) {
            case 1:
                while (StdDraw.hasNextKeyTyped())
                    StdDraw.nextKeyTyped();
                String filename = getFileChoice();
                play = new PlayMinesweeper();
                play.placeMines(filename);
                n = play.getGrid().length;
                play.openAllSquares();
                collage = new Collage(play.getGrid().length, play.getGrid()[0].length, false);
                collage.updateBoard(play, 0);
                play.closeAllSquares();
                gridFilled = false;
                allowPlayRandom = false;
                difficulty = null;
                break;

            case 2:
                collage.setCanvasSize();
                if (!gridFilled) {
                    play.fillGrid();
                    gridFilled = true;
                }
                play.openAllSquares();
                collage.updateBoard(play, 0);
                play.closeAllSquares();
                break;

            case 3:
                collage.setCanvasSize();
                collage.updateBoard(play, 0);
                coords = confirmClick(false);
                boolean openSquareReturn = play.openSquare(coords[1], coords[0]);
                collage.updateBoard(play, 0);
                waitFullClick();
                StdDraw.clear();
                StdDraw.setCanvasSize(700, 700);
                StdDraw.setScale(0, 4);
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.setFont(font);
                StdDraw.textLeft(0.25, 3.6, "The method openSquare returned " + openSquareReturn + ".");
                StdDraw.show();
                break;

            case 4:
                collage.setCanvasSize();
                collage.setFlagOn(true);
                collage.updateBoard(play, 0);
                waitFullClick();
                play.placeFlag((play.getGrid().length - 1) - (int) StdDraw.mouseY(), (int) StdDraw.mouseX());
                collage.updateBoard(play, 0);
                collage.setFlagOn(false);
                break;

            case 5:
                StdDraw.clear();
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.setFont(font);
                StdDraw.textLeft(0.25, 3.6, play.checkWinCondition() ? "You won!" : "You haven't won yet!");
                StdDraw.show();
                break;

            case 6:
                while (StdDraw.hasNextKeyTyped())
                    StdDraw.nextKeyTyped();
                String[] difficulties = {"Beginner", "Intermediate", "Advanced"};
                difficulty = getDifficulty(difficulties);
                play = new PlayMinesweeper();
                play.chooseDifficulty(difficulty);
                n = play.getGrid().length;
                collage = new Collage(n, play.getGrid()[0].length, false);
                collage.updateBoard(play, 0);
                gridFilled = false;
                allowPlayRandom = true;
                break;

            case 7:
                allowPlayRandom = false;
                StdRandom.setSeed(SEED);
                collage.setCanvasSize();
                collage.updateBoard(play, 0);
                coords = confirmClick(false);
                play.playRandom(difficulty, coords[1], coords[0]);
                play.fillGrid();
                gridFilled = true;
                play.openAllSquares();
                collage.updateBoard(play, 0);
                waitMouseClick(true, false);
                waitMouseRelease(false);
                play.closeAllSquares();
                play.openSquare(coords[1], coords[0]);
                collage.updateBoard(play, 0);
                break;
                
        }

        StdDraw.show();
        while (StdDraw.hasNextKeyTyped())
            StdDraw.nextKeyTyped();
        while (StdDraw.isMousePressed());
        waitMouseClick(true, false);
        StdDraw.setCanvasSize(700, 700);
        StdDraw.setScale(0, 4);
    }

    /**
     * Provides an interface for students to test all methods together to play the
     * PlayMinesweeper game.
     **/
    private static void playFullGame() {
        Font font = new Font("Arial", Font.PLAIN, 24);
        while (StdDraw.hasNextKeyTyped())
            StdDraw.nextKeyTyped();
        String[] difficulties = {"Beginner", "Intermediate", "Advanced"};
        difficulty = getDifficulty(difficulties);
        play = new PlayMinesweeper();
        play.chooseDifficulty(difficulty);
        n = play.getGrid().length;
        boolean flagOn = false;
        collage = new Collage(n, play.getGrid()[0].length, flagOn);
        collage.updateBoard(play, 0);
        int[] coords = confirmClick(false);
        play.playRandom(difficulty, coords[1], coords[0]);
        play.fillGrid();
        play.openSquare(coords[1], coords[0]);
        collage.updateBoard(play, 0);
        boolean openSquareReturn = false;
        start = System.currentTimeMillis();
        currTime = 0;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char nextKey = StdDraw.nextKeyTyped();
                if (nextKey == 'f' || nextKey == 'F') {
                    flagOn = !flagOn;
                    collage.setFlagOn(flagOn);
                    collage.updateBoard(play, currTime);
                }
            }
            if (StdDraw.isMousePressed()) {
                coords = confirmClick(true);
                if (flagOn) {
                    play.placeFlag(coords[1], coords[0]);
                    collage.updateBoard(play, currTime);
                } else if (play.getGrid()[coords[1]][coords[0]].getSqState() == State.CLOSED) {
                    openSquareReturn = play.openSquare(coords[1], coords[0]);
                    collage.updateBoard(play, currTime);
                    if (!openSquareReturn || play.checkWinCondition()) {
                        waitMouseRelease(false);
                        waitFullClick();
                        break;
                    }
                }
            }
            updateTime();
            StdDraw.show();
            StdDraw.pause(5);
        }
        if (!openSquareReturn) {
            play.openAllSquares();
            collage.updateBoard(play, currTime);
            waitFullClick();
            StdDraw.clear();
            StdDraw.setCanvasSize(700, 700);
            StdDraw.setScale(0, 4);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setFont(font);
            StdDraw.textLeft(0.25, 3.6, "You Lose!");
            StdDraw.show();
            StdDraw.pause(500);
            waitFullClick();
        } else {
            StdDraw.clear();
            StdDraw.setCanvasSize(700, 700);
            StdDraw.setScale(0, 4);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setFont(font);
            StdDraw.textLeft(0.25, 3.6, "You Win! Your time is " + currTime + " seconds.");
            StdIn.setFile("highscores/" + difficulty + "Highscore.txt");
            int highscoreTime = StdIn.readInt();
            if (currTime < highscoreTime) {
                StdDraw.setPenColor(Color.RED);
                StdDraw.textLeft(0.25, 3.3, "New Highscore!");
                StdDraw.setPenColor(Color.BLACK);
                StdOut.setFile("highscores/" + difficulty + "Highscore.txt");
                StdOut.println(currTime);
            } else
                StdDraw.textLeft(0.25, 3.3, "Your best time is " + highscoreTime + " seconds.");
            StdDraw.show();
            waitFullClick();
        }
    }

    /**
     * Updates the time on the Collage.
     */
    private static void updateTime() {
        if ((System.currentTimeMillis() - start) / 1000 > currTime) {
            currTime = (int) Math.min((System.currentTimeMillis() - start) / 1000, 999);
            collage.updateBoard(play, currTime);
        }
    }

    /**
     * Waits for a mouse click to occur.
     * @param keyboardAlso set to true if method should also 
     * wait for a keyboard input
     * @param trackTime set to true if method should update
     * time whiel waiting for a mouse click to occur
     */
    private static void waitMouseClick(boolean keyboardAlso, boolean trackTime) {
        while (true) {
            if (trackTime)
                updateTime();
            if (StdDraw.isMousePressed() || (keyboardAlso && StdDraw.hasNextKeyTyped()))
                break;
            StdDraw.pause(5);
        }
    }

    /**
     * Waits for a mouse release to occur.
     * @param trackTime set to true if method should
     * update time while waiting for a mouse click
     * to occur
     */
    private static void waitMouseRelease(boolean trackTime) {
        while (true) {
            if (trackTime)
                updateTime();
            if (!StdDraw.isMousePressed())
                break;
            StdDraw.pause(5);
        }
    }

    /**
     * Calls both waitMouseClick() and waitMouseRelease()
     * with false in all parameters.
     */
    private static void waitFullClick() {
        waitMouseClick(false, false);
        waitMouseRelease(false);
    }

    /**
     * Waits for a full click, and if the mouse has not
     * moved while clicking a tile, returns the x and
     * y coordinates of the square selected
     * @param trackTime set to true if method should
     * update time while waiting for a full click
     * to occur
     * @return an intege array with the x and y coordinates
     * of the mouse
     */
    private static int[] confirmClick(boolean trackTime) {
        while (true) {
            waitMouseClick(false, trackTime);
            int initialX = (int) StdDraw.mouseX();
            int initialY = n - 1 - (int) StdDraw.mouseY();
            waitMouseRelease(trackTime);
            int finalX = (int) StdDraw.mouseX();
            int finalY = n - 1 - (int) StdDraw.mouseY();
            if (initialX == finalX && initialY == finalY && finalY >= 0)
                return new int[] {finalX, finalY};
        }
    } 

}
