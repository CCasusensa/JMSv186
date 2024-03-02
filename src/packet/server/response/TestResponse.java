/*
 * Copyright (C) 2024 Riremito
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */
package packet.server.response;

import handling.MaplePacket;
import packet.server.ServerPacket;

/**
 *
 * @author Riremito
 */
public class TestResponse {
    // 0x005E @005E 00, ミニマップ点滅, 再読み込みかも?

    public static MaplePacket ReloadMiniMap() {
        ServerPacket p = new ServerPacket(ServerPacket.Header.UNKNOWN_RELOAD_MINIMAP);
        p.Encode1((byte) 0x00);
        return p.Get();
    }

    // 0x0083 @0083, 画面の位置をキャラクターを中心とした場所に変更, 背景リロードしてるかも?
    public static MaplePacket ReloadMap() {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_ClearBackgroundEffect);
        return p.Get();
    }
}
