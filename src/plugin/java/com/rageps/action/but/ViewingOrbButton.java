package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;

public class ViewingOrbButton extends ActionInitializer {
	
	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("Centre", 15239, player.getViewingOrb().getCentre());
				return true;
			}
		};
		e.register(59135);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("North-West", 15240, player.getViewingOrb().getNorthWest());
				return true;
			}
		};
		e.register(59136);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("North-East", 15241, player.getViewingOrb().getNorthEast());
				return true;
			}
		};
		e.register(59137);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("South-East", 15242, player.getViewingOrb().getSouthEast());
				return true;
			}
		};
		e.register(59138);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("South-West", 15243, player.getViewingOrb().getSouthWest());
				return true;
			}
		};
		e.register(59139);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getViewingOrb() != null) {
					player.getViewingOrb().close();
					player.setViewingOrb(null);
				}
				return true;
			}
		};
		e.register(17111);
	}
	
}
