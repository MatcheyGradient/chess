package piece;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class PieceImages {

    public static BufferedImage[][] images = new BufferedImage[2][6];

    public static void init(){
        for(int j = 0; j < 2; j++){
            for(int i = 1; i < 7; i++){
                AffineTransform at = new AffineTransform();
                BufferedImage after = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                BufferedImage image = null;

                try {
                    image = ImageIO.read(PieceImages.class.getResource("/resources/pieces.png"));
                } catch (Exception ignored) {}
                image = image.getSubimage((int) (getImageX(i)), (j == 0)? 0 : 133, 133, 133);

                at.scale(0.75, .75);
                AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                image = scaleOp.filter(image, after);
                images[j][i - 1] = image;
            }
        }
    }

    static float getImageX(int i){
        return switch(i){
            case 1 -> 500 * (4/3f);
            case 2 -> 400 * (4/3f);
            case 3 -> 300 * (4/3f);
            case 4 -> 200 * (4/3f);
            case 5 -> 100 * (4/3f);
            case 6 -> 0;
            default -> -1;
        };
    }
}
