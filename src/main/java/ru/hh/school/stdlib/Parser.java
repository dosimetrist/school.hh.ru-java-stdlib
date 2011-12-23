package ru.hh.school.stdlib;

import java.util.*;
import java.util.regex.*;

public class Parser {
    public String parse(String input, HashMap<String, String> map) {
        String result = input;
        Pattern p = Pattern.compile("\\$\\{([a-zA-Z0-9]*)\\}");
        Matcher m = p.matcher(input);

        while (m.find()) {

            String temp = m.group(1);
            String token = "";
            if (map.containsKey(temp)) token = map.get(temp);
            result  = m.replaceFirst(token);
            m = p.matcher(result);
        }

        return result;



    }
}
