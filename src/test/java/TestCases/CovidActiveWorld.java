package test.java.TestCases;

public class CovidActiveWorld  extends TestBase{
	
	public void CovidActiveWorld() {
	
	if(isElementPresent("active_covid_ID"))
	{
		if(Element_text("active_covid_ID").equalsIgnoreCase("You are in battelefield"))
		{
			click("start_button_ID");
			if(isElementPresent("contracted_covid_XPATH"))
			{
				Element_text("message_to_user_XPATH");
			}
		}
	}
	}

}
