package com.underground.undergroundmod.tileentity;

import java.util.Map;

import com.google.common.hash.BloomFilter;
import com.mojang.datafixers.types.templates.List;
import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.block.BlockPowerWire;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityPowerWire extends TileEntity implements ITickable{

	private boolean hasPower;

	private TileEntityGenerator tileentitygenerator;

	public TileEntityPowerWire(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public TileEntityPowerWire() {
		super(UnderGroundMod.TileEntityPowerWire);
	}

	@Override
	public void tick() {
		if(isPowerIn()) {
			this.hasPower = true;
		}else {
			this.hasPower = false;
		}
		this.serchWire();
	}

	public void serchWire() {
		for(int i = 0; i<6; ++i) {
			BlockPos nextpos =null;
			boolean flag = false;
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
			if(bs.getBlock() == UnderGroundMod.BlockPowerWire || bs.getBlock() == UnderGroundMod.BlockDecompMachine || bs.getBlock() == UnderGroundMod.BlockGenerator) {
				flag = true;
			}
			setSideWire(i, flag);
		}

	}

	public boolean isPowerIn() {
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
			if(bs.getBlock() == UnderGroundMod.BlockGenerator) {
				TileEntityGenerator tg = (TileEntityGenerator) world.getTileEntity(nextpos);
				this.tileentitygenerator = tg;
				if(tg.isPowerOn()) {
					return true;
				}
			}else if(bs.getBlock() == UnderGroundMod.BlockPowerWire) {
				TileEntityPowerWire pw = (TileEntityPowerWire) world.getTileEntity(nextpos);
				if(pw.getGenerator() != null) {
					this.tileentitygenerator = pw.getGenerator();
				}
				
				if(pw.getPower() && pw.getGenerator().isPowerOn()) {
					return true;
				}
			}
		}

		this.tileentitygenerator = null;
		this.hasPower =false;
		return false;
	}

	public void setSideWire(int i,boolean flag) {
		switch(i) {
		case 0:
			this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockPowerWire.EAST, Boolean.valueOf(flag)));
			break;
		case 1:
			this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockPowerWire.UP, Boolean.valueOf(flag)));
			break;
		case 2:
			this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockPowerWire.SOUTH, Boolean.valueOf(flag)));
			break;
		case 3:
			this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockPowerWire.WEST, Boolean.valueOf(flag)));
			break;
		case 4:
			this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockPowerWire.DOWN, Boolean.valueOf(flag)));
			break;
		case 5:
			this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockPowerWire.NORTH, Boolean.valueOf(flag)));
		}
	}

	public boolean getPower() {
		return hasPower;
	}


	public TileEntityGenerator getGenerator() {
		return this.tileentitygenerator;
	}

}
