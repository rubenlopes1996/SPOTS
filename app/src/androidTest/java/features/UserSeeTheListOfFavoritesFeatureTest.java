package features;

import android.support.test.rule.ActivityTestRule;

import com.example.rubenfilipe.spots.GuestDashboardActivity;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Locale;

import steps.UserSeeTheListOfFavoritesSteps;

@RunWith(Parameterized.class)
public class UserSeeTheListOfFavoritesFeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule<GuestDashboardActivity> activity=new ActivityTestRule<>(GuestDashboardActivity.class);

    public UserSeeTheListOfFavoritesFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name="{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig()
                .withFeatureFromAssets("assets/features/userSeeTheListOfFavorites.feature")
                .scenarios();
    }

    @Test
    public void test(){
        start(new UserSeeTheListOfFavoritesSteps());
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);

        FirebaseManager.INSTANCE.logout();


        FirebaseManager.INSTANCE.addToFavorites("Spot1","ruben@hotmail.com","ICcdNObKXUd6PXl9cjZqYJlMTkN2");
    }
}
