package dev.vrba.minecraft.fiscoin.blockchain;

import org.junit.Test;

import static org.junit.Assert.*;

public class FiscoinTransactionTests {

    @Test
    public void testTransactionCanBeCreated() {
        Wallet alice = new Wallet();
        Wallet bob = new Wallet();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublicKey(), bob.getPublicKey(), 0.1);

        assertNotNull(transaction);
    }

    @Test
    public void testTransactionCanBeSignedAndVerified() {
        Wallet alice = new Wallet();
        Wallet bob = new Wallet();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublicKey(), bob.getPublicKey(), 0.1)
                .sign(alice.getPrivateKey());

        assertNotNull(transaction);
        assertTrue(transaction.verify());
    }

    @Test
    public void testTransactionCannotBeVerifiedWithoutSignature() {
        Wallet alice = new Wallet();
        Wallet bob = new Wallet();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublicKey(), bob.getPublicKey(), 0.1);

        assertNotNull(transaction);
        assertFalse(transaction.verify());
    }

    @Test
    public void testTransactionCannotBeNotSignedAndVerified() {
        Wallet alice = new Wallet();
        Wallet bob = new Wallet();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublicKey(), bob.getPublicKey(), 0.1)
                .sign(bob.getPrivateKey());

        assertNotNull(transaction);
        assertFalse(transaction.verify());
    }
}
