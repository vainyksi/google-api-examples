package org.example;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TestGoogleCalendarApiService {

    private NetHttpTransport httpTransport;

    @BeforeEach
    void setUp() throws GeneralSecurityException, IOException {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    @Test
    void getCalendarService() throws IOException {
        Calendar service = CalendarQuickstart.getCalendarService(httpTransport);
        assertNotNull(service);
    }
}
