package com.underground.undergroundmod.item;

import com.underground.undergroundmod.UnderGroundMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class Wand extends Item implements ICommandSource{


	public Wand(Properties properties) {
		super(properties);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) {
			MinecraftServer minecraftserver=worldIn.getServer();
			if(minecraftserver!=null&&minecraftserver.isAnvilFileSet()) {
				try {
					CommandSource commandsource=playerIn.getCommandSource();
					String command="fill ~-15 ~ ~-15 ~15 ~30 ~15 air";
					String command2="fill ~-15 ~-1 ~-15 ~15 ~-30 ~15 stone";
					minecraftserver.getCommandManager().handleCommand(commandsource, command);
					minecraftserver.getCommandManager().handleCommand(commandsource, command2);
				}catch(Throwable throwable){
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Executing test item");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("Command to be executed");

				}
			}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);

	}


	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving) {
		// TODO 自動生成されたメソッド・スタブ

		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);



	}


	@Override
	public void sendMessage(ITextComponent component) {
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public boolean shouldReceiveFeedback() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}


	@Override
	public boolean shouldReceiveErrors() {
		UnderGroundMod.Debag.info("some err");
		return true;
	}


	@Override
	public boolean allowLogging() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
}
