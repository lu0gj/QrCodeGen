import java.util.Arrays;

public class Parcour {

    /* ------------ VARIABLES -------------------- */
    private final String textACoder;
    private int iterateur = 0;
    protected int[][] qr_data;
    protected int[][] qr_data_mask;
    protected int qr_size;
    private int format ;
    private final int qr_version;
    private final int qr_formatbits_size = 15;
    protected int qr_nbBytes;
    public String bit = "";
    private final ReedSolomon RS;

    /* ------------ CONSTRUCTEURS ---------------- */
    public Parcour(String textACoder, int qr_size, int qr_nbBytes) {
        this.RS = new ReedSolomon();
        this.qr_nbBytes = qr_nbBytes;
        this.qr_size = qr_size;
        this.qr_version = (this.qr_size - 17) / 4;
        this.qr_data = new int[this.qr_size][this.qr_size];
        this.qr_data_mask = new int[this.qr_size][this.qr_size];
        this.textACoder = textACoder;
        printMatrix(qr_data);
    }

    protected void encodeRS(int N) {
        int[] textEncodeRS = RS.encodeRs(textACoder, N);
        printMatrix(qr_data);
        for (int x : textEncodeRS) bit = bit.concat(intToBinary(x, 8));
    }

    protected String intToBinary(int n, int type) {
        String binary = "";
        for (int i = 0; i < type; ++i) {
            switch (n % 2) {
                case 0 -> binary = "0".concat(binary);
                case 1 -> binary = "1".concat(binary);
            }
            n /= 2;
        }
        return binary;
    }
    protected void encodeFormat(){
        int g = 0x537;
        for(int i = 4; i >= 0; i--){
            if ((format !=0) (1 << (i+10))) format ^= g << i;
        }
    }
    private void setData(int i,int j){
        bit+="1";
        qr_data[i][j] = (bit.charAt(iterateur)-48)^getMaskValue(0,i,j);
        iterateur++;
    }
    protected void Oeuil(int i,int j){ // dessine un repères autour du Qrcode
        for(int x =0; x<=6; x++){
            qr_data[i+x][j]=1;
            qr_data[i+x][j+6]=1;
        }
        for(int y = 1;y<=6;y++){
            qr_data[i][j+y]=1;
            qr_data[i+6][j+y]=1;
        }
        for(int x=2;x<=4;x++){
            for(int y=2;y<=4;y++){
                qr_data[i+x][y+j]=1;
            }
        }
    }

    protected void setDataUp(int i, int j, int nb_lines, int col) {
        if (col == 0) {
            for (int y = 0; y < nb_lines; y++) {
                setData((i-y),j);
                setData((i-y),j-1);
            }
        } else {
            for (int y = 0; y < nb_lines; y++) {
                setData((i-y),j);
            }
        }
    }

    protected void setDataDown(int i, int j, int nb_lines, int col) {
        if (col == 0) {
            for (int y = 0; y < nb_lines; y++) {
                setData(i+y,j);
                setData(i+y,j-1); }
        } else {
            for (int y = 0; y < nb_lines; y++) {
                setData(i+y,j);
            }
        }
    }

    public int[][] getQr_data() {
        return qr_data;
    }

    public String getBit() {
        return bit;
    }
    private int getMaskValue(int mask, int i, int j)                  // Renvoie la valeur du bit du masque mask à la position i,j
    {
        int value = 0;							// par défaut la valeur est 0
        switch(mask) {							// selon le mask on met à 1 les positions validant la condition du masque
            case 0:
                if ((i + j) % 2 == 0) value = 1;
                break;
            case 1:
                if (i % 2 == 0) value = 1;
                break;
            case 2:
                if (j % 3 == 0) value = 1;
                break;
            case 3:
                if ((i + j) % 3 == 0) value = 1;
                break;
            case 4:
                if ((i/2 + j/3) % 2 == 0) value = 1;
                break;
            case 5:
                if ((i * j) % 2 + (i * j) % 3 == 0) value = 1;
                break;
            case 6:
                if (((i * j) % 3 + i * j) % 2 == 0) value = 1;
                break;
            case 7:
                if (((i * j) % 3 + i + j) % 2 == 0) value = 1;
                break;
        }
        return value;
    }

    public static void printMatrix(int[][] Matrix) {
        for (int[] matrix : Matrix) {
            System.out.print(Arrays.toString(matrix));
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        Parcours21 parcours21 = new Parcours21("adazdadazddq");
        parcours21.encodeRS(3);
        System.out.println(parcours21.getBit());
        parcours21.setQrData();
        parcours21.setFixe();
        int[][] Matrix = parcours21.getQr_data();
        printMatrix(Matrix);

    }
}

