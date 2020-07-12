package com.rageps.net.rest.account;

import com.google.common.base.MoreObjects;
import com.rageps.util.Predicates;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a players account and their credentials stored on the forums.
 * @author Tamatea <tamateea@gmail.com>
 */
public final class ForumAccount {

    private final String username;

    private final ForumCredentials forumCredentials;

    private final MemberGroup primaryGroup;

    private final Set<MemberGroup> secondaryGroups;

    public ForumAccount(String username, ForumCredentials forumCredentials, MemberGroup primaryGroup, Set<MemberGroup> groups) {
        this.username = username;
        this.forumCredentials = forumCredentials;
        this.primaryGroup = primaryGroup;
        this.secondaryGroups = groups;
    }

    public static ForumAccount fromCredentials(String username, ForumCredentials forumCredentials) {
        Set<MemberGroup> groups = Stream.of(forumCredentials.getSecondaryGroups().split(","))
                .filter(Predicates.NOT_EMPTY)
                .mapToInt(Integer::parseInt)
                .mapToObj(MemberGroup::fromId)
                .collect(Collectors.toSet());
        return new ForumAccount(username, forumCredentials, MemberGroup.fromId(forumCredentials.getGroupId()), groups);
    }

    public ForumCredentials getForumCredentials() {
        return forumCredentials;
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
                .add("credentials", forumCredentials)
                .add("primaryGroup", primaryGroup)
                .add("secondaryGroups", secondaryGroups)
                .toString();
    }
}
