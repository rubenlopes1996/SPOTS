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

import steps.CheckWhereUserIsParkedSteps;

@RunWith(Parameterized.class)
public class CheckWhereUserIsParkedTest extends GreenCoffeeTest {
    public CheckWhereUserIsParkedTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Rule
    public ActivityTestRule<GuestDashboardActivity> activity= new ActivityTestRule<>(GuestDashboardActivity.class);


    @Parameterized.Parameters(name="{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/checkWhereUserIsParked.feature").scenarios();
    }

    @Test
    public void test(){ start(
            new CheckWhereUserIsParkedSteps());
    }
    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        FirebaseManager.INSTANCE.logout();

    }
}
