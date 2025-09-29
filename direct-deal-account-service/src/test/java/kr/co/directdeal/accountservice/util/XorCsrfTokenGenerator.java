package kr.co.directdeal.accountservice.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.DeferredCsrfToken;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

public final class XorCsrfTokenGenerator {

    private static final CsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();

    private static final CsrfTokenRequestHandler requestHandler = new XorCsrfTokenRequestAttributeHandler();

    private final DeferredCsrfToken deferredCsrfToken;

    private final CsrfToken xoredCsrfToken;

    private XorCsrfTokenGenerator(DeferredCsrfToken deferredCsrfToken, CsrfToken xoredCsrfToken) {
        this.deferredCsrfToken = deferredCsrfToken;
        this.xoredCsrfToken = xoredCsrfToken;
    }

    public static XorCsrfTokenGenerator generate() {
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        DeferredCsrfToken deferredCsrfToken = csrfTokenRepository.loadDeferredToken(httpServletRequest, httpServletResponse);
        requestHandler.handle(httpServletRequest, httpServletResponse, deferredCsrfToken::get);
        CsrfToken xoredCsrfToken = (CsrfToken) httpServletRequest.getAttribute(CsrfToken.class.getName());
        return new XorCsrfTokenGenerator(deferredCsrfToken, xoredCsrfToken);
    }

    public CsrfToken getXoredCsrfToken() {
        return this.xoredCsrfToken;
    }

    public CsrfToken getCsrfToken() {
        return this.deferredCsrfToken.get();
    }
}
