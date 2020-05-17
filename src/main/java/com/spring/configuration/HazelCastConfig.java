package com.spring.configuration;

import com.hazelcast.config.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.spring.model.Employee;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HazelCastConfig {
    private String configName = "group1";

    public <E> IList<E> getList(String listName) {
        HazelcastInstance instance = HazelCast.instance(configName);
        return instance.getList(listName);
    }
}