package filmorate.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre implements Comparable<Genre> {

    private int id;
    private String name;

    @Override
    public int compareTo(Genre o) {
        return Integer.compare(this.id, o.id);
    }
}
