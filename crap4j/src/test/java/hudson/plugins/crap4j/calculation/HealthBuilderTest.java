package hudson.plugins.crap4j.calculation;

import junit.framework.TestCase;

public class HealthBuilderTest extends TestCase {

	public void testHealthCalculation() throws Exception {
		HealthBuilder builder = new HealthBuilder();
		assertEquals(0.0d, builder.calculateHealthOf(15.0d), 1E-6);
		assertEquals(100.0d, builder.calculateHealthOf(0.0d), 1E-6);
		assertEquals(50.0d, builder.calculateHealthOf(7.5d), 1E-6);
		assertEquals(10.0d, builder.calculateHealthOf(13.5d), 1E-6);
		assertEquals(90.0d, builder.calculateHealthOf(1.5d), 1E-6);
	}
	
	public void testHealthSummary() throws Exception {
		HealthBuilder builder = new HealthBuilder();
		assertEquals("132 crappy methods (7.96%)", builder.getHealthSummary(132, 7.96d));
		assertEquals("0 crappy methods (0.0%)", builder.getHealthSummary(0, 0.0d));
		assertEquals("1 crappy methods (0.02%)", builder.getHealthSummary(1, 0.02d));
	}
}
