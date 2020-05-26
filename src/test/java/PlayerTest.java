
import com.valentine.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Predicate;

public class PlayerTest {

    private static EventBus eventBus;
    Player player1;
    Player player2;

    @Before
    public void setup() {
        eventBus = new EventBus("Test");
        player1 = new Player("player1", eventBus);
        player2 = new Player("player2", eventBus);
        eventBus.onSubscribe(player1);
        eventBus.onSubscribe(player2);
    }

    @Test
    public void checkThatEventBusHasSubscribers() {
        Assert.assertEquals(2, eventBus.getSubscribers().size());
    }

    @Test(expected = Test.None.class)
    public void expectNoExceptionThrown() {
        player1.sendMessage("hey");
    }
}
