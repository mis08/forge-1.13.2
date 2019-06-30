package com.underground.undergroundmod.tileentity.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerDecompMachine extends Container{
	private final IInventory tileDecomp;
	private final World world;
	private int cookTime;
	private int totalCookTime;
	private int furnaceBurnTime;
	private int currentItemBurnTime;


	public ContainerDecompMachine(InventoryPlayer playerInventory,IInventory decompInventory) {
		this.tileDecomp = decompInventory;
		this.world = playerInventory.player.world;
		this.addSlot(new Slot(decompInventory, 0, 56, 17));
		this.addSlot(new SlotFurnaceFuel(decompInventory, 1, 56, 53));
		this.addSlot(new SlotFurnaceOutput(playerInventory.player, decompInventory, 2, 116, 35));

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
	public void addListener(IContainerListener listener) {
		// TODO 自動生成されたメソッド・スタブ
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileDecomp);
	}

	public void clear() {
		this.tileDecomp.clear();
	}

	public int getOutputSlot() {
		return 2;
	}

	public int getWidth() {
		return 1;
	}

	public int getHeight() {
		return 1;
	}

	@OnlyIn(Dist.CLIENT)
	public int getSize() {
		return 3;
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for(IContainerListener icontainerlistener : this.listeners) {
			if (this.cookTime != this.tileDecomp.getField(2)) {
				icontainerlistener.sendWindowProperty(this, 2, this.tileDecomp.getField(2));
			}

			if (this.furnaceBurnTime != this.tileDecomp.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, this.tileDecomp.getField(0));
			}

			if (this.currentItemBurnTime != this.tileDecomp.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, this.tileDecomp.getField(1));
			}

			if (this.totalCookTime != this.tileDecomp.getField(3)) {
				icontainerlistener.sendWindowProperty(this, 3, this.tileDecomp.getField(3));
			}
		}

		this.cookTime = this.tileDecomp.getField(2);
		this.furnaceBurnTime = this.tileDecomp.getField(0);
		this.currentItemBurnTime = this.tileDecomp.getField(1);
		this.totalCookTime = this.tileDecomp.getField(3);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO 自動生成されたメソッド・スタブ
		return this.tileDecomp.isUsableByPlayer(playerIn);
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (this.canSmelt(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (TileEntityFurnace.isItemFuel(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 3 && index < 30) {
					if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

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

	private boolean canSmelt(ItemStack p_206253_1_) {
		for(IRecipe irecipe : this.world.getRecipeManager().getRecipes(net.minecraftforge.common.crafting.VanillaRecipeTypes.SMELTING)) {
			if (irecipe.getIngredients().get(0).test(p_206253_1_)) {
				return true;
			}
		}

		return false;
	}

}