package com.underground.undergroundmod.entity;

import javax.annotation.Nullable;

import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.block.BlockAlloy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.fixes.WolfCollarColor;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceFluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModLoader;

public class EntityLaser extends EntityArrow{

	private boolean dealtDamage;
	public int returningTicks;
	private int xTile;
	private int yTile;
	private int zTile;
	private double damage;
	private World worldIn;
	private IBlockState inBlockState;
	private int ticksInGround;
	private int ticksInAir;
	private EntityLivingBase player;
	
	//破壊の種類
	private Block[] onceDestroy={UnderGroundMod.BlockAlloy};
	private Block[] cantDestroy= {Blocks.BEDROCK};
	private Block[] needException= {Blocks.TNT};
	
	private int knockbackStrength;

	public EntityLaser(EntityType<?> type,World wordln, World worldIn) {
		super(type,wordln);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.pickupStatus = PickupStatus.DISALLOWED;
		this.setDamage(1D);
		this.setSize(0.5F, 0.5F);
		this.worldIn=worldIn;
	}

	public EntityLaser(World worldIn)
	{
		super(UnderGroundMod.EntityLaser, worldIn);
		this.worldIn=worldIn;

	}

	public EntityLaser(EntityType<?> type, double x, double y, double z, World worldIn) {
		this(worldIn);
		this.setPosition(x, y, z);
		this.worldIn=worldIn;
	}

	public EntityLaser(EntityType<?> type, EntityLivingBase shooter, World worldIn){
		this(type, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ,worldIn);
		this.func_212361_a(shooter);
		this.worldIn=worldIn;

		if (shooter instanceof EntityPlayer)
		{
			this.pickupStatus = EntityBullet.PickupStatus.ALLOWED;
		}
	}

	public EntityLaser(World worldIn2, EntityLivingBase shooter) {
		this(EntityType.ARROW,shooter,worldIn2);
		this.setNoGravity(true);
		this.player=shooter;
		
	}

	@Override
	public void shoot(double x, double y, double z, float velocity,float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double)f;
		y = y / (double)f;
		z = z / (double)f;
		//射弾の散布処理コメントアウト
		//    x = x + this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy;
		//    y = y + this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy;
		//    z = z + this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy;
		x = x * (double)velocity;
		y = y * (double)velocity;
		z = z * (double)velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float)(MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI));
		this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (double)(180F / (float)Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.ticksInGround = 0;
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

			//ブロック破壊処理
			this.DestroyBlock(iblockstate, blockpos);

			this.inBlockState = iblockstate;

			if (!iblockstate.isAir(world, blockpos)) {
				this.inBlockState.onEntityCollision(this.world, blockpos, this);
			}
		}
	}

	@Override
	protected void onHitEntity(RayTraceResult p_203046_1_) {
		Entity entity =p_203046_1_.entity;
		entity.setFire(5);
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		int i = MathHelper.ceil((double)f * this.damage);

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

//			this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			if (!(entity instanceof EntityEnderman)) {
				this.remove();
			}
		} else {
			this.motionX *= (double)-0.1F;
			this.motionY *= (double)-0.1F;
			this.motionZ *= (double)-0.1F;
			this.rotationYaw += 180.0F;
			this.prevRotationYaw += 180.0F;
			this.ticksInAir = 0;

				this.remove();
			
		}

	}



	@Override
	public void tick() {
		super.tick();

		//一秒で消滅する処理
		this.ticksInAir++;
		if(ticksInAir>20) {
			this.remove();
		}
	}

	public void DestroyBlock(IBlockState iblockstate,BlockPos blockpos) {
		Block block =iblockstate.getBlock();
		boolean Flag=false;

		for(Block b:cantDestroy) {
			if(block == b) {
				//何もしない
				Flag=true;
				this.remove();
			}
		}

		for(Block b:onceDestroy) {
			if(block == b) {
				iblockstate.dropBlockAsItem(worldIn, blockpos, 0);
				iblockstate.removedByPlayer(getEntityWorld(),blockpos, null,true,iblockstate.getFluidState() );
				Flag=true;
				this.remove();
			}
		}

		for(Block b:needException) {
			if(block == b) {
				if(block == Blocks.TNT) {
					BlockTNT tnt = (BlockTNT) block;
					tnt.explode(world, blockpos);
					world.removeBlock(blockpos);
				}
				Flag=true;
			}
		}

		if(!Flag) {
			iblockstate.dropBlockAsItem(worldIn, blockpos, 0);
			iblockstate.removedByPlayer(getEntityWorld(),blockpos, null,true,iblockstate.getFluidState() );
		}

	}


	//水中でも真っすぐ飛ぶように判定を殺す
	@Override
	public boolean isInWater() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}


	@Override
	protected ItemStack getArrowStack() {
		return null;
	}

}
