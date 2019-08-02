package com.underground.undergroundmod.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.underground.undergroundmod.Debug;
import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.block.BlockPowerWire;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityPowerWire extends TileEntity implements ITickable{

	private List<BlockPos> conectList = new ArrayList<>();

	public TileEntityPowerWire(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public TileEntityPowerWire() {
		super(UnderGroundMod.TileEntityPowerWire);
	}

	@Override
	public void tick() {
		this.addMySelfPos();
		this.serchPowerSorce();
		this.serchWire();
		
	}


	public void serchPowerSorce() {
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
				TileEntityPowerWire pw = (TileEntityPowerWire) world.getTileEntity(nextpos);
				if(!this.conectList.contains(nextpos)) {
					this.conectList.add(nextpos);
					pw.addPower4newWire(nextpos, this.getPower());
				}
				this.setConectList(pw);
				if(pw.getPower() < this.getPower()) {
					this.world.getBlockState(nextpos).with(BlockPowerWire.POWER, this.getPower());
				}
			}
		}

	}


	//接続可能なブロックを探す
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

	//指定されたブロックにくっつく
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



	public void addPower(int power) {
		this.serchNullConection();
		for(Iterator<BlockPos> it = this.conectList.iterator(); it.hasNext();) {
			TileEntityPowerWire pw =(TileEntityPowerWire) this.world.getTileEntity(it.next());
			pw.addPowerB(power);
		}
	}

	public void addPowerB(int power) {
		Integer in = this.getPower() + power;
		this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockPowerWire.POWER, in < 100 ? in : 100));
	}



	public void usePower(int power) {
		this.serchNullConection();
		for(Iterator<BlockPos> it = this.conectList.iterator(); it.hasNext();) {
			TileEntityPowerWire pw =(TileEntityPowerWire) this.world.getTileEntity(it.next());
			pw.usePowerB(power);
		}

	}

	//usePowerのためのパーツ
	public void usePowerB(int power) {
		Integer in = this.getPower() - power;
		this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockPowerWire.POWER, in > 0 ? in : 0));
	}

	public int getPower() {
		return this.world.getBlockState(this.pos).get(BlockPowerWire.POWER);
	}

	public void setConectList(TileEntityPowerWire pw) {
		for(Iterator<BlockPos> it = pw.conectList.iterator(); it.hasNext();) {
			BlockPos blockpos = it.next();
			if(!this.conectList.contains(blockpos)) {
				this.conectList.add(blockpos);
			}
		}
	}

	public void serchNullConection() {
		for(Iterator<BlockPos> it = this.conectList.iterator(); it.hasNext();) {
			BlockPos bs = it.next();
			if(this.world.getBlockState(bs).getBlock() != UnderGroundMod.BlockPowerWire ) {
				it.remove();
			}
		}
	}

	public void addPower4newWire(BlockPos bs,int powerLevel) {
		TileEntityPowerWire pw = (TileEntityPowerWire) this.world.getTileEntity(bs);
		pw.addPowerB(powerLevel);
	}
	
	public void addMySelfPos() {
		if(!this.conectList.contains(this.pos)) {
			this.conectList.add(this.pos);
		}
	}
	


}
