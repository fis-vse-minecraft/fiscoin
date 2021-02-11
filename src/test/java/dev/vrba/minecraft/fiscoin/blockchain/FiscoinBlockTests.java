package dev.vrba.minecraft.fiscoin.blockchain;

import org.junit.Test;

import static org.junit.Assert.*;

public class FiscoinBlockTests {

    @Test
    public void blocksGenerateSignature() {
        // Previous hash being equal to 0 means that this is the first transaction withing the blockchain
        FiscoinBlock block = new FiscoinBlock("0", "lmao", "10", 100_000_000);
        FiscoinBlock another = new FiscoinBlock(block.getHash(), "xddd", "1000", 100_000_001);

        assertEquals("80dde0fdf29a163536cd590072c846a015c528ccee8353b41e85cdde1be0abf9", block.getHash());
        assertEquals("1fe28beadd0ba5d9738739e4126f68e6520f808112200dd1a8fe391ac70c5338", another.getHash());
    }
}
