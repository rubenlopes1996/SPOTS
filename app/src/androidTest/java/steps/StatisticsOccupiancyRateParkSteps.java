package steps;

import com.example.rubenfilipe.spots.R;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;

public class StatisticsOccupiancyRateParkSteps extends GuestDashboardSteps {
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
        waitFor(3000);
    }

    @When("^I see the dashboard activity$")
    public void i_see_the_dashboard_activity() {
        waitFor(1000);
        onViewWithId(R.id.parkSpinner).isDisplayed();
    }

    @When("^I open the menu$")
    public void i_open_the_menu() {
        waitFor(1000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        waitFor(1000);
    }

    @When("^I click in Statisctis option$")
    public void i_click_in_Statisctis_option() {
        onViewWithText("Statistics").click();
        waitFor(1000);
    }

    @When("^I see statistic activity$")
    public void i_see_statistic_activity() {
        closeKeyboard();
        onViewWithId(R.id.graphDate);
        waitFor(1000);
    }

    @Then("^I see percentage of occupiancy by park$")
    public void i_see_percentage_of_occupiancy_by_park() {
        waitFor(1000);
        closeKeyboard();
        onViewWithId(R.id.barChart).isDisplayed();
    }

}
