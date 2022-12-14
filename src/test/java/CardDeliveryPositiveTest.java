import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryPositiveTest {
    String date(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

     @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldMeetingSuccessfullyBooked() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15)).should(exactText("Встреча успешно забронирована на " + planningDate));
    }


    @Test
    void shouldMeetingSuccessOnlyName() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Василий");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15)).should(exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void shouldMeetingSuccessOnlyShortName() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("И");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15)).should(exactText("Встреча успешно забронирована на " + planningDate));

    }

    @Test
    void shouldMeetingSuccess4DaysAfterDate() {
        String planningDate = date(4);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Иванов Василий");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15)).should(exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void shouldMeetingSuccessNameAndSurnameIsChange() {
        String planningDate = date(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (planningDate));
        $("[data-test-id=name] input").setValue("Василий Иванов");
        $("[data-test-id=phone] input").setValue("+78888888888");
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15)).should(exactText("Встреча успешно забронирована на " + planningDate));
    }
}