package pl.konrad.feak_news.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import pl.konrad.feak_news.model.entities.UserEntity;


@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class UserSession {
    private boolean isLogin;
    private String nick;
    private UserEntity.Status status;
}