package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;

public class UserDontHavePreferenceAndFindMeASpot extends GreenCoffeeSteps {
    @Given("^I open the dashboard$")
    public void i_open_the_dashboard() {
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        waitFor(1000);
        onViewWithId(R.id.loginEmail).type("ruben@hotmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456789");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();

    }

    @Given("^I click on the button find me a spot without preference$")
    public void i_click_on_the_button_find_me_a_spot_without_preference() {
        waitFor(2000);
        onViewWithId(R.id.findMeASpot).isDisplayed();
        FirebaseManager.INSTANCE.setUserCurrentSpot(-1);
        FirebaseManager.INSTANCE.setUserPreference("");
        onViewWithId(R.id.findMeASpot).click();

    }

    @Given("^I receive a notification to change the preferences$")
    public void i_receive_a_notification_to_change_the_preferences() {
        onViewWithText(R.string.preferenceChangeQuestion).isDisplayed();
    }

    @Given("^I click yes to accept$")
    public void i_click_yes_to_accept() {
        onViewWithText("Yes").isDisplayed();
        onViewWithText("Yes").click();
    }

    @Given("^I see profile activity$")
    public void i_see_profile_activity() {
       onViewWithId(R.id.editProfilePassword).isDisplayed();
       onViewWithId(R.id.editProfilePassword).type("123456789");
       closeKeyboard();
    }

    @Given("^I change the preference to \"([^\"]*)\"$")
    public void i_change_the_preference_to(String arg1) {
       onViewWithId(R.id.editProfilePreference).isDisplayed();
       onViewWithId(R.id.editProfilePreference).click();
       onViewWithText(arg1).click();
       onViewWithId(R.id.editProfileSave).click();
       waitFor(2000);

    }

    @Given("^I go to dashboard again$")
    public void i_go_to_dashboard_again() {

    }

    @Given("^I click on find me a spot button$")
    public void i_click_on_find_me_a_spot_button() {
        waitFor(1000);
        onViewWithId(R.id.findMeASpot).click();
    }

    @Given("^I receive a notification to the \"([^\"]*)\" to the \"([^\"]*)\"$")
    public void i_receive_a_notification_to_the_to_the(String arg1, String arg2) {
        waitFor(1000);
        onViewWithText("Preference: "+arg1+" Spot: "+arg2+", accept the spot?").isDisplayed();
    }

    @Given("^I click yes to accept the spot$")
    public void i_click_yes_to_accept_the_spot() {
        onViewWithText("Yes").isDisplayed();
        onViewWithText("Yes").click();

        waitFor(1000);
    }

    @Given("^I receive a notification that i have parked in the \"([^\"]*)\"$")
    public void i_receive_a_notification_that_i_have_parked_in_the(String arg1) {
        FirebaseManager.INSTANCE.setSpotLocation(arg1, 39.7348,-8.8209);
        FirebaseManager.INSTANCE.takeSpot(arg1);

        waitFor(1000);

        onViewWithText(R.string.takeSpotQuestion).isDisplayed();
    }

    @Given("^I Click yes to accept the automatic notification to park$")
    public void i_Click_yes_to_accept_the_automatic_notification_to_park() {
        onViewWithText("Yes").isDisplayed();
        onViewWithText("Yes").click();
    }

    @Then("^the number of free spots change$")
    public void the_number_of_free_spots_change() {
       onViewWithText("Free Spots: 2").isDisplayed();
    }
}
