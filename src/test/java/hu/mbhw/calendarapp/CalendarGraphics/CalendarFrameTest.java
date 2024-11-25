package hu.mbhw.calendarapp.CalendarGraphics;

import org.junit.jupiter.api.*;

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
        assertEquals(CalendarFrame.CALENDARFRAME_VIEW.MONTH, CalendarFrame.currentView, "Initial view should be MONTH.");
        calendarFrame.toggleView();
        assertEquals(CalendarFrame.CALENDARFRAME_VIEW.WEEK, CalendarFrame.currentView, "View should toggle to WEEK.");
        calendarFrame.toggleView();
        assertEquals(CalendarFrame.CALENDARFRAME_VIEW.MONTH, CalendarFrame.currentView, "View should toggle back to MONTH.");
    }
}
