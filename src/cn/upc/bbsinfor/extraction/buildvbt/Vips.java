/*
 * Tomas Popela, 2012
 * VIPS - Visual Internet Page Segmentation
 * Module - Vips.java
 */

package cn.upc.bbsinfor.extraction.buildvbt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.imageio.ImageIO;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.demo.DOMSource;
import org.fit.cssbox.layout.BrowserCanvas;
import org.fit.cssbox.layout.Viewport;
import org.w3c.dom.Document;

/**
 * 基于视觉的页面分块算法 Vision-based Page Segmentation algorithm
 * 
 * @author Tomas Popela
 */
public class Vips {
    private URL _url = null;
    
    private DOMAnalyzer _domAnalyzer = null;

    private BrowserCanvas _browserCanvas = null;

    private Viewport _viewport = null;

    private boolean _graphicsOutput = false;

    private boolean _outputToFolder = false;

    private boolean _outputEscaping = true;

    private int _pDoC = 11;

    private String _filename = "";

    private int sizeTresholdWidth = 350;

    private int sizeTresholdHeight = 400;

    private PrintStream originalOut = null;

    long startTime = 0;

    long endTime = 0;
    
    public String outputFolder = "";
    /**
     * Default constructor
     */
    public Vips() {
    }

    /**
     * 是否获得VIPS算法过程的图像 Enables or disables graphics output of VIPS algorithm.
     * 
     * @param enable True for enable, otherwise false.
     */
    public void enableGraphicsOutput(boolean enable) {
        _graphicsOutput = enable;
    }

    /**
     * 是否为VIPS算法的运行过程创建新的目录 Enables or disables creation of new directory for
     * every algorithm run.
     * 
     * @param enable True for enable, otherwise false.
     */
    public void enableOutputToFolder(boolean enable) {
        _outputToFolder = enable;
    }

    /**
     * Enables or disables output XML character escaping.
     * 
     * @param enable True for enable, otherwise false.
     */
    public void enableOutputEscaping(boolean enable) {
        _outputEscaping = enable;
    }

    /**
     * 设置预定义的相关性值 Sets permitted degree of coherence (pDoC) value.
     * 
     * @param value pDoC value.
     */
    public void setPredefinedDoC(int value) {
        if (value <= 0 || value > 11) {
            System.err.println("pDoC value must be between 1 and 11! Not " + value + "!");
            return;
        } else {
            _pDoC = value;
        }
    }

    /**
     * 设置web页面的URL
     * 
     * @param url Url
     * @throws MalformedURLException
     */
    public void setUrl(String url) {
        try {
            if (url.startsWith("http://") || url.startsWith("https://"))
                _url = new URL(url);
            else
                _url = new URL("http://" + url);
        } catch (Exception e) {
            System.err.println("无效的URL地址: " + url);
        }
    }

