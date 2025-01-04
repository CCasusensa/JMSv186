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
package packet.response;

import config.ServerConfig;
import debug.Debug;
import handling.MaplePacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import packet.ServerPacket;

/**
 *
 * @author Riremito
 */
public class ResCClientSocket {

    public static long GameServerIP = 0;

    public static long getGameServerIP() {
        if (GameServerIP != 0) {
            return GameServerIP;
        }
        try {
            byte[] ip_bytes = InetAddress.getByName("127.0.0.1").getAddress();
            GameServerIP = ip_bytes[0] | (ip_bytes[1] << 8) | (ip_bytes[2] << 16) | (ip_bytes[3] << 24);
        } catch (UnknownHostException ex) {
            GameServerIP = 16777343; // 127.0.0.1
            Debug.ErrorLog("GameServerIP set to 127.0.0.1");
        }
        return GameServerIP;
    }

    // サーバーのバージョン情報
    public static final MaplePacket getHello(final byte[] sendIv, final byte[] recvIv) {
        ServerPacket sp = new ServerPacket((short) 0); // dummy

        switch (ServerConfig.GetRegion()) {
            case KMS: {
                long xor_version = 0;
                xor_version ^= ServerConfig.GetVersion();
                xor_version ^= 1 << 15;
                xor_version ^= ServerConfig.GetSubVersion() << 16;
                sp.Encode2(291); // magic number
                sp.EncodeStr(String.valueOf(xor_version));
                break;
            }
            default: {
                sp.Encode2(ServerConfig.GetVersion());
                sp.EncodeStr(String.valueOf(ServerConfig.GetSubVersion()));
                break;
            }
        }
        sp.EncodeBuffer(recvIv);
        sp.EncodeBuffer(sendIv);
        sp.Encode1(ServerConfig.GetRegionNumber()); // JMS = 3

        /*
            // x64
            sp.Encode2(ServerConfig.GetVersion());
            sp.EncodeStr("1:" + ServerConfig.GetSubVersion()); // 1:1
            sp.EncodeBuffer(recvIv);
            sp.EncodeBuffer(sendIv);
            sp.Encode1(ServerConfig.GetRegionNumber());
            sp.Encode1(0);
            sp.Encode1(5);
            sp.Encode1(1);
         */
        // ヘッダにサイズを書き込む
        sp.SetHello();
        return sp.Get();
    }

    // CClientSocket::OnAuthenMessage
    public static final MaplePacket AuthenMessage() {
        ServerPacket sp = new ServerPacket(ServerPacket.Header.LP_AuthenMessage);
        sp.Encode4(1); // id
        sp.Encode1(1);
        return sp.Get();
    }

    // Internet Cafe
    // プレミアムクーポン itemid 5420007
    // CClientSocket::OnAuthenCodeChanged
    public static final MaplePacket AuthenCodeChanged() {
        ServerPacket sp = new ServerPacket(ServerPacket.Header.LP_AuthenCodeChanged);
        sp.Encode1(2); // Open UI
        sp.Encode4(1);
        return sp.Get();
    }

    // CClientSocket::OnMigrateCommand
    // getChannelChange
    public static final MaplePacket MigrateCommand(final int port) {
        ServerPacket sp = new ServerPacket(ServerPacket.Header.LP_MigrateCommand);
        sp.Encode1(1);
        sp.Encode4((int) GameServerIP); // IP, 127.0.0.1
        sp.Encode2(port);

        if (ServerConfig.JMS302orLater()) {
            sp.Encode1(0);
        }
        return sp.Get();
    }

    // CClientSocket::OnAliveReq
    // getPing
    public static final MaplePacket AliveReq() {
        ServerPacket sp = new ServerPacket(ServerPacket.Header.LP_AliveReq);
        return sp.Get();
    }
}
