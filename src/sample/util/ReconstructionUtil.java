package sample.util;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import static org.opencv.core.CvType.CV_32FC1;

public class ReconstructionUtil {

    public static Mat reconstructImage(Mat sinogram, int alpha, int beta, int detectors) {
        Mat result = new Mat(400, 400, CV_32FC1);
        double width = result.size().width;
        for(int i = 0; i < width; i++)
            for(int j = 0; j < width; j++) {
                result.put(i, j, .0);
            }

        for(int i = 0; i < 360/alpha; i++) {
            for(int j = 0; j < detectors; j++) {
                double radius = width/2.0 - 1.0;
                int x1 = (int)Math.round(width/2.0 + radius * Math.cos(Math.toRadians(i)));
                int y1 = (int)Math.round(width/2.0 + radius * Math.sin(Math.toRadians(i)));

                double step = beta / (detectors-1.0);
                double rotation = step * (j - (detectors/2.0));

                int x2 = (int)Math.round(width/2.0 - radius * Math.cos(Math.toRadians(i - rotation)));
                int y2 = (int)Math.round(width/2.0 - radius * Math.sin(Math.toRadians(i - rotation)));
                bresenhamLine(result, sinogram.get(i, j)[0], x1, y1, x2, y2);
            }
        }

        double max = -100;
        for(int i = 0; i < width; i++)
            for(int j = 0; j < width; j++) {
                if(result.get(i, j)[0] > max) {
                    max = result.get(i, j)[0];
                }
            };

        result.convertTo(result, -1, 255.0/max);

        return result;

    }

    public static void bresenhamLine(Mat picture, double value, int x1, int y1, int x2, int y2) {

        int d, dx, dy, ai, bi, xi, yi;
        int x = x1, y = y1;

        if(x1 < x2) {
            xi = 1;
            dx = x2 - x1;
        } else {
            xi = -1;
            dx = x1 - x2;
        }

        if(y1 < y2) {
            yi = 1;
            dy = y2 - y1;
        } else {
            yi = -1;
            dy = y1 - y2;
        }
        picture.put(x, y, picture.get(x, y)[0] + value);

        if (dx > dy) {
            ai = (dy - dx) * 2;
            bi = dy * 2;
            d = bi - dx;
            // pętla po kolejnych x
            while (x != x2) {
                // test współczynnika
                if (d >= 0) {
                    x += xi;
                    y += yi;
                    d += ai; }
                else {
                    d += bi;
                    x += xi;
                }
                picture.put(x, y, picture.get(x, y)[0] + value);
            }
        } else {
            ai = (dx - dy) * 2;
            bi = dx * 2;
            d = bi - dy;
            while (y != y2) {
                if (d >= 0) {
                    x += xi;
                    y += yi;
                    d += ai;
                } else {
                    d += bi;
                    y += yi;
                }
                picture.put(x, y, picture.get(x, y)[0] + value);
            }
        }
    }

}
