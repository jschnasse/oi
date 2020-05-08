import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.schnasse.oi.writer.ContextWriterUnitTest;
import org.schnasse.oi.writer.XmlWriterUnitTest;

@RunWith(Suite.class)
@SuiteClasses({ XmlWriterUnitTest.class, ContextWriterUnitTest.class })
public class UnitTests {

}
