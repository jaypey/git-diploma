package com.diploma.git.backend;

import com.diploma.git.backend.gitolite.GitoliteManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GitoliteManager gitoliteManager(){
        return new GitoliteManager();
    }
}
