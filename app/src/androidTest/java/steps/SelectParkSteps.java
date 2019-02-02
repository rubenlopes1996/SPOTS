package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.And;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.espresso.action.ViewActions.click;

public class SelectParkSteps extends GreenCoffeeSteps {
    @Given("^I login$")
    public void i_login() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        waitFor(1000);
        onViewWithId(R.id.loginEmail).type("ruben@hotmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456789");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
    }

    @Given("^I open the dashboard$")
    public void i_open_the_dashboard() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(2000);
        onViewWithId(R.id.findMeASpot).isDisplayed();
        FirebaseManager.INSTANCE.setUserCurrentSpot(-1);
    }

    @Given("^I click the spinner$")
    public void i_click_the_spinner() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).click();
    }


    @Given("^i select the park  \"([^\"]*)\"$")
    public void i_select_the_park(String arg1) {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(arg1).isDisplayed();
        onViewWithText(arg1).click();
    }

    @Then("^I see the \"([^\"]*)\" map$")
    public void i_see_the_map(String arg1) {
        // Write code here that turns the phrase above into concrete actions
        waitFor(1000);
        onViewWithText("Park selected "+arg1).isDisplayed();
    }

}