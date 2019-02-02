package features;

import android.support.test.rule.ActivityTestRule;

import com.example.rubenfilipe.spots.RegistrationActivity;
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

import steps.RegistrationSteps;

@RunWith(Parameterized.class)
public class RegistrationFeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule<RegistrationActivity> activity = new ActivityTestRule<>(RegistrationActivity.class);

    public RegistrationFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Test
    public void test() {
        start(new RegistrationSteps());
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/registration.feature").scenarios();
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
       //Should remove user that i will register
    }
}
