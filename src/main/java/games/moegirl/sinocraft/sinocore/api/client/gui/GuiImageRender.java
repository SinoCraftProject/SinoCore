package games.moegirl.sinocraft.sinocore.api.client.gui;

import games.moegirl.sinocraft.sinocore.api.gui.GuiImage;
import games.moegirl.sinocraft.sinocore.api.gui.GuiSprite;
import games.moegirl.sinocraft.sinocore.api.gui.GuiTexture;
import net.minecraft.client.gui.GuiGraphics;

public class GuiImageRender {
    public static void blitImage(GuiImage image, GuiGraphics guiGraphics, int x, int y, int width, int height) {
        if (image instanceof GuiTexture texture) {
            guiGraphics.blit(texture.getPath(), x, y, width, height, texture.getU0(), texture.getV0(),
                    texture.getUWidth(), texture.getVHeight(), texture.getTextureWidth(), texture.getTextureHeight());
            return;
        }

        if (image instanceof GuiSprite sprite) {
            guiGraphics.blitSprite(sprite.getPath(), x, y, width, height);
        }
    }
}
