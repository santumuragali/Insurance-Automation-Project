package com.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.utility.Log;
import com.aventstack.extentreports.MediaEntityBuilder;

public abstract class BaseTest {
	public static ResourceBundle gridproperties;
	public static ResourceBundle globalProperties;
	public static String Webdriveropsurl;
	public String externalsheet;
	protected static  WebDriver driver;
	public ExtentReports extent;
	public static ExtentTest extendReporterLogger;
	public String testname = "SanchayPlus";
	private static final Logger logger = Log.getInstance(BaseTest.class);
	private ExtentTest extentTest;

	public WebDriver getDriver() {
		return driver;
	}

	public BaseTest() {
		getgridProperties();
		getGlobalProperties();
	}

	public BaseTest(String browser) {
		setBrowser(browser);
	}

	@BeforeTest
	public void beforeTest(ITestContext context) {
		String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String reportPath = System.getProperty("user.dir") + "/test-output/ExteReport/ExtentReport" + date
				+ "/extentReport.html";
		ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extendReporterLogger = extent.createTest("Executting Suite :: " + (context.getSuite().getName()) + "::Test::"
				+ context.getCurrentXmlTest().getName());
		extendReporterLogger.log(Status.INFO, "Executing class :: " + this.getClass().getSimpleName());
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		String testmethodname = method.getName();
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		extentTest = extent.createTest(testmethodname);
		extentTest.log(Status.INFO, "Starting test: " + testmethodname);
		logger.info("\n" + "\nTest case name                 :" + testname + "\nTest method name               :"
				+ testmethodname + "\nStart time                     :" + currentTime+"/n"
				+ "--------------------------------------------------------------------------------------------");
	}

	@AfterMethod
	public void afternethod(ITestResult result) throws IOException {
		String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String currentTime = new SimpleDateFormat("HH-mm-ss").format(new Date());
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.log(Status.FAIL, "Test case failed: " + result.getName());
			extentTest.log(Status.FAIL, "Reason: " + result.getThrowable());
			String screenshot = takeScreenshot();
			extentTest.fail("Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
		} else if (result.getStatus() == ITestResult.SKIP) {
			extentTest.log(Status.SKIP, "Test case skipped: " + result.getName());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			extentTest.log(Status.PASS, "Test case passed: " + result.getName());
		}
		logger.info("aftre performing test cleanup closing webdriver \n"+"endtime   :"+currentDate+ "time :  "+currentTime);
	}

	@AfterTest
	public void afterTest() {
		if (extent != null) {
			extent.flush();
		}
	}

	public void setBrowser(String browserName) {
		if ("chrome".equalsIgnoreCase(browserName)) {
		    ChromeOptions options = new ChromeOptions();
		    options.addArguments("--disable-notifications");
			driver = new ChromeDriver(options);
			logger.info("Chrome browser launched.");
		} else if ("firefox".equalsIgnoreCase(browserName)) {
			driver = new FirefoxDriver();
			logger.info("Firefox browser launched.");
		} else {
			logger.error("Unsupported browser: " + browserName);
			throw new IllegalArgumentException("Unsupported browser: " + browserName);

		}
	}

	public String takeScreenshot() throws IOException {
		String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String currentTime = new SimpleDateFormat("HH-mm-ss").format(new Date());
		String folderPath = "test-output/ExteReport/ExtentReport" + currentDate + "/screenshot/";
		String fileName = testname + "_" + currentTime + ".png";
		String fullPath = folderPath + fileName;
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(src, new File(fullPath));
			logger.info("Screen shot has been taken");
		} catch (IOException e) {
			logger.error("The filename, directory name,or volume label syntax of screenshot is inqcorrect");
		}
		return fullPath;

	}

	protected void getgridProperties() {
		gridproperties = ResourceBundle.getBundle("grid");
		Webdriveropsurl = gridproperties.getString("Webdriver_opsurl");
		logger.info("Loaded grid properties. Webdriver_opsurl: " + Webdriveropsurl);
	}

	protected void getGlobalProperties() {
		globalProperties = ResourceBundle.getBundle("global");
		externalsheet = globalProperties.getString("ExcellSheet");
		logger.info("Loaded global properties. ExcellSheet: " + externalsheet);
	}

	public static String getOpsUrl() {
		return Webdriveropsurl;
	}
	
	
}
