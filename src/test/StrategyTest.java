
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.ReturnType;
import domain.analyzer.StrategyAnalyzer;

public class StrategyTest extends AnalyzerFixture<StrategyAnalyzer>{

	private final String[] exampleClasses = {
			"example/strategy/strategytype/typea/CompliantStrategyA1",
			"example/strategy/strategytype/typea/CompliantStrategyA2",
			"example/strategy/strategytype/typea/CompliantStrategyTypeA",
			"example/strategy/strategytype/typea/NonCompliantStrategyA3ImplementsOtherInterfaces",
			"example/strategy/strategytype/typeb/CompliantStrategyB1",
			"example/strategy/strategytype/typeb/CompliantStrategyB2",
			"example/strategy/strategytype/typeb/CompliantStrategyB3",
			"example/strategy/strategytype/typeb/CompliantStrategyTypeB",
			"example/strategy/strategytype/typeb/NonCompliantStrategyB4ImplementsOtherStrategyInterface",
			"example/strategy/strategytype/typec/WarningStrategyTypeCNoStrategies",
			"example/strategy/CompliantClassUsingStrategyA",
			"example/strategy/CompliantClassUsingStrategyB",
			"example/strategy/CompliantClassUsingMultipleSingleStrategy",
			"example/strategy/CompliantClassUsingDifferentSingleStrategy",
			"example/strategy/NonCompliantClassUsingConcreteStrategy",
			"example/strategy/CompliantFinalClassUsingStrategyWithMethods",
			"example/strategy/CompliantAbstractClassUsingSomeStrategy"
	};

	@Override
	@BeforeEach
	protected void initAnalyzerUUT() {
		this.populateParserData(exampleClasses);
		this.analyzer = new StrategyAnalyzer(parser);
		analyzer.getRelevantData(exampleClasses);		
	}

	@Test
	public void testStrategyTypeDetection() {
		analyzer.sweepInterfaces(exampleClasses);
		analyzer.lintInterfaceList();

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

		Map<String, List<String>> returnedMap = this.analyzer.returnStrategyMap();

		assertEquals(2, returnedMap.keySet().size());

		List<String> returnedStrategies = new ArrayList<>(returnedMap.keySet());

		assertTrue(expectedStrategyTypes.containsAll(returnedStrategies));
		assertTrue(returnedStrategies.containsAll(expectedStrategyTypes));

		List<String> returnedStrategiesTypeA = returnedMap
				.get("example/strategy/strategytype/typea/CompliantStrategyTypeA");

		assertTrue(expectedStrategyA.containsAll(returnedStrategiesTypeA));
		assertTrue(returnedStrategiesTypeA.containsAll(expectedStrategyA));

		List<String> returnedStrategiesTypeB = returnedMap
				.get("example/strategy/strategytype/typeb/CompliantStrategyTypeB");

		assertTrue(expectedStrategyB.containsAll(returnedStrategiesTypeB));
		assertTrue(returnedStrategiesTypeB.containsAll(expectedStrategyB));
	}

	@Test
	public void testProperUsingDetection() {
		analyzer.sweepInterfaces(exampleClasses);
		analyzer.lintInterfaceList();
		analyzer.getUsingClasses();

		List<String> expectedCompliantClasses = new ArrayList<>();
		expectedCompliantClasses.add("example/strategy/CompliantClassUsingStrategyA");
		expectedCompliantClasses.add("example/strategy/CompliantClassUsingStrategyB");
		expectedCompliantClasses.add("example/strategy/CompliantClassUsingMultipleSingleStrategy");
		expectedCompliantClasses.add("example/strategy/CompliantClassUsingDifferentSingleStrategy");
		expectedCompliantClasses.add("example/strategy/CompliantAbstractClassUsingSomeStrategy");
		expectedCompliantClasses.add("example/strategy/CompliantFinalClassUsingStrategyWithMethods");

		List<String> expectedUsesA = new ArrayList<>();
		expectedUsesA.add("example/strategy/strategytype/typea/CompliantStrategyTypeA");

		List<String> expectedUsesB = new ArrayList<>();
		expectedUsesB.add("example/strategy/strategytype/typeb/CompliantStrategyTypeB");

		List<String> expectedUsesBoth = new ArrayList<>();
		expectedUsesBoth.add("example/strategy/strategytype/typea/CompliantStrategyTypeA");
		expectedUsesBoth.add("example/strategy/strategytype/typeb/CompliantStrategyTypeB");

		Map<String, List<String>> returnedMap = analyzer.returnCompliantClassUsings();
		assertEquals(6, returnedMap.keySet().size());

		List<String> actualUsingClasses = new ArrayList<>(returnedMap.keySet());
		assertTrue(actualUsingClasses.containsAll(expectedCompliantClasses));
		assertTrue(expectedCompliantClasses.containsAll(actualUsingClasses));

		List<String> usedStrategies1 = returnedMap.get("example/strategy/CompliantClassUsingStrategyA");
		assertTrue(usedStrategies1.containsAll(expectedUsesA));
		assertTrue(expectedUsesA.containsAll(usedStrategies1));

		List<String> usedStrategies2 = returnedMap.get("example/strategy/CompliantClassUsingStrategyB");
		assertTrue(usedStrategies2.containsAll(expectedUsesB));
		assertTrue(expectedUsesB.containsAll(usedStrategies2));

		List<String> usedStrategies3 = returnedMap.get("example/strategy/CompliantClassUsingMultipleSingleStrategy");
		assertTrue(usedStrategies3.containsAll(expectedUsesA));
		assertTrue(expectedUsesA.containsAll(usedStrategies3));

		List<String> usedStrategies4 = returnedMap.get("example/strategy/CompliantClassUsingDifferentSingleStrategy");
		assertTrue(usedStrategies4.containsAll(expectedUsesBoth));
		assertTrue(expectedUsesBoth.containsAll(usedStrategies4));

		List<String> usedStrategies5 = returnedMap.get("example/strategy/CompliantFinalClassUsingStrategyWithMethods");
		assertTrue(usedStrategies5.containsAll(expectedUsesA));
		assertTrue(expectedUsesA.containsAll(usedStrategies5));

		List<String> usedStrategies6 = returnedMap.get("example/strategy/CompliantAbstractClassUsingSomeStrategy");
		assertTrue(usedStrategies6.containsAll(expectedUsesB));
		assertTrue(expectedUsesB.containsAll(usedStrategies6));

	}

	@Test
	public void testReturnType() {
		analyzer.analyzeData();
		ReturnType actual = analyzer.composeReturnType();

		assertEquals("Strategy Pattern Detection", actual.analyzerName);
		assertEquals(19, actual.errorsCaught.size());
	}

}
