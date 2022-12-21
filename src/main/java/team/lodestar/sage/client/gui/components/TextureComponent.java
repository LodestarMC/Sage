package team.lodestar.sage.client.gui.components;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

// TODO: A lot of stuff, actually.
// - Make user able to specify blit parameters. Useful when the texture is an atlas
// - Make this work with animated textures?
// - Allow the user to specify scaling
public class TextureComponent extends UIComponent {

    private ResourceLocation textureLocation;
    private int textureWidth;
    private int textureHeight;
    private float scaleX = 1f;
    private float scaleY = 1f;

    private TextureComponent(ResourceLocation resource) {
        textureLocation = resource;

        SimpleTexture.TextureImage texture = ((SimpleTexture)Minecraft.getInstance().getTextureManager().getTexture(resource)).getTextureImage(Minecraft.getInstance().getResourceManager());

        try {
            NativeImage nativeImage = texture.getImage();
            textureWidth = nativeImage.getWidth();
            textureHeight = nativeImage.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextureComponent(ResourceLocation resource, float scale) {
        this(resource);
        scaleX = scale;
        scaleY = scale;
        dimensions(textureWidth * scaleX, textureHeight * scaleY);
    }

    public TextureComponent(ResourceLocation resource, float scaleX, float scaleY) {
        this(resource);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        dimensions(textureWidth * scaleX, textureHeight * scaleY);
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }

    @Override
    protected void renderComponent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        poseStack.pushPose();

        VFXBuilders.createScreen()
                .setPosTexDefaultFormat()
                .setShaderTexture(textureLocation)
                .setColor(1f, 1f, 1f)
                .setPositionWithWidth(getAbsoluteX(), getAbsoluteY(), textureWidth * scaleX, textureHeight * scaleY)
                .draw(poseStack);

        poseStack.popPose();
    }
}
