//Products under a dollar, Enter search term
//give it some time, it'll work ;p

package org.amazon;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FreeProducts {
    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.amazon.ca/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"twotabsearchtextbox\"]"));
        //Enter your search term
        String searchTerm = "books";

        searchBox.sendKeys(searchTerm);
        searchBox.sendKeys(Keys.RETURN);

         driver.findElement(By.xpath("//span[@id='a-autoid-0-announce']")).click();
        driver.findElement(By.xpath("//a[@id='s-result-sort-select_1']")).click();
        XSSFWorkbook wf = new XSSFWorkbook();
        XSSFSheet sheet = wf.createSheet();

        sheet.createRow(0);
        sheet.getRow(0).createCell(0).setCellValue(searchTerm);
        sheet.getRow(0).createCell(1).setCellValue("Products under 1CAD");
        sheet.createRow(1);
        sheet.getRow(1).createCell(0).setCellValue(searchTerm);
        sheet.getRow(1).createCell(1).setCellValue("Product name");
        sheet.getRow(1).createCell(2).setCellValue("Price(in cents)");
        sheet.getRow(1).createCell(3).setCellValue("Shipping");

//        int count=driver.findElements(By.cssSelector("div[data-cel-widget*=MAIN-SEARCH_RESULTS]")).size();
//        System.out.println(count);

        for (int i = 1; i <= 51; i++)
        {
            if (!driver.findElements(By.xpath("//div[@data-component-type=\"s-search-result\"][" + i + "]//span[contains(@class,\"a-price-whole\")]")).isEmpty()) {
                if ((driver.findElement(By.xpath("//div[@data-component-type=\"s-search-result\"][" + i + "]//span[contains(@class,\"a-price-whole\")]"))).getText().equals("0"))
                {
                    WebElement pname = driver.findElement(By.xpath("//div[@data-component-type=\"s-search-result\"][" + i + "]//h2//a//span[contains(@class,\"a-size\")]"));
                    String name = pname.getText();
                    sheet.createRow(i + 1);
                    sheet.getRow(i + 1).createCell(1).setCellValue(name);
                    WebElement price = driver.findElement(By.xpath("//div[@data-component-type=\"s-search-result\"][" + i + "]//span[contains(@class,\"a-price-fraction\")]"));
                    String cost = price.getText();
                    sheet.getRow(i + 1).createCell(2).setCellValue(cost);
                    //ignore case in "xpath contains" locator and check for 2 terms
                    if (!driver.findElements(By.xpath("//div[@data-component-type=\"s-search-result\"][" + i + "]//span[contains((translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')),\"shipping\") or contains((translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')),\"delivery\")]")).isEmpty()) {
                        WebElement ship = driver.findElement(By.xpath("//div[@data-component-type=\"s-search-result\"][" + i + "]//span[contains((translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')),\"shipping\") or contains((translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')),\"delivery\")]"));
                        String shipping = ship.getAttribute("aria-label");
                        sheet.getRow(i + 1).createCell(3).setCellValue(shipping);
                    }

                }
                if(!(driver.findElement(By.xpath("//div[@data-component-type=\"s-search-result\"][" + i + "]//span[contains(@class,\"a-price-whole\")]"))).getText().equals("0")) {
                    break;
                }
                }
            else
            {
                    sheet.createRow(i + 1);
                    sheet.getRow(i + 1).createCell(1).setCellValue("Price unavailable");

            }
        }
        File file = new File((System.getProperty("user.dir") + "/src/main/resources/1CADProducts.xlsx"));
        FileOutputStream fs = new FileOutputStream(file);
        wf.write(fs);
        wf.close();
        driver.close();
    }
}