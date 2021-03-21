/**
 * 
 */
/**
 * @author Surbhi
 *
 */

package test.java.TestCases;

import java.io.BufferedReader;
import java.io.File;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;


import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.json.Json;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import test.java.Utilities.*;

public class TestBase {

	public static WebDriver driver;
	// public static DesiredCapabilities capabilities ;
	// public static ChromeDriverService service;
	
	
	public static Logger log = Logger.getLogger("devpinoyLogger");
	public static ExcelReader excel =new ExcelReader(
			System.getProperty("user.dir") + "//src//test//resources//excel//TestData_SampleProject.xlsx");

	public static Properties OR = new Properties();
	public static Properties Config = new Properties();
	public static Properties Text_verify = new Properties();
	public static FileInputStream fis;
	public static WebDriverWait wait;
	public static WebElement dropdown;

	@BeforeSuite
	public void setUp() {

		if (driver == null) {

			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "//src//test//resources//properties//Config.properties");
				
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
			try {
				Config.load(fis);
				System.out.println("Config properties loaded !!!");
				log.debug("Config properties loaded !!!");
			} catch (IOException e) {

				e.printStackTrace();
			}

			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "//src//test//resources//properties//OR.properties");
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
			try {
				OR.load(fis);
				log.debug("OR properties loaded !!!");
			} catch (IOException e) {

				e.printStackTrace();
			}
			//// Added recently
			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "//src//test//resources//properties//Text_verify.properties");
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
			try {
				Text_verify.load(fis);
				log.debug("Text Verify properties loaded !!!");
			} catch (IOException e) {

				e.printStackTrace();
			}

			//// check for the type of browser

			if (Config.getProperty("browser").equals("firefox")) {

				System.setProperty("webdriver.gecko.driver",
						System.getProperty("user.dir") + "//src//test//resources//geckodriver");

				driver = new FirefoxDriver();
				log.debug("Firefox Launched");
			} else if (Config.getProperty("browser").equals("chrome")) {
				// added this code for chrome crashing
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "/src/test/resources/executables/chromedriver");
				// capabilities = DesiredCapabilities.chrome();
				// String[] options = new String[] { "--start-maximized", "--headless" };
				// capabilities.setCapability("chrome.switches", options);
				// driver = new RemoteWebDriver(service.getUrl(), capabilities);
				driver = new ChromeDriver();
				log.debug("Chrome Launched");
			} else if (Config.getProperty("browser").equals("ie")) {

				System.setProperty("webdriver.ie.driver",
						System.getProperty("user.dir") + "//src//test//resources//executables//IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				log.debug("IE Launched");
			}

		//	driver.get(Config.getProperty("url"));
			driver.get(Config.getProperty("url"));
			log.debug("Navigated to : " + Config.getProperty("url"));
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(Config.getProperty("implicit.wait")),
					TimeUnit.SECONDS);
			wait = new WebDriverWait(driver, Integer.parseInt(Config.getProperty("explicit.wait")));

		}
		

	}

	public void click(String locator) {

		// condition put to manage the stale element exception
		boolean staleElement = true;
		while (staleElement) {
			if (locator.endsWith("_XPATH")) {
				try {
					driver.findElement(By.xpath(OR.getProperty(locator))).click();
					staleElement = false;
				} catch (StaleElementReferenceException e) {
					staleElement = true;
				}

			} else if (locator.endsWith("_CSS")) {
				try {
					driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
					staleElement = false;
				} catch (StaleElementReferenceException e) {
					staleElement = true;
				}
			}
			if (locator.endsWith("_ID")) {
				try {
					driver.findElement(By.id(OR.getProperty(locator))).click();
					staleElement = false;
				} catch (StaleElementReferenceException e) {
					staleElement = true;
				}
			}
			if (locator.endsWith("_TEXT")) {
				try {
					driver.findElement(By.linkText(OR.getProperty(locator))).click();
					staleElement = false;
				} catch (StaleElementReferenceException e) {
					staleElement = true;
				}
			}
		}

		log.debug("Clicking on an Element : " + locator);

	}
	//email fetch and read contents
	 public static void fetch(String Host, String user, String password)  throws MessagingException, IOException, NumberFormatException, InterruptedException{
		 	Folder emailFolder = null;
	        Store store = null;
	        String subject = null;
	        String url =null;
	        String email_content = "";
	        
	        TimeUnit.SECONDS.sleep(30);
	      //  WebDriverWait wait=new WebDriverWait(driver, 80);
	       
	        
		      try {
		         // create properties field
		         Properties properties = new Properties();
		         
		         if(Host.contains("googlemail"))
		         {
		         properties.setProperty("mail.store.protocol", "imaps");  
		         Session emailSession = Session.getDefaultInstance(properties,null);
		         emailSession.setDebug(true);

		         // create the imap store object and connect with the pop server
		         store = emailSession.getStore("imaps");
		         store.connect(Host, user, password);
		         }
		         else if(Host.contains("outlook"))
		         {
		        	 System.out.println("You are inside the hotmail properties settings");
		        	
		
		        	 Session emailSession = Session.getDefaultInstance(properties);
		        	 //emailSession.setDebug(true);

		         // create the pop3 store object and connect with the pop server
		        	 store = emailSession.getStore("imaps");
		       	 
		        	 store.connect(Host,993, user, password);
		        	
		         }
		         // create the folder object and open it
		         emailFolder = store.getFolder("INBOX");
		         emailFolder.open(Folder.READ_WRITE);
		         Flags seen=new Flags(Flags.Flag.SEEN);
		         FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
		       
		         int unCount = emailFolder.getUnreadMessageCount();
		         System.out.println("length of unread message is"+ unCount);
		         // retrieve the messages from the folder that are unread
		         Message[] messages = emailFolder.search(unseenFlagTerm);
		         int length = messages.length;
		         System.out.println("***********length: "+length);
		        for(int i=0; i<length; i++)
		        {
		        int desc = (length - 1) - i;
		      //  System.out.println("*****************************************************************************"+desc);
		      //  System.out.println("MESSAGE " + (desc) + ":");
		        Message msg =  messages[desc];
		      //  msg.setFlag(Flags.Flag.SEEN, false);
		        //System.out.println("To get the message flag"+ msg.getFlags());
		         subject=msg.getSubject();  
		         System.out.println("Subject: " + subject);
		         System.out.println("From: " + msg.getFrom());
		         System.out.println("To: "+msg.getAllRecipients());
		          System.out.println("Date: "+msg.getReceivedDate());
		          
		         if(subject.contains("Congratulations! You have successfully registered"))
		         {
		        	 System.out.println("you are inside the email body");
		        //	 msg.setFlag(Flags.Flag.SEEN, false);
		        	 if (msg.isMimeType("text/plain")) {
		        		 email_content = msg.getContent().toString();
		        		 //System.out.println("email content when the type is text and plain--->"+email_content );
		        	    } else if (msg.isMimeType("multipart/*")) {
		        	        MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
		        	        int count=mimeMultipart.getCount();
		        	      System.out.println("you are inside the mime multipart"+count);
		        	        for (int j = 0; j < count; j ++){
		        	            BodyPart bodyPart = mimeMultipart.getBodyPart(j);
		        	          //  System.out.println("content of bodypart is" + bodyPart.getContentType());
		        	            if (bodyPart.isMimeType("text/plain")){
		        	            	email_content = email_content + "\n" + bodyPart.getContent();
		        	            	 //System.out.println("email content when the type is multipart and bodypart is text/plain--->"+email_content );
		        	                break;  //without break same text appears twice in my tests
		        	            } else if (bodyPart.isMimeType("multipart/ALTERNATIVE")){
		        	            	  System.out.println("you are inside the  multipart/alternative");
		        	            	MimeMultipart mimebodyMultipart = (MimeMultipart) bodyPart.getContent();
		        	            	
		        	            	 int count_bodypart=mimebodyMultipart.getCount();
		        	            	 for (int k=0;k<count_bodypart;k++)
		        	            	 {
		        	            		 BodyPart multibodyPart = mimebodyMultipart.getBodyPart(k);
		        	            		// System.out.println(k + multibodyPart.getContentType());
		        	            		 if(!multibodyPart.getContentType().contains("TEXT/HTML"))
		        	            		 {
		        	            			 continue;
		        	            		 }
		        	            		 String html = (String) multibodyPart.getContent();
		        	            		//System.out.println("html for the email is"+ html);
		        	            		 Document doc=Jsoup.parse(html,"UTF-8");
		        	            		 for(Element table: doc.getElementsByClass("table-column button-link"))
		        	            		 {
		        	            			//System.out.println("You are inside the for loop for table element");
		        	            			 Elements links = table.getElementsByTag("a");
		        	            			
		        	            			 for(Element link :links)
		        	            			 {
		        	            				  url = link.attr("href");
		        	            				 System.out.println(url); 
		        	            				driver.get(url);
		        	            				break;
		        	            				
		        	                        }
		        	            			// msg.setFlag(Flags.Flag.SEEN, true);
		        	            			 
		        	            		 }
		        	            		 
				        	       
		        	            		 break;
		        	            	 }
		        	            }
		        	            break;
		        	    }
		        	 
		         }
		        
		        }
		         else if (subject.contains("Congratulations! You have successfully registered "))
		         {
		        	 if (msg.isMimeType("TEXT/PLAIN")) {
		        		 email_content = msg.getContent().toString();
		        		 System.out.println("email content when the type is text and plain--->"+email_content );
		        	    } else if (msg.isMimeType("multipart/*")) {
		        	        MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
		        	        int count=mimeMultipart.getCount();
		        	      //  System.out.println("count of mimemultipart is ---->"+count);
		        	        for (int j = 0; j < count; j ++){
		        	            BodyPart bodyPart = mimeMultipart.getBodyPart(j);
		        	            System.out.println("content of bodypart is" + bodyPart.getContentType());
		        	            if (bodyPart.isMimeType("text/plain")){
		        	            	email_content = email_content + "\n" + bodyPart.getContent();
		        	            	 System.out.println("email content when the type is multipart and bodypart is text/plain--->"+email_content );
		        	                break;  //without break same text appears twice in my tests
		        	            } else if (bodyPart.isMimeType("multipart/alternative")){
		        	            	MimeMultipart mimebodyMultipart = (MimeMultipart) bodyPart.getContent();
		        	            	
		        	            	 int count_bodypart=mimebodyMultipart.getCount();
		        	            	 for (int k=0;k<count_bodypart;k++)
		        	            	 {
		        	            		 BodyPart multibodyPart = mimebodyMultipart.getBodyPart(k);
		        	            		 System.out.println(k + multibodyPart.getContentType());
		        	            		 if(!multibodyPart.getContentType().contains("text/html"))
		        	            		 {
		        	            			 continue;
		        	            		 }
		        	            		 String html = (String) multibodyPart.getContent();
		        	            		 Document doc=Jsoup.parse(html,"UTF-8");
		        	            		 for(Element table: doc.getElementsByClass("table-column button-link"))
		        	            		 {
		        	            			// System.out.println("You are inside the for loop for table element");
		        	            			 Elements links = table.getElementsByTag("a");
		        	            			
		        	            			 for(Element link :links)
		        	            			 {
		        	            				  url = link.attr("href");
		        	            				 System.out.println(url); 
		        	            				driver.get(url);
		        	            				
		        	                        }
		        	            		 }
		        	            		 
				        	       
		        	            		 
		        	            	 }
		        	            }
		        	       
		        	    }
		        	 
		         }
		         }
		     else {
		        	 System.out.println("There is no email found");
		         }
		      break;
		        }
		        }
		      
		      catch (NoSuchProviderException e) {
		         e.printStackTrace();
		      } catch (MessagingException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		      finally
		      {
		    	  if (emailFolder != null && emailFolder.isOpen()) {
		    		  emailFolder.close(true);
		    		  }
		          if (store != null) { 
		        	  store.close(); 
		        	  }
		      }
		  
	 }
		   
		   

	public void click_es(String locator) {
		String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
		driver.findElement(By.linkText(locator)).sendKeys(selectLinkOpeninNewTab);// changed partial link to link text
	}

	public int elementCount(String locator) {
		int count = driver.findElements(By.xpath(OR.getProperty(locator))).size();
		return count;

	}
	
	public List<WebElement> fetchElements(String locator) {
		List <WebElement> value = driver.findElements(By.xpath(OR.getProperty(locator)));
		return value;

	}

	public void selectCheckboxAndRadioButon(String locator, String value) {
		List<WebElement> webelement = driver.findElements(By.xpath(OR.getProperty(locator)));

		boolean isSelected = false;
		for (WebElement e : webelement) {
			isSelected = webelement.get(0).isSelected();
			if (isSelected == true && value.equalsIgnoreCase("Yes") && value.isEmpty() == false) {
				System.out.println("do nothing, the checkbox/radiobutton is already selected");
			} else if (isSelected == false && value.equalsIgnoreCase("Yes") && value.isEmpty() == false) {
				webelement.get(0).click();
				System.out.println("checkbox/radio button is selected successfully");
			} else if (isSelected == true && value.equalsIgnoreCase("No") && value.isEmpty() == false) {
				webelement.get(0).click();
				System.out.println("checkbox/radio button is deselected successfully");
			} else if (isSelected == false && value.equalsIgnoreCase("No") && value.isEmpty() == false) {
				System.out.println("do nothing, the checkbox/radiobutton is 'No' in user input option");
			} else {
				System.out.println("There is no value for the the checkbox/radiobutton");
			}
		}
	}

	public void browserBack() {
		// takes the browser one step back
		driver.navigate().back();
	}

	public void display_all_errors(String locator) {

		List<WebElement> error_mess = driver.findElements(By.xpath(OR.getProperty(locator)));
		if (!error_mess.isEmpty()) {
			System.out.println("The error messages are");
			for (WebElement element : error_mess) {

				System.out.println(element.getText());
				log.debug(element.getText());

			}
		}

	}

	public void type(String locator, String value) {

		// condition put to manage the stale element exception
		boolean staleElement = true;
		while (staleElement) {
			if (locator.endsWith("_XPATH")) {
				try {
					driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
					staleElement = false;
				} catch (StaleElementReferenceException e) {
					staleElement = true;
				}

			} else if (locator.endsWith("_CSS")) {
				try {
					driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
					staleElement = false;
				} catch (StaleElementReferenceException e) {
					staleElement = true;
				}
			}
			if (locator.endsWith("_ID")) {
				try {
					driver.findElement(By.id(OR.getProperty(locator))).sendKeys(value);
					staleElement = false;
				} catch (StaleElementReferenceException e) {
					staleElement = true;
				}
			}

		}
		log.debug("Typing in an Element : " + locator + " entered value as " + value);

	}

	// checks for whether the element is present and is displayed to the user
	public boolean isElementPresent(String locator) {

		try {
			if (locator.endsWith("_XPATH")) {
				driver.findElement(By.xpath(OR.getProperty(locator))).isDisplayed();
			} else if (locator.endsWith("_CSS")) {
				driver.findElement(By.cssSelector(OR.getProperty(locator))).isDisplayed();

			}
			if (locator.endsWith("_ID")) {
				driver.findElement(By.id(OR.getProperty(locator))).isDisplayed();

			}

		} catch (Throwable t) {

			log.debug("Element not found : " + locator + t.getMessage());

			return false;

		}

		log.debug("Validating Presence of an Element : " + locator);
		return true;

	}

	public boolean isFileDownloaded(String downloadPath, String fileName, int row_count) throws Exception {
		
		boolean flag = false;
		int rows;
		File dir = new File(downloadPath);
		TimeUnit.SECONDS.sleep(10);
		
		File[] dir_content=dir.listFiles(new FilenameFilter(){
			@Override
			 public boolean accept(File dir, String fileName) {
		        return fileName.startsWith("CustomerRecord");
		    }
	        });
	
		try {
			if(dir_content==null || dir_content.length==0) {
				System.out.println("Matching file not found");
				return flag;
			}
			 File lastModifiedFile = dir_content[0];
			 for (int i = 1; i < dir_content.length; i++) {
				    if (lastModifiedFile.lastModified() < dir_content[i].lastModified())
					{
				    	 lastModifiedFile = dir_content[i];
				    	 continue;
					}
					POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(lastModifiedFile));
						HSSFWorkbook wb = new HSSFWorkbook(fs);
						 HSSFSheet sheet = wb.getSheetAt(0);
						System.out.println("file is downloaded successfully");
						rows=sheet.getPhysicalNumberOfRows();
						rows=rows-1;
						//System.out.println("row count in the file"+"  "+ lastModifiedFile.getName() +" "+ rows);
					//	System.out.println("row count on the screen"+"  "+ row_count);
						if(rows>0 && rows==row_count)
						{
						System.out.println("the file records match the screen record");
						return flag = true;
						}
					}
					
		
			return flag;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}

	public void uplod_image(String locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			if (locator.endsWith("_XPATH")) {
				WebElement element = driver.findElement(By.xpath(OR.getProperty(locator)));
				js.executeScript("arguments[0].click()", element);

			} else if (locator.endsWith("_CSS")) {
				WebElement element = driver.findElement(By.cssSelector(OR.getProperty(locator)));
				js.executeScript("arguments[0].click()", element);

			}
			if (locator.endsWith("_ID")) {
				WebElement element = driver.findElement(By.id(OR.getProperty(locator)));
				js.executeScript("arguments[0].click()", element);

			}
		} catch (Throwable t) {

			log.debug("Element not found : " + locator + t.getMessage());

		}

	}

	public void sendFocus(String locator, String value) {
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.xpath(OR.getProperty(locator)))).click().sendKeys(value).perform();
		Config.getProperty("explicit.wait");

	}

	public void scroll_window(String locator) {
		// scrolls till the locator is visible
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement element;
		try {
			if (locator.endsWith("_XPATH")) {
				element = driver.findElement(By.xpath(OR.getProperty(locator)));
				js.executeScript("arguments[0].scrollIntoView(true)", element);

			} else if (locator.endsWith("_CSS")) {
				element = driver.findElement(By.cssSelector(OR.getProperty(locator)));
				js.executeScript("arguments[0].scrollIntoView(true);", element);

			} else if (locator.endsWith("_ID")) {
				element = driver.findElement(By.id(OR.getProperty(locator)));
				js.executeScript("arguments[0].scrollIntoView(true);", element);

			}

		} catch (Throwable t) {

			log.debug("Element not found : " + locator + t.getMessage());
			System.out.println("Element not found : " + locator + t.getMessage());

		}

	}

	public void switch_to_window() {
		String parentWindow = driver.getWindowHandle();
		String subwindowhandler=null;
		for (String handle : driver.getWindowHandles()) {
			// switch to child window
			System.out.println("You are inside window handle function");
			System.out.println("handle is"+handle);
			driver.switchTo().window(handle);
		}
		driver.switchTo().window(parentWindow);
	}
	
	public void switch_window()
	{
		driver.switchTo().activeElement();
	}
	public boolean checkBrowserTabTitle(String Expected_Title, String locator) {
		boolean title_match=false;
		List<String> browserTabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(browserTabs .get(1));
		String Actual_Title=driver.findElement(By.xpath(OR.getProperty(locator))).getText();
		if(Actual_Title.equalsIgnoreCase(Expected_Title))
		{
			System.out.println("You are in correct page and title is" + Expected_Title );
			driver.switchTo().window(browserTabs.get(0));
			title_match=true;
		
			
		}
		else {
			System.out.println("You are in incorrect tab");
			title_match=false;
		
		}
		return title_match;
	}

	public void verify_Text(String locator, String Text_to_verify) {
		try {
			if (locator.endsWith("_XPATH")) {

				String actual_text = driver.findElement(By.xpath(OR.getProperty(locator))).getText();
				if (Text_to_verify.equals(actual_text)) {
					System.out.println("Text verification success");
				
				} else {
					System.out.println("Text verification failed");
					
				}
				

			} else if (locator.endsWith("_CSS")) {
				String actual_text = driver.findElement(By.cssSelector(OR.getProperty(locator))).getText();
				if (Text_to_verify.equals(actual_text)) {
					System.out.println("Text verification success");
				} else {
					System.out.println("Text verification failed");
				}

			}
			if (locator.endsWith("_ID")) {
				String actual_text = driver.findElement(By.id(OR.getProperty(locator))).getText();
				if (Text_to_verify.equals(actual_text)) {
					System.out.println("Text verification success");
				} else {
					System.out.println("Text verification failed");
				}
			}
			if (locator.endsWith("_TEXT")) {

				String actual_text = driver.findElement(By.linkText(OR.getProperty(locator))).getText();
				if (Text_to_verify.equals(actual_text)) {
					System.out.println("Text verification success");
				} else {
					System.out.println("Text verification failed");
				}

			}
		} catch (Throwable t) {

			log.debug("Element not found : " + locator + t.getMessage());

		}
	}

	public void clear(String locator)

	{

		try {
			if (locator.endsWith("_XPATH")) {
				driver.findElement(By.xpath(OR.getProperty(locator))).clear();

			} else if (locator.endsWith("_CSS")) {
				driver.findElement(By.cssSelector(OR.getProperty(locator))).clear();

			} else if (locator.endsWith("_ID")) {
				driver.findElement(By.id(OR.getProperty(locator))).clear();

			}

		} catch (Throwable t) {

			log.debug("Element not cleared : " + locator + t.getMessage());

		}

		log.debug("Clearing  Element : " + locator);

	}

	public void rerorderCategories(String FromElement, String ToElement) {

		WebElement from = driver.findElement(By.xpath(OR.getProperty(FromElement)));
		WebElement To = driver.findElement(By.xpath(OR.getProperty(ToElement)));
		new Actions(driver).dragAndDrop(from, To).build().perform();

	}

	public String Element_text(String locator) {

		return (driver.findElement(By.xpath(OR.getProperty(locator))).getText());

	}

	public void accept_alert() {
		driver.switchTo().alert().accept();
	}

	public void dismiss_alert() {
		driver.switchTo().alert().dismiss();
	}
