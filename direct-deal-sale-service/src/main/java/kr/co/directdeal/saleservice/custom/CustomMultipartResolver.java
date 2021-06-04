package kr.co.directdeal.saleservice.custom;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class CustomMultipartResolver extends CommonsMultipartResolver {

    @Override
    public void cleanupMultipart(MultipartHttpServletRequest request) {
        //log.debug("-- skip cleanupMultipart() --");
    }

    @Override
    public boolean isMultipart(HttpServletRequest request) {
        return super.isMultipart(request);
    }

    @Override
    public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
        return super.resolveMultipart(request);
    }
}
