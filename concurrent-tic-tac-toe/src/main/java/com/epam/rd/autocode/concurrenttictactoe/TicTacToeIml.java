package com.epam.rd.autocode.concurrenttictactoe;

public class TicTacToeIml implements TicTacToe {
    final char[][] tableForGame;
    private char lastMarkToAdd = ' ';

    public TicTacToeIml() {
        tableForGame = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tableForGame[i][j] = ' ';
            }
        }
    }

    @Override
    public void setMark(int x, int y, char mark) {
        if (tableForGame[x][y] == ' ') {
            tableForGame[x][y] = mark;
            lastMarkToAdd = mark;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public char[][] table() {
        char[][] copyTable = new char[3][3];
        for (int i = 0; i<3; i++){
            for (int j= 0; j<3; j++){
                copyTable[i][j] = tableForGame[i][j];
            }
        }
        return copyTable;
    }

    @Override
    public char lastMark() {
        return lastMarkToAdd;
    }
}
