package fr.snapgames.bgf.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import fr.snapgames.bgf.App;
import static junit.framework.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        App app = new App("My title");
        assertTrue( app.getTitle().equals("My title"));
    }
}
