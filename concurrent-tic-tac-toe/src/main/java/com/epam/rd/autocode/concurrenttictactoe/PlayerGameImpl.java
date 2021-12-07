package com.epam.rd.autocode.concurrenttictactoe;

public class PlayerGameImpl implements Player {
    private final TicTacToe ticTacToe;
    private final char mark;
    private final PlayerStrategy strategy;

    public PlayerGameImpl(TicTacToe ticTacToe, char mark, PlayerStrategy strategy) {
        this.ticTacToe = ticTacToe;
        this.mark = mark;
        this.strategy = strategy;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (ticTacToe) {
                    if ((ticTacToe.lastMark() == mark) || (ticTacToe.lastMark() == ' ' && mark == 'O')) {
                        ticTacToe.wait();
                        if (gameFinished(ticTacToe.table())) {
                            return;
                        }
                    }
                    try {
                        Move movePlayer = strategy.computeMove(mark, ticTacToe);
                        ticTacToe.setMark(movePlayer.row, movePlayer.column, mark);
                        if (gameFinished(ticTacToe.table())) {
                            ticTacToe.notify();
                            return;
                        }
                    } catch (IllegalArgumentException e) {
                        return;
                    }
                    ticTacToe.notify();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            ticTacToe.notify();
        }
    }

    public boolean gameFinished(char[][] tableForGame) {
        if ((tableForGame[0][0] != ' ') && (tableForGame[0][0] == tableForGame[1][1]
                && tableForGame[1][1] == tableForGame[2][2])) {
            return true;
        }
        if ((tableForGame[2][0] != ' ') && (tableForGame[2][0] == tableForGame[1][1]
                && tableForGame[1][1] == tableForGame[0][2])) {
            return true;
        }
        for (int i = 0; i < 3; i++) {
            if ((tableForGame[i][0] != ' ') && (tableForGame[i][0] == tableForGame[i][1]
                    && tableForGame[i][1] == tableForGame[i][2])) {
                return true;
            }
            if ((tableForGame[0][i] != ' ') && (tableForGame[0][i] == tableForGame[1][i]
                    && tableForGame[1][i] == tableForGame[2][i])) {
                return true;
            }
        }
        return false;
    }
}
