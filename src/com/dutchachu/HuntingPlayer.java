package com.dutchachu;

import org.bukkit.entity.Player;

public class HuntingPlayer
{

    private final Player player;
    
    // Use class Boolean so we can have tri-state
    private Boolean isHunter;

    public HuntingPlayer(Player player, Boolean isHunter)
    {
    	this.player = player;
    	this.isHunter = isHunter;
	}

    public Player getPlayer()
    {
        return player;
    }

    public Boolean getRole()
    {
    	// 'true': Player is hunter
    	// 'false': Player is hunted
    	// 'null': Player hasn't chosen a role
        return isHunter;
    }
    
    public void setHunter(Boolean value)
    {
    	this.isHunter = value;
    }

}
