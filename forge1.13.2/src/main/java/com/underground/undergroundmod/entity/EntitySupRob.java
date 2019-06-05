package com.underground.undergroundmod.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.monster.entity.EntitySkyRoamer;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class EntitySupRob extends EntityTameable implements IRangedAttackMob,IInventoryChangedListener{

	private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.createKey(EntitySupRob.class,DataSerializers.FLOAT);
	private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntitySupRob.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> STATUS = EntityDataManager.createKey(EntitySupRob.class, DataSerializers.BYTE);
	private net.minecraftforge.common.util.LazyOptional<?> itemHandler = null;
	private float headRotationCourseOld;
	private float headRotationCourse;
	
	protected ContainerHorseChest suprobChest;
	
	public EntitySupRob(EntityType<?> entity,World worldIn) {
		super(entity,worldIn);
	}
	
	public EntitySupRob(World worldIn) {
		super(UnderGroundMod.EntitySupRob,worldIn);
		this.setSize(0.6F, 0.85F);
		this.setTamed(false);

	}
	
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAIFollowOwner(this, 1.0D, 6.0F,2.0F));
		this.tasks.addTask(2, new EntityAIAttackRanged(this,1.0D,1,5,20.0F));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class,8.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this,EntitySkyRoamer.class,true));
	}
	
	@Override
	protected void registerData() {
		// TODO 自動生成されたメソッド・スタブ
		super.registerData();
		this.dataManager.register(DATA_HEALTH_ID,this.getHealth());
		this.dataManager.register(BEGGING,false);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		// TODO 自動生成されたメソッド・スタブ
		
		return this.isTamed() && this.dataManager.get(DATA_HEALTH_ID) < 10.0F ? UnderGroundMod.R2D2Beap : UnderGroundMod.R2D2flat;
 	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		// TODO 自動生成されたメソッド・スタブ
		return UnderGroundMod.R2D2Scream;
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.3F);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	    
	}
	

	   
	   public void setTamed(boolean tamed) {
      super.setTamed(tamed);
      if (tamed) {
         this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
      } else {
         this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
      }

      this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
   }
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		Item item = itemstack.getItem();
		
	      if (this.isTamed()) {
	    	  if(player.isSneaking()) {
	    		  this.openGUI(player);
	    		  return true;
	    	  }
	    	  
	          if (!itemstack.isEmpty()) {
	             if (item instanceof ItemFood) {
	                ItemFood itemfood = (ItemFood)item;
	                if (itemfood.isMeat() && this.dataManager.get(DATA_HEALTH_ID) < 20.0F) {
	                   if (!player.abilities.isCreativeMode) {
	                      itemstack.shrink(1);
	                   }

	                   this.heal((float)itemfood.getHealAmount(itemstack));
	                   return true;
	                }
	             }
	          }
	          


	          if (this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(itemstack)) {
	             this.aiSit.setSitting(!this.isSitting());
	             this.isJumping = false;
	             this.navigator.clearPath();
	             this.setAttackTarget((EntityLivingBase)null);
	          }
	       } else if (item == UnderGroundMod.RobotConnecter) {
	          if (!player.abilities.isCreativeMode) {
	             itemstack.shrink(1);
	          }

	          if (!this.world.isRemote) {
	             if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
	                this.setTamedBy(player);
	                this.navigator.clearPath();
	                this.setAttackTarget((EntityLivingBase)null);
	                this.aiSit.setSitting(false);
	                this.setHealth(50.0F);
	                this.playTameEffect(true);
	                this.world.setEntityState(this, (byte)7);
	             } else {
	                this.playTameEffect(false);
	                this.world.setEntityState(this, (byte)6);
	             }
	          }

	          return true;
	       }

	       return super.processInteract(player, hand);
	}
	
	@OnlyIn(Dist.CLIENT)
	public float getInterestedAngle(float p_70917_1_) {
		return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * (float)Math.PI;
	}

	@Override
	public void tick() {
		super.tick();
	      this.headRotationCourseOld = this.headRotationCourse;
	      if (this.isBegging()) {
	         this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
	      } else {
	         this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
	      }

	}
	
	public boolean isBegging() {
		return this.dataManager.get(BEGGING);
	}
	
	

	
	@Override
	public EntitySupRob createChild(EntityAgeable ageable) {
		EntitySupRob entitySupRob= new EntitySupRob(this.world);
		UUID uuid = this.getOwnerId();
		if(uuid !=null) {
			entitySupRob.setOwnerId(uuid);
			entitySupRob.setTamed(true);
		}
		return entitySupRob;
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target,float distanceFactor) {
		Gunfire(target, distanceFactor);
	}
	
	public void Gunfire(EntityLivingBase target,float distanceFactor) {
		EntityBullet entityarrow = this.getbullet(distanceFactor);
		double d0 = target.posX - this.posX;
		double d1 = target.getBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
		entityarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 3 * 3.0F,1.0F);
		this.playSound(UnderGroundMod.GunSound, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entityarrow);
	}
	
	protected EntityBullet getbullet(float p_190726_1_) {
		EntityBullet entitybullet =new EntityBullet(this.world,this);
      return entitybullet;
   }
	
	protected int getInventorySize() {
		return 2;
	}
	
	protected void initSupRobChest() {
		ContainerHorseChest containersuprobchest = this.suprobChest;
		this.suprobChest = new ContainerHorseChest(this.getName(), this.getInventorySize());
		this.suprobChest.setCustomName(this.getCustomName());
		if(containersuprobchest !=null) {
			containersuprobchest.removeListener(this);
			int i = Math.min(containersuprobchest.getSizeInventory(), this.suprobChest.getSizeInventory());
			
			for(int j=0; j<i; ++j) {
				ItemStack itemstack = containersuprobchest.getStackInSlot(j);
				if(!itemstack.isEmpty()){
					this.suprobChest.setInventorySlotContents(j, itemstack.copy());
				}
			}
		}
		
		this.suprobChest.addListener(this);
		this.updateSupRobSlots();
		this.itemHandler = net.minecraftforge.common.util.LazyOptional.of(() -> new net.minecraftforge.items.wrapper.InvWrapper(this.suprobChest));
	}
	
	protected void updateSupRobSlots() {
		if(!this.world.isRemote) {
			this.setSupRobSaddled(!this.suprobChest.getStackInSlot(0).isEmpty() && this.canBeSaddled());
		}
	}
	
	
	public boolean canBeSaddled() {
		return true;
	}
	
	public void setSupRobSaddled(boolean saddled) {
		this.setSupRobWatchableBoolean(4,saddled);
	}
	
	protected void setSupRobWatchableBoolean(int sam1,boolean sam2) {
		byte b0 =this.dataManager.get(STATUS);
		if(sam2) {
			this.dataManager.set(STATUS,(byte)(b0 | sam1));
		}else {
			this.dataManager.set(STATUS, (byte)(b0 & ~sam1));
		}
	}


	@Override
	public void onInventoryChanged(IInventory invBasic) {
		if(this.ticksExisted > 20) {
			this.playSound(SoundEvents.ENTITY_HORSE_SADDLE, 0.5F, 1.0F);
		}
	}
	
	public void openGUI(EntityPlayer playerEntity) {
		if(!this.world.isRemote && this.isTamed()) {
			this.suprobChest.setCustomName(this.getCustomName());
			playerEntity.displayGUIChest(suprobChest);
		}
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		// TODO 自動生成されたメソッド・スタブ
		super.onDeath(cause);
		
		if(!this.world.isRemote && this.suprobChest != null) {
			for(int i=0; i<this.suprobChest.getSizeInventory(); ++i) {
				ItemStack itemstack = this.suprobChest.getStackInSlot(i);
				if(!itemstack.isEmpty()) {
					this.entityDropItem(itemstack);
				}
			}
		}
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap,@Nullable net.minecraft.util.EnumFacing facing) {
		
		if(this.isAlive() && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && itemHandler != null){
			return itemHandler.cast();
		}
		return super.getCapability(cap,facing);
	}
	
	@Override
	public void remove(boolean keepData) {

		super.remove(keepData);
		if(!keepData && itemHandler !=null) {
			itemHandler.invalidate();
			itemHandler = null;
			}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
