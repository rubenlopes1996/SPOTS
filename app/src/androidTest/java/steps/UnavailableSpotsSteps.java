package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class UnavailableSpotsSteps extends GreenCoffeeSteps {
    @When("^i login the app$")
    public void i_login_the_app() {
/*
        FirebaseManager.INSTANCE.logout();
        // Write code here that turns the phrase above into concrete actions
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        onViewWithId(R.id.loginEmail).type("sergiotrindade100@gmail.com");
        onViewWithId(R.id.loginPassword).type("123456");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();*/
    }

    @When("^I click the spinner$")
    public void i_click_the_spinner() {
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).click();
    }

    @When("^I select the park \"([^\"]*)\" map$")
    public void i_select_the_park_map(String arg1) {
        waitFor(1000);
        onViewWithText(arg1).isDisplayed();
        waitFor(1000);
        onViewWithText(arg1).click();
    }

    @When("^i see the park \"([^\"]*)\" map$")
    public void i_see_the_park_map(String arg1) {
        waitFor(1000);
        onViewWithText("Park selected "+arg1).isDisplayed();
    }

    @Then("^I see the (\\d+) spots unavailable$")
    public void i_see_the_spots_unavailable(int arg1) {
        waitFor(1000);
        onViewWithText("Busy Spots: "+arg1).isDisplayed();
    }
}
