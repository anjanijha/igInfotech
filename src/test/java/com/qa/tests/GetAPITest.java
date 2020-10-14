package com.qa.tests;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.base.TestBase;
import com.qa.util.TestUtil;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetAPITest extends TestBase{
	TestBase testBase;
	String serviceUrl;
	String apiUrl;
	RequestSpecification httpRequest;
	  Response response;
	  JsonPath jsonPath;
	  String responseBody;
	
	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException{
		testBase = new TestBase();
		serviceUrl = prop.getProperty("URL");
		apiUrl = prop.getProperty("serviceURL");
		
		RestAssured.baseURI= serviceUrl + apiUrl;
		 httpRequest=RestAssured.given();
		  response=httpRequest.request(Method.GET);
		  jsonPath=response.jsonPath();
		 responseBody=response.getBody().asString();
		
	}
	
	
	
	@Test(priority=1)
	public void validateStatusCode() throws ClientProtocolException, IOException{
		 System.out.println("Response Body is:" +responseBody);
		  int statusCode=response.getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		Assert.assertEquals(jsonPath.get("status"),200);
	
}

	

	@Test
	public void validateResponseHeader() throws ClientProtocolException, IOException{
		  
		String contentType = response.header("Content-Type");
		Assert.assertEquals(contentType,"application/json; charset=UTF-8");
		System.out.println("Content-Type value: " + contentType);
}

	@Test
	public void validateResponseBody() throws ClientProtocolException, IOException{
		
		JSONObject responseJson = new JSONObject(responseBody);
		System.out.println("Response JSON from API---> "+ responseJson);
		//get the value from JSON ARRAY:
		String status = TestUtil.getValueByJPath(responseJson, "status");
		String age = TestUtil.getValueByJPath(responseJson, "/employeeData[0]/age");
		String dob = TestUtil.getValueByJPath(responseJson, "/employeeData[0]/dob");
		String role = TestUtil.getValueByJPath(responseJson, "/employeeData[0]/role");
		String message = TestUtil.getValueByJPath(responseJson, "message");
		Assert.assertEquals(status,"200");
		Assert.assertEquals(age,"25");
		Assert.assertEquals(role,"QA Automation Developer");
		Assert.assertEquals(dob,"25-02-1994");
		Assert.assertEquals(message,"data retrieved successful");
}

	@Test
	public void validateCompanyData() throws ClientProtocolException, IOException{
		JSONObject responseJson = new JSONObject(responseBody);
	  
		String company = TestUtil.getValueByJPath(responseJson, "/employeeData[0]/company");
		Assert.assertEquals(company,"ABC Infotech");
		System.out.println(company);
}
}