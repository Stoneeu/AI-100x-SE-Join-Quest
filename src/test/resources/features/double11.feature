@double11_promotion
Feature: 雙十一優惠活動
  作為一個消費者
  我希望在雙十一活動期間，訂單能自動套用每 10 件同商品 8 折的優惠
  讓我能享受更多折扣

  Scenario: 購買 12 件同商品享有 10 件 8 折優惠
    Given 雙十一優惠活動已啟動
    When 客戶下單:
      | productName | quantity | unitPrice |
      | 襪子          | 12       | 100       |
    Then 訂單總價應為:
      | totalAmount |
      | 1000        |
    And 客戶應收到:
      | productName | quantity |
      | 襪子          | 12       |

  Scenario: 購買 27 件同商品享有 20 件 8 折優惠
    Given 雙十一優惠活動已啟動
    When 客戶下單:
      | productName | quantity | unitPrice |
      | 襪子          | 27       | 100       |
    Then 訂單總價應為:
      | totalAmount |
      | 2300        |
    And 客戶應收到:
      | productName | quantity |
      | 襪子          | 27       |

  Scenario: 購買 10 種不同商品各一件不享有折扣
    Given 雙十一優惠活動已啟動
    When 客戶下單:
      | productName | quantity | unitPrice |
      | A           | 1        | 100       |
      | B           | 1        | 100       |
      | C           | 1        | 100       |
      | D           | 1        | 100       |
      | E           | 1        | 100       |
      | F           | 1        | 100       |
      | G           | 1        | 100       |
      | H           | 1        | 100       |
      | I           | 1        | 100       |
      | J           | 1        | 100       |
    Then 訂單總價應為:
      | totalAmount |
      | 1000        |
    And 客戶應收到:
      | productName | quantity |
      | A           | 1        |
      | B           | 1        |
      | C           | 1        |
      | D           | 1        |
      | E           | 1        |
      | F           | 1        |
      | G           | 1        |
      | H           | 1        |
      | I           | 1        |
      | J           | 1        |
