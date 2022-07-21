import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        WebDriverManager.firefoxdriver().setup();

        FirefoxDriver driver = new FirefoxDriver();

        driver.get("http://live.techpanda.org/index.php/");
        WebDriverWait wait =  new WebDriverWait(driver, Duration.ofSeconds(10));

        assertEquals("Home page", driver.getTitle());

        driver.findElement(By.cssSelector(".level0.first")).click();
        assertEquals("Mobile", driver.getTitle());

        //Сортировка товаров

        Select selectSort = new Select(driver.findElement(By.cssSelector(".sort-by [title = 'Sort By']")));
        selectSort.selectByVisibleText("Name");

        List<WebElement> titleMobile = driver.findElements(By.cssSelector(".product-name a"));

        String[] titleArraySort = new String[titleMobile.size()];
        String[] titleArray = new String[titleMobile.size()];

        for(int i = 0; i < titleMobile.size(); i++) {
            titleArray[i] = titleMobile.get(i).getText();
            titleArraySort[i] = titleMobile.get(i).getText();
        }

        Arrays.sort(titleArraySort);

        assertEquals(Arrays.toString(titleArray),Arrays.toString(titleArraySort));

        String priceXperia = driver.findElement(By.cssSelector("#product-price-1  span.price")).getText();

        wait.until((ExpectedConditions.elementToBeClickable(By.cssSelector("[title=\"Xperia\"]"))));
        driver.findElement(By.cssSelector("[title=\"Xperia\"]")).click();

        assertEquals(priceXperia, driver.findElement(By.className("price")).getText());

        //Проверка того, что нельзя заказть большое количество товаров
        driver.findElement(By.cssSelector(".level0.first")).click();


        driver.findElement(By.xpath("//span[@id=\"product-price-1\"]/../..//button")).click();

        driver.findElement(By.cssSelector("[title=Qty]")).sendKeys("000");

        driver.findElement(By.cssSelector("[title=Update]")).click();

        wait.until((ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".item-msg.error")))));

        assertEquals("* The maximum quantity allowed for purchase is 500.", driver.findElement(By.cssSelector(".item-msg.error")).getText());

        driver.findElement(By.cssSelector("[title=\"Empty Cart\"]")).click();

        assertEquals("You have no items in your shopping cart.", driver.findElement(By.cssSelector(".cart-empty p")).getText());

        //Сравнение двух товаров
        driver.findElement(By.cssSelector(".level0.first")).click();

        String firstMobile =  driver.findElement(By.cssSelector("[title = 'Sony Xperia']")).getText();
        driver.findElement(By.xpath("//span[@id=\"product-price-1\"]/../..//a[@class=\"link-compare\"]")).click();

        String secondMobile =  driver.findElement(By.cssSelector("h2 [title = 'IPhone']")).getText();
        driver.findElement(By.xpath("//span[@id=\"product-price-2\"]/../..//a[@class=\"link-compare\"]")).click();

        String originalWindow = driver.getWindowHandle();

        driver.findElement(By.cssSelector("[title=\"Compare\"]")).click();

        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        for (String windowHandle : driver.getWindowHandles()) {
            if(!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));

        assertEquals("COMPARE PRODUCTS", driver.findElement(By.tagName("h1")).getText());

        assertEquals(firstMobile, driver.findElement(By.cssSelector("h2 [title=\"Sony Xperia\"]")).getText());

        assertEquals(secondMobile, driver.findElement(By.cssSelector("h2 [title=\"IPhone\"]")).getText());

        driver.findElement(By.xpath("//button[@title='Close Window']")).click();
        driver.switchTo().window(originalWindow);

        driver.get("http://live.techpanda.org/");

        driver.findElement(By.cssSelector(".account-cart-wrapper a")).click();

        driver.findElement(By.cssSelector("[title=\"My Account\"]")).click();

//        Зарегестрироваться. Закомментировал, так как возникнет ошибка, если я использую повторно один @email

//        driver.findElement(By.cssSelector("[title=\"Create an Account\"]")).click();

//        driver.findElement(By.id("firstname")).sendKeys("Anon");
//        driver.findElement(By.id("lastname")).sendKeys("Anonievich");
//        driver.findElement(By.id("email_address")).sendKeys("sinoptik@mxn.com");
//        driver.findElement(By.id("password")).sendKeys("123456");
//        driver.findElement(By.id("confirmation")).sendKeys("123456");

//        driver.findElement(By.xpath("//button[@title='Register']")).click();

//        assertEquals("Thank you for registering with Main Website Store.", driver.findElement(By.cssSelector(".success-msg span")).getText());


        //Войти через свой логин
        driver.findElement(By.id("email")).sendKeys("sinoptik@mxn.com");
        driver.findElement(By.id("pass")).sendKeys("123456");
        driver.findElement(By.id("send2")).click();

        //Добавить товар с списов желаемых
        driver.findElement(By.cssSelector(".level0.last")).click();

        driver.findElement(By.xpath("//a[@title=\"LG LCD\"]/..//a[@class=\"link-wishlist\"]")).click();

        //Поделиться списом
        driver.findElement(By.cssSelector("[title=\"Share Wishlist\"]")).click();

        driver.findElement(By.cssSelector("[name=\"emails\"]")).sendKeys("qwerty@qwerty.ru");

        driver.findElement(By.cssSelector("[title=\"Share Wishlist\"]")).click();

        assertEquals("Your Wishlist has been shared.", driver.findElement(By.cssSelector(".success-msg span")).getText());


//        Заказать товар в первый раз. Закомменировал, потому что возникнет ошибка, так как адрес сохраняется
//        driver.findElement(By.cssSelector("[title=\"Add to Cart\"]")).click();
//
//        driver.findElement(By.cssSelector(".method-checkout-cart-methods-onepage-bottom [title=\"Proceed to Checkout\"]")).click();
//
//        driver.findElement(By.id("billing:street1")).clear();
//        driver.findElement(By.id("billing:street1")).sendKeys("ABC");
//        driver.findElement(By.id("billing:city")).clear();
//        driver.findElement(By.id("billing:city")).sendKeys("New York");
//        new Select(driver.findElement(By.id("billing:country_id"))).selectByValue("US");
//        driver.findElement(By.id("billing:postcode")).clear();
//        driver.findElement(By.id("billing:postcode")).sendKeys("542896");
//        new Select(driver.findElement(By.id("billing:region_id"))).selectByValue("43");
//        driver.findElement(By.id("billing:telephone")).clear();
//        driver.findElement(By.id("billing:telephone")).sendKeys("12345678");
//
//        driver.findElement(By.cssSelector("[onclick=\"billing.save()\"]")).click();
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[onclick=\"shippingMethod.save()\"]")));
//        driver.findElement(By.cssSelector("[onclick=\"shippingMethod.save()\"]")).click();
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[title='Check / Money order']")));
//        driver.findElement(By.cssSelector("[title='Check / Money order']")).click();
//        driver.findElement(By.cssSelector("[onclick=\"payment.save()\"]")).click();
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[onclick=\"review.save();\"]")));
//        driver.findElement(By.cssSelector("[onclick=\"review.save();\"]")).click();
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
//        driver.findElement(By.tagName("h1")).click();

    }

}
