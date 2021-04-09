import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImgGenerator{
    private BufferedImage img ;

    public void createimage(int n) { // fonction crée un fond blanc pour l'image de taille n
        try {
            img = new BufferedImage(
                    n,n, BufferedImage.TYPE_INT_RGB);
            int col = (255 << 16) | (255 << 8) | 255;
            for(int x = 0; x < n ; x++){
                for(int y = 0; y < n; y++){
                    img.setRGB(x, y, col);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void draw(int[][] qr_table, int r, int g, int b){ // parcourt la table pour colorer les pixels représentés par un 1
        int size = qr_table.length;
        try{
            int col = (r << 16) | (g << 8) | b; // définition de la couleur
            for(int x = 0; x < size ; x++){
                for(int y = 0; y < size; y++){
                    if (qr_table[x][y] == 1){
                        for(int k = 0; k<10; k++){
                            for(int i = 0; i<10; i++){
                                img.setRGB(10*x+k, 10*y+i, col);
                            }

                        }
                    }
                }
            }
            // enregistrement
            File f = new File("MyFile.png");
            ImageIO.write(img, "PNG", f);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }}