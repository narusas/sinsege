package org.jpedal.external;

import org.jpedal.objects.GraphicsState;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Allow user to augment image handling in JPedal
 */
public interface ImageHandler {

        //tell JPedal if it ignores its own Image code or not
        public boolean alwaysIgnoreGenericHandler();

        //pass in raw data for image handling - if valid image returned it will be used.
        //if alwaysIgnoreGenericHandler() is true JPedal code always ignored. If false, JPedal code used if null
        public BufferedImage processImageData(Map values, GraphicsState gs );

        /**Indicate that image already scaled so should not be scaled/clipped by JPedal*/
        public boolean imageHasBeenScaled();
}
