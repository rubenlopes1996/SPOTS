package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class UserLeaveTheSpotManuallySteps extends GreenCoffeeSteps {
    @When("^I see the login form and logged in$")
    public void i_see_the_login_form_and_logged_in() {
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        waitFor(1000);
        onViewWithId(R.id.loginEmail).type("ruben@hotmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456789");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
    }

    @When("^I open the dashboard$")
    public void i_open_the_dashboard() {
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
        FirebaseManager.INSTANCE.setUserCurrentSpot(1);
        waitFor(2000);
    }

    @When("^I click in the button my spot$")
    public void i_click_in_the_button_my_spot() {
        onViewWithId(R.id.dashboardMySpot).isDisplayed();
        onViewWithId(R.id.dashboardMySpot).click();
    }

    @When("^I click yes in the confirmation dialog that I leave the spot$")
    public void i_click_yes_in_the_confirmation_dialog_that_I_leave_the_spot() {
        onViewWithText(R.string.leaveSpotQuestion).isDisplayed();
        onViewWithText("Yes").isDisplayed();
        onViewWithText("Yes").click();
    }

    @Then("^The number of free spots change$")
    public void the_number_of_free_spots_change() {
        onViewWithId(R.id.textViewFreeSpots).contains("Free Spots: 3");
    }

    @When("^I click in the button my spot to leave the spot$")
    public void i_click_in_the_button_my_spot_to_leave_the_spot() {
        FirebaseManager.INSTANCE.leaveSpot();
        waitFor(1000);
        onViewWithId(R.id.dashboardMySpot).isDisplayed();
        onViewWithId(R.id.dashboardMySpot).click();
    }

    @Then("^I see a message to take the spot$")
    public void i_see_a_message_to_take_the_spot() {
        onViewWithText(R.string.takeSpotQuestion).isDisplayed();
    }
}
