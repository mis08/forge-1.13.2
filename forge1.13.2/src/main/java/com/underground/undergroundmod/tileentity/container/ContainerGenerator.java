package com.underground.undergroundmod.tileentity.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public class ContainerGenerator extends Container{

	private final IInventory tileGenerator;
	private final World world;
	
	public ContainerGenerator(InventoryPlayer playerInventory, IInventory generatorInventory) {
		this.tileGenerator = generatorInventory;
		this.world = playerInventory.player.world;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO 自動生成されたメソッド・スタブ
		return this.tileGenerator.isUsableByPlayer(playerIn);
	}

}
