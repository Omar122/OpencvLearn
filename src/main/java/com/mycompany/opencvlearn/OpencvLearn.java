package com.mycompany.opencvlearn;

import java.awt.Color;
import java.io.IOException;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import static org.bytedeco.opencv.global.opencv_highgui.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import org.bytedeco.opencv.global.opencv_imgproc;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
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

        Mat img = imread("C:\\Users\\carbo\\Documents\\NetBeansProjects\\OpencvLearn\\src\\main\\java\\com\\mycompany\\opencvlearn\\20230215_182117.jpg");
        Mat gray = new Mat();
        cvtColor(img, gray, opencv_imgproc.CV_BGR2GRAY);
        Mat label = new Mat();
        int connectedComponents = opencv_imgproc.connectedComponents(gray, label);
        System.out.println("::Connected result: " + connectedComponents);
        if (connectedComponents < 2) {
        } else {
             
            Mat output = Mat.zeros(gray.size(), gray.type()).asMat();
            RNG rng = new RNG(0xFFFFFFFF);
            for (int i = 0; i < connectedComponents; i++) {
                UMat mask = new UMat(label.ptr(i));
                output.setTo(mask);      
                System.out.println("::Component i: " + i);
                imwrite("connted" + i + ".jpg", mask);
            }
            //imshow(name, output);
        }

    }

}
