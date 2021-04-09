public class Parcours21 extends Parcour {

    public Parcours21(String textACoder){
        super(textACoder,21,26);
    }
    protected void setQrData(){
        /* --- Variables --- */
        // positions de départ hardcodées (i,j), sens de parcours nombres de lignes (0:down, 1:up),
        // nombres de lignes à lire (nb_lines) , (sur 1 seule colonne:1)
        int[][] start_bits_list = new int[][] {{20,20,1,12,0}, {9,18,0,12,0}, {20,16,1,12,0}, {9,14,0,12,0},
                {20,12,1,14,0}, {5,12,1,6,0}, {0,10,0,6,0}, {7,10,0,14,0},
                {12,8,1,4,0}, {9,5,0,4,0}, {12,3,1,4,0}, {9,1,0,4,0}};
        int[] start_bit;

        /* --- Traitement --- */
        for (int[] ints : start_bits_list) { //Parcours de la liste start bit
            start_bit = ints;

            if (start_bit[2] == 1) { // sens de parcours : UP
                setDataUp(start_bit[0], start_bit[1], start_bit[3], start_bit[4]);
            } else {                     // sens de parcours : DOWN
                setDataDown(start_bit[0], start_bit[1], start_bit[3], start_bit[4]);
            }
        }
    }
    protected void setFixe(){ //met un pixel noir sur les parties fixes d'un Qrcode21
        Oeuil(0,0);
        Oeuil(0,14);
        Oeuil(14,0);
        qr_data[8][6]=1;
        qr_data[10][6]=1;
        qr_data[12][6]=1;
        qr_data[6][8]=1;
        qr_data[6][10]=1;
        qr_data[6][12]=1;
        qr_data[13][8]=1;
    }
    protected int[] getCorrectionValue(int formatbits) { // Renvoie le nombres d'octets de redondance
        int[] correctionValue = new int[] {-1, -1};
        int correctionLevel = formatbits >> 13;

        //nb de bytes de redondance (total - bytes de données); nb de blocs
        switch (correctionLevel) {
            case 1 -> correctionValue = new int[]{7, 1}; //Low
            case 0 -> correctionValue = new int[]{10, 1};
            case 3 -> correctionValue = new int[]{13, 1};
            case 2 -> correctionValue = new int[]{17, 1};
        }
        return correctionValue;
    }

}
