package com.example.identity.service;

import com.example.identity.dto.request.AuthenticationRequest;
import com.example.identity.dto.request.IntrospectRequest;
import com.example.identity.dto.request.LogoutRequest;
import com.example.identity.dto.request.RefreshTokenRequest;
import com.example.identity.dto.response.AuthenticationResponse;
import com.example.identity.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException;

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;
}
