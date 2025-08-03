package com.base;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.Logger;
import com.utility.Log;

import lombok.val;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected WebDriver driver;
    private static final Logger logger = Log.getInstance(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void launchOpsUrl() {
        String url = BaseTest.getOpsUrl();
        if (url != null) {
            try {
                driver.get(url);
                driver.manage().window().maximize();
                logger.info("Launched OPS URL: " + url);
            } catch (UnreachableBrowserException e) {
                logger.error("Unable to load the OPS URL: " + url, e);
                throw new UnreachableBrowserException("unable to load the ops url : " + url);
            }
        } else {
            logger.error("Unable to load the URL. Please provide a valid URL.");
            throw new UnreachableBrowserException("unable to load the url please provide valid url");
        }
    }
    public void alert(String value) {
    	Alert alert = driver.switchTo().alert();
    	logger.info("alert is populate in Screen "+alert.getText());
    	if(value.equalsIgnoreCase("dismiss")) {
    		alert.dismiss();
    	}
    	else if(value.equalsIgnoreCase("accept")) {
    		alert.accept();
    	}
    }
    
    public void select(WebElement elem,String text) {
    	Select se  =new Select(elem);
    	se.deselectByVisibleText(text);
    	logger.info("The elemnt is selected By value:  "+text);
    	
    }
    public  void addExplcitwait(By locator,int pollingtime,String condition) {
		logger.info("Waiting for element: " + locator.toString() + 
                " | Timeout: " + 10 + "s | Polling: " + pollingtime + "ms | Condition: " + condition);
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.pollingEvery(Duration.ofMillis(pollingtime));
			wait.ignoring(NoSuchElementException.class,StaleElementReferenceException.class);
			
			switch (condition.toLowerCase()) {
			case "visible":
				wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
				 logger.info("Element is visible: " + locator.toString());
				break;
			case "clickable":
			wait.until(ExpectedConditions.elementToBeClickable(locator));
				logger.info("Element is clickable: " + locator.toString());
			 break;
			case "present":
				wait.until(ExpectedConditions.presenceOfElementLocated(locator));
				break;
			}
		}
		catch(TimeoutException  e) {
			String msg = "Timeout after " + 10 + "s waiting for element with locator: " + locator.toString();
            logger.error(msg);
            throw new TimeoutException(msg, e);
		}
		catch(Exception e) {
			 String msg = "Unexpected error while waiting for element: " + locator.toString();
	            logger.error(msg);
	            throw new RuntimeException(msg, e);
		}
	}
    public void javaScriptiExecutor(WebElement element) {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	js.executeScript("arguments[0].click();", element);
    }
}