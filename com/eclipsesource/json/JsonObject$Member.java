package com.eclipsesource.json;

import com.eclipsesource.json.JsonValue;

public class JsonObject$Member {
    private final String name;
    private final JsonValue value;

    JsonObject$Member(String name, JsonValue value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public JsonValue getValue() {
        return this.value;
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + this.name.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }

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
        JsonObject$Member other = (JsonObject$Member)object;
        return this.name.equals(other.name) && this.value.equals(other.value);
    }

    static String access$000(JsonObject$Member x0) {
        return x0.name;
    }

    static JsonValue access$100(JsonObject$Member x0) {
        return x0.value;
    }
}
