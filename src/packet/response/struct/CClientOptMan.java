/*
 * Copyright (C) 2023 Riremito
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
 * You should not develop private server for your business.
 * You should not ban anyone who tries hacking in private server.
 */
package packet.response.struct;

import packet.ServerPacket;

/**
 *
 * @author Riremito
 */
public class CClientOptMan {

    // CClientOptMan::DecodeOpt
    public static byte[] EncodeOpt() {
        ServerPacket p = new ServerPacket();

        p.Encode2(0); // not 0, Encode4, Encode4
        return p.get().getBytes();
    }
}