public void clickclear(String locator)
{
	try {
		if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).click();
		}
			

		else if (locator.endsWith("_CSS")) {
			
			driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
		}
		if (locator.endsWith("_ID")) {
			driver.findElement(By.id(OR.getProperty(locator))).click();
		}
		
	} catch (Throwable t) {

		log.debug("Element not found : " + locator + t.getMessage());

	}
}
	public void searchQueryResults(String locator, String value, int table_column)
	{
		
		List<WebElement> rows=driver.findElements(By.xpath(OR.getProperty(locator)));
		System.out.println("There are"+ rows.size()+"records matching the search criteria");
		for(int i=0;i<rows.size();i++)
		{	
			List<WebElement> cols=rows.get(i).findElements(By.tagName("td"));
			String value_case_insensitive=value.toLowerCase();
			if (cols.get(table_column).getText().toLowerCase().contains((value_case_insensitive))) {
				System.out.println("Row" +(i+1)+"---->"+"    "+cols.get(table_column).getAttribute("data-title")+ " matches the user value   "+value);
				
				
		//	System.out.println("Length of the columns is"+ cols.size());
			/*for(WebElement column: cols)
			{
				String[] splitString = value.split(",");
				//System.out.println("Length of split string"+ splitString.length);
				if(splitString.length>=1)
				{
					for (int j = 0; j < splitString.length; j++) {
				if(column.get(i).getText().equalsIgnoreCase(splitString[j]))
				{
					System.out.println("For row number--->" + (i));
					System.out.println(column.getAttribute("data-title")+ " matches the user value   "+splitString[j]);
					
				}
				}
			}
			
			}*/
		}
			}
		}
		
	
	
	public void changeVariantsValue(String locator, String value)
	{
		List<WebElement> webelement = driver.findElements(By.xpath(OR.getProperty(locator)));

		Iterator<WebElement> i = webelement.iterator();
		
		while(i.hasNext())
		{
			
		}
	}
	public String getAttributeValue(String locator, String attribute) {

		boolean staleElement = true;
		String attributeValue="false";
		while (staleElement) {
			if (locator.endsWith("_XPATH")) {
				try {

					staleElement = false;
					attributeValue = driver.findElement(By.xpath(OR.getProperty(locator))).getAttribute(attribute);
					if(attributeValue==null)
					{
						attributeValue="false";
					}
					return attributeValue;
				} catch (StaleElementReferenceException | NullPointerException e1) {
					staleElement = true;

				}
				return attributeValue;
			} else if (locator.endsWith("_CSS")) {
				try {
					attributeValue=driver.findElement(By.cssSelector(OR.getProperty(locator))).getAttribute(attribute);
					if(attributeValue==null)
					{
						attributeValue="false";
					}
					staleElement = false;
					return attributeValue;
				} catch (StaleElementReferenceException | NullPointerException e1) {
					staleElement = true;

				}
				return attributeValue;
			}
			if (locator.endsWith("_ID")) {
				try {
					attributeValue=driver.findElement(By.id(OR.getProperty(locator))).getAttribute(attribute);
					if(attributeValue==null)
					{
						attributeValue="false";
					}
					staleElement = false;
					System.out.println(attributeValue);
					return attributeValue;
				} catch (StaleElementReferenceException | NullPointerException e1) {
					staleElement = true;

				}
				return attributeValue;
			}
			if (locator.endsWith("_TEXT")) {
				try {
					driver.findElement(By.linkText(OR.getProperty(locator))).getAttribute(attribute);
					if(attributeValue==null)
					{
						attributeValue="false";
					}
					staleElement = false;
					return attributeValue;
				} catch (StaleElementReferenceException | NullPointerException e1) {
					staleElement = true;

				}
				return attributeValue;
			}

		}
		return attributeValue;
	}

	public boolean checkWebElementValueAlreadyExists(String locator, String value) {
		List<WebElement> webelement = driver.findElements(By.xpath(OR.getProperty(locator)));

		Iterator<WebElement> i = webelement.iterator();
		boolean matchFound=false;
		while (i.hasNext()) {
			WebElement element = i.next();
			if (element.getText().contains(value)) {
				System.out.println("Matching webelement is found");
				element.click();
				matchFound=true;
				log.debug("Matching webelement is found");
				return matchFound;
			}
			
		}
		if (!matchFound) {
		    System.out.println("No value found for the webelement");
		}
		return matchFound;
	}
	
	
	
	 public void checkVariantsExist(String variant_row,String value, String variant_select, String variant_price1, String variant_saleprice1,String variant_sku1,String variant_barcode1,String variant_stock_qty_1) {
			List<WebElement> rowelement = driver.findElements(By.xpath(OR.getProperty(variant_row)));
			
			boolean matchFound=false;
			//System.out.println("size of row is " + rowelement.size());
			for(int row=0;row<rowelement.size();row++)
			{
				
				List<WebElement> colelement =  rowelement.get(row).findElements(By.tagName("td"));
				
//				//System.out.println("size of column is " + colelement.size());
			//Iterator<WebElement> i = colelement.iterator();
		//	while (i.hasNext()) 
				for(int col=0;col<colelement.size();col++)
				{
				
					WebElement element =colelement.get(col);
					//System.out.println(element.getText());
					
					// What column element I have
					
					//System.out.println(element.findElement(By.tagName("input")).getAttribute("checked"));
					if(col==0)
					{
					String variant_checked=element.findElement(By.tagName("input")).getAttribute("checked");
					if(variant_checked.equalsIgnoreCase("true") && StringUtils.isBlank(variant_select)==false && (variant_select.equals("no")))
					{
						new Actions(driver).moveToElement(element.findElement(By.tagName("input"))).click().build().perform();
						
						//click(element.findElement(By.tagName("input")).toString());
					}
					}
					if(col==1)
					{
						String variant_value=element.getText();
						if(variant_value.equalsIgnoreCase(value))
						{
							System.out.println("Matching variant  found");
							matchFound=true;
						}
						else
						{
							matchFound=false;
						}
					}
					if(col==2 && matchFound==true )
					{
					String variant_price_id=element.findElement(By.tagName("input")).getAttribute("id");
					
			
						if(variant_price_id.equalsIgnoreCase("variantPrice") && StringUtils.isBlank(variant_price1)==false )
						{
							
							Config.getProperty("explicit.wait");
							new Actions(driver).moveToElement(element.findElement(By.tagName("input"))).click();
							element.findElement(By.tagName("input")).clear();
							
							element.findElement(By.tagName("input")).sendKeys(variant_price1);
						//element.clear();
							//type("variant_price_XPATH",variant_price1);
						}
						//System.out.println(element.findElement(By.tagName("input")).getAttribute("value"));
					}
						if( col==3 &&  matchFound==true)
						{
							String variant_sale_price=element.findElement(By.tagName("input")).getAttribute("id");
							if(variant_sale_price.equals("variantSalePrice") && StringUtils.isBlank(variant_saleprice1)==false)
							{
								element.findElement(By.tagName("input")).clear();
								element.findElement(By.tagName("input")).sendKeys(variant_saleprice1);
							}
						}
						if(col==4  && matchFound==true)
						{
							String variant_sku=element.findElement(By.tagName("input")).getAttribute("id");
							if(variant_sku.equalsIgnoreCase("variantSku") && StringUtils.isBlank(variant_sku1)==false)
							{
								element.findElement(By.tagName("input")).clear();
								element.findElement(By.tagName("input")).sendKeys(variant_sku1);
							}
						}
						if(col==5  && matchFound==true)
						{
							String variant_barcode=element.findElement(By.tagName("input")).getAttribute("id");
							if(variant_barcode.equalsIgnoreCase("variantBarcode") && StringUtils.isBlank(variant_barcode1)==false)
							{
								element.findElement(By.tagName("input")).clear();
								element.findElement(By.tagName("input")).sendKeys(variant_barcode1);
							}
						}
						if(col==6  && matchFound==true)
						{
							String variant_stock_qty=element.findElement(By.tagName("input")).getAttribute("class");
							if(variant_stock_qty.equalsIgnoreCase("variantStockQuantity") && StringUtils.isBlank(variant_stock_qty_1)==false )
							{
								if(element.findElement(By.tagName("input")).isDisplayed()==true)
								{
								element.findElement(By.tagName("input")).clear();
								element.findElement(By.tagName("input")).sendKeys(variant_stock_qty_1);
								}
							}
						}
					
				}
			
					
			}
					log.debug("Matching webelement is found");
				//	return matchFound;
				
				
				
			
			if (!matchFound) {
			    System.out.println("No value found for the webelement");
			}
		//	return matchFound;
			}
		
	
	public void removeExistingVariants(String locator) {
		boolean staleElement=true ;
		while (staleElement) {
			if (locator.endsWith("_XPATH")) {
				List<WebElement> webelement = driver.findElements(By.xpath(OR.getProperty(locator)));
				Iterator<WebElement> i = webelement.iterator();
				while (i.hasNext()) {
					WebElement element = i.next();
					System.out.println("size of webelement is"+element );
				try {
						
					//	element.click();
						//System.out.println("element size is" + webelement.size());
						new Actions(driver).moveToElement(element).click().build().perform();	
						//Config.getProperty("explicit.wait");
						System.out.println("Varaint Value has been successfully removed");
						staleElement=false;
						
					}
				
				catch (StaleElementReferenceException e) {
					List<WebElement> webelement1 = driver.findElements(By.xpath(OR.getProperty(locator)));
					i=webelement1.iterator();
					//System.out.println("size of webelement is"+element );
					//staleElement=true;
					}
				
			
				//System.out.println("size of element is"+ webelement.size());
			
						}
			}
			break;
			
				}
		
	}
