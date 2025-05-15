import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {


    public static final int x = 7;
    public static final int y = 6;

    public static void main(String[] args) {

        Board board = new Board(x, y);


        int turnsPlayed = 0;
        int maxTurns = board.getX() * board.getY();

        int turnChoice;

        int humanPlayer = 1;
        int computerPlayer = 2;

        int currentPlayer;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Which player goes first 1 = human or 2 = computer?");
        int choice = scanner.nextInt();

        while (choice != 1 && choice != 2) {
            System.out.println("Incorrect input!");
            System.out.println("Which player goes first 1 = human or 2 = computer?");
            choice = scanner.nextInt();
        }

        if (choice == 1) {
            currentPlayer = humanPlayer;
        } else {
            currentPlayer = computerPlayer;
        }


        System.out.println("How long should the AI be allowed to think?");
        int seconds = scanner.nextInt();
        System.out.println("Timer set to " + seconds + " seconds!");
        maxTimer = seconds * 1000L;

        System.out.println(board.getBoard());

        while (turnsPlayed < maxTurns) {

            if (currentPlayer == humanPlayer) {
                System.out.println("Human player's turn");

                System.out.println("Make your move (1-7)");
                turnChoice = makeTurnChoice(scanner);

                while (!board.checkMove(turnChoice - 1)) {
                    System.out.println("Cannot make move!");
                    System.out.println("Make your move (1-7)");
                    turnChoice = makeTurnChoice(scanner);
                }
                board.makeMove(turnChoice - 1, currentPlayer);

                System.out.println("Human player made a move");
                currentPlayer = computerPlayer;
            } else {
                System.out.println("Computer player's turn");

                turnChoice = AI(board, humanPlayer, computerPlayer, turnsPlayed);
                board.makeMove(turnChoice, currentPlayer);

                System.out.println("Computer player made a move");
                currentPlayer = humanPlayer;
            }

            System.out.println(board.getBoard());

            if (board.findWinCondition() == 1) {
                System.out.println("Human player wins!");
                break;
            } else if (board.findWinCondition() == 2) {
                System.out.println("Computer player wins!");
                break;
            }

            turnsPlayed++;
        }
        if (turnsPlayed == maxTurns) {
            System.out.println("It's a tie!");
        }

    }

    private static int makeTurnChoice(Scanner scanner) {
        int turnChoice;

        try {
            turnChoice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Incorrect input!");
            System.out.println("Make your move (1-7)");
            turnChoice = scanner.nextInt();
        }

        while (turnChoice < 1 || turnChoice > 7) {
            System.out.println("Cannot make move!");
            System.out.println("Make your move (1-7)");
            turnChoice = scanner.nextInt();
        }
        return turnChoice;
    }

    static int counter;
    static long timerStart;
    static long maxTimer = 15000;
    static int maxDepth = 42; //it can manage 11 depth in 15 seconds on my laptop

    static int AI(Board board, int humanPlayer, int computerPlayer, int turnsPlayed) {

        ScoreMove bestMove;
        ScoreMove aiMove = null;

        //it always picks 4 as its first move if it goes first, regardless of depth
        //therefore it's set as its first move
        //it is also objectively the best move it can make based on the static evaluation
        //it is also the best move if it isnt the first player
        if (turnsPlayed == 0 || turnsPlayed == 1) {
            bestMove = new ScoreMove(0, 3); //it uses zero index so the middle = 3
            System.out.println("""
                    ***
                    MinMax made a hardcoded move!
                    Duration: 0 milliseconds
                    ***
                    """
            );
            return bestMove.move();
        }

        int depth = 0;
        counter = 0;
        timerStart = System.currentTimeMillis();


        for (int j = 0; j < maxDepth; j++) {

            bestMove = minMax(board, humanPlayer, computerPlayer, 0, j, false,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (bestMove != null) {
                aiMove = bestMove;
                depth = j;
            }
        }

        long elapsedTime = System.currentTimeMillis() - timerStart;

        System.out.printf("""
                ***
                MinMax performed %s searches!
                Duration: %s milliseconds
                Depth: %s
                Move: %s
                ***
                """, counter, elapsedTime, depth, aiMove.move());
        return aiMove.move();
    }

    static boolean timer(){
        long currentTime = System.currentTimeMillis();
        long difference = currentTime - timerStart;

        return difference > maxTimer;
    }

    static ScoreMove minMax(Board board, int humanPlayer, int computerPlayer, int depth, int depthGoal,
                            boolean isMaximising, int alpha, int beta) {

        counter++;

        if (timer()) {
            return null;
        }

        ScoreMove bestMove;

        if (depth == depthGoal || board.findWinCondition() != 0) { //winCondition == 0 means tie
            int scoreEvaluation = scoreEvaluation(board, depth, board.findWinCondition(),
                    humanPlayer, computerPlayer);
            return new ScoreMove(scoreEvaluation, 0);
        }

        if (isMaximising) {
            bestMove = new ScoreMove(Integer.MIN_VALUE,0);
            for (int i = 0; i < board.getX(); i++) {
                if (board.checkMove(i)) {

                    int lastMoveY = board.makeMove(i, computerPlayer); //used to store y coordinate
                    ScoreMove result = minMax(board, humanPlayer, computerPlayer, depth + 1, depthGoal,
                            false, alpha, beta);
                    board.resetLastMove(i, lastMoveY);

                    if (result == null) {
                        return null;
                    }
                    if(result.score() > bestMove.score()) {
                        bestMove = new ScoreMove(result.score(), i);
                    }
                    if (beta <= alpha) {
                        break; //prune
                    }
                    alpha = Math.max(alpha, bestMove.score());
                }
            }
            return bestMove;
        } else {
            bestMove = new ScoreMove(Integer.MAX_VALUE,0);
            for (int i = 0; i < board.getX(); i++) {
                if (board.checkMove(i)) {

                    int lastMoveY = board.makeMove(i, humanPlayer); //used to store y coordinate
                    ScoreMove result = minMax(board, humanPlayer, computerPlayer, depth + 1, depthGoal,
                            true, alpha, beta);
                    board.resetLastMove(i, lastMoveY);

                    if (result == null) {
                        return null;
                    }
                    if(result.score() < bestMove.score()) {
                        bestMove = new ScoreMove(result.score(), i);
                    }

                    if (beta <= alpha) {
                        break; //prune
                    }

                    beta = Math.min(beta, bestMove.score());
                }
            }
            return bestMove;
        }
    }

    static int[][] points = { //stole from stackExchange
            {3, 4, 5, 7, 5, 4, 3},
            {4, 6, 8, 10, 8, 6, 4},
            {5, 8, 11, 13, 11, 8, 5},
            {5, 8, 11, 13, 11, 8, 5},
            {4, 6, 8, 10, 8, 6, 4},
            {3, 4, 5, 7, 5, 4, 3},
    };

    static int scoreEvaluation(Board board, int depth, int winConditionResult, int human, int computer) {
        if (winConditionResult != 0) { //0 == tie
            if (winConditionResult == computer) {
                return 1000 - depth;
            } else if (winConditionResult == human) {
                return -1000 + depth;
            }
        }

        int computerPoints = 0;
        int humanPoints = 0;

        for (int x = 0; x < board.getX(); x++) {
            for (int y = 0; y < board.getY(); y++) {
                char current = board.getBoardAt(x, y);
                if (current == 'O') {
                    computerPoints += points[y][x];
                }
                if (current == 'X') {
                    humanPoints += points[y][x];
                }
            }
        }

        return computerPoints - humanPoints;
    }
}