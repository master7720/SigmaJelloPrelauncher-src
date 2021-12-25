package com.eclipsesource.json;

import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonWriter;
import java.io.IOException;

class JsonString
extends JsonValue {
    private final String string;

    JsonString(String string) {
        if (string == null) {
            throw new NullPointerException("string is null");
        }
        this.string = string;
    }

    @Override
    void write(JsonWriter writer) throws IOException {
        writer.writeString(this.string);
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String asString() {
        return this.string;
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
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
        JsonString other = (JsonString)object;
        return this.string.equals(other.string);
    }
}
