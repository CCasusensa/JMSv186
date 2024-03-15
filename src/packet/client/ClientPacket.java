// クライアント側から送信されるパケットのヘッダの定義
package packet.client;

import config.ServerConfig;
import java.util.Properties;

public class ClientPacket {

    private byte[] packet;
    private int decoded;

    // MapleのInPacketのDecodeのように送信されたパケットを再度Decodeする
    public ClientPacket(byte[] b) {
        packet = b;
        decoded = 0;
    }

    public static Header ToHeader(short w) {
        for (final Header h : Header.values()) {
            if (h.Get() == w) {
                return h;
            }
        }

        return Header.UNKNOWN;
    }

    public String Packet() {
        short header = (short) (((int) packet[0] & 0xFF) | ((int) (packet[1] & 0xFF) << 8));
        String text = String.format("@%04X", header);

        for (int i = 2; i < packet.length; i++) {
            text += String.format(" %02X", packet[i]);
        }

        return text;
    }

    public String GetOpcodeName() {
        if (packet.length < 2) {
            return Header.UNKNOWN.toString();
        }

        short header = (short) (((int) packet[0] & 0xFF) | ((int) (packet[1] & 0xFF) << 8));
        return ToHeader(header).toString();
    }

    public Header GetOpcode() {
        if (packet.length < 2) {
            return Header.UNKNOWN;
        }

        short header = (short) (((int) packet[0] & 0xFF) | ((int) (packet[1] & 0xFF) << 8));
        return ToHeader(header);
    }

    public byte Decode1() {
        return (byte) packet[decoded++];
    }

    public short Decode2() {
        return (short) (((short) Decode1() & 0xFF) | (((short) Decode1() & 0xFF) << 8));
    }

    public int Decode4() {
        return (int) (((int) Decode2() & 0xFFFF) | (((int) Decode2() & 0xFFFF) << 16));
    }

    public long Decode8() {
        return (long) (((long) Decode4() & 0xFFFFFFFF) | (((long) Decode4() & 0xFFFFFFFF) << 32));
    }

    public byte[] DecodeBuffer() {
        int length = Decode2();
        byte[] buffer = new byte[length];

        for (int i = 0; i < length; i++) {
            buffer[i] = Decode1();
        }

        return buffer;
    }

    public String DecodeStr() {
        int length = Decode2();
        byte[] buffer = new byte[length];

        for (int i = 0; i < length; i++) {
            buffer[i] = Decode1();
        }

        return new String(buffer, ServerConfig.utf8 ? ServerConfig.codepage_utf8 : ServerConfig.codepage_ascii);
    }

    public byte[] DecodeAll() {
        int length = packet.length - decoded;
        byte[] buffer = new byte[length];

        for (int i = 0; i < length; i++) {
            buffer[i] = Decode1();
        }

        return buffer;
    }

    public byte[] DecodeMovePath() {
        int length = packet.length - decoded - 8; // short x 4

        if (GetOpcode() == Header.CP_UserMove) {
            length -= 1 + 9;
        }

        byte[] buffer = new byte[length];

        for (int i = 0; i < length; i++) {
            buffer[i] = Decode1();
        }

        return buffer;
    }

    public int getMoveAction() {
        int length = packet.length - decoded - 8; // short x 4

        if (GetOpcode() == Header.CP_UserMove) {
            length -= 1 + 9;
        }
        return (int) packet[decoded + length - 3];
    }

