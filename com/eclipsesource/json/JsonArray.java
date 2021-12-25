package com.eclipsesource.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JsonArray
extends JsonValue
implements Iterable<JsonValue> {
    private final List<JsonValue> values;

    public JsonArray() {
        this.values = new ArrayList<JsonValue>();
    }

    public JsonArray(JsonArray array) {
        this(array, false);
    }

    private JsonArray(JsonArray array, boolean unmodifiable) {
        if (array == null) {
            throw new NullPointerException("array is null");
        }
        this.values = unmodifiable ? Collections.unmodifiableList(array.values) : new ArrayList<JsonValue>(array.values);
    }

    @Deprecated
    public static JsonArray readFrom(Reader reader) throws IOException {
        return JsonValue.readFrom(reader).asArray();
    }

    @Deprecated
    public static JsonArray readFrom(String string) {
        return JsonValue.readFrom(string).asArray();
    }

    public static JsonArray unmodifiableArray(JsonArray array) {
        return new JsonArray(array, true);
    }

    public JsonArray add(int value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(long value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(float value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(double value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(boolean value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(String value) {
        this.values.add(Json.value(value));
        return this;
    }

    public JsonArray add(JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.values.add(value);
        return this;
    }

    public JsonArray set(int index, int value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(int index, long value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(int index, float value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(int index, double value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(int index, boolean value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(int index, String value) {
        this.values.set(index, Json.value(value));
        return this;
    }

    public JsonArray set(int index, JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.values.set(index, value);
        return this;
    }

    public JsonArray remove(int index) {
        this.values.remove(index);
        return this;
    }

    public int size() {
        return this.values.size();
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public JsonValue get(int index) {
        return this.values.get(index);
    }

    public List<JsonValue> values() {
        return Collections.unmodifiableList(this.values);
    }

    @Override
    public Iterator<JsonValue> iterator() {
        final Iterator<JsonValue> iterator = this.values.iterator();
        return new Iterator<JsonValue>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonValue next() {
                return (JsonValue)iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    void write(JsonWriter writer) throws IOException {
        writer.writeArrayOpen();
        Iterator<JsonValue> iterator = this.iterator();
        if (iterator.hasNext()) {
            iterator.next().write(writer);
            while (iterator.hasNext()) {
                writer.writeArraySeparator();
                iterator.next().write(writer);
            }
        }
        writer.writeArrayClose();
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public JsonArray asArray() {
        return this;
    }

    @Override
    public int hashCode() {
        return this.values.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        JsonArray other = (JsonArray)object;
        return this.values.equals(other.values);
    }
}
