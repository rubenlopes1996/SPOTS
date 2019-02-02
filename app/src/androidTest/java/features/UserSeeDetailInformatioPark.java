package features;

import android.support.test.rule.ActivityTestRule;
import com.example.rubenfilipe.spots.DashboardActivity;
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

import steps.UserSeeDetailInformatioParkSteps;


@RunWith(Parameterized.class)
public class UserSeeDetailInformatioPark extends GreenCoffeeTest {
    public UserSeeDetailInformatioPark(ScenarioConfig scenario) {
        super(scenario);
    }

    @Rule
    public ActivityTestRule<GuestDashboardActivity> activity = new ActivityTestRule<>(GuestDashboardActivity.class);


    @Parameterized.Parameters(name="{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/userSeeDetailInformationPark.feature").scenarios();
    }

    @Test
    public void test(){
        start(new UserSeeDetailInformatioParkSteps());
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);
        FirebaseManager.INSTANCE.logout();
    }
}
