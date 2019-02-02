package features;

import android.support.test.rule.ActivityTestRule;

import com.example.rubenfilipe.spots.GuestDashboardActivity;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import steps.GuestCheckParkInformationSteps;

@RunWith(Parameterized.class)
public class GuestCheckParkInformation extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule<GuestDashboardActivity> activity= new ActivityTestRule<>(GuestDashboardActivity.class);

    public GuestCheckParkInformation(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name="{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/guestCheckParkInformation.feature").scenarios();
    }

    @Test
    public void test(){ start( new GuestCheckParkInformationSteps());
    }

}
