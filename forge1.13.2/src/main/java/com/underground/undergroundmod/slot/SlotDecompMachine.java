package com.underground.undergroundmod.slot;

import java.util.Map.Entry;

import com.underground.undergroundmod.irecipe.DecompMachineRecipe;
import com.underground.undergroundmod.tileentity.TileEntityDecompMachine;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeHidden;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.hooks.BasicEventHooks;

public class SlotDecompMachine extends Slot{
	private final EntityPlayer player;
	private int removeCount;


	public SlotDecompMachine(EntityPlayer player, IInventory inventoryIn, int slotindex, int xPosition,
			int yPosition) {
		super(inventoryIn, slotindex, xPosition, yPosition);
		this.player = player;
		// TODO 自動生成されたコンストラクター・スタブ
	}


	//アイテムをこのスロットに置けるか
	@Override
	public boolean isItemValid(ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		// TODO 自動生成されたメソッド・スタブ
		if(this.getHasStack()) {
			this.removeCount += Math.min(amount, this.getStack().getCount());
		}
		return super.decrStackSize(amount);
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		this.onCrafting(stack);
		super.onTake(thePlayer, stack);
		return stack;
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		// TODO 自動生成されたメソッド・スタブ
		this.removeCount += amount;
		super.onCrafting(stack);
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.player.world, this.player, this.removeCount);
		if(!this.player.world.isRemote) {
			for(Entry<ResourceLocation,Integer> entry : ((TileEntityDecompMachine)this.inventory).getRecipeUseCounts().entrySet()) {
				DecompMachineRecipe decomprecipe = (DecompMachineRecipe)this.player.world.getRecipeManager().getRecipe(entry.getKey());
				float f;
				if(decomprecipe != null) {
					f = decomprecipe.getExperience();
				}else {
					f = 0.0F;
				}

				int i = entry.getValue();
				if(f == 0.0F) {
					i = 0;
				}else if(f < 1.0F){
					int j = MathHelper.floor((float)i * f);
					if(j < MathHelper.ceil((float)i * f )&& Math.random() < (double)((float)i * f -(float)j)){
						++j;
					}
					
					i = j;
 				}
				
				while(i > 0) {
					int k = EntityXPOrb.getXPSplit(i);
					i -= k;
					this.player.world.spawnEntity(new EntityXPOrb(this.player.world,this.player.posX,this.player.posY + 0.5D,this.player.posZ + 0.5D,k));
				}
			}
			
			//実績解除？
			((IRecipeHolder)this.inventory).onCrafting(this.player);
		}
		
		this.removeCount = 0;
		
		//たぶんいらない
//		BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
	}


}
