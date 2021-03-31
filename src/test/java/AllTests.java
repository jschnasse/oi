/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ IntegrationTests.class, UnitTests.class })
public class AllTests {

}
