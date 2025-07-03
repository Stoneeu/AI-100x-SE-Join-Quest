# 讓 Cucumber 測試能被 Surefire 正確執行

Maven Surefire 預設只會執行符合下列命名規則的測試類別：
- `*Test.java`
- `*Tests.java`
- `*TestCase.java`

目前 Cucumber runner 類別為 `RunCucumberTest.java`，但 Surefire 只會執行預設 package 下的 runner，且 `@Cucumber` runner 需放在 `src/test/java` 下，且名稱需符合上述規則。

## 解決方案
1. **移除 `steps/RunCucumberTest.java`，只保留預設 package 下的 `RunCucumberTest.java`**
2. **確保 `RunCucumberTest.java` 沒有 package 宣告，並放在 `src/test/java/` 根目錄**
3. **名稱必須為 `*Test.java`**

---

- 目前 `RunCucumberTest.java` 已正確放在根目錄且無 package。
- 但 feature 路徑與 glue 需在 `cucumber.properties` 設定。
- 若還是無法執行，請檢查 Surefire plugin 設定。

## 參考
- https://cucumber.io/docs/guides/10-minute-tutorial/
- https://maven.apache.org/surefire/maven-surefire-plugin/examples/inclusion-exclusion.html
