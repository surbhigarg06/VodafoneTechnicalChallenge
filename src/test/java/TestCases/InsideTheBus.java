package test.java.TestCases;

public class InsideTheBus  extends TestBase{
	
	public void InsideTheBus() {
		//System.out.println("You are inside bus function");
		switch_window();
	
	if(isElementPresent("bus_img_ID"))
	{
		//System.out.println("You are inside pop up");
		System.out.println(Element_text("active_covid_XPATH"));
		
		if(Element_text("active_covid_XPATH").contains("You have taken the public bus"))
		{
			click("start_button_ID");
			if(isElementPresent("bus_question1_ID")){
				click("bus_answer1_ID");
				if(isElementPresent("correct_answer_XPATH")&& Element_text("correct_answer_XPATH").contains("that is correct!"))
				{
					click("try_next_battle_ID");
				}
			}
			if(isElementPresent("contracted_covid_XPATH"))
			{
				Element_text("message_to_user_XPATH");
			}
			if(isElementPresent("time_up_XPATH")) {
				Element_text("message_to_user_XPATH");
				click("try_again_XPATH");
			}
			
		}
		
	}
	}

}
