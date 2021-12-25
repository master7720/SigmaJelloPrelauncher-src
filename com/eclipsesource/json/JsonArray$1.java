package com.eclipsesource.json;

import com.eclipsesource.json.JsonValue;
import java.util.Iterator;

class JsonArray$1
implements Iterator<JsonValue> {
    final Iterator val$iterator;

    JsonArray$1(Iterator iterator) {
        this.val$iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return this.val$iterator.hasNext();
    }

    @Override
    public JsonValue next() {
        return (JsonValue)this.val$iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
