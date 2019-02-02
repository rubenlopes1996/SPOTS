package steps;

import com.example.rubenfilipe.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.And;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;

public class LogoffSteps extends GreenCoffeeSteps {
    @When("^i login to the app$")
    public void i_login_to_the_app() {
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        waitFor(1000);
        onViewWithId(R.id.loginEmail).type("ruben@hotmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456789");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
    }

    @When("^I click on the menu dropdown$")
    public void i_click_on_the_menu_dropdown() {
        waitFor(3000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        waitFor(1000);
        onViewWithText("Logout").isDisplayed();
    }

    @When("^I click on the Logoff button$")
    public void i_click_on_the_logoff_button() {
        //onViewWithId(R.id.menuLogout).perform(click());
        onViewWithText("Logout").click();
    }

    @Then("^I see the Login Activity$")
    public void i_see_the_LoginActivity$() {
        waitFor(1000);
        onViewWithId(R.id.guestDashboardMap).isDisplayed();
    }

}
