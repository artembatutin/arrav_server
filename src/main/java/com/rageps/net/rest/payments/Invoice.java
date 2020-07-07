package com.rageps.net.rest.payments;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ryley Kimmel on 12/3/2017.
 */
public final class Invoice {
    @SerializedName("id")
    private final int invoiceId;

    @SerializedName("username")
    private final String username;

    @SerializedName("item")
    private final String itemName;

    @SerializedName("amount")
    private final int itemAmount;

    @SerializedName("cost")
    private final String cost;

    @SerializedName("ip")
    private final String ipAddress;

    @SerializedName("timestamp")
    private final String timestamp;

    @SerializedName("redeem")
    private final int redeemed;

    public Invoice(int invoiceId, String username, String itemName, int itemAmount, String cost, String ipAddress, String timestamp, int redeemed) {
        this.invoiceId = invoiceId;
        this.username = username;
        this.itemName = itemName;
        this.itemAmount = itemAmount;
        this.cost = cost;
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
        this.redeemed = redeemed;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCost() {
        return cost;
    }

    public int getRedeemed() {
        return redeemed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("invoiceId", invoiceId)
                .add("username", username)
                .add("itemName", itemName)
                .add("itemAmount", itemAmount)
                .add("cost", cost)
                .add("ipAddress", ipAddress)
                .add("timestamp", timestamp)
                .add("redeemed", redeemed)
                .toString();
    }
}
