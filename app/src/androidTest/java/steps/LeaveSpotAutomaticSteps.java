package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class LeaveSpotAutomaticSteps extends GreenCoffeeSteps {
    @When("^I see the Dashboard activity$")
    public void i_see_the_Dashboard_activity() {
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        onViewWithId(R.id.loginEmail).type("sergiotrindade100@gmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
        waitFor(1000);
        FirebaseManager.INSTANCE.changeUserCurrentSpotId(4);
        waitFor(1000);
    }

    @When("^I receive a notification to leave the spot$")
    public void i_receive_a_notification_to_leave_the_spot() {
        FirebaseManager.INSTANCE.changeSpotAvailable("Spot4",true);
        waitFor(1000);
        onViewWithText("Are you leaving the spot?").isDisplayed();
    }

    @Then("^I can accept, and I get the requested spot\\.$")
    public void i_can_accept_and_I_get_the_requested_spot() {
        waitFor(1000);
        onViewWithText("Yes").isDisplayed();
        onViewWithText("Yes").click();
    }

    @Then("^I can cancel, and I stay in the same spott$")
    public void i_can_cancel_and_I_stay_in_the_same_spott() {
        waitFor(1000);
        onViewWithText("No").isDisplayed();
        onViewWithText("No").click();
    }

}
