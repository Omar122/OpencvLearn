package com.mycompany.opencvlearn;

import java.awt.BorderLayout;
import java.awt.Button;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.bytedeco.javacpp.Loader;
import static org.bytedeco.opencv.global.opencv_core.inRange;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_java;
import org.opencv.highgui.HighGui;

public class ThresholdInRange {

    private static int MAX_VALUE = 255;
    private static int MAX_VALUE_H = 360 / 2;
    private static final String WINDOW_NAME = "Thresholding Operations using inRange demo";
    private static final String LOW_H_NAME = "Low H";
    private static final String LOW_S_NAME = "Low S";
    private static final String LOW_V_NAME = "Low V";
    private static final String HIGH_H_NAME = "High H";
    private static final String HIGH_S_NAME = "High S";
    private static final String HIGH_V_NAME = "High V";
    private JSlider sliderLowH;
    private JSlider sliderHighH;
    private JSlider sliderLowS;
    private JSlider sliderHighS;
    private JSlider sliderLowV;
    private JSlider sliderHighV;
    Mat matFrame;

    private JFrame frame;
    private JLabel imgCaptureLabel;
    private JLabel imgDetectionLabel;
    Button button;

    public ThresholdInRange() {
        matFrame = imread("C:\\Users\\oalfuraydi.MURL097631B55LG\\Documents\\NetBeansProjects\\OpencvLearn\\src\\main\\java\\com\\mycompany\\opencvlearn\\recipts2.jpg");
        System.out.println("====111 " + matFrame.getPointer().toString() + " Address " + matFrame.address());
        // Create and set up the window.
        frame = new JFrame(WINDOW_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        // Set up the content pane.

        Image img = HighGui.toBufferedImage(new org.opencv.core.Mat(matFrame.address())).getScaledInstance(matFrame.rows() / 6, matFrame.cols() / 6, Image.SCALE_DEFAULT);

        addComponentsToPane(frame.getContentPane(), img);
        // Use the content pane's default BorderLayout. No need for
        // setLayout(new BorderLayout());
        // Display the window.
        frame.pack();
        frame.setVisible(true);

        button.addActionListener((ActionEvent e) -> {

            this.start();

        });

    }

    public void start() {

        matFrame = imread("C:\\Users\\oalfuraydi.MURL097631B55LG\\Documents\\NetBeansProjects\\OpencvLearn\\src\\main\\java\\com\\mycompany\\opencvlearn\\recipts2.jpg");
        System.out.println("==== 222 " + matFrame.getPointer().toString() + " Address " + matFrame.address());
        process(matFrame);

    }

    private void addComponentsToPane(Container pane, Image img) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        button = new Button("button!!!!");
        JPanel sliderPanel = new JPanel();
        sliderPanel.add(button);
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.add(new JLabel(LOW_H_NAME));
        sliderLowH = new JSlider(0, MAX_VALUE_H, 0);
        sliderLowH.setMajorTickSpacing(50);
        sliderLowH.setMinorTickSpacing(10);
        sliderLowH.setPaintTicks(true);
        sliderLowH.setPaintLabels(true);
        sliderPanel.add(sliderLowH);
        sliderPanel.add(new JLabel(HIGH_H_NAME));
        sliderHighH = new JSlider(0, MAX_VALUE_H, MAX_VALUE_H);
        sliderHighH.setMajorTickSpacing(50);
        sliderHighH.setMinorTickSpacing(10);
        sliderHighH.setPaintTicks(true);
        sliderHighH.setPaintLabels(true);
        sliderPanel.add(sliderHighH);
        sliderPanel.add(new JLabel(LOW_S_NAME));
        sliderLowS = new JSlider(0, MAX_VALUE, 0);
        sliderLowS.setMajorTickSpacing(50);
        sliderLowS.setMinorTickSpacing(10);
        sliderLowS.setPaintTicks(true);
        sliderLowS.setPaintLabels(true);
        sliderPanel.add(sliderLowS);
        sliderPanel.add(new JLabel(HIGH_S_NAME));
        sliderHighS = new JSlider(0, MAX_VALUE, MAX_VALUE);
        sliderHighS.setMajorTickSpacing(50);
        sliderHighS.setMinorTickSpacing(10);
        sliderHighS.setPaintTicks(true);
        sliderHighS.setPaintLabels(true);
        sliderPanel.add(sliderHighS);
        sliderPanel.add(new JLabel(LOW_V_NAME));
        sliderLowV = new JSlider(0, MAX_VALUE, 0);
        sliderLowV.setMajorTickSpacing(50);
        sliderLowV.setMinorTickSpacing(10);
        sliderLowV.setPaintTicks(true);
        sliderLowV.setPaintLabels(true);
        sliderPanel.add(sliderLowV);
        sliderPanel.add(new JLabel(HIGH_V_NAME));
        sliderHighV = new JSlider(0, MAX_VALUE, MAX_VALUE);
        sliderHighV.setMajorTickSpacing(50);
        sliderHighV.setMinorTickSpacing(10);
        sliderHighV.setPaintTicks(true);
        sliderHighV.setPaintLabels(true);
        sliderPanel.add(sliderHighV);
        sliderLowH.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int valH = Math.min(sliderHighH.getValue() - 1, source.getValue());
                sliderLowH.setValue(valH);
            }
        });
        sliderHighH.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int valH = Math.max(source.getValue(), sliderLowH.getValue() + 1);
                sliderHighH.setValue(valH);
            }
        });
        sliderLowS.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int valS = Math.min(sliderHighS.getValue() - 1, source.getValue());
                sliderLowS.setValue(valS);
            }
        });
        sliderHighS.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int valS = Math.max(source.getValue(), sliderLowS.getValue() + 1);
                sliderHighS.setValue(valS);
            }
        });
        sliderLowV.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int valV = Math.min(sliderHighV.getValue() - 1, source.getValue());
                sliderLowV.setValue(valV);
            }
        });
        sliderHighV.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int valV = Math.max(source.getValue(), sliderLowV.getValue() + 1);
                sliderHighV.setValue(valV);
            }
        });
        pane.add(sliderPanel, BorderLayout.PAGE_START);
        JPanel framePanel = new JPanel();
        imgCaptureLabel = new JLabel(new ImageIcon(img));
        framePanel.add(imgCaptureLabel);
        imgDetectionLabel = new JLabel(new ImageIcon(img));
        framePanel.add(imgDetectionLabel);
        framePanel.add(button);
        pane.add(framePanel, BorderLayout.CENTER);
    }

    private void update(Mat matCapture, Mat matThresh) throws InterruptedException, IOException {

    }

    protected void process(Mat frame) {

    }

    private class CaptureTask extends SwingWorker<Void, Mat> {

        @Override
        protected Void doInBackground() throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        protected void done(Mat matCapture, Mat matThresh) {
            //opencv_imgcodecs.imwrite("imgThresh.jpg", imgThresh);
            System.out.println("imgThresh Address:" + matCapture.toString());
            System.out.println("imgCapture Address:" + matThresh.toString());

            Image imgCapture = HighGui.toBufferedImage(new org.opencv.core.Mat(matCapture.address())).getScaledInstance(matCapture.rows() / 7, matCapture.cols() / 7, Image.SCALE_DEFAULT);
            Image imgThresh = HighGui.toBufferedImage(new org.opencv.core.Mat(matThresh.address())).getScaledInstance(matThresh.rows() / 7, matThresh.cols() / 7, Image.SCALE_DEFAULT);

            imgCaptureLabel.setIcon(new ImageIcon(imgCapture));
            imgDetectionLabel.setIcon(new ImageIcon(imgThresh));
            matCapture.deallocate();
            matThresh.deallocate();
            //frame.repaint();
        }

        @Override
        protected void process(List<Mat> chunks) {
            Mat frameHSV = new Mat();
            Mat frame=chunks.get(chunks.size()-1);
            System.out.println("==== 333 " + frame.getPointer().toString() + " Address " + frame.address());
            //Mat frames = new Mat(new Pointer(framesAdd));

            opencv_imgproc.cvtColor(frame, frameHSV, opencv_imgproc.COLOR_BGR2HSV);
            Mat thresh = new Mat();
            inRange(frameHSV, new Mat((double) sliderLowH.getValue(), (double) sliderLowS.getValue(), (double) sliderLowV.getValue()),
                    new Mat((double) sliderHighH.getValue(), (double) sliderHighS.getValue(), (double) sliderHighV.getValue()), thresh);

            done(frame, thresh);
            frameHSV.deallocate();
            frame.deallocate();
            System.gc();
        }

    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        // Load the native OpenCV library

        Loader.load(opencv_java.class);
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.

        javax.swing.SwingUtilities.invokeAndWait(() -> {
            ThresholdInRange thresholdInRange = new ThresholdInRange();

        });

    }
}
