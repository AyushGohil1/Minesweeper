import java.io.File;
import java.util.ArrayList;

/**
 * Text Driver -- tests students' play minesweeper through the terminal.
 * GraphicalDriver.java is equivalent to TextDriver.java
 * 
 * @author Jessica de Brito
 * @author Jeremy Hui
 */
public class TextDriver {
    private static boolean UTF = true; // Change this to false if your computer does not support UTF-8 characters 
    private static String CLOSEDSYMBOL = UTF ? "□" : "?";
    private static String FLAGGEDSYMBOL = UTF ? "⚑" : "F";
    private static String MINESYMBOL = UTF ? "✸" : "X";

    private static PlayMinesweeper play;
    private static boolean gridFilled = false;
    private static boolean allowPlayRandom = false;
    private static String difficulty = null;
    private static int SEED = 2024; // Used by the autograder to ensure the same random values are always generated

    /**
     * Main method
     * @param args not used
     */
    public static void main(String[] args) {
        String[] actions = { "Test individual methods", "Play full game", "Quit" };
        int select = 0;
        while (true) {
            System.out.println("What do you want to do?");
            for (int i = 0; i < actions.length; i++)
                System.out.printf("%d. %s%n", i + 1, actions[i]);
            System.out.print("Select an option from the menu above: ");
            select = StdIn.readInt();
            System.out.println();
            switch (select) {
                case 1:
                    testIndividualMethods();
                    break;
                case 2:
                    playFullGame();
                    System.exit(0);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please enter a valid choice.");
                    break;
            }
        }
    }

    private static void testIndividualMethods() {
        String[] methods = { "placeMines", "fillGrid", "openSquare", "placeFlag", 
            "checkWinCondition", "chooseDifficulty", "playRandom", "quit"};
        int select = 0;
        while (true) {
            System.out.println("What method would you like to test?");
            for (int i = 0; i < methods.length; i++)
                System.out.printf("%d. %s%n", i + 1, methods[i]);
            System.out.print("Select an option from the menu above: ");
            select = StdIn.readInt();
            StdIn.readLine();
            System.out.println();
            if (select < 1 || select > 8) {
                System.out.println("Please enter a valid choice.\n");
            } else if (play == null && (2 <= select && select <= 5 || select == 7)) {
                System.out.println("Please setup the board first!\n");
            } else if (3 <= select && select <= 5 && !gridFilled) {
                System.out.println("Please call fillGrid first!\n");
            } else if (select == 7 && !allowPlayRandom) {
                System.out.println("playRandom has already been called, or you used a file to setup the board!\n");
            } else {
                switch (select) {
                    case 1:
                        testPlaceMines();
                        break;
                    case 2:
                        testFillGrid();
                        break;
                    case 3:
                        testOpenSquare();
                        break;
                    case 4:
                        testPlaceFlag();
                        break;
                    case 5:
                        testCheckWinCondition();
                        break;
                    case 6:
                        testChooseDifficulty();
                        break;
                    case 7:
                        testPlayRandom();
                        break;
                    case 8:
                        System.exit(0);
                        break;
                }
                StdIn.resync();
                System.out.print("Press enter to continue: ");
                StdIn.readLine();
                System.out.println();
            }
        }
    }

    private static void testPlaceMines() {
        File[] allFiles = new File(".").listFiles();
        ArrayList<String> files = new ArrayList<>();
        for (File f : allFiles) {
            if (f.getName().endsWith(".in")) {
                files.add(f.getName());
            }
        }
        String inputFile;
        while (true) {
            System.out.print("Enter input file to test: ");
            inputFile = StdIn.readLine();
            if (files.contains(inputFile))
                break;
            else
                System.out.println("Could not open " + inputFile + ".");
        }
        play = new PlayMinesweeper();
        play.placeMines(inputFile);
        System.out.println("The following numbers are present in the grid:");
        play.printGridNums();
        gridFilled = false;
        allowPlayRandom = false;
        difficulty = null;
    }

    private static void testFillGrid() {
        if (!gridFilled) {
            play.fillGrid();
            gridFilled = true;
        }
        System.out.println("The following numbers are present in the grid:");
        play.printGridNums();
    }

    private static void testOpenSquare() {
        System.out.println("The grid currently appears as follows:");
        printGrid(play.getGrid());
        System.out.print("Enter the row index of the square you want to open: ");
        int openRowInd = StdIn.readInt();
        System.out.print("Enter the column index of the square you want to open: ");
        int openColInd = StdIn.readInt();
        System.out.printf("The following is the result of opening the square at (%d, %d):%n", 
            openRowInd, openColInd);
        boolean openSquareReturn = play.openSquare(openRowInd, openColInd);
        printGrid(play.getGrid());
        System.out.println("The method openSquare returned " + openSquareReturn + ".");
    }

    private static void testPlaceFlag() {
        System.out.print("Enter the row index of the square you want to flag: ");
        int flagRowInd = StdIn.readInt();
        System.out.print("Enter the column index of the square you want to flag: ");
        int flagColInd = StdIn.readInt();
        System.out.printf("The following is the result of flagging the square at (%d, %d):%n", 
            flagRowInd, flagColInd);
        play.placeFlag(flagRowInd, flagColInd);
        printGrid(play.getGrid());
        System.out.print("FLAGS: " + (play.getMinesCount() - play.getFlagCount()) + "\n");
    }
    
    private static void testCheckWinCondition() {
        if (play.checkWinCondition())
            System.out.println("You won!");
        else
            System.out.println("You haven't won yet!");
    }