    public enum Header {
        // 独自仕様
        CP_CUSTOM_,
        CP_CUSTOM_WZ_HASH,
        CP_CUSTOM_MEMORY_SCAN,
        // v414.1 test
        //CP_TEST1(0x66),
        //CP_TEST2(0xA3),
        // JMS v131.0
        CP_T_UpdateGameGuard,
        // Names from v95 PDB
        // ログインサーバー
        CP_BEGIN_SOCKET,
        CP_CheckPassword, // LOGIN_PASSWORD
        CP_Check2ndPassword, // v188 test
        CP_Check2ndPassword_cancel, // v188 test
        //CP_GuestIDLogin,
        CP_AccountInfoRequest,
        CP_WorldInfoRequest, // SERVERLIST_REQUEST
        CP_SelectWorld, // CHARLIST_REQUEST
        CP_CheckUserLimit,
        //CP_ConfirmEULA,
        //CP_SetGender,
        CP_CheckPinCode,
        //CP_UpdatePinCode,
        CP_WorldRequest,
        CP_LogoutWorld,
        CP_ViewAllChar,
        //CP_SelectCharacterByVAC,
        //CP_VACFlagSet,
        //CP_CheckNameChangePossible,
        //CP_RegisterNewCharacter,
        //CP_CheckTransferWorldPossible,
        CP_SelectCharacter, // CHAR_SELECT
        CP_MigrateIn, // PLAYER_LOGGEDIN
        CP_CheckDuplicatedID, // CHECK_CHAR_NAME
        CP_CreateNewCharacter, // CREATE_CHAR
        CP_CreateNewCharacterInCS,
        CP_DeleteCharacter, // DELETE_CHAR
        CP_AliveAck,
        CP_ExceptionLog,
        CP_SecurityPacket,
        //CP_EnableSPWRequest,
        //CP_CheckSPWRequest,
        //CP_EnableSPWRequestByACV,
        //CP_CheckSPWRequestByACV,
        //CP_CheckOTPRequest,
        //CP_CheckDeleteCharacterOTP,
        CP_CreateSecurityHandle,
        //CP_SSOErrorLog,
        //CP_ClientDumpLog,
        //CP_CheckExtraCharInfo,
        //CP_CreateNewCharacter_Ex,
        CP_END_SOCKET,
        // ゲームサーバー
        CP_BEGIN_USER,
        CP_UserTransferFieldRequest, // CHANGE_MAP
        CP_UserTransferChannelRequest, // CHANGE_CHANNEL
        CP_UserMigrateToCashShopRequest, // ENTER_CASH_SHOP
        CP_UserMove, // MOVE_PLAYER
        CP_UserSitRequest, // CANCEL_CHAIR
        CP_UserPortableChairSitRequest, // USE_CHAIR
        CP_UserMeleeAttack, // CLOSE_RANGE_ATTACK
        CP_UserShootAttack, // RANGED_ATTACK
        CP_UserMagicAttack, // MAGIC_ATTACK
        CP_UserBodyAttack,
        //CP_UserMovingShootAttackPrepare,
        CP_UserHit, // TAKE_DAMAGE
        CP_UserAttackUser,
        CP_UserChat, // GENERAL_CHAT
        CP_UserADBoardClose,
        CP_UserEmotion, // FACE_EXPRESSION
        CP_UserActivateEffectItem, // USE_ITEMEFFECT
        CP_UserUpgradeTombEffect,
        CP_UserHP,
        CP_Premium,
        CP_UserBanMapByMob,
        CP_UserMonsterBookSetCover,
        CP_UserSelectNpc, // NPC_TALK
        CP_UserRemoteShopOpenRequest,
        CP_UserScriptMessageAnswer, // NPC_TALK_MORE
        CP_UserShopRequest, // NPC_SHOP
        CP_UserTrunkRequest, // STORAGE
        CP_UserEntrustedShopRequest,
        CP_UserStoreBankRequest,
        CP_UserParcelRequest,
        CP_UserEffectLocal,
        CP_ShopScannerRequest,
        CP_ShopLinkRequest,
        CP_AdminShopRequest,
        CP_UserGatherItemRequest,
        CP_UserSortItemRequest,
        CP_UserChangeSlotPositionRequest, // ITEM_MOVE
        CP_UserStatChangeItemUseRequest, // USE_ITEM
        CP_UserStatChangeItemCancelRequest, // CANCEL_ITEM_EFFECT
        CP_UserStatChangeByPortableChairRequest,
        CP_UserMobSummonItemUseRequest, // USE_SUMMON_BAG
        CP_UserPetFoodItemUseRequest, // PET_FOOD
        CP_UserTamingMobFoodItemUseRequest, // USE_MOUNT_FOOD
        CP_UserScriptItemUseRequest,
        CP_UserConsumeCashItemUseRequest, // USE_CASH_ITEM
        CP_UserDestroyPetItemRequest,
        CP_UserBridleItemUseRequest, // USE_CATCH_ITEM
        CP_UserSkillLearnItemUseRequest, // USE_SKILL_BOOK
        CP_UserSkillResetItemUseRequest,
        CP_JMS_MONSTERBOOK_SET,
        CP_UserShopScannerItemUseRequest, // USE_OWL_MINERVA
        CP_UserMapTransferItemUseRequest, // USE_TELE_ROCK
        CP_UserPortalScrollUseRequest, // USE_RETURN_SCROLL
        CP_UserUpgradeItemUseRequest, // USE_UPGRADE_SCROLL
        CP_UserHyperUpgradeItemUseRequest,
        CP_UserItemOptionUpgradeItemUseRequest,
        //CP_UserUIOpenItemUseRequest,
        CP_UserItemReleaseRequest,
        CP_UserAbilityUpRequest, // DISTRIBUTE_AP
        CP_UserAbilityMassUpRequest,
        CP_UserChangeStatRequest, // HEAL_OVER_TIME
        CP_UserChangeStatRequestByItemOption,
        CP_UserSkillUpRequest, // DISTRIBUTE_SP
        CP_UserSkillUseRequest, // SPECIAL_MOVE
        CP_UserSkillCancelRequest, // CANCEL_BUFF
        CP_UserSkillPrepareRequest, // SKILL_EFFECT
        CP_UserDropMoneyRequest, // MESO_DROP
        CP_UserGivePopularityRequest, // GIVE_FAME
        CP_UserPartyRequest,
        CP_UserCharacterInfoRequest, // CHAR_INFO_REQUEST
        CP_UserActivatePetRequest, // SPAWN_PET
        CP_UserTemporaryStatUpdateRequest, // CANCEL_DEBUFF
        CP_UserPortalScriptRequest, // CHANGE_MAP_SPECIAL
        CP_UserPortalTeleportRequest, // USE_INNER_PORTAL
        CP_UserMapTransferRequest, // TROCK_ADD_MAP
        CP_UserAntiMacroItemUseRequest,
        CP_UserAntiMacroSkillUseRequest,
        CP_UserAntiMacroQuestionResult,
        CP_UserClaimRequest,
        CP_UserQuestRequest, // QUEST_ACTION
        CP_UserCalcDamageStatSetRequest,
        CP_UserThrowGrenade,
        CP_UserMacroSysDataModified,
        CP_UserSelectNpcItemUseRequest,
        CP_UserLotteryItemUseRequest,
        CP_UserItemMakeRequest,
        //CP_UserSueCharacterRequest,
        CP_UserUseGachaponBoxRequest,
        //CP_UserUseGachaponRemoteRequest,
        //CP_UserUseWaterOfLife,
        CP_UserRepairDurabilityAll,
        CP_UserRepairDurability,
        CP_UserQuestRecordSetState,
        CP_UserClientTimerEndRequest,
        CP_UserFollowCharacterRequest,
        CP_UserFollowCharacterWithdraw,
        //CP_UserSelectPQReward,
        //CP_UserRequestPQReward,
        CP_SetPassenserResult,
        CP_BroadcastMsg,
        CP_GroupMessage, // PARTYCHAT
        CP_Whisper, // WHISPER
        //CP_CoupleMessage,
        CP_Messenger,
        CP_MiniRoom,
        CP_PartyRequest,
        CP_PartyResult,
        CP_ExpeditionRequest,
        CP_PartyAdverRequest,
        CP_GuildRequest,
        CP_GuildResult,
        CP_Admin,
        CP_Log,
        CP_FriendRequest,
        CP_MemoRequest,
        CP_MemoFlagRequest,
        CP_EnterTownPortalRequest,
        CP_EnterOpenGateRequest,
        CP_SlideRequest,
        CP_FuncKeyMappedModified,
        CP_RPSGame,
        CP_MarriageRequest,
        CP_WeddingWishListRequest,
        CP_WeddingProgress,
        CP_GuestBless,
        CP_BoobyTrapAlert,
        CP_StalkBegin,
        CP_AllianceRequest,
        CP_AllianceResult,
        CP_FamilyChartRequest,
        CP_FamilyInfoRequest,
        CP_FamilyRegisterJunior,
        CP_FamilyUnregisterJunior,
        CP_FamilyUnregisterParent,
        CP_FamilyJoinResult,
        CP_FamilyUsePrivilege,
        CP_FamilySetPrecept,
        CP_FamilySummonResult,
        CP_ChatBlockUserReq,
        CP_GuildBBS,
        CP_JMS_PINKBEAN_PORTAL_ENTER,
        CP_JMS_PINKBEAN_PORTAL_CREATE,
        CP_UserMigrateToITCRequest,
        CP_UserExpUpItemUseRequest,
        CP_UserTempExpUseRequest,
        CP_JMS_JUKEBOX, // ItemID 2150001
        CP_NewYearCardRequest,
        CP_RandomMorphRequest,
        CP_CashItemGachaponRequest,
        CP_CashGachaponOpenRequest,
        CP_ChangeMaplePointRequest,
        CP_TalkToTutor,
        CP_RequestIncCombo,
        CP_MobCrcKeyChangedReply,
        CP_RequestSessionValue,
        CP_UpdateGMBoard,
        CP_AccountMoreInfo,
        CP_FindFriend,
        CP_AcceptAPSPEvent,
        CP_UserDragonBallBoxRequest,
        CP_UserDragonBallSummonRequest,
        CP_BEGIN_PET,
        CP_PetMove, // MOVE_PET
        CP_PetAction,
        CP_PetInteractionRequest,
        CP_PetDropPickUpRequest,
        CP_PetStatChangeItemUseRequest,
        CP_PetUpdateExceptionListRequest,
        CP_END_PET,
        CP_BEGIN_SUMMONED,
        CP_SummonedMove,
        CP_SummonedAttack,
        CP_SummonedHit,
        CP_SummonedSkill,
        CP_Remove,
        CP_END_SUMMONED,
        CP_BEGIN_DRAGON,
        CP_DragonMove,
        CP_END_DRAGON,
        CP_QuickslotKeyMappedModified,
        CP_PassiveskillInfoUpdate,
        CP_UpdateScreenSetting,
        CP_UserAttackUser_Specific,
        CP_UserPamsSongUseRequest,
        //CP_QuestGuideRequest,
        //CP_UserRepeatEffectRemove,
        CP_END_USER,
        CP_BEGIN_FIELD,
        CP_BEGIN_LIFEPOOL,
        CP_BEGIN_MOB,
        CP_MobMove, // MOVE_LIFE
        CP_MobApplyCtrl, // AUTO_AGGRO
        CP_MobDropPickUpRequest,
        CP_MobHitByObstacle,
        CP_MobHitByMob,
        CP_MobSelfDestruct,
        CP_MobAttackMob,
        CP_MobSkillDelayEnd,
        CP_MobTimeBombEnd,
        CP_MobEscortCollision,
        CP_MobRequestEscortInfo,
        CP_MobEscortStopEndRequest,
        CP_END_MOB,
        CP_BEGIN_NPC,
        CP_NpcMove,
        CP_NpcSpecialAction,
        CP_END_NPC,
        CP_END_LIFEPOOL,
        CP_BEGIN_DROPPOOL,
        CP_DropPickUpRequest, // ITEM_PICKUP
        CP_END_DROPPOOL,
        CP_BEGIN_REACTORPOOL,
        CP_ReactorHit, // DAMAGE_REACTOR
        CP_ReactorTouch,
        CP_RequireFieldObstacleStatus,
        CP_END_REACTORPOOL,
        CP_BEGIN_EVENT_FIELD,
        CP_EventStart,
        CP_SnowBallHit,
        CP_SnowBallTouch,
        CP_CoconutHit,
        CP_TournamentMatchTable,
        CP_PulleyHit,
        CP_END_EVENT_FIELD,
        CP_BEGIN_MONSTER_CARNIVAL_FIELD,
        CP_MCarnivalRequest,
        CP_END_MONSTER_CARNIVAL_FIELD,
        CP_CONTISTATE, // 船の状態取得
        CP_BEGIN_PARTY_MATCH,
        CP_INVITE_PARTY_MATCH,
        CP_CANCEL_INVITE_PARTY_MATCH,
        CP_END_PARTY_MATCH,
        //CP_RequestFootHoldInfo,
        //CP_FootHoldInfo,
        CP_END_FIELD,
        // ポイントショップ
        CP_BEGIN_CASHSHOP,
        CP_CashShopChargeParamRequest,
        CP_CashShopQueryCashRequest,
        CP_CashShopCashItemRequest,
        CP_CashShopCheckCouponRequest,
        CP_CashShopGiftMateInfoRequest,
        CP_END_CASHSHOP,
        //CP_CheckSSN2OnCreateNewCharacter,
        //CP_CheckSPWOnCreateNewCharacter,
        //CP_FirstSSNOnCreateNewCharacter,
        // 多分絵具とかアイテム集める系のUI
        CP_BEGIN_RAISE,
        CP_RaiseRefesh,
        CP_RaiseUIState,
        CP_RaiseIncExp,
        CP_RaiseAddPiece,
        CP_END_RAISE,
        //CP_SendMateMail,
        //CP_RequestGuildBoardAuthKey,
        //CP_RequestConsultAuthKey,
        //CP_RequestClassCompetitionAuthKey,
        //CP_RequestWebBoardAuthKey,
        CP_BEGIN_ITEMUPGRADE,
        // ビシャスのハンマー
        CP_GoldHammerRequest,
        //CP_GoldHammerComplete,
        //CP_ItemUpgradeComplete,
        CP_END_ITEMUPGRADE,
        CP_BEGIN_BATTLERECORD,
        CP_BATTLERECORD_ONOFF_REQUEST,
        CP_END_BATTLERECORD,
        //CP_BEGIN_MAPLETV,
        CP_MapleTVSendMessageRequest,
        //CP_MapleTVUpdateViewCount,
        //CP_END_MAPLETV,
        // MTS
        CP_BEGIN_ITC,
        CP_ITCChargeParamRequest,
        CP_ITCQueryCashRequest,
        CP_ITCItemRequest,
        CP_END_ITC,
        //CP_BEGIN_CHARACTERSALE,
        //CP_CheckDuplicatedIDInCS,
        //CP_END_CHARACTERSALE,
        CP_LogoutGiftSelect,
        CP_NO,
        // ヘッダに対応する処理の名前を定義
        UNKNOWN_BEGIN,
        UNKNOWN,
        REACHED_LOGIN_SCREEN, // 名称不明, ログイン画面に到達
        WHEEL_OF_FORTUNE, // 不明
        GM_COMMAND_MAPLETV, // Super Megaphone Exploitのパケット
        SOLOMON, // 名称不明
        USE_TREASUER_CHEST, // 名称不明
        CYGNUS_SUMMON, // 名称不明
        LEFT_KNOCK_BACK, // イベント関連?
        RECOMMENDED_AVATAR, // おすすめアバター
        UPDATE_QUEST, // 不明
        QUEST_ITEM, // 不明
        USE_ITEM_QUEST, // 多分Quest Value Addition Exploitのパケット
        BEANS_OPERATION, // PACHINKO_GAME, パチンコ
        BEANS_UPDATE, // PACHINKO_UPDATE, パチンコ
        UNKNOWN_END;

