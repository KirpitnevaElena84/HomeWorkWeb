package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderTest {

    private WebDriver driver;
    private CardOrderPage cardOrderPage;

    private static final String APP_URL = System.getProperty("app.url", "http://localhost:9999");

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // --- Ключевое для CI: headless ---
        // Локально окно не нужно, в CI дисплея нет вообще.
        // Управляется свойством: -Dheadless=true (в CI) либо по умолчанию true.
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        if (headless) {
            options.addArguments("--headless=new");
        }

        // --- Флаги, обязательные на Linux-раннере GitHub Actions ---
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        cardOrderPage = new CardOrderPage(driver);
    }

    @Test
    void shouldSuccessfullySendOrderWithValidData() {
        cardOrderPage
                .open(APP_URL)
                .fillName("Иван Иванов")
                .fillPhone("+79990001122")
                .acceptAgreement();

        cardOrderPage.submit();

        String message = cardOrderPage.getSuccessMessage();
        assertTrue(
                message.contains("Ваша заявка успешно отправлена"),
                "Ожидалось сообщение об успешной отправке, но получено: " + message
        );
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
