import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImgGenerator{
    // On donne des tableaux de 0 et 1 et la classe les transforme en Qrcode
    private BufferedImage img ;
    private int [][] rqr_table;
    private int [][] gqr_table;
    private int [][] bqr_table;
    private  int size;
    private int scale;

    public ImgGenerator(int [][] rqr_table, int [][] gqr_table, int [][] bqr_table,int scale){//generer Qr code RGB
        this.bqr_table=bqr_table;
        this.rqr_table=rqr_table;
        this.gqr_table=gqr_table;
        this.size=bqr_table.length;
        this.scale=scale;
        this.img = new BufferedImage(scale*size,scale*size, BufferedImage.TYPE_INT_RGB);
    }
    public ImgGenerator(int [][] rqr_table, int [][] gqr_table, int [][] bqr_table) {//generer Qr code RGB
        this( rqr_table, gqr_table,bqr_table,10*rqr_table.length);

        }
    public ImgGenerator(int[][] qr_table,int scale){//generer Qr noir et blanc
        this(qr_table,qr_table,qr_table,scale);
    }

    public ImgGenerator(int[][] qr_table){
        this(qr_table,qr_table,qr_table,10*qr_table.length);
    }

    public void draw(){
        for(int x = 0; x < size ; x++){
            for(int y = 0; y < size; y++){
                drawPixels(x,y);
            }
        }
    }

    public void enregistrement(String addr){
        try{
            File f = new File(addr);
            ImageIO.write(img, "PNG", f);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void drawPixels(int i, int j){
        int col = ((255*(1-rqr_table[j][i])) << 16) | ((255*(1-gqr_table[j][i])) << 8) | (255*(1-bqr_table[j][i]));
        for(int k = 0; k<scale; k++){
            for(int l = 0; l<scale;l++){
                img.setRGB(scale*i+k, scale*j+l, col);
            }
        }
    }
}