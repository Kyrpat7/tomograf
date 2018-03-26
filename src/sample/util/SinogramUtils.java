package sample.util;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.util.ArrayList;

public class SinogramUtils {

    public static ArrayList<ArrayList<Integer>> listOfLines = new ArrayList<ArrayList<Integer>>();

    public static double bresenhamLine(Mat picture, int x1, int y1, int x2, int y2) {
        double result = 0;
        int len = 0;
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
        result += picture.get(x, y)[0];
        len++;

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
                result += picture.get(x, y)[0];
                len++;
            }
        } else {
            ai = ( dx - dy ) * 2;
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
                result += picture.get(x, y)[0];
                len++;
            }
        }

        return result / len;
    }

    public static double calculatePixel(Mat picture, double i, int j, int w, double beta, int count) {
        double radius = w/2.0 - 1.0;
        int x1 = (int)Math.round(w/2.0 + radius * Math.cos(Math.toRadians(i)));
        int y1 = (int)Math.round(w/2.0 + radius * Math.sin(Math.toRadians(i)));

        double step = beta / (count-1.0);
        double rotation = step * (j - (count/2.0));

        int x2 = (int)Math.round(w/2.0 - radius * Math.cos(Math.toRadians(i - rotation)));
        int y2 = (int)Math.round(w/2.0 - radius * Math.sin(Math.toRadians(i - rotation)));

        double result = bresenhamLine(picture, x1, y1, x2, y2);
        return result;
    }

    public static Mat createSinogram(String path, double alpha, double beta, int detectors) {
        Mat picture = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        double width = picture.size().width;
        Mat result = new Mat((int)Math.floor(360.0/alpha), (int)detectors, CvType.CV_32FC1);

        double max = -100;
        for(int i = 0; i < 360/alpha; i++) {
            for(int j = 0; j < detectors; j++) {
                double pixel = calculatePixel(picture, i * alpha, j, (int)width, beta, detectors);
                if(pixel > max)
                    max = pixel;
                result.put(i, j, pixel);
            }
        }
        result.convertTo(result, -1, 255.0 / max);

        return result;
    }
}
