package fr.snapgames.bgf.test;

import org.junit.Test;

import fr.snapgames.bgf.core.Game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;

/**
 * Unit test for simple Game.
 */

@FixMethodOrder
public class AppTest{

    /**
     * Rigourous Test :-)
     */
    @Test
    public void testAppTitle()
    {
        Game app = new Game("My title", new String[]{});
        assertTrue(app.getTitle().equals("My title"));
    }

    @Test
    public void testAppWindowSize(){
        Game app = new Game("My title", new String[]{"w=320","h=240"});
        app.initialize();
        assertNotNull("The Window object is null !", app.getWindow());
        assertEquals("the window width is not set to the requested value", app.getRender().getBuffer().getWidth(),320);
        assertEquals("the window height is not set to the requested value", app.getRender().getBuffer().getHeight(),240);
    }
}
