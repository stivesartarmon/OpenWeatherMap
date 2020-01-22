package testCases;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Problem1_VerifyCityID_APIRequest {
	WebDriver driver;
	String windSpeed;
	String cloudiness;
	String pressure;
	String humidity;

	@Test
	public void verifyAPIRequest() throws FileNotFoundException, InterruptedException{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\main\\java\\resources\\chromedriver.exe");
    	driver=new ChromeDriver();
    	
    //navigate to application url	
    	driver.get("https://openweathermap.org/");
    	
		 String path = System.getProperty("user.dir")+"\\src\\main\\java\\resources\\CityList.json";
		 String apiKey="b6907d289e10d714a6e88b30761fae22";
		 String[] cityList=new String[]{"Dunnigan","West Greenwich","Guernsey","Hoback","Pinedale"};
		    BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

		    Gson gson = new Gson();
		    JsonParser parser = new JsonParser();
		    JsonArray jsarray = parser.parse(bufferedReader).getAsJsonArray();
		    for(int i=0;i<jsarray.size();i++){
		    	JsonObject jsobj=jsarray.get(i).getAsJsonObject();
		    	
		    	JsonElement jsEle=jsobj.get("name");
		    	String cityname=jsEle.getAsString();
		    
		    //Iterating over city list taken above to get CIty Ids	
		    	for(String city:cityList){
		    		if(cityname.equals(city)){
		    			String cityId=jsobj.get("id").toString();
				    	System.out.println(cityname + "-->"+cityId);
				    //Placing API hit	
				    	Response resp=RestAssured.get("https://samples.openweathermap.org/data/2.5/weather?id="+cityId+"&appid="+apiKey);
				    	windSpeed=resp.path("wind.speed").toString();
				    	cloudiness=resp.path("main.temp_max").toString();
				    	pressure=resp.path("main.temp_max").toString();
				    	humidity=resp.path("main.humidity").toString();
				    //getting response	
				    	System.out.println(cityname+"-->"+resp.asString());
				    //Verifying Status Code	
				    	Assert.assertTrue(resp.getStatusCode()==200);
				    //verifying API request time
				    	System.out.println("Time taken for API request ->"+resp.getTime());
				    	
				  /*
				   * Navigate to the weather map portal and compare the api results with UI
				   *   	
				   */
				    	
				    //search for city	
				    	driver.findElement(By.xpath("//div[@class='form-group search-cities__block']/input[@id='q']")).sendKeys(city);
				    //click search button	
				    	driver.findElement(By.xpath("//button[contains(text(),'Search')]")).click();
				    	Thread.sleep(2000);
				    //Click on searched city name		
				    	driver.findElement(By.xpath("//div[@id='forecast-list']//a[contains(text(),'"+city+"')]")).click();
				    	Thread.sleep(3000);
				    	
				    //Verify Wind speed with API Response
				    	WebElement windEle=driver.findElement(By.xpath("//td[@id='weather-widget-wind']"));
				    	Assert.assertTrue(windEle.getText().contains(windSpeed), "Wind Speed verified from API response");
				    	
				    	//Verify Cloudiness with API Response
				    	WebElement cloudEle=driver.findElement(By.xpath("//td[@id='weather-widget-cloudiness']"));
				    	Assert.assertTrue(cloudEle.getText().contains(cloudiness), "Cloudiness verified from API response");
				    	
				    	//Verify pressure with API Response
				    	WebElement pressureEle=driver.findElement(By.xpath("//td[text()='Pressure']/following-sibling::td"));
				    	Assert.assertTrue(pressureEle.getText().contains(pressure), "Pressure verified from API response");
				    
				    	//Verify Humidity with API Response
				    	WebElement humidityEle=driver.findElement(By.xpath("//td[text()='Humidity']/following-sibling::td"));
				    	Assert.assertTrue(humidityEle.getText().contains(humidity), "Humidity verified from API response");
				    
				    	
		    		}
		    	}		    	
		    }
		    
		    driver.quit();
	}
}
