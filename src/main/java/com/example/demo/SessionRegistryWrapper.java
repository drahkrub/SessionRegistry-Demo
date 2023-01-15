package com.example.demo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

/**
 * @author Burkhard Graves
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SessionRegistryWrapper {
    
    private final SessionRegistry sessionRegistry;

    public List<SessionInformation> getSessionInformationList() {
        
        return sessionRegistry.getAllPrincipals().stream().map(
                p -> sessionRegistry.getAllSessions(p, false)
        ).flatMap(List::stream)
                .sorted(Comparator.comparing(SessionInformation::getLastRequest).reversed())
                .collect(Collectors.toList());
    }
    
    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        
        final List<SessionRegistryMemo> deserializedEntries
                = SessionRegistryMemo.getAndClearDeserializedEntries();
        
        deserializedEntries.forEach(memo-> sessionRegistry.registerNewSession(
                memo.getSessionId(), memo.getPrincipal()));
        
        log.debug("number of restored SessionInformation entries: {}",
                deserializedEntries.size());
    }
}
