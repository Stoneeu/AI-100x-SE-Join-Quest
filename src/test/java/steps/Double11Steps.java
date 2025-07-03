package steps;

import ai._x.se.quest.OrderService;
import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class Double11Steps {
    private List<Map<String, String>> items;
    private List<Map<String, String>> promotions;
    private Map<String, Object> result;
    private boolean double11Active = false;

    @Given("雙十一優惠活動已啟動")
    public void activateDouble11() {
        double11Active = true;
    }

    @When("客戶下單:")
    public void customerPlacesOrder(DataTable table) {
        items = table.asMaps(String.class, String.class);
        promotions = new ArrayList<>();
        OrderService service = new OrderService();
        // 新增 double11Active 參數，bogoCosmeticsActive 設為 false
        result = service.placeOrder(items, promotions, false, double11Active);
    }

    @Then("訂單總價應為:")
    public void orderTotalShouldBe(DataTable table) {
        List<Map<String, String>> expected = table.asMaps(String.class, String.class);
        int expectedTotal = Integer.parseInt(expected.get(0).get("totalAmount"));
        assertEquals(expectedTotal, ((Number) result.get("totalAmount")).intValue());
    }

    @And("客戶應收到:")
    public void customerShouldReceive(DataTable table) {
        List<Map<String, String>> expected = table.asMaps(String.class, String.class);
        List<Map<String, String>> actual = (List<Map<String, String>>) result.get("items");
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).get("productName"), actual.get(i).get("productName"));
            assertEquals(expected.get(i).get("quantity"), actual.get(i).get("quantity"));
        }
    }
}
