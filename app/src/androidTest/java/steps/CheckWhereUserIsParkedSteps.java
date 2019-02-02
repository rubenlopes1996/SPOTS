package steps;

import com.example.rubenfilipe.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.ScenarioConfig;
import com.mauriciotogneri.greencoffee.annotations.Given;

public class CheckWhereUserIsParkedSteps extends GreenCoffeeSteps {
    @Given("^I login$")
    public void i_login() {
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        waitFor(1000);
        onViewWithId(R.id.loginEmail).type("ruben@hotmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456789");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
    }

    @Given("^see where i am parked$")
    public void see_where_i_am_parked() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(1000);
        onViewWithText("Parked at: 3").isDisplayed();
    }
}
