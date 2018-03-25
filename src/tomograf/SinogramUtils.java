package tomograf;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class SinogramUtils {

    public static double calculatePixel(Mat picture, double i, int j, int w, double beta, int count) {
        double radius = w/2.0 - 1.0;
        double x1 = w/2.0 + radius * Math.cos(Math.toRadians(i));
        double y1 = w/2.0 + radius * Math.sin(Math.toRadians(i));

        double step = beta / (count-1.0);
        double rotation = step * (j - (count/2.0));

        double x2 = w/2.0 - radius * Math.cos(Math.toRadians(i - rotation));
        double y2 = w/2.0 - radius * Math.sin(Math.toRadians(i - rotation));

        double vecX = x2 - x1;
        double vecY = y2 - y1;

        double size = Math.sqrt(vecX * vecX + vecY * vecY);

        vecX = 0.2 * vecX / size;
        vecY = 0.2 * vecY / size;

        double posX = x1;
        double posY = y1;

        double result = 0;

        for(int it = 1; it <= 5 * size; it++) {
            posX += vecX;
            posY += vecY;
            if(Math.round(posX) >= 0 && Math.round(posX) < w && Math.round(posY) >= 0 && Math.round(posY) < w) {
                result += picture.get((int) Math.round(posX), (int) Math.round(posY))[0];
            }
        }

        return result/size;
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
