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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TestGoogleCalendarApiService {

    public static final String CALENDAR_ID = "primary";
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
                .setMaxResults(100)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> listOfEvents = events.getItems();
        assertNotNull(listOfEvents);
        assertFalse(listOfEvents.isEmpty());
        System.out.println(listOfEvents.stream().map(Event::getSummary).collect(Collectors.toList()));
    }

    @Test
    void removeDuplicateNotRecurringEvents() throws IOException {
        List<String> eventsToDelete = List.of("Anniversary",
                "Birthday",
                "vyrocie",
                "narodky",
                "meniny",
                "narodeniny");

        final Calendar service = CalendarQuickstart.getCalendarService(httpTransport);

        for (String eventSummary : eventsToDelete) {
            deleteAllReccurrenciesOfEventWithSummary(eventSummary, service);
        }
    }

    private void deleteAllReccurrenciesOfEventWithSummary(String eventSummary, Calendar service) throws IOException {
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list(CALENDAR_ID)
                .setMaxResults(100)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .setQ(eventSummary)
                .execute();

        events.getItems().stream()
                .map(Event::getId)
                .forEach(eventId -> {
                    System.out.println("deleting event (summary: " + eventSummary + ") \t\t ID: " + eventId);
                    deleteEvent(eventId, service);
                });

    }

    private static void deleteEvent(String eventId, Calendar service) {
        try {
            service.events()
                    .delete(CALENDAR_ID, eventId)
                    .execute();
        } catch (IOException e) {
            System.err.println("Could not delete event with ID: " + eventId);
        }
    }
}
