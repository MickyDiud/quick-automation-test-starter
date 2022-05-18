package com.catalog.store.example.steps;

import com.catalog.store.example.utils.ExcelUtilites;
import com.qmetry.qaf.automation.step.CommonStep;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

public class GlobalStepDefs {

	private static final String address = "https://opensea.io/collection/catalog-lu-store";
	private static String templateAutoTestDataPath  = System.getProperty("user.dir") +"/data/";
	private static Logger logger = Logger.getLogger(GlobalStepDefs.class);

	protected static String testDataFileName  = getBundle().getString("test.data.file.excel");
	protected static String testDataFileSheet = getBundle().getString("test.data.file.sheet");
	protected static String autoTestDataFile  = templateAutoTestDataPath + testDataFileName;
	protected static QAFExtendedWebDriver webdriver = new WebDriverTestBase().getDriver();

	// new_built QAFExtendsWebElementObject
	public static QAFExtendedWebElement newlyBuild(String locator){
		return new QAFExtendedWebElement(locator);
	}


	protected static void initDriver() {

		System.setProperty("webdriver.chrome.driver",getDriverPath()) ;
		ChromeOptions options = new ChromeOptions();
		options.addArguments("no-sandbox");
		webdriver.get(address);

//		webdriver = new ChromeDriver(options);
		webdriver.manage().window().maximize();
//		webdriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
//		openBrowserToWebpage(address);
		logger.debug("Redirect to site address.");
	}

	private static String getDriverPath() {
//		URL path = StepsLibrary.class.getClassLoader().getResource("./driver/");
		String path = System.getProperty("user.dir") + "/driver/";
		String chromeDriverPath = path + "chromedriver.exe";
		System.out.println(chromeDriverPath);
		return chromeDriverPath;
	}

	public static void openBrowserToWebpage(String url) {
		webdriver.get(url);
	}

	// Encapsulation click function
	public static void click(String locator){
		CommonStep.click(locator);
		logger.debug("click button is " + locator + "\n");
	}

	/**
	 * Gets all handles from current open window
	 * @return
	 */
	public static List<String> getLastWindowHandle() {
		Set<String> Allhandles = webdriver.getWindowHandles();
		return new ArrayList<>(Allhandles);
	}

	public static void scrollDownIntoView(Actions action, int resolution) {

		try {
			TimeUnit.SECONDS.sleep(2);

			action.sendKeys(Keys.PAGE_DOWN).perform();

//			TimeUnit.SECONDS.sleep(2);
//			action.sendKeys(Keys.ARROW_DOWN).perform();

			while (true) {

				int itemSize = webdriver.findElements(By.xpath("//div[@role=\"grid\"]/div")).size();
				System.out.println(">>> " + itemSize);
				if ( itemSize < resolution) {
					System.out.println("<<< " + itemSize);
					break;
				}
				TimeUnit.SECONDS.sleep(1);
				action.sendKeys(Keys.ARROW_DOWN).perform();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void copyTemplateFileToData() {
		File sourceFile = new File(templateAutoTestDataPath+"template/"+testDataFileName);
		File destinationFile = new File(templateAutoTestDataPath + testDataFileName);
		try {
			FileUtils.copyFile(sourceFile, destinationFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("file copy");
	}
}
