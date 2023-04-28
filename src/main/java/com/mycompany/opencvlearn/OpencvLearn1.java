package com.mycompany.opencvlearn;

import java.io.IOException;
import org.bytedeco.opencv.global.opencv_core;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import org.bytedeco.opencv.global.opencv_imgproc;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 *
 * @author carbo
 */
public class OpencvLearn1 {

    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("org.bytedeco.openblas.load", "mkl");
        System.setProperty("org.bytedeco.javacpp.logger.debug", "false");
        int MAX_VALUE = 255;
        int MAX_VALUE_H = 360 / 2;
        Mat img = imread("C:\\Users\\oalfuraydi.MURL097631B55LG\\Documents\\NetBeansProjects\\OpencvLearn\\src\\main\\java\\com\\mycompany\\opencvlearn\\recipts2.jpg");

        Mat gray = new Mat();
        Mat blured = new Mat();
        Mat testhsv = new Mat();
        System.out.println("img type" + img.type());

        //blur(img, blured, new Size(3, 3));
        //imwrite("blured.jpg", blured);
        cvtColor(img, gray, opencv_imgproc.CV_BGR2BGRA, 0);
        imwrite("gray.jpg", gray);
        opencv_core.inRange(gray, new Mat(100.0, 100.0, 100.0), new Mat(255.0, 255.0, 255.0), testhsv);
        
        Mat result = new Mat();

        opencv_core.bitwise_and(img, img, result, testhsv);
        imwrite("testhsv.jpg", testhsv);
        imwrite("result.jpg", result);
    }
}
