package features;

import android.support.test.rule.ActivityTestRule;

import com.example.rubenfilipe.spots.DashboardActivity;
import com.example.rubenfilipe.spots.GuestDashboardActivity;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;
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

import steps.LogoffSteps;

@RunWith(Parameterized.class)
public class LogoffFeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule<GuestDashboardActivity> activity = new ActivityTestRule<>(GuestDashboardActivity.class);

    public LogoffFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name="{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().
                withFeatureFromAssets("assets/features/logoff.feature").
                scenarios();
    }

    @Test
    public void test(){ start(
            new LogoffSteps());
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        FirebaseManager.INSTANCE.logout();
    }
}
