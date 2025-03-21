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
package tools.data.input;

import config.ServerConfig;
import debug.Debug;
import java.awt.Point;

/**
 * Provides a generic interface to a Little Endian stream of bytes.
 *
 * @version 1.0
 * @author Frz
 * @since Revision 323
 */
public class GenericLittleEndianAccessor implements LittleEndianAccessor {

    private final ByteInputStream bs;
    private boolean already_used = false;

    /**
     * Class constructor - Wraps the accessor around a stream of bytes.
     *
     * @param bs The byte stream to wrap the accessor around.
     */
    public GenericLittleEndianAccessor(final ByteInputStream bs) {
        this.bs = bs;
    }

    @Override
    public final int readByteAsInt() {
        return bs.readByte();
    }

    /**
     * Read a single byte from the stream.
     *
     * @return The byte read.
     * @see net.sf.odinms.tools.data.input.ByteInputStream#readByte
     */
    @Override
    public final byte readByte() {
        if (!already_used) {
            already_used = true;
            Debug.DebugLog("OLD_CLIENT_PACKET readByte");
            StackTraceElement[] ste = new Throwable().getStackTrace();
            if (1 < ste.length) {
                Debug.DebugLog(ste[1].getFileName() + ":" + ste[1].getLineNumber());
                Debug.DebugLog(ste[1].getClassName() + "." + ste[1].getMethodName());
            }
        }
        return (byte) bs.readByte();
    }

    /**
     * Reads an integer from the stream.
     *
     * @return The integer read.
     */
    @Override
    public final int readInt() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();

        if (!already_used) {
            already_used = true;
            Debug.DebugLog("OLD_CLIENT_PACKET readInt");
            StackTraceElement[] ste = new Throwable().getStackTrace();
            if (1 < ste.length) {
                Debug.DebugLog(ste[1].getFileName() + ":" + ste[1].getLineNumber());
                Debug.DebugLog(ste[1].getClassName() + "." + ste[1].getMethodName());
            }
        }

        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    /**
     * Reads a short integer from the stream.
     *
     * @return The short read.
     */
    @Override
    public final short readShort() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();

        if (!already_used) {
            StackTraceElement[] ste = new Throwable().getStackTrace();
            if (1 < ste.length) {
                if ("messageReceived".equals(ste[1].getMethodName())) {
                } else {
                    already_used = true;
                    Debug.DebugLog("OLD_CLIENT_PACKET readShort");
                    Debug.DebugLog(ste[1].getFileName() + ":" + ste[1].getLineNumber());
                    Debug.DebugLog(ste[1].getClassName() + "." + ste[1].getMethodName());
                }
            }
        }

        return (short) ((byte2 << 8) + byte1);
    }

    /**
     * Reads a single character from the stream.
     *
     * @return The character read.
     */
    @Override
    public final char readChar() {
        return (char) readShort();
    }

    /**
     * Reads a long integer from the stream.
     *
     * @return The long integer read.
     */
    @Override
    public final long readLong() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        final int byte5 = bs.readByte();
        final int byte6 = bs.readByte();
        final int byte7 = bs.readByte();
        final int byte8 = bs.readByte();

        return (long) ((byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32) + (byte4 << 24) + (byte3 << 16)
                + (byte2 << 8) + byte1);
    }

    /**
     * Reads a floating point integer from the stream.
     *
     * @return The float-type integer read.
     */
    @Override
    public final float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Reads a double-precision integer from the stream.
     *
     * @return The double-type integer read.
     */
    @Override
    public final double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Reads an ASCII string from the stream with length <code>n</code>.
     *
     * @param n Number of characters to read.
     * @return The string read.
     */
    public final String readAsciiString(final int n) {
        final byte ret[] = new byte[n];
        for (int x = 0; x < n; x++) {
            ret[x] = readByte();
        }

        String conv_str = new String(ret, ServerConfig.utf8 ? ServerConfig.codepage_utf8 : ServerConfig.codepage_ascii);
        return conv_str;
    }

    /**
     * Gets the number of bytes read from the stream so far.
     *
     * @return A long integer representing the number of bytes read.
     * @see net.sf.odinms.tools.data.input.ByteInputStream#getBytesRead()
     */
    public final long getBytesRead() {
        return bs.getBytesRead();
    }

    /**
     * Reads a MapleStory convention lengthed ASCII string. This consists of a
     * short integer telling the length of the string, then the string itself.
     *
     * @return The string read.
     */
    @Override
    public final String readMapleAsciiString() {
        return readAsciiString(readShort());
    }

    /**
     * Reads a MapleStory Position information. This consists of 2 short
     * integer.
     *
     * @return The Position read.
     */
    @Override
    public final Point readPos() {
        final int x = readShort();
        final int y = readShort();
        return new Point(x, y);
    }

    /**
     * Reads <code>num</code> bytes off the stream.
     *
     * @param num The number of bytes to read.
     * @return An array of bytes with the length of <code>num</code>
     */
    @Override
    public final byte[] read(final int num) {
        byte[] ret = new byte[num];
        for (int x = 0; x < num; x++) {
            ret[x] = readByte();
        }
        return ret;
    }

    /**
     * Skips the current position of the stream <code>num</code> bytes ahead.
     *
     * @param num Number of bytes to skip.
     */
    @Override
    public void skip(final int num) {
        for (int x = 0; x < num; x++) {
            readByte();
        }
        if (!already_used) {
            already_used = true;
            Debug.DebugLog("OLD_CLIENT_PACKET skip");
            StackTraceElement[] ste = new Throwable().getStackTrace();
            if (1 < ste.length) {
                Debug.DebugLog(ste[1].getFileName() + ":" + ste[1].getLineNumber());
                Debug.DebugLog(ste[1].getClassName() + "." + ste[1].getMethodName());
            }
        }
    }

    /**
     * @see net.sf.odinms.tools.data.input.ByteInputStream#available
     */
    @Override
    public final long available() {
        return bs.available();
    }

    /**
     * @see java.lang.Object#toString
     */
    @Override
    public final String toString() {
        return bs.toString();
    }

    @Override
    public final String toString(final boolean b) {
        return bs.toString(b);
    }

}
