package com.underground.undergroundmod.tileentity;

import com.underground.undergroundmod.UnderGroundMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGenerator extends TileEntity implements ISidedInventory{

	private NonNullList<ItemStack> generatorItemStacks = NonNullList.withSize(1, ItemStack.EMPTY);

	public TileEntityGenerator(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public TileEntityGenerator() {
		super(UnderGroundMod.TileEntityGenerator);
	}

	@Override
	public int getSizeInventory() {
		// TODO 自動生成されたメソッド・スタブ
		return generatorItemStacks.size();
	}

	@Override
	public boolean isEmpty() {
		// TODO 自動生成されたメソッド・スタブ
		for(ItemStack itemStack : this.generatorItemStacks) {
			if(!itemStack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO 自動生成されたメソッド・スタブ
		return this.generatorItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO 自動生成されたメソッド・スタブ
		return ItemStackHelper.getAndSplit(this.generatorItemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO 自動生成されたメソッド・スタブ
		return ItemStackHelper.getAndRemove(this.generatorItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		this.generatorItemStacks.set(index, stack);
		if(stack.getCount()>this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public int getField(int id) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public int getFieldCount() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void clear() {
		// TODO 自動生成されたメソッド・スタブ

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
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,
			EnumFacing direction) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,
			EnumFacing direction) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
