package com.eclipsesource.json;

import com.eclipsesource.json.JsonWriter;
import java.io.IOException;
import java.io.Writer;

class PrettyPrint$PrettyPrintWriter
extends JsonWriter {
    private final char[] indentChars;
    private int indent;

    private PrettyPrint$PrettyPrintWriter(Writer writer, char[] indentChars) {
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
