package hu.mbhw.calendarapp.tests;

import hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame;
import hu.mbhw.calendarapp.CalendarGraphics.Enums;
import hu.mbhw.calendarapp.actionlisteners.ChangeViewButtonListener;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ChangeViewButtonListenerTest {
    private CalendarFrame calendarFrame;
    private ChangeViewButtonListener listener;

    @BeforeEach
    void setUp() {
        calendarFrame = new CalendarFrame(LocalDate.now());
        listener = new ChangeViewButtonListener(calendarFrame);
    }

    @Test
    void testToggleViewAction() {
        ActionEvent event = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "ToggleView");
        listener.actionPerformed(event);

        assertEquals(CalendarFrame.currentView, Enums.CALENDARFRAME_VIEW.WEEK, "View should toggle to WEEK.");
        listener.actionPerformed(event);
        assertEquals(CalendarFrame.currentView, Enums.CALENDARFRAME_VIEW.MONTH, "View should toggle back to MONTH.");
    }
}