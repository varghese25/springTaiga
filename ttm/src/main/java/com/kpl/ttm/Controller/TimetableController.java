package com.kpl.ttm.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class TimetableController {

    @GetMapping("/timeTable")
    public String getTimetable(Model model) {
        // Simulated timetable data - you could pull this from your database
        String[][] timetable = {
            {"09:00-09:45 AM", "English", "Tamil", "Games", "Tamil", "PT", "", "", ""},
            {"09:45-10:15 AM", "Tamil", "English", "Games", "Tamil", "Tamil", "", "", ""},
            {"10:15-10:30 AM", "Break", "Break", "Break", "Break", "Break", "", "", ""},
            {"10:30-11:15 AM", "Science", "Maths", "Maths", "Social Science", "Tamil", "", "", ""},
            {"11:15-12:00 PM", "Social Science", "Social Science", "Maths", "English", "Science", "", "", ""},
            {"12:00-12:45 PM", "Lunch Break", "Lunch Break", "Lunch Break", "Lunch Break", "Lunch Break", "", "", ""},
            {"12:45-01:30 PM", "Maths", "Library", "Tamil", "Library", "Library", "", "", ""},
            {"01:30-02:15 PM", "Library", "Library", "English", "Science", "English", "", "", ""},
            {"02:15-02:30 PM", "Break", "Break", "Break", "Break", "Break", "", "", ""},
            {"02:30-03:15 PM", "Tamil", "English", "Science", "PT", "Maths", "", "", ""}
        };

        model.addAttribute("timetable", timetable);
        return "timetable"; // This will refer to the timetable.html template
    }
}