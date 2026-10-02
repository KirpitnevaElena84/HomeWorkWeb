package ru.netology.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CardOrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Поля обёрнуты в контейнеры с data-test-id, а сам input лежит внутри — отсюда вложенность.
    private final By nameInput      = By.cssSelector("[data-test-id=name] input");
    private final By phoneInput     = By.cssSelector("[data-test-id=phone] input");
    private final By agreementBox   = By.cssSelector("[data-test-id=agreement] input");
    private final By submitButton   = By.cssSelector("[data-test-id=button]");
    private final By successMessage = By.cssSelector("[data-test-id=order-success]");

    public CardOrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public CardOrderPage open(String url) {
        driver.get(url);
        return this;
    }

    public CardOrderPage fillName(String value) {
        driver.findElement(nameInput).sendKeys(value);
        return this;
    }

    public CardOrderPage fillPhone(String value) {
        driver.findElement(phoneInput).sendKeys(value);
        return this;
    }

    public CardOrderPage acceptAgreement() {
        WebElement checkbox = driver.findElement(agreementBox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }

    public CardOrderPage submit() {
        driver.findElement(submitButton).click();
        return this;
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }
}
