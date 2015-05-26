/** ImageToGif
 * 
 *  @version 010425
 *  
 */
package utils;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Vector;

public class ImageToGif extends Canvas {

    public ImageToGif(Image image1, OutputStream outputstream)
            throws IOException {
        image = image1;
        output = outputstream;
    }

    public void startProc() {
        try {
            code = new short[5003];
            prefix = new short[5003];
            codeCharacter = new short[5003];
            Block = new short[256];
            if (image != null && getImageInfo() && openGif() && calcGif()) {
                closeGif();
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    boolean getImageInfo() {
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int ai1[] = {
            0xffffff, 0xffffff, 0xe0e0c0, 0xe0e0e0, 0xf0f0f0, 0xf8f8f8, 0xfcfcfc, 0xfefefe, 0xffffff, 0xffffff
        };
        try {
            imgWidth = (short) image.getWidth(this);
            imgHeight = (short) image.getHeight(this);
            int l = imgWidth * imgHeight;
            int ai[] = new int[l];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, imgWidth, imgHeight, ai, 0, imgWidth);
            Vector vector = new Vector(256);
            Vector vector1 = new Vector(1);
            Integer ainteger[] = new Integer[0];
            Integer ainteger1[] = new Integer[1];
            pixelgrabber.grabPixels();
            vector1.addElement(new Integer(0));
            while (j1 <= 256 || ai1[i1] == 0xffffff) {
                ainteger = new Integer[vector.size()];
                vector.copyInto(ainteger);
                k1 = j1;
                i1++;
                vector.removeAllElements();
                vector.addElement(new Integer(ai[0] & ai1[i1]));
                vector.addElement(new Integer(0x1000000));
                j1 = 2;
                palSize = 2;
                boolean flag1 = true;
                byte byte0 = 2;
                for (int l1 = 1; l1 < l; l1++) {
                    int i = ai[l1] & ai1[i1];
                    int j = palSize - 1;
                    int k = palSize;
                    boolean flag;
                    do {
                        vector1.setElementAt(vector.elementAt(j), 0);
                        vector1.copyInto(ainteger1);
                        if (i == ainteger1[0].intValue()) {
                            k = 0;
                            flag = true;
                        } else {
                            k /= 2;
                            if (i < ainteger1[0].intValue()) {
                                j -= k;
                            } else {
                                j += k;
                            }
                            flag = false;
                        }
                    } while (k > 0);
                    if (flag) {
                        continue;
                    }
                    vector1.setElementAt(vector.elementAt(j), 0);
                    vector1.copyInto(ainteger1);
                    if (i < ainteger1[0].intValue()) {
                        vector.insertElementAt(new Integer(ai[l1] & ai1[i1]), j);
                    } else {
                        vector.insertElementAt(new Integer(ai[l1] & ai1[i1]), j + 1);
                    }
                    if (j1++ > 256) {
                        break;
                    }
                    vector.removeElement(vector.lastElement());
                    if (j1 > palSize) {
                        for (int j2 = palSize; j2 < 2 * palSize; j2++) {
                            vector.addElement(new Integer(0x1000000));
                        }
                        palSize *= 2;
                    }
                }

                if (--j1 <= 256 && ai1[i1] == 0xffffff) {
                    ainteger = new Integer[vector.size()];
                    vector.copyInto(ainteger);
                    k1 = j1;
                    break;
                }
                if (j1 > 256 && ai1[i1] != 0xffffff || i1 > 3 && ai1[i1] == 0xffffff) {
                    break;
                }
            }
            i1--;
            palSize = k1;
            palArr = new int[palSize];
            for (int i2 = 0; i2 < palSize; i2++) {
                palArr[i2] = ainteger[i2].intValue() | (ai1[i1] ^ 0xffffff) >> 1 & (ai1[i1] ^ 0xffffff);
            }
            pixCol = new short[l];
            for (int k2 = 0; k2 < l; k2++) {
                for (short word0 = 0; word0 < palSize; word0++) {
                    if ((ai[k2] & ai1[i1]) != (palArr[word0] & ai1[i1])) {
                        continue;
                    }
                    pixCol[k2] = word0;
                    break;
                }

            }

        } catch (Exception _ex) {
            return false;
        }
        return true;
    }

    boolean openGif() {
        try {
            dataOut = new DataOutputStream(output);
        } catch (RuntimeException _ex) {
            return false;
        }
        return true;
    }

    char MSBtoLSB(short word0) {
        short word1 = (short) (word0 >> 8);
        short word2 = (short) ((word0 & 0xff) << 8);
        return (char) (word2 + word1);
    }

    boolean calcGif() {
        try {
            return writeHeader() && writeLSD() && writeGlobalPalette() && writeImageDescriptor() && writeDataBlocks();
        } catch (Exception _ex) {
            return false;
        }
    }

    boolean writeDataBlocks() {
        int ai[] = {
            0, 1, 3, 7, 15, 31, 63, 127, 255, 511,
            1023, 2047, 4095, 8191
        };
        try {
            int j = (flagLSD & 7) + 1;
            if (j > 2) {
                sOriginalCodeSize = (short) j;
            } else {
                sOriginalCodeSize = 2;
            }
            dataOut.writeByte((byte) sOriginalCodeSize);
            CalcSpecialCodes();
            ClearCompressionTable();
            Block[0] = 0;
            BlockPos = 1;
            lzwData = 0;
            lzwBits = 0;
            if (!lzwOutput(clearCode)) {
                return false;
            }
            int l = 0;
            int i = l * imgWidth;
            short word3 = (short) (pixCol[i++] & 0xff);
            int k = 1;
            while (l < imgHeight) {
                short word2 = pixCol[i++];
                short word0 = findSlot(word3, word2);
                if (code[word0] != -1) {
                    word3 = code[word0];
                } else {
                    if (freeSlot <= ai[sActCodeSize] + 1) {
                        code[word0] = freeSlot++;
                        prefix[word0] = word3;
                        codeCharacter[word0] = word2;
                    }
                    if (!lzwOutput(word3)) {
                        return false;
                    }
                    word3 = word2;
                    if (freeSlot > ai[sActCodeSize] + 1 || freeSlot == 4096) {
                        if (sActCodeSize < 12) {
                            sActCodeSize++;
                        } else {
                            if (!lzwOutput(clearCode)) {
                                return false;
                            }
                            sActCodeSize = sOriginalCodeSize;
                            CalcSpecialCodes();
                            ClearCompressionTable();
                        }
                    }
                }
                if (++k == imgWidth) {
                    k = 0;
                    if (++l < imgHeight) {
                        i = l * imgWidth;
                    }
                }
            }
            if (!lzwOutput(word3)) {
                return false;
            }
            if (!lzwOutput((short) 0)) {
                return false;
            }
            if (!lzwOutput(endCode)) {
                return false;
            }
            if (!lzwOutput((short) 0)) {
                return false;
            }
            if (Block[0] != 0) {
                dataOut.writeByte(Block[0]);
                for (short word1 = 1; word1 <= Block[0];) {
                    dataOut.writeByte(Block[word1++]);
                }
                dataOut.writeByte(0);
            }
            return true;
        } catch (Exception _ex) {
            return false;
        }
    }

    void ClearCompressionTable() {
        for (short word0 = 0; word0 < 5003; word0++) {
            code[word0] = -1;
        }
    }

    short findSlot(short word0, short word1) {
        short word2 = (short) (word1 << 4 ^ word0);
        short word3 = (short) (word2 != 0 ? 5003 - word2 : 1);
        do {
            do {
                if (code[word2] == -1) {
                    return word2;
                }
                if (prefix[word2] == word0 && codeCharacter[word2] == word1) {
                    return word2;
                }
                word2 -= word3;
            } while (word2 >= 0);
            word2 += 5003;
        } while (true);
    }

    boolean lzwOutput(short word0) {
        try {
            lzwData |= word0 << lzwBits;
            for (lzwBits += sActCodeSize; lzwBits >= 8;) {
                Block[BlockPos++] = (byte) (lzwData & 0xff);
                Block[0]++;
                lzwData >>= 8;
                lzwBits -= 8;
                if (BlockPos == 256) {
                    for (int i = 0; i < 256; i++) {
                        dataOut.writeByte(Block[i]);
                    }
                    Block[0] = 0;
                    BlockPos = 1;
                }
            }

            return true;
        } catch (Exception _ex) {
            return false;
        }
    }

    void CalcSpecialCodes() {
        clearCode = (short) (1 << sOriginalCodeSize);
        for (short word0 = 0; word0 < clearCode; word0++) {
            prefix[word0] = word0;
            codeCharacter[word0] = (byte) word0;
        }

        endCode = (short) (clearCode + 1);
        freeSlot = (short) (clearCode + 2);
        sActCodeSize = (short) (sOriginalCodeSize + 1);
    }

    boolean writeHeader() {
        try {
            dataOut.writeBytes("GIF87a");
        } catch (Exception _ex) {
            return false;
        }
        return true;
    }

    boolean writeGlobalPalette() {
        int j = 1;
        for (int i = palSize - 1; (i >>= 1) > 0;) {
            j++;
        }
        try {
            for (int k = 0; k < palSize; k++) {
                Color color = new Color(palArr[k]);
                dataOut.writeByte(color.getRed());
                dataOut.writeByte(color.getGreen());
                dataOut.writeByte(color.getBlue());
            }

            for (int l = palSize; l < 1 << j; l++) {
                Color color1 = new Color(palArr[0]);
                dataOut.writeByte(color1.getRed());
                dataOut.writeByte(color1.getGreen());
                dataOut.writeByte(color1.getBlue());
            }

        } catch (Exception _ex) {
            return false;
        }
        return true;
    }

    boolean writeImageDescriptor() {
        try {
            byte byte0 = (byte) (flagLSD & 7);
            dataOut.writeByte(44);
            dataOut.writeChar(0);
            dataOut.writeChar(0);
            dataOut.writeChar(MSBtoLSB(imgWidth));
            dataOut.writeChar(MSBtoLSB(imgHeight));
            dataOut.writeByte(byte0);
        } catch (Exception _ex) {
            return false;
        }
        return true;
    }

    boolean writeLSD() {
        byte byte0 = 0;
        int i = palSize - 1;
        try {
            while ((i >>= 1) > 0) {
                byte0++;
            }
            flagLSD = (byte) ((byte0 + 8 << 4) + byte0);
            dataOut.writeChar(MSBtoLSB(imgWidth));
            dataOut.writeChar(MSBtoLSB(imgHeight));
            dataOut.writeByte(flagLSD);
            dataOut.writeByte(0);
            dataOut.writeByte(0);
        } catch (Exception _ex) {
            return false;
        }
        return true;
    }

    void closeGif() throws IOException {
        dataOut.writeByte(59);
        output.close();
    }
    private static final int TABLESIZE = 5003;
    private static final int COLOR_MAX = 256;
    private Image image;
    private OutputStream output;
    private DataOutputStream dataOut;
    private int palSize;
    private int lzwData;
    private int palArr[];
    private byte flagLSD;
    private short sOriginalCodeSize;
    private short sActCodeSize;
    private short clearCode;
    private short endCode;
    private short freeSlot;
    private short lzwBits;
    private short BlockPos;
    private short imgWidth;
    private short imgHeight;
    private short code[];
    private short prefix[];
    private short Block[];
    private short pixCol[];
    private short codeCharacter[];
    private boolean message;
}

