package com.ops.page;

import org.openqa.selenium.WebDriver;

import com.base.BasePage;

public class HomePage extends BasePage{

	public HomePage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	public void launchBasePage() {
		launchOpsUrl();
	}
	public void handleAlert() {
		alert("dismiss");
	}

}
