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

        Mat contrast = Mat.zeros(img.size(), CV_64FC4).asMat();

        opencv_core.convertScaleAbs(img, contrast, 1.6, 1.0);
        //imwrite("contrast.jpg", contrast);
        //System.exit(0);
        Mat gray = new Mat();
        Mat blured = new Mat();
        blur(img, blured, new Size(3, 3));
        cvtColor(blured, gray, opencv_imgproc.CV_BGR2GRAY);
        Mat DoubleBlured = new Mat();
        blur(gray, DoubleBlured, new Size(3, 3));
        Mat threash = new Mat();
        //double threashvfalues=threshold(gray, threash, 50, 255, THRESH_TOZERO);
        adaptiveThreshold(DoubleBlured, threash, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 7, 3);
        Mat label = new Mat(threash.size(), CV_32S);
        Mat dilate = new Mat(threash.size(), CV_32S);
        Mat kernal = getStructuringElement(CV_8U, new Size(3, 3));
        morphologyEx(threash, label, MORPH_OPEN, kernal);
        dilate(label, dilate, kernal, null, 0, BORDER_CONSTANT, AbstractScalar.BLACK);
        Mat distance = new Mat(threash.size(), CV_32S);
        distanceTransform(threash, distance, DIST_L2, 5);
        double[] minval={0};
        double[] maxval={0};
        opencv_core.minMaxLoc(distance, minval, maxval, new Point(), new Point(), dilate);
        System.out.println("::minval maxval: "+ Arrays.toString(maxval));
        Mat distace_thresh = new Mat(threash.size(), CV_32S);
        threshold(distance, distace_thresh, maxval[0]*0.3, 255.0, 0);
        //imwrite("label.jpg", label);
        //imwrite("dilate.jpg", dilate);
        //imwrite("gray.jpg", gray);
        //imwrite("thresh.jpg", threash);
        imwrite("distance.jpg", distance);
        imwrite("distace_thresh.jpg", distace_thresh);
        Mat distace_threshint= new Mat(distace_thresh.size(), CV_8U,distace_thresh.retainReference());
        Mat centroids = new Mat();
        Mat labels = new Mat();
        Mat stats = new Mat();
        
        int connectedComponents = opencv_imgproc.connectedComponents(distace_threshint, labels);
       
        
        //int connectedComponents = opencv_imgproc.connectedComponentsWithStats(distace_thresh, labels, stats, centroids);
        System.out.println("::Connected result: " + connectedComponents);

        Mat areas = stats.col(opencv_imgproc.CC_STAT_AREA).rowRange(1, connectedComponents);

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
        
    }

}
