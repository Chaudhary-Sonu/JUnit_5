package io.javabrains;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestReporter;

	/*

	 1. There is also a technical solution for the above situation in line:44 and that is by annotating the test class with
			@TestInstace(TesTInstacne.Lifecycle.PER_CLASS). It is basically of 2 types that is 
			1. "PER_CLASS" - (single instance for the complete class).
			2. "PER_METHOD" - (create an instance before every method).
	
	 2. @TestInstance(TestInstance.Lifecycle.PER_METHOD)
		This will basically create the object of class for which you are writing the unit tests, based on PER_METHOD or PER_CLASS.
	
	*/

class MathUtilsTest {
	
	/*
	 
	 1. @BeforeAll static void beforeAllInit() {
	 		System.out.println("This is the @BeforeAll method"); }
	
     2. Below method can be used in case when the class is annotated with @TestInstance, in that case no need for the @BeforeAll method
    	to be declared as static.
    		@BeforeAll void beforeAllInitCase2() {
	 		System.out.println("This is the @BeforeAll method"); }
	
	 3. All the test cases in the codeBase should be independent of each other, as Junit-5 creates separate instances for all test methods.
	 		@BeforeAll and @AfterAll --> these methods executes even before the instance of the class for which the testClass is written is created,
	 		so, in that case if u'll run the test class it will throw an error. In order to get rid of this issue you need to make the
	 		@BeforeAll and @AfterAll methods to be static. Because, static methods executes even before the instances are created.
	 
	 */
	
	MathUtils mathUtils;
	TestInfo testInfo;
	TestReporter testReporter;	
	
	@BeforeEach
	void initialization(TestInfo testInfo, TestReporter testReporter) {
		this.testInfo = testInfo;
		this.testReporter = testReporter;
		/*
		 1. Using "TestInfo" and "TestReporter" -> are basically 2 interfaces that Junit is Injecting to get the various info for a specific test case.
		 
		 2. "TestInfo" is used to get specific info about a test case.
		 2. "TestReporter" is used to publish something as the output about the specific test case that contains it. 
		*/
		
		//Creating object of the class before each method (test case / test method) is executed.
		mathUtils = new MathUtils();
	}
	
	
	//It will execute after each test case.
	@AfterEach
	void afterEach() { 
		System.out.println("Cleaning Up");
	}	
	

	//@Test is the annotation that is used to tell the jUnit platform that it's a test. Similarly, as JavaCompiler search for main method.
	@Test
	void test() {
		int expected = 25;
		int actual = mathUtils.add(20, 5);
		assertEquals(expected, actual, "The Add method should have 2 inputs");
		assertEquals(expected, actual, () -> "The Add method should have 2 inputs, Junit will only call this lambda if test case fails");
		/*
		  	The string that we have added at last will only be executed if the test method fails, but sometimes writing string can be an expensive thing,
		  	so rather we can just pass a "LAMBDA Expression" there which JUnit will only call if the test case fails.
		 */
	}
	
	
	//@Test
	@RepeatedTest(3) //@RepeatedTest() is used in a scenario where you want the specific test to be executed few times.
	@DisplayName("Testing areaOfCircle method") //@DisplayName is used to give specific name to your test case for better readability.
	void testAreaOFCircle(RepetitionInfo repetitionInfo) {
		repetitionInfo.getCurrentRepetition();
		assertEquals(Math.PI * 2 * 2, mathUtils.areaOfCircle(2), "Should get the radius as Input");
	}
	
	
	/*
	 	Suppose you did some TDD(Test Driven Development) and you created some method that will fail when you'll run the build. So, in that case 
	 	you can mark the specific method as @Disabled -> it will make your build success and won't execute that specific test case.
	*/
	@Test
	@Disabled
	void testDivide() {
		assertThrows(ArithmeticException.class, () -> mathUtils.divide(1, 0), "Throws an Arithematic Exception");
		//assertThrows --> it basically validates the particular exception thrown by the method. If you pass in some different exception it would fail.
	}
	
	
	//Use of Assumptions
	//Assuming for a certain value and then only executing the test. Otherwise, don't run the test.
	@Test
	void testAssumptions() {
		boolean isServerUp = false;
		assumeTrue(isServerUp);
	}
	
	
	//Using assertAll -> in case you need to check quite a few assertions as once the you can use assertAll
	@Test
	void testMultiply() {
		assertAll(
				() -> assertEquals(4, mathUtils.multiply(2, 2)),
				() -> assertEquals(0, mathUtils.multiply(2, 0)),
				() -> assertEquals(-2, mathUtils.multiply(2, -1))
				);
	}
	
	
	//Using Nested test Cases.
	@Nested
	@Tag("Multiply") 
	//@Tag -> Suppose, you need to run tests which have specific "Tags" only, then you can create new configuration and mention about
					//the tag there and run that to run those specific tests. You can also configure this thing in <maven-dependency> file.
	class testMultiply {
		
			@Test
			@Tag("PositiveMultiplyCheck")
			@DisplayName("testMultiplyPositive-(Case-1)")
			void testMultiplyPositive() {
				assertEquals(4, mathUtils.multiply(2, 2));
				System.out.println("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags());
				testReporter.publishEntry("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags());
			}
			
			@Test
			@Tag("NegativeMultiplyCheck")
			@DisplayName("testMultiplyPositive-(Case-2)")
			void testMultiplyNegative() {
				assertEquals(-2, mathUtils.multiply(2, -1));
				System.out.println("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags());
				testReporter.publishEntry("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags());
			}
			
			@Test
			@Tag("ZeroMultiplyCheck")
			@DisplayName("testMultiplyPositive-(Case-3)")
			void testMultiplyZero() {
				assertEquals(0, mathUtils.multiply(2, 0));
				System.out.println("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags());
				testReporter.publishEntry("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags());
			}
	}

}