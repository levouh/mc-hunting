package com.dutchachu;

import com.dutchachu.commands.CommandRole;
import com.dutchachu.commands.CommandStart;
import com.dutchachu.listeners.Listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin 
{
	private static Plugin instance;
	
	// All players on the server, in a HashMap where the key
	// is a UUID that identifies each player, and the value is
	//   Pair<Player, boolean>
	// where the Player represents a player (omg), and the boolean
	// value is 'false' if they are hunted, and 'true' if they are
	// a hunter.
	public HashMap<UUID, HuntingPlayer> players = new HashMap<UUID, HuntingPlayer>();
	
	// Note that a game has started, obviously this is in-memory state, so if a server
	// is shut down, this will be incorrect.
	public boolean started = false;

	public static Plugin getInstance() 
	{
		return instance;
	}
	
	@Override
	public void onEnable() 
	{
		instance = this;
		
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		
		// Commands here must be further added to 'plugin.yml'
		//
		// Allow the calling player to set their role with the GUI 
		this.getCommand("role").setExecutor(new CommandRole(this));
		
		// Only call-able by OP players, and will fail if not all players
		// have chosen their role
		//
		// Start the round, give the hunters a debuf for a minute
		this.getCommand("start").setExecutor(new CommandStart(this));
		
		// Only call-able by OP players
		//
		// Respawn all players, clear inventory/levels/roles
		this.getCommand("reset").setExecutor(new CommandStart(this));
	}
}