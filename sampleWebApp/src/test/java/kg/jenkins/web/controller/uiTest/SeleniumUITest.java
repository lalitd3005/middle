package kg.jenkins.web.controller.uiTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Created by TrialCI on 6/27/2018.
 */
//@Config(
//        url = "http://google.com", // base url that the test launches against
//        browser = Browser.CHROME // the browser to use.
//       )
public class SeleniumUITest { //extends Locomotive {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testHome() throws Exception {
        //System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
//
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless");
//        chromeOptions.addArguments("--disable-gpu");
//        WebDriver driver = new ChromeDriver(chromeOptions);
//        System.out.println("Testing....");
//        driver.get("http://google.com");
//        Thread.sleep(2000);
//        driver.quit();
    }
}