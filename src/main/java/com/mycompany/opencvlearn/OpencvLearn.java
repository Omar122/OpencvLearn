package com.mycompany.opencvlearn;

import java.io.IOException;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.opencv.global.opencv_core;
import static org.bytedeco.opencv.global.opencv_core.BORDER_CONSTANT;
import static org.bytedeco.opencv.global.opencv_core.CV_32S;
import static org.bytedeco.opencv.global.opencv_core.CV_64FC4;
import static org.bytedeco.opencv.global.opencv_core.CV_8U;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import org.bytedeco.opencv.global.opencv_imgproc;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import org.bytedeco.opencv.opencv_core.*;
import java.util.Arrays;
import static org.bytedeco.opencv.global.opencv_core.CV_8UC3;

/**
 *
 * @author carbo
 */
public class OpencvLearn {

    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("org.bytedeco.openblas.load", "mkl");
        System.setProperty("org.bytedeco.javacpp.logger.debug", "false");

        Mat img = imread("C:\\Users\\oalfuraydi.MURL097631B55LG\\Documents\\NetBeansProjects\\OpencvLearn\\src\\main\\java\\com\\mycompany\\opencvlearn\\recipts2.jpg");

        Mat contrast = Mat.zeros(img.size(), CV_64FC4).asMat();

        opencv_core.convertScaleAbs(img, contrast, 0.5, 0.0);
        imwrite("contrast.jpg", contrast);
       
        Mat gray = new Mat();
        Mat blured = new Mat();
        blur(contrast, blured, new Size(5, 5));
        cvtColor(blured, gray, opencv_imgproc.CV_BGR2GRAY);
      
     
        Mat threash = new Mat();
        //double threashvfalues=threshold(gray, threash, 50, 255, THRESH_TOZERO);
        adaptiveThreshold(gray, threash, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 3, 1);
        Mat label = new Mat(threash.size(), CV_32S);
        Mat dilate = new Mat(threash.size(), CV_32S);
        Mat kernal = getStructuringElement(CV_8U, new Size(3, 3));
        morphologyEx(threash, label, MORPH_OPEN, kernal);
        dilate(label, dilate, kernal, null, 1, BORDER_CONSTANT, AbstractScalar.BLACK);
        Mat distance = new Mat(threash.size(), CV_32S);
        distanceTransform(threash, distance, DIST_L2, 3);
        double[] minval = {0};
        double[] maxval = {0};
        opencv_core.minMaxLoc(distance, minval, maxval, new Point(), new Point(), dilate);
        System.out.println("::minval maxval: " + Arrays.toString(maxval));
        Mat distace_thresh = new Mat(threash.size(), CV_32S);
        threshold(distance, distace_thresh, maxval[0] * 0.3, 255.0, 0);

        
        imwrite("label.jpg", label);
        imwrite("dilate.jpg", dilate);
        imwrite("gray.jpg", gray);
        imwrite("thresh.jpg", threash);
        
        Mat distace_threshint = new Mat();
        distace_thresh.convertTo(distace_threshint, CV_8U);

        imwrite("distance.jpg", distance);
        imwrite("distace_threshint.jpg", distace_threshint);
        System.out.println("distace_thresh Type" + distace_thresh.type());

        Mat centroids = new Mat();
        Mat labels = new Mat();
        Mat stats = new Mat();

        System.out.println("label Type" + labels.type());
        System.out.println("distace_threshint Type" + distace_threshint.type());

        MatExpr unknowen = opencv_core.subtract(dilate, distace_threshint);

        System.out.println("unknowen Type" + unknowen.type());

        imwrite("unknowen.jpg", unknowen.asMat());

        int connectedComponents = opencv_imgproc.connectedComponentsWithStats(distace_threshint, labels, stats, centroids);
        System.out.println("::Connected result: " + connectedComponents);
        MatVector mv = new MatVector();
        findContours(dilate, mv, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

        Mat markers = new Mat(dilate.size(), CV_8U);

        for (int i = 0; i < mv.size(); i++) {
            drawContours(markers, mv, i, AbstractScalar.BLUE);

        }

       

        //Mat areas = stats.col(opencv_imgproc.CC_STAT_AREA).rowRange(1, connectedComponents);
        if (connectedComponents < 2) {
        } else {
            Mat output = Mat.zeros(gray.size(), CV_64FC4).asMat();
            RNG rng = new RNG(0xFFFFFFFF);
            for (int i = 1; i <= connectedComponents; i++) {
                Mat mask = opencv_core.equals(labels, i).asMat();
                output = output.setTo(new Mat(rng.uniform(0, 255), rng.uniform(0, 255), rng.uniform(0, 255), 255), mask);

            }
            imwrite("output.jpg", output);
        }
        
        System.out.println("img Type" + img.type());
        System.out.println("markers Type" + markers.type());
        Mat ImgResul = new Mat(markers.size(),markers.type());
        ImgResul.convertTo(img, markers.type());
        System.out.println("ImgResul Type" + ImgResul.type());
        watershed(ImgResul, markers);
        
         imwrite("markers.jpg", markers);
        int maxLabel = 0;
        int maxSize = 0;
        for (int i = 1; i < connectedComponents; i++) {
            int size = stats.ptr(i, opencv_imgproc.CC_STAT_AREA).getInt();
            System.err.println("Row : " + i + " Size: " + size);
            if (size > maxSize) {
                maxSize = size;
                maxLabel = i;
            }
        }

        System.out.println("maxLabel : " + maxLabel);
        System.out.println("maxSize: " + maxSize);

        Rect boundingBox = new Rect(
                stats.getIntBuffer().get(maxLabel * stats.cols() + opencv_imgproc.CC_STAT_LEFT),
                stats.getIntBuffer().get(maxLabel * stats.cols() + opencv_imgproc.CC_STAT_TOP),
                stats.getIntBuffer().get(maxLabel * stats.cols() + opencv_imgproc.CC_STAT_WIDTH),
                stats.getIntBuffer().get(maxLabel * stats.cols() + opencv_imgproc.CC_STAT_HEIGHT)
        );

        // Extract the ROI of the largest label
        Mat roi = new Mat(img, boundingBox);
        imwrite("roi.jpg", roi);

    }

}
