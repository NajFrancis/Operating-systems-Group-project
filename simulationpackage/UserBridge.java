package simulationpackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import member4.User;

/*
 * member4.User keeps username, user_ID, group_assigned and its Scanner private and has no getters,
 * so Main cannot ask it who just logged in. This class reads those fields without changing her code.
 * If member4.User ever gets getUsername()/getGroup()/getUserId(), this whole class can be deleted.
 */
public class UserBridge {
    private UserBridge() {
    }

    private static Object read(User user, String fieldName) {
        try {
            Field f = User.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(user);
        } catch (Exception e) {
            return null;
        }
    }

    /* Her register()/login() read from this Scanner, so Main must share it (two Scanners on System.in lose input). */
    public static Scanner scannerOf(User user) {
        Object s = read(user, "input");
        return (s instanceof Scanner) ? (Scanner) s : new Scanner(System.in);
    }

    public static String usernameOf(User user) {
        Object v = read(user, "username");
        return v == null ? "unknown" : v.toString().trim();
    }

    public static int idOf(User user) {
        Object v = read(user, "user_ID");
        return (v instanceof Integer) ? (Integer) v : -1;
    }

    /*
     * Returns "Admin", "Standard", or null if it cannot be found.
     * register() fills in group_assigned, but login() never loads it, so for a returning user the group
     * is looked up in the files register() wrote. password.txt and group.txt get one line per registration,
     * in the same order, so the matching line number is used (user IDs are random 0-15 and can repeat).
     */
    public static String groupOf(User user) {
        Object g = read(user, "group_assigned");
        if (g != null && !g.toString().trim().isEmpty()) {
            return g.toString().trim();
        }
        return lookupGroup(usernameOf(user), idOf(user));
    }

    private static String lookupGroup(String username, int id) {
        List<String> passwords = lines("password.txt");
        List<String> groups = lines("group.txt");
        for (int i = 0; i < passwords.size() && i < groups.size(); i++) {
            String[] p = passwords.get(i).split(" ");
            if (p.length == 3 && p[0].equals(username) && p[1].equals(String.valueOf(id))) {
                String[] g = groups.get(i).split(" ");
                if (g.length == 2) return g[1];
            }
        }
        return null;
    }

    private static List<String> lines(String fileName) {
        List<String> result = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) result.add(line);
            reader.close();
        } catch (IOException e) {
            // missing or unreadable file: return what we have
        }
        return result;
    }
}
