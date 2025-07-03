package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class ExampleSteps {
    @Given("I have an example precondition")
    public void i_have_an_example_precondition() {
        // 實作前置條件
    }

    @When("I perform an example action")
    public void i_perform_an_example_action() {
        // 實作動作
    }

    @Then("I expect an example result")
    public void i_expect_an_example_result() {
        // 實作驗證
    }
}
