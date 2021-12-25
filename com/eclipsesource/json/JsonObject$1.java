package com.eclipsesource.json;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.Iterator;

class JsonObject$1
implements Iterator<JsonObject.Member> {
    final Iterator val$namesIterator;
    final Iterator val$valuesIterator;

    JsonObject$1(Iterator iterator, Iterator iterator2) {
        this.val$namesIterator = iterator;
        this.val$valuesIterator = iterator2;
    }

    @Override
    public boolean hasNext() {
        return this.val$namesIterator.hasNext();
    }

    @Override
    public JsonObject.Member next() {
        String name = (String)this.val$namesIterator.next();
        JsonValue value = (JsonValue)this.val$valuesIterator.next();
        return new JsonObject.Member(name, value);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
