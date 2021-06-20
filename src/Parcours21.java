public class Parcours21 extends Parcour {

    public Parcours21(String textACoder){
        super(textACoder,21);
    }

    @Override
    protected void setLevelCorrection(int lenMessage){
        if (lenMessage<=9){NbreRedondance=17;levelCorrection="H";}
        else if (lenMessage<=13){NbreRedondance=13;levelCorrection="Q";}
        else if (lenMessage<=16){NbreRedondance=10;levelCorrection="M";}
        else if (lenMessage<=19){NbreRedondance=7;levelCorrection="L";}
        else {throw new java.lang.RuntimeException("mot trop long");}
    }

    @Override
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
        iterateur=0;
    }
    protected void setMaskFormat(int i, int x, int y){ //applique le masque de format et positionne sur le qrcode à la plac x,y
        String formatMask ="101010000010010";
        qr_data[x][y]=((int) formatStr.charAt(i) -48)^((int) formatMask.charAt(i) -48);
    }
    protected void setMaskOnFormat(){
        for (int i=0;i<=5;i++){
            setMaskFormat(i,8,i);
            setMaskFormat(i,(20-i),8);
        }
        for (int i=15;i<=20;i++){
            setMaskFormat(i-6,8,i);
            setMaskFormat(i-6,(20-i),8);
        }
        setMaskFormat(6,8,7);
        setMaskFormat(7,8,8);
        setMaskFormat(8,7,8);
        setMaskFormat(6,14,8);
        setMaskFormat(7,8,13);
        setMaskFormat(8,8,14);
    }

    @Override
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


}
