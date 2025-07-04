package ai._x.se.quest;

import java.util.*;

public class ChessService {
    public static final int ROWS = 10;
    public static final int COLS = 9;

    public enum PieceType {
        GENERAL, GUARD, ROOK, HORSE, CANNON, ELEPHANT, SOLDIER, NONE
    }

    public enum Color {
        RED, BLACK, NONE
    }

    public static class Piece {
        public PieceType type;
        public Color color;

        public Piece(PieceType type, Color color) {
            this.type = type;
            this.color = color;
        }
    }

    public static class Position {
        public int row, col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Position))
                return false;
            Position p = (Position) o;
            return row == p.row && col == p.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    public static class Board {
        public Piece[][] board = new Piece[ROWS + 1][COLS + 1];

        public Board() {
            for (int i = 1; i <= ROWS; i++)
                for (int j = 1; j <= COLS; j++)
                    board[i][j] = new Piece(PieceType.NONE, Color.NONE);
        }

        public void setPiece(int row, int col, Piece piece) {
            board[row][col] = piece;
        }

        public Piece getPiece(int row, int col) {
            return board[row][col];
        }

        public void removePiece(int row, int col) {
            board[row][col] = new Piece(PieceType.NONE, Color.NONE);
        }
    }

    public Board board = new Board();

    public void clearBoard() {
        board = new Board();
    }

    public void placePiece(String pieceName, int row, int col) {
        PieceType type = parsePieceType(pieceName);
        Color color = parseColor(pieceName);
        board.setPiece(row, col, new Piece(type, color));
    }

    private PieceType parsePieceType(String name) {
        if (name.contains("General"))
            return PieceType.GENERAL;
        if (name.contains("Guard"))
            return PieceType.GUARD;
        if (name.contains("Rook"))
            return PieceType.ROOK;
        if (name.contains("Horse"))
            return PieceType.HORSE;
        if (name.contains("Cannon"))
            return PieceType.CANNON;
        if (name.contains("Elephant"))
            return PieceType.ELEPHANT;
        if (name.contains("Soldier") || name.contains("Pawn"))
            return PieceType.SOLDIER;
        return PieceType.NONE;
    }

    private Color parseColor(String name) {
        if (name.contains("Red"))
            return Color.RED;
        if (name.contains("Black"))
            return Color.BLACK;
        return Color.NONE;
    }

    public boolean isLegalMove(String pieceName, int fromRow, int fromCol, int toRow, int toCol) {
        PieceType type = parsePieceType(pieceName);
        Color color = parseColor(pieceName);
        if (type == PieceType.GENERAL) {
            // 先檢查基本規則
            boolean legal = isLegalGeneralMove(color, fromRow, fromCol, toRow, toCol);
            if (!legal)
                return false;
            // 模擬移動後，檢查將帥是否同一路徑相望
            Piece backup = board.getPiece(toRow, toCol);
            board.setPiece(toRow, toCol, board.getPiece(fromRow, fromCol));
            board.removePiece(fromRow, fromCol);
            boolean facing = generalsFacing();
            // 還原
            board.setPiece(fromRow, fromCol, board.getPiece(toRow, toCol));
            board.setPiece(toRow, toCol, backup);
            if (facing)
                return false;
            return true;
        } else if (type == PieceType.GUARD) {
            return isLegalGuardMove(color, fromRow, fromCol, toRow, toCol);
        } else if (type == PieceType.ROOK) {
            return isLegalRookMove(color, fromRow, fromCol, toRow, toCol);
        } else if (type == PieceType.HORSE) {
            return isLegalHorseMove(color, fromRow, fromCol, toRow, toCol);
        } else if (type == PieceType.CANNON) {
            return isLegalCannonMove(color, fromRow, fromCol, toRow, toCol);
        } else if (type == PieceType.ELEPHANT) {
            return isLegalElephantMove(color, fromRow, fromCol, toRow, toCol);
        } else if (type == PieceType.SOLDIER) {
            return isLegalSoldierMove(color, fromRow, fromCol, toRow, toCol);
        } else {
            return false;
        }
    }

    // 檢查將帥是否同一路徑相望
    private boolean generalsFacing() {
        int genCol = -1, redRow = -1, blackRow = -1;
        // 找出紅黑將位置
        for (int row = 1; row <= ROWS; row++) {
            for (int col = 1; col <= COLS; col++) {
                Piece p = board.getPiece(row, col);
                if (p.type == PieceType.GENERAL) {
                    if (p.color == Color.RED) {
                        redRow = row;
                        genCol = col;
                    } else if (p.color == Color.BLACK) {
                        blackRow = row;
                        genCol = col;
                    }
                }
            }
        }
        if (genCol == -1 || redRow == -1 || blackRow == -1)
            return false;
        if (genCol < 1 || genCol > COLS)
            return false;
        // 檢查兩將是否同一路徑
        if (board.getPiece(redRow, genCol).type != PieceType.GENERAL
                || board.getPiece(blackRow, genCol).type != PieceType.GENERAL)
            return false;
        int min = Math.min(redRow, blackRow), max = Math.max(redRow, blackRow);
        for (int r = min + 1; r < max; r++) {
            if (board.getPiece(r, genCol).type != PieceType.NONE)
                return false;
        }
        return true;
    }

    // --- 各棋子規則 (僅部分，後續補齊) ---
    private boolean isLegalGeneralMove(Color color, int fromRow, int fromCol, int toRow,
            int toCol) {
        // 宮範圍
        int minRow = (color == Color.RED) ? 1 : 8;
        int maxRow = (color == Color.RED) ? 3 : 10;
        if (toRow < minRow || toRow > maxRow || toCol < 4 || toCol > 6)
            return false;
        // 只能走一步直或橫
        int dr = Math.abs(fromRow - toRow), dc = Math.abs(fromCol - toCol);
        if (dr + dc != 1)
            return false;
        return true;
    }

    private boolean isLegalGuardMove(Color color, int fromRow, int fromCol, int toRow, int toCol) {
        int minRow = (color == Color.RED) ? 1 : 8;
        int maxRow = (color == Color.RED) ? 3 : 10;
        if (toRow < minRow || toRow > maxRow || toCol < 4 || toCol > 6)
            return false;
        int dr = Math.abs(fromRow - toRow), dc = Math.abs(fromCol - toCol);
        if (dr == 1 && dc == 1)
            return true;
        return false;
    }

    private boolean isLegalRookMove(Color color, int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol)
            return false;
        int dr = Integer.signum(toRow - fromRow), dc = Integer.signum(toCol - fromCol);
        int r = fromRow + dr, c = fromCol + dc;
        while (r != toRow || c != toCol) {
            if (board.getPiece(r, c).type != PieceType.NONE)
                return false;
            r += dr;
            c += dc;
        }
        return true;
    }

    private boolean isLegalHorseMove(Color color, int fromRow, int fromCol, int toRow, int toCol) {
        int dr = toRow - fromRow, dc = toCol - fromCol;
        if (!((Math.abs(dr) == 2 && Math.abs(dc) == 1) || (Math.abs(dr) == 1 && Math.abs(dc) == 2)))
            return false;
        // 馬腿判斷
        if (Math.abs(dr) == 2) {
            int legRow = fromRow + dr / 2;
            if (board.getPiece(legRow, fromCol).type != PieceType.NONE)
                return false;
        } else {
            int legCol = fromCol + dc / 2;
            if (board.getPiece(fromRow, legCol).type != PieceType.NONE)
                return false;
        }
        return true;
    }

    private boolean isLegalCannonMove(Color color, int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol)
            return false;
        int dr = Integer.signum(toRow - fromRow), dc = Integer.signum(toCol - fromCol);
        int r = fromRow + dr, c = fromCol + dc, screens = 0;
        while (r != toRow || c != toCol) {
            if (board.getPiece(r, c).type != PieceType.NONE)
                screens++;
            r += dr;
            c += dc;
        }
        Piece target = board.getPiece(toRow, toCol);
        if (target.type == PieceType.NONE) {
            return screens == 0;
        } else {
            return screens == 1;
        }
    }

    private boolean isLegalElephantMove(Color color, int fromRow, int fromCol, int toRow,
            int toCol) {
        int dr = toRow - fromRow, dc = toCol - fromCol;
        if (Math.abs(dr) != 2 || Math.abs(dc) != 2)
            return false;
        // 不可過河
        if (color == Color.RED && toRow > 5)
            return false;
        if (color == Color.BLACK && toRow < 6)
            return false;
        // 象眼判斷
        int midRow = fromRow + dr / 2, midCol = fromCol + dc / 2;
        if (board.getPiece(midRow, midCol).type != PieceType.NONE)
            return false;
        return true;
    }

    private boolean isLegalSoldierMove(Color color, int fromRow, int fromCol, int toRow,
            int toCol) {
        int dr = toRow - fromRow, dc = toCol - fromCol;
        if (color == Color.RED) {
            if (fromRow <= 5) {
                // 未過河
                if (dr != 1 || dc != 0)
                    return false;
            } else {
                // 過河
                if (!((dr == 1 && dc == 0) || (dr == 0 && Math.abs(dc) == 1)))
                    return false;
            }
        } else {
            if (fromRow >= 6) {
                if (dr != -1 || dc != 0)
                    return false;
            } else {
                if (!((dr == -1 && dc == 0) || (dr == 0 && Math.abs(dc) == 1)))
                    return false;
            }
        }
        return true;
    }

    public boolean isWinningMove(String pieceName, int toRow, int toCol) {
        PieceType type = parsePieceType(pieceName);
        Piece target = board.getPiece(toRow, toCol);
        return type == PieceType.ROOK && target.type == PieceType.GENERAL;
    }
}
