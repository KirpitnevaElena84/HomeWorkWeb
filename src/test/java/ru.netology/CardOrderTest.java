package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1280, 1024));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldSuccessfullySendOrderWithValidData() {
        CardOrderPage page = new CardOrderPage(driver).open("http://localhost:7777");

        page.fillName("Иван Иванов")
                .fillPhone("+79001234567")
                .acceptAgreement()
                .submit();

        String message = page.getSuccessMessage();
        Assertions.assertTrue(
                message.contains("Ваша заявка успешно отправлена"),
                "Не дождались сообщения об успешной отправке. Текст на странице: " + message
        );
    }
}

