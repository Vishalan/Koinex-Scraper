package com.vishalan.koinex.firebase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KoinexScrapper {
	private static Firebase firebase = null;
	private static final String baseUrl = "https://koinex.in/exchange/ripple";
	private static final String btcInrText = "BTC/INR";
	private static final String xrpInrText = "XRP/INR";
	private static final String ethInrText = "ETH/INR";
	private static final String ltcInrText = "LTC/INR";
	private static final String bchInrText = "BCH/INR";
	static WebDriver driver = null;
	public static void main(String[] args) {
		firebase = new Firebase();
		
		System.setProperty("webdriver.gecko.driver","D:\\Study\\Koinex\\GeckoDriver\\geckodriver.exe");
		driver = new FirefoxDriver();
        driver.get(baseUrl);

//        ExpectedCondition<Boolean> expectation = new
//                ExpectedCondition<Boolean>() {
//                    public Boolean apply(WebDriver driver) {
//                        return driver.findElements(By.className("coin")).size()>4;
//                    }
//                };
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Reached");
        List<WebElement> coinPrices = driver.findElements(By.className("coin"));
        for(WebElement we: coinPrices)
        {
        	System.out.println(we.getText());
        }
//        WebDriverWait wait = new WebDriverWait(driver, 30);
//        wait.until(expectation);
        while(true)
        {
        	try {
    			Thread.sleep(1000);
    			coinPrices = driver.findElements(By.className("coin"));
    			if(coinPrices.size()>7)
    			{
    				Rates rate = new Rates();
    				for(WebElement we: coinPrices)
        	        {
    					String text = we.getText();
    					int indexOfColon = text.indexOf(":");
    					String key = text.substring(0,indexOfColon);
    					String value = text.substring(indexOfColon+1,text.length()).replaceAll(",", "");
    					if(key.equals(btcInrText))
    					{
    						rate.setBtcINR(Double.parseDouble(value));
    					}
    					else if(key.equals(bchInrText))
    					{
    						rate.setBchINR(Double.parseDouble(value));
    					}
    					else if(key.equals(xrpInrText))
    					{
    						rate.setXrpINR(Double.parseDouble(value));
    					}
    					else if(key.equals(ethInrText))
    					{
    						rate.setEthINR(Double.parseDouble(value));
    					}
    					else if(key.equals(ltcInrText))
    					{
    						rate.setLtcINR(Double.parseDouble(value));
    					}
        	        }
    				updateData(rate);
    			}
				else
				{
					Thread.sleep(5000);
				}
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	
        }
			
	}
	
	public static void updateData(Rates rate)
	{
		
		Map<String, Object> rates = new HashMap<String, Object>();
		rates.put("conversions",rate);
		firebase.update(rates, "rates");
	}
	
}
