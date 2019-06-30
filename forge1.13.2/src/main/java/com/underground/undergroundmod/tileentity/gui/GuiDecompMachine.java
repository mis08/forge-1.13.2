package com.underground.undergroundmod.tileentity.gui;

import com.underground.undergroundmod.tileentity.TileEntityDecompMachine;
import com.underground.undergroundmod.tileentity.container.ContainerDecompMachine;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiDecompMachine extends GuiContainer{

	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/furnace.png");
	/** The player inventory bound to this GUI. */
	private final InventoryPlayer playerInventory;
	private final IInventory tileDecomp;

	public GuiDecompMachine(InventoryPlayer playerInv,IInventory decompInv) {
		super(new ContainerDecompMachine(playerInv, decompInv));
		this.playerInventory = playerInv;
		this.tileDecomp = decompInv;
	}
	
	public GuiDecompMachine(InventoryPlayer playerInv,TileEntityDecompMachine dm) {
		super(new ContainerDecompMachine(playerInv, dm));
		this.playerInventory = playerInv;
		this.tileDecomp = dm;
	}

	@Override
	protected void initGui() {
		// TODO 自動生成されたメソッド・スタブ
		super.initGui();
	}


	/**
	 * Called from the main game loop to update the screen.
	 */
	public void tick() {
		super.tick();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		super.render(mouseX, mouseY, partialTicks);

		this.renderHoveredToolTip(mouseX, mouseY);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.tileDecomp.getDisplayName().getFormattedText();
		this.fontRenderer.drawString(s, (float)(this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2), 6.0F, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		if (TileEntityDecompMachine.isBurning(this.tileDecomp)) {
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
	}


	/**
	 * Called when the mouse is clicked over a slot or outside the gui.
	 */
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	private int getCookProgressScaled(int pixels) {
		int i = this.tileDecomp.getField(2);
		int j = this.tileDecomp.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	private int getBurnLeftScaled(int pixels) {
		int i = this.tileDecomp.getField(1);
		if (i == 0) {
			i = 200;
		}

		return this.tileDecomp.getField(0) * pixels / i;
	}

}
