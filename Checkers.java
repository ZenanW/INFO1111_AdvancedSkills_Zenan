import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
public class Checkers {

    /**
     * Represents the game board as a 2D array.
     * 'b' for Black pieces, 'w' for White pieces, ' ' for Empty spaces, 'B' for
     * Black Kings, and 'W' for White Kings.
     */
    private static char[][] board = new char[8][8];
    private static Map<String, int[]> positionMap = new HashMap<>();
    private static char kingB = 'B';
    private static char kingW = 'W';

    /**
     * Initialises the board with pieces in their starting positions.
     */
    private static void initialiseBoard() {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = ' ';
            }
        }
        
        for (int i = 0; i < 3; i++) {
            for (int j = (i % 2 != 0) ? 0 : 1; j < board[i].length; j += 2) {
                board[i][j] = 'w';
            }
        }
        
        for (int i = 5; i < 8; i++) {
            for (int j = (i % 2 != 0) ? 0 : 1; j < board[i].length; j += 2) {
                board[i][j] = 'b';
            }
        }
    }

        
    // Implement this method to fill the board array with pieces in their starting
    // positions.

    /**
     * Displays the current state of the board to the console.
     */

    
    private static void createPositionMap() {
        char[] columns = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                String key = columns[j] + Integer.toString(i + 1);
                positionMap.put(key, new int[]{i, j});
            }
        }
    }


    private static void displayBoard(boolean printExtraSpace) {
        for (int i = 0; i < board.length; i++) {
            System.out.print("|");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + "");
                if (j < board[i].length) { 
                    System.out.print("|");
                }
            }
            System.out.println(); 
        }
        if (printExtraSpace) {
            System.out.println();
            System.out.println();
        }
        else {
            System.out.println();  
        }
    }

    /**
     * Main game loop. Handles player turns and checks for game end conditions.
     */
    private static void startGame() {
        // Implement the game loop, handling player input, turn switching, and win
        // condition checking.

        Scanner scanner = new Scanner(System.in);
        createPositionMap();
        displayBoard(false);

        boolean currentPlayer = true;
        // Game loop
        while (true) {
            isGameOver();
            scanner.hasNextLine();
            String move = scanner.nextLine(); 

            // Check for exit command
            if (move.equalsIgnoreCase("exit")) {
                break; 
            }

            if (move.equalsIgnoreCase("View")) {
                displayBoard(false); 
                continue; 
            }

            // Process the move
            if (!processMove(move, currentPlayer)) {
                displayBoard(false);
            } else {
                currentPlayer = !currentPlayer;
            }
        }

        System.out.println();

        scanner.close();      
    }

    /**
     * Processes a player's move.
     * 
     * @param move A string representing the player's move (e.g., "C3 to D4").
     * @return true if the move is valid and executed, false otherwise.
     */
    private static boolean processMove(String move, boolean currentPlayer) {
        // Implement this method to process the player's move.
        // You should validate the move and execute it if it's valid.
        if (move.length() != 8 || !move.matches("[A-H][1-8] to [A-H][1-8]")) {
            System.out.println("Error!\n");
            return false;
        }

        String[] wordsInMove = move.split(" to ");

        String fromPosition = wordsInMove[0];
        String toPosition = wordsInMove[1];

        if (!positionMap.containsKey(fromPosition) || !positionMap.containsKey(toPosition)) {
            System.out.println("Error!");
            System.out.println();
            return false;
        }

        int[] fromIndices = positionMap.get(fromPosition);
        int[] toIndices = positionMap.get(toPosition);

        int fromRow = fromIndices[0];
        int fromCol = fromIndices[1];
        int toRow = toIndices[0];
        int toCol = toIndices[1];

        int midRow = (fromRow + toRow) / 2;
        int midCol = (fromCol + toCol) / 2;

        // Validate the move using the isValidMove method
        if (!isValidMove(fromRow, fromCol, toRow, toCol, currentPlayer)) {
            System.out.println("Error!");
            System.out.println();
            return false;
        }

        if (isValidMove(fromRow, fromCol, toRow, toCol, currentPlayer) && isMidPointOccupied(fromRow, fromCol, toRow, toCol, currentPlayer)) {
            int distance = Math.abs(toRow - fromRow);
            if (distance == 2) { 
                if (board[midRow][midCol] == 'b' && currentPlayer == false) {
                    capturePiece(fromRow, fromCol, toRow, toCol); 
                } else if (board[midRow][midCol] == 'w' && currentPlayer == true) {
                    capturePiece(fromRow, fromCol, toRow, toCol);
                } else if (board[midRow][midCol] == 'B' && currentPlayer == false) {
                    capturePiece(fromRow, fromCol, toRow, toCol);
                } else if (board[midRow][midCol] == 'W' && currentPlayer == true) {
                    capturePiece(fromRow, fromCol, toRow, toCol);
                
                } else {
                    board[toRow][toCol] = board[fromRow][fromCol]; 
                    board[fromRow][fromCol] = ' ';
                    displayBoard(false); 
                    return true;
                }
            }
        }

        // Move the piece normally if the move is valid
        board[toRow][toCol] = board[fromRow][fromCol]; 
        board[fromRow][fromCol] = ' '; 
        promoteToKing();

        displayBoard(false);

        return true;
    }

    /**
     * Checks if a move is valid.
     * 
     * @param fromRow the starting row of the move.
     * @param fromCol the starting column of the move.
     * @param toRow   the ending row of the move.
     * @param toCol   the ending column of the move.
     * @return true if the move is legal, false otherwise.
     */
    private static boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, boolean currentPlayer) {

        char piece = board[fromRow][fromCol];
        boolean isKing = (piece == kingB || piece == kingW);
        boolean movingForward = (currentPlayer && toRow < fromRow) || (!currentPlayer && toRow > fromRow); // Black moves up (decrease row), White moves down (increase row)

        // Making sure move is diagonal
        if (fromRow < 0 || fromRow >= 8 || fromCol < 0 || fromCol >= 8 ||
            toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8 ||
            Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) {
            return false;
        }

        if ((!isKing && (piece != (currentPlayer ? 'b' : 'w'))) || piece == ' ') {
            return false;
        }

        int distance = Math.abs(toRow - fromRow);
        if (distance == 1 && board[toRow][toCol] == ' ') {
            return isKing || movingForward;
        } else if (distance == 2) {
            // Jumping over another piece
            int midRow = (fromRow + toRow) / 2;
            int midCol = (fromCol + toCol) / 2;
            char midPiece = board[midRow][midCol];
            // Ensure there's a piece to jump over and the landing spot is empty
            if (midPiece != ' ' && board[toRow][toCol] == ' ') {
                // For Kings, allow jumping in any direction. For regular pieces, check direction.
                return isKing || movingForward;
            }
        }

        return false; 

    }

    private static boolean isMidPointOccupied(int fromRow, int fromCol, int toRow, int toCol, boolean currentPlayer) {
        // Calculate the midpoint
        int midRow = (fromRow + toRow) / 2;
        int midCol = (fromCol + toCol) / 2;

        char pieceAtMidpoint = board[midRow][midCol];

        // Check if the square is occupied, indicating a potential jump
        if (pieceAtMidpoint == 'w' || pieceAtMidpoint == 'b' || pieceAtMidpoint == 'W' || pieceAtMidpoint == 'B') { 
            return true; 
        }

        return false; 
    }

    private static void capturePiece(int fromRow, int fromCol, int toRow, int toCol) {
        // Calculate the position of piece in between
        int midRow = (fromRow + toRow) / 2;
        int midCol = (fromCol + toCol) / 2;

        board[midRow][midCol] = ' '; 
    }

    private static void promoteToKing() {
        for (int col = 0; col < 8; col++) {
            if (board[0][col] == 'b') { // If a black piece reaches the white side
                board[0][col] = kingB; // Promote to Black King
            }
            if (board[7][col] == 'w') { // If a white piece reaches the black side
                board[7][col] = kingW; // Promote to White King
            }
        }
    }
    

    /**
     * Checks if the game has ended.
     * 
     * The program should terminate if the game has finished.
     */
    private static void isGameOver() {
        int blackPieces = 0;
        int whitePieces = 0;
        
        // Iterate over the board to count pieces for each player
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                char piece = board[row][col];
                if (piece == 'b' || piece == kingB) {
                    blackPieces++;
                } else if (piece == 'w' || piece == kingW) {
                    whitePieces++;
                }
            }
        }
        
        // Check win conditions
        if (blackPieces == 0) {
            System.exit(0);
        } else if (whitePieces == 0) {
            System.exit(0);
        }
    }

    /**
     * Main method to run the game.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        initialiseBoard();
        startGame();
    }
}