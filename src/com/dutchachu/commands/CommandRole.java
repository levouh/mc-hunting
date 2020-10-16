package com.dutchachu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dutchachu.Plugin;
import com.dutchachu.ui.HuntingGUI;

public class CommandRole implements CommandExecutor 
{

	@SuppressWarnings("unused")
	private Plugin server;

	public CommandRole(Plugin server) 
	{
		super();
		this.server = server;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		// Non-players can't choose a role
		if (!(sender instanceof Player)) 
		{
			sender.sendMessage("This command can only be invoked by a player");
			
			return false;
		}

		// Open the GUI to let them choose a role
		return HuntingGUI.openGUI((Player) sender);
	}
}
