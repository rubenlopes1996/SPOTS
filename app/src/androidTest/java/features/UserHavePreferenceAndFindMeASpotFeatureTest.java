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

import steps.UserHavePreferenceAndFindMeASpotSteps;

@RunWith(Parameterized.class)
public class UserHavePreferenceAndFindMeASpotFeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule<GuestDashboardActivity> activity=new ActivityTestRule<>(GuestDashboardActivity.class);

    public UserHavePreferenceAndFindMeASpotFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name="{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().
                withFeatureFromAssets("assets/features/userHavePreferenceAndFindMeASpot.feature").
                scenarios();
    }

    @Test
    public void test(){
        start(new UserHavePreferenceAndFindMeASpotSteps());
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);
        FirebaseManager.INSTANCE.logout();

        FirebaseManager.INSTANCE.setSpotLocation("Spot1",39.73484,-8.820749);
        FirebaseManager.INSTANCE.setSpotLocation("Spot2",39.735276, -8.820217);
        FirebaseManager.INSTANCE.setSpotLocation("Spot3",39.733575,-8.821218);
        FirebaseManager.INSTANCE.setSpotLocation("Spot4",39.73551666666667, -8.82146666666666);
        FirebaseManager.INSTANCE.changeSpotAvailable("Spot1",true);
        FirebaseManager.INSTANCE.changeSpotAvailable("Spot2",true);
        FirebaseManager.INSTANCE.changeSpotAvailable("Spot3",true);
        FirebaseManager.INSTANCE.changeSpotAvailable("Spot4",true);
    }
}
