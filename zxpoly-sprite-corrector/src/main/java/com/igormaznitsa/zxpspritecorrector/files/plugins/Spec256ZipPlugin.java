/*
 * Copyright (C) 2020 igorm
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.igormaznitsa.zxpspritecorrector.files.plugins;

import com.igormaznitsa.jbbp.io.JBBPByteOrder;
import com.igormaznitsa.jbbp.io.JBBPOut;
import com.igormaznitsa.zxpspritecorrector.components.ZXPolyData;
import com.igormaznitsa.zxpspritecorrector.files.Info;
import com.igormaznitsa.zxpspritecorrector.files.SessionData;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.PAGE_SIZE;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.VERSION_1;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.VERSION_2;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.VERSION_3A;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.VERSION_3B;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.Z80_MAINPART;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.convertZ80BankIndexesToPages;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.getVersion;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.is48k;
import static com.igormaznitsa.zxpspritecorrector.files.plugins.Z80Plugin.makePair;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.FilenameUtils;

public class Spec256ZipPlugin extends AbstractFilePlugin {

    public static final int[] ARGB_PALETTE_ZXPOLY = new int[]{
        0xFF000000,
        0xFF0000BE,
        0xFFBE0000,
        0xFFBE00BE,
        0xFF00BE00,
        0xFF00BEBE,
        0xFFBEBE00,
        0xFFBEBEBE,
        0xFF000000,
        0xFF0000FF,
        0xFFFF0000,
        0xFFFF00FF,
        0xFF00FF00,
        0xFF00FFFF,
        0xFFFFFF00,
        0xFFFFFFFF};

    private static final int[] PALETTE_ARGB_SPEC256 = readSpec256RawPalette();
    private static final byte[] MAP_ZXPOLY2SPEC256INDEX = makePaletteMapIndexes(PALETTE_ARGB_SPEC256);

    @Override
    public boolean isImportable() {
        return false;
    }

    @Override
    public String getToolTip(final boolean forExport) {
        return "Spec256 ZIP container";
    }

    @Override
    public boolean doesContainInternalFileItems() {
        return false;
    }

    @Override
    public FileFilter getImportFileFilter() {
        return this;
    }

    @Override
    public FileFilter getExportFileFilter() {
        return this;
    }

    @Override
    public String getPluginDescription(final boolean forExport) {
        return "ZIP Spec256 container";
    }

    @Override
    public String getPluginUID() {
        return "S2ZP";
    }

    @Override
    public List<Info> getImportingContainerFileList(final File file) {
        return Collections.emptyList();
    }

    @Override
    public String getExtension(final boolean forExport) {
        return "zip";
    }

    @Override
    public ReadResult readFrom(final File file, final int index) throws IOException {
        throw new IOException("Reading is unsupported");
    }

    @Override
    public void writeTo(
            final File file,
            final ZXPolyData data,
            final SessionData sessionData) throws IOException {
        if (!(data.getPlugin() instanceof Z80Plugin)) {
            throw new IOException("Only imported Z80 snapshot can be exported");
        }

        final byte[] extraData = data.getInfo().getExtra();
        final byte[] bankIndexes;

        final int banksInExtra;
        if (extraData[0] == 0) {
            banksInExtra = 0;
            bankIndexes = new byte[]{8, 4, 5};
        } else {
            bankIndexes = new byte[extraData[0] & 0xFF];
            banksInExtra = bankIndexes.length;
            for (int i = 0; i < bankIndexes.length; i++) {
                bankIndexes[i] = extraData[i + 1];
            }
        }
        final byte[] z80header = Arrays.copyOfRange(extraData, banksInExtra + 1, extraData.length);
        final int version = getVersion(z80header);
        final boolean mode48 = is48k(version, z80header);
        final Z80Plugin.Z80MainHeader mheader = Z80_MAINPART.parse(z80header).mapTo(new Z80Plugin.Z80MainHeader());
        final int regPc = version == VERSION_1 ? mheader.reg_pc : ((z80header[32] & 0xFF) << 8) | (z80header[33] & 0xFF);
        final byte[] pageIndexes = convertZ80BankIndexesToPages(bankIndexes, mode48, version);

        final int port7ffd;
        if (version == VERSION_1) {
            port7ffd = 0x30;
        } else {
            final int hwmode = z80header[34];
            switch (version) {
                case VERSION_2: {
                    if (hwmode == 3 || hwmode == 4) {
                        port7ffd = z80header[35] & 0xFF;
                    } else {
                        port7ffd = 0x30;
                    }
                }
                break;
                case VERSION_3A:
                case VERSION_3B: {
                    if (hwmode == 4 || hwmode == 5 || hwmode == 6) {
                        port7ffd = z80header[35] & 0xFF;
                    } else {
                        port7ffd = 0x30;
                    }
                }
                break;
                default:
                    port7ffd = 0x30;
            }
        }

        final JBBPOut sna = JBBPOut.BeginBin()
                .Byte(makeSnaHeaderFromZ80Header(mheader, mode48));

        if (mode48) {
            for (int i = 0; i<3; i++) {
                sna.Byte(getPhysicalBasePage(i, data));
            }
        } else {
            sna.Byte(getPhysicalBasePage(5, data));
            sna.Byte(getPhysicalBasePage(2, data));
            sna.Byte(getPhysicalBasePage(port7ffd & 7, data));

            sna.Short(regPc)
                    .Byte(port7ffd)
                    .Byte(0);

            for (int i : pageIndexes) {
                if (i == 2 || i == 5 || i == (port7ffd & 7)) {
                    continue;
                }
                sna.Byte(data.getBaseData(i));
            }
        }

        final byte[] snaData = sna.End().toByteArray();
        JBBPOut gfxOut = JBBPOut.BeginBin();
        
        int pageIndex = 0;
        for (byte page = 0; page < 3; page++) {
            final byte[] pageMask = getPhysicalMaskPage(page, data);
            final byte[] baseData = getPhysicalBasePage(page, data);

            final byte[] cpuData0 = getPhysicalCpuPage(0, page, data);
            final byte[] cpuData1 = getPhysicalCpuPage(1, page, data);
            final byte[] cpuData2 = getPhysicalCpuPage(2, page, data);
            final byte[] cpuData3 = getPhysicalCpuPage(3, page, data);

            pageIndex++;

            for (int i = 0; i < PAGE_SIZE; i++) {
                if (pageMask[i] == 0) {
                    final byte [] baseCopies = new byte[8];
                    Arrays.fill(baseCopies, baseData[i]);
                    gfxOut.Byte(baseCopies);
                } else {
                    gfxOut.Byte(zxPoly2Spec256(
                            cpuData0[i],
                            cpuData1[i],
                            cpuData2[i],
                            cpuData3[i],
                            baseData[i],
                            pageMask[i]));
                }
            }
        }

        saveSpec256Zip(file, snaData, gfxOut.End().toByteArray());
    }

    public static byte [] getPhysicalCpuPage(final int cpu, final int page, final ZXPolyData data) {
        final byte[] bankData = new byte[PAGE_SIZE];
        System.arraycopy(data.getDataForCPU(cpu), page  * PAGE_SIZE, bankData, 0, PAGE_SIZE);
        return bankData;
    }

    
    public static byte[] getPhysicalMaskPage(final int page, final ZXPolyData data) {
        final byte[] maskData = new byte[PAGE_SIZE];
        System.arraycopy(data.getMask(), page * PAGE_SIZE, maskData, 0, PAGE_SIZE);
        return maskData;
    }

    public static byte[] getPhysicalBasePage(final int page, final ZXPolyData data) {
        final byte[] baseData = new byte[PAGE_SIZE];
        System.arraycopy(data.getBaseData(), page * PAGE_SIZE, baseData, 0, PAGE_SIZE);
        return baseData;
    }

    private static byte findCloseIndex(final int argb, final int[] argbSpec256Palette, final int lowIndex, final int highIndex) {
        final int r = (argb >>> 16) & 0xFF;
        final int g = (argb >>> 8) & 0xFF;
        final int b = argb & 0xFF;

        double lastDistance = Double.MAX_VALUE;
        int lastIndex = lowIndex;

        for (int i = lowIndex; i < highIndex; i++) {
            final int currentIndex = i;

            final int zr = (argbSpec256Palette[i] >>> 16) & 0xFF;
            final int zg = (argbSpec256Palette[i] >>> 8) & 0xFF;
            final int zb = argbSpec256Palette[i] & 0xFF;

            final double distance = Math.sqrt((Math.pow(r - zr, 2) + Math.pow(g - zg, 2) + Math.pow(b - zb, 2)));
            if (distance < lastDistance) {
                lastIndex = i;
                lastDistance = distance;
            }
        }

        return (byte) lastIndex;
    }

    private static byte[] makePaletteMapIndexes(final int[] argbSpec256Palette) {
        final byte[] result = new byte[ARGB_PALETTE_ZXPOLY.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = findCloseIndex(ARGB_PALETTE_ZXPOLY[i], argbSpec256Palette, 0,191);
        }

        return result;
    }

    private static int[] readSpec256RawPalette() {
        try (InputStream in = Spec256ZipPlugin.class.getResourceAsStream("/spec256.pal")) {
            final int[] result = new int[256];

            for (int i = 0; i < 256; i++) {
                final int red = in.read();
                final int green = in.read();
                final int blue = in.read();
                if (red < 0 || green < 0 || blue < 0) {
                    throw new EOFException();
                }
                result[i] = 0xFF000000 | (red << 16) | (green << 8) | blue;
            }
            return result;
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    private static byte[] zxPoly2Spec256(
            byte cpu0,
            byte cpu1,
            byte cpu2,
            byte cpu3,
            byte baseData,
            byte maskData
    ) {
        final byte[] bankBytes = new byte[8];

        if (maskData == 0){
            Arrays.fill(bankBytes, baseData);
        } else {
            for(int i = 0; i < 8; i ++) {
                if ((maskData & 1) == 0) {
                    bankBytes[i] = (baseData & 1) == 0 ? 0 : (byte)0xFF;
                } else {
                    final int polyPaletteIndex = ((cpu3 & 1) == 0 ? 0 : 0x08)
                            | ((cpu0 & 1) == 0 ? 0 : 0x04)
                            | ((cpu1 & 1) == 0 ? 0 : 0x02)
                            | ((cpu2 & 1) == 0 ? 0 : 0x01);
 
                    bankBytes[i] = MAP_ZXPOLY2SPEC256INDEX[polyPaletteIndex];
                }
                
                cpu0 >>>= 1;
                cpu1 >>>= 1;
                cpu2 >>>= 1;
                cpu3 >>>= 1;
                maskData >>>= 1;
                baseData >>>= 1;
            }
        }
        return bankBytes;//packSpec256(bankBytes);
    }

    private void saveSpec256Zip(final File file, final byte[] snaData, final byte[] gfxData) throws IOException {
        final String name = FilenameUtils.getBaseName(file.getName()).toUpperCase(Locale.ENGLISH);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))) {
            final ZipEntry snaEntry = new ZipEntry(name + ".SNA");
            zos.putNextEntry(snaEntry);
            zos.write(snaData);
            zos.closeEntry();

            final ZipEntry gfxEntry = new ZipEntry(name + ".GFX");
            zos.putNextEntry(gfxEntry);
            zos.write(gfxData);
            zos.closeEntry();

//            final ZipEntry cfgEntry = new ZipEntry(name + ".CFG");
//            zos.putNextEntry(cfgEntry);
//            zos.write(makeCfg().getBytes(StandardCharsets.UTF_8));
//            zos.closeEntry();

            zos.finish();
        }
    }

    private String makeCfg() {
        return "GFXLeveledXOR=0\n"
                + "GFXLeveledOR=0\n"
                + "GFXLeveledAND=0\n"
                + "GFXScreenXORbuffered=0\n"
                + "OrderPaletteSignedBytes=0\n"
                + "UpColorsMixed=1\n"
                + "DownColorsMixed=0\n"
                + "UpMixChgBright=50\n"
                + "DownMixChgBright=0\n"
                + "UseBrightInMix=0\n"
                + "UpMixPaper=0\n"
                + "DownMixPaper=1\n"
                + "BkMixed=0\n"
                + "BkMixBkAttr=0\n"
                + "BkOverFF=0";
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory()
                || f.getName().toLowerCase(Locale.ENGLISH).endsWith(".zip");
    }

    @Override
    public String getDescription() {
        return this.getToolTip(true) + " (*.zip)";
    }

    private byte[] makeSnaHeaderFromZ80Header(
            final Z80Plugin.Z80MainHeader z80header,
            final boolean pcOnStack
    ) throws IOException {
        return JBBPOut.BeginBin(JBBPByteOrder.LITTLE_ENDIAN)
                .Byte(z80header.reg_ir)
                .Short(z80header.reg_hl_alt)
                .Short(z80header.reg_de_alt)
                .Short(z80header.reg_bc_alt)
                .Short(makePair(z80header.reg_a_alt, z80header.reg_f_alt))
                .Short(z80header.reg_hl)
                .Short(z80header.reg_de)
                .Short(z80header.reg_bc)
                .Short(z80header.reg_iy)
                .Short(z80header.reg_ix)
                .Byte(z80header.iff2 == 0 ? 0 : 4)
                .Byte(z80header.reg_r)
                .Short(makePair(z80header.reg_a, z80header.reg_f))
                .Short(z80header.reg_sp - (pcOnStack ? 2 : 0))
                .Byte(z80header.emulFlags.interruptmode)
                .Byte(z80header.flags.bordercolor)
                .End()
                .toByteArray();
    }
}
