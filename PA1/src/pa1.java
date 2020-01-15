import java.util.*;
import java.util.stream.*;


public class pa1 {

    // lsp
    static <T> List<T> longestSmallerPrefix(List<T> a, List<T> b, Comparator<? super T> cmp){

        //final array
        List<T> resultingList;

        Stream<List<Integer>> equalityResult;

        equalityResult = IntStream.range(0, Math.min(a.size(),b.size())).mapToObj(i -> {
            if( cmp.compare(a.get(i), b.get(i)) <= 0){
                return Arrays.asList(1, i);
            }else{
                return Arrays.asList(0, i);
            }
        });

        final Boolean[] done = new Boolean[]{false};

        resultingList = equalityResult.filter(i -> {
            if (i.get(0) == 1 && !done [0]){
                return true;
            } else {
                done[0] = true;
                return false;
            }
        }).map(i -> a.get(i.get(1))).collect(Collectors.toList());

        System.out.println(resultingList);
        return resultingList;
    }

    public static void main (String [] args){

        List<Integer> a = Arrays.asList(1,2);
        List<Integer> b = Arrays.asList(2,1);

        longestSmallerPrefix(a,b, (x,y) -> x-y);
    }
}
