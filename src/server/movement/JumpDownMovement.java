/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server.movement;

import java.awt.Point;
import packet.ServerPacket;

import tools.data.output.LittleEndianWriter;

public class JumpDownMovement extends AbstractLifeMovement {

    private Point pixelsPerSecond;
    private Point offset;
    private int unk;
    private int fh;

    public JumpDownMovement(int type, Point position, int duration, int newstate) {
        super(type, position, duration, newstate);
    }

    public Point getPixelsPerSecond() {
        return pixelsPerSecond;
    }

    public void setPixelsPerSecond(Point wobble) {
        this.pixelsPerSecond = wobble;
    }

    public Point getOffset() {
        return offset;
    }

    public void setOffset(Point wobble) {
        this.offset = wobble;
    }

    public int getUnk() {
        return unk;
    }

    public void setUnk(int unk) {
        this.unk = unk;
    }

    public int getFH() {
        return fh;
    }

    public void setFH(int fh) {
        this.fh = fh;
    }

    @Override
    public void serialize(LittleEndianWriter lew) {
        lew.write(getType());
        lew.writePos(getPosition());
        lew.writePos(pixelsPerSecond);
        lew.writeShort(unk);
        lew.writeShort(fh);
        lew.writePos(offset);
        lew.write(getNewstate());
        lew.writeShort(getDuration());
    }

    @Override
    public void serialize(ServerPacket data) {
        data.Encode1(getType());
        data.Encode2(getPosition().x);
        data.Encode2(getPosition().y);
        data.Encode2(pixelsPerSecond.x);
        data.Encode2(pixelsPerSecond.y);
        data.Encode2(unk);
        data.Encode2(fh);
        data.Encode2(offset.x);
        data.Encode2(offset.y);
        data.Encode1(getNewstate());
        data.Encode2(getDuration());
    }
}
