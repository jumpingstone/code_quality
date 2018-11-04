package com.jumpingstone.codequality.fireeye.service.model;

/**
 * Created by chenwei on 2018/11/3.
 */
public class GitProjectDefinition {
    private String gitRepo;
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getGitRepo() {
        return gitRepo;
    }

    public void setGitRepo(String gitRepo) {
        this.gitRepo = gitRepo;
    }
}
