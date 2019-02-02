package steps;

import com.example.rubenfilipe.spots.R;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;

public class UserRemoveFavoritesSteps extends GreenCoffeeSteps {
    @When("^I login the app$")
    public void i_login_the_app() {
        waitFor(1000);
        onViewWithId(R.id.buttonGuestDashboardLogin).click();
        waitFor(1000);
        onViewWithId(R.id.loginEmail).type("sergiotrindade100@gmail.com");
        closeKeyboard();
        onViewWithId(R.id.loginPassword).type("123456");
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
    }

    @When("^I see the dashboard activity$")
    public void i_see_the_dashboard_activity() {
        waitFor(2000);
        onViewWithId(R.id.findMeASpot).isDisplayed();
        waitFor(2000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
        //FirebaseManager.INSTANCE.clearMyFavorites("ruben@hotmail.com");

    }

    @When("^I open the menu$")
    public void i_open_the_menu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        waitFor(1000);
        onViewWithText("Favorites").isDisplayed();
    }

    @When("^I click in Favorites option$")
    public void i_click_in_Favorites_option() {
        waitFor(1000);
        onViewWithText("Favorites").click();
    }

    @When("^I see Favorites activity$")
    public void i_see_Favorites_activity() {
        waitFor(1000);
        onViewWithText("My Favorites Spots").isDisplayed();
        onViewWithId(R.id.myFavorites).isNotEmpty();
    }

    @Then("^I choose a favorite and I remove it\\.$")
    public void i_choose_a_favorite_and_I_remove_it() {
        FirebaseManager.INSTANCE.addToFavorites("Spot6","sergiotrindade100@gmail.com","IkiqNqVMsHORUYVHD5DRa67ftf72");
        waitFor(2000);
        onViewWithText("Spot6").click();
        waitFor(1000);
        onViewWithText(R.string.removeFavoritesQuestion).isDisplayed();
        onViewWithText("Yes").isDisplayed();
        waitFor(1000);
        onViewWithText("Yes").click();
        waitFor(1000);
        onViewWithText("Favorite removed succesfully.").isDisplayed();

    }

    @Then("^I can't remove favorites because there isn't any$")
    public void i_can_t_remove_favorites_because_there_isn_t_any() {
        waitFor(1000);
        onViewWithText("My Favorites Spots").isDisplayed();
        onViewWithText(R.string.noFavorites).isDisplayed();
    }
}
