public class Board {

    private final int x = 7;
    private final int y = 6;

    char[][] board = new char[x][y];

    public Board() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public char getBoardAtXY(int x, int y) {
        return board[x][y];
    }

    public boolean checkMove(int x, int player){
        char playerOneTile = 'X';
        char playerTwoTile = 'O';

        for (int i = 0; i < y; i++) {
            if (board[x][y - 1 - i] != playerOneTile && board[x][y - 1 - i] != playerTwoTile) {
                return true;
            }
        }
        return false;
    }


    public int makeMove(int x, int player) {
        char playerOneTile = 'X';
        char playerTwoTile = 'O';

        char playerTile = playerOneTile;
        if (player != 1) playerTile = playerTwoTile;

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

    private char getB(int x, int y) {
        return board[x][y];
    }

    public int findWinCondition() {
        //1 is human win, 2 is computer win, 0 is no win / tie
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                //horizontal win
                if (j + 3 < x) {
                    if (getB(j, i) == 'X' && getB(j + 1, i) == 'X' &&
                            getB(j + 2, i) == 'X' && getB(j + 3, i) == 'X')
                    {
                        return 1;
                    }
                    else if (getB(j, i) == 'O' && getB(j + 1, i) == 'O' &&
                            getB(j + 2, i) == 'O' && getB(j + 3, i) == 'O')
                    {
                        return 2;
                    }
                }
                //vertical win
                if (i + 3 < y) {
                    if (getB(j, i) == 'X' && getB(j, i + 1) == 'X' &&
                            getB(j, i + 2) == 'X' && getB(j, i + 3) == 'X')
                    {
                        return 1;
                    }
                    else if(getB(j, i) == 'O' && getB(j, i + 1) == 'O' &&
                            getB(j, i + 2) == 'O' && getB(j, i + 3) == 'O')
                    {
                        return 2;
                    }
                }
                //diagonal win
                if (i + 3 < y && j + 3 < x) {
                    if (getB(j, i) == 'X' && getB(j+1, i+1) == 'X' &&
                            getB(j+2, i+2) == 'X' && getB(j+3, i+3) == 'X')
                    {
                        return 1;
                    }
                    else if(getB(j, i) == 'O' && getB(j+1, i + 1) == 'O' &&
                            getB(j+2, i + 2) == 'O' && getB(j+3, i + 3) == 'O')
                    {
                        return 2;
                    }
                }
                if (i - 3 > 0 && j + 3 < x) {
                    if (getB(j, i) == 'X' && getB(j+1, i-1) == 'X' &&
                            getB(j+2, i-2) == 'X' && getB(j+3, i-3) == 'X')
                    {
                        return 1;
                    }
                    else if(getB(j, i) == 'O' && getB(j+1, i-1) == 'O' &&
                            getB(j+2, i-2) == 'O' && getB(j+3, i-3) == 'O')
                    {
                        return 2;
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

                string.append(getB(j, i));
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
