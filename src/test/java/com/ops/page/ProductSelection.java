package com.ops.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.base.BasePage;

public class ProductSelection extends BasePage{
	
	public ProductSelection(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	public void proudcSelection(String categeroy,String product) {
		if(categeroy.equalsIgnoreCase("Saving")) {
			WebElement saving = driver.findElement(By.xpath("//div[@id ='s2id_savings']"));
			saving.click();
			WebElement pass = driver.findElement(By.xpath("//input[@class='select2-input select2-focused']"));
			pass.sendKeys(product);
			Actions actions = new Actions(driver);
			actions.sendKeys(Keys.ENTER).build().perform();
			
		}
		WebElement clikingElemnt = driver.findElement(By.xpath("//a[text()='" + product + "']"));
		clikingElemnt.click();
		
	}

}
