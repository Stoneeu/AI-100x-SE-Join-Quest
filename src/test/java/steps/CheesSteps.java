package steps;

import ai._x.se.quest.CheesService;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class CheesSteps {
    CheesService service = new CheesService();
    String lastPiece;
    int lastFromRow, lastFromCol, lastToRow, lastToCol;
    boolean lastLegal;
    boolean lastWin;

    @Given("^the board is empty except for a (.+) at \\((\\d+), (\\d+)\\)$")
    public void the_board_is_empty_except_for_a_piece_at(String piece, int row, int col) {
        service.clearBoard();
        service.placePiece(piece, row, col);
        lastPiece = piece;
    }

    @Given("the board has:")
    public void the_board_has(io.cucumber.datatable.DataTable dataTable) {
        service.clearBoard();
        for (Map<String, String> row : dataTable.asMaps()) {
            String piece = row.get("Piece");
            String pos = row.get("Position");
            pos = pos.replace("(", "").replace(")", "");
            String[] parts = pos.split(",");
            int r = Integer.parseInt(parts[0].trim());
            int c = Integer.parseInt(parts[1].trim());
            service.placePiece(piece, r, c);
        }
    }

    @When("^Red moves the (.+) from \\((\\d+), (\\d+)\\) to \\((\\d+), (\\d+)\\)$")
    public void red_moves_the_piece_from_to(String piece, int fromRow, int fromCol, int toRow,
            int toCol) {
        lastPiece = piece;
        lastFromRow = fromRow;
        lastFromCol = fromCol;
        lastToRow = toRow;
        lastToCol = toCol;
        lastLegal = service.isLegalMove("Red " + piece, fromRow, fromCol, toRow, toCol);
        lastWin = service.isWinningMove("Red " + piece, toRow, toCol);
    }

    @Then("the move is legal")
    public void the_move_is_legal() {
        assertTrue(lastLegal);
    }

    @Then("the move is illegal")
    public void the_move_is_illegal() {
        assertFalse(lastLegal);
    }

    @Then("Red wins immediately")
    public void red_wins_immediately() {
        assertTrue(lastWin);
    }

    @Then("the game is not over just from that capture")
    public void the_game_is_not_over_just_from_that_capture() {
        assertFalse(lastWin);
    }
}
