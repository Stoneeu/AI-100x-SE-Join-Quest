package steps;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(expected.get(0).get("playerId"), players.get(0).get("playerId"));
        assertEquals(expected.get(0).get("playerName"), players.get(0).get("playerName"));
    }

    @Given("scenario Game Creation: Create a new game")
    public void scenario_create_a_new_game() {
        no_existing_game();
        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList("gameId", "playerName"));
        rows.add(Arrays.asList("ABC", "Johnny"));
        DataTable table = DataTable.create(rows);
        a_player_joins_a_game_with(table);
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
            assertEquals(expected.get(i).get("id"), players.get(i).get("playerId"));
            assertEquals(expected.get(i).get("name"), players.get(i).get("playerName"));
        }
    }

    @Given("scenario Game Creation: P2 Join an existing game")
    public void scenario_p2_join_an_existing_game() {
        scenario_create_a_new_game();
        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList("gameId", "playerName"));
        rows.add(Arrays.asList("ABC", "Sean"));
        DataTable table = DataTable.create(rows);
        a_player_joins_a_game_with(table);
    }

    @Then("the request fails")
    public void the_request_fails() {
        assertTrue(requestFailed);
    }
}
