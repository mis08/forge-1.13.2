package com.underground.undergroundmod.irecipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.underground.undergroundmod.Debug;
import com.underground.undergroundmod.ModIdHolder;
import com.underground.undergroundmod.UnderGroundMod;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DecompMachineRecipe extends ForgeRegistryEntry<DecompMachineRecipe> implements IRecipe{

	private final ResourceLocation id;
	private final String group;
	private final Ingredient input;
	private final ItemStack output;
	private final float experience;
	private final int cookingTime;
	public ItemStack[] moreresult;

	public DecompMachineRecipe(ResourceLocation rlocation,String group,Ingredient ing,ItemStack itemstack,float exp,int time) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.id = rlocation;
		this.group = group;
		this.input = ing;
		this.output = itemstack;
		this.experience = exp;
		this.cookingTime = time;
	}
	

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		// TODO 自動生成されたメソッド・スタブ
		return this.input.test(inv.getStackInSlot(0));
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		// TODO 自動生成されたメソッド・スタブ
		return this.output.copy();
	}

	@Override
	public boolean canFit(int width, int height) {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}

	public NonNullList<Ingredient> getIngredients(){
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.input);
		return nonnulllist;
	}

	public float getExperience() {
		return this.experience;
	}

	@Override
	public ItemStack getRecipeOutput() {
		// TODO 自動生成されたメソッド・スタブ
		return this.output;
	}

	//RecipeBook関連、おそらく不要
	public String getGroup() {
		return this.group;
	}

	public int getCookingTime() {
		return this.cookingTime;
	}

	@Override
	public ResourceLocation getId() {
		// TODO 自動生成されたメソッド・スタブ
		return this.id;
	}

	public RecipeType<DecompMachineRecipe> getType(){
		return UnderGroundMod.DECOMP;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		// TODO 自動生成されたメソッド・スタブ
		return UnderGroundMod.DECOPM;
	}
	

	public static class Serializer implements IRecipeSerializer<DecompMachineRecipe>{
		private static ResourceLocation NAME = new ResourceLocation(ModIdHolder.MODID,"decomp");
		@SuppressWarnings("deprecation")
		public DecompMachineRecipe read(ResourceLocation recipeId,JsonObject json) {
			String s = JsonUtils.getString(json, "group", "");
			Ingredient ingredient;
			if(JsonUtils.isJsonArray(json, "ingredient")) {
				ingredient = Ingredient.fromJson(JsonUtils.getJsonArray(json, "ingredient"));
			}else {
				ingredient = Ingredient.fromJson(JsonUtils.getJsonObject(json, "ingredient"));
			}

			String s1 = JsonUtils.getString(json, "result");
			
			Item item = IRegistry.field_212630_s.func_212608_b(new ResourceLocation(s1));
			
			if(item != null) {
				ItemStack itemstack = new ItemStack(item);
				float Ivt_8_1_ = JsonUtils.getFloat(json, "experience",0.0F);
				int Ivt_9_1_ = JsonUtils.getInt(json, "cookingtime", 200);
				DecompMachineRecipe dmr = new DecompMachineRecipe(recipeId, s, ingredient, itemstack, Ivt_8_1_, Ivt_9_1_);
				sendJson(recipeId, json);
//				dmr.moreresult=makeMoreResult(recipeId, json);
				return dmr;
			}else {
				throw new IllegalStateException(s1 + "did not exist");
			}
		}
		@Override
		public DecompMachineRecipe read(ResourceLocation recipeId,
				PacketBuffer buffer) {
			// TODO 自動生成されたメソッド・スタブ
			String s = buffer.readString(32767);
			Ingredient ingredinent = Ingredient.fromBuffer(buffer);
			ItemStack itemstack = buffer.readItemStack();
			float f = buffer.readFloat();
			int i = buffer.readInt();
			return new DecompMachineRecipe(recipeId,s,ingredinent,itemstack,f,i);
		}
		@Override
		public void write(PacketBuffer buffer, DecompMachineRecipe recipe) {
			// TODO 自動生成されたメソッド・スタブ
			buffer.writeString(recipe.group);
			recipe.input.writeToBuffer(buffer);
			buffer.writeItemStack(recipe.output);
			buffer.writeVarInt(recipe.cookingTime);

		}
		@Override
		public ResourceLocation getName() {
			// TODO 自動生成されたメソッド・スタブ
			return NAME;
		}
		
		public void sendJson(ResourceLocation recipId, JsonObject json) {
			MoreRecipeHolder.setMore(recipId, json);
		}

		public ItemStack[] makeMoreResult(ResourceLocation recipeId,JsonObject json) {
			//jsonからarrayを受け取ってItemStack配列に変換する
			ItemStack[] outPut = new ItemStack[9];
			String[] results=new String[9];
			JsonArray jsonResults =JsonUtils.getJsonArray(json, "moreresult");
			for(JsonElement je : jsonResults) {
				for(int i = 0; i<10; ++i) {
					ItemStack is = new ItemStack(IRegistry.field_212630_s.func_212608_b(new ResourceLocation(je.toString())));
					if(is != null) {
						outPut[i] = is;
					}
				}
			}
			return outPut;
		}
		
		
	}
}



