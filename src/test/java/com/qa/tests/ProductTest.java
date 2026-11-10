package com.qa.tests;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.qa.framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductTest extends BaseTest {

    @Test(groups = {"smoke"})
    public void verifyHomePage() {
        getPage().navigate(getBaseUrl());

        Assert.assertTrue(getPage().url().contains("practicesoftwaretesting.com"));
        System.out.println("Homepage loaded successfully");
    }

    @Test(groups = {"regression"})
    public void verifyProductPage() {
        getPage().navigate(getBaseUrl());

        PlaywrightAssertions.assertThat(getPage().locator("body")).isVisible();
        System.out.println("Product page loaded successfully");
    }
}