package net.rptools.maptool.api.player;

public record PlayerDatabaseInfo(boolean supportsBlocking, boolean supportsIndividualPasswords,
                                 boolean supportsAsymmetricalKeys) {
}
