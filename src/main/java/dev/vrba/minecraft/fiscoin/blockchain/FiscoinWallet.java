package dev.vrba.minecraft.fiscoin.blockchain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;

import java.security.*;

@Getter
@EqualsAndHashCode
public class FiscoinWallet {
    private final PublicKey publicKey;

    private final PrivateKey privateKey;

    public FiscoinWallet() {
        this(generateRandomKeyPair());
    }

    public FiscoinWallet(@NotNull KeyPair pair) {
        this(pair.getPublic(), pair.getPrivate());
    }

    public FiscoinWallet(@NotNull PublicKey publicKey, @NotNull PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public static @NotNull String fingerprint(@NotNull PublicKey publicKey) {
        try {
            byte[] key = publicKey.getEncoded();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return Hex.encodeHexString(messageDigest.digest(key));
        }
        catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception);
        }
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