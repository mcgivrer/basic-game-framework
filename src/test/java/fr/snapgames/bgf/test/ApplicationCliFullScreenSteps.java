/**
 * 
 */
package fr.snapgames.bgf.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import cucumber.api.java8.En;
import fr.snapgames.bgf.App;
import fr.snapgames.bgf.Window;

/**
 * @author 212391884
 *
 */
public class ApplicationCliFullScreenSteps implements En {
	static private App application;
	static private Window window;
	static private String[] args;

	private boolean fullScreenMode;

	/**
	 * 
	 */
	public ApplicationCliFullScreenSteps() {
		Given("^An Application fullWindow$", () -> {
			args = new String[] { "" };
			application = new App("fullWindow", args);
		});

		Given("^has arg f=(.*)+$", (Boolean fullScreenModeValue) -> {
			Arrays.copyOf(args, args.length + 1);
			args[args.length - 1] = "f=" + fullScreenModeValue;
		});

		Given("^Application is initialized$", () -> {
			application.setArgs(args);
		});

		When("^getting fullscreen status$", () -> {
			application.initialize();
			window = application.getWindow();
			fullScreenMode = window.isFullScreenMode();
		});

		Then("^the fullscreen status is (\\d+)$", (Boolean status) -> {
			fullScreenMode = status;
		});

		Then("^the window is in full screen mode$", () -> {
			assertTrue("Application is in pfull screen mode", window.isFullScreenMode() == fullScreenMode);
		});

	}

}
