/**
 * 
 */
/**
 * @author Surbhi Garg
 *
 */
package test.java.TestCases;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.java.Utilities.*;

import test.java.Utilities.*;
import org.apache.poi.*;
import org.apache.log4j.Logger;

public class StartGame extends TestBase {

	@Test(dataProvider = "Login")

	public void login(String WarriorName){
	if(isElementPresent("warrior_name_ID"))
	{
		type("warrior_name_ID",WarriorName);
	}
	click ("create_warrior_ID");
	Config.getProperty("explicit.wait");

if(isElementPresent("start_journey_ID"))
{
	Config.getProperty("explicit.wait");
	click("start_journey_ID");
	if(isElementPresent("heading_XPATH") && Element_text("heading_XPATH").equalsIgnoreCase("covid-19 the game"))
	{
		log.debug("warrior is  created successfully "+WarriorName);
		System.out.println("warrior is  created successfully");
	}
}
else
{
	
	//AssertJUnit.assertTrue(!isElementPresent("error_message_XPATH"));
	log.debug("warrior is not created successfully "+WarriorName);
	
}
}
	
	@DataProvider
	public Object[][] Login() {
		return TestUtil.getData("Login");
	}
	
	
}
