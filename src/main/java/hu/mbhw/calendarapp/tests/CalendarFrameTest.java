package hu.mbhw.calendarapp.tests;

import hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame;
import hu.mbhw.calendarapp.CalendarGraphics.Enums;
import org.junit.jupiter.api.*;
import javax.swing.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CalendarFrameTest {

    private CalendarFrame calendarFrame;

    @BeforeEach
    void setUp() {
        calendarFrame = new CalendarFrame(LocalDate.now());
    }

    @Test
    void testToggleView() {
        assertEquals(Enums.CALENDARFRAME_VIEW.MONTH, CalendarFrame.currentView, "Initial view should be MONTH.");
        calendarFrame.toggleView();
        assertEquals(Enums.CALENDARFRAME_VIEW.WEEK, CalendarFrame.currentView, "View should toggle to WEEK.");
        calendarFrame.toggleView();
        assertEquals(Enums.CALENDARFRAME_VIEW.MONTH, CalendarFrame.currentView, "View should toggle back to MONTH.");
    }
}
