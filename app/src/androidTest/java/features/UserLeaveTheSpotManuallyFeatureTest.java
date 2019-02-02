package features;

import android.support.test.rule.ActivityTestRule;

import com.example.rubenfilipe.spots.GuestDashboardActivity;
import com.example.rubenfilipe.spots.LoginActivity;
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

import steps.UserLeaveTheSpotManuallySteps;

@RunWith(Parameterized.class)
public class UserLeaveTheSpotManuallyFeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule<GuestDashboardActivity> activity=new ActivityTestRule<>(GuestDashboardActivity.class);

    public UserLeaveTheSpotManuallyFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name="{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().
                withFeatureFromAssets("assets/features/userLeaveTheSpotManually.feature").
                scenarios();
    }

    @Test
    public void test(){
        start(new UserLeaveTheSpotManuallySteps());
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);
        FirebaseManager.INSTANCE.logout();
    }
}
