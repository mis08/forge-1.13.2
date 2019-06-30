package com.underground.undergroundmod.tileentity.interactionobject;

import com.underground.undergroundmod.ModIdHolder;
import com.underground.undergroundmod.tileentity.TileEntityDecompMachine;
import com.underground.undergroundmod.tileentity.container.ContainerDecompMachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;

public class InteractionObjectDecompMachine implements IInteractionObject{

	private final TileEntityDecompMachine dm;

	public InteractionObjectDecompMachine(TileEntityDecompMachine dm) {
		this.dm=dm;
	}
	@Override
	public ITextComponent getName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public ITextComponent getCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory,
			EntityPlayer playerIn) {
		// TODO 自動生成されたメソッド・スタブ
		return new ContainerDecompMachine(playerInventory, dm);
	}

	@Override
	public String getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return ModIdHolder.MODID + ":decompgui";
	}

}
