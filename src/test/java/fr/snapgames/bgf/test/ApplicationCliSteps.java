/**
 * 
 */
package fr.snapgames.bgf.test;

import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;
import fr.snapgames.bgf.App;

/**
 * BDD test for App command line
 * 
 * @author Frédéric Delorme
 *
 */
public class ApplicationCliSteps implements En {

	static private App application;
	static private String[] args;
	static private String title;

	public ApplicationCliSteps() {

		Given("^An application with title \"([^\"]*)\"$", (String title) -> {
			args = new String[] {};
			application = new App(title, args);
		});

		When("^getting the application title$", () -> {
			title = application.getTitle();
		});

		Then("^title is \"([^\"]*)\"\\.$", (String titleValue) -> {
			assertEquals(String.format("The title %s is not the arg value %s", titleValue, title), titleValue, title);
		});
	}
}
