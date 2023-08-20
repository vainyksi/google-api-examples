package org.example;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    void getListOfEvents() throws IOException {
        final Calendar service = CalendarQuickstart.getCalendarService(httpTransport);
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> listOfEvents = events.getItems();
        assertNotNull(listOfEvents);
        assertFalse(listOfEvents.isEmpty());
    }


}
