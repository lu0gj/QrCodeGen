import java.util.Arrays;

public class TableGenerator {
    private final String texte;
    private int[] textencode;
    public TableGenerator(String texte){
        this.texte=texte;
    }
    private void encodeRS(int N){
        ReedSolomon reedSolomon = new ReedSolomon();
        textencode = reedSolomon.encodeRs(texte,N);
    }
    public static void main(String[] args) {
        TableGenerator tableGenerator = new TableGenerator("test");
        tableGenerator.encodeRS(3);
        System.out.println(Arrays.toString(tableGenerator.textencode));
    }
}
