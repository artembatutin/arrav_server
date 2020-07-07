package com.rageps.net.rest.payments;

import com.google.common.collect.Sets;
import com.rageps.world.entity.actor.player.assets.group.DonatorPrivilege;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Ryley Kimmel on 11/28/2016.
 */
public enum PaymentTier {
	ELDER(15, 49, DonatorPrivilege.ELDER),
	ALPHA(50, 199, DonatorPrivilege.ALPHA),
	ROYAL(200, 599, DonatorPrivilege.ROYAL),
	DIVINE(600, 999, DonatorPrivilege.DIVINE),
	LEGENDARY(1000, 1499, DonatorPrivilege.LEGENDARY),
	RENOWNED(1500, 1999, DonatorPrivilege.RENOWNED),
	CELESTIAL(2000, Long.MAX_VALUE, DonatorPrivilege.CELESTIAL);

	public static final Set<PaymentTier> ALL = Sets.immutableEnumSet(EnumSet.allOf(PaymentTier.class));

	private final long minimumAmount;

	private final long maximumAmount;

	private final DonatorPrivilege rights;

	PaymentTier(long minimumAmount, long maximumAmount, DonatorPrivilege rights) {
		this.minimumAmount = minimumAmount;
		this.maximumAmount = maximumAmount;
		this.rights = rights;
	}

	public long getMinimumAmount() {
		return minimumAmount;
	}

	public long getMaximumAmount() {
		return maximumAmount;
	}

	public DonatorPrivilege getRights() {
		return rights;
	}

	public PaymentTier next() {
		int index = ordinal() + 1;
		PaymentTier[] paymentTiers = values();
		if (index < 0 || index >= paymentTiers.length) {
			return null;
		}
		return paymentTiers[index];
	}
}
