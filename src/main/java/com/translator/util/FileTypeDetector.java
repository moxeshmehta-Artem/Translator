package com.translator.util;

public class FileTypeDetector {
    public static String detect(String payload) {
        if (payload == null)
            return "UNKNOWN";
        String trimmed = payload.trim();

        // Handle quoted payloads
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).replace("\\n", "\n").replace("\\r", "\r").trim();
        }

        if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
            return "JSON";
        }
        // HL7 messages start with MSH segment
        if (trimmed.startsWith("MSH")) {
            return "HL7";
        }
        return "UNKNOWN";
    }
}
