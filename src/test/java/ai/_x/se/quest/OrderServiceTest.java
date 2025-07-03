package ai._x.se.quest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class OrderServiceTest {

    @Test
    public void testBasicOrder() {
        OrderService orderService = new OrderService();

        List<Map<String, String>> items = new ArrayList<>();
        Map<String, String> item = new HashMap<>();
        item.put("productName", "T-shirt");
        item.put("quantity", "1");
        item.put("unitPrice", "500");
        items.add(item);

        Map<String, Object> result = orderService.placeOrder(items, null, false);

        assertEquals(500, result.get("totalAmount"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> resultItems = (List<Map<String, String>>) result.get("items");
        assertEquals(1, resultItems.size());
        assertEquals("T-shirt", resultItems.get(0).get("productName"));
        assertEquals("1", resultItems.get(0).get("quantity"));
    }

    @Test
    public void testBuyOneGetOneCosmetics() {
        OrderService orderService = new OrderService();

        List<Map<String, String>> items = new ArrayList<>();
        Map<String, String> item = new HashMap<>();
        item.put("productName", "口紅");
        item.put("category", "cosmetics");
        item.put("quantity", "1");
        item.put("unitPrice", "300");
        items.add(item);

        Map<String, Object> result = orderService.placeOrder(items, null, true);

        assertEquals(300, result.get("totalAmount"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> resultItems = (List<Map<String, String>>) result.get("items");
        assertEquals(1, resultItems.size());
        assertEquals("口紅", resultItems.get(0).get("productName"));
        assertEquals("2", resultItems.get(0).get("quantity")); // 買一送一
    }
}