        // 定義値の変更や取得
        private int value;

        Header(int header) {
            value = header;
        }

        Header() {
            value = 0xFFFF;
        }

        public boolean Set(int header) {
            value = header;
            return true;
        }

        public int Get() {
            return value;
        }

    }

    // チート対策
    public static void SetCustomHeader() {
        // WZファイルのハッシュ値の送信
        Header.CP_CUSTOM_WZ_HASH.Set(0x77AA);
        // メモリスキャン
        Header.CP_CUSTOM_MEMORY_SCAN.Set(0x77BB);
    }

    public static boolean Load(Properties props) {

        for (Header header : Header.values()) {
            String[] vars = props.getProperty(header.name(), "@FFFF").trim().split(" ");
            int base = -1;
            int offset = 0;
            switch (vars.length) {
                case 1: {
                    if ("@FFFF".length() <= vars[0].length() && vars[0].charAt(0) == '@') {
                        base = Integer.parseInt(vars[0].substring(1), 16);
                    } else {
                        base = Integer.parseInt(vars[0]);
                    }
                    break;
                }
                case 3: {
                    // + or -
                    if (vars[1].length() != 1 || (vars[1].charAt(0) != '+' && vars[1].charAt(0) != '-')) {
                        continue;
                    }
                    offset = Integer.parseInt(vars[2]);
                    if (vars[1].charAt(0) == '-') {
                        offset = -offset;
                    }
                    // get base value
                    if ("@FFFF".length() <= vars[0].length() && vars[0].charAt(0) == '@') {
                        base = Integer.parseInt(vars[0].substring(1), 16);
                    } else {
                        for (Header base_header : Header.values()) {
                            if (base_header.name().equals(vars[0])) {
                                base = base_header.Get();
                                break;
                            }
                        }
                    }
                    break;
                }
                default: {
                    break;
                }
            }

            if (base == -1) {
                continue;
            }

            header.Set((short) (base + offset));
        }

        return true;
    }
}
