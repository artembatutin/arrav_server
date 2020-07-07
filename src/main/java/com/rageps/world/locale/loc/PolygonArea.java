package com.rageps.world.locale.loc;

import com.rageps.world.locale.Position;

public final class PolygonArea extends Area {

    private final Position[] positions;

    public PolygonArea(Position... positions) {
        this.positions = positions;
        if (positions.length < 3) throw new IllegalStateException("A polygon must have at least 3 vertices.");
    }

    @Override
    public boolean inArea(Position position) {
        Position extreme = new Position(Short.MAX_VALUE, position.getY());
        int count = 0, i = 0;
        do {
            int next = (i + 1) % positions.length;

            // Check if the line segment from 'p' to 'extreme' intersects
            // with the line segment from 'polygon[i]' to 'polygon[next]'
            if (intersects(positions[i], positions[next], position, extreme)) {
                // If the point 'p' is colinear with line segment 'i-next',
                // then check if it lies on segment. If it lies, return true,
                // otherwise false
                if (orientation(positions[i], position, positions[next]) == 0)
                    return onSegment(positions[i], position, positions[next]);
                count++;
            }
            i = next;
        } while (i != 0);
        // Return true if count is odd, false otherwise
        return count % 2 == 1;
    }

    // Given three colinear points p, q, r, the function checks if
    // point q lies on line segment 'pr'
    boolean onSegment(Position p, Position q, Position r) {
        return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.max(p.getY(), r.getY());
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    int orientation(Position p, Position q, Position r) {
        int val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) return 0;  // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    // The function that returns true if line segment 'p1q1' and 'p2q2' intersect.
    boolean intersects(Position p1, Position q1, Position p2, Position q2) {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        // p1, q1 and p2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

    @Override
    public boolean isWithin(Position position, int distance) {
        return false;
    }
}
