package at.breitenfellner.roomquestions.util;

import java.util.HashMap;

/**
 * This class provides integer ID values for string keys.
 */

public class SimpleKeyIdSource implements KeyIdSource {
    private HashMap<String, Long> idMap;
    private long lastId = 0L;

    public SimpleKeyIdSource() {
        idMap = new HashMap<>();
    }

    public long getId(String key) {
        Long id = idMap.get(key);
        if (id == null) {
            id = lastId++;
            idMap.put(key, id);
        }
        return id;
    }
}
