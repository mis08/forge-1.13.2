package com.underground.undergroundmod.entity;

import com.underground.undergroundmod.UnderGroundMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBullet extends EntityArrow{

	private int xTile;
	private int yTile;
	private int zTile;
	private IBlockState inBlockState;
	private int knockbackStrength;
	private int ticksInAir;
	private World worldIn;
	private double damage;


	public EntityBullet(EntityType<?> type, World worldIn) {
		super(type, worldIn);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.pickupStatus = PickupStatus.DISALLOWED;
		this.damage = 2.0D;
		this.setSize(0.5F, 0.5F);
		this.worldIn=worldIn;
	}

	
	public EntityBullet(World worldIn)
    {
        super(UnderGroundMod.EntityBullet, worldIn);
        this.worldIn=worldIn;
       
    }

	public EntityBullet(EntityType<?> type, double x, double y, double z, World worldIn) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.worldIn=worldIn;
    }

    public EntityBullet(EntityType<?> type, EntityLivingBase shooter, World worldIn)
    {
        this(type, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ,worldIn);
        this.func_212361_a(shooter);
        this.worldIn=worldIn;

        if (shooter instanceof EntityPlayer)
        {
            this.pickupStatus = EntityBullet.PickupStatus.ALLOWED;
        }
    }

	   
	   public EntityBullet(World worldIn2, EntityPlayer shooter) {
		   this(EntityType.ARROW,shooter,worldIn2);
	}


	/**
	    * Called when the arrow hits a block or an entity
	    */
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
	         this.inBlockState = iblockstate;
	         this.motionX = (double)((float)(raytraceResultIn.hitVec.x - this.posX));
	         this.motionY = (double)((float)(raytraceResultIn.hitVec.y - this.posY));
	         this.motionZ = (double)((float)(raytraceResultIn.hitVec.z - this.posZ));
	         float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) * 20.0F;
	         this.posX -= this.motionX / (double)f;
	         this.posY -= this.motionY / (double)f;
	         this.posZ -= this.motionZ / (double)f;
	         //this.playSound(this.getHitGroundSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	         //this.worldIn.createExplosion(this, this.posX, this.posY, this.posZ, 3F, true);
	         //this.remove();
	         this.inGround = true;
	         this.arrowShake = 7;
	         this.setIsCritical(false);
	         if (!iblockstate.isAir(world, blockpos)) {
	            this.inBlockState.onEntityCollision(this.world, blockpos, this);
	         }
	      }
	      this.remove();
	   }

	   protected void onHitEntity(RayTraceResult p_203046_1_) {
	      Entity entity = p_203046_1_.entity;
	      float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
	      int i = MathHelper.ceil((double)f * this.getDamage());
	      if (this.getIsCritical()) {
	         i += this.rand.nextInt(i / 2 + 2);
	      }

	      Entity entity1 = this.func_212360_k();
	      DamageSource damagesource;
	      if (entity1 == null) {
	         damagesource = DamageSource.causeArrowDamage(this, this);
	      } else {
	         damagesource = DamageSource.causeArrowDamage(this, entity1);
	      }

	      if (this.isBurning() && !(entity instanceof EntityEnderman)) {
	         entity.setFire(5);
	      }

	      if (entity.attackEntityFrom(damagesource, (float)i)) {
	         if (entity instanceof EntityLivingBase) {
	            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
	            if (!this.world.isRemote) {
	               entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
	            }

	            if (this.knockbackStrength > 0) {
	               float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
	               if (f1 > 0.0F) {
	                  entitylivingbase.addVelocity(this.motionX * (double)this.knockbackStrength * (double)0.6F / (double)f1, 0.1D, this.motionZ * (double)this.knockbackStrength * (double)0.6F / (double)f1);
	               }
	            }

	            if (entity1 instanceof EntityLivingBase) {
	               EnchantmentHelper.applyThornEnchantments(entitylivingbase, entity1);
	               EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)entity1, entitylivingbase);
	            }

	            this.arrowHit(entitylivingbase);
	            if (entity1 != null && entitylivingbase != entity1 && entitylivingbase instanceof EntityPlayer && entity1 instanceof EntityPlayerMP) {
	               ((EntityPlayerMP)entity1).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
	            }
	         }
	         //this.worldIn.createExplosion(this, this.posX, this.posY, this.posZ, 3F, true);
	         //this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	         
	         if (!(entity instanceof EntityEnderman)) {
	            this.remove();
	         }
	      } else {
	    	  /*矢の跳ね返り処理を無効化
	         this.motionX *= (double)-0.1F;
	         this.motionY *= (double)-0.1F;
	         this.motionZ *= (double)-0.1F;
	         this.rotationYaw += 180.0F;
	         this.prevRotationYaw += 180.0F;
	         this.ticksInAir = 0;
	         */
	         if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < (double)0.001F) {
	            if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED) {
	               this.entityDropItem(this.getArrowStack(), 0.1F);
	            }

	           //this.remove();
	         }
	      }
	      this.remove();
	   }



	protected ItemStack getArrowStack() {
		return new ItemStack(UnderGroundMod.ExpArrow);
	}
}
