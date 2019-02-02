package steps;

import com.example.rubenfilipe.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class LoginSteps extends GreenCoffeeSteps {


    @Given("^I see an empty login form$")
    public void i_see_an_empty_login_form() {
        onViewWithId(R.id.loginEmail).isEmpty();
        onViewWithId(R.id.loginPassword).isEmpty();
    }

    @When("^i click on the cancel button$")
    public void i_click_on_the_cancel_button() {
        onViewWithId(R.id.buttonCancel).click();
    }

    @Then("^i see the GuestDashboardActivity$")
    public void i_see_the_GuestDashboardActivity() {
        onViewWithId(R.id.buttonGuestDashboardLogin).isDisplayed();
    }

    @When("^I introduce as username \"([^\"]*)\"$")
    public void i_introduce_as_username(String arg1) {
        onViewWithId(R.id.loginEmail).type(arg1);
        closeKeyboard();
    }

    @When("^I introduce as password \"([^\"]*)\"$")
    public void i_introduce_as_password(String arg1) {
        onViewWithId(R.id.loginPassword).type(arg1);
        closeKeyboard();
    }

    @When("^I press the login button$")
    public void i_press_the_login_button() {
        onViewWithId(R.id.buttonLogin).click();
    }

    @Then("^I see an error message saying 'Invalid credentials'$")
    public void i_see_an_error_message_saying_Invalid_credentials() {
        waitFor(1000);
        onViewWithText(R.string.invalidCredentials).isDisplayed();
        onViewWithText("Invalid credentials").isDisplayed();
    }

    @When("^I introduce a valid username \"([^\"]*)\"$")
    public void i_introduce_a_valid_username(String arg1) {
        onViewWithId(R.id.loginEmail).type("ruben@hotmail.com");
        closeKeyboard();
    }

    @When("^I introduce a valid password \"([^\"]*)\"$")
    public void i_introduce_a_valid_password(String arg1) {
        onViewWithId(R.id.loginPassword).type("123456789");
        closeKeyboard();
    }

    @Then("^I see the dashboard screen$")
    public void i_see_the_dashboard_screen() {
        waitFor(1000);
        onViewWithText(R.string.dashboardTitle).isDisplayed();
    }

}

