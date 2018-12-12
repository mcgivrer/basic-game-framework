package fr.snapgames.bgf.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * This is the starting point for Unit testing of <code>Game</code>.
 * 
 * @see Game
 * 
 * @author Frédéric Delorme
 */
@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/features", format = { "pretty",
        "json:target/cucumber.json" }, tags = { "~@ignore" })
public class BDDTest {

}