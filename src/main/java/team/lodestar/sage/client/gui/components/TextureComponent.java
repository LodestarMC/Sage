package team.lodestar.sage.client.gui.components;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.sage.SageMod;

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

        try {
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(resource);
            if (texture instanceof SimpleTexture simpleTexture) {
                NativeImage image = simpleTexture.getTextureImage(Minecraft.getInstance().getResourceManager()).getImage();
                textureWidth = image.getWidth();
                textureHeight = image.getHeight();
            }
            else if (texture instanceof DynamicTexture dynamicTexture) {
                NativeImage image = dynamicTexture.getPixels();
                textureWidth = image.getWidth();
                textureHeight = image.getHeight();
                SageMod.LOGGER.warn("DynamicTexture may not work yet");
            }
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
                .setPosColorTexDefaultFormat()
                .setShader(GameRenderer::getPositionColorTexShader)
                .setShaderTexture(textureLocation)
                .setColor(255, 255, 255)
                .setPositionWithWidth(getAbsoluteX(), getAbsoluteY(), textureWidth * scaleX, textureHeight * scaleY)
                .draw(poseStack);

        poseStack.popPose();
    }
}
