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
package packet.content;

import client.MapleCharacter;
import client.MapleClient;
import config.ServerConfig;
import handling.MaplePacket;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import java.util.List;
import java.util.Random;
import packet.ClientPacket;
import packet.ServerPacket;
import packet.Structure;

/**
 *
 * @author Riremito
 */
public class LoginPacket {

    // v131 - v186 OK
    public enum LoginResult {
        SUCCESS(0x00),
        BLOCKED_MAPLEID_WITH_MESSAGE(0x02), // 青窓BAN
        BLOCKED_MAPLEID(0x03), // 無条件BAN
        INVALID_PASSWORD(0x04),
        UNREGISTERED_MAPLEID(0x05),
        SYSTEM_ERROR(0x06), // 0x08, 0x09
        ALREADY_LOGGEDIN(0x07),
        TOO_MANY_USERS(0x0A),
        INVALID_ADMIN_IP(0x0D),
        BLOCKED_IP(0x13),
        UNKNOWN;
        /*
        v131
        00 : OK
        01 : Crash
        02 : 削除又は接続中止になっているアカウントです。 (青窓BAN)
        03 : 削除又は接続中止になっているアカウントです。 (キノコBAN)
        04 : パスワードが間違っています。
        05 : 登録されてないアカウントです。
        06 : システムのエラーで接続出来ません。
        07 : 接続中のIDです。
        08 : システムのエラーで接続出来ません。
        09 : システムのエラーで接続出来ません。
        0A : 現在、サーバーへの接続要請が多すぎます。
        0B : 韓国語
        0C : なし
        0D : 現在のIPではマスターログイン出来ません。
        0E : 韓国語
        0F : 韓国語
        10 : Crash
        11 : 韓国語
        12 : Crash
        13 : 臨時遮断IP経由で接続しました。
        14以上 : Crash
         */
        private int value;

        LoginResult(int reason) {
            value = reason;
        }

        LoginResult() {
            value = -1;
        }

        public int Get() {
            return value;
        }

        public static LoginResult Find(int find_val) {
            for (final LoginResult lr : LoginResult.values()) {
                if (lr.Get() == find_val) {
                    return lr;
                }
            }
            return UNKNOWN;
        }
    }

