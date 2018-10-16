/**
 * 
 */
package fr.snapgames.bgf.test;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.snapgames.bgf.App;
import fr.snapgames.bgf.GameObject;

/**
 * @author 212391884
 *
 */
public class ApplicationSteps {

	App application;
	Collection<GameObject> objects;
	String title;

	@Given("^An application$")
	public void an_Application() {
		application = new App("mytests");
		application.run();
	}

	@When("^getting the object list$")
	public void getting_the_object_list() {
		objects = application.getObjects();
	}

	@Then("^it has at least ([0-9]+) GameObject$")
	public void has_At_Least_N_GameObject(int nb) {
		assertTrue(objects.size() > nb);
	}

}
