


package steps;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import static org.junit.jupiter.api.Assertions.*;

import ai._x.se.quest.OrderService;
import java.util.*;

public class OrderSteps {
    private OrderService orderService = new OrderService();
    private Map<String, Object> orderResult;
    private List<Map<String, String>> promotions = new ArrayList<>();
    private boolean bogoCosmeticsActive = false;

    @Given("the buy one get one promotion for cosmetics is active")
    public void the_buy_one_get_one_promotion_for_cosmetics_is_active() {
        bogoCosmeticsActive = true;
    }

    @Given("no promotions are applied")
    public void no_promotions_are_applied() {
        // 預設不啟用任何優惠
    }

    @When("a customer places an order with:")
    public void a_customer_places_an_order_with(DataTable table) {
        List<Map<String, String>> items = table.asMaps(String.class, String.class);
        orderResult = orderService.placeOrder(items, promotions, bogoCosmeticsActive);
    }

    @Given("the threshold discount promotion is configured:")
    public void the_threshold_discount_promotion_is_configured(DataTable table) {
        promotions.clear();
        promotions.addAll(table.asMaps(String.class, String.class));
    }

    @Then("the order summary should be:")
    public void the_order_summary_should_be(DataTable table) {
        List<Map<String, String>> expected = table.asMaps(String.class, String.class);
        Map<String, String> expectedSummary = expected.get(0);
        for (String key : expectedSummary.keySet()) {
            assertEquals(expectedSummary.get(key), String.valueOf(orderResult.get(key)));
        }
    }

    @Then("the customer should receive:")
    public void the_customer_should_receive(DataTable table) {
        List<Map<String, String>> expected = table.asMaps(String.class, String.class);
        List<Map<String, String>> actual = (List<Map<String, String>>) orderResult.get("items");
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            for (String key : expected.get(i).keySet()) {
                assertEquals(expected.get(i).get(key), actual.get(i).get(key));
            }
        }
    }
}
