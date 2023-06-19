// HappyBirthdayApp.java
// D. Singletary
// 1/29/23
// wish multiple users a happy birthday

package edu.fscj.cop3330c.birthday;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TimeZone;

// utility class with constant list of desired timezones (US/)
class DesiredTimeZones {
    public static final ArrayList<String> ZONE_LIST = new ArrayList<>();

    public DesiredTimeZones() {
        String[] availableTimezones = TimeZone.getAvailableIDs();
        for (String s : availableTimezones) {
            if (s.length() >= 3 && s.substring(0, 3).equals("US/")) {
                ZONE_LIST.add(s);
            }
        }
    }

    // show the list of zones as a numeric menu
    public void showMenu() {
        int menuCount = 1;
        for (String s : ZONE_LIST)
            System.out.println(menuCount++ + ". " + s);
    }
}

// main mpplication class
public class HappyBirthdayApp implements BirthdayGreeter {
    //private User user;
    private ArrayList<User> birthdays = new ArrayList<>();

    public HappyBirthdayApp() { }

    public void sendCard(User u) {
        // test with odd length (comment to test with even length, below)
        final String WISHES = "Hope all of your birthday wishes come true!";
        // uncomment to test with even length
        //final String WISHES = "Hope all of your birthday wishes come true!x";
        String msg = "Happy Birthay, " + u.getName() + "\n" + WISHES;
        String card = buildCard(msg);
        System.out.println(card);
        System.out.println("sending email to " + u.getEmail() + "\n");
    }

    public String buildCard(String msg) {
        final String NEWLINE = "\n";

        // get the widest line and number of lines in the message
        int longest = getLongest(msg);

        // need to center lines
        // dashes on top (header) and bottom (footer)
        // vertical bars on the sides
        // |-----------------------|
        // | longest line in group |
        // |      other lines      |
        // |-----------------------|
        //
        // pad with an extra space if the length is odd

        int numDashes = (longest + 2) + (longest % 2);  // pad if odd length
        char[] dashes = new char[numDashes];  // header and footer
        char[] spaces = new char[numDashes];  // body lines
        Arrays.fill(dashes, '-');
        Arrays.fill(spaces, ' ');
        String headerFooter = "|" + new String(dashes) + "|\n";
        String spacesStr = "|" + new String(spaces) + "|\n";

        // start the card with the header
        String card = headerFooter;

        // split the message into separate strings
        String[] splitStr = msg.split(NEWLINE);
        for (String s : splitStr) {
              String line = spacesStr;  // start with all spaces

              // create a StringBuilder with all spaces,
              // then replace some spaces with the centered string
              StringBuilder buildLine = new StringBuilder(spacesStr);

              // start at  middle minus half the length of the string (0-based)
              int start = (spacesStr.length() / 2) - (s.length() / 2);
              // end at the starting index plus the length of the string
              int end = start + s.length();
              /// replace the spaces and create a String, then append
              buildLine.replace(start, end, s);
              line = new String(buildLine);
              card += line;
        }
        // append the footer
        card += headerFooter;
        //System.out.println(card); // debug
        return card;
    }

    // given a String containing a (possibly) multi-line message,
    // split the lines, find the longest line, and return its length
    public int getLongest(String s) {
        final String NEWLINE = "\n";
        int maxLength = 0;
        String[] splitStr = s.split(NEWLINE);
        for (String line : splitStr)
            if (line.length() > maxLength)
                maxLength = line.length();
        return maxLength;
    }

    // show prompt msg with no newline
    public static void prompt(String msg) {
        System.out.print(msg + ": ");
    }

    // compare current month and day to user's data
    // to see if it is their birthday
    public boolean isBirthday(User u) {
        boolean result = false;

        LocalDate today = LocalDate.now();
        if (today.getMonth() == u.getBirthday().getMonth() &&
                today.getDayOfMonth() == u.getBirthday().getDayOfMonth())
            result = true;

        return result;
    }

    // add multiple birthdays
    public void addBirthdays(User... users) {
        for (User u : users) {
            birthdays.add(u);
        }
    }

    // main program
    public static void main(String[] args) {

        HappyBirthdayApp hba = new HappyBirthdayApp();

        // list of desired timezones to use for our app, use the US/ zones
        DesiredTimeZones dzt = new DesiredTimeZones();

        // test  varargs method by creating multiple birthdays and adding them.
        // names  generated using http://random-name-generator.info/
        // be sure to test positive case (today is someone's birthday)
        // and negative (today is not their birthday)

        // use current date for testing, adjust where necessary
        ZonedDateTime currentDate = ZonedDateTime.now();

        // negative test
        User u1 = new User("Dianne", "Romero", "Dianne.Romero@email.test",
                currentDate.minusDays(1));

        // positive tests
        // test with odd length full name
        User u2 = new User("Sally", "Roberts", "Sally.Roberts@email.test",
                currentDate);
        // test with even length full name
        User u3 = new User("Edwin", "Peterson", "Edwin.Peterson@email.test",
                currentDate);

          hba.addBirthdays(u1, u2, u3);

        // show the birthdays
        if (!hba.birthdays.isEmpty()) {
            System.out.println("Here are the birthdays:");
            for (User u : hba.birthdays) {
                System.out.println(u.getName() + ":");
                // see if today is their birthday
                if (!hba.isBirthday(u))
                   System.out.println("Sorry, today is not their birthday.\n");
                else
                   hba.sendCard(u);
            }
        }
    }
}

// user class
class User {
    private StringBuilder name;
    private String email;
    private ZonedDateTime birthday;

    public User(String fName, String lName, String email, 
                ZonedDateTime birthday) {
        this.name = new StringBuilder();
        this.name.append(fName).append(" ").append(lName);
        this.email = email;
        this.birthday = birthday;
    }

    public StringBuilder getName() {
        return name;
    }

    public String getEmail() { return email; }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    //public void setBirthday(ZonedDateTime birthday) {
    //    this.birthday = birthday;
    //}

    @Override
    public String toString() {
        String s = this.name + "," + this.birthday;
        return s;
    }
}
