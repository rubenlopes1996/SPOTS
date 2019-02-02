package steps;

import com.example.rubenfilipe.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.When;

public class GuestCheckParkInformationSteps extends GreenCoffeeSteps {
    @When("^I open the dashboard$")
    public void i_open_the_dashboard() {
        waitFor(1000);
        onViewWithId(R.id.guestDashboardMap).isDisplayed();
    }

    @When("^i see a map$")
    public void i_see_a_map() {
        onViewWithId(R.id.guestDashboardMap).isDisplayed();
    }

    @When("^see the free spost$")
    public void see_the_free_spost() {
        onViewWithId(R.id.textViewGuestFreeSpots).isDisplayed();

    }

    @When("^the busy spots$")
    public void the_busy_spots() {
        onViewWithId(R.id.textViewGuestBusySpots).isDisplayed();

    }

    @When("^the update date$")
    public void the_update_date() {
        onViewWithId(R.id.textViewGuestLastUpdatedSpots).isDisplayed();

    }

}
