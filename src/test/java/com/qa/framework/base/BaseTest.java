package com.qa.framework.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qa.framework.config.ConfigReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        Playwright pw = Playwright.create();
        playwright.set(pw);

        String browserName = ConfigReader.get("browser");
        boolean headless = ConfigReader.getBoolean("headless");

        BrowserType.LaunchOptions options =
                new BrowserType.LaunchOptions().setHeadless(headless);

        Browser br;
        switch (browserName.toLowerCase()) {
            case "chromium":
                br = pw.chromium().launch(options);
                break;
            case "firefox":
                br = pw.firefox().launch(options);
                break;
            case "webkit":
                br = pw.webkit().launch(options);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
        browser.set(br);

        BrowserContext browserContext = br.newContext();
        context.set(browserContext);

        Page browserPage = browserContext.newPage();
        page.set(browserPage);
        browserPage.setDefaultTimeout(ConfigReader.getInt("timeout"));

        System.out.println("Browser: " + browserName
                + " | Thread: " + Thread.currentThread().getName());
    }

    protected Page getPage() {
        return page.get();
    }

    protected BrowserContext getContext() {
        return context.get();
    }

    protected String getBaseUrl() {
        return ConfigReader.get("baseUrl");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            if (context.get() != null) context.get().close();
            if (browser.get() != null) browser.get().close();
            if (playwright.get() != null) playwright.get().close();
        } finally {
            page.remove();
            context.remove();
            browser.remove();
            playwright.remove();
        }
    }
}