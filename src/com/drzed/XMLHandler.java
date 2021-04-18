package com.drzed;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.drzed.Main.producePastebinFromXML;

public class XMLHandler {
    public XMLHandler() {

    }
//<Spec title=
    //useSecondWeaponSet="nil" -> useSecondWeaponSet="false"
    // id="#"> # = last available linear
    // title=Give Name Prompt? , from null
    public static String getXMLName(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("./imports/" + xml));
        doc.getDocumentElement().normalize();

        String outName = "";

        Element root = doc.getDocumentElement();
//        System.out.println(root.getNodeName());

        NodeList nList = root.getChildNodes();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeName().equalsIgnoreCase("#text")) continue;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equalsIgnoreCase("Build")) {
                    Element eElement = (Element) node;
                    outName = "lvl_" + eElement.getAttribute("level") + "_B_" +
                            eElement.getAttribute("bandit") + "_" + eElement.getAttribute("className") + "_"+
                            eElement.getAttribute("ascendClassName") + ".xml";
                    return outName;
//                    break;
                }
            }
        }
        return outName;
    }

    public static List<NodeList> parseXML(String input) throws ParserConfigurationException, IOException, SAXException {
        NodeList skillGems = null, itemList = null, tree = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(input));
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        NodeList nList = root.getChildNodes();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeName().equalsIgnoreCase("#text")) continue;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equalsIgnoreCase("Build")) {
                }
                if (node.getNodeName().equalsIgnoreCase("Skills")) {
                    skillGems = node.getChildNodes();
                }
                if (node.getNodeName().equalsIgnoreCase("Tree")) {
                    tree = node.getChildNodes();
                }
                if (node.getNodeName().equalsIgnoreCase("Items")) {
                    itemList = node.getChildNodes();
                }
            }
        }
        return Arrays.asList(skillGems, tree, itemList);
    }

    private static void writeDoc(Document doc, String s) {
        try {
            //for output to file, console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            //write to console or file
            (new File("./output/", s)).mkdirs();
            StreamResult file = new StreamResult(new File("./output/" + s, s + ".xml"));
            //write data
            transformer.transform(source, file);
            producePastebinFromXML(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Document mergeXML(DocumentBuilderFactory factory, DocumentBuilder builder, Document doc, NodeList skls, NodeList tres, NodeList itms) {
        Element root = doc.getDocumentElement();
        NodeList nList = root.getChildNodes();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeName().equalsIgnoreCase("#text")) continue;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equalsIgnoreCase("Skills")) {
                    for (int i = 0; i < skls.getLength(); i++) {
                        Node firstDocImportedNode = doc.importNode(skls.item(i), true);
                        node.appendChild(firstDocImportedNode);
//                            node.appendChild(skls.item(i));
                    }
                }
                if (node.getNodeName().equalsIgnoreCase("Tree")) {
                    for (int i = 0; i < tres.getLength(); i++) {
                        Node firstDocImportedNode = doc.importNode(tres.item(i), true);
                        node.appendChild(firstDocImportedNode);
//                            node.appendChild(tres.item(i));
                    }
                }
                if (node.getNodeName().equalsIgnoreCase("Items")) {
                    for (int i = 0; i < itms.getLength(); i++) {
                        Node firstDocImportedNode = doc.importNode(itms.item(i), true);
                        node.appendChild(firstDocImportedNode);
//                            node.appendChild(itms.item(i));
                    }
                }
            }
        }
        return doc;
    }

    private static NodeList findTree(List<NodeList> nls) {
        for (NodeList nl : nls) {
            String nn = nl.item(1).getNodeName();
            if (nn.equalsIgnoreCase("Spec"))
                return nl;
        }
        return null;
    }

    private static NodeList findItems(List<NodeList> nls) {
        for (NodeList nl : nls) {
            String nn = nl.item(1).getNodeName();
            if (nn.equalsIgnoreCase("Item"))
                return nl;
        }
        return null;
    }

    private static NodeList findSkills(List<NodeList> nls) {
        for (NodeList nl : nls) {
            String nn = nl.item(1).getNodeName();
            if (nn.equalsIgnoreCase("Skill"))
                return nl;
        }
        return null;
    }

    public static void parsePastebins(String in) throws ParserConfigurationException, SAXException, IOException {
        String[] ins = in.split(",");
        List<String> inputs = new ArrayList<>();
        for (String s : ins) {
            if (s.contains("=")) s = s.split("=")[1];
            try {
                inputs.add(handle(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        XMLHandler.mergeXMLS(inputs);
    }

    private static String handle(String url) throws Exception {
        String pb = url.split("/")[url.split("/").length - 1];
        Main.decompressPastebinToXML(pb);
        return pb;
    }

    private static void mergeXMLS(List<String> inputs) throws IOException, SAXException, ParserConfigurationException {
        HashMap<String, List<NodeList>> nlz = new HashMap<>();
        String baseXML = "";
        for (String s : inputs) {
            File f = new File("vault/" + s + "/" + s + ".xml");
            if (baseXML.isEmpty()) {
                baseXML = s + "/" + s + ".xml";
            }
            nlz.put("vault/" + s + "/" + s + ".xml", parseXML("vault/" + s + "/" + s + ".xml"));
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("./vault/", baseXML));
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        for (String s : nlz.keySet()) {
            List<NodeList> nls = nlz.get(s);
            NodeList skls = findSkills(nls);
            NodeList tres = findTree(nls);
            NodeList itms = findItems(nls);
            doc = mergeXML(factory, builder, doc, skls, tres, itms);
        }
        writeDoc(doc, inputs.get(0));
    }
}
