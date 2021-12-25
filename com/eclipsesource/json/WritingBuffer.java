package com.eclipsesource.json;

import java.io.IOException;
import java.io.Writer;

class WritingBuffer
extends Writer {
    private final Writer writer;
    private final char[] buffer;
    private int fill = 0;

    WritingBuffer(Writer writer) {
        this(writer, 16);
    }

    WritingBuffer(Writer writer, int bufferSize) {
        this.writer = writer;
        this.buffer = new char[bufferSize];
    }

    @Override
    public void write(int c) throws IOException {
        if (this.fill > this.buffer.length - 1) {
            this.flush();
        }
        this.buffer[this.fill++] = (char)c;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (this.fill > this.buffer.length - len) {
            this.flush();
            if (len > this.buffer.length) {
                this.writer.write(cbuf, off, len);
                return;
            }
        }
        System.arraycopy(cbuf, off, this.buffer, this.fill, len);
        this.fill += len;
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        if (this.fill > this.buffer.length - len) {
            this.flush();
            if (len > this.buffer.length) {
                this.writer.write(str, off, len);
                return;
            }
        }
        str.getChars(off, off + len, this.buffer, this.fill);
        this.fill += len;
    }

    @Override
    public void flush() throws IOException {
        this.writer.write(this.buffer, 0, this.fill);
        this.fill = 0;
    }

    @Override
    public void close() throws IOException {
    }
}
