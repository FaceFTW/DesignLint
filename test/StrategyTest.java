import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import datasource.ASMParser;
import domain.analyzer.StrategyAnalyzer;

public class StrategyTest {

	private ASMParser parser;
	private final String[] exampleClasses = {
			"example/strategy/strategytype/typea/CompliantStrategyA1",
			"example/strategy/strategytype/typea/CompliantStrategyA2",
			"example/strategy/strategytype/typea/CompliantStrategyTypeA",
			"example/strategy/strategytype/typea/NonCompliantStrategyA3ImplementsOtherInterfaces",
			"example/strategy/strategytype/typeb/CompliantStrategyB1",
			"example/strategy/strategytype/typeb/CompliantStrategyB2",
			"example/strategy/strategytype/typeb/CompliantStrategyB3",
			"example/strategy/strategytype/typeb/CompliantStrategyTypeB",
			"example/strategy/strategytype/typeb/NonCompliantStrategyB3ImplementsOtherStrategyInterface",
			"example/strategy/strategytype/typec/WarningStrategyTypeCNoStrategies",
			"example/strategy/CompliantClassUsingStrategyA",
			"example/strategy/CompliantClassUsingStrategyB",
			"example/strategy/CompliantClassUsingMultipleSingleStrategy",
			"example/strategy/CompliantClassUsingDifferentSingleStrategy",
			"example/strategy/NonCompliantClassUsingConcreteStrategy",
			"example/strategy/CompliantClassUsingStrategyWithMethods",
			"example/strategy/CompliantFinalClassUsingStrategyWithMethods",
	};

	// We use an explicit instance to test the protected method checkViolation()
	private StrategyAnalyzer analyzer;

	// Common Testing Setup
	public void setupAnalyzer() {
		try {
			this.parser = new ASMParser(exampleClasses);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.analyzer = new StrategyAnalyzer(parser);
		analyzer.getRelevantData(exampleClasses);
	}

	@Test
	public void testStrategyTypeDetection() {
		setupAnalyzer();

		List<String> expectedStrategyTypes = new ArrayList<>();
		expectedStrategyTypes.add("example/strategy/strategytype/typea/CompliantStrategyTypeA");
		expectedStrategyTypes.add("example/strategy/strategytype/typeb/CompliantStrategyTypeB");

		List<String> expectedStrategyA = new ArrayList<>();
		expectedStrategyA.add("example/strategy/strategytype/typea/CompliantStrategyA1");
		expectedStrategyA.add("example/strategy/strategytype/typea/CompliantStrategyA2");

		List<String> expectedStrategyB = new ArrayList<>();
		expectedStrategyB.add("example/strategy/strategytype/typeb/CompliantStrategyB1");
		expectedStrategyB.add("example/strategy/strategytype/typeb/CompliantStrategyB2");
		expectedStrategyB.add("example/strategy/strategytype/typeb/CompliantStrategyB3");

		Map<String, String[]> returnedMap = this.analyzer.returnStrategyMap();

		assertEquals(returnedMap.keySet().size(), 2);

		List<String> returnedStrategies = new ArrayList<>(returnedMap.keySet());

		assertTrue(expectedStrategyTypes.containsAll(returnedStrategies));
		assertTrue(returnedStrategies.containsAll(expectedStrategyTypes));

		List<String> returnedStrategiesTypeA = Arrays
				.asList(returnedMap.get("example/strategy/strategytype/typea/CompliantStrategyTypeA"));

		assertTrue(expectedStrategyA.containsAll(returnedStrategiesTypeA));
		assertTrue(returnedStrategiesTypeA.containsAll(expectedStrategyA));

		List<String> returnedStrategiesTypeB = Arrays
				.asList(returnedMap.get("example/strategy/strategytype/typea/CompliantStrategyTypeB"));

		assertTrue(expectedStrategyB.containsAll(returnedStrategiesTypeB));
		assertTrue(returnedStrategiesTypeB.containsAll(expectedStrategyB));
	}

	@Test
	public void testProperUsingDetection() {
		setupAnalyzer();
	}

	@Test
	public void testReturnType() {
		setupAnalyzer();
	}
}
