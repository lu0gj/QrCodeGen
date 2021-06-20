

public class QrCreator {
    private ImgGenerator imgGenerator;
    public QrCreator(String text){
        int l = text.length();
        if (l > 17){throw new java.lang.RuntimeException("mot trop long");}
        else {
            imgGenerator =new ImgGenerator(new Parcours21(text).tableGen());
            imgGenerator.draw();
        }
    }

    public QrCreator(String text, boolean RGB){
        int l = text.length();
        int size=l/3;
        if (l>17*3){throw new java.lang.RuntimeException("mot trop long");}
        else {
            imgGenerator =new ImgGenerator(new Parcours21(text.substring(0,size)).tableGen(),new Parcours21(text.substring(size,2*size)).tableGen(),new Parcours21(text.substring(2*size,l)).tableGen());
            imgGenerator.draw();
        }
    }
    public void enregistrement(String addr){
        imgGenerator.enregistrement(addr);
    }
    public void enregistrement(){
        imgGenerator.enregistrement("newfile.png");
    }

    public static void main(String[] args) {
        new QrCreator("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy",true).enregistrement();
    }
}
