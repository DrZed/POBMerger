package com.drzed;

import org.jpaste.exceptions.PasteException;
import org.jpaste.pastebin.Pastebin;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import static com.drzed.XMLHandler.parsePastebins;

@SuppressWarnings("all")
public class Main {

    public static void main(String[] args) throws Exception {
        if (!new File("./vault/").exists()) new File("./vault/").mkdirs();
        if (!new File("./imports/").exists()) new File("./imports/").mkdirs();
        String pastes = "";
        for (String arg : args) {
            if (arg.startsWith("http") && arg.contains("pastebin")) {
                if (pastes.isEmpty())
                    pastes += arg;
                else
                    pastes += "," + arg;
            }
        }
        if (!pastes.isEmpty()) {
            parsePastebins(pastes);
        } else {
            System.exit(-1);
        }
//        decompressPastebinToXML("cY9pvbMg");
//        producePastebinFromXML();
    }

    public static void producePastebinFromXML(String in) throws Exception {
        byte[] bray2 = compressBytes("./output/" + in + "/" + in + ".xml");
        String outputString = Base64.getUrlEncoder().encodeToString(bray2);
//        String outputString = new String(brayComp);
        String outfil = "./output/" + in + "/output.txt";
        File of = new File("./output/", in);
        of.mkdirs();
        FileWriter fw = new FileWriter(new File(outfil));
        fw.append(outputString);
        fw.close();
        paste(outfil);
    }

    public static void decompressPastebinToXML(String pbkey) throws Exception {
        if ((new File("./vault/" + pbkey + "/" + pbkey + ".xml")).exists()) {
            return;
        }
        File vf = new File("./vault", pbkey);
        vf.mkdirs();
        String b = Pastebin.getContents(pbkey);
        byte[] bz = b.getBytes("UTF-8");
        byte[] bzdc = Base64.getUrlDecoder().decode(bz);
        byte[] nb = decompressBytes(bzdc);
        String outputString = new String(nb,"UTF-8");
        FileWriter fw = new FileWriter(new File(vf,pbkey + ".xml"));
        fw.append(outputString);
        fw.close();
    }

    private static final String developerKey = "da5d91148c5892aaf646ba55ef9cfe2a";
    private static final String title = "";
    private static void paste(String ou) {
        try {
            File f = new File(ou);
            if (f.exists()) {
                BufferedReader fr = new BufferedReader(new FileReader(f));
                String pblink = Pastebin.pastePaste(developerKey, fr.readLine(), title).toString();
                FileWriter fw = new FileWriter(new File(ou.replaceAll("output\\.txt", "paste.txt")));
                fw.append(pblink);
                fw.close();
            } else {
                System.out.println("FUCK");
            }
        } catch (IOException | PasteException e) {
            e.printStackTrace();
        }
    }

    public static byte[] compressBytes(String infil) throws Exception {
        FileInputStream fis = new FileInputStream(new File(infil));
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        DeflaterOutputStream dos = new DeflaterOutputStream(fos, deflater);

        doCopy(fis, dos);
        return fos.toByteArray();
    }

    public static byte[] decompressBytes(byte[] inby) throws Exception {
        ByteArrayInputStream fis2 = new ByteArrayInputStream(inby);
        InflaterInputStream iis = new InflaterInputStream(fis2);
        ByteArrayOutputStream fos2 = new ByteArrayOutputStream();
        doCopy(iis, fos2);
        return fos2.toByteArray();
    }

    public static void doCopy(InputStream is, OutputStream os) throws Exception {
        int oneByte;
        while ((oneByte = is.read()) != -1) {
            os.write(oneByte);
        }
        is.close();
        os.close();
    }
}
