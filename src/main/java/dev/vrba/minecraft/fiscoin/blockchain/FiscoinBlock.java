package dev.vrba.minecraft.fiscoin.blockchain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.util.Date;

@Getter
@EqualsAndHashCode
public class FiscoinBlock {
    /**
     * Hash of the previous block in the given blockchain,
     * if the previous block hash is equal to 0, it is considered being the first block
     */
    private final String previousHash;

    private final String data;

    private final long timestamp;

    private final String nonce;

    private final String hash;

    public FiscoinBlock(@NotNull String previousHash, @NotNull String data, @NotNull String nonce) {
        this(previousHash, data, nonce, new Date().getTime());
    }

    public FiscoinBlock(@NotNull String previousHash, @NotNull String data, @NotNull String nonce, long timestamp) {
        this.previousHash = previousHash;
        this.data = data;
        this.nonce = nonce;
        this.timestamp = timestamp;
        this.hash = calculateBlockHash();
    }

    private @NotNull String calculateBlockHash() {
        try {
            String input = previousHash + timestamp + nonce + data;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            return Hex.encodeHexString(digest.digest(input.getBytes()));
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
