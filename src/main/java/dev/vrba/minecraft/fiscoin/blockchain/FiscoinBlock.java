package dev.vrba.minecraft.fiscoin.blockchain;

import dev.vrba.minecraft.fiscoin.Fiscoin;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.util.Date;

@Getter
@EqualsAndHashCode
public class FiscoinBlock {

    public static final String INITIAL_HASH = "0";

    /**
     * Hash of the previous block in the given blockchain,
     * if the previous block hash is equal to 0, it is considered being the first block
     */
    private final String previousHash;

    private final FiscoinTransaction transaction;

    private final long timestamp;

    private final String nonce;

    private final String hash;

    public FiscoinBlock(@NotNull String previousHash, @NotNull FiscoinTransaction transaction, @NotNull String nonce) {
        this(previousHash, transaction, nonce, new Date().getTime());
    }

    public FiscoinBlock(@NotNull String previousHash, @NotNull FiscoinTransaction transaction, @NotNull String nonce, long timestamp) {
        this.previousHash = previousHash;
        this.transaction = transaction;
        this.nonce = nonce;
        this.timestamp = timestamp;
        this.hash = calculateBlockHash();
    }

    private @NotNull String calculateBlockHash() {
        try {
            String input = previousHash + timestamp + nonce + transaction.toString();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            return Hex.encodeHexString(digest.digest(input.getBytes()));
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean hasValidHash() {
        String proof = new String(new char[Fiscoin.MINING_DIFFICULTY]).replace('\0', '0');
        return hash.startsWith(proof);
    }
}
