
package cn.upc.bbsinfor.extraction.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * 文件读写工具类
 * 
 * @author Belief
 */
public class FileIO {
    public static void writeXML(Document document, String filename) {
        Writer fileWriter;
        try {
            fileWriter = new FileWriter(filename);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setIndent(true); // 设置是否缩进
            format.setIndent("   "); // 以空格方式实现缩进
            format.setNewlines(true); // 设置是否换行
            XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Document readXML(String filename) throws DocumentException {
        Document document = null;
        SAXReader saxReader = new SAXReader();
        document = saxReader.read(filename);
        return document;
    }
}
