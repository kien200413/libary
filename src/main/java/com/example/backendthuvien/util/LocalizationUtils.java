package com.example.backendthuvien.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    public String getLocal(String messageKey,Object...params){
        HttpServletRequest request=Webutils.getCurren();
        Locale locale=localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey,params,locale);
    }
}
