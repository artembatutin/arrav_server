package com.rageps.net.sql.forum.account;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

public final class MultifactorAuthentication {
    @SerializedName("google")
    private final String secret;

    public MultifactorAuthentication(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("secret", secret).toString();
    }

}
