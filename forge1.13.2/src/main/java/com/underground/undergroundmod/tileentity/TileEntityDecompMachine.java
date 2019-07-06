package com.underground.undergroundmod.tileentity;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.underground.undergroundmod.Debug;
import com.underground.undergroundmod.ModIdHolder;
import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.block.BlockDecompMachine;
import com.underground.undergroundmod.irecipe.DecompMachineRecipe;
import com.underground.undergroundmod.tileentity.container.ContainerDecompMachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityDecompMachine extends TileEntity implements ISidedInventory,ITickable,IRecipeHelperPopulator,IRecipeHolder{

	private NonNullList<ItemStack> decompItemStacks = NonNullList.withSize(9, ItemStack.EMPTY);
	private ITextComponent decompCutomName;
	private static final int[] SLOTS_TOP = new int[]{0};
	private static final int[] SLOTS_BOTTOM = new int[]{2, 1};
	private static final int[] SLOTS_SIDES = new int[]{1};

	private ItemStackHandler inventory = new ItemStackHandler(10);
	private Map<ResourceLocation, Integer> recipeUseCounts = Maps.newHashMap();
	private int DecompBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

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
		
		boolean flag= this.isDecomping();
		boolean flag1 = false;
		if(this.isDecomping()) {
//			--this.DecompBurnTime;
		}

		if(!this.world.isRemote) {
	
			if(this.isDecomping() || !this.decompItemStacks.get(0).isEmpty()) {
				IRecipe irecipe = this.world.getRecipeManager().getRecipe(this,this.world,UnderGroundMod.DECOMP);
				if(!this.isDecomping() && this.canDecomp(irecipe)) {
					this.currentItemBurnTime = this.DecompBurnTime;
					if(this.isDecomping()) {
						flag1 = true;
					}
				}
				
				
				if(this.isDecomping() && this.canDecomp(irecipe)) {
					UnderGroundMod.Debag.info("in!!");
					++this.cookTime;
					if(this.cookTime == this.totalCookTime) {
						Debug.text("Finished cook");
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime();
						this.decompItItem(irecipe);
						flag1 = true;
					}
				}else {
					this.cookTime = 0;
				}
			}else if(!this.isDecomping() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime -2, 0, this.totalCookTime);
			}
			
			if(flag != this.isDecomping()) {
				flag1 = true;
				this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockDecompMachine.DECOMP, Boolean.valueOf(this.isDecomping())),3);
			}
		}
		
		if(flag1) {
			this.markDirty();
		}
	}

	private void decompItItem(@Nullable IRecipe recipe) {
		// TODO 自動生成されたメソッド・スタブ
		if(recipe != null && this.canDecomp(recipe)) {
			ItemStack itemstack = this.decompItemStacks.get(0);
			ItemStack itemstack1 = recipe.getRecipeOutput();
			ItemStack itemstack2  = this.decompItemStacks.get(1);
			if(itemstack2.isEmpty()) {
				this.decompItemStacks.set(1, itemstack1.copy());
			}else if(itemstack2.getItem() == itemstack1.getItem()) {
				itemstack2.grow(itemstack1.getCount());
			}
			
			if(!this.world.isRemote) {
				this.canuseRecipe(this.world, (EntityPlayerMP)null, recipe);
			}
			
			itemstack.shrink(1);
		}
	}

	private int getCookTime() {
		// TODO 自動生成されたメソッド・スタブ
		DecompMachineRecipe decomprecipe = this.world.getRecipeManager().getRecipe(this,this.world,UnderGroundMod.DECOMP);
		return decomprecipe != null ? decomprecipe.getCookingTime() : 200;
	}


	private boolean canDecomp(@Nullable IRecipe recipe) {
		// TODO 自動生成されたメソッド・スタブ
		if(!this.decompItemStacks.get(0).isEmpty() && recipe != null) {
			ItemStack itemstack = recipe.getRecipeOutput();
			if(itemstack.isEmpty()) {
				return false;
			}else {
				ItemStack itemstack1 = this.decompItemStacks.get(1);
				if(itemstack1.isEmpty()) {
					return true;
				}else if(!itemstack.isItemEqual(itemstack)) {
					return false;
				}else if(itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() < itemstack1.getMaxStackSize()) {
					return true;
				}else {
					return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
				}
			}
		}else {
			return false;
		}
	}

	private boolean isDecomping() {
		// TODO 自動生成されたメソッド・スタブ
//		return this.DecompBurnTime >0;
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	private static boolean isDecomping(IInventory inventory) {
		return inventory.getField(0)> 0;
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
		if(index==0 && !flag) {
			this.totalCookTime = this.getCookTime();
			this.cookTime = 0;
			this.markDirty();
		}

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
		switch(id) {
			case 0:
				return this.DecompBurnTime;
			case 1:
				return this.currentItemBurnTime;
			case 2:
				return this.cookTime;
			case 3:
				return this.totalCookTime;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		// TODO 自動生成されたメソッド・スタブ
		switch(id) {
			case 0:
				this.DecompBurnTime = value;
				break;
			case 1:
				this.currentItemBurnTime = value;
				break;
			case 2:
				this.cookTime = value;
				break;
			case 3:
				this.totalCookTime = value;
		}

	}

	@Override
	public int getFieldCount() {
		// TODO 自動生成されたメソッド・スタブ
		return 4;
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

	public void setRecipeUsed(IRecipe recipe) {
		if(this.recipeUseCounts.containsKey(recipe.getId())) {
			this.recipeUseCounts.put(recipe.getId(), this.recipeUseCounts.get(recipe.getId()) + 1);
		}else {
			this.recipeUseCounts.put(recipe.getId(), 1);
		}
	}

	@Nullable
	public IRecipe getRecipeUsed() {
		return null;
	}

	public boolean canuseRecipe(World worldIn, EntityPlayerMP player, @Nullable IRecipe recipe) {
		if(recipe != null) {
			this.setRecipeUsed(recipe);
			return true;
		}else {
			return false;
		}
	}

	public void onCrafting(EntityPlayer player) {
		if(!this.world.getGameRules().getBoolean("doLimitedCrafting"));{
			List<IRecipe> list = Lists.newArrayList();

			for(ResourceLocation resoucelocation : this.recipeUseCounts.keySet()) {
				IRecipe irecipe = player.world.getRecipeManager().getRecipe(resoucelocation);
				if(irecipe != null) {
					list.add(irecipe);
				}
			}

			player.unlockRecipes(list);
		}
		this.recipeUseCounts.clear();
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

	public Map<ResourceLocation,Integer> getRecipeUseCounts(){
		return this.recipeUseCounts;
	}

	@Override
	public void fillStackedContents(RecipeItemHelper helper) {
		// TODO 自動生成されたメソッド・スタブ
		for(ItemStack itemstack : this.decompItemStacks) {
			helper.accountStack(itemstack);
		}
		
	}

}
