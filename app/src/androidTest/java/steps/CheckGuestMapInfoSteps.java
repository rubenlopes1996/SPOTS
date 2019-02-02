package steps;

import com.example.rubenfilipe.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class CheckGuestMapInfoSteps extends GreenCoffeeSteps {
    @When("^i open the dashboard$")
    public void i_open_the_dashboard() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.guestDashboardTitle).isDisplayed();
    }

    @When("^i see the Parque A map$")
    public void i_see_the_Parque_A_map() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.guestDashboardMap).isDisplayed();
        onViewWithText("Park selected Parque A").isDisplayed();
    }

    @When("^I see the (\\d+) spots available$")
    public void i_see_the_spots_available(int arg1) {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.guestDashboardMap).isDisplayed();
        waitFor(1000);
        onViewWithId(R.id.textViewGuestFreeSpots).contains("Free Spots: "+arg1);
    }

    @When("^I see the (\\d+) spots busy$")
    public void i_see_the_spots_busy(int arg1) {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText("Busy Spots: "+arg1).isDisplayed();
    }

    @Then("^I see the last updated availability$")
    public void i_see_the_last_updated_availability() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.textViewGuestLastUpdatedSpots).isDisplayed();
    }
}