    // サーバーのバージョン情報
    public static final MaplePacket getHello(final byte[] sendIv, final byte[] recvIv) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.HELLO); // dummy header

        if (ServerConfig.version < 414) {
            p.Encode2(ServerConfig.version);
            p.EncodeStr(String.valueOf(ServerConfig.version_sub));
            p.EncodeBuffer(recvIv);
            p.EncodeBuffer(sendIv);
            p.Encode1(3); // JMS
        } else {
            // x64
            p.Encode2(ServerConfig.version);
            p.EncodeStr("1:" + ServerConfig.version_sub); // 1:1 ?
            p.EncodeBuffer(recvIv);
            p.EncodeBuffer(sendIv);
            p.Encode1(3); // JMS
            p.Encode1(0);
            p.Encode1(5);
            p.Encode1(1);
        }

        // ヘッダにサイズを書き込む
        p.SetHello();
        return p.Get();
    }

    public static final MaplePacket CheckPasswordResult(MapleClient client, int result) {
        return CheckPasswordResult(client, LoginResult.Find(result));
    }

    // CLogin::OnCheckPasswordResult
    // CClientSocket::OnCheckPassword
    // getAuthSuccessRequest, getLoginFailed
    public static final MaplePacket CheckPasswordResult(MapleClient client, LoginResult result) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_CheckPasswordResult);

        p.Encode1(result.Get());
        /*
        v186 Message Flag
        00 : OK
        20 : BAN Blue Message
        40 : BAN Blue Message
         */
        switch (result) {
            case SUCCESS: {
                p.Encode1(0); // OK
                p.Encode4(client.getAccID());
                p.Encode1(client.getGender()); // 性別
                p.Encode1(client.isGm() ? 1 : 0);

                if (164 <= ServerConfig.version) {
                    p.Encode1(client.isGm() ? 1 : 0);
                }

                p.EncodeStr(client.getAccountName());
                p.EncodeStr(client.getAccountName());
                p.Encode1(0);
                p.Encode1(0);
                p.Encode1(0);
                p.Encode1(0);

                if (164 <= ServerConfig.version) {
                    p.Encode1(0);
                }

                if (186 <= ServerConfig.version) {
                    p.Encode1(0);
                }

                // 2次パスワード
                if (188 <= ServerConfig.version) {
                    // -1, 無視
                    // 0, 初期化
                    // 1, 登録済み
                    p.Encode1(-1);
                }

                // 旧かんたん会員
                if (ServerConfig.version >= 302) {
                    // 0, 旧かんたん会員
                    // 1, 通常
                    p.Encode1(1);
                }

                p.Encode8(0); // buf
                p.EncodeStr(""); // v131: available name for new character, later version does not use this string
                break;
            }
            case BLOCKED_MAPLEID_WITH_MESSAGE: {
                p.Encode1(0x20); // 0x20 and 0x40 are blue message flag
                break;
            }
            default: {
                p.Encode1(0); // no blue message
                break;
            }
        }
        return p.Get();
    }

    // CLogin::OnGuestIDLoginResult
    // CWvsContext::SetAccountInfo
    public static final MaplePacket GuestIDLoginResult(MapleClient c, LoginResult result) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_GuestIDLoginResult);

        p.Encode1(result.Get()); // result code

        switch (result) {
            case SUCCESS: {
                p.Encode4(c.getAccID()); // dwAccountId
                p.Encode1(c.getGender()); // nGender
                p.Encode1(0); // nGradeCode
                p.EncodeStr(c.getAccountName()); // sNexonClubID
                p.Encode1(0);
                p.Encode1(0);
                p.Encode8(0); // buf
                p.EncodeStr("");
                p.EncodeStr("");
            }
            default: {
                break;
            }
        }

        return p.Get();
    }

    // いらない機能
    public static final MaplePacket LoginAUTH(ClientPacket p, MapleClient c) {
        // JMS v186.1には3つのログイン画面が存在するのでランダムに割り振ってみる
        String LoginScreen[] = {"MapLogin", "MapLogin1", "MapLogin2"};
        if (ServerConfig.version != 186) {
            return LoginAUTH(LoginScreen[0]);
        }
        return LoginAUTH(LoginScreen[(new Random().nextInt(3))]);
    }

    // ログイン画面へ切り替え
    public static final MaplePacket LoginAUTH(String LoginScreen) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LOGIN_AUTH);

        // ログイン画面の名称
        p.EncodeStr(LoginScreen);

        if (ServerConfig.version >= 187) {
            p.Encode4(0);
        }

        return p.Get();
    }

    // ワールドセレクト
    public static final MaplePacket getServerList(final int serverId) {
        return getServerList(serverId, true, 0);
    }

    // ワールドセレクト
    public static final MaplePacket getServerList(final int serverId, boolean internalserver, int externalch) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_WorldInformation);
        // ワールドID
        p.Encode1(serverId);
        // ワールド名
        p.EncodeStr(LoginServer.WorldName[serverId]);
        // ワールドの旗
        p.Encode1(LoginServer.WorldFlag[serverId]);
        // 吹き出し
        p.EncodeStr(LoginServer.WorldEvent[serverId]);
        // 経験値倍率?
        p.Encode2(100);
        // ドロップ倍率?
        p.Encode2(100);
        // チャンネル数
        p.Encode1(internalserver ? ChannelServer.getChannels() : externalch);
        // チャンネル情報
        for (int i = 0; i < (internalserver ? ChannelServer.getChannels() : externalch); i++) {
            // チャンネル名
            p.EncodeStr(LoginServer.WorldName[serverId] + "-" + (i + 1));
            // 接続人数表示
            if (internalserver && ChannelServer.getPopulation(i + 1) < 5) {
                p.Encode4(ChannelServer.getPopulation(i + 1) * 200);
            } else {
                p.Encode4(1000);
            }

            // ワールドID
            p.Encode1(serverId);
            if (ServerConfig.version < 302) {
                // チャンネルID
                p.Encode2(i);
            } else {
                p.Encode1(i);
                p.Encode1(0);
                p.Encode1(0);
            }
        }

        p.Encode2(0);

        if (ServerConfig.version >= 302) {
            p.Encode4(0);
        }

        return p.Get();
    }

    // ワールドセレクト
    public static final MaplePacket getEndOfServerList() {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_WorldInformation);
        p.Encode1(0xFF);
        return p.Get();
    }

    // キャラクターセレクト
    // getCharList
    // public static final MaplePacket getCharList(final boolean secondpw, final List<MapleCharacter> chars, int charslots) {
    public static final MaplePacket getCharList(MapleClient c, LoginResult result) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_SelectWorldResult);

        p.Encode1(result.Get());

        if (result != LoginResult.SUCCESS) {
            // error
            return p.Get();
        }

        List<MapleCharacter> chars = c.loadCharacters(c.getWorld());
        int charslots = c.getCharacterSlots();

        p.EncodeStr("");
        // キャラクターの数

        p.Encode1(chars.size());

        for (MapleCharacter chr : chars) {
            Structure.CharEntry(p, chr, false, false);
        }

        if (ServerConfig.version <= 131) {
            p.Encode1(3); // charslots
            p.Encode1(0);
            return p.Get();
        }

        // 2次パスワードの利用状態
        if (ServerConfig.version <= 186) {
            p.Encode2(2);
        } else {
            p.Encode1(0);
        }

        if (ServerConfig.version >= 302) {
            p.Encode4(0);
            p.Encode4(0);
            p.Encode4(0);
            p.Encode4(charslots);
        } else if (ServerConfig.version >= 190) {
            p.Encode4(0);
            p.Encode4(0);
            p.Encode4(charslots);
        } else if (ServerConfig.version <= 176) {
            p.Encode4(charslots);
        } else {
            p.Encode8(charslots);
        }

        return p.Get();
    }

    // キャラクター削除
    public static final MaplePacket deleteCharResponse(final int cid, final int state) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_DeleteCharacterResult);
        p.Encode4(cid);
        p.Encode1(state);
        return p.Get();
    }

    public static final MaplePacket addNewCharEntry(final MapleCharacter chr, final boolean worked) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_CreateNewCharacterResult);
        p.Encode1(worked ? 0 : 1);
        Structure.GW_CharacterStat(p, chr);
        Structure.AvatarLook(p, chr);
        return p.Get();
    }

    public static final MaplePacket charNameResponse(final String charname, final boolean nameUsed) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_CheckDuplicatedIDResult);

        p.EncodeStr(charname);
        p.Encode1(nameUsed ? 1 : 0);
        return p.Get();
    }

    // v131+
    // CLogin::OnCheckGameGuardUpdatedResult
    public static MaplePacket CheckGameGuardUpdate() {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_T_UpdateGameGuard);

        // 0 = Update Game Guard
        // 1 = Enable Login Button
        p.Encode1(1);

        return p.Get();
    }

    // CLogin::OnViewAllCharResult
    public static MaplePacket ViewAllCharResult_Alloc(MapleClient c) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_ViewAllCharResult);

        List<MapleCharacter> chars = c.loadCharacters(0);
        p.Encode1(1); // error code
        p.Encode4(1); // m_nCountRelatedSvrs
        p.Encode4(chars.size()); // m_nCountCharacters
        return p.Get();
    }

    // CLogin::OnViewAllCharResult
    public static MaplePacket ViewAllCharResult(MapleClient c) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_ViewAllCharResult);

        List<MapleCharacter> chars = c.loadCharacters(0); // world 0 only (test)

        p.Encode1(0); // error code
        p.Encode1(0); // nWorldID
        p.Encode1(chars.size());
        for (MapleCharacter chr : chars) {
            Structure.CharEntry(p, chr, false, true);
        }

        return p.Get();
    }

    // CLogin::OnLatestConnectedWorld
    public static MaplePacket LatestConnectedWorld() {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_LatestConnectedWorld);

        p.Encode4(0); // World ID
        return p.Get();
    }

    // v186+
    // CLogin::OnRecommendWorldMessage
    public static MaplePacket RecommendWorldMessage() {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_RecommendWorldMessage);

        String recommendedReasons[] = {"これはSELECTを押してもワールドがアクティブになるだけです", "ゴミ機能です", "XXXX"};
        p.Encode1(recommendedReasons.length);

        for (int world_id = 0; world_id < recommendedReasons.length; world_id++) {
            p.Encode4(world_id);
            p.EncodeStr(recommendedReasons[world_id]);
        }

        return p.Get();
    }

    // not tested
    public static final MaplePacket secondPwError(final byte mode) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.LP_CheckPinCodeResult);

        /*
            14 : Invalid password
            15 : Second password is incorrect
         */
        p.Encode1(mode);
        return p.Get();
    }

    public static final MaplePacket getServerStatus(final int status) {
        ServerPacket p = new ServerPacket(ServerPacket.Header.SERVERSTATUS);

        /*
            0 : Normal
            1 : Highly populated
            2 : Full
         */
        p.Encode2(status);
        return p.Get();
    }
}
