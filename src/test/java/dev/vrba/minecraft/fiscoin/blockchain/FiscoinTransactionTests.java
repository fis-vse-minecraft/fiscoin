package dev.vrba.minecraft.fiscoin.blockchain;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

import static org.junit.Assert.*;

public class FiscoinTransactionTests {

    private @NotNull KeyPair generateKeyRandomPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048, new SecureRandom());

            return generator.generateKeyPair();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testTransactionCanBeCreated() {
        KeyPair alice = generateKeyRandomPair();
        KeyPair bob = generateKeyRandomPair();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublic(), bob.getPublic(), 0.1);

        assertNotNull(transaction);
    }

    @Test
    public void testTransactionCanBeSignedAndVerified() {
        KeyPair alice = generateKeyRandomPair();
        KeyPair bob = generateKeyRandomPair();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublic(), bob.getPublic(), 0.1).sign(alice.getPrivate());

        assertNotNull(transaction);
        assertTrue(transaction.verify());
    }

    @Test
    public void testTransactionCannotBeNotSignedAndVerified() {
        KeyPair alice = generateKeyRandomPair();
        KeyPair bob = generateKeyRandomPair();

        FiscoinTransaction transaction = new FiscoinTransaction(alice.getPublic(), bob.getPublic(), 0.1)
                .sign(bob.getPrivate());

        assertNotNull(transaction);
        assertFalse(transaction.verify());
    }
}
