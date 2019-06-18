package com.underground.undergroundmod.entity;

import javax.annotation.Nullable;

import com.underground.undergroundmod.UnderGroundMod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.server.MinecraftServer;
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

	private static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.createKey(EntityLaser.class, DataSerializers.BYTE);
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
	private int ticksKill;
	private double preX;
	private double preY;
	private double preZ;
	private BlockPos[] killpos=new BlockPos[25];
	private int successCount;
	private EntityLivingBase player;

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
		this(worldIn);
		this.setPosition(x, y, z);
		this.worldIn=worldIn;
		this.preX=x;
		this.preY=y;
		this.preZ=z;

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
//			this.comandkill(this.xTile, this.yTile, this.zTile);
			this.dobulKill(blockpos);
			iblockstate.dropBlockAsItem(worldIn, blockpos, 0);
			iblockstate.removedByPlayer(getEntityWorld(),blockpos, null,true,iblockstate.getFluidState() );
//

//			this.world.setBlockState(blockpos, Block.getStateById(0));

			
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
	
	private void comandkill(int x,int y,int z) {
		String posx,posy,posz,killcmd;
		posx=String.valueOf(x);
		posy=String.valueOf(y);
		posz=String.valueOf(z);
//		killcmd="/fill "+posx+" "+posy+" "+posz+" "+posx+" "+posy+" "+posz+" air destroy";
		killcmd="excute at @s run fill ~ ~ ~ ~ ~ ~ minecraft:air";
//		MinecraftServer minecraftserver =this.player.getServer();
		CommandSource cs= this.getCommandSource().withResultConsumer((p_209527_1_, p_209527_2_, p_209527_3_) -> {
			if (p_209527_2_) {
				++this.successCount;
			}
		});
		
//		CommandSource cs = this.player.getCommandSource();
		try {
//			minecraftserver.getCommandManager().handleCommand(cs,killcmd);
		}catch(Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Executing Entity Laser");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Command to be executed");
            if(cs == null) {
            	crashreportcategory.addDetail("cs", "null");
            }else {
            	crashreportcategory.addDetail("cs", cs.getName());
            }
            if(killcmd == null) {
            	crashreportcategory.addDetail("cmd", "null");
            }else {
            	crashreportcategory.addDetail("cmd", killcmd);
            }
            
            crashreportcategory.addDetail("Command", killcmd);
            crashreportcategory.addDetail("Name", () -> {
               return this.getName().getString();
            });
            throw new ReportedException(crashreport);
		}
	}
	
	private void dobulKill(@Nullable BlockPos pos) {
		killpos[this.ticksInAir]=pos;

		if(this.ticksInAir >20) {
			for(BlockPos p:killpos) {
				if(p!=null) {
					this.world.setBlockState(p, Block.getStateById(0));
					UnderGroundMod.Debag.info("called not null");
				}
			}
			UnderGroundMod.Debag.info("called dobulkill");
		}
		
	}

	@Override
	public void tick() {
		super.tick();
		
		//一秒で消滅する処理
		this.ticksInAir++;
		if(ticksInAir>20) {
			this.dobulKill(null);
			this.remove();
		}
	}


	@Override
	protected ItemStack getArrowStack() {
		return null;
	}

}
