
/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.schnasse.oi.Issue2;
import org.schnasse.oi.Issue4;

@RunWith(Suite.class)
@SuiteClasses({ IntegrationTests.class, UnitTests.class, Issue2.class, Issue4.class })
public class AllTests {

}
