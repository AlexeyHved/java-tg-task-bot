package com.github.alexeyhved.taskbot.dto;

public class TxtMapper {
    public static String coverSymbols(String s) {
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : chars) {
            if (ch == '.' || ch == '_' || ch == '*' || ch == '[' || ch == ']' || ch == '(' || ch == ')' || ch == '~'
                    || ch == '`' || ch == '>' || ch == '#' || ch == '+' || ch == '-' || ch == '=' || ch == '|'
                    || ch == '{' || ch == '}' || ch == '!') {
                sb.append("\\").append(ch);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
