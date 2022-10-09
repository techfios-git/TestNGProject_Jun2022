package vairousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {

	WebDriver driver;
	String browser;
	String url;

	// Element list
	By userNameField = By.xpath("//input[@id='username']");
	By passwordField = By.xpath("//input[@id='password']");
	By signInButtonField = By.xpath("//button[@name='login']");
	By dashboardHeaderField = By.xpath("//h2[contains(text(), 'Dashboard')]");
	By customerMenuField = By.xpath("//*[@id=\"side-menu\"]/li[3]/a/span[1]");
	By addCustomerMenuField = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li[1]/a");
	By addCustomerHeaderField = By.xpath("//h5[text()='Add Contact']");
	By fullNameField = By.xpath("//*[@id=\"account\"]");
	By companyDropdownField = By.xpath("//select[@id='cid']");
	By emailField = By.xpath("//*[@id=\"email\"]");
	By countryDropdownField = By.xpath("//select[@id=\"country\"]");
	
	@BeforeClass
	public void readConfig() {
		//InputStream //BufferedReader //FileReader //Scanner
		
		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("Used Browser: " + browser);
			url = prop.getProperty("url");
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	@BeforeMethod
	public void init() {
		
		if (browser.equalsIgnoreCase("chrome")) {

			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();

		} else if(browser.equalsIgnoreCase("firefox")){

			System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
			driver = new FirefoxDriver();

		}
		
		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	//@Test(priority = 1)
	public void loginTest() {

		driver.findElement(userNameField).sendKeys("demo@techfios.com");
		driver.findElement(passwordField).sendKeys("abc123");
		driver.findElement(signInButtonField).click();

		Assert.assertEquals(driver.findElement(dashboardHeaderField).getText(), "Dashboard", "Page not found!!!");
		
		/*Assert.assertEquals(driver.getTitle(), "Dashboard- iBilling", "Page not found!!!");
		Assert.assertTrue(driver.findElement(dashboardHeaderField).isDisplayed(), "Page not found!!!");*/

	}

	@Test(priority = 2)
	public void addCustomer() throws InterruptedException {

		loginTest();
		driver.findElement(customerMenuField).click();
		driver.findElement(addCustomerMenuField).click();

		Thread.sleep(3000);// explecit wait
		Assert.assertEquals(driver.findElement(addCustomerHeaderField).getText(), "Add Contact", "Page not found!!!");
		
		driver.findElement(fullNameField).sendKeys("selenium" + generateRandomNum(999));
		
		selectFromDropdown(driver.findElement(companyDropdownField), "Techfios");
		
		driver.findElement(emailField).sendKeys(generateRandomNum(9999) + "demo@techfios.com");
		
		selectFromDropdown(driver.findElement(countryDropdownField), "Afghanistan");
		selectFromDropdownWithBy(countryDropdownField, "Afghanistan");
		
		
	}

	private void selectFromDropdown(WebElement element, String visibleText) {
		
		Select sel = new Select(element);
		sel.selectByVisibleText(visibleText);
	}
	
	private void selectFromDropdownWithBy(By field, String visibleText) {
		
		Select sel = new Select(driver.findElement(field));
		sel.selectByVisibleText(visibleText);
	}
	
	

	private int generateRandomNum(int boundaryNum) {
		Random rnd = new Random();
		int generatedNum = rnd.nextInt(boundaryNum);
		return generatedNum;
	}

	
	

}
