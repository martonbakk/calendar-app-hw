package hu.mbhw.calendarapp.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    private static List<Event> data=new ArrayList<>();
    private static List<Event> currentlyDisplayedEvents=new ArrayList<>(); ;
    public static String fileName;

    public static void loadData(){
        data = readEventsFromFile();
        for (Event event : data) {
            System.out.println(event.name);
        }
        filterEventList("January", "Home");

    }

    public static List<Event> readEventsFromFile() {
        List<Event> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 6) {
                    String name = parts[0];
                    String type = parts[1];
                    String month = parts[2];
                    int day = Integer.parseInt(parts[3]);
                    int hour = Integer.parseInt(parts[4]);
                    String details = parts[5].replace(";", ""); // Remove trailing semicolon
                    events.add(new Event(name, type, month, day, hour, details));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events;
    }

    // Események visszaírása a fájlba
    public static void writeEventsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Event event : data) {
                writer.write(event.toString());
                writer.newLine(); // Új sor
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Event> currentlyDisplayedEvents() {
        return currentlyDisplayedEvents;
    }

    public static void filterEventList( String month, String type) {
        currentlyDisplayedEvents.clear();
        for(Event event : data) {

            System.out.println(((event.type.equals(type)&&event.month.equals(month))==true?"Yes":"no")+" "+event.name+" ");

            if(event.type.equals(type)&&event.month.equals(month)){
                currentlyDisplayedEvents.add(event);
            }
        }
    }

    public static String[][] newMatrix(){
        return new String[][] {
                {"1", "2", "3", "4", "5", "6", "7"},
                {"8", "9", "10", "11", "12", "13", "14"},
                {"15", "16", "17", "18", "19", "20", "21"},
                {"22", "23", "24", "25", "26", "27", "28"},
                {"29", "30", "31", "1", "2", "3", "4"}};
    }

    public static String[] newColumnNames(){
        return new String[]{"H", "K", "Sz", "Cs", "P", "Szo", "V"};
    }

    public static List<Event> getCurrentlyDisplayedEvents() {

        return currentlyDisplayedEvents;
    }

    private static void sortCurrentlyDisplayedEvents() {
        currentlyDisplayedEvents.sort((e1, e2) -> {
            // Rendezés először nap szerint, majd óra szerint
            int dayComparison = Integer.compare(e1.day, e2.day);
            if (dayComparison != 0) {
                return dayComparison;
            }
            return Integer.compare(e1.hour, e2.hour);
        });
    }

    public static void insertRecordIntoDatabase(Event event) {
        data.add(event);
        writeEventsToFile();
    }
}
