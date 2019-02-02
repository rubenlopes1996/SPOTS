package steps;

import com.example.rubenfilipe.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;

public class PassSteps extends GreenCoffeeSteps {
    @When("^I login the app and see password activity$")
    public void i_login_the_app_and_see_password_activity() {
        waitFor(2000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        waitFor(2000);
        onViewWithId(R.id.loginEmail).type("sergiotrindade100@gmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("12345678");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
        waitFor(2000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        waitFor(2000);
        onViewWithText("Profile").click();
        waitFor(2000);
        onViewWithText("CHANGE PASSWORD").click();
    }

    @When("^I introduced old password as (\\d+)$")
    public void i_introduced_old_password_as(int arg1) {
        onViewWithId(R.id.editOldPass).type(Integer.toString(arg1));
        closeKeyboard();
    }

    @When("^I introduced new password as (\\d+)$")
    public void i_introduced_new_password_as(int arg1) {
        onViewWithId(R.id.editNewPass).type(Integer.toString(arg1));
        closeKeyboard();
    }

    @When("^I click the save button $")
    public void i_click_the_save_button() {
        onViewWithId(R.id.passBtnSave).click();
    }

    @Then("^I see an error message saying \"([^\"]*)\"$")
    public void i_see_an_error_message_saying(String arg1) {
        waitFor(2000);
        onViewWithText(arg1).isDisplayed();
    }

    @Then("^I see the DashboardActivity$")
    public void i_see_the_DashboardActivity() {
        waitFor(2000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
    }

    @When("^I click the cancel button $")
    public void i_click_the_cancel_button() {
        onViewWithId(R.id.passBtnCancel).isDisplayed();
        onViewWithId(R.id.passBtnCancel).click();
    }
}
