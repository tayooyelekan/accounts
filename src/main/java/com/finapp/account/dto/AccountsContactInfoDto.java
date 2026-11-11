package com.finapp.account.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;


@ConfigurationProperties(prefix = "accounts") //This tells Spring Boot to automatically look in your configuration files (like application.properties or application.yml) for any properties that start with the prefix accounts
//It binds those external properties directly to the fields of this class.
public record AccountsContactInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {
    //A record is a compact way to create immutable data classes
    //Because it is a record, the fields (message, contactDetails, onCallSupport) are final.
    //They are set once when Spring creates the bean and cannot be changed during runtime, which is a best practice for configuration data.
}
