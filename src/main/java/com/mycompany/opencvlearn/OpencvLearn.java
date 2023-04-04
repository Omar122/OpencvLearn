package com.mycompany.opencvlearn;

import java.io.IOException;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.opencv.global.opencv_core;
import static org.bytedeco.opencv.global.opencv_core.BORDER_ISOLATED;
import static org.bytedeco.opencv.global.opencv_core.CV_32S;
import static org.bytedeco.opencv.global.opencv_core.CV_64FC4;
import static org.bytedeco.opencv.global.opencv_core.CV_8UC3;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import org.bytedeco.opencv.global.opencv_imgproc;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import org.bytedeco.opencv.opencv_core.*;

/**
 *
 * @author carbo
 */
public class OpencvLearn {

    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("org.bytedeco.openblas.load", "mkl");
        System.setProperty("org.bytedeco.javacpp.logger.debug", "false");

        Pointer pointer = new Pointer();
        BytePointer name = new BytePointer("window1");

        Mat img = imread("C:\\Users\\carbo\\Documents\\NetBeansProjects\\OpencvLearn\\src\\main\\java\\com\\mycompany\\opencvlearn\\2022-12-07 (5).png");
        Mat gray = new Mat();
        Mat blured = new Mat();
        medianBlur(img, blured, 3);
        cvtColor(blured, gray, opencv_imgproc.CV_BGR2GRAY);
        Mat threash = new Mat();
        //double threashvfalues=threshold(gray, threash, 50, 255, THRESH_TOZERO);
        adaptiveThreshold(gray, threash, 50, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 5, 3.3);
        Mat label = new Mat(threash.size(), CV_32S);

        imwrite("gray.jpg", gray);
        imwrite("thresh.jpg", threash);

        //int connectedComponents = opencv_imgproc.connectedComponents(gray, label);
        int connectedComponents = opencv_imgproc.connectedComponents(threash, label, 8, CV_32S);
        System.out.println("::Connected result: " + connectedComponents);

        if (connectedComponents < 2) {
        } else {
            Mat output = Mat.zeros(gray.size(), CV_64FC4).asMat();
            RNG rng = new RNG(0xFFFFFFFF);
            for (int i = 1; i <= connectedComponents; i++) {
                Scalar color = new Scalar(rng.uniform(0, 255), rng.uniform(0, 255), rng.uniform(0, 255), 255);
                System.out.println("Component Color :" + i + color.toString());
                Mat mask = opencv_core.equals(label, i).asMat();
                output = output.setTo(new Mat(color), mask);

            }
            Mat finaloutput = new Mat();
           cvtColor(output, finaloutput, opencv_imgproc.COLOR_GRAY2RGBA);
            imwrite("output.jpg", output);
            imwrite("finaloutput.jpg", finaloutput);
          
        }

    }

}
