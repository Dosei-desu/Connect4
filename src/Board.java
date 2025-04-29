public class Board {

    private final int x;
    private final int y;
    private final char empty = ' ';
    private final char playerOneTile = 'X';
    private final char playerTwoTile = 'O';

    char[][] board;

    public Board(int x, int y) {
        this.x = x;
        this.y = y;

        board = new char[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                board[i][j] = empty;
            }
        }
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public boolean checkMove(int x){
        //checks if it's possible to make a move in a column by going up the rows and
        //searching for empty spots
        for (int i = 0; i < y; i++) {
            if (board[x][y - 1 - i] == empty){
                return true;
            }
        }
        return false;
    }


    public int makeMove(int x, int player) {
        //reworked to ternary operator thanks to (cat emoji)'s suggestion
        char playerTile = player == 1
                ? playerOneTile
                : playerTwoTile;

        for (int i = 0; i < y; i++) {

            //y -i is to invert the moves so they go on the bottom (which is actually the top)
            if (board[x][y - 1 - i] != playerOneTile && board[x][y - 1 - i] != playerTwoTile) {

                //saving last move y coordinate for resetting purposes
                int lastMoveY = y-1-i;

                board[x][y - 1 - i] = playerTile;
                return lastMoveY;
            }
        }
        return -1;
    }

    public void resetLastMove(int x, int y){
        board[x][y] = ' ';
    }

    public char getBoardAt(int x, int y) {
        return board[x][y];
    }

    public int findWinCondition() {
        //1 is human win, 2 is computer win, 0 is no win / tie
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {

                char check = getBoardAt(j, i);
                //checks if the spot is empty, if it is, all checks are skipped
                //should reduce search time a lot
                if(check != empty) {

                    //horizontal win
                    if (j + 3 < x) {
                        //rewrite suggestion by (cat emoji)
                        //halves number of checks and should hopefully result in faster searches
                        if (
                                getBoardAt(j + 1, i) == check
                                && getBoardAt(j + 2, i) == check
                                && getBoardAt(j + 3, i) == check
                        ) {
                            return check == playerOneTile ? 1 : 2;
                        }
                    /*
                    //old version
                    if (getBoardAt(j, i) == 'X' && getBoardAt(j + 1, i) == 'X' &&
                            getBoardAt(j + 2, i) == 'X' && getBoardAt(j + 3, i) == 'X')
                    {
                        return 1;
                    }
                    else if (getBoardAt(j, i) == 'O' && getBoardAt(j + 1, i) == 'O' &&
                            getBoardAt(j + 2, i) == 'O' && getBoardAt(j + 3, i) == 'O')
                    {
                        return 2;
                    }
                     */
                    }
                    //vertical win
                    if (i + 3 < y) {
                        if (
                                getBoardAt(j, i + 1) == check
                                && getBoardAt(j, i + 2) == check
                                && getBoardAt(j, i + 3) == check
                        ) {
                            return check == playerOneTile ? 1 : 2;
                        }
                    /*
                    if (getBoardAt(j, i) == 'X' && getBoardAt(j, i + 1) == 'X' &&
                            getBoardAt(j, i + 2) == 'X' && getBoardAt(j, i + 3) == 'X')
                    {
                        return 1;
                    }
                    else if(getBoardAt(j, i) == 'O' && getBoardAt(j, i + 1) == 'O' &&
                            getBoardAt(j, i + 2) == 'O' && getBoardAt(j, i + 3) == 'O')
                    {
                        return 2;
                    }
                     */
                    }
                    //diagonal win
                    if (i + 3 < y && j + 3 < x) {
                        if (
                                getBoardAt(j+1, i + 1) == check
                                && getBoardAt(j+2, i + 2) == check
                                && getBoardAt(j+3, i + 3) == check
                        ) {
                            return check == playerOneTile ? 1 : 2;
                        }
                        /*
                        if (getBoardAt(j, i) == 'X' && getBoardAt(j + 1, i + 1) == 'X' &&
                                getBoardAt(j + 2, i + 2) == 'X' && getBoardAt(j + 3, i + 3) == 'X') {
                            return 1;
                        } else if (getBoardAt(j, i) == 'O' && getBoardAt(j + 1, i + 1) == 'O' &&
                                getBoardAt(j + 2, i + 2) == 'O' && getBoardAt(j + 3, i + 3) == 'O') {
                            return 2;
                        }
                         */
                    }
                    if (i - 3 > 0 && j + 3 < x) {
                        if (
                                getBoardAt(j+1, i - 1) == check
                                        && getBoardAt(j+2, i - 2) == check
                                        && getBoardAt(j+3, i - 3) == check
                        ) {
                            return check == playerOneTile ? 1 : 2;
                        }
                        /*
                        if (getBoardAt(j, i) == 'X' && getBoardAt(j + 1, i - 1) == 'X' &&
                                getBoardAt(j + 2, i - 2) == 'X' && getBoardAt(j + 3, i - 3) == 'X') {
                            return 1;
                        } else if (getBoardAt(j, i) == 'O' && getBoardAt(j + 1, i - 1) == 'O' &&
                                getBoardAt(j + 2, i - 2) == 'O' && getBoardAt(j + 3, i - 3) == 'O') {
                            return 2;
                        }
                         */
                    }
                }
            }
        }
        return 0;
    }

    public String getBoard() {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {

                if (j == 0) string.append(" | ");

                string.append(getBoardAt(j, i));
                if (j < x - 1) {
                    string.append(" - ");
                } else {
                    string.append(" | ");
                }
            }

            if (i < y - 1) string.append("\n");

        }

        return string.toString();
    }

    public String getPlayGuide() {
        StringBuilder string = new StringBuilder();
        string.append(" + ");
        for (int i = 0; i < x; i++) {
            string.append(i + 1);
            if (i < x - 1) string.append(" - ");
        }
        string.append(" + ");
        return string.toString();
    }
}
