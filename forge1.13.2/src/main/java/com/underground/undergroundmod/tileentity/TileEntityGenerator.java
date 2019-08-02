package com.underground.undergroundmod.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mojang.datafixers.types.templates.List;
import com.underground.undergroundmod.Debug;
import com.underground.undergroundmod.UnderGroundMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityGenerator extends TileEntity implements ISidedInventory,ITickable{

	private NonNullList<ItemStack> generatorItemStacks = NonNullList.withSize(2, ItemStack.EMPTY);
	private ITextComponent generatorCustomName;

	private static final int[] SLOTS_TOP = new int[]{0};
	private static final int[] SLOTS_BOTTOM = new int[]{2, 1};
	private static final int[] SLOTS_SIDES = new int[]{1};

	private int PowerLevel;
	private int PowerMassage;
	private int PowerCount;
	private java.util.List<BlockPos> conectList = new ArrayList<BlockPos>();
	public TileEntityGenerator(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public TileEntityGenerator() {
		super(UnderGroundMod.TileEntityGenerator);
	}

	@Override
	public void tick() {
		// TODO 自動生成されたメソッド・スタブ
		ItemStack itemstack = generatorItemStacks.get(0);
		if(itemstack.getItem() == UnderGroundMod.PowerCell) {
			this.PowerLevel = itemstack.getDamage();
			this.PowerMassage = 1;
		}else if(generatorItemStacks.get(0).isEmpty()){
			this.PowerLevel = 0;
			this.PowerMassage = 0;
		}
		
		this.serchConecter();
		this.sendPower();
		
		
	}
	
	public void serchConecter() {
		for(int i = 0; i<6; ++i) {
			BlockPos nextpos =null;
			switch(i) {
			case 0:
				nextpos = new BlockPos(this.pos.getX()+1, this.pos.getY(), this.pos.getZ());
				break;
			case 1:
				nextpos = new BlockPos(this.pos.getX(), this.pos.getY()+1, this.pos.getZ());
				break;
			case 2:
				nextpos = new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()+1);
				break;
			case 3:
				nextpos = new BlockPos(this.pos.getX()-1, this.pos.getY(), this.pos.getZ());
				break;
			case 4:
				nextpos = new BlockPos(this.pos.getX(), this.pos.getY()-1, this.pos.getZ());
				break;
			case 5:
				nextpos = new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()-1);
				break;
			}
			IBlockState bs = this.world.getBlockState(nextpos);
			if(bs.getBlock() == UnderGroundMod.BlockPowerWire) {
				if(!conectList.contains(nextpos)) {
					conectList.add(nextpos);
				}
			}
		}

	}
	
	public boolean isRecivershasPower(int powerLevel) {
		this.serchNullConection();
		for(Iterator<BlockPos> it = this.conectList.iterator(); it.hasNext();) {
			TileEntityPowerWire pw = (TileEntityPowerWire) this.world.getTileEntity(it.next());
			if(pw.getPower() > powerLevel){
				return true;
			}
		}
		return false;
	}
	
	public void serchNullConection() {
		for(Iterator<BlockPos> it = this.conectList.iterator(); it.hasNext();) {
			if(this.world.getBlockState(it.next()).getBlock() != UnderGroundMod.BlockPowerWire ) {
				it.remove();
			}
		}
	}
	
	public void sendPower() {
		if(!isRecivershasPower(50)) {
			for(Iterator<BlockPos> it = this.conectList.iterator(); it.hasNext();) {
				TileEntityPowerWire pw = (TileEntityPowerWire) this.world.getTileEntity(it.next());
				pw.addPower(20);
				this.powerSetDamage(20);
			}
		}
	}

	public boolean isPowerOn() {
		return !generatorItemStacks.get(0).isEmpty();
	}

	public void powerSetDamage(int count) {
		ItemStack itemstack = generatorItemStacks.get(0);
			if(PowerCount > count) {
				itemstack.setDamage(itemstack.getDamage() + 1);
				PowerCount = 0;
			}else {
				PowerCount++;
			}

		
		if(itemstack.getDamage() > itemstack.getMaxDamage()) {
			generatorItemStacks.set(0, ItemStack.EMPTY);
		}
	}
	

	public void debug() {
		Debug.text(generatorItemStacks.get(0).toString());
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, this.generatorItemStacks);
		compound.setInt("PowerLevel", this.PowerLevel);
		compound.setInt("PowerMassage", this.PowerMassage);

		return compound;
	}

	@Override
	public void read(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.read(compound);
		this.generatorItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.generatorItemStacks);
		this.PowerLevel = compound.getInt("PowerLevel");
		this.PowerMassage = compound.getInt("PowerMassage");
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
		this.markDirty();
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
		switch (id) {
			case 0:
				return this.PowerLevel;
			case 1:
				return this.PowerMassage;
			default :
				return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		// TODO 自動生成されたメソッド・スタブ
		switch (id) {
			case 0:
				this.PowerLevel = value;
				break;
			case 1:
				this.PowerMassage = value;
		}

	}

	@Override
	public int getFieldCount() {
		// TODO 自動生成されたメソッド・スタブ
		return 2;
	}

	@Override
	public void clear() {
		// TODO 自動生成されたメソッド・スタブ
		this.generatorItemStacks.clear();
	}

	@Override
	public ITextComponent getName() {
		// TODO 自動生成されたメソッド・スタブ
		return (ITextComponent)(this.generatorCustomName != null ? this.generatorCustomName : new TextComponentTranslation("GENERATOR"));
	}

	@Override
	public boolean hasCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return this.generatorCustomName !=null;
	}

	@Override
	public ITextComponent getCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return this.generatorCustomName;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if(side == EnumFacing.DOWN) {
			return SLOTS_BOTTOM;
		}else {
			return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,
			EnumFacing direction) {
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

	
	


}
