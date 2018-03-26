package com.maven.browserMob2;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverSetup {

	WebDriver driver = null;
	DesiredCapabilities capability = null;
	Map<String, String> map = null;

	public WebDriverSetup() {
		// TODO Auto-generated constructor stub
	}

	public WebDriver getDriverInstance(String browser)
			throws MalformedURLException {
		map = LoadPlaceHolders.getProperties();
		System.out.println(map.get("remoteExecution"));
		if (map.get("remoteExecution").equalsIgnoreCase("false")) {

			if (browser.equalsIgnoreCase("Chrome")) {
				System.out.println("inside chrome block..");
				System.setProperty("webdriver.chrome.driver",
						"./drivers/win/chromedriver.exe");
				driver = new ChromeDriver();
			} else if (browser.equalsIgnoreCase("IE")) {
				System.setProperty("webdriver.ie.driver",
						"./drivers/win/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			} else if (browser.equalsIgnoreCase("FF")) {
				driver = new FirefoxDriver();
			}

		} else {

			capability = new DesiredCapabilities();
			if (browser.equals("FF")) {
				System.out.println(browser);
				capability = DesiredCapabilities.firefox();
				capability.setBrowserName("firefox");
				capability.setPlatform(Platform.WINDOWS);
				capability.setJavascriptEnabled(true);
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

			} else if (browser.equals("CH")) {
				System.out.println(browser);
				capability = DesiredCapabilities.chrome();
				capability.setBrowserName("chrome");
				capability.setPlatform(Platform.WINDOWS);

			} else if (browser.equals("IE")) {
				System.out.println(browser);
				capability = DesiredCapabilities.internetExplorer();
				capability.setBrowserName("iexplore");
				capability.setPlatform(Platform.WINDOWS);

			}
			System.out.println("Property: "
					+ System.getProperty("webdriver.ie.driver"));
			driver = new RemoteWebDriver(new URL(map.get("gridhubUrl")),
					capability);

		}
		driver.get(map.get("baseURL"));
		return driver;
	}

}
