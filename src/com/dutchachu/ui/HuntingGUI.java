package com.dutchachu.ui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HuntingGUI 
{

	public static Inventory menu = Bukkit.createInventory(null, 27, "Choose a role:");
	
	public HuntingGUI() 
	{
		
	}
	
	public static boolean openGUI(Player player) 
	{
		// Nice little GUI that looks like an inventory screen
		// where the player can choose a role
		//
		// Show a book to allow the player to choose to be a
		// hunter
		ItemStack hunterOption = new ItemStack(Material.BOOK);
	    ItemMeta hunterMeta = hunterOption.getItemMeta();
	    hunterMeta.setDisplayName("Hunter");
	    hunterOption.setItemMeta(hunterMeta);
	    
	    // Show a book to allow a player to be hunted
		ItemStack huntedOption = new ItemStack(Material.BOOK);
	    ItemMeta huntedMeta = huntedOption.getItemMeta();
	    huntedMeta.setDisplayName("Hunted");
	    huntedOption.setItemMeta(huntedMeta);

	    // Arrange so that all spaces are blank, except
	    // the two books above
	    for (int i = 0; i < 27; i++) 
	    {
	    	if (i == 12) 
	    	{
	    		// Book for hunter
	    		menu.setItem(12, hunterOption);
	    	}
	    	else if (i == 14) 
	    	{
	    		// Book for hunted
	    		menu.setItem(14, huntedOption);
	    	}
	    	else 
	    	{
	    		// Blank space
	    		menu.setItem(i, new ItemStack(Material.AIR, 1));
	    	}
	    }	
		
	    // Now actually show the created GUI
		player.openInventory(menu);
		
		return true;
	}
}
