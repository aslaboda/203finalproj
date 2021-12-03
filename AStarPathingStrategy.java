import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy
{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        Comparator<Node> f_value_comp = (n1, n2) -> n1.get_total_dist() - n2.get_total_dist();
        Queue<Node> open_lst_PQ = new PriorityQueue<>(f_value_comp);
        HashMap<Point, Node> open_lst_HM = new HashMap<>();
        HashMap<Point, Node> closed_lst = new HashMap<>();

        Node current_node = new Node(start, start, end, null); // The first node's 'previous' is null
        open_lst_PQ.add(current_node);
        open_lst_HM.put(start, current_node);


        while(withinReach.test(current_node.getPt(), end) == false && !open_lst_PQ.isEmpty())
        {
            // Add valid Cardinal Nodes to open list (Must be able to pass through, not be start or end, & not be in closed list)
            List<Point> valid_neighbors = potentialNeighbors.apply(current_node.getPt())
                    .filter(canPassThrough)
                    .filter(pt -> !pt.equals(start)
                            && !pt.equals(end)
                            && !closed_lst.containsKey(pt))
                    .collect(Collectors.toList());

            for (Point current_pt : valid_neighbors)
            {
                Node current_neighbor = new Node(current_pt, start, end, current_node);

                if (!closed_lst.containsKey(current_pt))
                {
                    // If not in open list or closed list, add to open list!
                    open_lst_HM.put(current_neighbor.getPt(), current_neighbor);
                    open_lst_PQ.add(current_neighbor);
                }
                else {
                    // If already in open list, recheck g-value & update accordingly
                    if (current_neighbor.get_start_dist() < open_lst_HM.get(current_pt).get_total_dist())
                    {
                        open_lst_PQ.remove(open_lst_HM.get(current_pt)); // Remove old 'g' value from PQ
                        open_lst_PQ.add(current_neighbor); // Add new 'g' value to PQ
                        open_lst_HM.replace(current_pt, current_neighbor); // Add new 'g' value to HM
                    }
                }
            }

                closed_lst.put(current_node.getPt(), current_node);
                open_lst_HM.remove(current_node.getPt());

                current_node = open_lst_PQ.poll();
        }

        List<Point> path = new LinkedList<>();

        // Exception for when there is no valid path!
        if (open_lst_PQ.isEmpty())
        {
            return path;
        }

        // Now return the path by tracing the last 'current node' to its previous nodes
        Node trace_node = current_node;
        while (trace_node.getPt() != start)
        {
            path.add(0, trace_node.getPt());
            trace_node = trace_node.get_prev_node();
        }

        return path;
    }
}

