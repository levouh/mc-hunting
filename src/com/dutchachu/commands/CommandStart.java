package com.dutchachu.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.dutchachu.Plugin;
import com.dutchachu.HuntingPlayer;

public class CommandStart implements CommandExecutor 
{
	
	private Plugin server;

	public CommandStart(Plugin server) 
	{
		super();
		this.server = server;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		// Can also happen from the server-side, so check to see if the
		// sender of the command is a player, and if so, they must be an operator
		//
		// Otherwise whoever is controlling the server can do w/e they want
		if ((sender instanceof Player) && !(sender.isOp())) 
		{
			sender.sendMessage("This command can only be invoked by a server operator");
			
			return false;
		}
		
		if (this.server.started) 
		{
			sender.sendMessage("A game has already been started, use '/reset'");
			
			return false;
		}
		
		// Ensure all players are on one of the two available teams
		for (HuntingPlayer playerInfo : this.server.players.values()) 
		{
			if (playerInfo == null || playerInfo.getRole() == null) {
				sender.sendMessage("Not all players have chosen a role");
				
				return false;
			}
    	}
		
		// 20 ticks per second, lasts 60s
		//
		// Make jump do nothing, and hunters are slowed down
		PotionEffect slownessEffect = new PotionEffect(PotionEffectType.SLOW, 60 * 20, 5);
		PotionEffect jumpEffect = new PotionEffect(PotionEffectType.JUMP, 60 * 20, 100000);
		Location defaultSpawn = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
		
		for (HuntingPlayer playerInfo : this.server.players.values()) {
			Player targetPlayer = playerInfo.getPlayer();
			
			// Apply potion effect to hunters
			if (playerInfo.getRole()) {
				targetPlayer.addPotionEffect(slownessEffect);
				targetPlayer.addPotionEffect(jumpEffect);
			}
			
			// Clear player, and move them back to the starting location
			targetPlayer.spigot().respawn();
			targetPlayer.teleport(defaultSpawn);
    	}

		// Note that the game has started
		this.server.started = true;
		
		return false;
	}
}
