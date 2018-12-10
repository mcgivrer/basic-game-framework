/**
 * 
 */
package fr.snapgames.bgf.test;

import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;
import fr.snapgames.bgf.core.Game;

/**
 * BDD test for Game command line
 * 
 * @author Frédéric Delorme
 *
 */
public class ApplicationCliSteps implements En {

	private Game application;
	private String[] args;
	private String title;

	public ApplicationCliSteps() {

		Given("^An application with title \"([^\"]*)\"$", (String title) -> {
			args = new String[] {};
			application = new Game(title, args);
		});

		When("^getting the application title$", () -> {
			title = application.getTitle();
		});

		Then("^title is \"([^\"]*)\"\\.$", (String titleValue) -> {
			assertEquals(String.format("The title %s is not the arg value %s", titleValue, title), titleValue, title);
		});
	}
}
