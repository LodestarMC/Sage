package team.lodestar.sage;

import com.mojang.logging.LogUtils;
import net.minecraft.client.GameNarrator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import team.lodestar.sage.capability.SageLevelChunkCapability;
import team.lodestar.sage.client.graphics.notification.NotificationRenderer;
import team.lodestar.sage.client.gui.SageScreen;
import team.lodestar.sage.client.gui.components.UIComponent;
import team.lodestar.sage.network.PacketHandler;
import team.lodestar.sage.notification.Notification;
import team.lodestar.sage.notification.NotificationManager;
import team.lodestar.sage.notification.behavior.NotificationBehaviorRegistry;

import java.util.List;

@Mod(SageMod.MODID)
public class SageMod
{
    public static final String MODID = "sage";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SageMod()
    {
        NotificationBehaviorRegistry.registerNotifications();
        MinecraftForge.EVENT_BUS.register(SageMod.class);
    }

    public static ResourceLocation in(String path) {
        return new ResourceLocation(MODID, path);
    }

    @SubscribeEvent
    public static void onAttachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        SageLevelChunkCapability.attachToChunk(event);
    }

    @SubscribeEvent
    public static void testPlaceNotif(BlockEvent.CropGrowEvent.Post event) {
        if (event.getLevel().isClientSide())
            return;

        // get all players
        event.getLevel().getServer().getPlayerList().getPlayers().forEach(player -> {
            if (player.position().distanceTo(Vec3.atCenterOf(event.getPos())) < 40)
                NotificationManager.addNotification((Level) event.getLevel(), new Notification(Vec3.atCenterOf(event.getPos()), NotificationRenderer.DEFAULT_RENDERER), List.of(player));
        });
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            NotificationManager.tick(event.level);
    }

    @Mod.EventBusSubscriber(modid = SageMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class ModBusSubscribers {
        @SubscribeEvent
        public static void onSetup(FMLCommonSetupEvent event) {
            PacketHandler.registerPackets(event);
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            SageLevelChunkCapability.register(event);
        }
    }

    @Mod.EventBusSubscriber(modid = SageMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    private static class ClientModBusSubscribers {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            NotificationRenderer.registerSageNotificationRenderers();
        }
    }
}

//@Mod.EventBusSubscriber(modid = SageMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//class Test {
//    private static SageScreen TEST_SCREEN = new SageScreen(GameNarrator.NO_TITLE, Test::setup).centered();
//
//    private static UIComponent setup() {
//        return new PanelComponent(472, 246, Color.BLACK)
//                .at(4, 4)
//                .withChild(new HorizontalComponent(4)
//                        .paddingTopLeft(4)
//                        .withChild(clickPanel("Databank", SageMod.in("textures/gui/chest.png"), 0.145f))
//                        .withChild(clickPanel("Research", SageMod.in("textures/gui/potion.png"), 0.145f))
//                        .withChild(clickPanel("Event Log", SageMod.in("textures/gui/chat_bubble.png"), 0.145f))
//                )
//                .withChild(new PanelComponent(472, 2, new Color(133, 133, 133)).at(0, 25))
//                .withChild(new PanelComponent(2, 221, new Color(133, 133, 133)).at(81, 25))
//                .withChild(new VerticalComponent(3)
//                        .at(4, 32)
//                        .withChild(clickPanel("Data Types", SageMod.in("textures/gui/bar_chart.png"), 0.15f))
//                        .withChild(clickPanel("Data Tree", SageMod.in("textures/gui/data_tree.png"), 0.15f))
//                        .withChild(clickPanel("third thing", SageMod.in("textures/gui/data_tree.png"), 0.15f))
//                );
//    }
//
//    private static UIComponent clickPanel(String text, ResourceLocation texture, float scale) {
//        return new PanelComponent(73, 17, new Color(34, 34, 34))
//                .withChild(new HorizontalComponent(4)
//                        .paddingTopLeft(2)
//                        .withChild(new TextureComponent(texture, scale).paddingUp(1))
//                        .withChild(new team.lodestar.sage.client.gui.components.TextComponent(text, Color.WHITE, false).paddingUp(2))
//                )
//                //.addHandler(new WidenOnHoverEffect(77, 0.5f, Easings::easeOutQuint))
//                .propagateHoverEvents()
//                .withChild(new PanelComponent(1, 0, new Color(140, 140, 140)).paddingRight(2).paddingUp(8).addHandler(new StretchOnHoverEffect(0, 17, 0.3f, Easings::easeOutQuart)).addHandler(new ChangePosOnHoverEffect(0, -8, 0.3f, Easings::easeOutQuart)))
//                .withChild(new PanelComponent(1, 0, new Color(140, 140, 140)).paddingLeft(74).paddingUp(8).addHandler(new StretchOnHoverEffect(0, 17, 0.3f, Easings::easeOutQuart)).addHandler(new ChangePosOnHoverEffect(0, -8, 0.3f, Easings::easeOutQuart)))
//                .onHover(component -> {
//                    PanelComponent panelComponent = (PanelComponent) component;
//                    panelComponent.fillColor = new Color(
//                            Math.min(255, panelComponent.fillColor.getRed() + 20),
//                            Math.min(255, panelComponent.fillColor.getGreen() + 20),
//                            Math.min(255, panelComponent.fillColor.getBlue() + 20)
//                    );
//
//                    TextureComponent textureComponent = (TextureComponent) panelComponent.getChild(0).getChild(0);
//                    textureComponent.color = new Color(
//                            Math.max(0, textureComponent.color.getRed() - 20),
//                            Math.max(0, textureComponent.color.getGreen() - 20),
//                            Math.max(0, textureComponent.color.getBlue() - 20)
//                    );
//
//                    TextComponent textComponent = (TextComponent) panelComponent.getChild(0).getChild(1);
//                    textComponent.color = new Color(
//                            Math.max(0, textComponent.color.getRed() - 20),
//                            Math.max(0, textComponent.color.getGreen() - 20),
//                            Math.max(0, textComponent.color.getBlue() - 20)
//                    );
//                })
//                .onNotHover(component -> {
//                    PanelComponent panelComponent = (PanelComponent) component;
//                    panelComponent.fillColor = new Color(
//                            Math.max(34, panelComponent.fillColor.getRed() - 20),
//                            Math.max(34, panelComponent.fillColor.getGreen() - 20),
//                            Math.max(34, panelComponent.fillColor.getBlue() - 20)
//                    );
//
//                    TextureComponent textureComponent = (TextureComponent) panelComponent.getChild(0).getChild(0);
//                    textureComponent.color = new Color(
//                            Math.min(255, textureComponent.color.getRed() + 20),
//                            Math.min(255, textureComponent.color.getGreen() + 20),
//                            Math.min(255, textureComponent.color.getBlue() + 20)
//                    );
//
//                    TextComponent textComponent = (TextComponent) panelComponent.getChild(0).getChild(1);
//                    textComponent.color = new Color(
//                            Math.min(255, textComponent.color.getRed() + 20),
//                            Math.min(255, textComponent.color.getGreen() + 20),
//                            Math.min(255, textComponent.color.getBlue() + 20)
//                    );
//                });
//    }
//
//    @SubscribeEvent
//    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
//        TEST_SCREEN.show();
//    }
//}

//class EntryLogTest {
//    private static SageScreen ENTRYLOG_SCREEN = new SageScreen(GameNarrator.NO_TITLE, EntryLogTest::setup);
//
//    private static UIComponent setup() {
//        UIComponent panelComponent
//    }
//}
