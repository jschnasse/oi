
/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.schnasse.oi.ExceptionTests;
import org.schnasse.oi.Issue2;
import org.schnasse.oi.Issue4;
import org.schnasse.oi.writer.ContextWriterUnitTest;
import org.schnasse.oi.writer.JsonWriterUnitTest;
import org.schnasse.oi.writer.XmlWriterUnitTest;
import org.schnasse.oi.writer.YamlWriterUnitTest;

@RunWith(Suite.class)
@SuiteClasses({ XmlWriterUnitTest.class, ContextWriterUnitTest.class, JsonWriterUnitTest.class,
		YamlWriterUnitTest.class, Issue2.class, Issue4.class, ExceptionTests.class })
public class UnitTests {

}
