package com.perf2k2.rsaxml2pem;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class Main {
    private static final String[] PRIVATE_KEY_XML_NODES = new String[]{"Modulus", "Exponent", "P", "Q", "DP", "DQ", "InverseQ", "D"};
    private static final String[] PUBLIC_KEY_XML_NODES = new String[]{"Modulus", "Exponent"};

    public static void main(String[] var0) {
        if (var0.length != 1) {
            System.out.println("Usage:\n  java XMLSec2PEM <XMLSecurityRSAKeyValueFile.xml>");
            System.exit(0);
        }

        try {
            Document var1 = parseXMLFile(var0[0]);
            System.out.print("Determining the key type: ");
            int var2 = getKeyType(var1);
            if (var2 != 1 && var2 != 2) {
                System.exit(1);
            } else {
                System.out.println("seems to be a " + (var2 == 1 ? "private" : "public") + " XML Security key");
            }

            System.out.print("Checking the XML file structure: ");
            if (checkXMLRSAKey(var2, var1)) {
                System.out.println("OK");
            } else {
                System.exit(1);
            }

            String var3 = "";
            System.out.println("Outputting the resulting key:\n");
            if (var2 == 1) {
                var3 = convertXMLRSAPrivateKeyToPEM(var1);
                System.out.println("-----BEGIN PRIVATE KEY-----");
                System.out.println(var3);
                System.out.println("-----END PRIVATE KEY-----");
            } else {
                var3 = convertXMLRSAPublicKeyToPEM(var1);
                System.out.println("-----BEGIN PUBLIC KEY-----");
                System.out.println(var3);
                System.out.println("-----END PUBLIC KEY-----");
            }
        } catch (Exception var4) {
            System.err.println(var4);
        }

    }

    private static int getKeyType(Document var0) {
        Node var1 = var0.getFirstChild();
        if (!var1.getNodeName().equals("RSAKeyValue")) {
            System.out.println("Expecting <RSAKeyValue> node, encountered <" + var1.getNodeName() + ">");
            return 0;
        } else {
            NodeList var2 = var1.getChildNodes();
            return var2.getLength() == PUBLIC_KEY_XML_NODES.length ? 2 : 1;
        }
    }

    private static boolean checkXMLRSAKey(int var0, Document var1) {
        Node var2 = var1.getFirstChild();
        NodeList var3 = var2.getChildNodes();
        String[] var4 = new String[0];
        if (var0 == 1) {
            var4 = PRIVATE_KEY_XML_NODES;
        } else {
            var4 = PUBLIC_KEY_XML_NODES;
        }

        for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5];
            boolean var7 = false;

            for(int var8 = 0; var8 < var3.getLength(); ++var8) {
                if (var3.item(var8).getNodeName().equals(var6)) {
                    var7 = true;
                    break;
                }
            }

            if (!var7) {
                System.out.println("Cannot find node <" + var6 + ">");
                return false;
            }
        }

        return true;
    }

    private static String convertXMLRSAPrivateKeyToPEM(Document var0) {
        Node var1 = var0.getFirstChild();
        NodeList var2 = var1.getChildNodes();
        BigInteger var3 = null;
        BigInteger var4 = null;
        BigInteger var5 = null;
        BigInteger var6 = null;
        BigInteger var7 = null;
        BigInteger var8 = null;
        BigInteger var9 = null;
        BigInteger var10 = null;

        for(int var11 = 0; var11 < var2.getLength(); ++var11) {
            Node var12 = var2.item(var11);
            String var13 = var12.getTextContent();
            if (var12.getNodeName().equals("Modulus")) {
                var3 = new BigInteger(b64decode(var13));
            } else if (var12.getNodeName().equals("Exponent")) {
                var4 = new BigInteger(b64decode(var13));
            } else if (var12.getNodeName().equals("P")) {
                var5 = new BigInteger(b64decode(var13));
            } else if (var12.getNodeName().equals("Q")) {
                var6 = new BigInteger(b64decode(var13));
            } else if (var12.getNodeName().equals("DP")) {
                var7 = new BigInteger(b64decode(var13));
            } else if (var12.getNodeName().equals("DQ")) {
                var8 = new BigInteger(b64decode(var13));
            } else if (var12.getNodeName().equals("InverseQ")) {
                var9 = new BigInteger(b64decode(var13));
            } else if (var12.getNodeName().equals("D")) {
                var10 = new BigInteger(b64decode(var13));
            }
        }

        try {
            RSAPrivateCrtKeySpec var15 = new RSAPrivateCrtKeySpec(var3, var4, var10, var5, var6, var7, var8, var9);
            KeyFactory var16 = KeyFactory.getInstance("RSA");
            PrivateKey var17 = var16.generatePrivate(var15);
            return b64encode(var17.getEncoded());
        } catch (Exception var14) {
            System.out.println(var14);
            return null;
        }
    }

    private static String convertXMLRSAPublicKeyToPEM(Document var0) {
        Node var1 = var0.getFirstChild();
        NodeList var2 = var1.getChildNodes();
        BigInteger var3 = null;
        BigInteger var4 = null;

        for(int var5 = 0; var5 < var2.getLength(); ++var5) {
            Node var6 = var2.item(var5);
            String var7 = var6.getTextContent();
            if (var6.getNodeName().equals("Modulus")) {
                var3 = new BigInteger(b64decode(var7));
            } else if (var6.getNodeName().equals("Exponent")) {
                var4 = new BigInteger(b64decode(var7));
            }
        }

        try {
            RSAPublicKeySpec var9 = new RSAPublicKeySpec(var3, var4);
            KeyFactory var10 = KeyFactory.getInstance("RSA");
            PublicKey var11 = var10.generatePublic(var9);
            return b64encode(var11.getEncoded());
        } catch (Exception var8) {
            System.out.println(var8);
            return null;
        }
    }

    private static Document parseXMLFile(String var0) {
        try {
            DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder var2 = var1.newDocumentBuilder();
            Document var3 = var2.parse(new File(var0));
            return var3;
        } catch (Exception var4) {
            System.err.println(var4);
            return null;
        }
    }

    private static final String b64encode(byte[] var0) {
        BASE64Encoder var1 = new BASE64Encoder();
        String var2 = var1.encodeBuffer(var0).trim();
        return var2;
    }

    private static final byte[] b64decode(String var0) {
        try {
            BASE64Decoder var1 = new BASE64Decoder();
            byte[] var2 = var1.decodeBuffer(var0.trim());
            return var2;
        } catch (IOException var3) {
            System.out.println("Exception caught when base64 decoding!" + var3.toString());
            return null;
        }
    }
}
