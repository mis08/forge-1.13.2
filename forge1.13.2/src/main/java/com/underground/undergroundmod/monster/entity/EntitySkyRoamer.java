package com.underground.undergroundmod.monster.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.entity.AddEntity;


import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntitySkyRoamer extends EntityFlying implements IMob{
	   private static final DataParameter<Integer> SIZE = EntityDataManager.createKey(EntitySkyRoamer.class, DataSerializers.VARINT);
	   private Vec3d field_203036_b = Vec3d.ZERO;
	   private BlockPos field_203037_c = BlockPos.ORIGIN;
	   private EntitySkyRoamer.AttackPhase attackPhase = EntitySkyRoamer.AttackPhase.CIRCLE;
	   
	   //damage変更
	   public static final IAttribute ATTACK_DAMAGE = new RangedAttribute((IAttribute)null, "generic.attackDamage", 64.0D, 32.0D, 4096.0D);
	   

	   public EntitySkyRoamer(World p_i48793_1_) {
	      super(AddEntity.SKYROAMER, p_i48793_1_);
	      this.experienceValue = 10;
	      this.setSize(0.9F, 0.5F);
	      this.moveHelper = new EntitySkyRoamer.MoveHelper(this);
	      this.lookHelper = new EntitySkyRoamer.LookHelper(this);
	   }
	   
	   
	   

	   protected EntityBodyHelper createBodyHelper() {
	      return new EntitySkyRoamer.BodyHelper(this);
	   }

	   protected void initEntityAI() {
	      this.tasks.addTask(1, new EntitySkyRoamer.AIPickAttack());
	      this.tasks.addTask(2, new EntitySkyRoamer.AISweepAttack());
	      this.tasks.addTask(3, new EntitySkyRoamer.AIOrbitPoint());
	      this.targetTasks.addTask(1, new EntitySkyRoamer.AIAttackPlayer());
	   }

	   protected void registerAttributes() {
	      super.registerAttributes();
	      this.getAttributeMap().registerAttribute(ATTACK_DAMAGE);
	   }

	   protected void registerData() {
	      super.registerData();
	      this.dataManager.register(SIZE, 0);
	   }

	   public void setPhantomSize(int p_203034_1_) {
	      if (p_203034_1_ < 0) {
	         p_203034_1_ = 0;
	      } else if (p_203034_1_ > 64) {
	         p_203034_1_ = 64;
	      }

	      this.dataManager.set(SIZE, p_203034_1_);
	      this.func_203033_m();
	   }

	   public void func_203033_m() {
	      int i = this.dataManager.get(SIZE);
	      this.setSize(0.9F + 0.2F * (float)i, 0.5F + 0.1F * (float)i);
	      this.getAttribute(ATTACK_DAMAGE).setBaseValue((double)(6 + i));
	   }

	   public int getPhantomSize() {
	      return this.dataManager.get(SIZE);
	   }

	   public float getEyeHeight() {
	      return this.height * 0.35F;
	   }

	   public void notifyDataManagerChange(DataParameter<?> key) {
	      if (SIZE.equals(key)) {
	         this.func_203033_m();
	      }

	      super.notifyDataManagerChange(key);
	   }

	   /**
	    * Called to update the entity's position/logic.
	    */
	   public void tick() {
	      super.tick();
	      if (this.world.isRemote) {
	         float f = MathHelper.cos((float)(this.getEntityId() * 3 + this.ticksExisted) * 0.13F + (float)Math.PI);
	         float f1 = MathHelper.cos((float)(this.getEntityId() * 3 + this.ticksExisted + 1) * 0.13F + (float)Math.PI);
	         if (f > 0.0F && f1 <= 0.0F) {
	            this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PHANTOM_FLAP, this.getSoundCategory(), 0.95F + this.rand.nextFloat() * 0.05F, 0.95F + this.rand.nextFloat() * 0.05F, false);
	         }

	         int i = this.getPhantomSize();
	         float f2 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F)) * (1.3F + 0.21F * (float)i);
	         float f3 = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F)) * (1.3F + 0.21F * (float)i);
	         float f4 = (0.3F + f * 0.45F) * ((float)i * 0.2F + 1.0F);
	         this.world.spawnParticle(Particles.MYCELIUM, this.posX + (double)f2, this.posY + (double)f4, this.posZ + (double)f3, 0.0D, 0.0D, 0.0D);
	         this.world.spawnParticle(Particles.MYCELIUM, this.posX - (double)f2, this.posY + (double)f4, this.posZ - (double)f3, 0.0D, 0.0D, 0.0D);
	      }

	      if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
	         //this.remove();
	    	  onDeath(getLastDamageSource());
	      }

	   }

	   /**
	    * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	    * use this to react to sunlight and start to burn.
	    */
	   //焼かれないように変更
	   public void livingTick() {
	      if (this.isInDaylight()) {
	         //this.setFire(8);
	      }

	      super.livingTick();
	   }

	   protected void updateAITasks() {
	      super.updateAITasks();
	   }

	   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData, @Nullable NBTTagCompound itemNbt) {
	      this.field_203037_c = (new BlockPos(this)).up(5);
	      this.setPhantomSize(0);
	      return super.onInitialSpawn(difficulty, entityLivingData, itemNbt);
	   }

	   /**
	    * (abstract) Protected helper method to read subclass entity data from NBT.
	    */
	   public void readAdditional(NBTTagCompound compound) {
	      super.readAdditional(compound);
	      if (compound.hasKey("AX")) {
	         this.field_203037_c = new BlockPos(compound.getInt("AX"), compound.getInt("AY"), compound.getInt("AZ"));
	      }

	      this.setPhantomSize(compound.getInt("Size"));
	   }

	   /**
	    * Writes the extra NBT data specific to this type of entity. Should <em>not</em> be called from outside this class;
	    * use {@link #writeUnlessPassenger} or {@link #writeWithoutTypeId} instead.
	    */
	   public void writeAdditional(NBTTagCompound compound) {
	      super.writeAdditional(compound);
	      compound.setInt("AX", this.field_203037_c.getX());
	      compound.setInt("AY", this.field_203037_c.getY());
	      compound.setInt("AZ", this.field_203037_c.getZ());
	      compound.setInt("Size", this.getPhantomSize());
	   }

	   /**
	    * Checks if the entity is in range to render.
	    */
	   @OnlyIn(Dist.CLIENT)
	   public boolean isInRangeToRenderDist(double distance) {
	      return true;
	   }

	   public SoundCategory getSoundCategory() {
	      return SoundCategory.HOSTILE;
	   }

	   protected SoundEvent getAmbientSound() {
	      return SoundEvents.ENTITY_PHANTOM_AMBIENT;
	   }

	   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return SoundEvents.ENTITY_PHANTOM_HURT;
	   }

	   
	   protected SoundEvent getDeathSound() {
	      return SoundEvents.ENTITY_PHANTOM_DEATH;
	      
	   }
	   
	   @Nullable
	   protected ResourceLocation getLootTable() {
	      return UnderGroundMod.ENTITIES_SKYROAMER;
	   }

	   public CreatureAttribute getCreatureAttribute() {
	      return CreatureAttribute.UNDEAD;
	   }

	   /**
	    * Returns the volume for the sounds this mob makes.
	    */
	   protected float getSoundVolume() {
	      return 1.0F;
	   }

	   /**
	    * Returns true if this entity can attack entities of the specified class.
	    */
	   public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
	      return true;
	   }

	   class AIAttackPlayer extends EntityAIBase {
	      private int field_203142_b = 20;

	      private AIAttackPlayer() {
	      }

	      /**
	       * Returns whether the EntityAIBase should begin execution.
	       */
	      public boolean shouldExecute() {
	         if (this.field_203142_b > 0) {
	            --this.field_203142_b;
	            return false;
	         } else {
	            this.field_203142_b = 60;
	            AxisAlignedBB axisalignedbb = EntitySkyRoamer.this.getBoundingBox().grow(16.0D, 64.0D, 16.0D);
	            List<EntityPlayer> list = EntitySkyRoamer.this.world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
	            if (!list.isEmpty()) {
	               list.sort((p_203140_0_, p_203140_1_) -> {
	                  return p_203140_0_.posY > p_203140_1_.posY ? -1 : 1;
	               });

	               for(EntityPlayer entityplayer : list) {
	                  if (EntityAITarget.isSuitableTarget(EntitySkyRoamer.this, entityplayer, false, false)) {
	                     EntitySkyRoamer.this.setAttackTarget(entityplayer);
	                     return true;
	                  }
	               }
	            }

	            return false;
	         }
	      }

	      /**
	       * Returns whether an in-progress EntityAIBase should continue executing
	       */
	      public boolean shouldContinueExecuting() {
	         return EntityAITarget.isSuitableTarget(EntitySkyRoamer.this, EntitySkyRoamer.this.getAttackTarget(), false, false);
	      }
	   }

	   abstract class AIMove extends EntityAIBase {
	      public AIMove() {
	         this.setMutexBits(1);
	      }

	      protected boolean func_203146_f() {
	         return EntitySkyRoamer.this.field_203036_b.squareDistanceTo(EntitySkyRoamer.this.posX, EntitySkyRoamer.this.posY, EntitySkyRoamer.this.posZ) < 4.0D;
	      }
	   }

	   class AIOrbitPoint extends EntitySkyRoamer.AIMove {
	      private float field_203150_c;
	      private float field_203151_d;
	      private float field_203152_e;
	      private float field_203153_f;

	      private AIOrbitPoint() {
	         super();
	      }

	      /**
	       * Returns whether the EntityAIBase should begin execution.
	       */
	      public boolean shouldExecute() {
	         return EntitySkyRoamer.this.getAttackTarget() == null || EntitySkyRoamer.this.attackPhase == EntitySkyRoamer.AttackPhase.CIRCLE;
	      }

	      /**
	       * Execute a one shot task or start executing a continuous task
	       */
	      public void startExecuting() {
	         this.field_203151_d = 5.0F + EntitySkyRoamer.this.rand.nextFloat() * 10.0F;
	         this.field_203152_e = -4.0F + EntitySkyRoamer.this.rand.nextFloat() * 9.0F;
	         this.field_203153_f = EntitySkyRoamer.this.rand.nextBoolean() ? 1.0F : -1.0F;
	         this.func_203148_i();
	      }

	      /**
	       * Keep ticking a continuous task that has already been started
	       */
	      public void tick() {
	         if (EntitySkyRoamer.this.rand.nextInt(350) == 0) {
	            this.field_203152_e = -4.0F + EntitySkyRoamer.this.rand.nextFloat() * 9.0F;
	         }

	         if (EntitySkyRoamer.this.rand.nextInt(250) == 0) {
	            ++this.field_203151_d;
	            if (this.field_203151_d > 15.0F) {
	               this.field_203151_d = 5.0F;
	               this.field_203153_f = -this.field_203153_f;
	            }
	         }

	         if (EntitySkyRoamer.this.rand.nextInt(450) == 0) {
	            this.field_203150_c = EntitySkyRoamer.this.rand.nextFloat() * 2.0F * (float)Math.PI;
	            this.func_203148_i();
	         }

	         if (this.func_203146_f()) {
	            this.func_203148_i();
	         }

	         if (EntitySkyRoamer.this.field_203036_b.y < EntitySkyRoamer.this.posY && !EntitySkyRoamer.this.world.isAirBlock((new BlockPos(EntitySkyRoamer.this)).down(1))) {
	            this.field_203152_e = Math.max(1.0F, this.field_203152_e);
	            this.func_203148_i();
	         }

	         if (EntitySkyRoamer.this.field_203036_b.y > EntitySkyRoamer.this.posY && !EntitySkyRoamer.this.world.isAirBlock((new BlockPos(EntitySkyRoamer.this)).up(1))) {
	            this.field_203152_e = Math.min(-1.0F, this.field_203152_e);
	            this.func_203148_i();
	         }

	      }

	      private void func_203148_i() {
	         if (BlockPos.ORIGIN.equals(EntitySkyRoamer.this.field_203037_c)) {
	            EntitySkyRoamer.this.field_203037_c = new BlockPos(EntitySkyRoamer.this);
	         }

	         this.field_203150_c += this.field_203153_f * 15.0F * ((float)Math.PI / 180F);
	         EntitySkyRoamer.this.field_203036_b = (new Vec3d(EntitySkyRoamer.this.field_203037_c)).add((double)(this.field_203151_d * MathHelper.cos(this.field_203150_c)), (double)(-4.0F + this.field_203152_e), (double)(this.field_203151_d * MathHelper.sin(this.field_203150_c)));
	      }
	   }

	   class AIPickAttack extends EntityAIBase {
	      private int field_203145_b;

	      private AIPickAttack() {
	      }

	      /**
	       * Returns whether the EntityAIBase should begin execution.
	       */
	      public boolean shouldExecute() {
	         return EntityAITarget.isSuitableTarget(EntitySkyRoamer.this, EntitySkyRoamer.this.getAttackTarget(), false, false);
	      }

	      /**
	       * Execute a one shot task or start executing a continuous task
	       */
	      public void startExecuting() {
	         this.field_203145_b = 10;
	         EntitySkyRoamer.this.attackPhase = EntitySkyRoamer.AttackPhase.CIRCLE;
	         this.func_203143_f();
	      }

	      /**
	       * Reset the task's internal state. Called when this task is interrupted by another one
	       */
	      public void resetTask() {
	         EntitySkyRoamer.this.field_203037_c = EntitySkyRoamer.this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, EntitySkyRoamer.this.field_203037_c).up(10 + EntitySkyRoamer.this.rand.nextInt(20));
	      }

	      /**
	       * Keep ticking a continuous task that has already been started
	       */
	      public void tick() {
	         if (EntitySkyRoamer.this.attackPhase == EntitySkyRoamer.AttackPhase.CIRCLE) {
	            --this.field_203145_b;
	            if (this.field_203145_b <= 0) {
	               EntitySkyRoamer.this.attackPhase = EntitySkyRoamer.AttackPhase.SWOOP;
	               this.func_203143_f();
	               this.field_203145_b = (8 + EntitySkyRoamer.this.rand.nextInt(4)) * 20;
	               EntitySkyRoamer.this.playSound(SoundEvents.ENTITY_PHANTOM_SWOOP, 10.0F, 0.95F + EntitySkyRoamer.this.rand.nextFloat() * 0.1F);
	            }
	         }

	      }

	      private void func_203143_f() {
	         EntitySkyRoamer.this.field_203037_c = (new BlockPos(EntitySkyRoamer.this.getAttackTarget())).up(20 + EntitySkyRoamer.this.rand.nextInt(20));
	         if (EntitySkyRoamer.this.field_203037_c.getY() < EntitySkyRoamer.this.world.getSeaLevel()) {
	            EntitySkyRoamer.this.field_203037_c = new BlockPos(EntitySkyRoamer.this.field_203037_c.getX(), EntitySkyRoamer.this.world.getSeaLevel() + 1, EntitySkyRoamer.this.field_203037_c.getZ());
	         }

	      }
	   }

	   class AISweepAttack extends EntitySkyRoamer.AIMove {
	      private AISweepAttack() {
	         super();
	      }

	      /**
	       * Returns whether the EntityAIBase should begin execution.
	       */
	      public boolean shouldExecute() {
	         return EntitySkyRoamer.this.getAttackTarget() != null && EntitySkyRoamer.this.attackPhase == EntitySkyRoamer.AttackPhase.SWOOP;
	      }

	      /**
	       * Returns whether an in-progress EntityAIBase should continue executing
	       */
	      public boolean shouldContinueExecuting() {
	         EntityLivingBase entitylivingbase = EntitySkyRoamer.this.getAttackTarget();
	         if (entitylivingbase == null) {
	            return false;
	         } else if (!entitylivingbase.isAlive()) {
	            return false;
	         } else {
	            return !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).isSpectator() && !((EntityPlayer)entitylivingbase).isCreative() ? this.shouldExecute() : false;
	         }
	      }

	      /**
	       * Execute a one shot task or start executing a continuous task
	       */
	      public void startExecuting() {
	      }

	      /**
	       * Reset the task's internal state. Called when this task is interrupted by another one
	       */
	      public void resetTask() {
	         EntitySkyRoamer.this.setAttackTarget((EntityLivingBase)null);
	         EntitySkyRoamer.this.attackPhase = EntitySkyRoamer.AttackPhase.CIRCLE;
	      }

	      /**
	       * Keep ticking a continuous task that has already been started
	       */
	      public void tick() {
	         EntityLivingBase entitylivingbase = EntitySkyRoamer.this.getAttackTarget();
	         EntitySkyRoamer.this.field_203036_b = new Vec3d(entitylivingbase.posX, entitylivingbase.posY + (double)entitylivingbase.height * 0.5D, entitylivingbase.posZ);
	         if (EntitySkyRoamer.this.getBoundingBox().grow((double)0.2F).intersects(entitylivingbase.getBoundingBox())) {
	            EntitySkyRoamer.this.attackEntityAsMob(entitylivingbase);
	            EntitySkyRoamer.this.attackPhase = EntitySkyRoamer.AttackPhase.CIRCLE;
	            EntitySkyRoamer.this.world.playEvent(1039, new BlockPos(EntitySkyRoamer.this), 0);
	         } else if (EntitySkyRoamer.this.collidedHorizontally || EntitySkyRoamer.this.hurtTime > 0) {
	            EntitySkyRoamer.this.attackPhase = EntitySkyRoamer.AttackPhase.CIRCLE;
	         }

	      }
	   }

	   static enum AttackPhase {
	      CIRCLE,
	      SWOOP;
	   }

	   class BodyHelper extends EntityBodyHelper {
	      public BodyHelper(EntityLivingBase p_i48805_2_) {
	         super(p_i48805_2_);
	      }

	      /**
	       * Update the Head and Body rendenring angles
	       */
	      public void updateRenderAngles() {
	         EntitySkyRoamer.this.rotationYawHead = EntitySkyRoamer.this.renderYawOffset;
	         EntitySkyRoamer.this.renderYawOffset = EntitySkyRoamer.this.rotationYaw;
	      }
	   }

	   class LookHelper extends EntityLookHelper {
	      public LookHelper(EntityLiving p_i48802_2_) {
	         super(p_i48802_2_);
	      }

	      /**
	       * Updates look
	       */
	      public void tick() {
	      }
	   }

	   class MoveHelper extends EntityMoveHelper {
	      private float field_203105_j = 0.1F;

	      public MoveHelper(EntityLiving p_i48801_2_) {
	         super(p_i48801_2_);
	      }

	      public void tick() {
	         if (EntitySkyRoamer.this.collidedHorizontally) {
	            EntitySkyRoamer.this.rotationYaw += 180.0F;
	            this.field_203105_j = 0.1F;
	         }

	         float f = (float)(EntitySkyRoamer.this.field_203036_b.x - EntitySkyRoamer.this.posX);
	         float f1 = (float)(EntitySkyRoamer.this.field_203036_b.y - EntitySkyRoamer.this.posY);
	         float f2 = (float)(EntitySkyRoamer.this.field_203036_b.z - EntitySkyRoamer.this.posZ);
	         double d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
	         double d1 = 1.0D - (double)MathHelper.abs(f1 * 0.7F) / d0;
	         f = (float)((double)f * d1);
	         f2 = (float)((double)f2 * d1);
	         d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
	         double d2 = (double)MathHelper.sqrt(f * f + f2 * f2 + f1 * f1);
	         float f3 = EntitySkyRoamer.this.rotationYaw;
	         float f4 = (float)MathHelper.atan2((double)f2, (double)f);
	         float f5 = MathHelper.wrapDegrees(EntitySkyRoamer.this.rotationYaw + 90.0F);
	         float f6 = MathHelper.wrapDegrees(f4 * (180F / (float)Math.PI));
	         EntitySkyRoamer.this.rotationYaw = MathHelper.approachDegrees(f5, f6, 4.0F) - 90.0F;
	         EntitySkyRoamer.this.renderYawOffset = EntitySkyRoamer.this.rotationYaw;
	         if (MathHelper.degreesDifferenceAbs(f3, EntitySkyRoamer.this.rotationYaw) < 3.0F) {
	            this.field_203105_j = MathHelper.approach(this.field_203105_j, 1.8F, 0.005F * (1.8F / this.field_203105_j));
	         } else {
	            this.field_203105_j = MathHelper.approach(this.field_203105_j, 0.2F, 0.025F);
	         }

	         float f7 = (float)(-(MathHelper.atan2((double)(-f1), d0) * (double)(180F / (float)Math.PI)));
	         EntitySkyRoamer.this.rotationPitch = f7;
	         float f8 = EntitySkyRoamer.this.rotationYaw + 90.0F;
	         double d3 = (double)(this.field_203105_j * MathHelper.cos(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f / d2);
	         double d4 = (double)(this.field_203105_j * MathHelper.sin(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f2 / d2);
	         double d5 = (double)(this.field_203105_j * MathHelper.sin(f7 * ((float)Math.PI / 180F))) * Math.abs((double)f1 / d2);
	         EntitySkyRoamer.this.motionX += (d3 - EntitySkyRoamer.this.motionX) * 0.2D;
	         EntitySkyRoamer.this.motionY += (d5 - EntitySkyRoamer.this.motionY) * 0.2D;
	         EntitySkyRoamer.this.motionZ += (d4 - EntitySkyRoamer.this.motionZ) * 0.2D;
	      }
	   }
	   

	   
	   
	   
	   

}
