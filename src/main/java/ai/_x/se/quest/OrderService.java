package ai._x.se.quest;

import java.util.*;

public class OrderService {
    // 最小實作，僅支援單一商品、無優惠
    public Map<String, Object> placeOrder(List<Map<String, String>> items,
            List<Map<String, String>> promotions) {
        return placeOrder(items, promotions, false, false);
    }

    public Map<String, Object> placeOrder(List<Map<String, String>> items,
            List<Map<String, String>> promotions, boolean bogoCosmeticsActive) {
        return placeOrder(items, promotions, bogoCosmeticsActive, false);
    }

    // 新增 double11Active 參數
    public Map<String, Object> placeOrder(List<Map<String, String>> items,
            List<Map<String, String>> promotions, boolean bogoCosmeticsActive,
            boolean double11Active) {
        Map<String, Object> result = new HashMap<>();
        int totalAmount = 0;
        int originalAmount = 0;
        int discount = 0;
        List<Map<String, String>> received = new ArrayList<>();

        // 雙十一優惠計算
        if (double11Active) {
            for (Map<String, String> item : items) {
                int quantity = Integer.parseInt(item.getOrDefault("quantity", "1"));
                int unitPrice = Integer.parseInt(item.getOrDefault("unitPrice", "0"));
                String productName = item.get("productName");
                int group = quantity / 10;
                int remain = quantity % 10;
                int subtotal = group * 10 * unitPrice * 80 / 100 + remain * unitPrice;
                totalAmount += subtotal;
                Map<String, String> receivedItem = new HashMap<>();
                receivedItem.put("productName", productName);
                receivedItem.put("quantity", String.valueOf(quantity));
                received.add(receivedItem);
            }
            result.put("totalAmount", totalAmount);
            result.put("items", received);
            return result;
        }

        // 原有邏輯
        for (Map<String, String> item : items) {
            int quantity = Integer.parseInt(item.getOrDefault("quantity", "1"));
            int unitPrice = Integer.parseInt(item.getOrDefault("unitPrice", "0"));
            String category = item.getOrDefault("category", "");
            int receivedQty = quantity;
            // 處理 bogo for cosmetics
            if (bogoCosmeticsActive && "cosmetics".equalsIgnoreCase(category)) {
                // 買一送一：每個訂單行項目送一件免費
                receivedQty = quantity + 1;
            }
            originalAmount += quantity * unitPrice;
            Map<String, String> receivedItem = new HashMap<>();
            receivedItem.put("productName", item.get("productName"));
            receivedItem.put("quantity", String.valueOf(receivedQty));
            received.add(receivedItem);
        }
        // 處理 threshold discount promotion
        if (promotions != null) {
            for (Map<String, String> promo : promotions) {
                if (promo.containsKey("threshold") && promo.containsKey("discount")) {
                    int threshold = Integer.parseInt(promo.get("threshold"));
                    int promoDiscount = Integer.parseInt(promo.get("discount"));
                    if (originalAmount >= threshold) {
                        discount += promoDiscount;
                    }
                }
            }
        }
        totalAmount = originalAmount - discount;
        if (discount > 0)
            result.put("discount", discount);
        if (originalAmount > 0 && discount > 0)
            result.put("originalAmount", originalAmount);
        result.put("totalAmount", totalAmount);
        result.put("items", received);
        return result;
    }
}
