import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BFS
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors){

        List<Point> path = new LinkedList<>();
        List<Point> Closed_list = new LinkedList<>();
        Comparator<Point> comp_point = (p1, p2) -> p1.g_val - p2.g_val;
        PriorityQueue<Point> pointQueue = new PriorityQueue<>(comp_point);
        HashMap<Integer, Point> pointHashMap = new HashMap<>();
        Point Current_pt = start;
        pointQueue.add(Current_pt);
        pointHashMap.put(Current_pt.hashCode(), Current_pt);

        while (!withinReach.test(Current_pt, end) && !pointQueue.isEmpty()) {

            Current_pt = pointQueue.poll();

            List<Point> new_potential_neighbors = potentialNeighbors.apply(Current_pt)
                    .filter(canPassThrough)
                    .filter(pt -> !pt.equals(start))
                    .filter(pt -> !pt.equals(end))
                    .filter(point -> Closed_list.stream().noneMatch(pt -> pt.equals(point)))
                    .collect(Collectors.toList());

            for (Point pt : new_potential_neighbors){

                if (!pointHashMap.containsKey(pt.hashCode())){
                    pt.setG_val(Current_pt.g_val + 1);
                    pt.SetPrior(Current_pt);
                    pointHashMap.put(pt.hashCode(), pt);
                    pointQueue.add(pt);
                }
                else {

                    int g = Current_pt.g_val + 1;

                    if (g < pointHashMap.get(pt.hashCode()).g_val){
                        pt.SetPrior(Current_pt);
                        pt.setG_val(g);
                        pointQueue.remove(pointHashMap.get(pt.hashCode()));
                        pointQueue.add(pt);
                        pointHashMap.remove(pt.hashCode());
                        pointHashMap.put(pt.hashCode(), pt);
                    }
                }

                Closed_list.add(Current_pt);
            }
        }

        if (pointQueue.isEmpty()){return path;}

        while (!Current_pt.equals(start)){
            path.add(0, Current_pt);
            Current_pt = Current_pt.prior_node;
        }
        return path;
    }
}
