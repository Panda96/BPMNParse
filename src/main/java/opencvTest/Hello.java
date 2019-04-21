package opencvTest;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Created by PandaLin on 2019/1/15.
 */
public class Hello {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new Hello().run();

    }

    public void run(){
        String base = "src/main/resources/imgs/";
        String fileName = "Cologne.png";
        Mat img = Imgcodecs.imread(base+fileName);

        HighGui.namedWindow("image", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow("image", img);

        Mat grey = new Mat();
        Imgproc.cvtColor(img, grey, Imgproc.COLOR_BGR2GRAY);
        Mat sobelx = new Mat();
        Imgproc.Sobel(grey, sobelx, CvType.CV_32F, 1, 0);
        Core.MinMaxLocResult res = Core.minMaxLoc(sobelx); // find minimum and maximum intensities
        Mat draw = new Mat();
        double maxVal = res.maxVal, minVal = res.minVal;
        sobelx.convertTo(draw, CvType.CV_8U, 255.0 / (maxVal - minVal), -minVal * 255.0 / (maxVal - minVal));
        HighGui.namedWindow("draw", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow("draw", draw);

        HighGui.waitKey(0);
        System.exit(0);
    }
}
