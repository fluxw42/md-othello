package be.dechamps.model;


import org.junit.Test;

public class OthelloEngineTest {

    @Test
    public void startup() {
        OthelloEngine engine = new OthelloEngine();
        engine.thinkAndMove();
        String s = engine.getCurrentPosition().toGraphString();
        System.out.println(s);
    }
}