    /**
     * 从页面源代码构建DOM树
     * 
     * @param urlStream Input stream with page source.
     */
    private void getDomTree(InputStream urlStream) {
        DOMSource parser = new DOMSource(urlStream);
        // 利用反射修改编码
        Class<?> parserClass = parser.getClass();
        try {
            Field charset = parserClass.getDeclaredField("charset");
            charset.setAccessible(true);
            try {
                charset.set(parser, "gbk");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        try {
            Document domTree = parser.parse();
            _domAnalyzer = new DOMAnalyzer(domTree, _url);
            _domAnalyzer.attributesToStyles();
            _domAnalyzer.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT);
            _domAnalyzer.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT);
            _domAnalyzer.getStyleSheets();
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * 获得页面的视口
     */
    private void getViewport() {
        _browserCanvas = new BrowserCanvas(_domAnalyzer.getRoot(), _domAnalyzer,
                new java.awt.Dimension(1000, 600), _url); // 设置视口的尺寸
        _viewport = _browserCanvas.getViewport();
    }

    /**
     * 将主题页面转换成图片
     */
    private void exportPageToImage() {
        try {
            BufferedImage page = _browserCanvas.getImage();
            String filename = System.getProperty("user.dir") + "/page.png";
            ImageIO.write(page, "png", new File(filename));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成输出结果文件夹名 Generates folder filename
     * 
     * @return Folder filename
     */
    public String generateFolderName() {
        String outputFolder = "";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        outputFolder += sdf.format(cal.getTime());
        outputFolder += "_";
        outputFolder = _url.getHost().replaceAll("\\.", "_").replaceAll("/", "_");

        return outputFolder;
    }

    /**
     * 执行页面分块 Performs page segmentation.
     */
    private void performSegmentation() {

        startTime = System.nanoTime();
        int numberOfIterations = 10; // 迭代的次数
        int pageWidth = _viewport.getWidth();
        int pageHeight = _viewport.getHeight();

        if (_graphicsOutput)
            exportPageToImage();

        VipsSeparatorGraphicsDetector detector;
        VipsParser vipsParser = new VipsParser(_viewport);
        VisualStructureConstructor constructor = new VisualStructureConstructor(_pDoC);
        constructor.setGraphicsOutput(_graphicsOutput);

        for (int iterationNumber = 1; iterationNumber < numberOfIterations + 1; iterationNumber++) {
            detector = new VipsSeparatorGraphicsDetector(pageWidth, pageHeight);

            // 视觉块检测visual blocks detection
            vipsParser.setSizeTresholdHeight(sizeTresholdHeight);
            vipsParser.setSizeTresholdWidth(sizeTresholdWidth);

            vipsParser.parse();

            VipsBlock vipsBlocks = vipsParser.getVipsBlocks();

            if (iterationNumber == 1) {
                if (_graphicsOutput) {
                    // in first round we'll export global separators
                    detector.setVipsBlock(vipsBlocks);
                    detector.fillPool();
                    detector.saveToImage("blocks" + iterationNumber);
                    detector.setCleanUpSeparators(0);
                    detector.detectHorizontalSeparators();
                    detector.detectVerticalSeparators();
                    detector.exportHorizontalSeparatorsToImage();
                    detector.exportVerticalSeparatorsToImage();
                    detector.exportAllToImage();
                }

                // visual structure construction
                constructor.setVipsBlocks(vipsBlocks);
                constructor.setPageSize(pageWidth, pageHeight);
            } else {
                vipsBlocks = vipsParser.getVipsBlocks();
                constructor.updateVipsBlocks(vipsBlocks);

                if (_graphicsOutput) {
                    detector.setVisualBlocks(constructor.getVisualBlocks());
                    detector.fillPool();
                    detector.saveToImage("blocks" + iterationNumber);
                }
            }

            // visual structure construction
            constructor.constructVisualStructure();

            // prepare tresholds for next iteration
            if (iterationNumber <= 5) {
                sizeTresholdHeight -= 50;
                sizeTresholdWidth -= 50;

            }
            if (iterationNumber == 6) {
                sizeTresholdHeight = 100;
                sizeTresholdWidth = 100;
            }
            if (iterationNumber == 7) {
                sizeTresholdHeight = 80;
                sizeTresholdWidth = 80;
            }
            if (iterationNumber == 8) {
                sizeTresholdHeight = 40;
                sizeTresholdWidth = 10;
            }
            if (iterationNumber == 9) {
                sizeTresholdHeight = 1;
                sizeTresholdWidth = 1;
            }

        }

        // constructor.normalizeSeparatorsSoftMax();
        constructor.normalizeSeparatorsMinMax();

        VipsOutput vipsOutput = new VipsOutput(_pDoC);
        vipsOutput.setEscapeOutput(_outputEscaping);
        vipsOutput.setOutputFileName(_filename);
        vipsOutput.writeXML(constructor.getVisualStructure(), _viewport);

        endTime = System.nanoTime();

        long diff = endTime - startTime;

        System.out.println("Execution time of VIPS: " + diff + " ns; " + (diff / 1000000.0)
                + " ms; " + (diff / 1000000000.0) + " sec");
    }

    /**
     * 开始对给定的URL进行页面分块 Starts segmentation on given address
     * 
     * @param url
     */
    public void startSegmentation(String url) {
        setUrl(url);

        startSegmentation();
    }

    /**
     * Restores stdout
     */
    private void restoreOut() {
        if (originalOut != null) {
            System.setOut(originalOut);
        }
    }

    /**
     * Redirects stdout to nowhere
     */
    private void redirectOut() {
        originalOut = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        }));
    }

    /**
     * 开始网页的视觉分块 Starts visual segmentation of page
     * 
     * @throws Exception
     */
    public void startSegmentation() {
        try {
            URLConnection urlConnection = _url.openConnection();
            InputStream urlStream = urlConnection.getInputStream();

            redirectOut();

            getDomTree(urlStream);
            startTime = System.nanoTime();
            getViewport();
            restoreOut();
            
            String oldWorkingDirectory = "";
            String newWorkingDirectory = "";

            if (_outputToFolder) {
                outputFolder = generateFolderName();

                if (!new File(outputFolder).mkdir()) {
                    System.err.println("创建目录错误（Something goes wrong during directory creation!）");
                } else {
                    oldWorkingDirectory = System.getProperty("user.dir");
                    newWorkingDirectory += oldWorkingDirectory + "/" + outputFolder + "/";
                    System.setProperty("user.dir", newWorkingDirectory);
                }
            }

            performSegmentation();

            if (_outputToFolder)
                System.setProperty("user.dir", oldWorkingDirectory);

            urlStream.close();
        } catch (Exception e) {
            System.err.println("Something's wrong!");
            e.printStackTrace();
        }
    }

    public void setOutputFileName(String filename) {
        if (!filename.equals("")) {
            _filename = filename;
        } else {
            System.out.println("Invalid filename!");
        }
    }
}
