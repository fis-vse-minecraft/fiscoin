package dev.vrba.minecraft.fiscoin.blockchain;

import org.junit.Test;

import static org.junit.Assert.*;

public class FiscoinTransactionTests {

    @Test
    public void testTransactionCanBeCreated() {
        FiscoinWallet alice = new FiscoinWallet();
        FiscoinWallet bob = new FiscoinWallet();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublicKey(), bob.getPublicKey(), 0.1);

        assertNotNull(transaction);
    }

    @Test
    public void testTransactionCanBeSignedAndVerified() {
        FiscoinWallet alice = new FiscoinWallet();
        FiscoinWallet bob = new FiscoinWallet();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublicKey(), bob.getPublicKey(), 0.1)
                .sign(alice.getPrivateKey());

        assertNotNull(transaction);
        assertTrue(transaction.verify());
    }

    @Test
    public void testTransactionCannotBeVerifiedWithoutSignature() {
        FiscoinWallet alice = new FiscoinWallet();
        FiscoinWallet bob = new FiscoinWallet();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublicKey(), bob.getPublicKey(), 0.1);

        assertNotNull(transaction);
        assertFalse(transaction.verify());
    }

    @Test
    public void testTransactionCannotBeNotSignedAndVerified() {
        FiscoinWallet alice = new FiscoinWallet();
        FiscoinWallet bob = new FiscoinWallet();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublicKey(), bob.getPublicKey(), 0.1)
                .sign(bob.getPrivateKey());

        assertNotNull(transaction);
        assertFalse(transaction.verify());
    }
}
