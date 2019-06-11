package com.underground.undergroundmod.entity;

import com.underground.undergroundmod.UnderGroundMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.datafix.fixes.WolfCollarColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityLaser extends EntityBullet{

	public EntityLaser(EntityType<?> type,World wordln, World worldIn) {
		super(type,wordln);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.pickupStatus = PickupStatus.DISALLOWED;
		this.damage = 2.0D;
		this.setSize(0.5F, 0.5F);
		this.worldIn=worldIn;
	}

	public EntityLaser(World worldIn)
	{
		super(UnderGroundMod.EntityLaser, worldIn);
		this.worldIn=worldIn;

	}

	public EntityLaser(EntityType<?> type, double x, double y, double z, World worldIn) {
		super(worldIn);
		this.setPosition(x, y, z);
		this.worldIn=worldIn;
	}

	public EntityLaser(EntityType<?> type, EntityLivingBase shooter, World worldIn){
		this(type, shooter.posX+0.7D, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ,worldIn);
		this.func_212361_a(shooter);
		this.worldIn=worldIn;

		if (shooter instanceof EntityPlayer)
		{
			this.pickupStatus = EntityBullet.PickupStatus.ALLOWED;
		}
	}
	
	   public EntityLaser(World worldIn2, EntityLivingBase shooter) {
		   this(EntityType.ARROW,shooter,worldIn2);
	}
	
	   @Override
	   protected void onHit(RayTraceResult raytraceResultIn) {
	      if (raytraceResultIn.entity != null) {
	         this.onHitEntity(raytraceResultIn);
	      } else {
	         BlockPos blockpos = raytraceResultIn.getBlockPos();
	         this.xTile = blockpos.getX();
	         this.yTile = blockpos.getY();
	         this.zTile = blockpos.getZ();
	         IBlockState iblockstate = this.world.getBlockState(blockpos);
	         iblockstate.dropBlockAsItem(worldIn, blockpos, 0);
	         iblockstate.removedByPlayer(getEntityWorld(),blockpos, null,true,iblockstate.getFluidState() );
	         this.inBlockState = iblockstate;
//	         this.motionX = (double)((float)(raytraceResultIn.hitVec.x - this.posX));
//	         this.motionY = (double)((float)(raytraceResultIn.hitVec.y - this.posY));
//	         this.motionZ = (double)((float)(raytraceResultIn.hitVec.z - this.posZ));
//	         float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) * 20.0F;
//	         this.posX -= this.motionX / (double)f;
//	         this.posY -= this.motionY / (double)f;
//	         this.posZ -= this.motionZ / (double)f;
	         //this.playSound(this.getHitGroundSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	         //this.worldIn.createExplosion(this, this.posX, this.posY, this.posZ, 3F, true);
	         //this.remove();
//	         this.inGround = true;
//	         this.arrowShake = 7;
//	         this.setIsCritical(false);
	         if (!iblockstate.isAir(world, blockpos)) {
	            this.inBlockState.onEntityCollision(this.world, blockpos, this);
	         }
	      }
//	      this.remove();
	   }

}
