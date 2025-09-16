/**
 * Square objects used to make up the grid 
 * in the PlayMinesweeper object.
 * 
 * @author Jessica de Brito
 * @author Jeremy Hui
 */
public class Square {
    private int sqNum;
    private State sqState;

    /**
     * Default constructor.
     * Initializes a Square with square number 
     * set to 0 and State to CLOSED.
     */
    public Square() {
        this(0, State.CLOSED);
    }

    /**
     * Constructor initializes instance variables to parameter values.
     * @param sqNum the given square number
     * @param sqState the given State
     */
    public Square(int sqNum, State sqState) {
        this.sqNum = sqNum;
        this.sqState = sqState;
    }

    /**
     * Returns the square number.
     * @return the square number
     */
    public int getSqNum(){
        return sqNum;
    }

    /**
     * Returns the State
     * @return the State
     */
    public State getSqState(){
        return sqState;
    }

    /**
     * Increments the square number by 1.
     */
    public void increaseSqNum() {
        this.sqNum++;
    }

    /**
     * Sets the square number to the given square number.
     * @param num the given square number
     */
    public void setSqNum(int num){
        this.sqNum = num;
    }

    /**
     * Sets the state to the given state.
     * @param state the given state
     */
    public void setSqState(State state) {
        this.sqState = state;
    }
}
