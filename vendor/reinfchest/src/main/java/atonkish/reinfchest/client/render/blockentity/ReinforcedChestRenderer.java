package atonkish.reinfchest.client.render.blockentity;

import atonkish.reinfchest.block.entity.ReinforcedChestBlockEntity;
import atonkish.reinfchest.client.render.ModChestSprites;
import atonkish.reinfcore.util.ReinforcingMaterial;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.state.properties.ChestType;

/**
 * Keeps vanilla state extraction and submits vanilla chest models with the legacy tier sprites.
 * Each registered block-entity type gets its own renderer, so the tier is immutable per instance.
 * This uses only APIs verified in Mojang's unmodified 26.2 client; no NeoForge hooks or mixins.
 */
public final class ReinforcedChestRenderer extends ChestRenderer<ReinforcedChestBlockEntity> {
    private final SpriteGetter tierSpriteGetter;
    private final MultiblockChestResources<ChestModel> tierModels;
    private final MultiblockChestResources<SpriteId> tierSprites;

    public ReinforcedChestRenderer(BlockEntityRendererProvider.Context context, ReinforcingMaterial material) {
        super(context);
        tierSpriteGetter = context.sprites();
        tierModels = ChestRenderer.LAYERS.map(layer -> new ChestModel(context.bakeLayer(layer)));
        // Cache identifiers, never resolved atlas sprites: resource reloads retain pack overrides.
        tierSprites = new MultiblockChestResources<>(
                ModChestSprites.get(material, ChestType.SINGLE),
                ModChestSprites.get(material, ChestType.LEFT),
                ModChestSprites.get(material, ChestType.RIGHT)
        );
    }

    @Override
    public void submit(
            ChestRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera
    ) {
        // The inherited extractor decides the season once per renderer, exactly as vanilla does.
        if (state.material == ChestRenderState.ChestMaterialType.CHRISTMAS) {
            super.submit(state, poseStack, submitNodeCollector, camera);
            return;
        }

        poseStack.pushPose();
        try {
            poseStack.mulPose(ChestRenderer.modelTransformation(state.facing));
            float open = 1.0F - state.open;
            open = 1.0F - open * open * open;
            submitNodeCollector.submitModel(
                    tierModels.select(state.type), open, poseStack,
                    state.lightCoords, OverlayTexture.NO_OVERLAY, -1,
                    tierSprites.select(state.type), tierSpriteGetter, 0, state.breakProgress
            );
        } finally {
            poseStack.popPose();
        }
    }
}
