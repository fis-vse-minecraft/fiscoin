package dev.vrba.minecraft.fiscoin.blockchain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.security.*;

@Getter
@EqualsAndHashCode
public class Wallet {
    private final PublicKey publicKey;

    private final PrivateKey privateKey;

    public Wallet() {
        this(generateRandomKeyPair());
    }

    public Wallet(@NotNull KeyPair pair) {
        this(pair.getPublic(), pair.getPrivate());
    }

    public Wallet(@NotNull PublicKey publicKey, @NotNull PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    private static @NotNull KeyPair generateRandomKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048, new SecureRandom());

            return generator.generateKeyPair();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}