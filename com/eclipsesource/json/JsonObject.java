package com.eclipsesource.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JsonObject
extends JsonValue
implements Iterable<Member> {
    private final List<String> names;
    private final List<JsonValue> values;
    private transient HashIndexTable table;

    public JsonObject() {
        this.names = new ArrayList<String>();
        this.values = new ArrayList<JsonValue>();
        this.table = new HashIndexTable();
    }

    public JsonObject(JsonObject object) {
        this(object, false);
    }

    private JsonObject(JsonObject object, boolean unmodifiable) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        if (unmodifiable) {
            this.names = Collections.unmodifiableList(object.names);
            this.values = Collections.unmodifiableList(object.values);
        } else {
            this.names = new ArrayList<String>(object.names);
            this.values = new ArrayList<JsonValue>(object.values);
        }
        this.table = new HashIndexTable();
        this.updateHashIndex();
    }

    @Deprecated
    public static JsonObject readFrom(Reader reader) throws IOException {
        return JsonValue.readFrom(reader).asObject();
    }

    @Deprecated
    public static JsonObject readFrom(String string) {
        return JsonValue.readFrom(string).asObject();
    }

    public static JsonObject unmodifiableObject(JsonObject object) {
        return new JsonObject(object, true);
    }

    public JsonObject add(String name, int value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(String name, long value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(String name, float value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(String name, double value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(String name, boolean value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(String name, String value) {
        this.add(name, Json.value(value));
        return this;
    }

    public JsonObject add(String name, JsonValue value) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.table.add(name, this.names.size());
        this.names.add(name);
        this.values.add(value);
        return this;
    }

    public JsonObject set(String name, int value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(String name, long value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(String name, float value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(String name, double value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(String name, boolean value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(String name, String value) {
        this.set(name, Json.value(value));
        return this;
    }

    public JsonObject set(String name, JsonValue value) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        int index = this.indexOf(name);
        if (index != -1) {
            this.values.set(index, value);
        } else {
            this.table.add(name, this.names.size());
            this.names.add(name);
            this.values.add(value);
        }
        return this;
    }

    public JsonObject remove(String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        int index = this.indexOf(name);
        if (index != -1) {
            this.table.remove(index);
            this.names.remove(index);
            this.values.remove(index);
        }
        return this;
    }

    public boolean contains(String name) {
        return this.names.contains(name);
    }

    public JsonObject merge(JsonObject object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        for (Member member : object) {
            this.set(member.name, member.value);
        }
        return this;
    }

    public JsonValue get(String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        int index = this.indexOf(name);
        return index != -1 ? this.values.get(index) : null;
    }

    public int getInt(String name, int defaultValue) {
        JsonValue value = this.get(name);
        return value != null ? value.asInt() : defaultValue;
    }

    public long getLong(String name, long defaultValue) {
        JsonValue value = this.get(name);
        return value != null ? value.asLong() : defaultValue;
    }

    public float getFloat(String name, float defaultValue) {
        JsonValue value = this.get(name);
        return value != null ? value.asFloat() : defaultValue;
    }

    public double getDouble(String name, double defaultValue) {
        JsonValue value = this.get(name);
        return value != null ? value.asDouble() : defaultValue;
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        JsonValue value = this.get(name);
        return value != null ? value.asBoolean() : defaultValue;
    }

    public String getString(String name, String defaultValue) {
        JsonValue value = this.get(name);
        return value != null ? value.asString() : defaultValue;
    }

    public int size() {
        return this.names.size();
    }

    public boolean isEmpty() {
        return this.names.isEmpty();
    }

    public List<String> names() {
        return Collections.unmodifiableList(this.names);
    }

    @Override
    public Iterator<Member> iterator() {
        final Iterator<String> namesIterator = this.names.iterator();
        final Iterator<JsonValue> valuesIterator = this.values.iterator();
        return new Iterator<Member>(){

            @Override
            public boolean hasNext() {
                return namesIterator.hasNext();
            }

            @Override
            public Member next() {
                String name = (String)namesIterator.next();
                JsonValue value = (JsonValue)valuesIterator.next();
                return new Member(name, value);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    void write(JsonWriter writer) throws IOException {
        writer.writeObjectOpen();
        Iterator<String> namesIterator = this.names.iterator();
        Iterator<JsonValue> valuesIterator = this.values.iterator();
        if (namesIterator.hasNext()) {
            writer.writeMemberName(namesIterator.next());
            writer.writeMemberSeparator();
            valuesIterator.next().write(writer);
            while (namesIterator.hasNext()) {
                writer.writeObjectSeparator();
                writer.writeMemberName(namesIterator.next());
                writer.writeMemberSeparator();
                valuesIterator.next().write(writer);
            }
        }
        writer.writeObjectClose();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public JsonObject asObject() {
        return this;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.names.hashCode();
        result = 31 * result + this.values.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        JsonObject other = (JsonObject)obj;
        return this.names.equals(other.names) && this.values.equals(other.values);
    }

    int indexOf(String name) {
        int index = this.table.get(name);
        if (index != -1 && name.equals(this.names.get(index))) {
            return index;
        }
        return this.names.lastIndexOf(name);
    }

    private synchronized void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        this.table = new HashIndexTable();
        this.updateHashIndex();
    }

    private void updateHashIndex() {
        int size = this.names.size();
        for (int i = 0; i < size; ++i) {
            this.table.add(this.names.get(i), i);
        }
    }

    static class HashIndexTable {
        private final byte[] hashTable = new byte[32];

        HashIndexTable() {
        }

        HashIndexTable(HashIndexTable original) {
            System.arraycopy(original.hashTable, 0, this.hashTable, 0, this.hashTable.length);
        }

        void add(String name, int index) {
            int slot = this.hashSlotFor(name);
            this.hashTable[slot] = index < 255 ? (byte)(index + 1) : (byte)0;
        }

        void remove(int index) {
            for (int i = 0; i < this.hashTable.length; ++i) {
                if ((this.hashTable[i] & 0xFF) == index + 1) {
                    this.hashTable[i] = 0;
                    continue;
                }
                if ((this.hashTable[i] & 0xFF) <= index + 1) continue;
                int n = i;
                this.hashTable[n] = (byte)(this.hashTable[n] - 1);
            }
        }

        int get(Object name) {
            int slot = this.hashSlotFor(name);
            return (this.hashTable[slot] & 0xFF) - 1;
        }

        private int hashSlotFor(Object element) {
            return element.hashCode() & this.hashTable.length - 1;
        }
    }

    public static class Member {
        private final String name;
        private final JsonValue value;

        Member(String name, JsonValue value) {
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
            Member other = (Member)object;
            return this.name.equals(other.name) && this.value.equals(other.value);
        }
    }
}
