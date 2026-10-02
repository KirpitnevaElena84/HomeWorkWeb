package ru.netology;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CardOrderPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // === Поля формы ===
    // Если у полей есть data-test-id — лучше перейти на него (проверь в DevTools).
    private static final By NAME_INPUT = By.cssSelector("input[name='name']");
    private static final By PHONE_INPUT = By.cssSelector("input[name='phone']");

    // === Чекбокс согласия ===
    // Настоящий input обычно скрыт через CSS, поэтому кликаем по label.
    private static final By AGREEMENT_INPUT = By.cssSelector("input[type='checkbox']");
    private static final By AGREEMENT_LABEL =
            By.cssSelector("label[for='agreement'], .checkbox__label, .checkbox-label");

    // === Кнопка отправки ===
    // Реальный HTML: <button role="button" type="button" class="button button_view_extra ...">
    private static final By SUBMIT_BUTTON = By.cssSelector("button.button_view_extra");

    // === Сообщение об успехе ===
    // Подтверждено по вёрстке: <p data-test-id="order-success"> ... </p>
    private static final By SUCCESS_MESSAGE =
            By.cssSelector("[data-test-id='order-success']");

    public CardOrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public CardOrderPage open(String url) {
        driver.get(url);
        // Ждём, пока форма отрисуется — убирает гонку в следующих шагах
        wait.until(ExpectedConditions.visibilityOfElementLocated(NAME_INPUT));
        return this;
    }

    public CardOrderPage fillName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(NAME_INPUT));
        field.clear();
        field.sendKeys(name);
        return this;
    }

    public CardOrderPage fillPhone(String phone) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(PHONE_INPUT));
        field.clear();
        field.sendKeys(phone);
        return this;
    }

    public CardOrderPage acceptAgreement() {
        // Ждём присутствия, а НЕ кликабельности: input часто скрыт через CSS.
        WebElement checkbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(AGREEMENT_INPUT));

        if (checkbox.isSelected()) {
            return this;
        }

        // 1) Пробуем кликнуть по видимому label
        try {
            wait.until(ExpectedConditions.elementToBeClickable(AGREEMENT_LABEL)).click();
        } catch (Exception labelNotFound) {
            // 2) Фолбэк: клик по самому input через JS (работает и для скрытого)
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }

        // Контроль: если после клика чекбокс всё ещё не выбран — кликаем JS по input
        if (!checkbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }

        return this;
    }

    public void submit() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", button);

        try {
            button.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    public String getSuccessMessage() {
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(SUCCESS_MESSAGE));
        return message.getText();
    }

    // === Хелперы для отладки и проверок ===

    public String getNameValue() {
        return driver.findElement(NAME_INPUT).getAttribute("value");
    }

    public String getPhoneValue() {
        return driver.findElement(PHONE_INPUT).getAttribute("value");
    }

    public boolean isAgreementSelected() {
        return driver.findElement(AGREEMENT_INPUT).isSelected();
    }
}
