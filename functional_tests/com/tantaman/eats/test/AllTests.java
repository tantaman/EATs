package com.tantaman.eats.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tantaman.eats.test.aop.AOPTests;
import com.tantaman.eats.test.core.InstrumentationTest;
import com.tantaman.eats.test.tools.ToolsTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AOPTests.class,
	InstrumentationTest.class,
	ToolsTests.class
})
public class AllTests {
}