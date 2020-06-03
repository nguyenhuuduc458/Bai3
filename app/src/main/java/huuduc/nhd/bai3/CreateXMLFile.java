package huuduc.nhd.bai3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CreateXMLFile {

    public CreateXMLFile(){ }

    public void  saveXMLFile(List<Note> items){
        try{
            DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document               = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("notes");
            document.appendChild(root);

            for(int i = 0 ; i < items.size(); i++){

                Element note = document.createElement("note");

                note.setAttribute("id",String.valueOf(items.get(i).getId()));

                Element title = document.createElement("title");
                title.appendChild(document.createTextNode(items.get(i).getTitle()));
                note.appendChild(title);

                Element content = document.createElement("content");
                content.appendChild(document.createTextNode(items.get(i).getContent()));
                note.appendChild(content);

                Element date = document.createElement("date");
                date.appendChild(document.createTextNode(Note.FORMAT.format(items.get(i).getDate())));
                note.appendChild(date);

                root.appendChild(note);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer               = transformerFactory.newTransformer();
            DOMSource source                      = new DOMSource(document);
            StreamResult result                   = new StreamResult(new File("/data/user/0/huuduc.nhd.bai3/files/note.xml"));
//          StreamResult result                   = new StreamResult(new File("/sdcard/Android/data/huuduc.nhd.bai3/files/note.xml"));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source,result);


        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public List<Note> loadXMLFile(){
        List<Note> items = new ArrayList<>();
        Note item      = null;
        String title   = null;
        String content = null;
        String date    = null;
        int id;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder               = documentBuilderFactory.newDocumentBuilder();

            Document document   = documentBuilder.parse(new File("/data/user/0/huuduc.nhd.bai3/files/note.xml"));
//          Document document   = documentBuilder.parse(new File("/sdcard/Android/data/huuduc.nhd.bai3/files/note.xml"));

            Element root  = document.getDocumentElement();
            NodeList list = root.getChildNodes();

            for(int i = 0 ; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node instanceof Element){
                    Element element = (Element) node;

                    id = Integer.parseInt(element.getAttribute("id"));
                    title   = element.getElementsByTagName("title").item(0).getTextContent();
                    content =  element.getElementsByTagName("content").item(0).getTextContent();
                    date    =  element.getElementsByTagName("date").item(0).getTextContent();

                    item    = new Note(id,title,content,Note.FORMAT.parse(date));

                    items.add(item);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return items;
        }
    }

}
