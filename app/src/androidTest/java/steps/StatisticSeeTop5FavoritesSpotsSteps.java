package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;

public class StatisticSeeTop5FavoritesSpotsSteps extends GreenCoffeeSteps {
    @When("^I login the app$")
    public void i_login_the_app() {
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        waitFor(1000);
        onViewWithId(R.id.loginEmail).type("ruben@hotmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456789");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
    }

    @When("^I see the dashboard activity$")
    public void i_see_the_dashboard_activity() {
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
    }

    @When("^I open the menu$")
    public void i_open_the_menu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        waitFor(1000);
    }

    @When("^I click in Statisctis option$")
    public void i_click_in_Statisctis_option() {
        onViewWithText("Statistics").click();
    }

    @When("^I see statistic activity$")
    public void i_see_statistic_activity() {
        onViewWithText("Statistics").isDisplayed();
    }

    @Then("^I see \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"  \"([^\"]*)\"  \"([^\"]*)\" text$")
    public void i_see_text(String arg1, String arg2, String arg3, String arg4, String arg5) {
        onViewWithId(R.id.textViewFavSpot1).contains(arg1);
        onViewWithId(R.id.textViewFavSpot2).contains(arg2);
        onViewWithId(R.id.textViewFavSpot3).contains(arg3);
        onViewWithId(R.id.textViewFavSpot4).contains(arg4);
        onViewWithId(R.id.textViewFavSpot5).contains(arg5);
    }

}
