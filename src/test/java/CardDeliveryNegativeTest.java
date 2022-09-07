import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryNegativeTest {
    String date(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:9999");
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void  shouldMeetingNoSuccessfulEmptyCity() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=city].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Поле обязательно для заполнения"));
    }


    @Test
    void shouldMeetingNoSuccessfulWrongCity() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Кёльн");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=city].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldMeetingNoSuccessfulNameEng() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Ivanov Vasiliy");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=name].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldMeetingNoSuccessfulNameWithSymbol() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Петр 1-ый");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=name].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldMeetingNoSuccessfulWithoutNameAndSurname() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=name].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldMeetingNoSuccessful2DaysAfterDate() {
        String planningDate = date(2);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=date] .input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldMeetingNoSuccessfulToday() {
        String planningDate = date(0);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=date] .input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldMeetingNoSuccessfulWithoutCheckBox() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=agreement].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldMeetingNoSuccessfulWrongPhoneSymbol() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("!78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldMeetingNoSuccessfulPhoneOver11Symbol() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("+788888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldMeetingNoSuccessfulPhoneLess11Symbol() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("+7888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldMeetingNoSuccessfulPhoneWithoutPlus() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Васильевич Василий");
        $("[data-test-id=phone] input").setValue("78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldMeetingNoSuccessfulWithoutPhone() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Васильевич Василий");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Поле обязательно для заполнения"));
    }












}

