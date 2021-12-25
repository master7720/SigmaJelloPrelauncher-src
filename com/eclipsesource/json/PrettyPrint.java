package com.eclipsesource.json;

import com.eclipsesource.json.JsonWriter;
import com.eclipsesource.json.WriterConfig;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class PrettyPrint
extends WriterConfig {
    private final char[] indentChars;

    protected PrettyPrint(char[] indentChars) {
        this.indentChars = indentChars;
    }

    public static PrettyPrint singleLine() {
        return new PrettyPrint(null);
    }

    public static PrettyPrint indentWithSpaces(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number is negative");
        }
        char[] chars = new char[number];
        Arrays.fill(chars, ' ');
        return new PrettyPrint(chars);
    }

    public static PrettyPrint indentWithTabs() {
        return new PrettyPrint(new char[]{'\t'});
    }

    @Override
    protected JsonWriter createWriter(Writer writer) {
        return new PrettyPrintWriter(writer, this.indentChars);
    }

    private static class PrettyPrintWriter
    extends JsonWriter {
        private final char[] indentChars;
        private int indent;

        private PrettyPrintWriter(Writer writer, char[] indentChars) {
            super(writer);
            this.indentChars = indentChars;
        }

        @Override
        protected void writeArrayOpen() throws IOException {
            ++this.indent;
            this.writer.write(91);
            this.writeNewLine();
        }

        @Override
        protected void writeArrayClose() throws IOException {
            --this.indent;
            this.writeNewLine();
            this.writer.write(93);
        }

        @Override
        protected void writeArraySeparator() throws IOException {
            this.writer.write(44);
            if (!this.writeNewLine()) {
                this.writer.write(32);
            }
        }

        @Override
        protected void writeObjectOpen() throws IOException {
            ++this.indent;
            this.writer.write(123);
            this.writeNewLine();
        }

        @Override
        protected void writeObjectClose() throws IOException {
            --this.indent;
            this.writeNewLine();
            this.writer.write(125);
        }

        @Override
        protected void writeMemberSeparator() throws IOException {
            this.writer.write(58);
            this.writer.write(32);
        }

        @Override
        protected void writeObjectSeparator() throws IOException {
            this.writer.write(44);
            if (!this.writeNewLine()) {
                this.writer.write(32);
            }
        }

        private boolean writeNewLine() throws IOException {
            if (this.indentChars == null) {
                return false;
            }
            this.writer.write(10);
            for (int i = 0; i < this.indent; ++i) {
                this.writer.write(this.indentChars);
            }
            return true;
        }
    }
}
