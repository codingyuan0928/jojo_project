package com.group1.gosports_jojo.dto;

public class UserProfileUpdateRequest {
    private String username;
    private byte[] avatar;
    private Integer interestsTag;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Integer getInterestsTag() {
        return interestsTag;
    }

    public void setInterestsTag(Integer interestsTag) {
        this.interestsTag = interestsTag;
    }
}

