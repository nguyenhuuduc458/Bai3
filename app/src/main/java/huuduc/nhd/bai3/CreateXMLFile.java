package huuduc.nhd.bai3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

            Element root = document.createElement("notes");
            document.appendChild(root);

            for(int i = 0 ; i < items.size(); i++){

                Element note = document.createElement("note");
                note.setAttribute("id",String.valueOf(items.get(i).getId()));

                createElement(document,items,"title",note,i);
                createElement(document,items,"content",note, i);
                createElement(document,items,"date",note, i);

                root.appendChild(note);
            }

            tranformDOMToXML(document);

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

    public void createElement(Document document,List<Note> items, String nameOfElement, Element parent, int position)
    {
        Element name = document.createElement(nameOfElement);
        switch (nameOfElement){
            case "title" : {
                name.appendChild(document.createTextNode(items.get(position).getTitle()));
                break;
            }
            case "content":{
                name.appendChild(document.createTextNode(items.get(position).getContent()));
                break;
            }
            case "date":{
                name.appendChild(document.createTextNode(Note.FORMAT.format(items.get(position).getDate())));
                break;
            }
        }
        parent.appendChild(name);

    }

    public void tranformDOMToXML(Document document) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer               = transformerFactory.newTransformer();
        DOMSource source                      = new DOMSource(document);
        StreamResult result                   = new StreamResult(new File("/data/user/0/huuduc.nhd.bai3/files/note.xml"));

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(source,result);

    }

}
