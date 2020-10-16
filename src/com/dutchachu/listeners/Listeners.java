package com.dutchachu.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

import com.dutchachu.Plugin;
import com.dutchachu.HuntingPlayer;

public class Listeners implements Listener 
{
	private Plugin server;

	public Listeners(Plugin server) 
	{
		super();
		this.server = server;
	}

	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
		// Player has just joined, set them up
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		// Default to 'null' for whether or not the player is
		// a hunter, they'll have to choose later with the 'role'
		// command
		HuntingPlayer newPlayer = new HuntingPlayer(player, null);
		this.server.players.put(uuid, newPlayer);
		
        event.setJoinMessage("Fuck you, " + event.getPlayer().getName());
    }
	
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
		// Player quit, remove them from the roles page
		UUID uuid = event.getPlayer().getUniqueId();
		
		// Remove them, so that future games can be started, etc.
		this.server.players.remove(uuid);
    }
	
	@EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
		UUID uuid = event.getPlayer().getUniqueId();
		HuntingPlayer playerInfo = this.server.players.get(uuid);
		
		if (playerInfo == null) 
		{
			return;
		}
		
		// Second value denotes whether or not the player is a hunter
		// and note that the deaths are handled separately, so that
		// when a hunted player dies, they are put in spectator mode
		//
		// If they are, give them a compass
		if (playerInfo.getRole())
		{
			ItemStack[] itemStack = {new ItemStack(Material.COMPASS)};
			event.getPlayer().getInventory().addItem(itemStack);
		}
    }
	
	@EventHandler
    public void onRightClick(PlayerInteractEvent event)
	{
        Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		HuntingPlayer playerInfo = this.server.players.get(uuid);
        
		// Do nothing special if they are a hunted player
		//
		// Explicitly compare to 'false', this is a tri-state field
        if (playerInfo == null || playerInfo.getRole() == false)
        {
        	return;
        }
        
        // Check that the action is correct to not block other actions
        boolean actionGood = (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
        
        // See what they have in their hand, and proceed if it is a compass
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if(actionGood && itemInHand.getType() == Material.COMPASS) 
        {
        	// Find the closest hunted player, and point the compass towards them
            Location playerLocation = player.getLocation();
            
            // Information for the 'closest so far'
            Location closestLocation = null;
            Player closestPlayer = null;
            double shortestDistance = Double.POSITIVE_INFINITY;
            
        	for (HuntingPlayer targetInfo : this.server.players.values()) 
        	{
        		// Skip other hunters
        		if (targetInfo == null || targetInfo.getRole()) 
        		{
        			continue;
        		}
        		
        		// Save if this player is the new closest
        	    Location targetLocation = targetInfo.getPlayer().getLocation();
        	    double distanceToTarget = playerLocation.distance(targetLocation);
        	    if (distanceToTarget < shortestDistance) 
        	    {
        	    	shortestDistance = distanceToTarget;
        	    	closestLocation = targetLocation;
        	    	closestPlayer = targetInfo.getPlayer();
        	    }
        	}

        	if (closestLocation != null && closestPlayer != null) 
        	{
        		// Point compass as closest player
        		player.setCompassTarget(closestLocation);
        		player.sendMessage(String.format("Oh, %s, is so fucked", closestPlayer.getName()));
        	}
        }
    }
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		
		// The player clicked a role on the HuntingGUI screen, process their choice
		if (event.getView().getTitle().equals("Choose a role:") && event.getSlotType() != SlotType.OUTSIDE) 
		{
			UUID uuid = player.getUniqueId();
			HuntingPlayer playerInfo = this.server.players.get(uuid);
			ItemStack clicked = event.getCurrentItem();
			ItemMeta clickedInfo = clicked.getItemMeta();

			if (clickedInfo.getDisplayName().equals("Hunter")) 
			{
				playerInfo.setHunter(true);

				// Give them a compass if they don't have one already
				if (!player.getInventory().contains(Material.COMPASS))
				{
					ItemStack[] itemStack = {new ItemStack(Material.COMPASS)};
					player.getInventory().addItem(itemStack);
				}
				
				player.sendMessage("You are now a hunter, get that ass, kid");
			} 
			else if (clickedInfo.getDisplayName().equals("Hunted")) 
			{
				playerInfo.setHunter(false);
				
				player.sendMessage("You are now being hunted, lube up, son");
			}
			
			event.setCancelled(true);
			player.updateInventory();
			player.closeInventory();
		}
	}
	
	@EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
		Player player = event.getEntity();
		UUID uuid = player.getUniqueId();
		HuntingPlayer playerInfo = this.server.players.get(uuid);
		
		// Do nothing if they die before the game has started
		if (playerInfo == null || !this.server.started) {
			return;
		}
		
		// Player is hunted and died, move them to spectator mode
		if (playerInfo.getRole() == false) {
			playerInfo.getPlayer().setGameMode(GameMode.SPECTATOR);
		}
    }
}