public void hoverOverMenu(String locator) {
	Actions builder = new Actions(driver);
	WebElement hoverElement=driver.findElement(By.xpath(OR.getProperty(locator)));
	builder.moveToElement(hoverElement).perform();
}
public void switchToIFrame(String locator)
{
	
	driver.switchTo().frame(driver.findElement(By.xpath(OR.getProperty(locator))));
}


	public void pickFirstSuggestion(String locator) {
		List<WebElement> webelement = driver.findElements(By.xpath(OR.getProperty(locator)));

		Iterator<WebElement> i = webelement.iterator();

		while (i.hasNext()) {
			WebElement element = i.next();
			System.out.println("First url is selected from the suggested url's " + element.getText());
			log.debug("First url is selected");
			// selects the first url suggestion
			element.click();
			break;
		}
	}

	public WebElement getNextElementByName(String locator, String elementName) {
		List<WebElement> webelement = driver.findElements(By.xpath(OR.getProperty(locator)));
		Iterator<WebElement> i = webelement.iterator();
		boolean checkForElement = false;
		
		while (i.hasNext()) {
		{
			
			WebElement element = i.next();
			System.out.println(element.getText());
			
					if (element.getText().startsWith(elementName)) {
					element.click();
						return element;
					}
				
			}
			
			
		}
		
		System.out.println("No matching value found for the category to perform the actions");

		return null;

	}
	
