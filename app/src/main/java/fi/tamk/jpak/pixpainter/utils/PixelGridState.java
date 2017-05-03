package fi.tamk.jpak.pixpainter.utils;

/**
 * Created by Juuso on 3.5.2017.
 */
public class PixelGridState {

    private static Pixel[][] pixels;

    public static void setPixels(Pixel[][] pixels) {
        if (pixels != null) {
            PixelGridState.pixels = pixels;
        }
    }

    public static Pixel[][] getPixels() {
        return PixelGridState.pixels;
    }
}
