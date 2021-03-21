package test.java.TestCases;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.java.Utilities.TestUtil;

public class ChooseBattleField extends TestBase {

	@Test(dataProvider = "BattleField")

	public void ChooseBattleField(String BattleField){
		
		switch(BattleField)
		{
		case "Game":
			click("are_you_game_XPATH");
			System.out.println("You are inside covid active battlefield");
			break;
		case "Take Bus":
			click("take_bus_XPATH");
			System.out.println("You need to go somewhere important");
			InsideTheBus bus= new InsideTheBus();
			bus.InsideTheBus();
			break;
		case "Public place":
			click("go_to_public_place_ID");
			System.out.println("You are allowed to go out as lockdown rules have been eased out");
			break;
		case "Office":
			click("go_to_office_ID");
			System.out.println("You are required to attend your workplace");
			break;
		default:
			System.out.println("You have not made any selection");
		}
	
}

	
	@DataProvider
	public Object[][] BattleField() {
		return TestUtil.getData("BattleField");
	}
	
	
}
