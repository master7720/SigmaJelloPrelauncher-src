package com.eclipsesource.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonLiteral;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonWriter;
import com.eclipsesource.json.WriterConfig;
import com.eclipsesource.json.WritingBuffer;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

public abstract class JsonValue
implements Serializable {
    @Deprecated
    public static final JsonValue TRUE = new JsonLiteral("true");
    @Deprecated
    public static final JsonValue FALSE = new JsonLiteral("false");
    @Deprecated
    public static final JsonValue NULL = new JsonLiteral("null");

    JsonValue() {
    }

    @Deprecated
    public static JsonValue readFrom(Reader reader) throws IOException {
        return Json.parse(reader);
    }

    @Deprecated
    public static JsonValue readFrom(String text) {
        return Json.parse(text);
    }

    @Deprecated
    public static JsonValue valueOf(int value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(long value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(float value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(double value) {
        return Json.value(value);
    }

    @Deprecated
    public static JsonValue valueOf(String string) {
        return Json.value(string);
    }

    @Deprecated
    public static JsonValue valueOf(boolean value) {
        return Json.value(value);
    }

    public boolean isObject() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isTrue() {
        return false;
    }

    public boolean isFalse() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public JsonObject asObject() {
        throw new UnsupportedOperationException("Not an object: " + this.toString());
    }

    public JsonArray asArray() {
        throw new UnsupportedOperationException("Not an array: " + this.toString());
    }

    public int asInt() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public long asLong() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public float asFloat() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public double asDouble() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public String asString() {
        throw new UnsupportedOperationException("Not a string: " + this.toString());
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException("Not a boolean: " + this.toString());
    }

    public void writeTo(Writer writer) throws IOException {
        this.writeTo(writer, WriterConfig.MINIMAL);
    }

    public void writeTo(Writer writer, WriterConfig config) throws IOException {
        if (writer == null) {
            throw new NullPointerException("writer is null");
        }
        if (config == null) {
            throw new NullPointerException("config is null");
        }
        WritingBuffer buffer = new WritingBuffer(writer, 128);
        this.write(config.createWriter(buffer));
        buffer.flush();
    }

    public String toString() {
        return this.toString(WriterConfig.MINIMAL);
    }

    public String toString(WriterConfig config) {
        StringWriter writer = new StringWriter();
        try {
            this.writeTo(writer, config);
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return writer.toString();
    }

    public boolean equals(Object object) {
        return super.equals(object);
    }

    public int hashCode() {
        return super.hashCode();
    }

    abstract void write(JsonWriter var1) throws IOException;
}
