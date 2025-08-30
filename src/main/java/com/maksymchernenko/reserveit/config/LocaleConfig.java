package com.maksymchernenko.reserveit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuration class that enables internationalization (i18n) support for the application.
 * <p>
 * Defines a default locale, allows switching locales via request parameters
 * and registers the necessary interceptors.
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * Defines the {@link LocaleResolver} used to determine the current locale.
     * <p>
     * This implementation stores the selected locale in the user's HTTP session.
     * Default locale is set to {@link Locale#US}.
     *
     * @return a {@link LocaleResolver} with default locale configured
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);

        return slr;
    }

    /**
     * Defines the interceptor that allows changing the current locale
     * by passing a request parameter {@code ?lang=uk}.
     *
     * @return a {@link LocaleChangeInterceptor} configured with parameter name "lang"
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");

        return lci;
    }

    /**
     * Registers the {@link LocaleChangeInterceptor} so that locale switching
     * can be applied to all incoming requests.
     *
     * @param registry interceptor registry where the locale change interceptor is added
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
