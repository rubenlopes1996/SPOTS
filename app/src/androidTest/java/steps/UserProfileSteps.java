package steps;

import android.support.test.InstrumentationRegistry;

import com.example.rubenfilipe.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;

public class UserProfileSteps extends GreenCoffeeSteps {
    @When("^I open profile$")
    public void i_open_profile() {
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        onViewWithId(R.id.loginEmail).type("sergiotrindade100@gmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456");
        waitFor(2000);
        closeKeyboard();
        waitFor(2000);
        onViewWithId(R.id.buttonLogin).click();
        waitFor(2000);

    }

    @When("^I click on the Profile button$")
    public void i_click_on_the_profile_button() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        waitFor(2000);
        onViewWithText("Profile").isDisplayed();
        waitFor(2000);
        onViewWithText("Profile").click();
    }

    @When("^I see my name$")
    public void i_see_my_name() {
        waitFor(2000);
        onViewWithId(R.id.editProfileName).isDisplayed();
    }

    @When("^I see my age$")
    public void i_see_my_age() {
        waitFor(2000);
        onViewWithId(R.id.editProfileAge).isDisplayed();
    }

    @When("^I see my email$")
    public void i_see_my_email() {
        waitFor(2000);
        onViewWithId(R.id.editMail).isDisplayed();
    }

    @Then("^I see the Save button$")
    public void i_see_the_save_button() {
        waitFor(2000);
        onViewWithId(R.id.editProfileSave).click();
    }

}

