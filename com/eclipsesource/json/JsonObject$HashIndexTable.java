package com.eclipsesource.json;

class JsonObject$HashIndexTable {
    private final byte[] hashTable = new byte[32];

    JsonObject$HashIndexTable() {
    }

    JsonObject$HashIndexTable(JsonObject$HashIndexTable original) {
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
