package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;

public class UserEditProfileSteps extends GreenCoffeeSteps {
    @When("^I login the app and see profile activity$")
    public void i_login_the_app_and_see_profile_activity() {
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        waitFor(1000);
        onViewWithId(R.id.loginEmail).type("ruben@hotmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456789");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        waitFor(1000);
        onViewWithText("Profile").click();
        waitFor(1000);
        onViewWithText("My Profile").isDisplayed();
    }

    @When("^I introduced password as $")
    public void i_introduced_password_as() {
        onViewWithId(R.id.editProfilePassword).clearText();
        closeKeyboard();
    }

    @When("^I introduce name as \"([^\"]*)\"$")
    public void i_introduce_name_as(String arg1) {
        onViewWithId(R.id.editProfileName).clearText();
        onViewWithId(R.id.editProfileName).type(arg1);
        closeKeyboard();
    }

    @When("^I introduce age as (\\d+)$")
    public void i_introduce_age_as(int arg1) {
        onViewWithId(R.id.editProfileAge).clearText();
        onViewWithId(R.id.editProfileAge).type(Integer.toString(arg1));
        closeKeyboard();
    }

    @When("^I see emails as \"([^\"]*)\"$")
    public void i_see_emails_as(String arg1) {
        onViewWithText(arg1).isDisplayed();
    }

    @When("^I select preference as \"([^\"]*)\"$")
    public void i_select_preference_as(String arg1) {
        onViewWithId(R.id.editProfilePreference).isDisplayed();
        onViewWithId(R.id.editProfilePreference).click();
        onViewWithText(arg1).isDisplayed();
        onViewWithText(arg1).click();
    }

    @When("^I click the save button $")
    public void i_click_the_save_button() {
        onViewWithId(R.id.editProfileSave).click();
    }

    @Then("^I see an error message saying \"([^\"]*)\"$")
    public void i_see_an_error_message_saying(String arg1) {
        waitFor(1000);
        onViewWithText(arg1).isDisplayed();
    }

    @When("^I introduced password as (\\d+)$")
    public void i_introduced_password_as(int arg1) {
        onViewWithId(R.id.editProfilePassword).clearText();
        onViewWithId(R.id.editProfilePassword).type(Integer.toString(arg1));
        closeKeyboard();
    }

    @When("^I introduce name as $")
    public void i_introduce_name_as() {
        onViewWithId(R.id.editTextName).clearText();
        closeKeyboard();
    }

    @When("^I introduce age as $")
    public void i_introduce_age_as() {
        onViewWithId(R.id.editTextAge).clearText();
        closeKeyboard();
    }

    @Then("^I see the DashboardActivity$")
    public void i_see_the_DashboardActivity() {
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
    }

    @When("^I click the cancel button $")
    public void i_click_the_cancel_button() {
        onViewWithId(R.id.editProfileCancel).isDisplayed();
        onViewWithId(R.id.editProfileCancel).click();
    }


}



