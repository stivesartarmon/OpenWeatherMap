package testCases;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Problem2_VerifyTemperatureDifferenceRequest {

@Test(dataProvider="getCityIds")
public void verifyTemperatureDifference(String cityId){
	System.out.println(cityId);
	 String apiKey="b6907d289e10d714a6e88b30761fae22";
	 //Placing API hit	
	Response resp=RestAssured.get("https://samples.openweathermap.org/data/2.5/weather?id="+cityId+"&appid="+apiKey);
//getting response	
	System.out.println(cityId+"-->"+resp.asString());
	
	JsonPath jsonPathEvaluator = resp.jsonPath();
	 
	 String minTemp = jsonPathEvaluator.get("main");
	 
	 System.out.println("Min temp from Response " + minTemp);
}


@DataProvider
  public Object[]  getCityIds() throws FileNotFoundException{
	 String path = System.getProperty("user.dir")+"\\src\\main\\java\\resources\\CityList.json";
	 String[] cityList=new String[]{"Dunnigan","West Greenwich","Guernsey","Hoback","Pinedale"};
	 List<String>  citylDs=new ArrayList<String>();
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
			    	//System.out.println(cityname + "-->"+cityId); 
			    	citylDs.add(cityId);
	    		}
	    	}
	    }
    Object[]  data=new String[citylDs.size()];
	    		
    for (int  j=0;j<citylDs.size();j++)
    {
	   data[j]=  citylDs.get(j);
	    
    }
    return  data;
}
}
