package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class CheckFreeSpotsText extends GreenCoffeeSteps {

    @When("^i login the app$")
    public void i_login_the_app() {
        // Write code here that turns the phrase above into concrete actions
        // Write code here that turns the phrase above into concrete actions
        FirebaseManager.INSTANCE.logout();
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        onViewWithId(R.id.loginEmail).type("qwe@qwe.qwe");
        onViewWithId(R.id.loginPassword).type("123456");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
    }

    @When("^I click the spinner$")
    public void i_click_the_spinner() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).click();
    }

    @When("^I select the park \"([^\"]*)\" map$")
    public void i_select_the_park_map(String arg1) {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(arg1).isDisplayed();
        onViewWithText(arg1).click();
    }

    @When("^i see the park \"([^\"]*)\" map$")
    public void i_see_the_park_map(String arg1) {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText("Park selected "+arg1).isDisplayed();
    }

    @Then("^I see the (\\d+) spots available$")
    public void i_see_the_spots_available(int arg1) {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText("Free Spots: "+arg1).isDisplayed();
    }
}