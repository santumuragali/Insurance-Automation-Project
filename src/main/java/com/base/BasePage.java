package com.base;

import org.apache.logging.log4j.Logger;
import com.utility.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

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
}