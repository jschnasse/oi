import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.schnasse.oi.Issue2;

@RunWith(Suite.class)
@SuiteClasses({ IntegrationTests.class, UnitTests.class, Issue2.class })
public class AllTests {

}
