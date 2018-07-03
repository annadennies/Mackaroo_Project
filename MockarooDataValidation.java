package com.mockaroo;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MockarooDataValidation {

	WebDriver driver;
	String data_city = "city";
	String data_country = "country";

	@BeforeClass
	public void setUp() {
		System.out.println("Setting up WebDriver in BeforeClass");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().fullscreen();
		driver.get("https://mockaroo.com/");
	}

	@Test
	public void titleIsCorrect() {
		Assert.assertEquals(driver.findElement(By.xpath("//a[@href='/']/div[@class='brand']")).getText(), "mockaroo");
		Assert.assertEquals(driver.findElement(By.xpath("//a[@href='/']/div[@class='tagline']")).getText(),
				"realistic data generator");
	}

	@Test
	public void IConClick() {

		List<WebElement> booksWebElementList = driver
				.findElements(By.xpath("//a[@class='close remove-field remove_nested_fields']"));

		for (WebElement eachIcon : booksWebElementList) {
			eachIcon.click();
		}
	}

	@Test
	public void formatISOkey() {

		Assert.assertTrue(
				driver.findElement(By.xpath("//div[@class='column column-header column-name']")).isDisplayed());
		Assert.assertTrue(
				driver.findElement(By.xpath("//div[@class='column column-header column-type']")).isDisplayed());
		Assert.assertTrue(
				driver.findElement(By.xpath("//div[@class='column column-header column-options']")).isDisplayed());
		Assert.assertTrue(
				driver.findElement(By.xpath("//a[@href='javascript:void(0)'][.='Add another field']")).isEnabled());
		Assert.assertEquals(driver.findElement(By.id("num_rows")).getAttribute("value"), "1000");
		Assert.assertEquals(driver.findElement(By.id("schema_file_format")).getAttribute("value"), "csv");
		Assert.assertEquals(
				driver.findElement(By.xpath("//*[@id='schema_line_ending']/option[@value='unix']")).getText(),
				"Unix (LF)");
		Assert.assertTrue(driver.findElement(By.id("schema_include_header")).isSelected());
		Assert.assertFalse(driver.findElement(By.id("schema_bom")).isSelected());

	}

	@Test
	public void createFile() throws InterruptedException {
		createData(data_country);
		Thread.sleep(3000);
		createData(data_city);
		WebElement download = driver.findElement(By.id("download"));
		Thread.sleep(1000);
		download.click();

	}

	public void createData(String str) throws InterruptedException {
		driver.findElement(By.xpath("//a[text()='Add another field']")).click();
		Thread.sleep(1000);
		WebElement fieldName = driver
				.findElement(By.xpath("//div[@class='fields'][7]/div/input[@placeholder='enter name...']"));
		Thread.sleep(1000);
		fieldName.clear();
		Thread.sleep(1000);
		fieldName.sendKeys(str);
		Thread.sleep(1000);
		Assert.assertTrue(
				driver.findElement(By.xpath("//div[@class='fields'][7]/div/input[@placeholder='choose type...']"))
						.isDisplayed());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='fields'][7]/div/input[@placeholder='choose type...']")).click();
		Thread.sleep(1000);
		WebElement findButton = driver.findElement(By.id("type_search_field"));
		Thread.sleep(1000);
		findButton.click();
		Thread.sleep(1000);
		findButton.clear();
		Thread.sleep(1000);
		findButton.sendKeys(str);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id='type_list']/div")).click();

	}

	@Test
	public void workwithData() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/aylin/Downloads/MOCK_DATA.csv"));
		String line = bufferedReader.readLine();
		assertEquals(line, "city,country");

		List<String> lines = new ArrayList();
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		assertEquals(lines.size(), 1000);

		List<String> citiesList = new ArrayList();
		List<String> countriesList = new ArrayList();

		for (String lin : lines) {
			citiesList.add(lin.substring(0, lin.indexOf(",")));
			countriesList.add(lin.substring(lin.indexOf(",") + 1));
		}
		System.out.println("city :" + citiesList.size());
		System.out.println("country :" + countriesList.size());

		// Sort all cities
		Collections.sort(citiesList);
		// find the city with the longest name and shortest name
		System.out.println(findLongestName(citiesList));
		// shortest name
		System.out.println(findShortestName(citiesList));

		// how many times each Country is mentioned.
		Map map = new HashMap<String, Integer>();
		for (String countryname : countriesList) {
			map.put(countryname, Collections.frequency(countriesList, countryname));
		}
		System.out.println(map);
		System.out.println(map.keySet().size());

		// citiesSet HashSet
		Set<String> citiesSetHash = new HashSet<>(citiesList);
		System.out.println(citiesSetHash.size());
	

		// count how many unique cities are in cities ArrayLsit
		Map<String,Integer> citiesMap = new HashMap<>();
		for (String each : citiesList) {
		    
		      if( ! citiesMap.containsKey(each)) {      
		    	  citiesMap.put(each, 1) ; 
		      } else {
		        int exitsingCount = citiesMap.get(each) ; 
		        citiesMap.put(each, exitsingCount+1) ;   
		      }
		    }
		
		Assert.assertEquals(citiesSetHash.size(), citiesMap.size());
	

	//Add all Countries to countrySet HashSet
	Set<String> uniqueCountry = new HashSet<>(countriesList);
	System.out.println(uniqueCountry.size());
	Assert.assertEquals(map.size(), uniqueCountry.size());
	
	}	
	
	
	
	
	@AfterClass
	public void teardown() {
		driver.close();
	}

	public static String findLongestName(List<String> list) {
		String longestName = "";
		for (int i = 0; i < list.size(); i++) {
			for (int j = 1; j < list.size(); j++) {
				if (list.get(i).length() >= list.get(j).length()) {
					longestName = list.get(i);
					continue;
				} else {
					longestName = list.get(j);
					break;
				}
			}
		}
		return longestName;
	}

	public static String findShortestName(List<String> list) {
		String shortestName = "";
		for (int i = 0; i < list.size(); i++) {
			for (int j = 1; j < list.size(); j++) {
				if (list.get(i).length() <= list.get(j).length()) {
					shortestName = list.get(i);
					continue;
				} else {
					shortestName = list.get(j);
					break;
				}
			}
		}

		return shortestName;
	}

}
