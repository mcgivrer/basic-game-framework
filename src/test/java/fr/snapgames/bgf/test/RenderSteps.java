/**
 * 
 */
package fr.snapgames.bgf.test;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.snapgames.bgf.App;
import fr.snapgames.bgf.Render;

/**
 * Here are all the application's steps to perform the Cucumber tests.
 * 
 * @author Fréédéric Delorme
 *
 */
public class RenderSteps {

	private Render render;
	private App application;
	private String[] args;

	@Given("^A new App$")
	public void aNewApplication() {
		args = new String[] {};
		application = new App("Render tests", args);
		application.initialize();
	}

	@When("^getting the internal render$")
	public void gettingTheInternalRender() {
		render = application.getRender();
	}

	@Then("^the internal rendering buffer is set to (\\d+)x(\\d+)$")
	public void theInternalRenderingBufferIsSetTo(Integer width, Integer height) {
		BufferedImage buffer = render.getBuffer();
		assertTrue(buffer.getWidth() == 320);
		assertTrue(buffer.getHeight() == 240);
	}
}
