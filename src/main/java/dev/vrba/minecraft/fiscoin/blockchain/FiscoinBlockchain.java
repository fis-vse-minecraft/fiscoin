package dev.vrba.minecraft.fiscoin.blockchain;

import dev.vrba.minecraft.fiscoin.Fiscoin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class FiscoinBlockchain {

    @Getter
    private final ArrayList<FiscoinBlock> blocks;

    public FiscoinBlockchain() {
        this.blocks = new ArrayList<>();
    }

    public FiscoinBlockchain(@NotNull ArrayList<FiscoinBlock> blocks) {
        this.blocks = blocks;
    }

    public @NotNull FiscoinBlockchain add(@NotNull FiscoinBlock block) {
        this.blocks.add(block);
        return this;
    }

    public @NotNull String getLastBlockHash() {
        if (this.blocks.isEmpty()) {
            return FiscoinBlock.INITIAL_HASH;
        }

        return this.blocks.get(this.blocks.size() - 1).getHash();
    }

    public boolean isValid() {

        // An empty blockchain is considered to be valid
        if (blocks.isEmpty()) {
            return true;
        }

        String proof = new String(new char[Fiscoin.MINING_DIFFICULTY]).replace('\0', '0');

        try {
            FiscoinBlock last = blocks.stream()
                    .reduce((previous, current) -> {
                        if (!current.getPreviousHash().equals(previous.getHash())) {
                            throw new IllegalStateException("Block " + current.getHash() + " expected previous block to be " + previous.getHash());
                        }

                        if (!current.getHash().startsWith(proof)) {
                            throw new IllegalStateException("Block " + current.getHash() + " doesn't match the specified POW.");
                        }

                        return current;
                    })
                    // This shouldn't happen at all...
                    .orElseThrow();

            return true;
        }
        catch (IllegalStateException | NoSuchElementException exception) {
            return false;
        }
    }
}
