package com.eclipsesource.json;

import com.eclipsesource.json.JsonWriter;
import com.eclipsesource.json.WriterConfig;
import java.io.Writer;

final class WriterConfig$1
extends WriterConfig {
    WriterConfig$1() {
    }

    @Override
    JsonWriter createWriter(Writer writer) {
        return new JsonWriter(writer);
    }
}
