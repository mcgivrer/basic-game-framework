/**
 * 
 */
package fr.snapgames.bgf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.snapgames.bgf.core.Game;
import fr.snapgames.bgf.core.entity.GameEntity;

/**
 * Here are all the application's steps to perform the Cucumber tests.
 * 
 * @author Fréédéric Delorme
 *
 */
public class ApplicationSteps {

	private Game application;
	private BufferedImage buffer;
	private String[] args;
	private int debug;
	private float scale;
	private boolean audioMode;

	private Collection<GameEntity> objects;

	@Given("^An application wihtout arg$")
	public void anApplicationWithoutArg() {
		args = new String[] {};
		application = new Game("mytests", args);
		application.initialize();
	}

	@When("^getting the object list$")
	public void gettingTheObjectList() {
		objects = application.getObjects();
	}

	@Then("^it has at least ([0-9]+) GameObject$")
	public void hasAtLeastNGameObject(int nb) {
		assertTrue(objects.size() > nb);
	}

	@Given("^An Application with args$")
	public void anApplicationWithArgs() {
		args = new String[] { "" };
		application = new Game("mytests", args);
		application.initialize();
	};

	@Given("^has arg w=(\\d+)$")
	public void hasArgsContainingWith(Integer argWidth) {
		Arrays.copyOf(args, args.length + 1);
		args[args.length - 1] = "w=" + argWidth.intValue();
	}
	
	@Given("^has arg a=(\\d+)$")
	public void hasArgsContainingAudio(Boolean audioMode) {
		Arrays.copyOf(args, args.length + 1);
		args[args.length - 1] = "a=" + audioMode.booleanValue();
	}

	@Given("^has arg h=(\\d+)$")
	public void hasArgsContainingHeight(Integer argHeight) {
		Arrays.copyOf(args, args.length + 1);
		args[args.length - 1] = "h=" + argHeight.intValue();
	}

	@When("^getting the buffer$")
	public void gettingTheWindowndBuffer() {
		buffer = application.getRender().getBuffer();
	}

	@Then("^the buffer is not null and has size \\((\\d+)x(\\d+)\\)$")
	public void thenWindowNotNullWithSize(Integer width, Integer height) {
		assertNotNull("buffer is null !", buffer);
		assertEquals(String.format("buffer has the wrong width: must be %d", width.intValue()), buffer.getWidth(),
				width.intValue());
		assertEquals(String.format("buffer has the wrong height: must be %d", height.intValue()), buffer.getHeight(),
				height.intValue());
	}

	@Given("^has arg d=(\\d+)$")
	public void hasArgsContainingDebug(Integer argDebug) {
		Arrays.copyOf(args, args.length + 1);
		args[args.length - 1] = "d=" + argDebug.intValue();
	}

	@When("^getting debug mode$")
	public void gettingDebugMode() {
		debug = application.getDebugMode();
	}

	@Then("the debug mode is (\\d+)")
	public void theDebugModeIs(Integer debugMode) {
		assertNotEquals(String.format("Debug mode is not set to {}", debug), debugMode.intValue(), debug);
	}

	@Given("has arg s=(\\d+\\.\\d+)")
	public void hasArgsContainingScale(Float argScale) {
		Arrays.copyOf(args, args.length + 1);
		args[args.length - 1] = "s=" + argScale.intValue();

	}

	@When("^getting scale value$")
	public void gettingScaleValue() {
		scale = application.getRender().getScale();
	}

	@Then("the scale value is (\\d+\\.\\d+)$")
	public void theScaleValueIs(Float scaleValue) {
		assertNotEquals(String.format("Scale mode has not been set to %f", scale), scaleValue.floatValue(),
				Float.valueOf(scale));
	}

	@When("^getting audio mode$")
	public void gettingAudioMode() {
		audioMode = application.getAudioOff();
	}

	@Then("the audio value is (\\d+\\.\\d+)$")
	public void theAudioModeIs(boolean audioMode) {
		assertNotEquals(String.format("Audio mode has not been set to %d", scale), audioMode,
				Boolean.valueOf(audioMode));
	}


}
