package mavrikakis.reinfminecart.client.render;

import atonkish.reinfchest.client.render.ModChestSprites;
import atonkish.reinfcore.util.ReinforcingMaterial;

import com.mojang.blaze3d.vertex.PoseStack;

import mavrikakis.reinfminecart.entity.ReinforcedMinecart;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.ChestType;

/** Vanilla minecart renderer with the displayed chest replaced by the existing tier chest model. */
public final class ReinforcedMinecartRenderer
        extends AbstractMinecartRenderer<ReinforcedMinecart, MinecartRenderState> {
    private final ChestModel chestModel;
    private final SpriteGetter spriteGetter;
    private final SpriteId chestSprite;

    public ReinforcedMinecartRenderer(
            EntityRendererProvider.Context context,
            ReinforcingMaterial material
    ) {
        super(context, ModelLayers.MINECART);
        this.chestModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        this.spriteGetter = context.getSprites();
        this.chestSprite = ModChestSprites.get(material, ChestType.SINGLE);
    }

    @Override
    public MinecartRenderState createRenderState() {
        return new MinecartRenderState();
    }

    @Override
    protected void submitMinecartContents(
            MinecartRenderState state,
            BlockModelRenderState ignoredVanillaChest,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int light
    ) {
        poseStack.pushPose();
        try {
            poseStack.mulPose(ChestRenderer.modelTransformation(Direction.NORTH));
            // Sprite-backed models use this field as ARGB colour, not an outline sentinel.
            // Opaque white matches ChestRenderer; MinecartRenderState.NO_OUTLINE is 0 and made
            // the entire mounted chest transparent in alpha.1.
            submitNodeCollector.submitModel(
                    chestModel,
                    0.0F,
                    poseStack,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    -1,
                    chestSprite,
                    spriteGetter,
                    0,
                    null
            );
        } finally {
            poseStack.popPose();
        }
    }
}
