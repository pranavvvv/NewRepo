import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maven.browserMob2.LoadPlaceHolders;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class CaptureNetworkTraffic {
	
	
	
	
	public WebDriver driver;
	public BrowserMobProxy proxy;
	Map<String, String> map = null;
	
	@BeforeTest
	public void setUp() {
		
	   // start the proxy
	    proxy = new BrowserMobProxyServer();
	    proxy.start(0);

	    //get the Selenium proxy object - org.openqa.selenium.Proxy;
	    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

	    // configure it as a desired capability
	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
	    capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		
	    //set chromedriver system property
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\pranav.sharma\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver(capabilities);
		
	    // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
	    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

	    // create a new HAR with the label ""
	    proxy.newHar("selenium.com");

	    // open seleniumeasy.com
	    
	    map = LoadPlaceHolders.getProperties();
	    driver.get(map.get("url"));
	    driver.manage().window().maximize();
        
	}
	
	@Test
	public void testCaseOne() throws InterruptedException {
		driver.findElement(By.id("userName")).sendKeys(map.get("name"));
		 driver.findElement(By.id("password")).sendKeys(map.get("password"));
		 JavascriptExecutor jse = (JavascriptExecutor)driver;
		 jse.executeScript("window.scrollBy(0,100)");
		 Thread.sleep(3000);
		 driver.findElement(By.className("login-btn")).click();
	}
	
	@AfterTest
	public void tearDown() throws JsonProcessingException, IOException {

		// get the HAR data
		Har har = proxy.getHar();

		// Write HAR Data in a File
		File harFile = new File("C:\\tmp\\NewFile.har");
		try {
			har.writeTo(harFile);
		} catch (IOException ex) {
			 System.out.println (ex.toString());
		     System.out.println("Could not find file " );
		     
		}
		
		 ObjectMapper mapper = new ObjectMapper();
	     JsonNode root = mapper.readTree(new File("C:\\tmp\\NewFile.har"));
	     JsonNode entries = root.path("log").path("entries");
	     for (JsonNode entry : entries) {
	    	 if(entry.get("request").get("postData") != null && !entry.get("request").get("postData").isEmpty(null))
	         System.out.println("request:\n" + entry.get("request") + "\n");
	       //  System.out.println("response:\n" + entry.get("response") + "\n\n");
		
		//if (driver != null) {
			//proxy.stop();
			//driver.quit();
		//}
	}
	}
}