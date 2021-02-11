package dev.vrba.minecraft.fiscoin.blockchain;

import dev.vrba.minecraft.fiscoin.Fiscoin;
import org.apache.commons.lang.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;

public class FiscoinBlockchainTests {

    @Test
    public void testBlockchainCanBeCreated() {
        FiscoinBlockchain blockchain = new FiscoinBlockchain();

        assertEquals(0, blockchain.getBlocks().size());
        assertTrue(blockchain.isValid());
    }

    @Test
    public void testValidBlockChainCanBeValidated() {
        FiscoinBlockchain blockchain = new FiscoinBlockchain();

        // Those nonce values were pre-calculated using MINING_DIFFICULTY = 2
        FiscoinBlock a = new FiscoinBlock(FiscoinBlock.INITIAL_HASH, "xdd", "Rl1Iz3g7Uh", 1);
        FiscoinBlock b = new FiscoinBlock(a.getHash(), "lmao", "QmGOBwc13O", 2);
        FiscoinBlock c = new FiscoinBlock(b.getHash(), "this", "mYVs6URPRn", 3);
        FiscoinBlock d = new FiscoinBlock(c.getHash(), "should", "mkCk3yn0c0", 4);
        FiscoinBlock e = new FiscoinBlock(d.getHash(), "work", "O3vJPI2RXJ", 5);

        blockchain.add(a).add(b).add(c).add(d).add(e);

        assertEquals(5, blockchain.getBlocks().size());
        assertTrue(blockchain.isValid());
    }

    @Test
    public void testInvalidBlockChainCannotBeValidated() {
        FiscoinBlockchain blockchain = new FiscoinBlockchain();

        // Those nonce values were pre-calculated using MINING_DIFFICULTY = 2
        FiscoinBlock a = new FiscoinBlock(FiscoinBlock.INITIAL_HASH, "xdd", "Rl1Iz3g7Uh", 1);
        // Nonce of block b is invalid, and will result in hash that doesn't begin with "00"
        FiscoinBlock b = new FiscoinBlock(a.getHash(), "lmao", "H4ck3r1337", 2);
        FiscoinBlock c = new FiscoinBlock(b.getHash(), "this", "mYVs6URPRn", 3);
        FiscoinBlock d = new FiscoinBlock(c.getHash(), "should", "mkCk3yn0c0", 4);
        FiscoinBlock e = new FiscoinBlock(d.getHash(), "work", "O3vJPI2RXJ", 5);

        blockchain.add(a).add(b).add(c).add(d).add(e);

        assertEquals(5, blockchain.getBlocks().size());
        assertFalse(blockchain.isValid());
    }

    @Test
    public void testInvalidBlockChainCannotBeValidated2() {
        FiscoinBlockchain blockchain = new FiscoinBlockchain();

        // Those nonce values were pre-calculated using MINING_DIFFICULTY = 2
        FiscoinBlock a = new FiscoinBlock(FiscoinBlock.INITIAL_HASH, "xdd", "Rl1Iz3g7Uh", 1);
        FiscoinBlock b = new FiscoinBlock(a.getHash(), "lmao", "QmGOBwc13O", 2);
        FiscoinBlock c = new FiscoinBlock(b.getHash(), "this", "mYVs6URPRn", 3);
        // Block d doesn't match the previous block hash within this chain and will invalidate the chain
        FiscoinBlock d = new FiscoinBlock("this_is_not_a_valid_hash_lmao", "should", "mkCk3yn0c0", 4);
        FiscoinBlock e = new FiscoinBlock(d.getHash(), "work", "O3vJPI2RXJ", 5);

        blockchain.add(a).add(b).add(c).add(d).add(e);

        assertEquals(5, blockchain.getBlocks().size());
        assertFalse(blockchain.isValid());
    }
}
