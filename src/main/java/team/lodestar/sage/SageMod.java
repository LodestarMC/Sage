package team.lodestar.sage;

import com.mojang.logging.LogUtils;
import net.minecraft.client.GameNarrator;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import team.lodestar.sage.client.gui.SageScreen;
import team.lodestar.sage.client.gui.components.PanelComponent;
import team.lodestar.sage.client.gui.components.TextComponent;
import team.lodestar.sage.client.gui.components.VerticalComponent;

import java.awt.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SageMod.MODID)
public class SageMod
{
    public static final String MODID = "sage";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SageMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}

@Mod.EventBusSubscriber(modid = SageMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
class Test {
    private static final SageScreen TEST_SCREEN = new SageScreen(GameNarrator.NO_TITLE)
            .set(() ->
                    new PanelComponent(90, 90, Color.black)
                            .at(10, 10)
                            .withChild(
                                    new VerticalComponent(2)
                                            .paddingTopLeft(3)
                                            .withChild(new TextComponent("Hello, Sage!", Color.WHITE))
                                            .withChild(new TextComponent("This is text", Color.YELLOW))
                                            .withChild(
                                                    new PanelComponent(55, 10, Color.WHITE)
                                                            .withChild(
                                                                    new TextComponent("More text", Color.GREEN)
                                                                            .paddingTopLeft(1))
                                                            .onClick(c -> {
                                                                c.paddingUp(2);
                                                                c.recalculatePosition();
                                                            })
                                            )
                            )
            );

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        TEST_SCREEN.show();
    }
}
