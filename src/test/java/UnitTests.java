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
