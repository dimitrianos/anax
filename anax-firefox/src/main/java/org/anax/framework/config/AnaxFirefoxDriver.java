package org.anax.framework.config;

import org.anax.framework.configuration.AnaxDriver;
import org.anax.framework.controllers.WebController;
import org.anax.framework.controllers.WebDriverWebController;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.net.URL;


@Configuration
public class AnaxFirefoxDriver {

    @Value("${anax.target.url:http://www.google.com}")
    String targetUrl;
    @Value("${anax.remote.host:NOT_CONFIGURED}")
    String remoteHost;
    @Value("${anax.remote.port:NOT_CONFIGURED}")
    String remotePort;


    @ConditionalOnMissingBean
    @Bean
    public AnaxDriver defaultAnaxDriver(@Value("${anax.localdriver:true}") Boolean useLocal) {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        FirefoxOptions firefoxoptions;

        if (useLocal) {

            firefoxoptions = new FirefoxOptions();
            String x = (System.getProperty("os.name").toLowerCase().contains("mac")) ? "--start-fullscreen" : "--start-maximized";
            firefoxoptions.addArguments(x);
            return () -> {
                FirefoxDriver driver = new FirefoxDriver(firefoxoptions);
                driver.get(targetUrl);
                return driver;
            };
        } else {
            Augmenter augmenter = new Augmenter(); // adds screenshot capability to a default webdriver.
            return () -> augmenter.augment(new RemoteWebDriver(
                    new URL("http://" + remoteHost + ":" + remotePort + "/wd/hub"),
                    capabilities));
        }
    }


}
