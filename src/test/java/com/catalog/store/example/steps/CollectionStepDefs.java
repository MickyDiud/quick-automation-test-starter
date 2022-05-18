package com.catalog.store.example.steps;

import com.catalog.store.example.utils.TrackerStatus;
import com.qmetry.qaf.automation.step.CommonStep;
import com.qmetry.qaf.automation.step.QAFTestStepProvider;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.catalog.store.example.utils.StringUtilities;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.catalog.store.example.utils.ExcelUtilites.writeDataToFile;
import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.quantum.utils.DriverUtils.getDriver;

@QAFTestStepProvider
public class CollectionStepDefs extends GlobalStepDefs{

    private static final Logger logger = Logger.getLogger(CollectionStepDefs.class);

    /**
     *            : search term to be searched
     */
    @Given("^I am go to the home of catalog\\.lu store on OpenSea Page$")
    public void iAmGoToTheHomeOfCatalogLuStoreOnOpenSeaPage() {
        copyTemplateFileToData();
        initDriver();
    }

    @Then("^I verify the status of each value in the collection and populate them$")
    public void iVerifyTheStatusOfEachValueInTheCollectionAndPopulateThem()
            throws InterruptedException {

        Actions action = new Actions(webdriver);
        click("loc.grid.display");
        Set<String> recordSet = new HashSet<>();
        int initSize = 0, iterator = 0, counter = 0;

        while (true) {

            List<QAFWebElement> divGridViewItems = getDriver().findElements("loc.grid.view.items");
            int getSize = divGridViewItems.size();
            if (iterator < 1) {
                initSize = getSize*2;
                action.sendKeys(Keys.PAGE_DOWN).perform();
            }

            String beforeItem = StringUtilities.getItemNo(
                    getDriver().findElementByXPath("//div[@role=\"grid\"]/div["+getSize+"]//a").getAttribute("href"));

            System.out.println("before item is:" +beforeItem);

            System.out.println(">>>The total number of grid cells is :" + getSize);
            QAFWebElement gridCell = null;

            for (int i = 1; i <= getSize; i++) {

                TimeUnit.SECONDS.sleep(2);
                logger.debug("record No: " + (i));
                String eachCell = "//*[@role=\"grid\"]/div[" + i + "]";

                try {
                    gridCell = getDriver().findElement(By.xpath(eachCell));
                } catch (NoSuchElementException ex) {
                    System.err.println("This element is not found.");
                    continue;
                }

                TimeUnit.SECONDS.sleep(3);

                String anchorUrl = webdriver.findElementByXPath(eachCell + "//a").getAttribute("href");
                String clkItem = StringUtilities.getItemNo(anchorUrl);

                // Remove inspection duplicate items to ensure uniqueness with each click.
                if (recordSet.contains(clkItem)) {
                    System.out.println("duplicate no is : " + clkItem);
//                    action.sendKeys(Keys.PAGE_DOWN).perform();
                    continue;
                }

//                webdriver.executeScript("window.scrollBy()");
//                webdriver.executeScript("arguments[0].scrollIntoView();",
//                        webdriver.findElementByXPath(eachCell));

                // Add each item number into the collection for records.
                recordSet.add(clkItem);
                counter++;
                writeDataToFile(autoTestDataFile,testDataFileSheet,counter,0,String.valueOf(counter));
                writeDataToFile(autoTestDataFile,testDataFileSheet,counter,1,anchorUrl);
                System.out.println(anchorUrl);

                TimeUnit.SECONDS.sleep(2);
                try {
                    action.keyDown(Keys.CONTROL).click(gridCell).keyUp(Keys.CONTROL).perform();
                } catch (Exception e) {
                    // Fix:
                    if (gridCell.isPresent()) {
                        TimeUnit.SECONDS.sleep(2);
                        webdriver.executeScript("window.scrollBy()");
                        webdriver.executeScript("arguments[0].scrollIntoView();", webdriver.findElement(By.xpath(eachCell)));
                        gridCell = webdriver.findElement(By.xpath(eachCell));
                        action.keyDown(Keys.CONTROL).click(gridCell).keyUp(Keys.CONTROL).perform();
                    } else {
                        System.out.println("The lookup element does not exist and an error is logged.");
                    }
                }
                webdriver.switchTo().window(getLastWindowHandle().get(1));
                webdriver.getCurrentUrl();

//                action.click(gridCell).release().perform();
                logger.debug("record status is :" + TrackerStatus.Clicked);
                String itemNo = StringUtilities.getItemNo(webdriver.getCurrentUrl());

                CommonStep.click("loc.btn.refresh.metadata");
                writeDataToFile(autoTestDataFile,testDataFileSheet,counter,2,"Clicked");

                if (CommonStep.verifyPresent("loc.queued.alert")) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        String text = getDriver().findElement("loc.queued.alert.text").getText().split("\n")[1];
                        Assert.assertEquals(text, getBundle().getString("prop.text.queued"));

                        logger.debug("record status is: " + TrackerStatus.Queued);
                        writeDataToFile(autoTestDataFile,testDataFileSheet,counter,2,"Queued");
                        TimeUnit.SECONDS.sleep(15);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    logger.debug("record status is: " + TrackerStatus.Error);
                    writeDataToFile(autoTestDataFile,testDataFileSheet,counter,2,"Error");
                }
                webdriver.close();
                webdriver.switchTo().window(getLastWindowHandle().get(0));
            }

            TimeUnit.SECONDS.sleep(3);
            scrollDownIntoView(action, initSize);
            String afterItem;
            
            try {
                TimeUnit.SECONDS.sleep(3);
                afterItem = StringUtilities.getItemNo(
                        getDriver().findElementByXPath("//div[@role=\"grid\"]/div["+getSize+"]//a").getAttribute("href"));

                System.out.println("after item is:" + afterItem);

            } catch (NoSuchElementException | NullPointerException se) {
                System.err.println("This element is not found.");
                continue;
            }

            if (StringUtils.equals(beforeItem, afterItem)) {
                break;
            }
            iterator++;
        }
    }

    @And("^I close browser$")
    public void iCloseBrowser() {
        webdriver.quit();
    }
}
