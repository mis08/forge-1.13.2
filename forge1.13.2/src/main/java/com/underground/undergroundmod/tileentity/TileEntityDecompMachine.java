package com.underground.undergroundmod.tileentity;

import java.util.Map;

import javax.annotation.Nullable;

import com.underground.undergroundmod.ModIdHolder;
import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.tileentity.container.ContainerDecompMachine;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityDecompMachine extends TileEntity implements ISidedInventory,ITickable{

	private NonNullList<ItemStack> decompItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
	private ITextComponent decompCutomName;
	private static final int[] SLOTS_TOP = new int[]{0};
	private static final int[] SLOTS_BOTTOM = new int[]{2, 1};
	private static final int[] SLOTS_SIDES = new int[]{1};
	
	private ItemStackHandler inventory = new ItemStackHandler(3);

	public TileEntityDecompMachine(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public TileEntityDecompMachine() {
		super(UnderGroundMod.TileEntityDecompMachine);
	}


	public void debug() {
		UnderGroundMod.Debag.info("Conext TileEntity!");
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, this.decompItemStacks);
		return compound;
	}
	
	@Override
	public void read(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.read(compound);
		this.decompItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.decompItemStacks);
	}


	@Override
	public void tick() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public int getSizeInventory() {
		// TODO 自動生成されたメソッド・スタブ
		return this.decompItemStacks.size();
	}

	@Override
	public boolean isEmpty() {
		// TODO 自動生成されたメソッド・スタブ
		for(ItemStack itemstack:this.decompItemStacks) {
			if(!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO 自動生成されたメソッド・スタブ
		return this.decompItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO 自動生成されたメソッド・スタブ
		return ItemStackHelper.getAndSplit(this.decompItemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO 自動生成されたメソッド・スタブ
		return ItemStackHelper.getAndRemove(this.decompItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		ItemStack itemstack =this.decompItemStacks.get(index);
		boolean flag =!stack.isEmpty() && stack.isItemEqual(itemstack) && itemstack.areItemStackTagsEqual(stack, itemstack);
		this.decompItemStacks.set(index, stack);
		if(stack.getCount()>this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		//		焼く処理？
		//		if(index==0 && !flag) {
		//	         this.totalCookTime = this.getCookTime();
		//	         this.cookTime = 0;
		//	         this.markDirty();
		//		}

	}

	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ
		if(this.world.getTileEntity(this.pos)!=this) {
			return false;
		}else {
			return !(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
		}
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
		this.decompItemStacks.clear();
	}

	@Override
	public ITextComponent getName() {
		// TODO 自動生成されたメソッド・スタブ
		return (ITextComponent)(this.decompCutomName != null ? this.decompCutomName : new TextComponentTranslation("container.decomp"));
	}

	@Override
	public boolean hasCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return this.decompCutomName != null;
	}

	@Override
	public ITextComponent getCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return this.decompCutomName;
	}

	public void setCustomName(@Nullable ITextComponent name) {
		this.decompCutomName = name;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO 自動生成されたメソッド・スタブ
		if(side == EnumFacing.DOWN) {
			return SLOTS_BOTTOM;
		}else {
			return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,
			@Nullable	EnumFacing direction) {
		// TODO 自動生成されたメソッド・スタブ
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,
			EnumFacing direction) {
		// TODO 自動生成されたメソッド・スタブ
		if(direction == EnumFacing.DOWN && index ==1) {
			Item item =stack.getItem();
			if(item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}
		return true;
	}

	public String getGuiID() {
		return ModIdHolder.MODID+":decompgui";
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerDecompMachine(playerInventory, this);
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

}
