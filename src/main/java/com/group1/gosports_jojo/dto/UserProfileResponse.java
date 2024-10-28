package com.group1.gosports_jojo.dto;

public class UserProfileResponse {
    private String username;
    private Integer interestsTag;
    private String email;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getInterestsTag() {
        return interestsTag;
    }

    public void setInterestsTag(Integer interestsTag) {
        this.interestsTag = interestsTag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
