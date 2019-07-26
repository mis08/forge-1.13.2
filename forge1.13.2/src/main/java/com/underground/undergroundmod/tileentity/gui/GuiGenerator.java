package com.underground.undergroundmod.tileentity.gui;

import com.underground.undergroundmod.Debug;
import com.underground.undergroundmod.ModIdHolder;
import com.underground.undergroundmod.tileentity.TileEntityGenerator;
import com.underground.undergroundmod.tileentity.container.ContainerGenerator;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiGenerator extends GuiContainer{
	
	private static final ResourceLocation GENERATOR_GUI_TEXTURES = new ResourceLocation(ModIdHolder.MODID + ":textures/gui/generator_gui.png");
	private final InventoryPlayer playerInventory;
	private final IInventory tileGenerator;
	
	public GuiGenerator(InventoryPlayer playerInventory,IInventory generatorInv) {
		super(new ContainerGenerator(playerInventory, generatorInv));
		this.playerInventory = playerInventory;
		this.tileGenerator = generatorInv;
	}
	
	public GuiGenerator(InventoryPlayer playerInv,TileEntityGenerator tg) {
		super(new ContainerGenerator(playerInv, tg));
		this.playerInventory = playerInv;
		this.tileGenerator = tg;
	}
	
	@Override
	protected void initGui() {
		// TODO 自動生成されたメソッド・スタブ
		super.initGui();
	}
	
	@Override
	public void tick() {
		// TODO 自動生成されたメソッド・スタブ
		super.tick();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		// TODO 自動生成されたメソッド・スタブ
		this.drawDefaultBackground();
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		super.render(mouseX, mouseY, partialTicks);
		
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// TODO 自動生成されたメソッド・スタブ
		String s =this.tileGenerator.getDisplayName().getFormattedText();
		this.fontRenderer.drawString(s, (float)(this.xSize/2 - this.fontRenderer.getStringWidth(s)/2), 6.0F, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY) {
		// TODO 自動生成されたメソッド・スタブ
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GENERATOR_GUI_TEXTURES);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

		//CookBerの位置
		int l = this.getPowerScaled(80);
		this.drawTexturedModalRect(i + 79, j + 24, 176, 14, 80-l, 11);
		
		if(isPowerOn()) {
			this.drawTexturedModalRect(i+82,j+47 , 176,25, 68, 26);
		}else {
			this.drawTexturedModalRect(i+82, j+47, 176, 51, 68,26);
		}
	}
	
	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton,
			ClickType type) {
		// TODO 自動生成されたメソッド・スタブ
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
	}
	
	@Override
	public void onGuiClosed() {
		// TODO 自動生成されたメソッド・スタブ
		super.onGuiClosed();
	}
	
	
	private boolean isPowerOn() {
		if(this.tileGenerator.getField(1) == 0) {
			return false;
		}
		return true;
	}
	
	private int getPowerScaled(int pixels) {
		int i = 384;
		int j = this.tileGenerator.getField(0);
		j = j==0 ? 384: j;
		return  j != 0 ? j * pixels / i : 0;
	}
	

}
