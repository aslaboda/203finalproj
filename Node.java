//import java.awt.*; NOT CORRECT POINT OBJECT???
//import java.awt.Point;

public class Node {
    private Point pt;
    private Integer total_dist;
    private Integer goal_dist;
    private Integer start_dist;
    private Node prev_node;

    public Node(Point pt, Point start, Point goal, Node prev_node) {
        this.pt = pt;
        this.prev_node = prev_node;

        // Determine Start Distance (g)
        if (prev_node == null) {this.start_dist = 0;}
        else {this.start_dist = prev_node.start_dist + 1;}

        this.goal_dist = Math.abs(this.getPt().x - goal.x) + Math.abs(this.getPt().y - goal.y) ;
        this.total_dist = start_dist + goal_dist;
    }

    public Point getPt() {
        return pt;
    }

    public int get_total_dist() {
        return total_dist;
    }

    public Node get_prev_node() {
        return prev_node;
    }

    public int get_start_dist() {
        return start_dist;
    }

    public int get_goal_dist() {
        return goal_dist;
    }

}
