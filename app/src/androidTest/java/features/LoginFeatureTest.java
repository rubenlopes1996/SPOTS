package features;

import android.support.test.rule.ActivityTestRule;

import com.example.rubenfilipe.spots.LoginActivity;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import steps.LoginSteps;

@RunWith(Parameterized.class)
public class LoginFeatureTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule<LoginActivity> ativity=new ActivityTestRule<>(LoginActivity.class);

    public LoginFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name="{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/login.feature").scenarios();
    }

    @Test
    public void test(){
        start(new LoginSteps());
    }
}
