package net.rptools.maptool.model.tokens;

import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.gamedata.data.DataValue;

public record TokenPropertyChangeEvent(GUID tokenID, String name, DataValue dataType) {
}
