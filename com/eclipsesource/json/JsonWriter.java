package com.eclipsesource.json;

import java.io.IOException;
import java.io.Writer;

class JsonWriter {
    private static final int CONTROL_CHARACTERS_END = 31;
    private static final char[] QUOT_CHARS = new char[]{'\\', '\"'};
    private static final char[] BS_CHARS = new char[]{'\\', '\\'};
    private static final char[] LF_CHARS = new char[]{'\\', 'n'};
    private static final char[] CR_CHARS = new char[]{'\\', 'r'};
    private static final char[] TAB_CHARS = new char[]{'\\', 't'};
    private static final char[] UNICODE_2028_CHARS = new char[]{'\\', 'u', '2', '0', '2', '8'};
    private static final char[] UNICODE_2029_CHARS = new char[]{'\\', 'u', '2', '0', '2', '9'};
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected final Writer writer;

    JsonWriter(Writer writer) {
        this.writer = writer;
    }

    protected void writeLiteral(String value) throws IOException {
        this.writer.write(value);
    }

    protected void writeNumber(String string) throws IOException {
        this.writer.write(string);
    }

    protected void writeString(String string) throws IOException {
        this.writer.write(34);
        this.writeJsonString(string);
        this.writer.write(34);
    }

    protected void writeArrayOpen() throws IOException {
        this.writer.write(91);
    }

    protected void writeArrayClose() throws IOException {
        this.writer.write(93);
    }

    protected void writeArraySeparator() throws IOException {
        this.writer.write(44);
    }

    protected void writeObjectOpen() throws IOException {
        this.writer.write(123);
    }

    protected void writeObjectClose() throws IOException {
        this.writer.write(125);
    }

    protected void writeMemberName(String name) throws IOException {
        this.writer.write(34);
        this.writeJsonString(name);
        this.writer.write(34);
    }

    protected void writeMemberSeparator() throws IOException {
        this.writer.write(58);
    }

    protected void writeObjectSeparator() throws IOException {
        this.writer.write(44);
    }

    protected void writeJsonString(String string) throws IOException {
        int length = string.length();
        int start = 0;
        for (int index = 0; index < length; ++index) {
            char[] replacement = JsonWriter.getReplacementChars(string.charAt(index));
            if (replacement == null) continue;
            this.writer.write(string, start, index - start);
            this.writer.write(replacement);
            start = index + 1;
        }
        this.writer.write(string, start, length - start);
    }

    private static char[] getReplacementChars(char ch) {
        if (ch > '\\') {
            if (ch < ' ' || ch > ' ') {
                return null;
            }
            return ch == ' ' ? UNICODE_2028_CHARS : UNICODE_2029_CHARS;
        }
        if (ch == '\\') {
            return BS_CHARS;
        }
        if (ch > '\"') {
            return null;
        }
        if (ch == '\"') {
            return QUOT_CHARS;
        }
        if (ch > '') {
            return null;
        }
        if (ch == '\n') {
            return LF_CHARS;
        }
        if (ch == '\r') {
            return CR_CHARS;
        }
        if (ch == '\t') {
            return TAB_CHARS;
        }
        return new char[]{'\\', 'u', '0', '0', HEX_DIGITS[ch >> 4 & 0xF], HEX_DIGITS[ch & 0xF]};
    }
}
