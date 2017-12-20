/**
 * 
 */
package eraser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author
 *
 */
public class AppTest {

	App appo = new App();
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String s = appo.Sample();
		
		assertEquals("Sample", s);
	}

}
