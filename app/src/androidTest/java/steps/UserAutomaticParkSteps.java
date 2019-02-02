package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class UserAutomaticParkSteps extends GreenCoffeeSteps {
    @When("^I see the Dashboard activity$")
    public void i_see_the_Dashboard_activity() {
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        onViewWithId(R.id.loginEmail).type("sergiotrindade100@gmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
        waitFor(3000);
    }

    @When("^Allow permission to get my location$")
    public void allow_permission_to_get_my_location() {

    }

    @When("^I receive a notification in the nearest spot avaliable$")
    public void i_receive_a_notification_in_the_nearest_spot_avaliable() {
        onViewWithText("Are you parking?").isDisplayed();
    }

    @Then("^I can accept, and I get the requested spot\\.$")
    public void i_can_accept_and_I_get_the_requested_spot() {
        waitFor(1000);
        onViewWithText("Yes").isDisplayed();
        onViewWithText("Yes").click();
    }

}
