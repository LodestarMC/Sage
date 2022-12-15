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

    public TextureComponent(ResourceLocation resource) {
        textureLocation = resource;

        SimpleTexture.TextureImage texture = ((SimpleTexture)Minecraft.getInstance().getTextureManager().getTexture(resource)).getTextureImage(Minecraft.getInstance().getResourceManager());

        try {
            NativeImage nativeImage = texture.getImage();
            textureWidth = nativeImage.getWidth();
            textureHeight = nativeImage.getHeight();
            dimensions(textureWidth, textureHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextureComponent(ResourceLocation resource, float scale) {
        this(resource);
        scaleX = scale;
        scaleY = scale;
    }

    public TextureComponent(ResourceLocation resource, float scaleX, float scaleY) {
        this(resource);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setTexture(ResourceLocation resource) {
        this.textureLocation = resource;
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
                .setPositionWithWidth(getX(), getY(), textureWidth * scaleX, textureHeight * scaleY)
                .draw(poseStack);

        poseStack.popPose();
    }
}
