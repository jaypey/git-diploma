package com.diploma.git.backend.gitolite;

import gitolite.ManagedConfig;
import gitolite.ManagedConfigFactory;
import gitolite.git.GitException;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.IOException;


public class GitoliteManager {

    private ManagedConfig configManager;


    public GitoliteManager() {
        ManagedConfigFactory factory = new ManagedConfigFactory();
        String gitRepoPath = null;
        try {
            gitRepoPath = "ssh://git@" + System.getenv("GIT_SERVER_URL") + "/gitolite-admin";
            configManager = factory.init(gitRepoPath);
        } catch (InterruptedException | IOException | GitException e) {
            System.err.println("Couln't clone admin repository. Repository path: " + gitRepoPath);
        }
    }

    public ManagedConfig getConfigManager(){
        return configManager;
    }
}
