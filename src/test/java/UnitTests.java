
/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.schnasse.oi.writer.ContextWriterUnitTest;
import org.schnasse.oi.writer.JsonWriterUnitTest;
import org.schnasse.oi.writer.XmlWriterUnitTest;
import org.schnasse.oi.writer.YamlWriterUnitTest;

@RunWith(Suite.class)
@SuiteClasses({ XmlWriterUnitTest.class, ContextWriterUnitTest.class, JsonWriterUnitTest.class,
		YamlWriterUnitTest.class })
public class UnitTests {

}