    private static void testChooseDifficulty() {
        String[] difficulties = {"Beginner", "Intermediate", "Advanced"};
        int select = 0;
        while (true) {
            System.out.println("What difficulty would you like to test?");
            for (int i = 0; i < difficulties.length; i++)
                System.out.printf("%d. %s%n", i + 1, difficulties[i]);
            System.out.print("Select an option from the menu above: ");
            select = StdIn.readInt();
            if (select >= 1 && select <= 3)
                break;    
            System.out.println("Please enter a valid choice.\n");
        }
        difficulty = difficulties[select - 1];
        play = new PlayMinesweeper();
        play.chooseDifficulty(difficulty);
        System.out.printf("The following is the result of choosing the %s difficulty:%n", difficulty);
        printGrid(play.getGrid());
        gridFilled = false;
        allowPlayRandom = true;
    }

    private static void testPlayRandom() {
        StdRandom.setSeed(SEED);
        allowPlayRandom = false;
        System.out.print("Enter the row index of your first click: ");
        int firstClickRowInd = StdIn.readInt();
        System.out.print("Enter the column index of your first click: ");
        int firstClickColInd = StdIn.readInt();
        System.out.println("The following numbers are present in the grid:");
        play.playRandom(difficulty, firstClickRowInd, firstClickColInd);
        play.fillGrid();
        play.printGridNums();
        System.out.printf("The following is the result of opening the square at (%d, %d):%n", 
            firstClickRowInd, firstClickColInd);
        play.openSquare(firstClickRowInd, firstClickColInd);
        printGrid(play.getGrid());
    }

    private static void playFullGame() {
        String[] difficulties = {"Beginner", "Intermediate", "Advanced"};
        int select = 0;
        boolean openSquareReturn = true;
        while (true) {
            System.out.println("What difficulty would you like to test?");
            for (int i = 0; i < difficulties.length; i++)
                System.out.printf("%d. %s%n", i + 1, difficulties[i]);
            System.out.print("Select an option from the menu above: ");
            select = StdIn.readInt();
            if (select >= 1 && select <= 3)
                break;    
            System.out.println("Please enter a valid choice.\n");
        }
        difficulty = difficulties[select - 1];
        play = new PlayMinesweeper();
        play.chooseDifficulty(difficulty);
        printGrid(play.getGrid());

        System.out.print("Enter the row index of your first click: ");
        int firstClickRowInd = StdIn.readInt();
        System.out.print("Enter the column index of your first click: ");
        int firstClickColInd = StdIn.readInt();
        play.playRandom(difficulty, firstClickRowInd, firstClickColInd);
        play.fillGrid();
        System.out.printf("The following is the result of opening the square at (%d, %d):%n", 
            firstClickRowInd, firstClickColInd);
        play.openSquare(firstClickRowInd, firstClickColInd);
        printGrid(play.getGrid());
        long start = System.currentTimeMillis();
        while (true) {
            while (true) {
                System.out.println("Would you like to open or flag a square?");
                System.out.println("1. Open");
                System.out.println("2. Flag");
                System.out.print("Select an option from the menu above: ");
                select = StdIn.readInt();
                if (select == 1 || select == 2)
                    break;
                System.out.println("Please enter a valid choice.\n");
            }
            if (select == 1) {
                System.out.print("Enter the row index of the square you want to open: ");
                int openRowInd = StdIn.readInt();
                System.out.print("Enter the column index of the square you want to open: ");
                int openColInd = StdIn.readInt();
                System.out.printf("The following is the result of opening the square at (%d, %d):%n", 
                    openRowInd, openColInd);
                openSquareReturn = play.openSquare(openRowInd, openColInd);
                printGrid(play.getGrid());
                System.out.print("FLAGS: " + play.getFlagCount() + "\n");
                if (!openSquareReturn || play.checkWinCondition())
                    break;
            } else {
                System.out.print("Enter the row index of the square you want to flag: ");
                int flagRowInd = StdIn.readInt();
                System.out.print("Enter the column index of the square you want to flag: ");
                int flagColInd = StdIn.readInt();
                System.out.printf("The following is the result of flagging the square at (%d, %d):%n", 
                    flagRowInd, flagColInd);
                play.placeFlag(flagRowInd, flagColInd);
                printGrid(play.getGrid());
                System.out.print("FLAGS: " + play.getFlagCount() + "\n");
            }
            System.out.print("Press enter to continue:");
            StdIn.readLine();
        }
        if (!openSquareReturn) {
            play.printGridNums();
            System.out.println("You Lose!");
        } else {
            int currTime = (int) Math.min((System.currentTimeMillis() - start) / 1000, 999);
            System.out.println("You win! Your time is " + currTime + ".");
            StdIn.setFile("highscores/" + difficulty + "Highscore.txt");
            int highscoreTime = StdIn.readInt();
            if (currTime < highscoreTime) {
                System.out.println("New highscore!");
                StdOut.setFile("highscores/" + difficulty + "Highscore.txt");
                StdOut.println(currTime);
            } else
                System.out.println("Your best time is " + highscoreTime + "seconds.");
        }
    }

    /**
     * This method prints the 2D array of squares.
     * Called when testing individual methods in the TextDriver.
     */
    public static void printGrid(Square[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) { 
                if (grid[i][j].getSqState() == State.CLOSED) {
                    StdOut.print(CLOSEDSYMBOL + "\t");
                }
                else if (grid[i][j].getSqState() == State.FLAGGED) { 
                    StdOut.print(FLAGGEDSYMBOL + "\t");
                }
                else if (grid[i][j].getSqNum() == -1) { 
                    StdOut.print(MINESYMBOL + "\t");
                }
                else { 
                    StdOut.print(grid[i][j].getSqNum() + "\t");
                }
            }
            StdOut.println();
        }
    }
}
