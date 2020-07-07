package com.rageps.net.rest.payments;


import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.rageps.net.rest.RestfulNexus.NEXUS;
import static com.rageps.world.entity.actor.player.PlayerAttributes.REDEEMING;

/**
 * Created by Ryley Kimmel on 12/3/2017
 */
public final class Transaction {
    private static final Products PRODUCTS = new Products();
    private static final ExecutorService service = Executors.newSingleThreadExecutor();

    public static void query(Player player) {
        if (!player.getLocation().canReceiveItemRewards()) {
            player.message("You cannot redeem payments in this area.");
            return;
        }
        if(player.getAttributeMap().getBoolean(REDEEMING)) {
            player.message("Please wait for your first request to finish!");
            return;
        }

        player.getAttributeMap().set(REDEEMING, true);

        CompletableFuture.runAsync(() -> {
            try {
                Invoice[] invoices = NEXUS.getInvoices(player.credentials.username);
                if (invoices.length == 0) {
                    player.message("It does not appear that you have made a purchase.");
                    return;
                }

                for (Invoice invoice : invoices) {
                    // If for whatever reason this invoice was already redeemed, skip...
                    if (invoice.getRedeemed() == 1) {
                        continue;
                    }

                    // Post to the Nexus that this invoice has been redeemed
                    NEXUS.redeemInvoice(player.credentials.username, invoice.getInvoiceId());

                    // Attempt to receive this product
                    PRODUCTS.receive(player, new Product(invoice.getItemName(), invoice.getItemAmount()));
                }

                double total = NEXUS.getTotalSpent(player.credentials.username);

                for (PaymentTier paymentTier : PaymentTier.ALL) {
                    /*if (total >= paymentTier.getMinimumAmount() && total <= paymentTier.getMaximumAmount()) {
                        DonatorPrivilege privilege = paymentTier.getRights();
                        if (player.getDonatorPrivilege().isInferior(privilege)) {
                            player.setDonatorPrivilege(privilege);
                            player.addCrownIfAbsent(privilege.getCrown());
                            player.getPacketSender().sendRights();
                            player.message("Thank you for your purchase! You have reached a new tier, " + paymentTier.getRights().name() + "!");
                            break;
                        }
                    }*/
                }

                /*DialogueManager.start(player, new OptionDialogue("Yes, notify the world of my purchase.", "No, do NOT notify the world of my purchase.") {
                    @Override
                    public void option(Player player, int option) {
                        player.getPacketSender().sendInterfaceRemoval();
                        if (option == 1) {
                            String image = "<img=" + (player.getCrownId() - 1) + "> ";
                            World.broadcast(
                                    "[" + ChatColor.RED + "Store</col>]: " + image + player.getUsername() + " has just made a purchase from the store. Thanks for the support!");
                        }
                    }
                });*/
            } catch (Exception cause) {
                cause.printStackTrace();
                player.message("An unexpected error occurred, please try again later.");
            }
        }, service).thenAccept(__ ->  player.getAttributeMap().reset(REDEEMING));
    }

}
