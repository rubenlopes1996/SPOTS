package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static org.junit.Assert.assertFalse;

public class UserSeeTheListOfFavoritesSteps extends GreenCoffeeSteps {
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

    @Given("^I see the dashboard$")
    public void i_see_the_dashboard() {
        waitFor(2000);
        onViewWithId(R.id.findMeASpot).isDisplayed();
    }

    @Given("^I see the dashboard activity$")
    public void i_see_the_dashboard_activity() {
        waitFor(2000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
        FirebaseManager.INSTANCE.clearMyFavorites("ruben@hotmail.com");
    }

    @Given("^I click in option menu Favorites$")
    public void i_click_in_option_menu_Favorites() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        waitFor(1000);
        onViewWithText("Favorites").click();
    }

    @Then("^I see the My favorites activity$")
    public void i_see_the_My_favorites_activity() {
        waitFor(1000);
        onViewWithText("My Favorites Spots").isDisplayed();
        onViewWithId(R.id.myFavorites).isNotEmpty();
    }

    @Then("^I see a msg saying \"([^\"]*)\"$")
    public void i_see_a_msg_saying(String arg1) {
        waitFor(1000);
        onViewWithText("My Favorites Spots").isDisplayed();
        onViewWithText(R.string.noFavorites).isDisplayed();
    }
}
