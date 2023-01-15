package com.example.demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Burkhard Graves
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class SessionRegistryMemo implements Serializable {
    
    private static final List<SessionRegistryMemo> list = new ArrayList<>();
    
    public static final List<SessionRegistryMemo> getAndClearDeserializedEntries() {
        final ArrayList<SessionRegistryMemo> ret = new ArrayList<>(list);
        list.clear();
        return ret;
    }
    
    private final String sessionId;
    private final Object principal;

    private void writeObject(ObjectOutputStream out) throws IOException {
        log.info("trying to serialize {}, principal: {}", sessionId, principal);
        out.defaultWriteObject();
        log.info("success");
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        log.info("deserialized {}, principal: {}", sessionId, principal);
        list.add(this);
    }
}
