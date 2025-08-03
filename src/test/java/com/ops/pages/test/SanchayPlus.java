package com.ops.pages.test;

import org.testng.annotations.Test;

import com.base.BaseTest;
import com.ops.page.HomePage;
import com.ops.page.ProductSelection;

public class SanchayPlus extends BaseTest{
	public SanchayPlus() {
		super();
	}
	public SanchayPlus(String browser) {
		super(browser);
	}
	@Test(priority = 1,enabled = true)
public void quoteCreationSanchayPlus() {
	setBrowser("chrome");
	HomePage homepage = new HomePage(getDriver());
	homepage.launchBasePage();
	ProductSelection productselection = new ProductSelection(getDriver());
	productselection.proudcSelection("Saving","HDFC Life Sanchay Plus");
	
	
}
}
