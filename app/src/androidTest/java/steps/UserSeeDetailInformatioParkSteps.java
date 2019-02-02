package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class UserSeeDetailInformatioParkSteps extends GreenCoffeeSteps {
    @When("^i login the app$")
    public void i_login_the_app() {

        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        onViewWithId(R.id.loginEmail).type("ruben@hotmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456789");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
        waitFor(2000);
    }

    @When("^i see the park map$")
    public void i_see_the_park_map() {
        onViewWithId(R.id.userDashboardMap).isDisplayed();
    }

    @Then("^I see the number of spots available$")
    public void i_see_the_number_of_spots_available() {
        onViewWithId(R.id.textViewFreeSpots).isDisplayed();
    }

    @Then("^I see the last update of the spots map$")
    public void i_see_the_last_update_of_the_spots_map() {
        onViewWithId(R.id.textViewLastUpdatedSpots).isDisplayed();

    }

}
