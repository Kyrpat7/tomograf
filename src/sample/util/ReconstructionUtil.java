package sample.util;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32FC1;

public class ReconstructionUtil {

    /*public static double getFilter(int x1, int x2, int y1, int y2, int x, int y, double value) {
        double len = Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        double curr_len = Math.sqrt( (x-x1)*(x-x1) + (y-y1)*(y-y1));
        double k = len/2 - curr_len;
        if( k == 0) return 1 * value;
        //if(k % 2 == 1) return 0;
        double arg = Math.PI * curr_len / len;
        return value * (1 - 1.93*Math.cos(2 * arg) + 1.29 * Math.cos(4*arg) - 0.388*Math.cos(6*arg) + 0.028*Math.cos(8*arg));

    }*/

    public static double getFilter(int x1, int x2, int y1, int y2, int x, int y, double value) {
        double len = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        double curr_len = Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
        double k = len / 2 - curr_len;
        double arg = Math.PI * curr_len / len;
        return value * (0.53836 - 0.46164 * Math.cos(2*arg));
    }

    /*public static void convolve(Mat sin) {
        for(int i = 0; i < sin.size().width; i++) {
            for(int j = 0; j < sin.size().height; j++) {
                int size = (int)sin.size().height;
                double result = 0;
                for(int m = 0; m < sin.size().width; m++) {
                    double value = sin.get(j, m)[0];
                    result += value * getFilter( size - m);
                }
                sin.put(i, j, result);
            }
        }
    }*/


    public static Mat reconstructImage(Mat sinogram, int alpha, int beta, int detectors) {
        Mat result = new Mat(400, 400, CV_32FC1);
        int width = (int)result.size().width;
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

        result.convertTo(result, -1, 200.0/max);
        Imgproc.GaussianBlur(result, result, new Size(11,11), 0.15);
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
        picture.put(x, y, picture.get(x, y)[0] + getFilter(x1, x2, y1, y2, x, y, value));

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
                picture.put(x, y, picture.get(x, y)[0] + getFilter(x1, x2, y1, y2, x, y, value));
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
                picture.put(x, y, (picture.get(x, y)[0] + getFilter(x1, x2, y1, y2, x, y, value)));
            }
        }
    }

}
