
/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.schnasse.oi.CsvTest;
import org.schnasse.oi.JsonTest;
import org.schnasse.oi.XmlTest;
import org.schnasse.oi.YmlTest;
import org.schnasse.oi.helper.UrlUtilTest;
import org.schnasse.oi.main.MainTest;

@RunWith(Suite.class)
@SuiteClasses({ CsvTest.class, JsonTest.class, XmlTest.class, YmlTest.class, UrlUtilTest.class, MainTest.class })
public class IntegrationTests {

}
