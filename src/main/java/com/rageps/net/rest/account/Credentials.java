package com.rageps.net.rest.account;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

public final class Credentials {
    @SerializedName("member_id")
    private final int id;

    @SerializedName("member_group_id")
    private final int groupId;

    @SerializedName("mgroup_others")
    private final String secondaryGroups;

    @SerializedName("email")
    private final String email;

    @SerializedName("members_pass_hash")
    private final String passwordHash;

    @SerializedName("members_pass_salt")
    private final String passwordSalt;

    @SerializedName("mfa_details")
    private final MultifactorAuthentication authentication;

    public Credentials(int id, int groupId, String secondaryGroups, String email, String passwordHash, String passwordSalt, MultifactorAuthentication authentication) {
        this.id = id;
        this.groupId = groupId;
        this.secondaryGroups = secondaryGroups;
        this.email = email;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.authentication = authentication;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getSecondaryGroups() {
        return secondaryGroups;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public MultifactorAuthentication getAuthentication() {
        return authentication;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("groupId", groupId).add("secondaryGroups", secondaryGroups).add("email", email).add("passwordHash", passwordHash)
                .add("passwordSalt", passwordSalt).add("authentication", authentication).toString();
    }
}
