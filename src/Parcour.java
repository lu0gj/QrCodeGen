import java.util.Arrays;

public class Parcour {

    /* ------------ VARIABLES -------------------- */
    protected final String textACoder;
    protected final String ModeName;
    protected int iterateur = 0; // permet de parcourir les bits à écrire dans le Qrcode
    protected int[][] qr_data;
    protected int qr_size;
    protected int format ;
    protected String formatStr;
    public String bit = "";
    protected String levelCorrection="";
    protected int NbreRedondance;
    protected int mask;
    protected final ReedSolomon RS;

    /* ------------ CONSTRUCTEURS ---------------- */
    public Parcour(String textACoder, int qr_size) {
        this.RS = new ReedSolomon();
        this.qr_size = qr_size;
        this.qr_data = new int[this.qr_size][this.qr_size];
        this.textACoder = textACoder;
        if (textACoder.matches("[0-9]*")) {
            this.ModeName = "Numeric";
        }
        else {
            this.ModeName="Byte";
        }
    }

    protected void encodeRS() {
        String textModified;
        int[] textEncodeRS;
        switch (ModeName) {
            case "Byte" -> {
                textModified=transfoByte(textACoder,textACoder.length());
            }
            case "Numeric" -> {
                textModified = transfoNumeric(textACoder);
            }
            default -> throw new java.lang.RuntimeException("Mode pas encore implémenté");
        }
        textEncodeRS = RS.encodeRs(textModified, NbreRedondance);
        for (int x : textEncodeRS){ bit = bit.concat(intToBinary(x, 8));}
    }

    protected String transfoByte(String msg, int len) {//ajoute les info à un texte en byte
        // rend un String de 0 et 1 à coder
        StringBuilder bit= new StringBuilder("0100");
        bit.append(intToBinary(len,8));
        for (int i = 0; i < len; i++) {
            char c = msg.charAt(i);
            bit.append(intToBinary(c,8));
        }
        return toByte(bit);
    }

    protected String transfoNumeric(String msg) {//ajoute les info à un texte en numerique
        // rend un String de 0 et 1 à coder
        StringBuilder bit= new StringBuilder("0001");
        String[] textSub = msg.split("(?<=\\G.{3})");
        bit.append(intToBinary(textSub.length,10));
        for (String x : textSub) {
            bit.append(intToBinary(Integer.parseInt(x),10));
        }
        return toByte(bit);
    }

    private String toByte(StringBuilder bit){ //redécoupe la suite de 0 et 1 en block de byte pour calculer RS
        bit.append("0000");
        StringBuilder out = new StringBuilder();
        String[] bitSub = bit.toString().split("(?<=\\G.{8})"); //permet de séparer en block de 8
        for (String x : bitSub){ out.append((char) Integer.parseInt(x, 2));}
        setLevelCorrection(bitSub.length);
        return out.toString();
    }

    protected void setLevelCorrection(int lenMessage){} //définie dans les classes filles, elle permet de choisir le niveau de correction

    // les 2 fonctions suivantes permettent de calculet les bits de formats qui seront écrits dans le Qrcode
    protected int checkformat(int fmt){
        int g = 0x537;
        for(int i = 4; i >= 0; i--){
            if ((fmt & (1 << (i+10))) != 0) fmt ^= g << i;
        }
        return fmt ;
    }

    protected void encodeFormat(){
        format = (format<<10) ^ checkformat(format<<10);
        formatStr=intToBinary(format,15);
    }

    private int mesureScore(){// Permet de mesurer un score pour un mask donné
        int l = qr_data.length;
        int score= 0;
        for(int i=1;i<l-1;i++){
            for(int j=1;j<l-1;j++){
                int s=(qr_data[i-1][j]+qr_data[i+1][j]+qr_data[i][j-1]+qr_data[i][j+1]);
                if(qr_data[i][j]==1){ score+=4-s; }
                else {score+=s;}
            }
        }
        return score;
    }

    protected void setMask(){//Choix du mask en avec une fonction de score qui pénalise des pixels de mêmes couleurs à côté
        int m =0;
        int bestScore= 0;
        for(int i=0;i<=7;i++) {
            mask = i;
            setQrData();
            int Score = mesureScore();
            if (Score > bestScore) {
                m = i;
                bestScore = Score;
                }
            }
            mask=m;
            setQrData();
        }


    protected void setFormat(){//met le format en forme
        String maskStr=intToBinary(mask,3);
        switch (levelCorrection){
            case "L" -> format= Integer.parseInt("01".concat(maskStr),2);
            case "M" -> format= Integer.parseInt("00".concat(maskStr),2);
            case "Q" -> format= Integer.parseInt("11".concat(maskStr),2);
            case "H" -> format= Integer.parseInt("10".concat(maskStr),2);
        }
        encodeFormat();
    }

    private void setData(int i,int j){ // écrit directement sur le Qrcode en position i,j en fonction du mask
        try {
            qr_data[i][j] = (bit.charAt(iterateur) - 48) ^ getMaskValue(mask, i, j);
        } catch (Exception e) {
            qr_data[i][j] = getMaskValue(mask, i, j);
        }
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

    protected void setQrData(){
        System.out.println("ERROR : appel à setQrdata dans la classe mère");
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


    protected int[][] getQr_data(){
        return qr_data;
    }

    public int[][] tableGen(){
        encodeRS();
        setMask();
        setFormat();
        setFixe();
        setMaskOnFormat();
        return getQr_data();
    }

    protected void setMaskOnFormat() {
    }

    protected void setFixe() {
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
    public static void printMatrix(int[][] Matrix) {//utile pour debug
        for (int[] matrix : Matrix) {
            System.out.print(Arrays.toString(matrix));
            System.out.print("\n");
        }
    }
}

