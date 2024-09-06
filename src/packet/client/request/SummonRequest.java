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
package packet.client.request;

import client.ISkill;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import client.SummonSkillEntry;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import config.ServerConfig;
import debug.Debug;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import packet.client.ClientPacket;
import packet.client.request.struct.CMovePath;
import packet.server.response.MobResponse;
import packet.server.response.SummonResponse;
import server.MapleStatEffect;
import server.life.MapleMonster;
import server.life.SummonAttackEntry;
import server.maps.MapleMap;
import server.maps.MapleSummon;
import server.maps.SummonMovementType;

/**
 *
 * @author Riremito
 */
public class SummonRequest {

    // CUser::OnSummonedPacket
    public static boolean OnPacket(ClientPacket cp, ClientPacket.Header header, MapleClient c) {
        MapleCharacter chr = c.getPlayer();
        if (chr == null) {
            return false;
        }

        MapleMap map = chr.getMap();
        if (map == null) {
            return false;
        }

        int oid = cp.Decode4(); // older version = SkillID

        MapleSummon summon = null;
        if ((ServerConfig.IsJMS() && ServerConfig.GetVersion() <= 131)) {
            for (MapleSummon sms : chr.getSummons().values()) {
                if (sms.getSkill() == oid) {
                    summon = sms;
                    break;
                }
            }
        } else {
            summon = map.getSummonByOid(oid);
        }

        if (summon == null) {
            return false;
        }

        switch (header) {
            case CP_SummonedMove: {
                // CSummoned::OnMove
                // CField::OnSummonedMove
                OnMove(cp, chr, summon);
                return true;
            }
            case CP_SummonedAttack: {
                SummonAttack(cp, summon, chr);
                return true;
            }
            case CP_SummonedHit: {
                DamageSummon(cp, chr);
                return true;
            }
            case CP_SummonedSkill: {
                // ?
                break;
            }
            case CP_Remove: {
                // ?
                break;
            }
            default: {
                break;
            }
        }

        Debug.ErrorLog("Not coded: " + cp.GetOpcodeName());
        return false;
    }

    public static boolean OnMove(ClientPacket cp, MapleCharacter chr, MapleSummon summon) {
        if (summon.getMovementType() == SummonMovementType.STATIONARY || summon.isChangedMap()) {
            return false;
        }

        CMovePath data = CMovePath.Decode(cp);
        summon.setStance(data.getAction());
        summon.setPosition(data.getEnd());
        chr.getMap().broadcastMessage(chr, SummonResponse.moveSummon(summon, data), summon.getPosition());
        return true;
    }

    // SummonAttack
    public static void SummonAttack(ClientPacket cp, MapleSummon summon, MapleCharacter chr) {
        final MapleMap map = chr.getMap();

        final SummonSkillEntry sse = SkillFactory.getSummonData(summon.getSkill());

        if (sse == null) {
            return;
        }

        if (!(ServerConfig.IsJMS() && ServerConfig.GetVersion() <= 131)) {
            cp.Decode4();
            cp.Decode4();

            int tick = cp.Decode4();
            chr.updateTick(tick);
            summon.CheckSummonAttackFrequency(chr, tick);

            cp.Decode4();
            cp.Decode4();
        }

        final byte animation = cp.Decode1();

        if (!(ServerConfig.IsJMS() && ServerConfig.GetVersion() <= 131)) {
            cp.Decode4();
            cp.Decode4();
        }

        final byte numAttacked = cp.Decode1();

        if (!(ServerConfig.IsJMS() && ServerConfig.GetVersion() <= 131)) {
            cp.Decode2(); // x
            cp.Decode2(); // y
            cp.Decode2(); // x
            cp.Decode2(); // y
        }

        final List<SummonAttackEntry> allDamage = new ArrayList<SummonAttackEntry>();
        chr.getCheatTracker().checkSummonAttack();

        for (int i = 0; i < numAttacked; i++) {
            final MapleMonster mob = map.getMonsterByOid(cp.Decode4());

            if (mob == null) {
                continue;
            }

            if (!(ServerConfig.IsJMS() && ServerConfig.GetVersion() < 186)) {
                cp.Decode4(); // MobID
            }

            cp.Decode1();
            cp.Decode1();
            cp.Decode1();
            cp.Decode1();
            cp.Decode2();
            cp.Decode2();
            cp.Decode2();
            cp.Decode2();
            cp.Decode2();

            final int damage = cp.Decode4();
            allDamage.add(new SummonAttackEntry(mob, damage));
        }

        if ((ServerConfig.IsJMS() && ServerConfig.GetVersion() <= 131)) {
            cp.Decode2(); // X
            cp.Decode2(); // Y
        }

        if (!summon.isChangedMap()) {
            map.broadcastMessage(chr, SummonResponse.summonAttack(summon.getOwnerId(), summon.getObjectId(), animation, allDamage, chr.getLevel()), summon.getPosition());
        }
        final ISkill summonSkill = SkillFactory.getSkill(summon.getSkill());
        final MapleStatEffect summonEffect = summonSkill.getEffect(summon.getSkillLevel());

        if (summonEffect == null) {
            return;
        }
        for (SummonAttackEntry attackEntry : allDamage) {
            final int toDamage = attackEntry.getDamage();
            final MapleMonster mob = attackEntry.getMonster();

            if (toDamage > 0 && summonEffect.getMonsterStati().size() > 0) {
                if (summonEffect.makeChanceResult()) {
                    for (Map.Entry<MonsterStatus, Integer> z : summonEffect.getMonsterStati().entrySet()) {
                        mob.applyStatus(chr, new MonsterStatusEffect(z.getKey(), z.getValue(), summonSkill.getId(), null, false), summonEffect.isPoison(), 4000, false);
                    }
                }
            }
            mob.damage(chr, toDamage, true);
            chr.checkMonsterAggro(mob);
            if (!mob.isAlive()) {
                chr.getClient().SendPacket(MobResponse.Kill(mob, 1));
            }
        }

        if (summon.isGaviota()) {
            chr.getMap().broadcastMessage(SummonResponse.removeSummon(summon, true));
            chr.getMap().removeMapObject(summon);
            chr.removeVisibleMapObject(summon);
            chr.cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
            chr.cancelEffectFromBuffStat(MapleBuffStat.REAPER);
        }
    }

    public static final void DamageSummon(ClientPacket p, final MapleCharacter chr) {
        final int unkByte = p.Decode1();
        final int damage = p.Decode4();
        final int monsterIdFrom = p.Decode4();

        final Iterator<MapleSummon> iter = chr.getSummons().values().iterator();
        MapleSummon summon;

        while (iter.hasNext()) {
            summon = iter.next();
            if (summon.isPuppet() && summon.getOwnerId() == chr.getId()) { //We can only have one puppet(AFAIK O.O) so this check is safe.
                summon.addHP((short) -damage);
                if (summon.getHP() <= 0) {
                    chr.cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
                }
                chr.getMap().broadcastMessage(chr, SummonResponse.damageSummon(chr.getId(), summon.getSkill(), damage, unkByte, monsterIdFrom), summon.getPosition());
                break;
            }
        }
    }
}
