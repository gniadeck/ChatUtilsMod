package commands;

import com.mojang.brigadier.context.CommandContext;
import dev.komp15.commands.RandomPlayer;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class RandomPlayerTest {

    private CommandContext<FabricClientCommandSource> context;
    private RandomPlayer randomPlayer;

    @Before
    public void init(){
        randomPlayer = new RandomPlayer();
    }

    @Test
    public void getRandomPlayerNicknameShoudntReturnInvokerNickname() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        context = mock(CommandContext.class, RETURNS_DEEP_STUBS);
        FabricClientCommandSource source = mock(FabricClientCommandSource.class);
        ClientPlayerEntity player = mock(ClientPlayerEntity.class);
        when(context.getSource())
                .thenReturn(source);
        when(source.getPlayerNames())
                .thenReturn(List.of("one","two","three"));
        when(source.getPlayer())
                .thenReturn(player);
        when(player.getEntityName())
                .thenReturn("invoker");

        Method method = RandomPlayer.class.getDeclaredMethod("getRandomPlayerNickname", CommandContext.class);
        method.setAccessible(true);

        for(int i = 0; i < 100; i++){
            assertNotEquals("invoker", method.invoke(randomPlayer, context));
        }



    }



}
