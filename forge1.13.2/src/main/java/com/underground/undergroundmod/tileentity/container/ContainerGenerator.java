package com.underground.undergroundmod.tileentity.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerGenerator extends Container{

	private final IInventory tileGenerator;
	private final World world;
	private int PowerLevel;
	private int PowerMassage;

	public ContainerGenerator(InventoryPlayer playerInventory, IInventory generatorInventory) {
		this.tileGenerator = generatorInventory;
		this.world = playerInventory.player.world;

		this.addSlot(new Slot(generatorInventory, 0, 36, 33));

		for(int i=0; i<3; ++i) {
			for(int j=0; j<9; ++j) {
				this.addSlot(new Slot(playerInventory, j+i*9+9, 8+j*18, 84+i*18));
			}
		}

		for(int k=0; k<9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8+k*18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO 自動生成されたメソッド・スタブ
		return this.tileGenerator.isUsableByPlayer(playerIn);
	}

	@Override
	public void addListener(IContainerListener listener) {
		// TODO 自動生成されたメソッド・スタブ
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileGenerator);
	}

	public void clear() {
		this.tileGenerator.clear();
	}

	@OnlyIn(Dist.CLIENT)
	public int getSize() {
		return 1;
	}

	@Override
	public void detectAndSendChanges() {
		// TODO 自動生成されたメソッド・スタブ
		super.detectAndSendChanges();

		for(IContainerListener icontainerlistener : this.listeners) {
			if (this.PowerLevel != this.tileGenerator.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, this.tileGenerator.getField(0));
			}

			if (this.PowerMassage != this.tileGenerator.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, this.tileGenerator.getField(1));
			}
		}
		this.PowerLevel = tileGenerator.getField(0);
		this.PowerMassage = tileGenerator.getField(1);
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

}
