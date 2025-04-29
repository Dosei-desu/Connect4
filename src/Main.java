import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    static public int depth = 0;
    static final int x = 7;
    static final int y = 6;

    public static void main(String[] args) {

        Board board = new Board(x,y);


        int turnsPlayed = 0;
        int maxTurns = board.getX() * board.getY();

        int turnChoice;

        int humanPlayer = 1;
        int computerPlayer = 2;

        int currentPlayer;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Which player goes first 1 = human or 2 = computer?");
        int choice = scanner.nextInt();

        while (choice != 1 && choice != 2){
            System.out.println("Incorrect input!");
            System.out.println("Which player goes first 1 = human or 2 = computer?");
            choice = scanner.nextInt();
        }

        if (choice == 1){
            currentPlayer = humanPlayer;
        }
        else{
            currentPlayer = computerPlayer;
        }


        System.out.println("Computer difficulty? >8 is slow");
        int setDepth = scanner.nextInt();
        System.out.println("Depth = " + setDepth);
        depth = setDepth;

        System.out.println(board.getBoard());
        System.out.println(board.getPlayGuide());

        while (turnsPlayed < maxTurns) {

            if (currentPlayer == humanPlayer) {
                System.out.println("Human player's turn");
            }else{
                System.out.println("Computer player's turn");
            }


            if(currentPlayer == humanPlayer) {

                System.out.println("Make your move (1-7)");
                turnChoice = makeTurnChoice(scanner);

                while (!board.checkMove(turnChoice - 1)) {
                    System.out.println("Cannot make move!");
                    System.out.println("Make your move (1-7)");
                    turnChoice = makeTurnChoice(scanner);
                }
                board.makeMove(turnChoice - 1, currentPlayer);
            }
            else{
                turnChoice = AI(board,humanPlayer,computerPlayer);
                board.makeMove(turnChoice, currentPlayer);
            }


            if (currentPlayer == humanPlayer){
                System.out.println("Human player made a move");
                currentPlayer = computerPlayer;
            }else{
                System.out.println("Computer player made a move");
                currentPlayer = humanPlayer;
            }

            System.out.println(board.getBoard());

            if(board.findWinCondition() == 1){
                System.out.println("Human player wins!");
                break;
            }
            else if (board.findWinCondition() == 2) {
                System.out.println("Computer player wins!");
                break;
            }

            turnsPlayed++;
        }
        if(turnsPlayed == maxTurns){
            System.out.println("It's a tie!");
        }

    }

    private static int makeTurnChoice(Scanner scanner) {
        int turnChoice;

        try{
            turnChoice = scanner.nextInt();
        }
        catch (InputMismatchException e){
            System.out.println("Incorrect input!");
            System.out.println("Make your move (1-7)");
            turnChoice = scanner.nextInt();
        }

        while (turnChoice < 1 || turnChoice > 7){
            System.out.println("Cannot make move!");
            System.out.println("Make your move (1-7)");
            turnChoice = scanner.nextInt();
        }
        return turnChoice;
    }

    static int counter = 0;

    static int AI(Board board, int humanPlayer, int computerPlayer){

        int move = 0;
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < board.getX(); i++) { //x is width
            if(board.checkMove(i)){

                int lastMoveY = board.makeMove(i,computerPlayer); //used to store y coordinate
                int score = minMax(board, humanPlayer, computerPlayer,0,depth,false,
                        Integer.MIN_VALUE,Integer.MAX_VALUE);
                board.resetLastMove(i,lastMoveY);

                if(score > bestScore){
                    bestScore = score;
                    move = i;
                }
            }
        }

        System.out.println("***MinMax performed "+counter+" searches!***");
        counter = 0;
        return move;
    }

    static int minMax(Board board, int humanPlayer, int computerPlayer, int depth, int depthGoal,
                      boolean isMaximising, int alpha, int beta){

        counter++;

        if(depth == depthGoal || board.findWinCondition() != 0){ //winCondition == 0 means tie
            return scoreEvaluation(board,depth,board.findWinCondition(),
                    humanPlayer,computerPlayer);
        }

        if(isMaximising){
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < board.getX(); i++) {
                if (board.checkMove(i)) {

                    int lastMoveY = board.makeMove(i, computerPlayer); //used to store y coordinate
                    int score = minMax(board, humanPlayer, computerPlayer, depth + 1, depthGoal,
                            false, alpha, beta);
                    board.resetLastMove(i, lastMoveY);

                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(alpha, score);

                    if (beta <= alpha) {
                        break; //prune
                    }
                }
            }
            return bestScore;
        }
        else{
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < board.getX(); i++) {
                if (board.checkMove(i)) {

                    int lastMoveY = board.makeMove(i, humanPlayer); //used to store y coordinate
                    int score = minMax(board, humanPlayer, computerPlayer, depth + 1, depthGoal,
                            true, alpha, beta);
                    board.resetLastMove(i, lastMoveY);

                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, score);

                    if (beta <= alpha) {
                        break; //prune
                    }
                }
            }
            return bestScore;
        }
    }
    static int[][] points = { //stole from stackExchange
            {3,4,5,7,5,4,3},
            {4,6,8,10,8,6,4},
            {5,8,11,13,11,8,5},
            {5,8,11,13,11,8,5},
            {4,6,8,10,8,6,4},
            {3,4,5,7,5,4,3},
    };

    static int scoreEvaluation(Board board, int depth, int winConditionResult, int human, int computer){
        if(winConditionResult != 0) { //0 == tie
            if (winConditionResult == computer){
                return 1000 - depth;
            }else if (winConditionResult == human){
                return -1000 + depth;
            }
        }

        int computerPoints = 0;
        int humanPoints = 0;

        for (int x = 0; x < board.getX(); x++) {
            for (int y = 0; y < board.getY(); y++) {
                char current = board.getBoardAt(x,y);
                if (current == 'O'){
                    computerPoints += points[y][x];
                }
                if (current == 'X'){
                    humanPoints += points[y][x];
                }
            }
        }

        return computerPoints - humanPoints;
    }
}