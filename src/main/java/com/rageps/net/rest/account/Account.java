package com.rageps.net.rest.account;

import com.google.common.base.MoreObjects;
import com.rageps.util.Predicates;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Account {

    private final String username;

    private final Credentials credentials;

    private final MemberGroup primaryGroup;

    private final Set<MemberGroup> secondaryGroups;

    public Account(String username, Credentials credentials, MemberGroup primaryGroup, Set<MemberGroup> groups) {
        this.username = username;
        this.credentials = credentials;
        this.primaryGroup = primaryGroup;
        this.secondaryGroups = groups;
    }

    public static Account fromCredentials(String username, Credentials credentials) {
        Set<MemberGroup> groups = Stream.of(credentials.getSecondaryGroups().split(","))
                .filter(Predicates.NOT_EMPTY)
                .mapToInt(Integer::parseInt)
                .mapToObj(MemberGroup::fromId)
                .collect(Collectors.toSet());
        return new Account(username, credentials, MemberGroup.fromId(credentials.getGroupId()), groups);
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public String getUsername() {
        return username;
    }

    public MemberGroup getPrimaryGroup() {
        return primaryGroup;
    }

    public Set<MemberGroup> getSecondaryGroups() {
        return secondaryGroups;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("credentials", credentials)
                .add("primaryGroup", primaryGroup)
                .add("secondaryGroups", secondaryGroups)
                .toString();
    }
}
