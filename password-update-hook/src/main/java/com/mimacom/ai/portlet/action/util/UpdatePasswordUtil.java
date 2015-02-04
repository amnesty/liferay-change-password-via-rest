package com.mimacom.ai.portlet.action.util;

import com.liferay.portal.kernel.util.MapUtil;
import com.mimacom.ai.ssl.util.MySimpleClientHttpRequestFactory;
import com.mimacom.ai.ssl.util.SSLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.Properties;

public class UpdatePasswordUtil {

    private static final Logger log = LoggerFactory.getLogger(UpdatePasswordUtil.class);

    private static String API_KEY_PARAM_NAME;
    private static String USER_PARAM_NAME;
    private static String OLD_PASSWORD_PARAM_NAME;
    private static String NEW_PASSWORD_PARAM_NAME;


    private static String SERVICE_URL;
    private static String API_KEY;

    static {
        Properties props = new Properties();
        try {
            props.load(UpdatePasswordUtil.class.getClassLoader().getResourceAsStream("/config.properties"));

            SERVICE_URL = props.getProperty("server.url") + props.getProperty("service.path");
            if (SERVICE_URL == null) {
                log.error("Bad configuration found: nor 'server.url' nor 'service.path' property cannot be null");
                throw new IOException("Bad configuration found: nor 'server.url' nor 'service.path' property cannot be null");
            }

            API_KEY = props.getProperty("api-key");

            API_KEY_PARAM_NAME = props.getProperty("param.api-key");
            USER_PARAM_NAME = props.getProperty("param.user");
            OLD_PASSWORD_PARAM_NAME = props.getProperty("param.old-password");
            NEW_PASSWORD_PARAM_NAME = props.getProperty("param.new-password");

        } catch (IOException e) {
            log.error("ERROR LOADING REST SERVICE CONFIGURATION", e);
        }
    }


    public static boolean updatePassword(String user, String oldPassword, String newPassword) throws Exception {
        if(log.isDebugEnabled()) {
            log.debug("updating password...");
            log.debug("user: " + user);
            log.debug("oldPassword: " + oldPassword);
            log.debug("newPassword: " + newPassword);
        }
        
        boolean result = false;

        RestTemplate restTemplate = new RestTemplate(new org.springframework.http.client.HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        SSLUtil.turnOffSslChecking();
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        HostnameVerifier verifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        restTemplate.setRequestFactory(new MySimpleClientHttpRequestFactory(verifier));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(4);
        params.add(API_KEY_PARAM_NAME, API_KEY);
        params.add(USER_PARAM_NAME, user);
        params.add(OLD_PASSWORD_PARAM_NAME, oldPassword);
        params.add(NEW_PASSWORD_PARAM_NAME, newPassword);

        if(log.isDebugEnabled()) {
            log.debug("params...");
            log.debug(MapUtil.toString(params));
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, headers);
            ResponseEntity<String> response =  restTemplate.exchange(SERVICE_URL, HttpMethod.POST, request, String.class);

            HttpStatus status = response.getStatusCode();
            
            if( HttpStatus.OK.equals(status)) {
				result = true;
			} else {
                log.error("ERROR ACTUALITZANT PASSWORD: " + status.getReasonPhrase());
            }

        } catch (HttpClientErrorException e) {
            log.error("error: " + e.getStatusCode() + " " + e.getStatusText());
            log.error("error:  " + e.getResponseBodyAsString());
        }
        
        return result;

    }
}
