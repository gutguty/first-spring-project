package ru.gazprom.stockservice.users;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app")
public class AllowedUsers {
    private List<String> allowedUsers;
}
