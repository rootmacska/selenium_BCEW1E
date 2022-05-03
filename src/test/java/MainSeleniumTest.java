import java.util.concurrent.TimeUnit;

import org.junit.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class MainSeleniumTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 30);

        // Deleting all the cookies
        driver.manage().deleteAllCookies();

        // Specifiying pageLoadTimeout and Implicit wait
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    private final By bodyLocator = By.tagName("body");

    private final By loginLink = By.xpath("//div//a[@href='/login']");
    private final By loginEmailLocator = By.xpath("//div[@class='css-cy03ec']/input[@name='email']");
    private final By loginPassLocator = By.xpath("//div[@class='css-cy03ec']/input[@name='password']");
    private final By loginSubmitLocator = By.cssSelector("button[type='submit']");

    private final By accountDropDown = By.cssSelector("span.css-1w6frfr");
    private final By logOutLinkLocator = By
            .xpath("//*[@id='top']/header/div/div/div[2]/nav[2]/div/div/ul/li[3]/button");

    private final By plansLinkLocator = By.xpath("//div//a[@href='/plans']");
    private final By developersLinkLocator = By.xpath("//*[@id='top']/header/div/div[1]/div[2]/nav[2]/a[2]");

    private WebElement waitVisibiiltyAndFindElement(By locator) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this.driver.findElement(locator);
    }

    @Test
    public void LoginAndLogoutTest() {
        this.driver.get("https://www.file.io/");

        /*
         * Open Login Page
         */

        WebElement loginPageElement = this.waitVisibiiltyAndFindElement(loginLink);
        loginPageElement.click();
        wait.until(ExpectedConditions.titleContains("Log In | file.io"));
        String loginPageTitle = "Log In | file.io";
        // reading page title
        Assert.assertTrue(driver.getTitle().contains(loginPageTitle));

        /*
         * Login with Details
         */

        WebElement formEmailElement = waitVisibiiltyAndFindElement(loginEmailLocator);
        WebElement formPassElement = waitVisibiiltyAndFindElement(loginPassLocator);
        WebElement loginLinkElement = this.waitVisibiiltyAndFindElement(loginSubmitLocator);

        formEmailElement.sendKeys("tibej36266@3dmasti.com");
        formPassElement.sendKeys("tibej36266@A");
        loginLinkElement.click();
        wait.until(ExpectedConditions.titleContains("My Files | file.io"));
        WebElement bodyElement = waitVisibiiltyAndFindElement(bodyLocator);
        Assert.assertTrue(bodyElement.getText().contains("Downloads"));

        /*
         * Open drop-down menu and select Log Out
         */

        WebElement loggedInDropDownElement = this.waitVisibiiltyAndFindElement(accountDropDown);
        loggedInDropDownElement.click();
        String isDropDown = this.waitVisibiiltyAndFindElement(accountDropDown).getAttribute("aria-pressed");
        Assert.assertEquals("true", isDropDown);

        WebElement logOutLinkElement = this.driver.findElement(logOutLinkLocator);
        logOutLinkElement.click();
        bodyElement = waitVisibiiltyAndFindElement(bodyLocator);
        Assert.assertTrue(bodyElement.getText().contains("Log In"));
    }

    // Static Page Test
    @Test
    public void StaticPageTest() {
        this.driver.get("https://www.file.io/");

        WebElement websitePlansElement = this.waitVisibiiltyAndFindElement(plansLinkLocator);
        websitePlansElement.click();
        String plansPageTitle = "Plans | file.io";
        wait.until(ExpectedConditions.titleContains("Plans"));
        Assert.assertTrue(driver.getTitle().contains(plansPageTitle));
    }

    // hover text check

    @Test
    public void checkHoverForDevelopersLink() {
        // in case of normal pages, verify tooltips by using title attribute
        // https://www.h2kinfosys.com/blog/how-to-verify-tooltip-with-selenium-webdriver-using-java/

        this.driver.get("https://www.file.io/");
        WebElement websiteDevelopersElement = this.waitVisibiiltyAndFindElement(developersLinkLocator);
        String actualHoverText = websiteDevelopersElement.getAttribute("title");
        Assert.assertEquals("Developers", actualHoverText);
    }

    @After
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }
}
