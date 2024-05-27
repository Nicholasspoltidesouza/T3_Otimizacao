import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dominos {
    public final int a;
    public final int b;
    private static boolean primeiraSequenciaEncontrada = false;
    private static Map<List<Dominos>, Boolean> memo = new HashMap<>();

    public Dominos(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public Dominos inverso() {
        return new Dominos(b, a);
    }

    @Override
    public String toString() {
        return " " + a + " " + b + " ";
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Domino <input_file>");
            return;
        }

        String nomeArquivo = args[0];
        List<Dominos> listaDominos = leitorDeArquivo(nomeArquivo);

        long inicio = System.nanoTime();

        System.out.println("Sequência de dominós encontrada:");

        List<Dominos> sequencia = new ArrayList<>();
        sequenciaDominos(sequencia, listaDominos);

        long fim = System.nanoTime();
        double duracao = (fim - inicio) / 1_000_000_000.0;

        System.out.println("Tempo de execução: " + duracao + " segundos");
    }

    private static List<Dominos> leitorDeArquivo(String fileName) {
        List<Dominos> listaDominos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int a = Integer.parseInt(parts[0]);
                int b = Integer.parseInt(parts[1]);
                listaDominos.add(new Dominos(a, b));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaDominos;
    }

    private static void sequenciaDominos(List<Dominos> sequencia, List<Dominos> listaDominos) {
        if (listaDominos.isEmpty() || primeiraSequenciaEncontrada) {
            if (!primeiraSequenciaEncontrada) {
                System.out.println(sequencia);
                primeiraSequenciaEncontrada = true;
            }
            return;
        }
        for (int i = 0; i < listaDominos.size(); ++i) {
            Dominos domino = listaDominos.get(i);
            if (podeAdicionar(domino, sequencia)) {
                sequencia.add(domino);
                List<Dominos> resposta = new ArrayList<>(listaDominos);
                resposta.remove(i);
                if (!memo.containsKey(resposta)) {
                    memo.put(resposta, true);
                    sequenciaDominos(sequencia, resposta);
                }
                sequencia.remove(sequencia.size() - 1);
            }
            domino = domino.inverso();
            if (podeAdicionar(domino, sequencia)) {
                sequencia.add(domino);
                List<Dominos> resposta = new ArrayList<>(listaDominos);
                resposta.remove(i);
                if (!memo.containsKey(resposta)) {
                    memo.put(resposta, true);
                    sequenciaDominos(sequencia, resposta);
                }
                sequencia.remove(sequencia.size() - 1);
            }
        }
    }

    private static boolean podeAdicionar(Dominos dom, List<Dominos> to) {
        return to.isEmpty() || to.get(to.size() - 1 ).b == dom.a;
    }
}
