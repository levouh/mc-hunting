package com.dutchachu.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dutchachu.Plugin;
import com.dutchachu.HuntingPlayer;

public class CommandReset implements CommandExecutor 
{
	
	private Plugin server;

	public CommandReset(Plugin server) 
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
		
		// Send all players back to spawn
		Location defaultSpawn = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
		
		for (HuntingPlayer targetInfo : this.server.players.values())
		{
			Player targetPlayer = targetInfo.getPlayer();
			
			// Must choose a role again
			targetInfo.setHunter(null);
			
			// Reset them to the starting state
			targetPlayer.getInventory().clear();
			targetPlayer.getInventory().setArmorContents(null);
			targetPlayer.setLevel(0);
			
			// Hunted will be in spectator mode if they died
			targetPlayer.spigot().respawn();
			
			// Could have a bed, etc.
			targetPlayer.teleport(defaultSpawn);
    	}
		
		// Completely reset
		this.server.started = false;

		return false;
	}
}
