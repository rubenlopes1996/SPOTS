package steps;

import com.example.rubenfilipe.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class GuestDashboardSteps extends GreenCoffeeSteps {
    @When("^I open the dashboard$")
    public void i_open_the_dashboard() {
        onViewWithText(R.string.guestDashboardTitle).isDisplayed();
    }

    @When("^i see a map$")
    public void i_see_a_map() {
        onViewWithId(R.id.guestDashboardMap).isDisplayed();
    }

    @Then("^I see the Login Register button$")
    public void i_see_the_Login_Register_button() {
        onViewWithId(R.id.buttonGuestDashboardLogin).isDisplayed();
    }
}
