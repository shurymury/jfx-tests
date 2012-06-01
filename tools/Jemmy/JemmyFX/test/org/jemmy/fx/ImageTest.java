/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jemmy.fx;

import java.io.File;
import org.jemmy.fx.control.LabeledDock;
import org.jemmy.image.GlassImage;
import org.jemmy.image.Image;
import org.jemmy.image.ImageStore;
import org.jemmy.image.pixel.PNGFileImageStore;
import org.jemmy.resources.StringComparePolicy;
import org.jemmy.timing.State;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.*;

/**
 *
 * @author shura
 */
public class ImageTest {
    
    public ImageTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        AppExecutor.executeNoBlock(Controls.class);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    @Test
    public void hello() throws InterruptedException {
        final LabeledDock button = new LabeledDock(new SceneDock().asParent(), 
                "push me", StringComparePolicy.EXACT);
        Thread.sleep(2000);
        final Image beforeClick = button.wrap().getScreenImage();
        beforeClick.save("beforeClick.png");
        button.wrap().mouse().click();
        button.wrap().waitState(new State<Image>() {

            public Image reached() {
                return button.wrap().getScreenImage().compareTo(beforeClick);
            }
        });
        Thread.sleep(2000);
        final Image afterClick = button.wrap().getScreenImage();
        beforeClick.save("afterClick.png");
        button.wrap().mouse().press();
        Image afterPress = button.wrap().waitState(new State<Image>() {

            public Image reached() {
                return button.wrap().getScreenImage().compareTo(afterClick);
            }
        });
        button.wrap().mouse().release();
        button.wrap().waitImage(afterClick, "actual.png", "diff.png");
        Image afterRelease = button.wrap().getScreenImage();
        beforeClick.save("afterRelease.png");
        assertNotNull(afterRelease.compareTo(afterPress));
    }
}