public Boolean checkUserActionExists(String locator, String elementName)  {
	boolean user_action=false;

		List<WebElement> webelement = driver.findElements(By.xpath(OR.getProperty(locator)));
		Iterator<WebElement> i = webelement.iterator(); 
		
		while (i.hasNext()) 
		{
			
		 WebElement element = i.next();
		
		if(element.getAttribute("title").equals(elementName))
		{
			user_action=true;
			System.out.println("matching user input action found");
			return user_action;
			
		}
		
	
	}
		return user_action;
		
	
	
}
public void checkCategoryUserActionExists(String locator,String elementName){
	boolean user_action;
	List<WebElement> webelement = driver.findElements(By.xpath(OR.getProperty(locator)));
	Iterator<WebElement> i = webelement.iterator(); 

	while (i.hasNext()) 
	{
		
	 WebElement element = i.next();
	 
	
	if(element.getText().equalsIgnoreCase(elementName))
	{
		user_action=true;
	//	List<WebElement> actions=
	 
	}
	
}
}
		

	public boolean checkCategoryAlreadyExists(String locator, String value) {
		List<WebElement> webelement = driver.findElements(By.xpath(OR.getProperty(locator)));
		//System.out.println(x);
		Iterator<WebElement> i = webelement.iterator();

		while (i.hasNext()) {
			WebElement element = i.next();
		/*	String edit=element.findElement(By.tagName("span")).getAttribute("class");
			if(edit.equalsIgnoreCase("fa fa-pencil-square-o"))
			{
				element.click();
			}*/
			String element_with_special_character = element.getText();
			// this will replace all the +- charcters in categories
			String element_without_special_character = element_with_special_character.replaceAll("[-+.^:]", "");
			String[] splitString = value.split(",");
			if (element_without_special_character.equalsIgnoreCase(value) && value.indexOf(',') == -1) {
				System.out.println("Match found");

				log.debug("Match found");
				return true;

			} else if( splitString.length >1){
				for (String test : splitString) {
					if(test.equalsIgnoreCase(element_without_special_character))
					{
					for (int j = 0; j < splitString.length; j++) {
						if (test.equalsIgnoreCase(element_without_special_character)) {
							System.out.println("Match found for multiple category string");

							log.debug("Match found");
						

						}
					
					}
					return true;
					}
				
				}
			
			}
		
		}
		System.out.println("Match not found for  category string");
		return false;

	}

	public boolean Element_selected(String locator)

	{
		try {
			if (locator.endsWith("_XPATH")) {
				log.debug("Element selected True : " + locator);
				return driver.findElement(By.xpath(OR.getProperty(locator))).isSelected();

			} else if (locator.endsWith("_CSS")) {
				log.debug("Element selected True : " + locator);
				return driver.findElement(By.cssSelector(OR.getProperty(locator))).isSelected();

			}
			if (locator.endsWith("_ID")) {
				log.debug("Element selected True : " + locator);
				return driver.findElement(By.id(OR.getProperty(locator))).isSelected();

			}

		} catch (Throwable t) {

			log.debug("Element not selected : " + locator + t.getMessage());
			return false;

		}

		return false;

	}

	public void select(String locator, String value) {

		if (locator.endsWith("_XPATH")) {
			dropdown = driver.findElement(By.xpath(OR.getProperty(locator)));
		} else if (locator.endsWith("_CSS")) {
			dropdown = driver.findElement(By.cssSelector(OR.getProperty(locator)));
		}
		if (locator.endsWith("_ID")) {
			dropdown = driver.findElement(By.id(OR.getProperty(locator)));
		}

		if (locator.endsWith("_CLASS")) {
			dropdown = driver.findElement(By.className(OR.getProperty(locator)));
		}

		Select select = new Select(dropdown);
	if(dropdown.isSelected())
	{
		select.deselectAll();
	}
	
		List<WebElement> options = select.getAllSelectedOptions();
		String selectValue = value;
		
		for (WebElement option : options) {
			if (option.getText().contains(value)) {
				selectValue = option.getText();
				break;
			}
			if(value.isEmpty())
			{
				
			}
			
		}
		select.selectByVisibleText(selectValue);

		log.debug("Selected Element : " + locator + " selected value as " + selectValue);
	}

	// added method for multi-select dropdown box
	public static void selectMultipelValues(String locator, String multipleVals) {
		// Actions builder = new Actions(driver);
		if (locator.endsWith("_XPATH")) {
			dropdown = driver.findElement(By.xpath(OR.getProperty(locator)));
		} else if (locator.endsWith("_CSS")) {
			dropdown = driver.findElement(By.cssSelector(OR.getProperty(locator)));
		}
		if (locator.endsWith("_ID")) {
			dropdown = driver.findElement(By.id(OR.getProperty(locator)));
		}
		Select select_multiple = new Select(dropdown);
	//	if(dropdown.isSelected())
	//	{
			select_multiple.deselectAll();
	//	}
		String multipleSel[] = multipleVals.split(",");
		for (String valueToBeSelected : multipleSel) {
			List<WebElement> options = select_multiple.getOptions();
			String selectValue = valueToBeSelected;
			for (WebElement option : options) {
				if (option.getText().contains(valueToBeSelected)) {
					selectValue = option.getText();
					select_multiple.selectByVisibleText(selectValue);
					
				}
				else {
					continue;
				}
				break;
			// builder.keyDown(Keys.COMMAND);
		}
		}
		// builder.keyUp(Keys.COMMAND).build().perform();
	}

	@AfterSuite
	public void tearDown() {

		// driver.quit();
		log.debug("Test execution Completed !!!");

	}

}
