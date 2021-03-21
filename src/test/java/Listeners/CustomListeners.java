/**
 * 
 */
/**
 * @author Surbhi Garg
 *
 */
package test.java.Listeners;


import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import test.java.Utilities.*;

public class CustomListeners implements ITestListener{
	public static String testName;

	public void onTestStart(ITestResult result) {
		Reporter.log("About to begin executing Suite" +" "+ result.getName(), true);
	}

	public void onTestSuccess(ITestResult result) {
		
	
	testName = result.getName();
	
	System.setProperty("org.uncommons.reportng.escape-output", "false");
	

	try {
		TestUtil.captureScreenshot();
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Reporter.log("<a href="+TestUtil.fileName+" target=\"_blank\">Screenshot Captured</a>");
	Reporter.log("<br>");
	Reporter.log("<a href="+TestUtil.fileName+" target=\"_blank\"><img src="+TestUtil.fileName+" height=300 width=300></a>");
	Reporter.log("Test case Executed successfully" +" "+ result.getName(), true);

	}

	public void onTestFailure(ITestResult result) {
		
		
		testName = result.getName();
	
		System.setProperty("org.uncommons.reportng.escape-output", "false");

		try {
			TestUtil.captureScreenshot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Reporter.log("<a href="+TestUtil.fileName+" target=\"_blank\">Screenshot Captured</a>");
		Reporter.log("<br>");
		Reporter.log("<a href="+TestUtil.fileName+" target=\"_blank\"><img src="+TestUtil.fileName+" height=300 width=300></a>");
		Reporter.log("Test case failed" +" "+ result.getName(), true);
	}

	public void onTestSkipped(ITestResult result) {
		
		Reporter.log("Test case has been skipped" +" "+ result.getName(), true);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
		Reporter.log("Test case has failed with success percentage" +" "+ result.getName(), true);
		
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
