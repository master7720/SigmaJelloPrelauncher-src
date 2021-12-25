package com.eclipsesource.json;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonHandler;
import com.eclipsesource.json.JsonNumber;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonString;
import com.eclipsesource.json.JsonValue;

class Json$DefaultHandler
extends JsonHandler<JsonArray, JsonObject> {
    protected JsonValue value;

    Json$DefaultHandler() {
    }

    @Override
    public JsonArray startArray() {
        return new JsonArray();
    }

    @Override
    public JsonObject startObject() {
        return new JsonObject();
    }

    @Override
    public void endNull() {
        this.value = NULL;
    }

    @Override
    public void endBoolean(boolean bool) {
        this.value = bool ? TRUE : FALSE;
    }

    @Override
    public void endString(String string) {
        this.value = new JsonString(string);
    }

    @Override
    public void endNumber(String string) {
        this.value = new JsonNumber(string);
    }

    @Override
    public void endArray(JsonArray array) {
        this.value = array;
    }

    @Override
    public void endObject(JsonObject object) {
        this.value = object;
    }

    @Override
    public void endArrayValue(JsonArray array) {
        array.add(this.value);
    }

    @Override
    public void endObjectValue(JsonObject object, String name) {
        object.add(name, this.value);
    }

    JsonValue getValue() {
        return this.value;
    }
}
