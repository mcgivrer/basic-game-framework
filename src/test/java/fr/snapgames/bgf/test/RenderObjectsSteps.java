package fr.snapgames.bgf.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cucumber.api.java8.En;
import fr.snapgames.bgf.App;
import fr.snapgames.bgf.GameObject;
import fr.snapgames.bgf.Render;

public class RenderObjectsSteps implements En {

	private Render render;
	private App application;
	private String[] args;
	private Collection<GameObject> objects;

	public RenderObjectsSteps() {
		Given("^A new App with viewport set to (\\d+) x (\\d+)$", (Integer width, Integer height) -> {
			args = new String[] { "w=" + width, "h=" + height };
			application = new App("Render tests", args);
			application.initialize();
		});

		When("^adding (\\d+) GameObject$", (Integer arg1) -> {
			render = application.getRender();
			render.clearRenderingList();
			for (int i = 0; i < arg1; i++) {
				render.addObject(GameObject.builder("testObj_" + i));
			}
		});

		When("^adding a list of (\\d+) GameObject$", (Integer arg1) -> {
			render = application.getRender();
			render.clearRenderingList();
			List<GameObject> goToAdd = new ArrayList<>();
			for (int i = 0; i < arg1; i++) {
				goToAdd.add(GameObject.builder("testObj_" + i));
			}
			render.addAllObjects(goToAdd);
		});

		Then("^the internal rendering list contains (\\d+) element$", (Integer arg1) -> {
			objects = render.getRenderingList();
			assertTrue(objects.size() == arg1);
		});

	}
}