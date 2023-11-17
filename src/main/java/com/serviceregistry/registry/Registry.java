package com.serviceregistry.registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serviceregistry.framework.util.JsonUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Registry {

    private Map<String, List<RegistryElement>> registryMap;
    private boolean initialized;
    private final ScheduledExecutorService scheduler;
    private final Logger logger = Logger.getLogger(Registry.class.getName());
    private final Random random = new Random();

    public Registry() {
        this.registryMap = new ConcurrentHashMap<>();
        this.initialized = false;
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void init() {
        initialized = true;
        scheduler.scheduleAtFixedRate(this::evictElements, 3, 3, TimeUnit.MINUTES);
    }

    public void registerServiceUrl(String serviceName, String url) {
        if (!initialized) throw new RuntimeException("Registry not initialized!");
        LocalDateTime dateTime = LocalDateTime.now();
        if (Objects.isNull(registryMap.get(serviceName))) {
            registryMap.put(serviceName, new ArrayList<>());
        }
        RegistryElement registryElement = new RegistryElement(url, dateTime);
        registryMap.get(serviceName).removeIf(x -> x.url.equals(url));
        registryMap.get(serviceName).add(registryElement);
    }

    public String getRegistryJSON() throws JsonProcessingException {
        return JsonUtil.toJson(registryMap);
    }

    public String getServiceUrl(String serviceName) {
        if (!initialized) throw new RuntimeException("Registry not initialized!");
        List<RegistryElement> registryElements = registryMap.get(serviceName);
        if (Objects.isNull(registryElements) || registryElements.size() == 0) return null;
        // randomly return one url
        return registryElements.get(random.nextInt(registryElements.size())).getUrl();
    }

    private void evictElements() {
        if (!initialized) {
            logger.warning("Eviction cannot be performed unless logger is initialized. Manual eviction is not recommended.");
            return;
        }
        logger.info("Evicting dead services");
        LocalDateTime now = LocalDateTime.now();
        registryMap.values().forEach(registryElements -> registryElements.removeIf(e -> {
            long seconds = ChronoUnit.SECONDS.between(e.dateTime, now);
            return seconds > 120;
        }));
        registryMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public static class RegistryElement implements Comparable {
        private String url;
        private LocalDateTime dateTime;

        public RegistryElement(String url, LocalDateTime dateTime) {
            this.url = url;
            this.dateTime = dateTime;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof RegistryElement)) throw new RuntimeException("Only comparable with RegistryElement!");
            return this.dateTime.compareTo(((RegistryElement) o).dateTime);
        }
    }



}
