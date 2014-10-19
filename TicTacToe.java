/**
 *	Jaak Erisalu 135192IAPB
 *  TicTacToe
 *  21.09.2014
*/

 
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
	
	// Using ints that should never interfere with the rest of the program's functions
	public static final int PLAYER = 10000;
	public static final int COMPUTER = 10001;

	public static void main(String[] args) {
		int turnsToGo = 9;

		// Set the starting board here
		int board[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

		System.out.println("Welcome to TicTacToe");
		System.out.println("Squares have following indices: ");
        System.out.println("+---+---+---+");
        System.out.println("| 1 | 2 | 3 |");
        System.out.println("+---+---+---+");
        System.out.println("| 4 | 5 | 6 |");
        System.out.println("+---+---+---+");
        System.out.println("| 7 | 8 | 9 |");
        System.out.println("+---+---+---+");
        System.out.println("The starting board is as follows:");
        printBoard(board);

        // Find out how long the game can go on for
        for (int i = 0; i < 9; i++) {
        	if (board[i] != 0) {
        		turnsToGo--;
        	}
        }

        int turn = checkStart(board);

        for (int i = 0; i < turnsToGo; i++) {
            int move = 0;

        if (turn == PLAYER) {
            System.out.println("Please enter your move (1-9): ");
            move = readInput();
            while (board[move - 1] != 0) {
                    System.out.println("This place is taken, please try again: ");
                    move = readInput();
            }
            board[move - 1] = 1;
        } else {
            move = makeMove(board);
            board[move] = -1;
            System.out.println("Computer chose number " + (move + 1)); // MakeMove returns an INDEX
        }

        printBoard(board);

        if (checkWin(board)){
            if (turn == PLAYER) {
                System.out.println("Player WINS");
                break;
            } else {
                System.out.println("Computer WINS");
                break;
            }
        }

        if (turn == PLAYER) {
        	turn = COMPUTER;
        } else {
        	turn = PLAYER;
        }

    }

    System.out.println("Game over!");

	}

	/**
	* Figures out who's turn it is based on the board that was set
	* @param board Current state of the board
	* @return start Either PLAYER or COMPUTER
	*/
	public static int checkStart(int[] board) {
		int playersMoves = 0;
		int computersMoves = 0;

		for(int i=0; i < 9; i++){
			if (board[i] == 1){
				playersMoves++;
			} else if (board[i] == -1){
				computersMoves++;
			}
		}
		if (playersMoves > computersMoves) {
			return COMPUTER;
		} else {
			return PLAYER;
		}
	}

    /**
     * Takes the current state of the board
     * and returns the cell index where computer
     * makes its move.
     *
     * The one-dimensional board is indexed as follows:
     *
     * +---+---+---+
     * | 0 | 1 | 2 |
     * +---+---+---+
     * | 3 | 4 | 5 |
     * +---+---+---+
     * | 6 | 7 | 8 |
     * +---+---+---+
     *
     * So, the following state:
     *
     * +---+---+---+
     * | X |   |   |
     * +---+---+---+
     * |   | O | X |
     * +---+---+---+
     * |   |   |   |
     * +---+---+---+
     *
     * is given as an array:
     * {1, 0, 0, 0, -1, 1, 0, 0, 0}
     * where 1 indicates a player stone ("X") and
     * -1 indicates a computer stone ("O")
     *
     * @param board Current state of the board.
     * See the description of the method for more information.
     * @return Cell index where the computer makes its move.
     */
    public static int makeMove(int[] board) {
        int nextMove;

        // Copy of current board for prediction
        int boardCopy[] = Arrays.copyOf(board, board.length);

        // Vars for making moves
        int corners[] = {0, 2, 6, 8};
        boolean cornerIsFree = false;
        Random rand = new Random();
       
        // Tries to find a winning move by testing all the possible moves
        for(int i=0; i < 9; i++){
            if (board[i] == 0) {
                boardCopy[i] = -1;
                if (checkWin(boardCopy)){
                    nextMove = i;
                    return nextMove;
                }

                // Reset and continue
                boardCopy[i] = 0;
                continue;
            }
        }

        // Tries to block player from winning using the same methods
        for (int i=0; i < 9; i++) {
            if (board[i] == 0) {
                boardCopy[i] = 1;
                if (checkWin(boardCopy)){
                    nextMove = i;
                    return nextMove;
                }
                boardCopy[i] = 0;
                continue;
            }
        }
       
        // Tries to take the middle square
        if (board[4] == 0){
            nextMove = 4;
            return nextMove;
        }

        // Check if there's a free corner
        for(int i = 0; i < 4; i++){
            if(board[corners[i]] == 0){
                cornerIsFree = true;
            }
        }
       
        // Tries to take a random corner if it's not full, otherwise tries to take a random spot
        if (cornerIsFree) {
            nextMove = corners[(rand.nextInt(4))];
            while (board[nextMove] != 0){
                nextMove = corners[(rand.nextInt(4))];
            }
        } else {
            nextMove = rand.nextInt(9);
            while (board[nextMove] != 0) {
                nextMove = rand.nextInt(9);
            }
        }

        return nextMove;
    }

	/**
     * Text scanner which is used in readInput(). We declare it here as a
     * separate variable in order to avoid warnings about not closing it.
     */
    private static Scanner scanner = new Scanner(System.in);

	/**
     * Reads a number from the standard input and returns it. Beware: this
     * method needs some improvements!
     * @return Number from the input
     */
    public static int readInput() {
        int nr = 0;
       
        while (!scanner.hasNextInt()){
            System.out.println("Incorrect input!");
            scanner.next();
        }
       
        nr = scanner.nextInt();
       
        while (nr < 1 || nr > 9){
            System.out.println("Please enter a number between 1-9");
            nr = scanner.nextInt();
        }
       
        return nr;
    }

	/**
	* Checks the board against hard-coded win conditions
	* @param board The current state of the board
	* @return True if there's a winner
	*/
	public static boolean checkWin(int[] board) {
	    if (board[0] == board[1] && board[1] == board[2] && board[0] != 0 
            || board[0] == board[3] && board[3] == board[6] && board[0] != 0
            || board[0] == board[4] && board[4] == board[8] && board[0] != 0
            || board[1] == board[4] && board[4] == board[7] && board[1] != 0
            || board[2] == board[5] && board[5] == board[8] && board[2] != 0
            || board[3] == board[4] && board[4] == board[5] && board[3] != 0
            || board[6] == board[7] && board[7] == board[8] && board[6] != 0
            || board[6] == board[4] && board[4] == board[2] && board[6] != 0) {
	            return true;
	    	}
	    return false;
	}

	/**
	* Converts the 1's, -1's and 0s to human-friendly symbols
	* @param location An integer
	* @return result A string, either X, O or whitespace.
	*/
	public static String output(int location) {
        String result;

        if (location == 1) {
                result = "X";
        } else if (location == -1) {
                result = "O";
        } else {
                result = " ";
        }

        return result;
    }

    /**
	* Prints the game board according to the current scores
	* @param score Integer array with the current score
	*/
	public static void printBoard(int[] score) {
        System.out.println("+---+---+---+");
        System.out.printf("| %s | %s | %s |\n", output(score[0]), output(score[1]), output(score[2]));
        System.out.println("+---+---+---+");
        System.out.printf("| %s | %s | %s |\n", output(score[3]), output(score[4]), output(score[5]));
        System.out.println("+---+---+---+");
        System.out.printf("| %s | %s | %s |\n", output(score[6]), output(score[7]), output(score[8]));
        System.out.println("+---+---+---+");
	}
}