
package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameCreationSteps {
    private Map<String, Object> game = new HashMap<>();
    private List<Map<String, String>> players = new ArrayList<>();
    private String gameState;
    private boolean requestFailed = false;

    @Given("no existing game")
    public void no_existing_game() {
        game.clear();
        players.clear();
        gameState = null;
        requestFailed = false;
    }

    @When("a player joins a game with:")
    public void a_player_joins_a_game_with(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String gameId = row.get("gameId");
            String playerName = row.get("playerName");
            if (game.isEmpty()) {
                game.put("gameId", gameId);
                gameState = "Waiting for player";
                Map<String, String> player = new HashMap<>();
                player.put("playerId", "P1");
                player.put("playerName", playerName);
                players.add(player);
            } else if (players.size() == 1) {
                gameState = "Setting secret";
                Map<String, String> player = new HashMap<>();
                player.put("playerId", "P2");
                player.put("playerName", playerName);
                players.add(player);
            } else {
                requestFailed = true;
            }
        }
    }

    @Then("a new game should be created with:")
    public void a_new_game_should_be_created_with(DataTable dataTable) {
        List<Map<String, String>> expected = dataTable.asMaps(String.class, String.class);
        assertEquals(expected.get(0).get("gameId"), game.get("gameId"));
        assertEquals(expected.get(0).get("state"), gameState);
    }

    @Then("the game '{word}' should have 1 player:")
    public void the_game_should_have_1_player(String gameId, DataTable dataTable) {
        List<Map<String, String>> expected = dataTable.asMaps(String.class, String.class);
        assertEquals(1, players.size());
        String expId = expected.get(0).getOrDefault("playerId", expected.get(0).get("id"));
        String expName = expected.get(0).getOrDefault("playerName", expected.get(0).get("name"));
        assertEquals(expId, players.get(0).get("playerId"));
        assertEquals(expName, players.get(0).get("playerName"));
    }

    @Given("scenario Game Creation: Create a new game")
    public void scenario_create_a_new_game() {
        no_existing_game();
        // 直接呼叫業務邏輯而非建立 DataTable
        if (game.isEmpty()) {
            game.put("gameId", "ABC");
            gameState = "Waiting for player";
            Map<String, String> player = new HashMap<>();
            player.put("playerId", "P1");
            player.put("playerName", "Johnny");
            players.add(player);
        }
    }

    @Then("the game '{word}' should be:")
    public void the_game_should_be(String gameId, DataTable dataTable) {
        List<Map<String, String>> expected = dataTable.asMaps(String.class, String.class);
        assertEquals(expected.get(0).get("state"), gameState);
    }

    @Then("the game '{word}' should have 2 players:")
    public void the_game_should_have_2_players(String gameId, DataTable dataTable) {
        List<Map<String, String>> expected = dataTable.asMaps(String.class, String.class);
        assertEquals(2, players.size());
        for (int i = 0; i < 2; i++) {
            String expId = expected.get(i).getOrDefault("id", expected.get(i).get("playerId"));
            String expName =
                    expected.get(i).getOrDefault("name", expected.get(i).get("playerName"));
            assertEquals(expId, players.get(i).get("playerId"));
            assertEquals(expName, players.get(i).get("playerName"));
        }
    }

    @Given("scenario Game Creation: P2 Join an existing game")
    public void scenario_p2_join_an_existing_game() {
        scenario_create_a_new_game();
        // 直接呼叫業務邏輯而非建立 DataTable
        if (players.size() == 1) {
            gameState = "Setting secret";
            Map<String, String> player = new HashMap<>();
            player.put("playerId", "P2");
            player.put("playerName", "Sean");
            players.add(player);
        }
    }

    @Then("the request fails")
    public void the_request_fails() {
        assertTrue(requestFailed);
    }
}
